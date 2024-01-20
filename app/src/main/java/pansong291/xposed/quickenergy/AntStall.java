package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntStallRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;

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
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                if (!jo.getBoolean("hasRegister") || jo.getBoolean("hasQuit")) {
                    Log.farm("蚂蚁新村⛪请先开启蚂蚁新村");
                    return;
                }

                JSONObject astReceivableCoinVO = jo.getJSONObject("astReceivableCoinVO");
                if (astReceivableCoinVO.optBoolean("hasCoin")) {
                    settleReceivable();
                }

                if (Config.stallThrowManure()) {
                    throwManure();
                }

                JSONObject seatsMap = jo.getJSONObject("seatsMap");
                settle(seatsMap);

                collectManure();

                sendBack(seatsMap);

                if (Config.stallAutoClose()) {
                    closeShop();
                }

                if (Config.stallAutoOpen()) {
                    openShop();
                }

                taskList();
                achieveBeShareP2P();

                if (Config.stallDonate()) {
                    roadmap();
                }

            } else {
                Log.recordLog("home err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "home err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void sendBack(String billNo, String seatId, String shopId, String shopUserId) {
        String s = AntStallRpcCall.shopSendBackPre(billNo, seatId, shopId, shopUserId);
        try {
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONObject astPreviewShopSettleVO = jo.getJSONObject("astPreviewShopSettleVO");
                JSONObject income = astPreviewShopSettleVO.getJSONObject("income");
                int amount = (int) income.getDouble("amount");
                s = AntStallRpcCall.shopSendBack(seatId);
                jo = new JSONObject(s);
                if ("SUCCESS".equals(jo.getString("resultCode"))) {
                    Log.farm("蚂蚁新村⛪请走[" + FriendIdMap.getNameById(shopUserId) + "]的小摊" + (amount > 0 ? "获得金币" + amount : ""));

                    inviteOpen(seatId);
                } else {
                    Log.recordLog("sendBack err:", s);
                }
            } else {
                Log.recordLog("sendBackPre err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "sendBack err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void inviteOpen(String seatId) {
        String s = AntStallRpcCall.rankInviteOpen();
        try {
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray friendRankList = jo.getJSONArray("friendRankList");
                for (int i = 0; i < friendRankList.length(); i++) {
                    JSONObject friend = friendRankList.getJSONObject(i);
                    String friendUserId = friend.getString("userId");
                    if (!Config.stallInviteShopList().contains(friendUserId)) {
                        continue;
                    }
                    if (friend.getBoolean("canInviteOpenShop")) {
                        s = AntStallRpcCall.oneKeyInviteOpenShop(friendUserId, seatId);
                        jo = new JSONObject(s);
                        if ("SUCCESS".equals(jo.getString("resultCode"))) {
                            Log.farm("蚂蚁新村⛪邀请[" + FriendIdMap.getNameById(friendUserId) + "]开店成功");
                            return;
                        }
                    }
                }
            } else {
                Log.recordLog("inviteOpen err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "inviteOpen err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void sendBack(JSONObject seatsMap) {
        try {
            for (int i = 1; i <= 2; i++) {
                JSONObject seat = seatsMap.getJSONObject("GUEST_0" + i);
                String seatId = seat.getString("seatId");
                if ("FREE".equals(seat.getString("status"))) {
                    inviteOpen(seatId);
                    continue;
                }
                String rentLastUser = seat.getString("rentLastUser");
                //白名单直接跳过
                if (Config.stallWhiteList().contains(rentLastUser)) {
                    continue;
                }
                String rentLastBill = seat.getString("rentLastBill");
                String rentLastShop = seat.getString("rentLastShop");
                //黑名单直接赶走
                if (Config.stallBlackList().contains(rentLastUser)) {
                    sendBack(rentLastBill, seatId, rentLastShop, rentLastUser);
                    continue;
                }
                long bizStartTime = seat.getLong("bizStartTime");
                if ((System.currentTimeMillis() - bizStartTime) / 1000 / 60 > Config.stallAllowOpenTime()) {
                    sendBack(rentLastBill, seatId, rentLastShop, rentLastUser);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "sendBack err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void settle(JSONObject seatsMap) {
        try {
            JSONObject seat = seatsMap.getJSONObject("MASTER");
            if (seat.has("coinsMap")) {
                JSONObject coinsMap = seat.getJSONObject("coinsMap");
                JSONObject master = coinsMap.getJSONObject("MASTER");
                String assetId = master.getString("assetId");
                int settleCoin = (int) (master.getJSONObject("money").getDouble("amount"));
                if (settleCoin > 100) {
                    String s = AntStallRpcCall.settle(assetId, settleCoin);
                    JSONObject jo = new JSONObject(s);
                    if (jo.getString("resultCode").equals("SUCCESS")) {
                        Log.farm("蚂蚁新村⛪[收取金币]#" + settleCoin);
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
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
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
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
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
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
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
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                Log.farm("蚂蚁新村⛪在[" + FriendIdMap.getNameById(userId) + "]家摆摊");
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
                if ("SUCCESS".equals(jo.getString("resultCode"))) {
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

    private static void shopClose(String shopId, String billNo, String userId) {
        String s = AntStallRpcCall.preShopClose(shopId, billNo);
        try {
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONObject income = jo.getJSONObject("astPreviewShopSettleVO").getJSONObject("income");
                s = AntStallRpcCall.shopClose(shopId);
                jo = new JSONObject(s);
                if ("SUCCESS".equals(jo.getString("resultCode"))) {
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

    private static void taskList() {
        String s = AntStallRpcCall.taskList();
        try {
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
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
                        JSONObject bizInfo = new JSONObject(task.getString("bizInfo"));
                        String taskType = task.getString("taskType");
                        String title = bizInfo.optString("title", taskType);
                        if ("VISIT_AUTO_FINISH".equals(bizInfo.getString("actionType"))
                                || "ANTSTALL_NORMAL_OPEN_NOTICE".equals(taskType)
                                || "tianjiashouye".equals(taskType)
                                || "SHANGYEHUA_ceshi".equals(taskType)) {
                            if (finishTask(taskType)) {
                                Log.farm("蚂蚁新村⛪[完成任务]#" + title);
                                taskList();
                                return;
                            }
                        } else if ("ANTSTALL_NORMAL_DAILY_QA".equals(taskType)) {
                            if (ReadingDada.answerQuestion(bizInfo)) {
                                receiveTaskAward(taskType);
                            }
                        } else if ("ANTSTALL_NORMAL_INVITE_REGISTER".equals(taskType)) {
                            if (inviteRegister()) {
                                taskList();
                                return;
                            }
                        } else if ("ANTSTALL_P2P_DAILY_SHARER".equals(taskType)) {
                            shareP2P();
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
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                Log.farm("蚂蚁新村⛪[签到成功]");
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
            if (jo.getBoolean("success")) {
                Log.farm("蚂蚁新村⛪[领取奖励]");
            } else {
                Log.recordLog("receiveTaskAward err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static boolean finishTask(String taskType) {
        String s = AntStallRpcCall.finishTask(FriendIdMap.getCurrentUid() + "_" + taskType, taskType);
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                return true;
            } else {
                Log.recordLog("finishTask err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "finishTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static boolean inviteRegister() {
        if (!Config.stallInviteRegister()) {
            return false;
        }
        try {
            String s = AntStallRpcCall.rankInviteRegister();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray friendRankList = jo.optJSONArray("friendRankList");
                if (friendRankList != null && friendRankList.length() > 0) {
                    for (int i = 0; i < friendRankList.length(); i++) {
                        JSONObject friend = friendRankList.getJSONObject(i);
                        if (friend.optBoolean("canInviteRegister", false)
                                && "UNREGISTER".equals(friend.getString("userStatus"))) {/* 是否加名单筛选 */
                            String userId = friend.getString("userId");
                            jo = new JSONObject(AntStallRpcCall.friendInviteRegister(userId));
                            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                                Log.farm("邀请好友[" + FriendIdMap.getNameById(userId) + "]#开通新村");
                                return true;
                            } else {
                                Log.recordLog("friendInviteRegister err:", jo.toString());
                            }
                        }
                    }
                }
            } else {
                Log.recordLog("rankInviteRegister err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "InviteRegister err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void shareP2P() {
        try {
            String s = AntStallRpcCall.shareP2P();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                String shareId = jo.getString("shareId");
                /* 保存shareId到Statistics */
                Statistics.stallShareIdToday(FriendIdMap.getCurrentUid(), shareId);
                Log.recordLog("蚂蚁新村⛪[分享助力]");
            } else {
                Log.recordLog("shareP2P err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "shareP2P err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void achieveBeShareP2P() {
        try {
            if (!Statistics.canStallHelpToday(FriendIdMap.getCurrentUid()))
                return;
            List<String> UserIdList = Statistics.stallP2PUserIdList(FriendIdMap.getCurrentUid());
            for (String uid : UserIdList) {
                if (Statistics.canStallBeHelpToday(uid)) {
                    String shareId = Statistics.getStallShareId(uid);
                    if (shareId != null && Statistics.canStallP2PHelpToday(uid)) {
                        String s = AntStallRpcCall.achieveBeShareP2P(shareId);
                        JSONObject jo = new JSONObject(s);
                        if (jo.getBoolean("success")) {
                            Log.farm("新村助力🎈[" + FriendIdMap.getNameById(uid) + "]");
                            Statistics.stallHelpToday(FriendIdMap.getCurrentUid(), false);
                            Statistics.stallBeHelpToday(uid, false);
                            Statistics.stallP2PHelpeToday(uid);
                        } else if ("600000028".equals(jo.getString("code"))) {
                            Statistics.stallBeHelpToday(uid, true);
                            Log.recordLog("被助力次数上限:", uid);
                        } else if ("600000027".equals(jo.getString("code"))) {
                            Statistics.stallHelpToday(FriendIdMap.getCurrentUid(), true);
                            Log.recordLog("助力他人次数上限:", FriendIdMap.getCurrentUid());
                        } else {
                            Log.recordLog("achieveBeShareP2P err:", s);
                        }
                        Thread.sleep(3500L);
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "achieveBeShareP2P err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void donate() {
        String s = AntStallRpcCall.projectList();
        try {
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray astProjectVOS = jo.getJSONArray("astProjectVOS");
                for (int i = 0; i < astProjectVOS.length(); i++) {
                    JSONObject project = astProjectVOS.getJSONObject(i);
                    if ("ONLINE".equals(project.getString("status"))) {
                        String projectId = project.getString("projectId");
                        s = AntStallRpcCall.projectDetail(projectId);
                        JSONObject joProjectDetail = new JSONObject(s);
                        if ("SUCCESS".equals(joProjectDetail.getString("resultCode"))) {
                            s = AntStallRpcCall.projectDonate(projectId);
                            JSONObject joProjectDonate = new JSONObject(s);
                            if ("SUCCESS".equals(joProjectDonate.getString("resultCode"))) {
                                JSONObject astUserVillageVO = joProjectDonate.getJSONObject("astUserVillageVO");
                                if (astUserVillageVO.getInt("donateCount") >= astUserVillageVO.getInt("donateLimit")) {
                                    roadmap();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "donate err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void roadmap() {
        String s = AntStallRpcCall.roadmap();
        try {
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONObject userInfo = jo.getJSONObject("userInfo");
                JSONObject currentCoin = userInfo.getJSONObject("currentCoin");
                int amount = (int) currentCoin.getDouble("amount");
                if (amount < 15000) {
                    return;
                }
                JSONArray roadList = jo.getJSONArray("roadList");
                boolean unFinished = false;
                boolean canNext = false;
                for (int i = 0; i < roadList.length(); i++) {
                    JSONObject road = roadList.getJSONObject(i);
                    if ("FINISHED".equals(road.getString("status"))) {
                        continue;
                    }
                    if ("LOCK".equals(road.getString("status"))) {
                        canNext = true;
                        break;
                    }
                    if (road.getInt("donateCount") < road.getInt("donateLimit")) {
                        unFinished = true;
                    }
                }
                if (unFinished) {
                    donate();
                } else if (canNext) {
                    s = AntStallRpcCall.nextVillage();
                    jo = new JSONObject(s);
                    if ("SUCCESS".equals(jo.getString("resultCode"))) {
                        Log.farm("蚂蚁新村⛪进入下一村成功");
                    }
                }
            } else {
                Log.recordLog("roadmap err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "roadmap err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void collectManure() {
        String s = AntStallRpcCall.queryManureInfo();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject astManureInfoVO = jo.getJSONObject("astManureInfoVO");
                if (astManureInfoVO.optBoolean("hasManure")) {
                    int manure = astManureInfoVO.getInt("manure");
                    s = AntStallRpcCall.collectManure();
                    jo = new JSONObject(s);
                    if ("SUCCESS".equals(jo.getString("resultCode"))) {
                        Log.farm("蚂蚁新村⛪获得肥料" + manure + "g");
                    }
                }
            } else {
                Log.recordLog("collectManure err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectManure err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void throwManure(JSONArray dynamicList) {
        String s = AntStallRpcCall.throwManure(dynamicList);
        try {
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                Log.farm("蚂蚁新村⛪扔肥料");
            }
        } catch (Throwable th) {
            Log.i(TAG, "throwManure err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void throwManure() {
        String s = AntStallRpcCall.dynamicLoss();
        try {
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray astLossDynamicVOS = jo.getJSONArray("astLossDynamicVOS");
                JSONArray dynamicList = new JSONArray();
                for (int i = 0; i < astLossDynamicVOS.length(); i++) {
                    JSONObject lossDynamic = astLossDynamicVOS.getJSONObject(i);
                    if (lossDynamic.has("specialEmojiVO")) {
                        continue;
                    }
                    JSONObject dynamic = new JSONObject();
                    dynamic.put("bizId", lossDynamic.getString("bizId"));
                    dynamic.put("bizType", lossDynamic.getString("bizType"));
                    dynamicList.put(dynamic);
                    if (dynamicList.length() == 5) {
                        throwManure(dynamicList);
                        dynamicList = new JSONArray();
                    }
                }
                if (dynamicList.length() > 0) {
                    throwManure(dynamicList);
                }
            } else {
                Log.recordLog("throwManure err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "throwManure err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void settleReceivable() {
        String s = AntStallRpcCall.settleReceivable();
        try {
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                Log.farm("蚂蚁新村⛪收取应收金币");
            }
        } catch (Throwable th) {
            Log.i(TAG, "settleReceivable err:");
            Log.printStackTrace(TAG, th);
        }
    }
}