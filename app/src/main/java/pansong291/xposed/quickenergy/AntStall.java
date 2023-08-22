package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntStallRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;

/**
 * @author Constanline
 * @since 2023/08/22
 */
public class AntStall {
    private static final String TAG = AntStall.class.getCanonicalName();


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
                    Log.recordLog("蚂蚁新村⛪请先开启蚂蚁新村");
                    return;
                }
                settle(jo);

                shopList();

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
                        Log.recordLog("蚂蚁新村⛪收取金币" + settleCoin);
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
                        Log.recordLog("蚂蚁新村⛪一键收摊成功");
                    }
            } else {
                Log.recordLog("shopOneKeyClose err:", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "shopOneKeyClose err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void shopOneKeyOpen() {
        String s = AntStallRpcCall.oneKeyOpen();
        try {
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                Log.recordLog("蚂蚁新村⛪一键摆摊成功");
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
                Log.recordLog("蚂蚁新村⛪签到成功");
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
                Log.recordLog("蚂蚁新村⛪获取奖励成功");
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
                Log.recordLog("蚂蚁新村⛪完成任务成功");
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
