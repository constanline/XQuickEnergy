package pansong291.xposed.quickenergy;

import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntSportsRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;

public class AntSports {
    private static final String TAG = AntSports.class.getCanonicalName();

    private static final HashSet<String> waitOpenBoxNos = new HashSet<>();

    public static void start(ClassLoader loader) {
        new Thread() {
            ClassLoader loader;

            public Thread setData(ClassLoader cl) {
                loader = cl;
                return this;
            }

            @Override
            public void run() {
                try {
                    FriendIdMap.waitingCurrentUid();
                    if (Config.openTreasureBox())
                        queryMyHomePage(loader);

                    if (Config.receiveCoinAsset())
                        receiveCoinAsset();

                    if (Config.donateCharityCoin())
                        queryProjectList(loader);

                    if (Config.minExchangeCount() > 0 && Statistics.canExchangeToday(FriendIdMap.getCurrentUid()))
                        queryWalkStep(loader);

                    if (Config.tiyubiz()) {
                        userTaskGroupQuery("SPORTS_DAILY_SIGN_GROUP");
                        userTaskGroupQuery("SPORTS_DAILY_GROUP");
                        userTaskRightsReceive();
                        pathFeatureQuery();
                        participate();
                    }
                } catch (Throwable t) {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }.setData(loader).start();
    }

    private static void receiveCoinAsset() {
        try {
            String s = AntSportsRpcCall.queryCoinBubbleModule();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject data = jo.getJSONObject("data");
                if (!data.has("receiveCoinBubbleList"))
                    return;
                JSONArray ja = data.getJSONArray("receiveCoinBubbleList");
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    String assetId = jo.getString("assetId");
                    int coinAmount = jo.getInt("coinAmount");
                    jo = new JSONObject(AntSportsRpcCall.receiveCoinAsset(assetId, coinAmount));
                    if (jo.getBoolean("success")) {
                        Log.other("收集金币💰[" + coinAmount + "个]");
                    } else {
                        Log.recordLog("首页收集金币", jo.toString());
                    }
                }
            } else {
                Log.i(TAG, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveCoinAsset err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void queryMyHomePage(ClassLoader loader) {
        try {
            String s = AntSportsRpcCall.queryMyHomePage();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                s = jo.getString("pathJoinStatus");
                if ("GOING".equals(s)) {
                    if (jo.has("pathCompleteStatus")) {
                        if ("COMPLETED".equals(jo.getString("pathCompleteStatus"))) {
                            jo = new JSONObject(AntSportsRpcCall.queryBaseList());
                            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                                JSONArray allPathBaseInfoList = jo.getJSONArray("allPathBaseInfoList");
                                JSONArray otherAllPathBaseInfoList = jo.getJSONArray("otherAllPathBaseInfoList")
                                        .getJSONObject(0)
                                        .getJSONArray("allPathBaseInfoList");
                                join(loader, allPathBaseInfoList, otherAllPathBaseInfoList, "");
                            } else {
                                Log.i(TAG, jo.getString("resultDesc"));
                            }
                        }
                    } else {
                        String rankCacheKey = jo.getString("rankCacheKey");
                        JSONArray ja = jo.getJSONArray("treasureBoxModelList");
                        for (int i = 0; i < ja.length(); i++) {
                            parseTreasureBoxModel(loader, ja.getJSONObject(i), rankCacheKey);
                        }
                        JSONObject joPathRender = jo.getJSONObject("pathRenderModel");
                        String title = joPathRender.getString("title");
                        int minGoStepCount = joPathRender.getInt("minGoStepCount");
                        jo = jo.getJSONObject("dailyStepModel");
                        int consumeQuantity = jo.getInt("consumeQuantity");
                        int produceQuantity = jo.getInt("produceQuantity");
                        String day = jo.getString("day");
                        int canMoveStepCount = produceQuantity - consumeQuantity;
                        if (canMoveStepCount >= minGoStepCount) {
                            go(loader, day, rankCacheKey, canMoveStepCount, title);
                        }
                    }
                } else if ("NOT_JOIN".equals(s)) {
                    String firstJoinPathTitle = jo.getString("firstJoinPathTitle");
                    JSONArray allPathBaseInfoList = jo.getJSONArray("allPathBaseInfoList");
                    JSONArray otherAllPathBaseInfoList = jo.getJSONArray("otherAllPathBaseInfoList").getJSONObject(0)
                            .getJSONArray("allPathBaseInfoList");
                    join(loader, allPathBaseInfoList, otherAllPathBaseInfoList, firstJoinPathTitle);
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryMyHomePage err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void join(ClassLoader loader, JSONArray allPathBaseInfoList, JSONArray otherAllPathBaseInfoList,
            String firstJoinPathTitle) {
        try {
            int index = -1;
            String title = null;
            String pathId = null;
            JSONObject jo = new JSONObject();
            for (int i = allPathBaseInfoList.length() - 1; i >= 0; i--) {
                jo = allPathBaseInfoList.getJSONObject(i);
                if (jo.getBoolean("unlocked")) {
                    title = jo.getString("title");
                    pathId = jo.getString("pathId");
                    index = i;
                    break;
                }
            }
            if (index < 0 || index == allPathBaseInfoList.length() - 1) {
                for (int j = otherAllPathBaseInfoList.length() - 1; j >= 0; j--) {
                    jo = otherAllPathBaseInfoList.getJSONObject(j);
                    if (jo.getBoolean("unlocked")) {
                        if (j != otherAllPathBaseInfoList.length() - 1 || index != allPathBaseInfoList.length() - 1) {
                            title = jo.getString("title");
                            pathId = jo.getString("pathId");
                            index = j;
                        }
                        break;
                    }
                }
            }
            if (index >= 0) {
                String s;
                if (title.equals(firstJoinPathTitle)) {
                    s = AntSportsRpcCall.openAndJoinFirst();
                } else {
                    s = AntSportsRpcCall.join(pathId);
                }
                jo = new JSONObject(s);
                if ("SUCCESS".equals(jo.getString("resultCode"))) {
                    Log.other("加入线路🚶🏻‍♂️[" + title + "]");
                    queryMyHomePage(loader);
                } else {
                    Log.i(TAG, jo.getString("resultDesc"));
                }
            } else {
                Log.recordLog("好像没有可走的线路了！", "");
            }
        } catch (Throwable t) {
            Log.i(TAG, "join err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void go(ClassLoader loader, String day, String rankCacheKey, int stepCount, String title) {
        try {
            String s = AntSportsRpcCall.go(day, rankCacheKey, stepCount);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                Log.other("行走线路🚶🏻‍♂️[" + title + "]#前进了" + jo.getInt("goStepCount") + "步");
                boolean completed = "COMPLETED".equals(jo.getString("completeStatus"));
                JSONArray ja = jo.getJSONArray("allTreasureBoxModelList");
                for (int i = 0; i < ja.length(); i++) {
                    parseTreasureBoxModel(loader, ja.getJSONObject(i), rankCacheKey);
                }
                if (completed) {
                    Log.other("完成线路🚶🏻‍♂️[" + title + "]");
                    queryMyHomePage(loader);
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "go err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void parseTreasureBoxModel(ClassLoader loader, JSONObject jo, String rankCacheKey) {
        try {
            String canOpenTime = jo.getString("canOpenTime");
            String issueTime = jo.getString("issueTime");
            String boxNo = jo.getString("boxNo");
            String userId = jo.getString("userId");
            if (canOpenTime.equals(issueTime)) {
                openTreasureBox(loader, boxNo, userId);
            } else {
                long cot = Long.parseLong(canOpenTime);
                long now = Long.parseLong(rankCacheKey);
                long delay = cot - now;
                Log.recordLog("还有 " + delay + "ms 才能开宝箱", "");
                if (delay < Config.checkInterval()) {
                    if (waitOpenBoxNos.contains(boxNo)) {
                        return;
                    }
                    waitOpenBoxNos.add(boxNo);
                    new Thread() {
                        long delay;
                        ClassLoader loader;
                        String boxNo;
                        String userId;

                        public Thread setData(long l, ClassLoader cl, String bN, String uid) {
                            delay = l - 1000;
                            loader = cl;
                            boxNo = bN;
                            userId = uid;
                            return this;
                        }

                        @Override
                        public void run() {
                            try {
                                if (delay > 0)
                                    sleep(delay);
                                Log.recordLog("蹲点开箱开始", "");
                                long startTime = System.currentTimeMillis();
                                while (System.currentTimeMillis() - startTime < 5_000) {
                                    if (openTreasureBox(loader, boxNo, userId) > 0)
                                        break;
                                    sleep(200);
                                }
                            } catch (Throwable t) {
                                Log.i(TAG, "parseTreasureBoxModel.run err:");
                                Log.printStackTrace(TAG, t);
                            }
                        }

                    }.setData(delay, loader, boxNo, userId).start();
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "parseTreasureBoxModel err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static int openTreasureBox(ClassLoader loader, String boxNo, String userId) {
        try {
            String s = AntSportsRpcCall.openTreasureBox(boxNo, userId);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                waitOpenBoxNos.remove(boxNo);
                JSONArray ja = jo.getJSONArray("treasureBoxAwards");
                int num = 0;
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    num += jo.getInt("num");
                    Log.other("运动宝箱🎁[" + num + jo.getString("name") + "]");
                }
                return num;
            } else if ("TREASUREBOX_NOT_EXIST".equals(jo.getString("resultCode"))) {
                Log.recordLog(jo.getString("resultDesc"), "");
                return 1;
            } else {
                Log.recordLog(jo.getString("resultDesc"), "");
            }
        } catch (Throwable t) {
            Log.i(TAG, "openTreasureBox err:");
            Log.printStackTrace(TAG, t);
        }
        return 0;
    }

    private static void queryProjectList(ClassLoader loader) {
        try {
            String s = AntSportsRpcCall.queryProjectList(0);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                int charityCoinCount = jo.getInt("charityCoinCount");
                if (charityCoinCount < 10)
                    return;
                jo = jo.getJSONObject("projectPage");
                JSONArray ja = jo.getJSONArray("data");
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i).getJSONObject("basicModel");
                    if ("OPENING_DONATE".equals(jo.getString("footballFieldStatus"))) {
                        donate(loader, charityCoinCount / 10 * 10, jo.getString("projectId"), jo.getString("title"));
                        break;
                    }
                }
            } else {
                Log.recordLog(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryProjectList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void donate(ClassLoader loader, int donateCharityCoin, String projectId, String title) {
        try {
            String s = AntSportsRpcCall.donate(donateCharityCoin, projectId);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                Log.other("捐赠活动❤️[" + title + "][" + donateCharityCoin + "运动币]");
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "donate err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void queryWalkStep(ClassLoader loader) {
        try {
            String s = AntSportsRpcCall.queryWalkStep();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                jo = jo.getJSONObject("dailyStepModel");
                int produceQuantity = jo.getInt("produceQuantity");
                int hour = Integer.parseInt(Log.getFormatTime().split(":")[0]);
                if (produceQuantity >= Config.minExchangeCount() || hour >= Config.latestExchangeTime()) {
                    s = AntSportsRpcCall.walkDonateSignInfo(produceQuantity);
                    s = AntSportsRpcCall.donateWalkHome(produceQuantity);
                    jo = new JSONObject(s);
                    if (!jo.getBoolean("isSuccess"))
                        return;
                    JSONObject walkDonateHomeModel = jo.getJSONObject("walkDonateHomeModel");
                    JSONObject walkUserInfoModel = walkDonateHomeModel.getJSONObject("walkUserInfoModel");
                    if (!walkUserInfoModel.has("exchangeFlag")) {
                        Statistics.exchangeToday(FriendIdMap.getCurrentUid());
                        return;
                    }

                    String donateToken = walkDonateHomeModel.getString("donateToken");
                    JSONObject walkCharityActivityModel = walkDonateHomeModel.getJSONObject("walkCharityActivityModel");
                    String activityId = walkCharityActivityModel.getString("activityId");

                    s = AntSportsRpcCall.exchange(activityId, produceQuantity, donateToken);
                    jo = new JSONObject(s);
                    if (jo.getBoolean("isSuccess")) {
                        JSONObject donateExchangeResultModel = jo.getJSONObject("donateExchangeResultModel");
                        int userCount = donateExchangeResultModel.getInt("userCount");
                        double amount = donateExchangeResultModel.getJSONObject("userAmount").getDouble("amount");
                        Log.other("捐出活动❤️[" + userCount + "步]#兑换" + amount + "元公益金");
                        Statistics.exchangeToday(FriendIdMap.getCurrentUid());

                    } else if (s.contains("已捐步")) {
                        Statistics.exchangeToday(FriendIdMap.getCurrentUid());
                    } else {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryWalkStep err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /* 文体中心 */// SPORTS_DAILY_SIGN_GROUP SPORTS_DAILY_GROUP
    private static void userTaskGroupQuery(String groupId) {
        try {
            String s = AntSportsRpcCall.userTaskGroupQuery(groupId);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                jo = jo.getJSONObject("group");
                JSONArray userTaskList = jo.getJSONArray("userTaskList");
                for (int i = 0; i < userTaskList.length(); i++) {
                    jo = userTaskList.getJSONObject(i);
                    if (!"TODO".equals(jo.getString("status")))
                        continue;
                    JSONObject taskInfo = jo.getJSONObject("taskInfo");
                    String bizType = taskInfo.getString("bizType");
                    String taskId = taskInfo.getString("taskId");
                    jo = new JSONObject(AntSportsRpcCall.userTaskComplete(bizType, taskId));
                    if (jo.getBoolean("success")) {
                        String taskName = taskInfo.optString("taskName", taskId);
                        Log.other("完成任务🧾[" + taskName + "]");
                    } else {
                        Log.recordLog("文体每日任务", jo.toString());
                    }
                }
            } else {
                Log.recordLog("文体每日任务", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "userTaskGroupQuery err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void participate() {
        try {
            String s = AntSportsRpcCall.queryAccount();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                double balance = jo.getDouble("balance");
                if (balance < 100)
                    return;
                jo = new JSONObject(AntSportsRpcCall.queryRoundList());
                if (jo.getBoolean("success")) {
                    JSONArray dataList = jo.getJSONArray("dataList");
                    for (int i = 0; i < dataList.length(); i++) {
                        jo = dataList.getJSONObject(i);
                        if (!"P".equals(jo.getString("status")))
                            continue;
                        if (jo.has("userRecord"))
                            continue;
                        JSONArray instanceList = jo.getJSONArray("instanceList");
                        int pointOptions = 0;
                        String roundId = jo.getString("id");
                        String InstanceId = null;
                        String ResultId = null;
                        for (int j = instanceList.length() - 1; j >= 0; j--) {
                            jo = instanceList.getJSONObject(j);
                            if (jo.getInt("pointOptions") < pointOptions)
                                continue;
                            pointOptions = jo.getInt("pointOptions");
                            InstanceId = jo.getString("id");
                            ResultId = jo.getString("instanceResultId");
                        }
                        jo = new JSONObject(AntSportsRpcCall.participate(pointOptions, InstanceId, ResultId, roundId));
                        if (jo.getBoolean("success")) {
                            jo = jo.getJSONObject("data");
                            String roundDescription = jo.getString("roundDescription");
                            int targetStepCount = jo.getInt("targetStepCount");
                            Log.other("走路挑战🚶🏻‍♂️[" + roundDescription + "]#" + targetStepCount);
                        } else {
                            Log.recordLog("走路挑战赛", jo.toString());
                        }
                    }
                } else {
                    Log.recordLog("queryRoundList", jo.toString());
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "participate err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void userTaskRightsReceive() {
        try {
            String s = AntSportsRpcCall.userTaskGroupQuery("SPORTS_DAILY_GROUP");
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                jo = jo.getJSONObject("group");
                JSONArray userTaskList = jo.getJSONArray("userTaskList");
                for (int i = 0; i < userTaskList.length(); i++) {
                    jo = userTaskList.getJSONObject(i);
                    if (!"COMPLETED".equals(jo.getString("status")))
                        continue;
                    String userTaskId = jo.getString("userTaskId");
                    JSONObject taskInfo = jo.getJSONObject("taskInfo");
                    String taskId = taskInfo.getString("taskId");
                    jo = new JSONObject(AntSportsRpcCall.userTaskRightsReceive(taskId, userTaskId));
                    if (jo.getBoolean("success")) {
                        String taskName = taskInfo.optString("taskName", taskId);
                        JSONArray rightsRuleList = taskInfo.getJSONArray("rightsRuleList");
                        StringBuilder award = new StringBuilder();
                        for (int j = 0; j < rightsRuleList.length(); j++) {
                            jo = rightsRuleList.getJSONObject(j);
                            award.append(jo.getString("rightsName")).append("*").append(jo.getInt("baseAwardCount"));
                        }
                        Log.other("领取奖励🎖️[" + taskName + "]#" + award);
                    } else {
                        Log.recordLog("文体中心领取奖励", jo.toString());
                    }
                }
            } else {
                Log.recordLog("文体中心领取奖励", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "userTaskRightsReceive err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void pathFeatureQuery() {
        try {
            String s = AntSportsRpcCall.pathFeatureQuery();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject path = jo.getJSONObject("path");
                String pathId = path.getString("pathId");
                String title = path.getString("title");
                int minGoStepCount = path.getInt("minGoStepCount");
                if (jo.has("userPath")) {
                    JSONObject userPath = jo.getJSONObject("userPath");
                    FriendIdMap.setCurrentUid(userPath.getString("userId"));
                    String userPathRecordStatus = userPath.getString("userPathRecordStatus");
                    if ("COMPLETED".equals(userPathRecordStatus)) {
                        pathMapHomepage(pathId);
                        pathMapJoin(title, pathId);
                    } else if ("GOING".equals(userPathRecordStatus)) {
                        pathMapHomepage(pathId);
                        String countDate = Log.getFormatDate();
                        jo = new JSONObject(AntSportsRpcCall.stepQuery(countDate, pathId));
                        if (jo.getBoolean("success")) {
                            int canGoStepCount = jo.getInt("canGoStepCount");
                            if (canGoStepCount >= minGoStepCount) {
                                String userPathRecordId = userPath.getString("userPathRecordId");
                                tiyubizGo(countDate, title, canGoStepCount, pathId, userPathRecordId);
                            }
                        }
                    }
                } else {
                    pathMapJoin(title, pathId);
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "pathFeatureQuery err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void pathMapHomepage(String pathId) {
        try {
            String s = AntSportsRpcCall.pathMapHomepage(pathId);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                if (!jo.has("userPathGoRewardList"))
                    return;
                JSONArray userPathGoRewardList = jo.getJSONArray("userPathGoRewardList");
                for (int i = 0; i < userPathGoRewardList.length(); i++) {
                    jo = userPathGoRewardList.getJSONObject(i);
                    if (!"UNRECEIVED".equals(jo.getString("status")))
                        continue;
                    String userPathRewardId = jo.getString("userPathRewardId");
                    jo = new JSONObject(AntSportsRpcCall.rewardReceive(pathId, userPathRewardId));
                    if (jo.getBoolean("success")) {
                        jo = jo.getJSONObject("userPathRewardDetail");
                        JSONArray rightsRuleList = jo.getJSONArray("userPathRewardRightsList");
                        StringBuilder award = new StringBuilder();
                        for (int j = 0; j < rightsRuleList.length(); j++) {
                            jo = rightsRuleList.getJSONObject(j).getJSONObject("rightsContent");
                            award.append(jo.getString("name")).append("*").append(jo.getInt("count"));
                        }
                        Log.other("文体宝箱🎁[" + award + "]");
                    } else {
                        Log.recordLog("文体中心开宝箱", jo.toString());
                    }
                }
            } else {
                Log.recordLog("文体中心开宝箱", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "pathMapHomepage err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void pathMapJoin(String title, String pathId) {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.pathMapJoin(pathId));
            if (jo.getBoolean("success")) {
                Log.other("加入线路🚶🏻‍♂️[" + title + "]");
                pathFeatureQuery();
            } else {
                Log.i(TAG, jo.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "pathMapJoin err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void tiyubizGo(String countDate, String title, int goStepCount, String pathId,
            String userPathRecordId) {
        try {
            String s = AntSportsRpcCall.tiyubizGo(countDate, goStepCount, pathId, userPathRecordId);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                jo = jo.getJSONObject("userPath");
                Log.other("行走线路🚶🏻‍♂️[" + title + "]#前进了" + jo.getInt("userPathRecordForwardStepCount") + "步");
                pathMapHomepage(pathId);
                boolean completed = "COMPLETED".equals(jo.getString("userPathRecordStatus"));
                if (completed) {
                    Log.other("完成线路🚶🏻‍♂️[" + title + "]");
                    pathFeatureQuery();
                }
            } else {
                Log.i(TAG, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "tiyubizGo err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
