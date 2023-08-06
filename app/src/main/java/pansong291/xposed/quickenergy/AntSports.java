package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntSportsRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;

public class AntSports {
    private static final String TAG = AntSports.class.getCanonicalName();

    public static void start(ClassLoader loader, int times) {
        new Thread() {
            ClassLoader loader;
            int times;

            public Thread setData(ClassLoader cl, int i) {
                loader = cl;
                times = i;
                return this;
            }

            @Override
            public void run() {
                try {
                    while (FriendIdMap.currentUid == null || FriendIdMap.currentUid.isEmpty())
                        Thread.sleep(100);
                    if (Config.openTreasureBox())
                        queryMyHomePage(loader);

                    if (Config.receiveCoinAsset())
                        receiveCoinAsset();

                    if (Config.donateCharityCoin())
                        queryProjectList(loader);

                    if (Config.minExchangeCount() > 0 && Statistics.canExchangeToday(FriendIdMap.currentUid)
                            && times == 0)
                        queryWalkStep(loader);
                } catch (Throwable t) {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }.setData(loader, times).start();
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
            if (jo.getString("resultCode").equals("SUCCESS")) {
                s = jo.getString("pathJoinStatus");
                if (s.equals("GOING")) {
                    FriendIdMap.currentUid = jo.getJSONObject("myPositionModel").getString("userId");
                    if (jo.has("pathCompleteStatus")) {
                        if (jo.getString("pathCompleteStatus").equals("COMPLETED")) {
                            jo = new JSONObject(AntSportsRpcCall.queryBaseList());
                            if (jo.getString("resultCode").equals("SUCCESS")) {
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
                } else if (s.equals("NOT_JOIN")) {
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
                if (jo.getString("resultCode").equals("SUCCESS")) {
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
            if (jo.getString("resultCode").equals("SUCCESS")) {
                Log.other("行走线路🚶🏻‍♂️[" + title + "]#前进了" + jo.getInt("goStepCount") + "步");
                boolean completed = jo.getString("completeStatus").equals("COMPLETED");
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
            if (jo.getString("resultCode").equals("SUCCESS")) {
                JSONArray ja = jo.getJSONArray("treasureBoxAwards");
                int num = 0;
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    num += jo.getInt("num");
                   Log.other("运动宝箱🎁[" + num + jo.getString("name") + "]");
                }
                return num;
            } else if (jo.getString("resultCode").equals("TREASUREBOX_NOT_EXIST")) {
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
            if (jo.getString("resultCode").equals("SUCCESS")) {
                int charityCoinCount = jo.getInt("charityCoinCount");
                if (charityCoinCount < 10)
                    return;
                jo = jo.getJSONObject("projectPage");
                JSONArray ja = jo.getJSONArray("data");
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i).getJSONObject("basicModel");
                    if (jo.getString("footballFieldStatus").equals("OPENING_DONATE")) {
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
            if (jo.getString("resultCode").equals("SUCCESS")) {
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
            if (jo.getString("resultCode").equals("SUCCESS")) {
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
                        Statistics.exchangeToday(FriendIdMap.currentUid);
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
                        Statistics.exchangeToday(FriendIdMap.currentUid);

                    } else if (s.contains("已捐步")) {
                        Statistics.exchangeToday(FriendIdMap.currentUid);
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
}
