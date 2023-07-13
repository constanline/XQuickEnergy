package pansong291.xposed.quickenergy.util;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.AntFarm.SendType;

import java.util.ArrayList;
import java.util.List;

public class Config
{
    public enum RecallAnimalType
    {
        ALWAYS, WHEN_THIEF, WHEN_HUNGRY, NEVER;
        public static final CharSequence[] names =
                {ALWAYS.name(), WHEN_THIEF.name(), WHEN_HUNGRY.name(), NEVER.name()};
    }

    private static final String TAG = Config.class.getCanonicalName();
    public static final String
            /* application */
            jn_immediateEffect = "immediateEffect", jn_recordLog = "recordLog", jn_showToast = "showToast",
            jn_stayAwake = "stayAwake", jn_autoRestart = "autoRestart", jn_xedgeproData = "xedgeproData",
    /* forest */
    jn_collectEnergy = "collectEnergy", jn_ReturnWater30 = "returnWater30", jn_ReturnWater20 = "returnWater20",
            jn_ReturnWater10 = "returnWater10", jn_helpFriendCollect = "helpFriendCollect", jn_dontCollectList = "dontCollectList",
            jn_dontHelpCollectList = "dontHelpCollectList", jn_checkInterval = "checkInterval", jn_threadCount = "threadCount",
            jn_advanceTime = "advanceTime", jn_collectInterval = "collectInterval", jn_collectTimeout = "collectTimeout",
            jn_receiveForestTaskAward = "receiveForestTaskAward", jn_waterFriendList = "waterFriendList",
            jn_cooperateWater = "cooperateWater", jn_cooperateWaterList = "cooperateWaterList",
    /* farm */
    jn_enableFarm = "enableFarm",
            jn_rewardFriend = "rewardFriend", jn_sendBackAnimal = "sendBackAnimal", jn_sendType = "sendType",
            jn_dontSendFriendList = "dontSendFriendList", jn_recallAnimalType = "recallAnimalType", jn_receiveFarmToolReward = "receiveFarmToolReward",
            jn_useNewEggTool = "useNewEggTool", jn_harvestProduce = "harvestProduce", jn_donation = "donation",
            jn_answerQuestion = "answerQuestion", jn_receiveFarmTaskAward = "receiveFarmTaskAward", jn_feedAnimal = "feedAnimal",
            jn_useAccelerateTool = "useAccelerateTool", jn_feedFriendAnimalList = "feedFriendAnimalList", jn_notifyFriend = "notifyFriend",
            jn_dontNotifyFriendList = "dontNotifyFriendList",
    /* other */
    jn_receivePoint = "receivePoint", jn_openTreasureBox = "openTreasureBox", jn_donateCharityCoin = "donateCharityCoin",
            jn_minExchangeCount = "minExchangeCount", jn_latestExchangeTime = "latestExchangeTime", jn_kbSignIn = "kbSignIn";

    public static boolean shouldReload;
    public static boolean hasChanged;

    /* application */
    private boolean immediateEffect;
    private boolean recordLog;
    private boolean showToast;
    private boolean stayAwake;
    private boolean autoRestart;
    private String xedgeproData;

    /* forest */
    private boolean collectEnergy;
    private int checkInterval;
    private int threadCount;
    private int advanceTime;
    private int collectInterval;
    private int collectTimeout;
    private int returnWater30;
    private int returnWater20;
    private int returnWater10;
    private boolean helpFriendCollect;
    private List<String> dontCollectList;
    private List<String> dontHelpCollectList;
    private boolean receiveForestTaskAward;
    private List<String> waterFriendList;
    private List<Integer> waterCountList;
    private boolean cooperateWater;
    private List<String> cooperateWaterList;
    private List<Integer> cooperateWaterNumList;

    /* farm */
    private boolean enableFarm;
    private boolean rewardFriend;
    private boolean sendBackAnimal;
    private SendType sendType;
    private List<String> dontSendFriendList;
    private RecallAnimalType recallAnimalType;
    private boolean receiveFarmToolReward;
    private boolean useNewEggTool;
    private boolean harvestProduce;
    private boolean donation;
    private boolean answerQuestion;
    private boolean receiveFarmTaskAward;
    private boolean feedAnimal;
    private boolean useAccelerateTool;
    private List<String> feedFriendAnimalList;
    private List<Integer> feedFriendCountList;
    private boolean notifyFriend;
    private List<String> dontNotifyFriendList;

    /* other */
    private boolean receivePoint;
    private boolean openTreasureBox;
    private boolean donateCharityCoin;
    private int minExchangeCount;
    private int latestExchangeTime;
    private boolean kbSignIn;

    /* base */
    private static Config config;

    /* application */
    public static void setImmediateEffect(boolean b)
    {
        getConfig().immediateEffect = b;
        hasChanged = true;
    }

    public static boolean immediateEffect()
    {
        return getConfig().immediateEffect;
    }

    public static void setRecordLog(boolean b)
    {
        getConfig().recordLog = b;
        hasChanged = true;
    }

    public static boolean recordLog()
    {
        return getConfig().recordLog;
    }

    public static void setShowToast(boolean b)
    {
        getConfig().showToast = b;
        hasChanged = true;
    }

    public static boolean showToast()
    {
        return getConfig().showToast;
    }

    public static void setStayAwake(boolean b)
    {
        getConfig().stayAwake = b;
        hasChanged = true;
    }

    public static boolean stayAwake()
    {
        return getConfig().stayAwake;
    }

    public static void setAutoRestart(boolean b)
    {
        getConfig().autoRestart = b;
        hasChanged = true;
    }

    public static boolean autoRestart()
    {
        return getConfig().autoRestart;
    }

    public static void setXedgeproData(String s)
    {
        getConfig().xedgeproData = s;
        hasChanged = true;
    }

    public static String xedgeproData()
    {
        return getConfig().xedgeproData;
    }

    /* forest */
    public static void setCollectEnergy(boolean b)
    {
        getConfig().collectEnergy = b;
        hasChanged = true;
    }

    public static boolean collectEnergy()
    {
        return getConfig().collectEnergy;
    }

    public static void setCheckInterval(int i)
    {
        if (i < 600000) {
            i = 600000;
        }
        getConfig().checkInterval = i;
        hasChanged = true;
    }

    public static int checkInterval()
    {
        return getConfig().checkInterval;
    }

    public static void setThreadCount(int i)
    {
        getConfig().threadCount = i;
        hasChanged = true;
    }

    public static int threadCount()
    {
        return getConfig().threadCount;
    }

    public static void setAdvanceTime(int i)
    {
        getConfig().advanceTime = i;
        hasChanged = true;
    }

    public static int advanceTime()
    {
        return getConfig().advanceTime;
    }

    public static void setCollectInterval(int i)
    {
        getConfig().collectInterval = i;
        hasChanged = true;
    }

    public static int collectInterval()
    {
        return getConfig().collectInterval;
    }

    public static void setCollectTimeout(int i)
    {
        getConfig().collectTimeout = i;
        hasChanged = true;
    }

    public static int collectTimeout()
    {
        return getConfig().collectTimeout;
    }

    public static void setReturnWater30(int i)
    {
        getConfig().returnWater30 = i;
        hasChanged = true;
    }

    public static int returnWater30()
    {
        return getConfig().returnWater30;
    }

    public static void setReturnWater20(int i)
    {
        getConfig().returnWater20 = i;
        hasChanged = true;
    }

    public static int returnWater20()
    {
        return getConfig().returnWater20;
    }

    public static void setReturnWater10(int i)
    {
        getConfig().returnWater10 = i;
        hasChanged = true;
    }

    public static int returnWater10()
    {
        return getConfig().returnWater10;
    }

    public static void setHelpFriendCollect(boolean b)
    {
        getConfig().helpFriendCollect = b;
        hasChanged = true;
    }

    public static boolean helpFriendCollect()
    {
        return getConfig().helpFriendCollect;
    }

    public static List<String> getDontCollectList()
    {
        return getConfig().dontCollectList;
    }

    public static List<String> getDontHelpCollectList()
    {
        return getConfig().dontHelpCollectList;
    }

    public static void setReceiveForestTaskAward(boolean b)
    {
        getConfig().receiveForestTaskAward = b;
        hasChanged = true;
    }

    public static boolean receiveForestTaskAward()
    {
        return getConfig().receiveForestTaskAward;
    }

    public static List<String> getWaterFriendList()
    {
        return getConfig().waterFriendList;
    }

    public static List<Integer> getWaterCountList()
    {
        return getConfig().waterCountList;
    }

    public static void setCooperateWater(boolean b)
    {
        getConfig().cooperateWater = b;
        hasChanged = true;
    }

    public static boolean cooperateWater()
    {
        return getConfig().cooperateWater;
    }

    public static List<String> getCooperateWaterList()
    {
        return getConfig().cooperateWaterList;
    }

    public static List<Integer> getcooperateWaterNumList()
    {
        return getConfig().cooperateWaterNumList;
    }

    /* farm */
    public static void setEnableFarm(boolean b)
    {
        getConfig().enableFarm = b;
        hasChanged = true;
    }

    public static boolean enableFarm()
    {
        return getConfig().enableFarm;
    }

    public static void setRewardFriend(boolean b)
    {
        getConfig().rewardFriend = b;
        hasChanged = true;
    }

    public static boolean rewardFriend()
    {
        return getConfig().rewardFriend;
    }

    public static void setSendBackAnimal(boolean b)
    {
        getConfig().sendBackAnimal = b;
        hasChanged = true;
    }

    public static boolean sendBackAnimal()
    {
        return getConfig().sendBackAnimal;
    }

    public static void setSendType(int i)
    {
        getConfig().sendType = SendType.values()[i];
        hasChanged = true;
    }

    public static SendType sendType()
    {
        return getConfig().sendType;
    }

    public static List<String> getDontSendFriendList()
    {
        return getConfig().dontSendFriendList;
    }

    public static void setRecallAnimalType(int i)
    {
        getConfig().recallAnimalType = RecallAnimalType.values()[i];
        hasChanged = true;
    }

    public static RecallAnimalType recallAnimalType()
    {
        return getConfig().recallAnimalType;
    }

    public static void setReceiveFarmToolReward(boolean b)
    {
        getConfig().receiveFarmToolReward = b;
        hasChanged = true;
    }

    public static boolean receiveFarmToolReward()
    {
        return getConfig().receiveFarmToolReward;
    }

    public static void setUseNewEggTool(boolean b)
    {
        getConfig().useNewEggTool = b;
        hasChanged = true;
    }

    public static boolean useNewEggTool()
    {
        return getConfig().useNewEggTool;
    }

    public static void setHarvestProduce(boolean b)
    {
        getConfig().harvestProduce = b;
        hasChanged = true;
    }

    public static boolean harvestProduce()
    {
        return getConfig().harvestProduce;
    }

    public static void setDonation(boolean b)
    {
        getConfig().donation = b;
        hasChanged = true;
    }

    public static boolean donation()
    {
        return getConfig().donation;
    }

    public static void setAnswerQuestion(boolean b)
    {
        getConfig().answerQuestion = b;
        hasChanged = true;
    }

    public static boolean answerQuestion()
    {
        return getConfig().answerQuestion;
    }

    public static void setReceiveFarmTaskAward(boolean b)
    {
        getConfig().receiveFarmTaskAward = b;
        hasChanged = true;
    }

    public static boolean receiveFarmTaskAward()
    {
        return getConfig().receiveFarmTaskAward;
    }

    public static void setFeedAnimal(boolean b)
    {
        getConfig().feedAnimal = b;
        hasChanged = true;
    }

    public static boolean feedAnimal()
    {
        return getConfig().feedAnimal;
    }

    public static void setUseAccelerateTool(boolean b)
    {
        getConfig().useAccelerateTool = b;
        hasChanged = true;
    }

    public static boolean useAccelerateTool()
    {
        return getConfig().useAccelerateTool;
    }

    public static List<String> getFeedFriendAnimalList()
    {
        return getConfig().feedFriendAnimalList;
    }

    public static List<Integer> getFeedFriendCountList()
    {
        return getConfig().feedFriendCountList;
    }

    public static void setNotifyFriend(boolean b)
    {
        getConfig().notifyFriend = b;
        hasChanged = true;
    }

    public static boolean notifyFriend()
    {
        return getConfig().notifyFriend;
    }

    public static List<String> getDontNotifyFriendList()
    {
        return getConfig().dontNotifyFriendList;
    }

    /* other */
    public static void setReceivePoint(boolean b)
    {
        getConfig().receivePoint = b;
        hasChanged = true;
    }

    public static boolean receivePoint()
    {
        return getConfig().receivePoint;
    }

    public static void setOpenTreasureBox(boolean b)
    {
        getConfig().openTreasureBox = b;
        hasChanged = true;
    }

    public static boolean openTreasureBox()
    {
        return getConfig().openTreasureBox;
    }

    public static void setDonateCharityCoin(boolean b)
    {
        getConfig().donateCharityCoin = b;
        hasChanged = true;
    }

    public static boolean donateCharityCoin()
    {
        return getConfig().donateCharityCoin;
    }

    public static void setMinExchangeCount(int i)
    {
        getConfig().minExchangeCount = i;
        hasChanged = true;
    }

    public static int minExchangeCount()
    {
        return getConfig().minExchangeCount;
    }

    public static void setLatestExchangeTime(int i)
    {
        getConfig().latestExchangeTime = i;
        hasChanged = true;
    }

    public static int latestExchangeTime()
    {
        return getConfig().latestExchangeTime;
    }

    public static void setKbSginIn(boolean b)
    {
        getConfig().kbSignIn = b;
        hasChanged = true;
    }

    public static boolean kbSginIn()
    {
        return getConfig().kbSignIn;
    }

    /* base */
    private static Config getConfig()
    {
        if(config == null || shouldReload && config.immediateEffect)
        {
            shouldReload = false;
            String confJson = null;
            if(FileUtils.getConfigFile().exists())
                confJson = FileUtils.readFromFile(FileUtils.getConfigFile());
            config = json2Config(confJson);
        }
        return config;
    }

    public static Config defInit()
    {
        Config c = new Config();

        c.immediateEffect = true;
        c.recordLog = true;
        c.showToast = true;
        c.stayAwake = true;
        c.autoRestart = true;
        c.xedgeproData = "";

        c.collectEnergy = true;
        c.checkInterval = 720_000;
        c.threadCount = 1;
        c.advanceTime = 500;
        c.collectInterval = 100;
        c.collectTimeout = 2_000;
        c.returnWater30 = 0;
        c.returnWater20 = 0;
        c.returnWater10 = 0;
        c.helpFriendCollect = true;
        if(c.dontCollectList == null) c.dontCollectList = new ArrayList<>();
        if(c.dontHelpCollectList == null) c.dontHelpCollectList = new ArrayList<>();
        c.receiveForestTaskAward = true;
        if(c.waterFriendList == null) c.waterFriendList = new ArrayList<>();
        if(c.waterCountList == null) c.waterCountList = new ArrayList<>();
        c.cooperateWater = true;
        if(c.cooperateWaterList == null) c.cooperateWaterList = new ArrayList<>();
        if(c.cooperateWaterNumList == null) c.cooperateWaterNumList = new ArrayList<>();

        c.enableFarm = true;
        c.rewardFriend = true;
        c.sendBackAnimal = true;
        c.sendType = SendType.HIT;
        if(c.dontSendFriendList == null) c.dontSendFriendList = new ArrayList<>();
        c.recallAnimalType = RecallAnimalType.ALWAYS;
        c.receiveFarmToolReward = true;
        c.useNewEggTool = true;
        c.harvestProduce = true;
        c.donation = true;
        c.answerQuestion = true;
        c.receiveFarmTaskAward = true;
        c.feedAnimal = true;
        c.useAccelerateTool = true;
        if(c.feedFriendAnimalList == null) c.feedFriendAnimalList = new ArrayList<>();
        if(c.feedFriendCountList == null) c.feedFriendCountList = new ArrayList<>();
        c.notifyFriend = true;
        if(c.dontNotifyFriendList == null) c.dontNotifyFriendList = new ArrayList<>();

        c.receivePoint = true;
        c.openTreasureBox = true;
        c.donateCharityCoin = true;
        c.kbSignIn = true;
        return c;
    }

    public static boolean saveConfigFile()
    {
        return FileUtils.write2File(config2Json(config), FileUtils.getConfigFile());
    }

    public static Config json2Config(String json)
    {
        Config config = null;
        try
        {
            JSONObject jo = new JSONObject(json);
            JSONArray ja = null, jaa = null;
            config = new Config();

            config.immediateEffect = jo.optBoolean(jn_immediateEffect, true);
            Log.i(TAG, jn_immediateEffect + ":" + config.immediateEffect);

            config.recordLog = jo.optBoolean(jn_recordLog, true);
            Log.i(TAG, jn_recordLog + ":" + config.recordLog);

            config.showToast = jo.optBoolean(jn_showToast, true);
            Log.i(TAG, jn_showToast + ":" + config.showToast);

            config.stayAwake = jo.optBoolean(jn_stayAwake, true);
            Log.i(TAG, jn_stayAwake + ":" + config.stayAwake);

            config.autoRestart = jo.optBoolean(jn_autoRestart, true);
            Log.i(TAG, jn_autoRestart + ":" + config.autoRestart);

            config.xedgeproData = jo.optString(jn_xedgeproData, "");
            Log.i(TAG, jn_xedgeproData + ":" + config.xedgeproData);

            /* forest */
            config.collectEnergy = jo.optBoolean(jn_collectEnergy, true);
            Log.i(TAG, jn_collectEnergy + ":" + config.collectEnergy);

            config.checkInterval = jo.optInt(jn_checkInterval, 720_000);
            Log.i(TAG, jn_checkInterval + ":" + config.checkInterval);

            config.threadCount = jo.optInt(jn_threadCount, 1);
            Log.i(TAG, jn_threadCount + ":" + config.threadCount);

            config.advanceTime = jo.optInt(jn_advanceTime, 500);
            Log.i(TAG, jn_advanceTime + ":" + config.advanceTime);

            config.collectInterval = jo.optInt(jn_collectInterval, 100);
            Log.i(TAG, jn_collectInterval + ":" + config.collectInterval);

            config.collectTimeout = jo.optInt(jn_collectTimeout, 2_000);
            Log.i(TAG, jn_collectTimeout + ":" + config.collectTimeout);

            config.returnWater30 = jo.optInt(jn_ReturnWater30);
            Log.i(TAG, jn_ReturnWater30 + ":" + config.returnWater30);

            config.returnWater20 = jo.optInt(jn_ReturnWater20);
            Log.i(TAG, jn_ReturnWater20 + ":" + config.returnWater20);

            config.returnWater10 = jo.optInt(jn_ReturnWater10);
            Log.i(TAG, jn_ReturnWater10 + ":" + config.returnWater10);

            config.helpFriendCollect = jo.optBoolean(jn_helpFriendCollect, true);
            Log.i(TAG, jn_helpFriendCollect + ":" + config.helpFriendCollect);

            config.dontCollectList = new ArrayList<>();
            Log.i(TAG, jn_dontCollectList + ":[");
            if(jo.has(jn_dontCollectList))
            {
                ja = jo.getJSONArray(jn_dontCollectList);
                for(int i = 0; i < ja.length(); i++)
                {
                    config.dontCollectList.add(ja.getString(i));
                    Log.i(TAG, "  " + config.dontCollectList.get(i) + ",");
                }
            }

            config.dontHelpCollectList = new ArrayList<>();
            Log.i(TAG, jn_dontHelpCollectList + ":[");
            if(jo.has(jn_dontHelpCollectList))
            {
                ja = jo.getJSONArray(jn_dontHelpCollectList);
                for(int i = 0; i < ja.length(); i++)
                {
                    config.dontHelpCollectList.add(ja.getString(i));
                    Log.i(TAG, "  " + config.dontHelpCollectList.get(i) + ",");
                }
            }

            config.receiveForestTaskAward = jo.optBoolean(jn_receiveForestTaskAward, true);
            Log.i(TAG, jn_receiveForestTaskAward + ":" + config.receiveForestTaskAward);

            config.waterFriendList = new ArrayList<>();
            config.waterCountList = new ArrayList<>();
            Log.i(TAG, jn_waterFriendList + ":[");
            if(jo.has(jn_waterFriendList))
            {
                ja = jo.getJSONArray(jn_waterFriendList);
                for(int i = 0; i < ja.length(); i++)
                {
                    if(ja.get(i) instanceof JSONArray)
                    {
                        jaa = ja.getJSONArray(i);
                        config.waterFriendList.add(jaa.getString(0));
                        config.waterCountList.add(jaa.getInt(1));
                    }else
                    {
                        config.waterFriendList.add(ja.getString(i));
                        config.waterCountList.add(3);
                    }
                    Log.i(TAG, "  "
                            + config.waterFriendList.get(i) + ","
                            + config.waterCountList.get(i) + ",");
                }
            }

            config.cooperateWater = jo.optBoolean(jn_cooperateWater, true);

            config.cooperateWaterList = new ArrayList<>();
            config.cooperateWaterNumList = new ArrayList<>();
            Log.i(TAG, jn_cooperateWaterList + ":[");
            if(jo.has(jn_cooperateWaterList))
            {
                ja = jo.getJSONArray(jn_cooperateWaterList);
                for(int i = 0; i < ja.length(); i++)
                {
                    jaa = ja.getJSONArray(i);
                    config.cooperateWaterList.add(jaa.getString(0));
                    config.cooperateWaterNumList.add(jaa.getInt(1));
                    Log.i(TAG, "  "
                            + config.cooperateWaterList.get(i) + ","
                            + config.cooperateWaterNumList.get(i) + ",");
                }
            }

            /* farm */
            config.enableFarm = jo.optBoolean(jn_enableFarm, true);
            Log.i(TAG, jn_enableFarm + ":" + config.enableFarm);

            config.rewardFriend = jo.optBoolean(jn_rewardFriend, true);
            Log.i(TAG, jn_rewardFriend + ":" + config.rewardFriend);

            config.sendBackAnimal = jo.optBoolean(jn_sendBackAnimal, true);
            Log.i(TAG, jn_sendBackAnimal + ":" + config.sendBackAnimal);

            config.sendType = SendType.valueOf(jo.optString(jn_sendType, SendType.HIT.name()));
            Log.i(TAG, jn_sendType + ":" + config.sendType.name());

            config.dontSendFriendList = new ArrayList<>();
            Log.i(TAG, jn_dontSendFriendList + ":[");
            if(jo.has(jn_dontSendFriendList))
            {
                ja = jo.getJSONArray(jn_dontSendFriendList);
                for(int i = 0; i < ja.length(); i++)
                {
                    config.dontSendFriendList.add(ja.getString(i));
                    Log.i(TAG, "  " + config.dontSendFriendList.get(i) + ",");
                }
            }

            config.recallAnimalType = RecallAnimalType.valueOf(jo.optString(jn_recallAnimalType, RecallAnimalType.ALWAYS.name()));
            Log.i(TAG, jn_recallAnimalType + ":" + config.recallAnimalType.name());

            config.receiveFarmToolReward = jo.optBoolean(jn_receiveFarmToolReward, true);
            Log.i(TAG, jn_receiveFarmToolReward + ":" + config.receiveFarmToolReward);

            config.useNewEggTool = jo.optBoolean(jn_useNewEggTool, true);
            Log.i(TAG, jn_useNewEggTool + ":" + config.useNewEggTool);

            config.harvestProduce = jo.optBoolean(jn_harvestProduce, true);
            Log.i(TAG, jn_harvestProduce + ":" + config.harvestProduce);

            config.donation = jo.optBoolean(jn_donation, true);
            Log.i(TAG, jn_donation + ":" + config.donation);

            config.answerQuestion = jo.optBoolean(jn_answerQuestion, true);
            Log.i(TAG, jn_answerQuestion + ":" + config.answerQuestion);

            config.receiveFarmTaskAward = jo.optBoolean(jn_receiveFarmTaskAward, true);
            Log.i(TAG, jn_receiveFarmTaskAward + ":" + config.receiveFarmTaskAward);

            config.feedAnimal = jo.optBoolean(jn_feedAnimal, true);
            Log.i(TAG, jn_feedAnimal + ":" + config.feedAnimal);

            config.useAccelerateTool = jo.optBoolean(jn_useAccelerateTool, true);
            Log.i(TAG, jn_useAccelerateTool + ":" + config.useAccelerateTool);

            config.feedFriendAnimalList = new ArrayList<>();
            config.feedFriendCountList = new ArrayList<>();
            Log.i(TAG, jn_feedFriendAnimalList + ":[");
            if(jo.has(jn_feedFriendAnimalList))
            {
                ja = jo.getJSONArray(jn_feedFriendAnimalList);
                for(int i = 0; i < ja.length(); i++)
                {
                    if(ja.get(i) instanceof JSONArray)
                    {
                        jaa = ja.getJSONArray(i);
                        config.feedFriendAnimalList.add(jaa.getString(0));
                        config.feedFriendCountList.add(jaa.getInt(1));
                    }else
                    {
                        config.feedFriendAnimalList.add(ja.getString(i));
                        config.feedFriendCountList.add(1);
                    }
                    Log.i(TAG, "  "
                            + config.feedFriendAnimalList.get(i) + ","
                            + config.feedFriendCountList.get(i) + ",");
                }
            }

            config.notifyFriend = jo.optBoolean(jn_notifyFriend, true);
            Log.i(TAG, jn_notifyFriend + ":" + config.notifyFriend);

            config.dontNotifyFriendList = new ArrayList<>();
            Log.i(TAG, jn_dontNotifyFriendList + ":[");
            if(jo.has(jn_dontNotifyFriendList))
            {
                ja = jo.getJSONArray(jn_dontNotifyFriendList);
                for(int i = 0; i < ja.length(); i++)
                {
                    config.dontNotifyFriendList.add(ja.getString(i));
                    Log.i(TAG, "  " + config.dontNotifyFriendList.get(i) + ",");
                }
            }

            /* other */
            config.receivePoint = jo.optBoolean(jn_receivePoint, true);
            Log.i(TAG, jn_receivePoint + ":" + config.receivePoint);

            config.openTreasureBox = jo.optBoolean(jn_openTreasureBox, true);
            Log.i(TAG, jn_openTreasureBox + ":" + config.openTreasureBox);

            config.donateCharityCoin = jo.optBoolean(jn_donateCharityCoin, true);
            Log.i(TAG, jn_donateCharityCoin + ":" + config.donateCharityCoin);

            config.minExchangeCount = jo.optInt(jn_minExchangeCount);
            Log.i(TAG, jn_minExchangeCount + ":" + config.minExchangeCount);

            config.latestExchangeTime = jo.optInt(jn_latestExchangeTime, 21);
            Log.i(TAG, jn_latestExchangeTime + ":" + config.latestExchangeTime);

            config.kbSignIn = jo.optBoolean(jn_kbSignIn, true);
            Log.i(TAG, jn_kbSignIn + ":" + config.kbSignIn);

        }catch(Throwable t)
        {
            Log.printStackTrace(TAG, t);
            if(json != null)
            {
                Log.i(TAG, "配置文件格式有误，已重置配置文件并备份原文件");
                FileUtils.write2File(json, FileUtils.getBackupFile(FileUtils.getConfigFile()));
            }
            config = defInit();
        }
        String formated = config2Json(config);
        if(!formated.equals(json))
        {
            Log.i(TAG, "重新格式化 config.json");
            FileUtils.write2File(formated, FileUtils.getConfigFile());
        }
        return config;
    }

    public static String config2Json(Config config)
    {
        JSONObject jo = new JSONObject();
        JSONArray ja = null, jaa = null;
        try
        {
            if(config == null) config = Config.defInit();

            jo.put(jn_immediateEffect, config.immediateEffect);

            jo.put(jn_recordLog, config.recordLog);

            jo.put(jn_showToast, config.showToast);

            jo.put(jn_stayAwake, config.stayAwake);

            jo.put(jn_autoRestart, config.autoRestart);

            jo.put(jn_xedgeproData, config.xedgeproData);

            /* forest */
            jo.put(jn_collectEnergy, config.collectEnergy);

            jo.put(jn_checkInterval, config.checkInterval);

            jo.put(jn_threadCount, config.threadCount);

            jo.put(jn_advanceTime, config.advanceTime);

            jo.put(jn_collectInterval, config.collectInterval);

            jo.put(jn_collectTimeout, config.collectTimeout);

            jo.put(jn_ReturnWater30, config.returnWater30);

            jo.put(jn_ReturnWater20, config.returnWater20);

            jo.put(jn_ReturnWater10, config.returnWater10);

            jo.put(jn_helpFriendCollect, config.helpFriendCollect);

            ja = new JSONArray();
            for(String s: config.dontCollectList)
            {
                ja.put(s);
            }
            jo.put(jn_dontCollectList, ja);

            ja = new JSONArray();
            for(String s: config.dontHelpCollectList)
            {
                ja.put(s);
            }
            jo.put(jn_dontHelpCollectList, ja);

            jo.put(jn_receiveForestTaskAward, config.receiveForestTaskAward);

            ja = new JSONArray();
            for(int i = 0; i < config.waterFriendList.size(); i++)
            {
                jaa = new JSONArray();
                jaa.put(config.waterFriendList.get(i));
                jaa.put(config.waterCountList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_waterFriendList, ja);

            jo.put(jn_cooperateWater, config.cooperateWater);

            ja = new JSONArray();
            for(int i = 0; i < config.cooperateWaterList.size(); i++)
            {
                jaa = new JSONArray();
                jaa.put(config.cooperateWaterList.get(i));
                jaa.put(config.cooperateWaterNumList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_cooperateWaterList, ja);

            /* farm */
            jo.put(jn_enableFarm, config.enableFarm);

            jo.put(jn_rewardFriend, config.rewardFriend);

            jo.put(jn_sendBackAnimal, config.sendBackAnimal);

            jo.put(jn_sendType, config.sendType.name());

            ja = new JSONArray();
            for(String s: config.dontSendFriendList)
            {
                ja.put(s);
            }
            jo.put(jn_dontSendFriendList, ja);

            jo.put(jn_recallAnimalType, config.recallAnimalType);

            jo.put(jn_receiveFarmToolReward, config.receiveFarmToolReward);

            jo.put(jn_useNewEggTool, config.useNewEggTool);

            jo.put(jn_harvestProduce, config.harvestProduce);

            jo.put(jn_donation, config.donation);

            jo.put(jn_answerQuestion, config.answerQuestion);

            jo.put(jn_receiveFarmTaskAward, config.receiveFarmTaskAward);

            jo.put(jn_feedAnimal, config.feedAnimal);

            jo.put(jn_useAccelerateTool, config.useAccelerateTool);

            ja = new JSONArray();
            for(int i = 0; i < config.feedFriendAnimalList.size(); i++)
            {
                jaa = new JSONArray();
                jaa.put(config.feedFriendAnimalList.get(i));
                jaa.put(config.feedFriendCountList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_feedFriendAnimalList, ja);

            jo.put(jn_notifyFriend, config.notifyFriend);

            ja = new JSONArray();
            for(String s: config.dontNotifyFriendList)
            {
                ja.put(s);
            }
            jo.put(jn_dontNotifyFriendList, ja);

            /* other */
            jo.put(jn_receivePoint, config.receivePoint);

            jo.put(jn_openTreasureBox, config.openTreasureBox);

            jo.put(jn_donateCharityCoin, config.donateCharityCoin);

            jo.put(jn_minExchangeCount, config.minExchangeCount);

            jo.put(jn_latestExchangeTime, config.latestExchangeTime);

            jo.put(jn_kbSignIn, config.kbSignIn);

        }catch(Throwable t)
        {
            Log.printStackTrace(TAG, t);
        }
        return formatJson(jo, false);
    }

    public static String formatJson(JSONObject jo, boolean removeQuote)
    {
        String formated = null;
        try
        {
            formated = jo.toString(4);
        }catch(Throwable t)
        {
            return jo.toString();
        }
        if(!removeQuote) return formated;
        StringBuilder sb = new StringBuilder(formated);
        char currentChar, lastNonSpaceChar = 0;
        for(int i = 0; i < sb.length(); i++)
        {
            currentChar = sb.charAt(i);
            switch(currentChar)
            {
                case '"':
                    switch(lastNonSpaceChar)
                    {
                        case ':':
                        case '[':
                            sb.deleteCharAt(i);
                            i = sb.indexOf("\"", i);
                            sb.deleteCharAt(i);
                            if(lastNonSpaceChar != '[') lastNonSpaceChar = sb.charAt(--i);
                    }
                    break;

                case ' ':
                    break;

                default:
                    if(lastNonSpaceChar == '[' && currentChar != ']')
                        break;
                    lastNonSpaceChar = currentChar;
            }
        }
        formated = sb.toString();
        return formated;
    }

}
