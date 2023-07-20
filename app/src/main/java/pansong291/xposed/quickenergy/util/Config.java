package pansong291.xposed.quickenergy.util;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.AntFarm.SendType;
import pansong291.xposed.quickenergy.hook.XposedHook;

import java.util.ArrayList;
import java.util.List;

public class Config
{
    public enum RecallAnimalType
    {
        ALWAYS, WHEN_THIEF, WHEN_HUNGRY, NEVER;
        public static final CharSequence[] nickNames = {"始终召回", "作贼时召回", "饥饿时召回", "不召回"};
        public static final CharSequence[] names =
                {ALWAYS.nickName(), WHEN_THIEF.nickName(), WHEN_HUNGRY.nickName(), NEVER.nickName()};

        public CharSequence nickName()
        {
            return nickNames[ordinal()];
        }
    }

    private static final String TAG = Config.class.getCanonicalName();
    public static final String
            jn_pauseTime = "pauseTime";
    public static final String/* application */
            jn_immediateEffect = "immediateEffect";
    public static final String jn_recordLog = "recordLog";
    public static final String jn_showToast = "showToast";
    public static final String jn_stayAwake = "stayAwake";
    public static final String jn_autoRestart = "autoRestart";
    public static final String jn_xedgeproData = "xedgeproData";
    public static final String jn_stayAwakeType = "stayAwakeType";
    public static final String/* forest */
    jn_collectEnergy = "collectEnergy";
    public static final String jn_ReturnWater33 = "returnWater30";
    public static final String jn_ReturnWater18 = "returnWater20";
    public static final String jn_ReturnWater10 = "returnWater10";
    public static final String jn_helpFriendCollect = "helpFriendCollect";
    public static final String jn_dontCollectList = "dontCollectList";
    public static final String jn_dontHelpCollectList = "dontHelpCollectList";
    public static final String jn_checkInterval = "checkInterval";
    public static final String jn_threadCount = "threadCount";
    public static final String jn_advanceTime = "advanceTime";
    public static final String jn_collectInterval = "collectInterval";
    public static final String jn_collectTimeout = "collectTimeout";
    public static final String jn_receiveForestTaskAward = "receiveForestTaskAward";
    public static final String jn_waterFriendList = "waterFriendList";
    public static final String jn_cooperateWater = "cooperateWater";
    public static final String jn_cooperateWaterList = "cooperateWaterList";
    public static final String jn_energyRain = "energyRain";
    public static final String jn_giveEnergyRainList = "giveEnergyRainList";
    public static final String jn_waitWhenException = "waitWhenException";
    public static final String/* farm */
    jn_enableFarm = "enableFarm";
    public static final String jn_rewardFriend = "rewardFriend";
    public static final String jn_sendBackAnimal = "sendBackAnimal";
    public static final String jn_sendType = "sendType";
    public static final String jn_dontSendFriendList = "dontSendFriendList";
    public static final String jn_recallAnimalType = "recallAnimalType";
    public static final String jn_receiveFarmToolReward = "receiveFarmToolReward";
    public static final String jn_useNewEggTool = "useNewEggTool";
    public static final String jn_harvestProduce = "harvestProduce";
    public static final String jn_donation = "donation";
    public static final String jn_answerQuestion = "answerQuestion";
    public static final String jn_receiveFarmTaskAward = "receiveFarmTaskAward";
    public static final String jn_feedAnimal = "feedAnimal";
    public static final String jn_useAccelerateTool = "useAccelerateTool";
    public static final String jn_feedFriendAnimalList = "feedFriendAnimalList";
    public static final String jn_notifyFriend = "notifyFriend";
    public static final String jn_dontNotifyFriendList = "dontNotifyFriendList";
    public static final String/* other */
    jn_receivePoint = "receivePoint";
    public static final String jn_openTreasureBox = "openTreasureBox";
    public static final String jn_donateCharityCoin = "donateCharityCoin";
    public static final String jn_minExchangeCount = "minExchangeCount";
    public static final String jn_latestExchangeTime = "latestExchangeTime";
    public static final String jn_syncStepCount = "syncStepCount";
    public static final String jn_kbSignIn = "kbSignIn";

    public static boolean shouldReload;
    public static boolean hasChanged;

    private long forestPauseTime;

    /* application */
    private boolean immediateEffect;
    private boolean recordLog;
    private boolean showToast;
    private boolean stayAwake;
    private XposedHook.StayAwakeType stayAwakeType;
    private boolean autoRestart;
    private String xEdgeProData;

    /* forest */
    private boolean collectEnergy;
    private int checkInterval;
    private boolean limitCollect;
    private int limitCount;

    private boolean doubleCard;
    private int threadCount;
    private int advanceTime;
    private int collectInterval;
    private int collectTimeout;
    private int returnWater33;
    private int returnWater18;
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
    private boolean energyRain;

    private List<String> giveEnergyRainList;

    private int waitWhenException;

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
    private int syncStepCount;
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

    public static void setForestPauseTime(long b)
    {
        getConfig().forestPauseTime = b;
        hasChanged = true;
    }

    public static long forestPauseTime()
    {
        return getConfig().forestPauseTime;
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

    public static void setStayAwakeType(int i)
    {
        getConfig().stayAwakeType = XposedHook.StayAwakeType.values()[i];
        hasChanged = true;
    }

    public static XposedHook.StayAwakeType stayAwakeType() {
        return getConfig().stayAwakeType;
    }

    public static void setAutoRestart(boolean b) {
        getConfig().autoRestart = b;
        hasChanged = true;
    }

    public static boolean autoRestart() {
        return getConfig().autoRestart;
    }

    public static void setXEdgeProData(String s) {
        getConfig().xEdgeProData = s;
        hasChanged = true;
    }

    public static String xEdgeProData() {
        return getConfig().xEdgeProData;
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
        getConfig().checkInterval = i;
        hasChanged = true;
    }

    public static int checkInterval()
    {
        return getConfig().checkInterval;
    }

    public static void setWaitWhenException(int i)
    {
        getConfig().waitWhenException = i;
        hasChanged = true;
    }

    public static int waitWhenException()
    {
        return getConfig().waitWhenException;
    }

    public static boolean isLimitCollect() {
        return getConfig().limitCollect;
    }

    public static void setLimitCollect(boolean limitCollect) {
        getConfig().limitCollect = limitCollect;
        hasChanged = true;
    }

    public static int getLimitCount() {
        return getConfig().limitCount;
    }

    public static void setLimitCount(int limitCount) {
        getConfig().limitCount = limitCount;
        hasChanged = true;
    }

    public static boolean doubleCard() {
        return getConfig().doubleCard;
    }

    public static void setDoubleCard(boolean doubleCard) {
        getConfig().doubleCard = doubleCard;
        hasChanged = true;
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

    public static void setReturnWater33(int i)
    {
        getConfig().returnWater33 = i;
        hasChanged = true;
    }

    public static int returnWater33()
    {
        return getConfig().returnWater33;
    }

    public static void setReturnWater18(int i)
    {
        getConfig().returnWater18 = i;
        hasChanged = true;
    }

    public static int returnWater18()
    {
        return getConfig().returnWater18;
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

    public static void setEnergyRain(boolean b)
    {
        getConfig().energyRain = b;
        hasChanged = true;
    }

    public static List<String> getGiveEnergyRainList()
    {
        return getConfig().giveEnergyRainList;
    }

    public static boolean energyRain()
    {
        return getConfig().energyRain;
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

    public static void setSyncStepCount(int i)
    {
        getConfig().syncStepCount = i;
        hasChanged = true;
    }

    public static int syncStepCount()
    {
        return getConfig().syncStepCount;
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

        c.forestPauseTime = 0L;
        c.immediateEffect = true;
        c.recordLog = true;
        c.showToast = true;
        c.stayAwake = true;
        c.stayAwakeType = XposedHook.StayAwakeType.BROADCAST;
        c.autoRestart = true;
        c.xEdgeProData = "";

        c.collectEnergy = true;
        c.checkInterval = 720_000;
        c.waitWhenException = 60 * 60 * 1000;
        c.limitCollect = true;
        c.limitCount = 50;
        c.doubleCard = false;
        c.threadCount = 1;
        c.advanceTime = 500;
        c.collectInterval = 100;
        c.collectTimeout = 2_000;
        c.returnWater33 = 0;
        c.returnWater18 = 0;
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
        c.energyRain = true;
        if(c.giveEnergyRainList == null) c.giveEnergyRainList = new ArrayList<>();

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

    public static boolean saveConfigFile() {
        return FileUtils.write2File(config2Json(config), FileUtils.getConfigFile());
    }

    public static Config json2Config(String json)
    {
        Config config = null;
        try
        {
            JSONObject jo = new JSONObject(json);
            JSONArray ja, jaa;
            config = new Config();

            config.forestPauseTime = jo.optInt(jn_pauseTime, 0);

            config.immediateEffect = jo.optBoolean(jn_immediateEffect, true);

            config.recordLog = jo.optBoolean(jn_recordLog, true);

            config.showToast = jo.optBoolean(jn_showToast, true);

            config.stayAwake = jo.optBoolean(jn_stayAwake, true);

            config.stayAwakeType = XposedHook.StayAwakeType.valueOf(jo.optString(jn_stayAwakeType, XposedHook.StayAwakeType.BROADCAST.name()));

            config.autoRestart = jo.optBoolean(jn_autoRestart, true);

            config.xEdgeProData = jo.optString(jn_xedgeproData, "");

            /* forest */
            config.collectEnergy = jo.optBoolean(jn_collectEnergy, true);

            config.checkInterval = jo.optInt(jn_checkInterval, 720_000);

            config.waitWhenException = jo.optInt(jn_waitWhenException, 60 * 60 * 1000);

            config.limitCollect = jo.optBoolean("limitCollect", true);

            config.limitCount = jo.optInt("limitCount", 50);

            config.doubleCard = jo.optBoolean("doubleCard", false);

            config.threadCount = jo.optInt(jn_threadCount, 1);

            config.advanceTime = jo.optInt(jn_advanceTime, 500);

            config.collectInterval = jo.optInt(jn_collectInterval, 100);

            config.collectTimeout = jo.optInt(jn_collectTimeout, 2_000);

            config.returnWater33 = jo.optInt(jn_ReturnWater33);

            config.returnWater18 = jo.optInt(jn_ReturnWater18);

            config.returnWater10 = jo.optInt(jn_ReturnWater10);

            config.helpFriendCollect = jo.optBoolean(jn_helpFriendCollect, true);

            config.dontCollectList = new ArrayList<>();
            if(jo.has(jn_dontCollectList))
            {
                ja = jo.getJSONArray(jn_dontCollectList);
                for(int i = 0; i < ja.length(); i++)
                {
                    config.dontCollectList.add(ja.getString(i));
                }
            }

            config.dontHelpCollectList = new ArrayList<>();
            if(jo.has(jn_dontHelpCollectList))
            {
                ja = jo.getJSONArray(jn_dontHelpCollectList);
                for(int i = 0; i < ja.length(); i++)
                {
                    config.dontHelpCollectList.add(ja.getString(i));
                }
            }

            config.receiveForestTaskAward = jo.optBoolean(jn_receiveForestTaskAward, true);

            config.waterFriendList = new ArrayList<>();
            config.waterCountList = new ArrayList<>();
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
                }
            }

            config.cooperateWater = jo.optBoolean(jn_cooperateWater, true);

            config.cooperateWaterList = new ArrayList<>();
            config.cooperateWaterNumList = new ArrayList<>();
            if(jo.has(jn_cooperateWaterList))
            {
                ja = jo.getJSONArray(jn_cooperateWaterList);
                for(int i = 0; i < ja.length(); i++)
                {
                    jaa = ja.getJSONArray(i);
                    config.cooperateWaterList.add(jaa.getString(0));
                    config.cooperateWaterNumList.add(jaa.getInt(1));
                }
            }
            config.energyRain = jo.optBoolean(jn_energyRain, true);
            config.giveEnergyRainList = new ArrayList<>();
            if(jo.has(jn_giveEnergyRainList))
            {
                ja = jo.getJSONArray(jn_giveEnergyRainList);
                for(int i = 0; i < ja.length(); i++)
                {
                    jaa = ja.getJSONArray(i);
                    config.giveEnergyRainList.add(jaa.getString(0));
                }
            }

            /* farm */
            config.enableFarm = jo.optBoolean(jn_enableFarm, true);

            config.rewardFriend = jo.optBoolean(jn_rewardFriend, true);

            config.sendBackAnimal = jo.optBoolean(jn_sendBackAnimal, true);

            config.sendType = SendType.valueOf(jo.optString(jn_sendType, SendType.HIT.name()));

            config.dontSendFriendList = new ArrayList<>();
            if(jo.has(jn_dontSendFriendList))
            {
                ja = jo.getJSONArray(jn_dontSendFriendList);
                for(int i = 0; i < ja.length(); i++)
                {
                    config.dontSendFriendList.add(ja.getString(i));
                }
            }

            config.recallAnimalType = RecallAnimalType.valueOf(jo.optString(jn_recallAnimalType, RecallAnimalType.ALWAYS.name()));

            config.receiveFarmToolReward = jo.optBoolean(jn_receiveFarmToolReward, true);

            config.useNewEggTool = jo.optBoolean(jn_useNewEggTool, true);

            config.harvestProduce = jo.optBoolean(jn_harvestProduce, true);

            config.donation = jo.optBoolean(jn_donation, true);

            config.answerQuestion = jo.optBoolean(jn_answerQuestion, true);

            config.receiveFarmTaskAward = jo.optBoolean(jn_receiveFarmTaskAward, true);

            config.feedAnimal = jo.optBoolean(jn_feedAnimal, true);

            config.useAccelerateTool = jo.optBoolean(jn_useAccelerateTool, true);

            config.feedFriendAnimalList = new ArrayList<>();
            config.feedFriendCountList = new ArrayList<>();
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
                }
            }

            config.notifyFriend = jo.optBoolean(jn_notifyFriend, true);

            config.dontNotifyFriendList = new ArrayList<>();
            if(jo.has(jn_dontNotifyFriendList))
            {
                ja = jo.getJSONArray(jn_dontNotifyFriendList);
                for(int i = 0; i < ja.length(); i++)
                {
                    config.dontNotifyFriendList.add(ja.getString(i));
                }
            }

            /* other */
            config.receivePoint = jo.optBoolean(jn_receivePoint, true);

            config.openTreasureBox = jo.optBoolean(jn_openTreasureBox, true);

            config.donateCharityCoin = jo.optBoolean(jn_donateCharityCoin, true);

            config.minExchangeCount = jo.optInt(jn_minExchangeCount);

            config.latestExchangeTime = jo.optInt(jn_latestExchangeTime, 21);

            config.syncStepCount = jo.optInt(jn_syncStepCount, 22000);

            config.kbSignIn = jo.optBoolean(jn_kbSignIn, true);

        } catch(Throwable t) {
            Log.printStackTrace(TAG, t);
            if(json != null) {
                Log.i(TAG, "配置文件格式有误，已重置配置文件并备份原文件");
                FileUtils.write2File(json, FileUtils.getBackupFile(FileUtils.getConfigFile()));
            }
            config = defInit();
        }
        String formated = config2Json(config);
        if(!formated.equals(json)) {
            Log.i(TAG, "重新格式化 config.json");
            FileUtils.write2File(formated, FileUtils.getConfigFile());
        }
        return config;
    }

    public static String config2Json(Config config) {
        JSONObject jo = new JSONObject();
        JSONArray ja, jaa;
        try
        {
            if(config == null) config = Config.defInit();

            jo.put(jn_pauseTime, config.forestPauseTime);

            jo.put(jn_immediateEffect, config.immediateEffect);

            jo.put(jn_recordLog, config.recordLog);

            jo.put(jn_showToast, config.showToast);

            jo.put(jn_stayAwake, config.stayAwake);

            jo.put(jn_stayAwakeType, config.stayAwakeType);

            jo.put(jn_autoRestart, config.autoRestart);

            jo.put(jn_xedgeproData, config.xEdgeProData);

            /* forest */
            jo.put(jn_collectEnergy, config.collectEnergy);

            jo.put(jn_checkInterval, config.checkInterval);

            jo.put(jn_waitWhenException, config.waitWhenException);

            jo.put("limitCollect", config.limitCollect);

            jo.put("limitCount", config.limitCount);

            jo.put("doubleCard", config.doubleCard);

            jo.put(jn_threadCount, config.threadCount);

            jo.put(jn_advanceTime, config.advanceTime);

            jo.put(jn_collectInterval, config.collectInterval);

            jo.put(jn_collectTimeout, config.collectTimeout);

            jo.put(jn_ReturnWater33, config.returnWater33);

            jo.put(jn_ReturnWater18, config.returnWater18);

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

            jo.put(jn_energyRain, config.energyRain);
            ja = new JSONArray();
            for(int i = 0; i < config.giveEnergyRainList.size(); i++)
            {
                jaa = new JSONArray();
                jaa.put(config.giveEnergyRainList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_giveEnergyRainList, ja);

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

            jo.put(jn_syncStepCount, config.syncStepCount);

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
