package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntStallRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;

import java.util.*;

/**
 * @author Constanline
 * @since 2023/08/22
 */
public class AntStall {
    private static final String TAG = AntStall.class.getCanonicalName();


    private static class Seat {
        public String userId;
        public int hot;

        public Seat(String userId, int hot) {
            this.userId = userId;
            this.hot = hot;
        }
    }

    public static void start() {
        if (!Config.enableStall()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                home();
            }
        }.start();
    }
    private static void home() {
        String s = AntStallRpcCall.home();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                if (!jo.getBoolean("hasRegister") || jo.getBoolean("hasQuit")) {
                    Log.farm("蚂蚁新村⛪请先开启蚂蚁新村");
                    return;
                }
                settle(jo);

//                shopList();

                if (Config.stallAutoClose()) {
                    closeShop();
                }

                if (Config.stallAutoOpen()) {
                    openShop();
                }

                taskList();

            } else {
                Log.recordLog("home err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "home err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void settle(JSONObject stallHome) {
        try {
            JSONObject seatsMap = stallHome.getJSONObject("seatsMap");
            JSONObject seat = seatsMap.getJSONObject("MASTER");
            if (seat.has("coinsMap")) {
                JSONObject coinsMap = seat.getJSONObject("coinsMap");
                JSONObject master = coinsMap.getJSONObject("MASTER");
                String assetId = master.getString("assetId");
                int settleCoin = (int)(master.getJSONObject("money").getDouble("amount"));
                if (settleCoin > 1) {
                    String s = AntStallRpcCall.settle(assetId, settleCoin);
                    JSONObject jo = new JSONObject(s);
                    if (jo.getString("resultCode").equals("SUCCESS")) {
                        Log.farm("蚂蚁新村⛪收取金币" + settleCoin);
                    } else {
                        Log.recordLog("settle err:", s);
                    }
                }
            }

        } catch (Throwable t) {
            Log.i(TAG, "settle err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void closeShop() {
        String s = AntStallRpcCall.shopList();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                JSONArray astUserShopList = jo.getJSONArray("astUserShopList");
                for (int i = 0; i < astUserShopList.length(); i++) {
                    JSONObject shop = astUserShopList.getJSONObject(i);
                    if ("OPEN".equals(shop.getString("status"))) {
                        JSONObject rentLastEnv = shop.getJSONObject("rentLastEnv");
                        long gmtLastRent = rentLastEnv.getLong("gmtLastRent");
                        if (System.currentTimeMillis() - gmtLastRent > (long) Config.stallSelfOpenTime() * 60 * 1000) {
                            String shopId = shop.getString("shopId");
                            String rentLastBill = shop.getString("rentLastBill");
                            String rentLastUser = shop.getString("rentLastUser");
                            shopClose(shopId, rentLastBill, rentLastUser);
                        }
                    }
                }
            } else {
                Log.recordLog("closeShop err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "closeShop err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void openShop() {
        String s = AntStallRpcCall.shopList();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                JSONArray astUserShopList = jo.getJSONArray("astUserShopList");
                Queue<String> shopIds = new LinkedList<>();
                for (int i = 0; i < astUserShopList.length(); i++) {
                    JSONObject astUserShop = astUserShopList.getJSONObject(i);
                    if ("FREE".equals(astUserShop.getString("status"))) {
                        shopIds.add(astUserShop.getString("shopId"));
                    }
                }
                rankCoinDonate(shopIds);
            } else {
                Log.recordLog("closeShop err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "closeShop err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void rankCoinDonate(Queue<String> shopIds) {
        String s = AntStallRpcCall.rankCoinDonate();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                JSONArray friendRankList = jo.getJSONArray("friendRankList");
                List<Seat> seats = new ArrayList<>();
                for (int i = 0; i < friendRankList.length(); i++) {
                    JSONObject friendRank = friendRankList.getJSONObject(i);
                    if (friendRank.getBoolean("canOpenShop")) {
                        String userId = friendRank.getString("userId");
                        if (Config.stallOpenType()) {
                            if (!Config.stallOpenList().contains(userId)) {
                                continue;
                            }
                        } else if (Config.stallOpenList().contains(userId)) {
                            continue;
                        }
                        int hot = friendRank.getInt("hot");
                        seats.add(new Seat(userId, hot));
                    }
                }
                friendHomeOpen(seats, shopIds);
            } else {
                Log.recordLog("rankCoinDonate err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "rankCoinDonate err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void openShop(String seatId, String userId, Queue<String> shopIds) {
        String shopId = shopIds.peek();
        String s = AntStallRpcCall.shopOpen(seatId, userId, shopId);
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                shopIds.poll();
            }
        } catch (Throwable t) {
            Log.i(TAG, "openShop err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void friendHomeOpen(List<Seat> seats, Queue<String> shopIds) {
        Collections.sort(seats, (e1, e2) -> e2.hot - e1.hot);
        int idx = 0;
        while (seats.size() > idx && !shopIds.isEmpty()) {
            Seat seat = seats.get(idx);
            String userId = seat.userId;
            String s = AntStallRpcCall.friendHome(userId);
            try {
                JSONObject jo = new JSONObject(s);
                if (jo.getString("resultCode").equals("SUCCESS")) {
                    JSONObject seatsMap = jo.getJSONObject("seatsMap");
                    JSONObject guest = seatsMap.getJSONObject("GUEST_01");
                    if (guest.getBoolean("canOpenShop")) {
                        openShop(guest.getString("seatId"), userId, shopIds);
                    } else {
                        guest = seatsMap.getJSONObject("GUEST_02");
                        if (guest.getBoolean("canOpenShop")) {
                            openShop(guest.getString("seatId"), userId, shopIds);
                        }
                    }
                } else {
                    Log.recordLog("friendHomeOpen err:", s);
                }
            } catch (Throwable t) {
                Log.i(TAG, "friendHomeOpen err:");
                Log.printStackTrace(TAG, t);
            }
            idx++;
        }
    }

    private static void shopList() {
        String s = AntStallRpcCall.shopList();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                JSONArray astUserShopList = jo.getJSONArray("astUserShopList");
                int openShop = 0;
                for (int i = 0; i < astUserShopList.length(); i++) {
                    JSONObject shop = astUserShopList.getJSONObject(i);
                    if ("OPEN".equals(shop.getString("status"))) {
                        openShop++;
                    }
                }
                if (Config.stallAutoClose() && openShop > 0) {
                    shopOneKeyClose();
                    openShop = 0;
                }
                if (Config.stallAutoOpen() && openShop < 4) {
                    shopOneKeyOpen();
                }
            } else {
                Log.recordLog("shopList err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "shopList err:");
            Log.printStackTrace(TAG, t);
        }

    }

    private static void shopOneKeyClose() {
        String s = AntStallRpcCall.preOneKeyClose();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                s = AntStallRpcCall.oneKeyClose();
                    jo = new JSONObject(s);
                    if (jo.getString("resultCode").equals("SUCCESS")) {
                        Log.farm("蚂蚁新村⛪一键收摊成功");
                    }
            } else {
                Log.recordLog("shopOneKeyClose err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "shopOneKeyClose err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void shopClose(String shopId, String billNo, String userId) {
        String s = AntStallRpcCall.preShopClose(shopId, billNo);
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                JSONObject income = jo.getJSONObject("astPreviewShopSettleVO").getJSONObject("income");
                s = AntStallRpcCall.shopClose(shopId);
                jo = new JSONObject(s);
                if (jo.getString("resultCode").equals("SUCCESS")) {
                    Log.farm("蚂蚁新村⛪收取在[" + FriendIdMap.getNameById(userId) + "]的摊位获得" + income.getString("amount"));
                } else {
                    Log.recordLog("shopClose err:", s);
                }
            } else {
                Log.recordLog("shopClose err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "shopClose err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void shopOneKeyOpen() {
        String s = AntStallRpcCall.oneKeyOpen();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                Log.farm("蚂蚁新村⛪一键摆摊成功");
            } else {
                Log.recordLog("shopOneKeyOpen err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "shopOneKeyOpen err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void taskList() {
        String s = AntStallRpcCall.taskList();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                JSONObject signListModel = jo.getJSONObject("signListModel");
                if (!signListModel.getBoolean("currentKeySigned")) {
                    signToday();
                }

                JSONArray taskModels = jo.getJSONArray("taskModels");
                for (int i = 0; i < taskModels.length(); i++) {
                    JSONObject task = taskModels.getJSONObject(i);
                    String taskStatus = task.getString("taskStatus");
                    if ("FINISHED".equals(taskStatus)) {
                        receiveTaskAward(task.getString("taskType"));
                    } else if ("TODO".equals(taskStatus)) {
                        String taskType = task.getString("taskType");
                        if (taskType.startsWith("ANTSTALL_TASK_mulanxiaowu")) {
                            if (finishTask(taskType)) {
                                taskList();
                                return;
                            }
                        } else if ("ANTSTALL_NORMAL_DAILY_QA".equals(taskType)) {
                            String bizInfo = task.getString("bizInfo");
                            if (ReadingDada.answerQuestion(new JSONObject(bizInfo))) {
                                receiveTaskAward(taskType);
                            }
                        }
                    }
                }
            } else {
                Log.recordLog("taskList err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "taskList err:");
            Log.printStackTrace(TAG, t);
        }

    }

    private static void signToday() {
        String s = AntStallRpcCall.signToday();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                Log.farm("蚂蚁新村⛪签到成功");
            } else {
                Log.recordLog("signToday err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "signToday err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveTaskAward(String taskType) {
        if (!Config.stallReceiveAward()) {
            return;
        }
        String s = AntStallRpcCall.receiveTaskAward(taskType);
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                Log.farm("蚂蚁新村⛪获取奖励成功");
            } else {
                Log.recordLog("receiveTaskAward err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static boolean finishTask(String taskType) {
        String s = AntStallRpcCall.finishTask(FriendIdMap.currentUid + "_" + taskType, taskType);
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                Log.farm("蚂蚁新村⛪完成任务成功");
                return true;
            } else {
                Log.recordLog("receiveTaskAward err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }
}
