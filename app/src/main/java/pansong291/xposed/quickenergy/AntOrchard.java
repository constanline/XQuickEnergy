package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntOrchardRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FileUtils;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AntOrchard {
    private static final String TAG = AntOrchard.class.getCanonicalName();

    private static String userId;
    private static String treeLevel;

    public static void start() {
        if (!Config.antOrchard())
            return;
        new Thread() {
            @Override
            public void run() {
                try {
                    String s = AntOrchardRpcCall.orchardIndex();
                    JSONObject jo = new JSONObject(s);
                    if ("100".equals(jo.getString("resultCode"))) {
                        if (jo.optBoolean("userOpenOrchard")) {
                            JSONObject taobaoData = new JSONObject(jo.getString("taobaoData"));
                            treeLevel = Integer.toString(taobaoData.getJSONObject("gameInfo").getJSONObject("plantInfo")
                                    .getJSONObject("seedStage").getInt("stageLevel"));
                            JSONObject joo = new JSONObject(AntOrchardRpcCall.mowGrassInfo());
                            if ("100".equals(jo.getString("resultCode"))) {
                                userId = joo.getString("userId");
                                if (jo.has("lotteryPlusInfo"))
                                    drawLotteryPlus(jo.getJSONObject("lotteryPlusInfo"));
                                extraInfoGet();
                                if (Config.receiveOrchardTaskAward()) {
                                    doOrchardDailyTask(userId);
                                    triggerTbTask();
                                }
                                if (Config.getOrchardSpreadManureCount() > 0)
                                    orchardSpreadManure();

                                if (Config.getOrchardSpreadManureCount() >= 3
                                        && Config.getOrchardSpreadManureCount() < 10) {
                                    querySubplotsActivity(3);
                                } else if (Config.getOrchardSpreadManureCount() >= 10) {
                                    querySubplotsActivity(10);
                                }

                            } else {
                                Log.recordLog(jo.getString("resultDesc"), jo.toString());
                            }
                        } else {
                            Config.setAntOrchard(false);
                            Log.recordLog("请先开启芭芭农场！");
                        }
                    } else {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                } catch (Throwable t) {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }.start();
    }

    private static String[] wuaList;
    private static String getWua() {
        if (wuaList == null) {
            String content = FileUtils.readFromFile(FileUtils.getWuaFile());
            wuaList = content.split("\n");
        }
        if (wuaList.length > 0) {
            return wuaList[RandomUtils.nextInt(0, wuaList.length - 1)];
        }
        return "null";
    }

    private static void orchardSpreadManure() {
        try {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.orchardIndex());
            if ("100".equals(jo.getString("resultCode"))) {
                JSONObject spreadManureStage = jo.getJSONObject("spreadManureActivity")
                        .getJSONObject("spreadManureStage");
                if ("FINISHED".equals(spreadManureStage.getString("status"))) {
                    String sceneCode = spreadManureStage.getString("sceneCode");
                    String taskType = spreadManureStage.getString("taskType");
                    int awardCount = spreadManureStage.getInt("awardCount");
                    JSONObject joo = new JSONObject(AntOrchardRpcCall.receiveTaskAward(sceneCode, taskType));
                    if (joo.getBoolean("success")) {
                        Log.farm("丰收礼包🎁[肥料*" + awardCount + "]");
                    } else {
                        Log.recordLog(joo.getString("desc"), joo.toString());
                    }
                }
                String taobaoData = jo.getString("taobaoData");
                jo = new JSONObject(taobaoData);
                JSONObject plantInfo = jo.getJSONObject("gameInfo").getJSONObject("plantInfo");
                boolean canExchange = plantInfo.getBoolean("canExchange");
                JSONObject seedStage = plantInfo.getJSONObject("seedStage");
                String stageBefor = seedStage.getString("stageText");
                treeLevel = Integer.toString(seedStage.getInt("stageLevel"));
                JSONObject accountInfo = jo.getJSONObject("gameInfo").getJSONObject("accountInfo");
                int happyPoint = Integer.parseInt(accountInfo.getString("happyPoint"));
                int wateringCost = accountInfo.getInt("wateringCost");
                int wateringLeftTimes = accountInfo.getInt("wateringLeftTimes");
                if (happyPoint > wateringCost && wateringLeftTimes > 0 && !canExchange
                        && (200 - wateringLeftTimes < Config.getOrchardSpreadManureCount())) {
                    jo = new JSONObject(AntOrchardRpcCall.orchardSpreadManure(getWua()));
                    if ("100".equals(jo.getString("resultCode"))) {
                        taobaoData = jo.getString("taobaoData");
                        jo = new JSONObject(taobaoData);
                        String stageAfter = jo.getJSONObject("currentStage").getString("stageText");
                        Log.farm("农场施肥💩[" + stageAfter + "]");
                        orchardSpreadManure();
                    } else {
                        Log.recordLog(jo.getString("resultDesc"), jo.toString());
                    }
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "orchardSpreadManure err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void extraInfoGet() {
        try {
            String s = AntOrchardRpcCall.extraInfoGet();
            JSONObject jo = new JSONObject(s);
            if ("100".equals(jo.getString("resultCode"))) {
                JSONObject fertilizerPacket = jo.getJSONObject("data").getJSONObject("extraData")
                        .getJSONObject("fertilizerPacket");
                if (!"todayFertilizerWaitTake".equals(fertilizerPacket.getString("status")))
                    return;
                int todayFertilizerNum = fertilizerPacket.getInt("todayFertilizerNum");
                jo = new JSONObject(AntOrchardRpcCall.extraInfoSet());
                if ("100".equals(jo.getString("resultCode"))) {
                    Log.farm("每日肥料💩[" + todayFertilizerNum + "g]");
                } else {
                    Log.i(jo.getString("resultDesc"), jo.toString());
                }
            } else {
                Log.i(jo.getString("resultDesc"), jo.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "extraInfoGet err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void drawLotteryPlus(JSONObject lotteryPlusInfo) {
        try {
            if (!lotteryPlusInfo.has("userSevenDaysGiftsItem"))
                return;
            String itemId = lotteryPlusInfo.getString("itemId");
            JSONObject jo = lotteryPlusInfo.getJSONObject("userSevenDaysGiftsItem");
            JSONArray ja = jo.getJSONArray("userEverydayGiftItems");
            int index = -1;
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (jo.getString("itemId").equals(itemId)) {
                    if (!jo.getBoolean("received")) {
                        String singleDesc = jo.optString("singleDesc");
                        int awardCount = jo.optInt("awardCount", 1);
                        jo = new JSONObject(AntOrchardRpcCall.drawLottery());
                        if ("100".equals(jo.getString("resultCode"))) {
                            Log.farm("七日礼包🎁[" + (singleDesc != null ? singleDesc + "随机礼包" : "肥料") + "*" + awardCount
                                    + "]");
                        } else {
                            Log.i(jo.getString("resultDesc"), jo.toString());
                        }
                    } else {
                        Log.recordLog("七日礼包已领取", "");
                    }
                    break;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "drawLotteryPlus err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void doOrchardDailyTask(String userId) {
        try {
            String s = AntOrchardRpcCall.orchardListTask();
            JSONObject jo = new JSONObject(s);
            if ("100".equals(jo.getString("resultCode"))) {
                JSONObject signTaskInfo = jo.getJSONObject("signTaskInfo");
                orchardSign(signTaskInfo);
                JSONArray jaTaskList = jo.getJSONArray("taskList");
                for (int i = 0; i < jaTaskList.length(); i++) {
                    jo = jaTaskList.getJSONObject(i);
                    if (!"TODO".equals(jo.getString("taskStatus")))
                        continue;
                    String title = jo.getJSONObject("taskDisplayConfig").getString("title");
                    if ("TRIGGER".equals(jo.getString("actionType"))) {
                        String taskId = jo.getString("taskId");
                        String sceneCode = jo.getString("sceneCode");
                        jo = new JSONObject(AntOrchardRpcCall.finishTask(userId, sceneCode, taskId));
                        if (jo.getBoolean("success")) {
                            Log.farm("农场任务🧾[" + title + "]");
                        } else {
                            Log.recordLog(jo.getString("desc"), jo.toString());
                        }
                    }
                }
            } else {
                Log.recordLog(jo.getString("resultCode"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "doOrchardDailyTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void orchardSign(JSONObject signTaskInfo) {
        try {
            JSONObject currentSignItem = signTaskInfo.getJSONObject("currentSignItem");
            if (!currentSignItem.getBoolean("signed")) {
                int awardCount = currentSignItem.getInt("awardCount");
                JSONObject joSign = new JSONObject(AntOrchardRpcCall.orchardSign());
                if ("100".equals(joSign.getString("resultCode"))) {
                    Log.farm("农场签到📅[" + awardCount + "]g肥料");
                } else {
                    Log.i(joSign.getString("resultDesc"), joSign.toString());
                }
            } else {
                Log.recordLog("农场今日已签到", "");
            }
        } catch (Throwable t) {
            Log.i(TAG, "orchardSign err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void triggerTbTask() {
        try {
            String s = AntOrchardRpcCall.orchardListTask();
            JSONObject jo = new JSONObject(s);
            if ("100".equals(jo.getString("resultCode"))) {
                JSONArray jaTaskList = jo.getJSONArray("taskList");
                for (int i = 0; i < jaTaskList.length(); i++) {
                    jo = jaTaskList.getJSONObject(i);
                    if (!"FINISHED".equals(jo.getString("taskStatus")))
                        continue;
                    String title = jo.getJSONObject("taskDisplayConfig").getString("title");
                    int awardCount = jo.optInt("awardCount", 0);
                    String taskId = jo.getString("taskId");
                    String taskPlantType = jo.getString("taskPlantType");
                    jo = new JSONObject(AntOrchardRpcCall.triggerTbTask(taskId, taskPlantType));
                    if ("100".equals(jo.getString("resultCode"))) {
                        Log.farm("领取奖励🎖️[" + title + "]#" + awardCount + "g肥料");
                    } else {
                        Log.recordLog(jo.getString("resultDesc"), jo.toString());
                    }
                }
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "triggerTbTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void querySubplotsActivity(int taskRequire) {
        try {
            String s = AntOrchardRpcCall.querySubplotsActivity(treeLevel);
            JSONObject jo = new JSONObject(s);
            if ("100".equals(jo.getString("resultCode"))) {
                JSONArray subplotsActivityList = jo.getJSONArray("subplotsActivityList");
                for (int i = 0; i < subplotsActivityList.length(); i++) {
                    jo = subplotsActivityList.getJSONObject(i);
                    if (!"WISH".equals(jo.getString("activityType")))
                        continue;
                    String activityId = jo.getString("activityId");
                    if ("NOT_STARTED".equals(jo.getString("status"))) {
                        String extend = jo.getString("extend");
                        jo = new JSONObject(extend);
                        JSONArray wishActivityOptionList = jo.getJSONArray("wishActivityOptionList");
                        String optionKey = null;
                        for (int j = 0; j < wishActivityOptionList.length(); j++) {
                            jo = wishActivityOptionList.getJSONObject(j);
                            if (taskRequire == jo.getInt("taskRequire")) {
                                optionKey = jo.getString("optionKey");
                                break;
                            }
                        }
                        if (optionKey != null) {
                            jo = new JSONObject(
                                    AntOrchardRpcCall.triggerSubplotsActivity(activityId, "WISH", optionKey));
                            if ("100".equals(jo.getString("resultCode"))) {
                                Log.farm("农场许愿✨[每日施肥" + taskRequire + "次]");
                            } else {
                                Log.recordLog(jo.getString("resultDesc"), jo.toString());
                            }
                        }
                    } else if ("FINISHED".equals(jo.getString("status"))) {
                        jo = new JSONObject(AntOrchardRpcCall.receiveOrchardRights(activityId, "WISH"));
                        if ("100".equals(jo.getString("resultCode"))) {
                            Log.farm("许愿奖励✨[肥料" + jo.getInt("amount") + "g]");
                            querySubplotsActivity(taskRequire);
                        } else {
                            Log.recordLog(jo.getString("resultDesc"), jo.toString());
                        }
                    }
                }
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "triggerTbTask err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
