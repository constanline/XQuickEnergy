package pansong291.xposed.quickenergy.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.AntFarm.SendType;
import pansong291.xposed.quickenergy.hook.ClassMember;
import pansong291.xposed.quickenergy.hook.XposedHook;

import java.text.SimpleDateFormat;
import java.util.*;

public class Config {
    public enum RecallAnimalType {
        ALWAYS, WHEN_THIEF, WHEN_HUNGRY, NEVER;

        public static final CharSequence[] nickNames = { "始终召回", "作贼时召回", "饥饿时召回", "不召回" };
        public static final CharSequence[] names = { ALWAYS.nickName(), WHEN_THIEF.nickName(), WHEN_HUNGRY.nickName(),
                NEVER.nickName() };

        public CharSequence nickName() {
            return nickNames[ordinal()];
        }
    }

    private static final String TAG = Config.class.getCanonicalName();
    /* application */
    public static final String jn_pauseTime = "pauseTime";
    public static final String jn_immediateEffect = "immediateEffect";
    public static final String jn_recordLog = "recordLog";
    public static final String jn_showToast = "showToast";
    public static final String jn_toastOffsetY = "toastOffsetY";
    public static final String jn_stayAwake = "stayAwake";
    public static final String jn_timeoutRestart = "timeoutRestart";
    public static final String jn_stayAwakeType = "stayAwakeType";
    public static final String jn_stayAwakeTarget = "stayAwakeTarget";
    public static final String jn_timeoutType = "timeoutType";
    public static final String jn_startAt7 = "startAt7";

    /* forest */
    public static final String jn_collectEnergy = "collectEnergy";

    public static final String jn_collectWateringBubble = "collectWateringBubble";
    public static final String jn_collectProp = "collectProp";
    public static final String jn_ReturnWater33 = "returnWater30";
    public static final String jn_ReturnWater18 = "returnWater20";
    public static final String jn_ReturnWater10 = "returnWater10";
    public static final String jn_helpFriendCollect = "helpFriendCollect";
    public static final String jn_dontCollectList = "dontCollectList";
    public static final String jn_dontHelpCollectList = "dontHelpCollectList";
    public static final String jn_checkInterval = "checkInterval";
    public static final String jn_doubleCard = "doubleCard";
    public static final String jn_doubleCardTime = "doubleCardTime";
    public static final String jn_advanceTime = "advanceTime";
    public static final String jn_collectInterval = "collectInterval";
    public static final String jn_collectTimeout = "collectTimeout";
    public static final String jn_receiveForestTaskAward = "receiveForestTaskAward";
    public static final String jn_waterFriendList = "waterFriendList";
    public static final String jn_waterFriendCount = "waterFriendCount";
    public static final String jn_cooperateWater = "cooperateWater";
    public static final String jn_cooperateWaterList = "cooperateWaterList";
    public static final String jn_ancientTree = "ancientTree";
    public static final String jn_ancientTreeCityCodeList = "ancientTreeCityCodeList";
    public static final String jn_reserve = "reserve";
    public static final String jn_reserveList = "reserveList";
    public static final String jn_beach = "beach";
    public static final String jn_beachList = "beachList";
    public static final String jn_energyRain = "energyRain";
    public static final String jn_giveEnergyRainList = "giveEnergyRainList";
    public static final String jn_waitWhenException = "waitWhenException";
    public static final String jn_ancientTreeOnlyWeek = "ancientTreeOnlyWeek";
    /* farm */
    public static final String jn_enableFarm = "enableFarm";
    public static final String jn_rewardFriend = "rewardFriend";
    public static final String jn_sendBackAnimal = "sendBackAnimal";
    public static final String jn_sendType = "sendType";
    public static final String jn_dontSendFriendList = "dontSendFriendList";
    public static final String jn_recallAnimalType = "recallAnimalType";
    public static final String jn_receiveFarmToolReward = "receiveFarmToolReward";
    public static final String jn_recordFarmGame = "recordFarmGame";
    public static final String jn_kitchen = "kitchen";
    public static final String jn_useNewEggTool = "useNewEggTool";
    public static final String jn_harvestProduce = "harvestProduce";
    public static final String jn_donation = "donation";
    public static final String jn_answerQuestion = "answerQuestion";
    public static final String jn_receiveFarmTaskAward = "receiveFarmTaskAward";
    public static final String jn_feedAnimal = "feedAnimal";
    public static final String jn_useAccelerateTool = "useAccelerateTool";
    public static final String jn_feedFriendAnimalList = "feedFriendAnimalList";
    public static final String jn_farmGameTime = "farmGameTime";
    public static final String jn_animalSleepTime = "animalSleepTime";
    public static final String jn_notifyFriend = "notifyFriend";
    public static final String jn_dontNotifyFriendList = "dontNotifyFriendList";
    public static final String jn_antdodoCollect = "antdodoCollect";
    public static final String jn_antOcean = "antOcean";
    public static final String jn_userPatrol = "userPatrol";
    public static final String jn_animalConsumeProp = "animalConsumeProp";

    /* other */
    public static final String jn_receivePoint = "receivePoint";
    public static final String jn_openTreasureBox = "openTreasureBox";
    public static final String jn_receiveCoinAsset = "receiveCoinAsset";
    public static final String jn_donateCharityCoin = "donateCharityCoin";
    public static final String jn_minExchangeCount = "minExchangeCount";
    public static final String jn_latestExchangeTime = "latestExchangeTime";
    public static final String jn_syncStepCount = "syncStepCount";
    public static final String jn_kbSignIn = "kbSignIn";
    public static final String jn_ecoLifeTick = "ecoLifeTick";
    public static final String jn_tiyubiz = "tiyubiz";
    public static final String jn_insBlueBeanExchange = "insBlueBeanExchange";

    public static boolean shouldReload;
    public static boolean hasChanged;

    private long forestPauseTime;

    /* application */
    private boolean immediateEffect;
    private boolean recordLog;
    private boolean showToast;
    private int toastOffsetY;
    private boolean stayAwake;
    private XposedHook.StayAwakeType stayAwakeType;
    private XposedHook.StayAwakeTarget stayAwakeTarget;
    private boolean timeoutRestart;
    private XposedHook.StayAwakeType timeoutType;
    private boolean startAt7;

    /* forest */
    private boolean collectEnergy;
    private int checkInterval;

    private boolean collectWateringBubble;
    private boolean collectProp;
    private boolean limitCollect;
    private int limitCount;
    private boolean doubleCard;
    private List<String> doubleCardTime;
    private int doubleCountLimit;
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
    private int waterFriendCount;
    private List<Integer> waterCountList;
    private boolean cooperateWater;
    private List<String> cooperateWaterList;
    private List<Integer> cooperateWaterNumList;
    private boolean ancientTree;
    private List<String> ancientTreeCityCodeList;
    private boolean energyRain;
    private boolean reserve;
    private List<String> reserveList;
    private List<Integer> reserveCountList;
    private boolean beach;
    private List<String> beachList;
    private List<Integer> beachCountList;
    private boolean ancientTreeOnlyWeek;

    private List<String> giveEnergyRainList;

    private int waitWhenException;

    private boolean exchangeEnergyDoubleClick;
    private int exchangeEnergyDoubleClickCount;
    private boolean antdodoCollect;
    private boolean antOcean;
    private boolean userPatrol;
    private boolean animalConsumeProp;

    /* farm */
    private boolean enableFarm;
    private boolean rewardFriend;
    private boolean sendBackAnimal;
    private SendType sendType;
    private List<String> dontSendFriendList;
    private RecallAnimalType recallAnimalType;
    private boolean receiveFarmToolReward;
    private boolean recordFarmGame;
    private boolean kitchen;
    private boolean useNewEggTool;
    private boolean harvestProduce;
    private boolean donation;
    private boolean answerQuestion;
    private boolean receiveFarmTaskAward;
    private boolean feedAnimal;
    private boolean useAccelerateTool;
    private List<String> feedFriendAnimalList;
    private List<Integer> feedFriendCountList;

    private List<String> farmGameTime;
    private List<String> animalSleepTime;
    private boolean notifyFriend;
    private List<String> dontNotifyFriendList;

    /* other */
    private boolean receivePoint;
    private boolean openTreasureBox;
    private boolean receiveCoinAsset;
    private boolean donateCharityCoin;
    private int minExchangeCount;
    private int latestExchangeTime;
    private int syncStepCount;
    private boolean kbSignIn;
    private boolean ecoLifeTick;
    private boolean tiyubiz;
    private boolean insBlueBeanExchange;

    /* base */
    private static Config config;

    /* application */
    public static void setImmediateEffect(boolean b) {
        getConfig().immediateEffect = b;
        hasChanged = true;
    }

    public static boolean immediateEffect() {
        return getConfig().immediateEffect;
    }

    public static void setRecordLog(boolean b) {
        getConfig().recordLog = b;
        hasChanged = true;
    }

    public static boolean recordLog() {
        return getConfig().recordLog;
    }

    public static void setForestPauseTime(long b) {
        getConfig().forestPauseTime = b;
        hasChanged = true;
    }

    public static long forestPauseTime() {
        return getConfig().forestPauseTime;
    }

    public static void setShowToast(boolean b) {
        getConfig().showToast = b;
        hasChanged = true;
    }

    public static boolean showToast() {
        return getConfig().showToast;
    }

    public static void setToastOffsetY(int i) {
        getConfig().toastOffsetY = i;
        hasChanged = true;
    }

    public static int toastOffsetY() {
        return getConfig().toastOffsetY;
    }

    public static void setStayAwake(boolean b) {
        getConfig().stayAwake = b;
        hasChanged = true;
    }

    public static boolean stayAwake() {
        return getConfig().stayAwake;
    }

    public static void setStayAwakeType(int i) {
        getConfig().stayAwakeType = XposedHook.StayAwakeType.values()[i];
        hasChanged = true;
    }

    public static XposedHook.StayAwakeType stayAwakeType() {
        return getConfig().stayAwakeType;
    }

    public static void setStayAwakeTarget(int i) {
        getConfig().stayAwakeTarget = XposedHook.StayAwakeTarget.values()[i];
        hasChanged = true;
    }

    public static XposedHook.StayAwakeTarget stayAwakeTarget() {
        return getConfig().stayAwakeTarget;
    }

    public static void setTimeoutRestart(boolean b) {
        getConfig().timeoutRestart = b;
        hasChanged = true;
    }

    public static boolean timeoutRestart() {
        return getConfig().timeoutRestart;
    }

    public static void setTimeoutType(int i) {
        getConfig().timeoutType = XposedHook.StayAwakeType.values()[i];
        hasChanged = true;
    }

    public static XposedHook.StayAwakeType timeoutType() {
        return getConfig().timeoutType;
    }

    public static void setStartAt7(Context context, boolean b) {
        getConfig().startAt7 = b;
        if (b) {
            setAlarm7(context);
        } else {
            cancelAlarm7(context);
        }
        hasChanged = true;
    }

    public static boolean startAt7() {
        return getConfig().startAt7;
    }

    /* forest */
    public static void setCollectEnergy(boolean b) {
        getConfig().collectEnergy = b;
        hasChanged = true;
    }

    public static boolean collectEnergy() {
        return getConfig().collectEnergy;
    }

    public static void setCollectWateringBubble(boolean b) {
        getConfig().collectWateringBubble = b;
        hasChanged = true;
    }

    public static boolean collectWateringBubble() {
        return getConfig().collectWateringBubble;
    }

    public static void setCollectProp(boolean b) {
        getConfig().collectProp = b;
        hasChanged = true;
    }

    public static boolean collectProp() {
        return getConfig().collectProp;
    }

    public static void setCheckInterval(int i) {
        getConfig().checkInterval = i;
        hasChanged = true;
    }

    public static int checkInterval() {
        return getConfig().checkInterval;
    }

    public static void setWaitWhenException(int i) {
        getConfig().waitWhenException = i;
        hasChanged = true;
    }

    public static int waitWhenException() {
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

    public static void setDoubleCardTime(String i) {
        getConfig().doubleCardTime = Arrays.asList(i.split(","));
        hasChanged = true;
    }

    public static String doubleCardTime() {
        return String.join(",", getConfig().doubleCardTime);
    }

    public static boolean isDoubleCardTime() {
        for (String doubleTime : getConfig().doubleCardTime) {
            if (checkInTimeSpan(doubleTime))
                return true;
        }
        return false;
    }

    public static int getDoubleCountLimit() {
        return getConfig().doubleCountLimit;
    }

    public static void setDoubleCountLimit(int doubleCountLimit) {
        getConfig().doubleCountLimit = doubleCountLimit;
        hasChanged = true;
    }

    public static void setAdvanceTime(int i) {
        getConfig().advanceTime = i;
        hasChanged = true;
    }

    public static int advanceTime() {
        return getConfig().advanceTime;
    }

    public static void setCollectInterval(int i) {
        getConfig().collectInterval = i;
        hasChanged = true;
    }

    public static int collectInterval() {
        return getConfig().collectInterval;
    }

    public static void setCollectTimeout(int i) {
        getConfig().collectTimeout = i;
        hasChanged = true;
    }

    public static int collectTimeout() {
        return getConfig().collectTimeout;
    }

    public static void setReturnWater33(int i) {
        getConfig().returnWater33 = i;
        hasChanged = true;
    }

    public static int returnWater33() {
        return getConfig().returnWater33;
    }

    public static void setReturnWater18(int i) {
        getConfig().returnWater18 = i;
        hasChanged = true;
    }

    public static int returnWater18() {
        return getConfig().returnWater18;
    }

    public static void setReturnWater10(int i) {
        getConfig().returnWater10 = i;
        hasChanged = true;
    }

    public static int returnWater10() {
        return getConfig().returnWater10;
    }

    public static void setHelpFriendCollect(boolean b) {
        getConfig().helpFriendCollect = b;
        hasChanged = true;
    }

    public static boolean helpFriendCollect() {
        return getConfig().helpFriendCollect;
    }

    public static List<String> getDontCollectList() {
        return getConfig().dontCollectList;
    }

    public static List<String> getDontHelpCollectList() {
        return getConfig().dontHelpCollectList;
    }

    public static void setReceiveForestTaskAward(boolean b) {
        getConfig().receiveForestTaskAward = b;
        hasChanged = true;
    }

    public static boolean receiveForestTaskAward() {
        return getConfig().receiveForestTaskAward;
    }

    public static List<String> getWaterFriendList() {
        return getConfig().waterFriendList;
    }

    public static List<Integer> getWaterCountList() {
        return getConfig().waterCountList;
    }

    public static void setWaterFriendCount(int i) {
        getConfig().waterFriendCount = i;
        hasChanged = true;
    }

    public static int waterFriendCount() {
        return getConfig().waterFriendCount;
    }

    public static void setCooperateWater(boolean b) {
        getConfig().cooperateWater = b;
        hasChanged = true;
    }

    public static boolean cooperateWater() {
        return getConfig().cooperateWater;
    }

    public static List<String> getCooperateWaterList() {
        return getConfig().cooperateWaterList;
    }

    public static List<Integer> getcooperateWaterNumList() {
        return getConfig().cooperateWaterNumList;
    }

    public static void setAncientTree(boolean b) {
        getConfig().ancientTree = b;
        hasChanged = true;
    }

    public static boolean ancientTree() {
        return getConfig().ancientTree;
    }

    public static List<String> getAncientTreeCityCodeList() {
        return getConfig().ancientTreeCityCodeList;
    }

    public static void setReserve(boolean b) {
        getConfig().reserve = b;
        hasChanged = true;
    }

    public static boolean reserve() {
        return getConfig().reserve;
    }

    public static List<String> getReserveList() {
        return getConfig().reserveList;
    }

    public static List<Integer> getReserveCountList() {
        return getConfig().reserveCountList;
    }

    public static void setBeach(boolean b) {
        getConfig().beach = b;
        hasChanged = true;
    }

    public static boolean beach() {
        return getConfig().beach;
    }

    public static List<String> getBeachList() {
        return getConfig().beachList;
    }

    public static List<Integer> getBeachCountList() {
        return getConfig().beachCountList;
    }

    public static void setEnergyRain(boolean b) {
        getConfig().energyRain = b;
        hasChanged = true;
    }

    public static List<String> getGiveEnergyRainList() {
        return getConfig().giveEnergyRainList;
    }

    public static boolean energyRain() {
        return getConfig().energyRain;
    }

    public static boolean ExchangeEnergyDoubleClick() {
        return getConfig().exchangeEnergyDoubleClick;
    }

    public static void setExchangeEnergyDoubleClick(boolean exchangeEnergyDoubleClick) {
        getConfig().exchangeEnergyDoubleClick = exchangeEnergyDoubleClick;
        hasChanged = true;
    }

    public static int getExchangeEnergyDoubleClickCount() {
        return getConfig().exchangeEnergyDoubleClickCount;
    }

    public static void setExchangeEnergyDoubleClickCount(int exchangeEnergyDoubleClickCount) {
        getConfig().exchangeEnergyDoubleClickCount = exchangeEnergyDoubleClickCount;
        hasChanged = true;
    }

    public static void setAncientTreeOnlyWeek(boolean b) {
        getConfig().ancientTreeOnlyWeek = b;
        hasChanged = true;
    }

    public static boolean ancientTreeOnlyWeek() {
        return getConfig().ancientTreeOnlyWeek;
    }

    public static boolean isAncientTreeWeek() {
        if (!ancientTreeOnlyWeek()) {
            return true;
        }
        SimpleDateFormat sdf_week = new SimpleDateFormat("EEEE", Locale.getDefault());
        String week = sdf_week.format(new Date());
        return "星期一".equals(week) || "星期三".equals(week) || "星期五".equals(week);
    }

    public static void setAntdodoCollect(boolean b) {
        getConfig().antdodoCollect = b;
        hasChanged = true;
    }

    public static boolean antdodoCollect() {
        return getConfig().antdodoCollect;
    }

    public static void setAntOcean(boolean b) {
        getConfig().antOcean = b;
        hasChanged = true;
    }

    public static boolean antOcean() {
        return getConfig().antOcean;
    }

    public static void setUserPatrol(boolean b) {
        getConfig().userPatrol = b;
        hasChanged = true;
    }

    public static boolean userPatrol() {
        return getConfig().userPatrol;
    }

    public static void setAnimalConsumeProp(boolean b) {
        getConfig().animalConsumeProp = b;
        hasChanged = true;
    }

    public static boolean animalConsumeProp() {
        return getConfig().animalConsumeProp;
    }

    /* farm */
    public static void setEnableFarm(boolean b) {
        getConfig().enableFarm = b;
        hasChanged = true;
    }

    public static boolean enableFarm() {
        return getConfig().enableFarm;
    }

    public static void setRewardFriend(boolean b) {
        getConfig().rewardFriend = b;
        hasChanged = true;
    }

    public static boolean rewardFriend() {
        return getConfig().rewardFriend;
    }

    public static void setSendBackAnimal(boolean b) {
        getConfig().sendBackAnimal = b;
        hasChanged = true;
    }

    public static boolean sendBackAnimal() {
        return getConfig().sendBackAnimal;
    }

    public static void setSendType(int i) {
        getConfig().sendType = SendType.values()[i];
        hasChanged = true;
    }

    public static SendType sendType() {
        return getConfig().sendType;
    }

    public static List<String> getDontSendFriendList() {
        return getConfig().dontSendFriendList;
    }

    public static void setRecallAnimalType(int i) {
        getConfig().recallAnimalType = RecallAnimalType.values()[i];
        hasChanged = true;
    }

    public static RecallAnimalType recallAnimalType() {
        return getConfig().recallAnimalType;
    }

    public static void setReceiveFarmToolReward(boolean b) {
        getConfig().receiveFarmToolReward = b;
        hasChanged = true;
    }

    public static boolean receiveFarmToolReward() {
        return getConfig().receiveFarmToolReward;
    }

    public static void setRecordFarmGame(boolean b) {
        getConfig().recordFarmGame = b;
        hasChanged = true;
    }

    public static boolean recordFarmGame() {
        return getConfig().recordFarmGame;
    }

    public static void setKitchen(boolean b) {
        getConfig().kitchen = b;
        hasChanged = true;
    }

    public static boolean kitchen() {
        return getConfig().kitchen;
    }

    public static void setUseNewEggTool(boolean b) {
        getConfig().useNewEggTool = b;
        hasChanged = true;
    }

    public static boolean useNewEggTool() {
        return getConfig().useNewEggTool;
    }

    public static void setHarvestProduce(boolean b) {
        getConfig().harvestProduce = b;
        hasChanged = true;
    }

    public static boolean harvestProduce() {
        return getConfig().harvestProduce;
    }

    public static void setDonation(boolean b) {
        getConfig().donation = b;
        hasChanged = true;
    }

    public static boolean donation() {
        return getConfig().donation;
    }

    public static void setAnswerQuestion(boolean b) {
        getConfig().answerQuestion = b;
        hasChanged = true;
    }

    public static boolean answerQuestion() {
        return getConfig().answerQuestion;
    }

    public static void setReceiveFarmTaskAward(boolean b) {
        getConfig().receiveFarmTaskAward = b;
        hasChanged = true;
    }

    public static boolean receiveFarmTaskAward() {
        return getConfig().receiveFarmTaskAward;
    }

    public static void setFeedAnimal(boolean b) {
        getConfig().feedAnimal = b;
        hasChanged = true;
    }

    public static boolean feedAnimal() {
        return getConfig().feedAnimal;
    }

    public static void setUseAccelerateTool(boolean b) {
        getConfig().useAccelerateTool = b;
        hasChanged = true;
    }

    public static boolean useAccelerateTool() {
        return getConfig().useAccelerateTool;
    }

    public static List<String> getFeedFriendAnimalList() {
        return getConfig().feedFriendAnimalList;
    }

    public static List<Integer> getFeedFriendCountList() {
        return getConfig().feedFriendCountList;
    }

    public static void setFarmGameTime(String i) {
        getConfig().farmGameTime = Arrays.asList(i.split(","));
        hasChanged = true;
    }

    public static String farmGameTime() {
        return String.join(",", getConfig().farmGameTime);
    }

    public static boolean isFarmGameTime() {
        for (String doubleTime : getConfig().farmGameTime) {
            if (checkInTimeSpan(doubleTime))
                return true;
        }
        return false;
    }

    public static void setAnimalSleepTime(String i) {
        getConfig().animalSleepTime = Arrays.asList(i.split(","));
        hasChanged = true;
    }

    public static String animalSleepTime() {
        return String.join(",", getConfig().animalSleepTime);
    }

    public static boolean isAnimalSleepTime() {
        for (String doubleTime : getConfig().animalSleepTime) {
            if (checkInTimeSpan(doubleTime))
                return true;
        }
        return false;
    }

    private static boolean checkInTimeSpan(String timestr) {
        if (timestr.contains("-")) {
            String[] arr = timestr.split("-");
            String min = arr[0];
            String max = arr[1];
            String now = TimeUtil.getTimeStr();
            return min.compareTo(now) <= 0 && max.compareTo(now) >= 0;
        } else {
            return TimeUtil.checkInTime(-getConfig().checkInterval, timestr);
        }
    }

    public static void setNotifyFriend(boolean b) {
        getConfig().notifyFriend = b;
        hasChanged = true;
    }

    public static boolean notifyFriend() {
        return getConfig().notifyFriend;
    }

    public static List<String> getDontNotifyFriendList() {
        return getConfig().dontNotifyFriendList;
    }

    /* other */
    public static void setReceivePoint(boolean b) {
        getConfig().receivePoint = b;
        hasChanged = true;
    }

    public static boolean receivePoint() {
        return getConfig().receivePoint;
    }

    public static void setOpenTreasureBox(boolean b) {
        getConfig().openTreasureBox = b;
        hasChanged = true;
    }

    public static boolean openTreasureBox() {
        return getConfig().openTreasureBox;
    }

    public static void setReceiveCoinAsset(boolean b) {
        getConfig().receiveCoinAsset = b;
        hasChanged = true;
    }

    public static boolean receiveCoinAsset() {
        return getConfig().receiveCoinAsset;
    }

    public static void setDonateCharityCoin(boolean b) {
        getConfig().donateCharityCoin = b;
        hasChanged = true;
    }

    public static boolean donateCharityCoin() {
        return getConfig().donateCharityCoin;
    }

    public static void setMinExchangeCount(int i) {
        getConfig().minExchangeCount = i;
        hasChanged = true;
    }

    public static int minExchangeCount() {
        return getConfig().minExchangeCount;
    }

    public static void setLatestExchangeTime(int i) {
        getConfig().latestExchangeTime = i;
        hasChanged = true;
    }

    public static int latestExchangeTime() {
        return getConfig().latestExchangeTime;
    }

    public static void setSyncStepCount(int i) {
        getConfig().syncStepCount = i;
        hasChanged = true;
    }

    public static int syncStepCount() {
        return getConfig().syncStepCount;
    }

    public static void setKbSginIn(boolean b) {
        getConfig().kbSignIn = b;
        hasChanged = true;
    }

    public static boolean kbSginIn() {
        return getConfig().kbSignIn;
    }

    public static void setEcoLifeTick(boolean b) {
        getConfig().ecoLifeTick = b;
        hasChanged = true;
    }

    public static boolean ecoLifeTick() {
        return getConfig().ecoLifeTick;
    }

    public static void setTiyubiz(boolean b) {
        getConfig().tiyubiz = b;
        hasChanged = true;
    }

    public static boolean tiyubiz() {
        return getConfig().tiyubiz;
    }

    public static void setInsBlueBeanExchange(boolean b) {
        getConfig().insBlueBeanExchange = b;
        hasChanged = true;
    }

    public static boolean insBlueBeanExchange() {
        return getConfig().insBlueBeanExchange;
    }

    /* base */
    private static Config getConfig() {
        if (config == null || shouldReload && config.immediateEffect) {
            shouldReload = false;
            String confJson = null;
            if (FileUtils.getConfigFile(FriendIdMap.currentUid).exists())
                confJson = FileUtils.readFromFile(FileUtils.getConfigFile(FriendIdMap.currentUid));
            config = json2Config(confJson);
        }
        return config;
    }

    public static Config defInit() {
        Config c = new Config();

        // c.forestPauseTime = 0L;
        c.immediateEffect = true;
        c.recordLog = true;
        c.showToast = true;
        c.toastOffsetY = 0;
        c.stayAwake = true;
        c.stayAwakeType = XposedHook.StayAwakeType.BROADCAST;
        c.stayAwakeTarget = XposedHook.StayAwakeTarget.SERVICE;
        c.timeoutRestart = true;
        c.timeoutType = XposedHook.StayAwakeType.ALARM;
        c.startAt7 = false;

        c.collectEnergy = true;
        c.collectWateringBubble = true;
        c.collectProp = true;
        c.checkInterval = 720_000;
        c.waitWhenException = 60 * 60 * 1000;
        c.limitCollect = true;
        c.limitCount = 50;
        c.doubleCard = false;
        c.doubleCardTime = new ArrayList<>();
        c.doubleCardTime.add("0700-0730");
        c.doubleCountLimit = 6;
        c.advanceTime = 0;
        c.collectInterval = 100;
        c.collectTimeout = 2_000;
        c.returnWater33 = 0;
        c.returnWater18 = 0;
        c.returnWater10 = 0;
        c.helpFriendCollect = true;
        if (c.dontCollectList == null)
            c.dontCollectList = new ArrayList<>();
        if (c.dontHelpCollectList == null)
            c.dontHelpCollectList = new ArrayList<>();
        c.receiveForestTaskAward = true;
        if (c.waterFriendList == null)
            c.waterFriendList = new ArrayList<>();
        if (c.waterCountList == null)
            c.waterCountList = new ArrayList<>();
        c.waterFriendCount = 66;
        c.cooperateWater = true;
        if (c.cooperateWaterList == null)
            c.cooperateWaterList = new ArrayList<>();
        if (c.cooperateWaterNumList == null)
            c.cooperateWaterNumList = new ArrayList<>();
        c.ancientTree = true;
        if (c.ancientTreeCityCodeList == null)
            c.ancientTreeCityCodeList = new ArrayList<>();
        c.reserve = true;
        if (c.reserveList == null)
            c.reserveList = new ArrayList<>();
        if (c.reserveCountList == null)
            c.reserveCountList = new ArrayList<>();
        c.beach = true;
        if (c.beachList == null)
            c.beachList = new ArrayList<>();
        if (c.beachCountList == null)
            c.beachCountList = new ArrayList<>();
        c.energyRain = true;
        if (c.giveEnergyRainList == null)
            c.giveEnergyRainList = new ArrayList<>();
        c.exchangeEnergyDoubleClick = false;
        c.exchangeEnergyDoubleClickCount = 6;
        c.ancientTreeOnlyWeek = true;
        c.antdodoCollect = true;
        c.antOcean = true;
        c.userPatrol = true;
        c.animalConsumeProp = true;

        c.enableFarm = true;
        c.rewardFriend = true;
        c.sendBackAnimal = true;
        c.sendType = SendType.HIT;
        if (c.dontSendFriendList == null)
            c.dontSendFriendList = new ArrayList<>();
        c.recallAnimalType = RecallAnimalType.ALWAYS;
        c.receiveFarmToolReward = true;
        c.recordFarmGame = true;
        c.kitchen = true;
        c.useNewEggTool = true;
        c.harvestProduce = true;
        c.donation = true;
        c.answerQuestion = true;
        c.receiveFarmTaskAward = true;
        c.feedAnimal = true;
        c.useAccelerateTool = true;
        if (c.feedFriendAnimalList == null)
            c.feedFriendAnimalList = new ArrayList<>();
        if (c.feedFriendCountList == null)
            c.feedFriendCountList = new ArrayList<>();
        c.doubleCardTime = new ArrayList<>();
        c.doubleCardTime.add("0700-2000");
        c.notifyFriend = true;
        if (c.dontNotifyFriendList == null)
            c.dontNotifyFriendList = new ArrayList<>();

        c.receivePoint = true;
        c.openTreasureBox = true;
        c.receiveCoinAsset = true;
        c.donateCharityCoin = false;
        c.kbSignIn = true;
        c.syncStepCount = 22000;
        c.ecoLifeTick = true;
        c.tiyubiz = true;
        c.insBlueBeanExchange = true;
        return c;
    }

    public static boolean saveConfigFile() {
        return FileUtils.write2File(config2Json(config), FileUtils.getConfigFile());
    }

    public static Config json2Config(String json) {
        Config config;
        try {
            JSONObject jo = new JSONObject(json);
            JSONArray ja, jaa;
            config = new Config();

            // config.forestPauseTime = jo.optLong(jn_pauseTime, 0L);

            config.immediateEffect = jo.optBoolean(jn_immediateEffect, true);

            config.recordLog = jo.optBoolean(jn_recordLog, true);

            config.showToast = jo.optBoolean(jn_showToast, true);

            config.toastOffsetY = jo.optInt(jn_toastOffsetY, 0);

            config.stayAwake = jo.optBoolean(jn_stayAwake, true);

            config.stayAwakeType = XposedHook.StayAwakeType
                    .valueOf(jo.optString(jn_stayAwakeType, XposedHook.StayAwakeType.BROADCAST.name()));

            config.stayAwakeTarget = XposedHook.StayAwakeTarget
                    .valueOf(jo.optString(jn_stayAwakeTarget, XposedHook.StayAwakeTarget.SERVICE.name()));

            config.timeoutRestart = jo.optBoolean(jn_timeoutRestart, true);

            config.timeoutType = XposedHook.StayAwakeType
                    .valueOf(jo.optString(jn_timeoutType, XposedHook.StayAwakeType.BROADCAST.name()));

            config.startAt7 = jo.optBoolean(jn_startAt7, false);

            /* forest */
            config.collectEnergy = jo.optBoolean(jn_collectEnergy, true);

            config.collectWateringBubble = jo.optBoolean(jn_collectWateringBubble, true);

            config.collectProp = jo.optBoolean(jn_collectProp, true);

            config.checkInterval = jo.optInt(jn_checkInterval, 720_000);

            config.waitWhenException = jo.optInt(jn_waitWhenException, 60 * 60 * 1000);

            config.limitCollect = jo.optBoolean("limitCollect", true);

            config.limitCount = jo.optInt("limitCount", 50);

            config.doubleCard = jo.optBoolean("doubleCard", false);

            config.doubleCardTime = Arrays.asList(jo.optString(jn_doubleCardTime, "0700-0730").split(","));

            config.doubleCountLimit = jo.optInt("doubleCountLimit", 6);

            config.advanceTime = jo.optInt(jn_advanceTime, 0);

            config.collectInterval = jo.optInt(jn_collectInterval, 100);

            config.collectTimeout = jo.optInt(jn_collectTimeout, 2_000);

            config.returnWater33 = jo.optInt(jn_ReturnWater33);

            config.returnWater18 = jo.optInt(jn_ReturnWater18);

            config.returnWater10 = jo.optInt(jn_ReturnWater10);

            config.helpFriendCollect = jo.optBoolean(jn_helpFriendCollect, true);

            config.dontCollectList = new ArrayList<>();
            if (jo.has(jn_dontCollectList)) {
                ja = jo.getJSONArray(jn_dontCollectList);
                for (int i = 0; i < ja.length(); i++) {
                    config.dontCollectList.add(ja.getString(i));
                }
            }

            config.dontHelpCollectList = new ArrayList<>();
            if (jo.has(jn_dontHelpCollectList)) {
                ja = jo.getJSONArray(jn_dontHelpCollectList);
                for (int i = 0; i < ja.length(); i++) {
                    config.dontHelpCollectList.add(ja.getString(i));
                }
            }

            config.receiveForestTaskAward = jo.optBoolean(jn_receiveForestTaskAward, true);

            config.waterFriendList = new ArrayList<>();
            config.waterCountList = new ArrayList<>();
            if (jo.has(jn_waterFriendList)) {
                ja = jo.getJSONArray(jn_waterFriendList);
                for (int i = 0; i < ja.length(); i++) {
                    if (ja.get(i) instanceof JSONArray) {
                        jaa = ja.getJSONArray(i);
                        config.waterFriendList.add(jaa.getString(0));
                        config.waterCountList.add(jaa.getInt(1));
                    } else {
                        config.waterFriendList.add(ja.getString(i));
                        config.waterCountList.add(3);
                    }
                }
            }

            config.waterFriendCount = jo.optInt(jn_waterFriendCount, 66);

            config.cooperateWater = jo.optBoolean(jn_cooperateWater, true);

            config.cooperateWaterList = new ArrayList<>();
            config.cooperateWaterNumList = new ArrayList<>();
            if (jo.has(jn_cooperateWaterList)) {
                ja = jo.getJSONArray(jn_cooperateWaterList);
                for (int i = 0; i < ja.length(); i++) {
                    jaa = ja.getJSONArray(i);
                    config.cooperateWaterList.add(jaa.getString(0));
                    config.cooperateWaterNumList.add(jaa.getInt(1));
                }
            }

            config.ancientTree = jo.optBoolean(jn_ancientTree, true);

            config.ancientTreeCityCodeList = new ArrayList<>();
            if (jo.has(jn_ancientTreeCityCodeList)) {
                ja = jo.getJSONArray(jn_ancientTreeCityCodeList);
                for (int i = 0; i < ja.length(); i++) {
                    jaa = ja.getJSONArray(i);
                    config.ancientTreeCityCodeList.add(jaa.getString(0));
                }
            }

            config.energyRain = jo.optBoolean(jn_energyRain, true);
            config.giveEnergyRainList = new ArrayList<>();
            if (jo.has(jn_giveEnergyRainList)) {
                ja = jo.getJSONArray(jn_giveEnergyRainList);
                for (int i = 0; i < ja.length(); i++) {
                    jaa = ja.getJSONArray(i);
                    config.giveEnergyRainList.add(jaa.getString(0));
                }
            }

            config.reserve = jo.optBoolean(jn_reserve, true);
            config.reserveList = new ArrayList<>();
            config.reserveCountList = new ArrayList<>();
            if (jo.has(jn_reserveList)) {
                ja = jo.getJSONArray(jn_reserveList);
                for (int i = 0; i < ja.length(); i++) {
                    if (ja.get(i) instanceof JSONArray) {
                        jaa = ja.getJSONArray(i);
                        config.reserveList.add(jaa.getString(0));
                        config.reserveCountList.add(jaa.getInt(1));
                    } else {
                        config.reserveList.add(ja.getString(i));
                        config.reserveCountList.add(2);
                    }
                }
            }

            config.beach = jo.optBoolean(jn_beach, true);
            config.beachList = new ArrayList<>();
            config.beachCountList = new ArrayList<>();
            if (jo.has(jn_beachList)) {
                ja = jo.getJSONArray(jn_beachList);
                for (int i = 0; i < ja.length(); i++) {
                    if (ja.get(i) instanceof JSONArray) {
                        jaa = ja.getJSONArray(i);
                        config.beachList.add(jaa.getString(0));
                        config.beachCountList.add(jaa.getInt(1));
                    } else {
                        config.beachList.add(ja.getString(i));
                        config.beachCountList.add(2);
                    }
                }
            }

            config.exchangeEnergyDoubleClick = jo.optBoolean("exchangeEnergyDoubleClick", false);

            config.exchangeEnergyDoubleClickCount = jo.optInt("exchangeEnergyDoubleClickCount", 6);

            config.ancientTreeOnlyWeek = jo.optBoolean(jn_ancientTreeOnlyWeek, true);

            config.antdodoCollect = jo.optBoolean(jn_antdodoCollect, true);

            config.antOcean = jo.optBoolean(jn_antOcean, true);

            config.userPatrol = jo.optBoolean(jn_userPatrol, true);

            config.animalConsumeProp = jo.optBoolean(jn_animalConsumeProp, true);

            /* farm */
            config.enableFarm = jo.optBoolean(jn_enableFarm, true);

            config.rewardFriend = jo.optBoolean(jn_rewardFriend, true);

            config.sendBackAnimal = jo.optBoolean(jn_sendBackAnimal, true);

            config.sendType = SendType.valueOf(jo.optString(jn_sendType, SendType.HIT.name()));

            config.dontSendFriendList = new ArrayList<>();
            if (jo.has(jn_dontSendFriendList)) {
                ja = jo.getJSONArray(jn_dontSendFriendList);
                for (int i = 0; i < ja.length(); i++) {
                    config.dontSendFriendList.add(ja.getString(i));
                }
            }

            config.recallAnimalType = RecallAnimalType
                    .valueOf(jo.optString(jn_recallAnimalType, RecallAnimalType.ALWAYS.name()));

            config.receiveFarmToolReward = jo.optBoolean(jn_receiveFarmToolReward, true);

            config.recordFarmGame = jo.optBoolean(jn_recordFarmGame, true);

            config.kitchen = jo.optBoolean(jn_kitchen, true);

            config.useNewEggTool = jo.optBoolean(jn_useNewEggTool, true);

            config.harvestProduce = jo.optBoolean(jn_harvestProduce, true);

            config.donation = jo.optBoolean(jn_donation, true);

            config.answerQuestion = jo.optBoolean(jn_answerQuestion, true);

            config.receiveFarmTaskAward = jo.optBoolean(jn_receiveFarmTaskAward, true);

            config.feedAnimal = jo.optBoolean(jn_feedAnimal, true);

            config.useAccelerateTool = jo.optBoolean(jn_useAccelerateTool, true);

            config.feedFriendAnimalList = new ArrayList<>();
            config.feedFriendCountList = new ArrayList<>();
            if (jo.has(jn_feedFriendAnimalList)) {
                ja = jo.getJSONArray(jn_feedFriendAnimalList);
                for (int i = 0; i < ja.length(); i++) {
                    if (ja.get(i) instanceof JSONArray) {
                        jaa = ja.getJSONArray(i);
                        config.feedFriendAnimalList.add(jaa.getString(0));
                        config.feedFriendCountList.add(jaa.getInt(1));
                    } else {
                        config.feedFriendAnimalList.add(ja.getString(i));
                        config.feedFriendCountList.add(1);
                    }
                }
            }

            config.farmGameTime = Arrays.asList(jo.optString(jn_farmGameTime, "2200-2400").split(","));

            config.animalSleepTime = Arrays.asList(jo.optString(jn_animalSleepTime, "2200-2400,0000-0559").split(","));

            config.notifyFriend = jo.optBoolean(jn_notifyFriend, true);

            config.dontNotifyFriendList = new ArrayList<>();
            if (jo.has(jn_dontNotifyFriendList)) {
                ja = jo.getJSONArray(jn_dontNotifyFriendList);
                for (int i = 0; i < ja.length(); i++) {
                    config.dontNotifyFriendList.add(ja.getString(i));
                }
            }

            /* other */
            config.receivePoint = jo.optBoolean(jn_receivePoint, true);

            config.openTreasureBox = jo.optBoolean(jn_openTreasureBox, true);

            config.receiveCoinAsset = jo.optBoolean(jn_receiveCoinAsset, true);

            config.donateCharityCoin = jo.optBoolean(jn_donateCharityCoin, false);

            config.minExchangeCount = jo.optInt(jn_minExchangeCount);

            config.latestExchangeTime = jo.optInt(jn_latestExchangeTime, 21);

            config.syncStepCount = jo.optInt(jn_syncStepCount, 22000);

            config.kbSignIn = jo.optBoolean(jn_kbSignIn, true);

            config.ecoLifeTick = jo.optBoolean(jn_ecoLifeTick, true);

            config.tiyubiz = jo.optBoolean(jn_tiyubiz, true);

            config.insBlueBeanExchange = jo.optBoolean(jn_insBlueBeanExchange, true);

        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
            if (json != null) {
                Log.i(TAG, "配置文件格式有误，已重置配置文件并备份原文件");
                FileUtils.write2File(json, FileUtils.getBackupFile(FileUtils.getConfigFile()));
            }
            config = defInit();
        }
        String formated = config2Json(config);
        if (!formated.equals(json)) {
            Log.i(TAG, "重新格式化 config.json");
            FileUtils.write2File(formated, FileUtils.getConfigFile());
        }
        return config;
    }

    public static String config2Json(Config config) {
        JSONObject jo = new JSONObject();
        JSONArray ja, jaa;
        try {
            if (config == null)
                config = Config.defInit();

            // jo.put(jn_pauseTime, config.forestPauseTime);

            jo.put(jn_immediateEffect, config.immediateEffect);

            jo.put(jn_recordLog, config.recordLog);

            jo.put(jn_showToast, config.showToast);

            jo.put(jn_toastOffsetY, config.toastOffsetY);

            jo.put(jn_stayAwake, config.stayAwake);

            jo.put(jn_stayAwakeType, config.stayAwakeType);

            jo.put(jn_stayAwakeTarget, config.stayAwakeTarget);

            jo.put(jn_timeoutRestart, config.timeoutRestart);

            jo.put(jn_timeoutType, config.timeoutType);

            jo.put(jn_startAt7, config.startAt7);

            /* forest */
            jo.put(jn_collectEnergy, config.collectEnergy);

            jo.put(jn_collectWateringBubble, config.collectWateringBubble);

            jo.put(jn_collectProp, config.collectProp);

            jo.put(jn_checkInterval, config.checkInterval);

            jo.put(jn_waitWhenException, config.waitWhenException);

            jo.put("limitCollect", config.limitCollect);

            jo.put("limitCount", config.limitCount);

            jo.put(jn_doubleCard, config.doubleCard);

            jo.put(jn_doubleCardTime, String.join(",", config.doubleCardTime));

            jo.put("doubleCountLimit", config.doubleCountLimit);

            jo.put(jn_advanceTime, config.advanceTime);

            jo.put(jn_collectInterval, config.collectInterval);

            jo.put(jn_collectTimeout, config.collectTimeout);

            jo.put(jn_ReturnWater33, config.returnWater33);

            jo.put(jn_ReturnWater18, config.returnWater18);

            jo.put(jn_ReturnWater10, config.returnWater10);

            jo.put(jn_helpFriendCollect, config.helpFriendCollect);

            ja = new JSONArray();
            for (String s : config.dontCollectList) {
                ja.put(s);
            }
            jo.put(jn_dontCollectList, ja);

            ja = new JSONArray();
            for (String s : config.dontHelpCollectList) {
                ja.put(s);
            }
            jo.put(jn_dontHelpCollectList, ja);

            jo.put(jn_receiveForestTaskAward, config.receiveForestTaskAward);

            ja = new JSONArray();
            for (int i = 0; i < config.waterFriendList.size(); i++) {
                jaa = new JSONArray();
                jaa.put(config.waterFriendList.get(i));
                jaa.put(config.waterCountList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_waterFriendList, ja);

            jo.put(jn_waterFriendCount, config.waterFriendCount);

            jo.put(jn_cooperateWater, config.cooperateWater);

            ja = new JSONArray();
            for (int i = 0; i < config.cooperateWaterList.size(); i++) {
                jaa = new JSONArray();
                jaa.put(config.cooperateWaterList.get(i));
                jaa.put(config.cooperateWaterNumList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_cooperateWaterList, ja);

            jo.put(jn_ancientTree, config.ancientTree);

            ja = new JSONArray();
            for (int i = 0; i < config.ancientTreeCityCodeList.size(); i++) {
                jaa = new JSONArray();
                jaa.put(config.ancientTreeCityCodeList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_ancientTreeCityCodeList, ja);

            jo.put(jn_reserve, config.reserve);

            ja = new JSONArray();
            for (int i = 0; i < config.reserveList.size(); i++) {
                jaa = new JSONArray();
                jaa.put(config.reserveList.get(i));
                jaa.put(config.reserveCountList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_reserveList, ja);

            jo.put(jn_beach, config.beach);

            ja = new JSONArray();
            for (int i = 0; i < config.beachList.size(); i++) {
                jaa = new JSONArray();
                jaa.put(config.beachList.get(i));
                jaa.put(config.beachCountList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_beachList, ja);

            jo.put(jn_energyRain, config.energyRain);
            ja = new JSONArray();
            for (int i = 0; i < config.giveEnergyRainList.size(); i++) {
                jaa = new JSONArray();
                jaa.put(config.giveEnergyRainList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_giveEnergyRainList, ja);

            jo.put("exchangeEnergyDoubleClick", config.exchangeEnergyDoubleClick);

            jo.put("exchangeEnergyDoubleClickCount", config.exchangeEnergyDoubleClickCount);

            jo.put(jn_ancientTreeOnlyWeek, config.ancientTreeOnlyWeek);

            jo.put(jn_antdodoCollect, config.antdodoCollect);

            jo.put(jn_antOcean, config.antOcean);

            jo.put(jn_userPatrol, config.userPatrol);

            jo.put(jn_animalConsumeProp, config.animalConsumeProp);

            /* farm */
            jo.put(jn_enableFarm, config.enableFarm);

            jo.put(jn_rewardFriend, config.rewardFriend);

            jo.put(jn_sendBackAnimal, config.sendBackAnimal);

            jo.put(jn_sendType, config.sendType.name());

            ja = new JSONArray();
            for (String s : config.dontSendFriendList) {
                ja.put(s);
            }
            jo.put(jn_dontSendFriendList, ja);

            jo.put(jn_recallAnimalType, config.recallAnimalType);

            jo.put(jn_receiveFarmToolReward, config.receiveFarmToolReward);

            jo.put(jn_recordFarmGame, config.recordFarmGame);

            jo.put(jn_kitchen, config.kitchen);

            jo.put(jn_useNewEggTool, config.useNewEggTool);

            jo.put(jn_harvestProduce, config.harvestProduce);

            jo.put(jn_donation, config.donation);

            jo.put(jn_answerQuestion, config.answerQuestion);

            jo.put(jn_receiveFarmTaskAward, config.receiveFarmTaskAward);

            jo.put(jn_feedAnimal, config.feedAnimal);

            jo.put(jn_useAccelerateTool, config.useAccelerateTool);

            ja = new JSONArray();
            for (int i = 0; i < config.feedFriendAnimalList.size(); i++) {
                jaa = new JSONArray();
                jaa.put(config.feedFriendAnimalList.get(i));
                jaa.put(config.feedFriendCountList.get(i));
                ja.put(jaa);
            }
            jo.put(jn_feedFriendAnimalList, ja);

            jo.put(jn_farmGameTime, String.join(",", config.farmGameTime));

            jo.put(jn_animalSleepTime, String.join(",", config.animalSleepTime));

            jo.put(jn_notifyFriend, config.notifyFriend);

            ja = new JSONArray();
            for (String s : config.dontNotifyFriendList) {
                ja.put(s);
            }
            jo.put(jn_dontNotifyFriendList, ja);

            /* other */
            jo.put(jn_receivePoint, config.receivePoint);

            jo.put(jn_openTreasureBox, config.openTreasureBox);

            jo.put(jn_receiveCoinAsset, config.receiveCoinAsset);

            jo.put(jn_donateCharityCoin, config.donateCharityCoin);

            jo.put(jn_minExchangeCount, config.minExchangeCount);

            jo.put(jn_latestExchangeTime, config.latestExchangeTime);

            jo.put(jn_syncStepCount, config.syncStepCount);

            jo.put(jn_kbSignIn, config.kbSignIn);

            jo.put(jn_ecoLifeTick, config.ecoLifeTick);

            jo.put(jn_tiyubiz, config.tiyubiz);

            jo.put(jn_insBlueBeanExchange, config.insBlueBeanExchange);

        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
        return formatJson(jo, false);
    }

    public static String formatJson(JSONObject jo, boolean removeQuote) {
        String formated;
        try {
            formated = jo.toString(4);
        } catch (Throwable t) {
            return jo.toString();
        }
        if (!removeQuote)
            return formated;
        StringBuilder sb = new StringBuilder(formated);
        char currentChar, lastNonSpaceChar = 0;
        for (int i = 0; i < sb.length(); i++) {
            currentChar = sb.charAt(i);
            switch (currentChar) {
                case '"':
                    switch (lastNonSpaceChar) {
                        case ':':
                        case '[':
                            sb.deleteCharAt(i);
                            i = sb.indexOf("\"", i);
                            sb.deleteCharAt(i);
                            if (lastNonSpaceChar != '[')
                                lastNonSpaceChar = sb.charAt(--i);
                    }
                    break;

                case ' ':
                    break;

                default:
                    if (lastNonSpaceChar == '[' && currentChar != ']')
                        break;
                    lastNonSpaceChar = currentChar;
            }
        }
        formated = sb.toString();
        return formated;
    }

    private static PendingIntent alarm7Pi;

    private static PendingIntent getAlarm7Pi(Context context) {
        if (alarm7Pi == null) {
            Intent it = new Intent();
            it.setClassName(ClassMember.PACKAGE_NAME, ClassMember.CURRENT_USING_ACTIVITY);
            int piFlag;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                piFlag = PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
            } else {
                piFlag = PendingIntent.FLAG_UPDATE_CURRENT;
            }
            alarm7Pi = PendingIntent.getActivity(context, 999, it, piFlag);
        }
        return alarm7Pi;
    }

    public static void setAlarm7(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = getAlarm7Pi(context);
            Calendar calendar = Calendar.getInstance();
            // calendar.add(Calendar.SECOND, 10);
            if (calendar.get(Calendar.HOUR_OF_DAY) >= 7) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
            }
            // alarmManager.setAlarmClock(new
            // AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), pi);
        } catch (Throwable th) {
            Log.printStackTrace("alarm7", th);
        }
    }

    public static void cancelAlarm7(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = getAlarm7Pi(context);
            alarmManager.cancel(pi);

            context.sendBroadcast(new Intent("com.eg.android.AlipayGphone.xqe.cancelAlarm7"));
        } catch (Throwable th) {
            Log.printStackTrace("alarm7", th);
        }
    }

}
