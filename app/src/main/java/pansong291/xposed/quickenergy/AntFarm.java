package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntFarmRpcCall;
import pansong291.xposed.quickenergy.hook.DadaDailyRpcCall;
import pansong291.xposed.quickenergy.util.*;

import java.util.HashSet;
import java.util.Set;

public class AntFarm {
    private static final String TAG = AntFarm.class.getCanonicalName();

    public enum SendType {
        HIT, NORMAL;
        public static final CharSequence[] nickNames = {"攻击", "常规"};
        public static final CharSequence[] names = {HIT.nickName(), NORMAL.nickName()};
        public CharSequence nickName() {
            return nickNames[ordinal()];
        }
    }
    public enum AnimalBuff { ACCELERATING, INJURED, NONE }
    public enum AnimalFeedStatus { HUNGRY, EATING, SLEEPY}
    public enum AnimalInteractStatus { HOME, GOTOSTEAL, STEALING }
    public enum SubAnimalType { NORMAL, GUEST, PIRATE, WORK }
    public enum TaskStatus { TODO, FINISHED, RECEIVED }
    public enum ToolType {
        STEALTOOL, ACCELERATETOOL, SHARETOOL, FENCETOOL, NEWEGGTOOL;
        public static final CharSequence[] nickNames =
                {"蹭饭卡", "加速卡", "救济卡", "篱笆卡", "新蛋卡"};
        public CharSequence nickName() {
            return nickNames[ordinal()];
        }
    }

    public enum GameType {
        starGame, jumpGame;

        public static final CharSequence[] gameNames = { "星星球", "登山赛" };

        public CharSequence gameName() {
            return gameNames[ordinal()];
        }
    }

    private static class Animal {
        public String animalId, currentFarmId, masterFarmId,
                animalBuff, subAnimalType, animalFeedStatus, animalInteractStatus;
        public String locationType;

        public String currentFarmMasterUserId;
    }

    private static class RewardFriend {
        public String consistencyKey, friendId, time;
    }

    /**private static class FarmTool
     {
     public ToolType toolType;
     public String toolId;
     public int toolCount, toolHoldLimit;
     }/**/

    private static String ownerFarmId;
    private static String userId;
    private static Animal[] animals;
    private static Animal ownerAnimal;
    private static int foodStock;
    private static int foodStockLimit;
    private static String rewardProductNum;
    private static RewardFriend[] rewardList;
    private static double benevolenceScore;
    private static double harvestBenevolenceScore;
    private static int unreceiveTaskAward = 0;

    // private static FarmTool[] farmTools;

    public static void start() {
        if (!Config.enableFarm())
            return;
        new Thread() {

            @Override
            public void run() {
                try {
                    while (FriendIdMap.currentUid == null || FriendIdMap.currentUid.isEmpty())
                        Thread.sleep(100);
                    String s = AntFarmRpcCall.enterFarm("", FriendIdMap.currentUid);
                    if (s == null) {
                        Thread.sleep(RandomUtils.delay());
                        s = AntFarmRpcCall.enterFarm("", FriendIdMap.currentUid);
                    }
                    JSONObject jo = new JSONObject(s);
                    if ("SUCCESS".equals(jo.getString("memo"))) {
                        rewardProductNum = jo.getJSONObject("dynamicGlobalConfig").getString("rewardProductNum");
                        JSONObject joFarmVO = jo.getJSONObject("farmVO");
                        foodStock = joFarmVO.getInt("foodStock");
                        foodStockLimit = joFarmVO.getInt("foodStockLimit");
                        harvestBenevolenceScore = joFarmVO.getDouble("harvestBenevolenceScore");
                        parseSyncAnimalStatusResponse(joFarmVO.toString());
                        userId = joFarmVO.getJSONObject("masterUserInfoVO").getString("userId");
                        JSONArray cuisineList = jo.getJSONArray("cuisineList");
                        if (!AnimalFeedStatus.SLEEPY.name().equals(ownerAnimal.animalFeedStatus))
                            useFarmFood(cuisineList);
                        if (jo.has("lotteryPlusInfo")) {
                            drawLotteryPlus(jo.getJSONObject("lotteryPlusInfo"));
                        }
                    } else {
                        Log.recordLog("", s);
                    }

                    if (Config.rewardFriend())
                        rewardFriend();

                    if (Config.sendBackAnimal())
                        sendBackAnimal();

                    if (!AnimalInteractStatus.HOME.name().equals(ownerAnimal.animalInteractStatus)) {
                        syncAnimalStatusAtOtherFarm(ownerAnimal.currentFarmId);

                        if ("ORCHARD".equals(ownerAnimal.locationType)) {
                            Log.recordLog("小鸡到好友家除草了", "");
                            JSONObject joRecallAnimal = new JSONObject(AntFarmRpcCall
                                    .orchardRecallAnimal(ownerAnimal.animalId, ownerAnimal.currentFarmMasterUserId));

                            int manureCount = joRecallAnimal.getInt("manureCount");
                            Log.recordLog("", "召回小鸡，收获肥料" + manureCount + "g");
                        } else {
                            boolean guest = false;
                            switch (SubAnimalType.valueOf(ownerAnimal.subAnimalType)) {
                                case GUEST:
                                    guest = true;
                                    Log.recordLog("小鸡到好友家去做客了", "");
                                    break;
                                case NORMAL:
                                    Log.recordLog("小鸡太饿，离家出走了", "");
                                    break;
                                case PIRATE:
                                    Log.recordLog("小鸡外出探险了", "");
                                    break;
                                case WORK:
                                    Log.recordLog("小鸡出去工作啦", "");
                                    break;
                                default:
                                    Log.recordLog("小鸡不在庄园", ownerAnimal.subAnimalType);
                            }

                            boolean hungry = false;
                            String userName = FriendIdMap
                                    .getNameById(AntFarmRpcCall.farmId2UserId(ownerAnimal.currentFarmId));
                            switch (AnimalFeedStatus.valueOf(ownerAnimal.animalFeedStatus)) {
                                case HUNGRY:
                                    hungry = true;
                                    Log.recordLog("小鸡在[" + userName + "]的庄园里挨饿", "");
                                    break;

                                case EATING:
                                    Log.recordLog("小鸡在[" + userName + "]的庄园里吃得津津有味", "");
                                    break;
                            }

                            boolean recall = false;
                            switch (Config.recallAnimalType()) {
                                case ALWAYS:
                                    recall = true;
                                    break;
                                case WHEN_THIEF:
                                    recall = !guest;
                                    break;
                                case WHEN_HUNGRY:
                                    recall = hungry;
                                    break;
                            }
                            if (recall) {
                                recallAnimal(ownerAnimal.animalId, ownerAnimal.currentFarmId, ownerFarmId, userName);
                                syncAnimalStatus(ownerFarmId);
                            }
                        }

                    }

                    // if(Config.receiveFarmToolReward())
                    // {
                    // receiveToolTaskReward(loader);
                    // }

                    if (Config.recordFarmGame() && Config.isFarmGameTime()) {
                        recordFarmGame(GameType.starGame);
                        recordFarmGame(GameType.jumpGame);
                    }

                    if (Config.kitchen()) {
                        collectDailyFoodMaterial(userId);
                        collectDailyLimitedFoodMaterial();
                        cook(userId);
                    }

                    if (Config.useNewEggTool()) {
                        useFarmTool(ownerFarmId, ToolType.NEWEGGTOOL);
                        syncAnimalStatus(ownerFarmId);
                    }

                    if (Config.harvestProduce() && benevolenceScore >= 1) {
                        Log.recordLog("有可收取的爱心鸡蛋", "");
                        harvestProduce(ownerFarmId);
                    }

                    if (Config.donation() && Statistics.canDonationEgg(userId) && harvestBenevolenceScore >= 1) {
                        donation();
                    }

                    if (Config.answerQuestion() && Statistics.canAnswerQuestionToday(FriendIdMap.currentUid)) {
                        answerQuestion();
                    }

                    if (Config.receiveFarmTaskAward()) {
                        doFarmDailyTask();
                        receiveFarmTaskAward();
                    }

                    if (AnimalInteractStatus.HOME.name().equals(ownerAnimal.animalInteractStatus)) {
                        if (Config.feedAnimal()
                                && AnimalFeedStatus.HUNGRY.name().equals(ownerAnimal.animalFeedStatus)) {
                            Log.recordLog("小鸡在挨饿", "");
                            feedAnimal(ownerFarmId);
                            // syncAnimalStatus(loader,ownerFarmId);
                        }

                        if (AnimalBuff.ACCELERATING.name().equals(ownerAnimal.animalBuff)) {
                            Log.recordLog("小鸡正双手并用着加速吃饲料", "");
                        } else if (Config.useAccelerateTool()
                                && !AnimalFeedStatus.HUNGRY.name().equals(ownerAnimal.animalFeedStatus)) {
                            // 加速卡
                            useFarmTool(ownerFarmId, ToolType.ACCELERATETOOL);
                        }

                        if (unreceiveTaskAward > 0) {
                            Log.recordLog("还有待领取的饲料", "");
                            receiveFarmTaskAward();
                        }

                    }

                    // 帮好友喂鸡
                    feedFriend();

                    // 通知好友赶鸡
                    if (Config.notifyFriend())
                        notifyFriend();

                    if (!StringUtil.isEmpty(Config.animalSleepTime())) {
                        if (Config.isAnimalSleepTime()) {
                            animalSleep();
                        }
                    }

                } catch (Throwable t) {
                    Log.i(TAG, "AntFarm.start.run err:");
                    Log.printStackTrace(TAG, t);
                }
                FriendIdMap.saveIdMap();
            }
        }.start();

    }

    private static void animalSleep() {
        try {
            String s = AntFarmRpcCall.queryLoveCabin(FriendIdMap.currentUid);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("memo"))) {
                JSONObject sleepNotifyInfo = jo.getJSONObject("sleepNotifyInfo");
                if (sleepNotifyInfo.getBoolean("canSleep")) {
                    s = AntFarmRpcCall.sleep();
                    jo = new JSONObject(s);
                    if ("SUCCESS".equals(jo.getString("memo"))) {
                        Log.farm("小鸡睡觉🛌");
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "sleep err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void syncAnimalStatus(String farmId) {
        try {
            String s = AntFarmRpcCall.syncAnimalStatus(farmId);
            parseSyncAnimalStatusResponse(s);
        } catch (Throwable t) {
            Log.i(TAG, "syncAnimalStatus err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void syncAnimalStatusAtOtherFarm(String farmId) {
        try {
            String s = AntFarmRpcCall.enterFarm(farmId, "");
            JSONObject jo = new JSONObject(s);
            jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
            JSONArray jaAnimals = jo.getJSONArray("animals");
            for (int i = 0; i < jaAnimals.length(); i++) {
                jo = jaAnimals.getJSONObject(i);
                if (jo.getString("masterFarmId").equals(ownerFarmId)) {
                    if (ownerAnimal == null)
                        ownerAnimal = new Animal();
                    JSONObject animal = jaAnimals.getJSONObject(i);
                    ownerAnimal.animalId = animal.getString("animalId");
                    ownerAnimal.currentFarmId = animal.getString("currentFarmId");
                    ownerAnimal.currentFarmMasterUserId = animal.getString("currentFarmMasterUserId");
                    ownerAnimal.masterFarmId = ownerFarmId;
                    ownerAnimal.animalBuff = animal.getString("animalBuff");
                    ownerAnimal.locationType = animal.optString("locationType", "");
                    ownerAnimal.subAnimalType = animal.getString("subAnimalType");
                    animal = animal.getJSONObject("animalStatusVO");
                    ownerAnimal.animalFeedStatus = animal.getString("animalFeedStatus");
                    ownerAnimal.animalInteractStatus = animal.getString("animalInteractStatus");
                    break;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "syncAnimalStatusAtOtherFarm err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void rewardFriend() {
        try {
            if (rewardList != null) {
                for (RewardFriend rewardFriend : rewardList) {
                    String s = AntFarmRpcCall.rewardFriend(rewardFriend.consistencyKey, rewardFriend.friendId,
                            rewardProductNum, rewardFriend.time);
                    JSONObject jo = new JSONObject(s);
                    String memo = jo.getString("memo");
                    if ("SUCCESS".equals(memo)) {
                        double rewardCount = benevolenceScore - jo.getDouble("farmProduct");
                        benevolenceScore -= rewardCount;
                        Log.farm("打赏好友💰[" + FriendIdMap.getNameById(rewardFriend.friendId) + "]#得" + rewardCount
                                + "颗爱心鸡蛋");
                    } else {
                        Log.recordLog(memo, s);
                    }
                }
                rewardList = null;
            }
        } catch (Throwable t) {
            Log.i(TAG, "rewardFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void recallAnimal(String animalId, String currentFarmId, String masterFarmId, String user) {
        try {
            String s = AntFarmRpcCall.recallAnimal(animalId, currentFarmId, masterFarmId);
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if ("SUCCESS".equals(memo)) {
                double foodHaveStolen = jo.getDouble("foodHaveStolen");
                Log.farm("召回小鸡📣，偷吃[" + user + "]#" + foodHaveStolen + "g");
                // 这里不需要加
                // add2FoodStock((int)foodHaveStolen);
            } else {
                Log.recordLog(memo, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "recallAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void sendBackAnimal() {
        if (animals == null) {
            return;
        }
        try {
            for (Animal animal : animals) {
                if (AnimalInteractStatus.STEALING.name().equals(animal.animalInteractStatus)
                        && !SubAnimalType.GUEST.name().equals(animal.subAnimalType)
                        && !SubAnimalType.WORK.name().equals(animal.subAnimalType)) {
                    // 赶鸡
                    String user = AntFarmRpcCall.farmId2UserId(animal.masterFarmId);
                    if (Config.getDontSendFriendList().contains(user))
                        continue;
                    SendType sendType = Config.sendType();
                    user = FriendIdMap.getNameById(user);
                    String s = AntFarmRpcCall.sendBackAnimal(
                            sendType.name(), animal.animalId,
                            animal.currentFarmId, animal.masterFarmId);
                    JSONObject jo = new JSONObject(s);
                    String memo = jo.getString("memo");
                    if ("SUCCESS".equals(memo)) {
                        if (sendType == SendType.HIT) {
                            if (jo.has("hitLossFood")) {
                                s = "胖揍小鸡🤺[" + user + "]，掉落[" + jo.getInt("hitLossFood") + "g]";
                                if (jo.has("finalFoodStorage"))
                                    foodStock = jo.getInt("finalFoodStorage");
                            } else
                                s = "[" + user + "]的小鸡躲开了攻击";
                        } else {
                            s = "驱赶小鸡🧶[" + user + "]";
                        }
                        Log.farm(s);
                    } else {
                        Log.recordLog(memo, s);
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "sendBackAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveToolTaskReward() {
        try {
            String s = AntFarmRpcCall.listToolTaskDetails();
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if ("SUCCESS".equals(memo)) {
                JSONArray jaList = jo.getJSONArray("list");
                for (int i = 0; i < jaList.length(); i++) {
                    jo = jaList.getJSONObject(i);
                    if (jo.has("taskStatus")
                            && TaskStatus.FINISHED.name().equals(jo.getString("taskStatus"))) {
                        int awardCount = jo.getInt("awardCount");
                        String awardType = jo.getString("awardType");
                        ToolType toolType = ToolType.valueOf(awardType);
                        String taskType = jo.getString("taskType");
                        jo = new JSONObject(jo.getString("bizInfo"));
                        String taskTitle = jo.getString("taskTitle");
                        s = AntFarmRpcCall.receiveToolTaskReward(awardType, awardCount, taskType);
                        jo = new JSONObject(s);
                        memo = jo.getString("memo");
                        if ("SUCCESS".equals(memo)) {
                            Log.farm("领取道具🎖️[" + taskTitle + "-" + toolType.nickName() + "]#" + awardCount + "张");
                        } else {
                            memo = memo.replace("道具", toolType.nickName());
                            Log.recordLog(memo, s);
                        }
                    }
                }
            } else {
                Log.recordLog(memo, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveToolTaskReward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void harvestProduce(String farmId) {
        try {
            String s = AntFarmRpcCall.harvestProduce(farmId);
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if ("SUCCESS".equals(memo)) {
                double harvest = jo.getDouble("harvestBenevolenceScore");
                harvestBenevolenceScore = jo.getDouble("finalBenevolenceScore");
                Log.farm("收取鸡蛋🥚[" + harvest + "颗]#剩余" + harvestBenevolenceScore + "颗");
            } else {
                Log.recordLog(memo, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "harvestProduce err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void donation() {
        try {
            String s = AntFarmRpcCall.listActivityInfo();
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if ("SUCCESS".equals(memo)) {
                JSONArray jaActivityInfos = jo.getJSONArray("activityInfos");
                String activityId = null, activityName = null;
                for (int i = 0; i < jaActivityInfos.length(); i++) {
                    jo = jaActivityInfos.getJSONObject(i);
                    if (!jo.get("donationTotal").equals(jo.get("donationLimit"))) {
                        activityId = jo.getString("activityId");
                        activityName = jo.optString("projectName", activityId);
                        break;
                    }
                }
                if (activityId == null) {
                    Log.recordLog("今日已无可捐赠的活动", "");
                } else {
                    s = AntFarmRpcCall.donation(activityId, 1);
                    jo = new JSONObject(s);
                    memo = jo.getString("memo");
                    if ("SUCCESS".equals(memo)) {
                        jo = jo.getJSONObject("donation");
                        harvestBenevolenceScore = jo.getDouble("harvestBenevolenceScore");
                        Log.farm("捐赠活动❤️[" + activityName + "]#累计捐赠" + jo.getInt("donationTimesStat") + "次");
                        Statistics.donationEgg(userId);
                    } else {
                        Log.recordLog(memo, s);
                    }
                }
            } else {
                Log.recordLog(memo, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "donation err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void answerQuestion() {
        try {
            String s = AntFarmRpcCall.listFarmTask();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("memo"))) {
                JSONArray jaFarmTaskList = jo.getJSONArray("farmTaskList");
                for (int i = 0; i < jaFarmTaskList.length(); i++) {
                    jo = jaFarmTaskList.getJSONObject(i);
                    if ("庄园小课堂".equals(jo.getString("title"))) {
                        switch (TaskStatus.valueOf((jo.getString("taskStatus")))) {
                            case TODO:
                                s = DadaDailyRpcCall.home("100");
                                jo = new JSONObject(s);
                                if (jo.getBoolean("success")) {
                                    JSONObject question = jo.getJSONObject("question");
                                    Log.i("题目:" + question, "");
                                    long questionId = question.getLong("questionId");
                                    JSONArray labels = question.getJSONArray("label");
                                    String answer = null;
                                    String anotherAnswer = null;
                                    boolean existsResult = false;
                                    Set<String> dadaDailySet = Statistics.getDadaDailySet();
                                    if (dadaDailySet.contains(TimeUtil.getDateStr() + labels.getString(0))) {
                                        answer = labels.getString(0);
                                        anotherAnswer = labels.getString(1);
                                        existsResult = true;
                                    } else if (dadaDailySet.contains(TimeUtil.getDateStr() + labels.getString(1))) {
                                        answer = labels.getString(1);
                                        anotherAnswer = labels.getString(0);
                                        existsResult = true;
                                    }
                                    if (!existsResult) {
                                        answer = labels.getString(0);
                                        anotherAnswer = labels.getString(1);
                                    }

                                    s = DadaDailyRpcCall.submit("100", answer, questionId);
                                    JSONObject joDailySubmit = new JSONObject(s);
                                    if (joDailySubmit.getBoolean("success")) {
                                        Log.recordLog("提交结果", s);
                                        dadaDailySet = new HashSet<>();
                                        JSONObject extInfo = joDailySubmit.getJSONObject("extInfo");
                                        boolean correct = joDailySubmit.getBoolean("correct");
                                        if (!correct || !existsResult) {
                                            dadaDailySet.add(TimeUtil.getDateStr() + anotherAnswer);
                                        } else {
                                            dadaDailySet.add(TimeUtil.getDateStr() + answer);
                                        }
                                        Log.recordLog("答题" + (correct ? "正确" : "错误") + "可领取［"
                                                + extInfo.getString("award") + "克］");
                                        Statistics.answerQuestionToday(FriendIdMap.currentUid);

                                        JSONArray operationConfigList = joDailySubmit
                                                .getJSONArray("operationConfigList");
                                        for (int j = 0; j < operationConfigList.length(); j++) {
                                            JSONObject operationConfig = operationConfigList.getJSONObject(j);
                                            if ("PREVIEW_QUESTION".equals(operationConfig.getString("type"))) {
                                                JSONArray actionTitle = new JSONArray(
                                                        operationConfig.getString("actionTitle"));
                                                for (int k = 0; k < actionTitle.length(); k++) {
                                                    JSONObject joActionTitle = actionTitle.getJSONObject(k);
                                                    if (joActionTitle.getBoolean("correct")) {
                                                        dadaDailySet.add(TimeUtil.getDateStr(1)
                                                                + joActionTitle.getString("title"));
                                                    }
                                                }
                                            }
                                        }
                                        Statistics.setDadaDailySet(dadaDailySet);
                                    } else {
                                        Log.recordLog(TAG, s);
                                    }
                                    return;
                                } else {
                                    Log.recordLog(TAG, s);
                                }
                                break;

                            case RECEIVED:
                                Statistics.setQuestionHint(null);
                                Log.recordLog("今日答题已完成", "");
                                Statistics.answerQuestionToday(FriendIdMap.currentUid);
                                break;

                            case FINISHED:
                                Statistics.setQuestionHint(null);
                                Log.recordLog("已经答过题了，饲料待领取", "");
                                Statistics.answerQuestionToday(FriendIdMap.currentUid);
                                break;
                        }
                        break;
                    }
                }
            } else {
                Log.recordLog(TAG, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "answerQuestion err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void recordFarmGame(GameType gameType) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.initFarmGame(gameType.name()));
            if ("SUCCESS".equals(jo.getString("memo"))) {
                JSONObject gameAward = jo.getJSONObject("gameAward");
                if (gameAward.getBoolean("level3Get"))
                    return;
                jo = new JSONObject(AntFarmRpcCall.recordFarmGame(gameType.name()));
                if ("SUCCESS".equals(jo.getString("memo"))) {
                    JSONArray awardInfos = jo.getJSONArray("awardInfos");
                    StringBuilder award = new StringBuilder();
                    for (int i = 0; i < awardInfos.length(); i++) {
                        jo = awardInfos.getJSONObject(i);
                        award.append(jo.getString("awardName")).append("*").append(jo.getInt("awardCount"));
                    }
                    Log.farm("庄园游戏🎮[" + gameType.gameName() + "]#" + award);
                } else {
                    Log.i(TAG, jo.toString());
                }
            } else {
                Log.i(TAG, jo.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "recordFarmGame err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void doFarmDailyTask() {
        try {
            String s = AntFarmRpcCall.listFarmTask();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("memo"))) {
                JSONArray jaFarmTaskList = jo.getJSONArray("farmTaskList");
                for (int i = 0; i < jaFarmTaskList.length(); i++) {
                    jo = jaFarmTaskList.getJSONObject(i);
                    String title = null;
                    String taskId = null;
                    int awardCount = 0;
                    if (!jo.has("taskMode"))
                        continue;
                    if (jo.has("title"))
                        title = jo.getString("title");
                    if ("VIEW".equals(jo.getString("taskMode")) && "TODO".equals(jo.getString("taskStatus"))) {
                        taskId = jo.getString("taskId");
                        awardCount = jo.getInt("awardCount");
                        jo = new JSONObject(AntFarmRpcCall.doFarmTask(taskId));
                        if ("SUCCESS".equals(jo.getString("memo"))) {
                            Log.farm("完成任务🧾[" + title + "]#获得饲料" + awardCount + "g");
                        } else {
                            Log.recordLog(jo.getString("memo"), jo.toString());
                        }
                    } else if ("庄园小视频".equals(title) && "TODO".equals(jo.getString("taskStatus"))) {
                        awardCount = jo.getInt("awardCount");
                        jo = new JSONObject(AntFarmRpcCall.queryTabVideoUrl());
                        if ("SUCCESS".equals(jo.getString("memo"))) {
                            String videoUrl = jo.getString("videoUrl");
                            String contentId = videoUrl.substring(videoUrl.indexOf("&contentId=") + 1,
                                    videoUrl.indexOf("&refer"));
                            jo = new JSONObject(AntFarmRpcCall.videoDeliverModule(contentId));
                            if (jo.getBoolean("success")) {
                                Thread.sleep(15100);
                                jo = new JSONObject(AntFarmRpcCall.videoTrigger(contentId));
                                if (jo.getBoolean("success")) {
                                    Log.farm("完成任务🧾[" + title + "]#获得饲料" + awardCount + "g");
                                } else {
                                    Log.recordLog(jo.getString("resultMsg"), jo.toString());
                                }
                            } else {
                                Log.recordLog(jo.getString("resultMsg"), jo.toString());
                            }
                        } else {
                            Log.recordLog(jo.getString("memo"), jo.toString());
                        }
                    }
                }
            } else {
                Log.recordLog(jo.getString("memo"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "doFarmDailyTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveFarmTaskAward() {
        try {
            String s = AntFarmRpcCall.listFarmTask();
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if ("SUCCESS".equals(memo)) {
                JSONObject signList = jo.getJSONObject("signList");
                sign(signList);
                JSONArray jaFarmTaskList = jo.getJSONArray("farmTaskList");
                for (int i = 0; i < jaFarmTaskList.length(); i++) {
                    jo = jaFarmTaskList.getJSONObject(i);
                    String taskTitle = null;
                    if (jo.has("title"))
                        taskTitle = jo.getString("title");
                    switch (TaskStatus.valueOf(jo.getString("taskStatus"))) {
                        case TODO:
                            break;
                        case FINISHED:
                            int awardCount = jo.getInt("awardCount");
                            if (awardCount + foodStock > foodStockLimit) {
                                unreceiveTaskAward++;
                                Log.recordLog("领取" + awardCount + "克饲料后将超过[" + foodStockLimit + "克]上限，已终止领取", "");
                                break;
                            }
                            s = AntFarmRpcCall.receiveFarmTaskAward(jo.getString("taskId"));
                            jo = new JSONObject(s);
                            memo = jo.getString("memo");
                            if ("SUCCESS".equals(memo)) {
                                foodStock = jo.getInt("foodStock");
                                Log.farm("领取奖励🎖️[" + taskTitle + "]#" + jo.getInt("haveAddFoodStock") + "g");
                                if (unreceiveTaskAward > 0)
                                    unreceiveTaskAward--;
                            } else {
                                Log.recordLog(memo, s);
                            }
                            break;
                        case RECEIVED:
                            if ("庄园小课堂".equals(taskTitle)) {
                                Statistics.setQuestionHint(null);
                            }
                            break;
                    }
                }
            } else {
                Log.recordLog(memo, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveFarmTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void sign(JSONObject signList) {
        try {
            JSONArray jaFarmsignList = signList.getJSONArray("signList");
            boolean signed = true;
            int awardCount = 0;
            for (int i = 0; i < jaFarmsignList.length(); i++) {
                JSONObject jo = jaFarmsignList.getJSONObject(i);
                if (Log.getFormatDate().equals(jo.getString("signKey"))) {
                    signed = jo.getBoolean("signed");
                    awardCount = jo.getInt("awardCount");
                    break;
                }
            }
            if (!signed) {
                JSONObject joSign = new JSONObject(AntFarmRpcCall.sign());
                if ("SUCCESS".equals(joSign.getString("memo"))) {
                    Log.farm("庄园签到📅获得饲料" + awardCount + "g");
                } else {
                    Log.i(TAG, joSign.toString());
                }
            } else {
                Log.recordLog("庄园今日已签到", "");
            }
        } catch (Throwable t) {
            Log.i(TAG, "Farmsign err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void feedAnimal(String farmId) {
        try {
            if (foodStock < 180) {
                Log.recordLog("喂鸡饲料不足", "");
            } else {
                String s = AntFarmRpcCall.feedAnimal(farmId);
                JSONObject jo = new JSONObject(s);
                String memo = jo.getString("memo");
                if ("SUCCESS".equals(memo)) {
                    int feedFood = foodStock - jo.getInt("foodStock");
                    add2FoodStock(-feedFood);
                    Log.farm("投喂小鸡🥣[" + feedFood + "g]#剩余" + foodStock + "g");
                } else {
                    Log.recordLog(memo, s);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "feedAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void useFarmTool(String targetFarmId, ToolType toolType) {
        try {
            String s = AntFarmRpcCall.listFarmTool();
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if ("SUCCESS".equals(memo)) {
                JSONArray jaToolList = jo.getJSONArray("toolList");
                for (int i = 0; i < jaToolList.length(); i++) {
                    jo = jaToolList.getJSONObject(i);
                    if (toolType.name().equals(jo.getString("toolType"))) {
                        int toolCount = jo.getInt("toolCount");
                        if (toolCount > 0) {
                            String toolId = "";
                            if (jo.has("toolId"))
                                toolId = jo.getString("toolId");
                            s = AntFarmRpcCall.useFarmTool(targetFarmId, toolId, toolType.name());
                            jo = new JSONObject(s);
                            memo = jo.getString("memo");
                            if ("SUCCESS".equals(memo))
                                Log.farm("使用道具🎭[" + toolType.nickName() + "]#剩余" + (toolCount - 1) + "张");
                            else
                                Log.recordLog(memo, s);
                        }
                        break;
                    }
                }
            } else {
                Log.recordLog(memo, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "useFarmTool err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void feedFriend() {
        try {
            String s, memo;
            JSONObject jo;
            for (int i = 0; i < Config.getFeedFriendAnimalList().size(); i++) {
                String userId = Config.getFeedFriendAnimalList().get(i);
                if (userId.equals(FriendIdMap.currentUid))
                    continue;
                if (!Statistics.canFeedFriendToday(userId, Config.getFeedFriendCountList().get(i)))
                    continue;
                s = AntFarmRpcCall.enterFarm("", userId);
                jo = new JSONObject(s);
                memo = jo.getString("memo");
                if ("SUCCESS".equals(memo)) {
                    jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
                    String friendFarmId = jo.getString("farmId");
                    JSONArray jaAnimals = jo.getJSONArray("animals");
                    for (int j = 0; j < jaAnimals.length(); j++) {
                        jo = jaAnimals.getJSONObject(j);
                        String masterFarmId = jo.getString("masterFarmId");
                        if (masterFarmId.equals(friendFarmId)) {
                            jo = jo.getJSONObject("animalStatusVO");
                            if (AnimalInteractStatus.HOME.name().equals(jo.getString("animalInteractStatus"))
                                    && AnimalFeedStatus.HUNGRY.name().equals(jo.getString("animalFeedStatus"))) {
                                feedFriendAnimal(friendFarmId, FriendIdMap.getNameById(userId));
                            }
                            break;
                        }
                    }
                } else {
                    Log.recordLog(memo, s);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "feedFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void feedFriendAnimal(String friendFarmId, String user) {
        try {
            Log.recordLog("[" + user + "]的小鸡在挨饿", "");
            if (foodStock < 180) {
                Log.recordLog("喂鸡饲料不足", "");
                if (unreceiveTaskAward > 0) {
                    Log.recordLog("还有待领取的饲料", "");
                    receiveFarmTaskAward();
                }
            }
            if (foodStock >= 180) {
                String s = AntFarmRpcCall.feedFriendAnimal(friendFarmId);
                JSONObject jo = new JSONObject(s);
                String memo = jo.getString("memo");
                if ("SUCCESS".equals(memo)) {
                    int feedFood = foodStock - jo.getInt("foodStock");
                    if (feedFood > 0) {
                        add2FoodStock(-feedFood);
                        Log.farm("帮喂好友🥣[" + user + "]的小鸡[" + feedFood + "g]#剩余" + foodStock + "g");
                        Statistics.feedFriendToday(AntFarmRpcCall.farmId2UserId(friendFarmId));
                    }
                } else {
                    Log.recordLog(memo, s);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "feedFriendAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void notifyFriend() {
        if (foodStock >= foodStockLimit)
            return;
        try {
            boolean hasNext = false;
            int pageStartSum = 0;
            String s;
            JSONObject jo;
            do {
                s = AntFarmRpcCall.rankingList(pageStartSum);
                jo = new JSONObject(s);
                String memo = jo.getString("memo");
                if ("SUCCESS".equals(memo)) {
                    hasNext = jo.getBoolean("hasNext");
                    JSONArray jaRankingList = jo.getJSONArray("rankingList");
                    pageStartSum += jaRankingList.length();
                    for (int i = 0; i < jaRankingList.length(); i++) {
                        jo = jaRankingList.getJSONObject(i);
                        String userId = jo.getString("userId");
                        String userName = FriendIdMap.getNameById(userId);
                        if (Config.getDontNotifyFriendList().contains(userId)
                                || userId.equals(FriendIdMap.currentUid))
                            continue;
                        boolean starve = jo.has("actionType") && "starve_action".equals(jo.getString("actionType"));
                        if (jo.getBoolean("stealingAnimal") && !starve) {
                            s = AntFarmRpcCall.enterFarm("", userId);
                            jo = new JSONObject(s);
                            memo = jo.getString("memo");
                            if ("SUCCESS".equals(memo)) {
                                jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
                                String friendFarmId = jo.getString("farmId");
                                JSONArray jaAnimals = jo.getJSONArray("animals");
                                boolean notified = !Config.notifyFriend();
                                for (int j = 0; j < jaAnimals.length(); j++) {
                                    jo = jaAnimals.getJSONObject(j);
                                    String animalId = jo.getString("animalId");
                                    String masterFarmId = jo.getString("masterFarmId");
                                    if (!masterFarmId.equals(friendFarmId) && !masterFarmId.equals(ownerFarmId)) {
                                        if (notified)
                                            continue;
                                        jo = jo.getJSONObject("animalStatusVO");
                                        notified = notifyFriend(jo, friendFarmId, animalId, userName);
                                    }
                                }
                            } else {
                                Log.recordLog(memo, s);
                            }
                        }
                    }
                } else {
                    Log.recordLog(memo, s);
                }
            } while (hasNext);
            Log.recordLog("饲料剩余[" + foodStock + "g]", "");
        } catch (Throwable t) {
            Log.i(TAG, "notifyFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static boolean notifyFriend(JSONObject joAnimalStatusVO, String friendFarmId, String animalId,
            String user) {
        try {
            if (AnimalInteractStatus.STEALING.name().equals(joAnimalStatusVO.getString("animalInteractStatus"))
                    && AnimalFeedStatus.EATING.name().equals(joAnimalStatusVO.getString("animalFeedStatus"))) {
                String s = AntFarmRpcCall.notifyFriend(animalId, friendFarmId);
                JSONObject jo = new JSONObject(s);
                String memo = jo.getString("memo");
                if ("SUCCESS".equals(memo)) {
                    double rewardCount = jo.getDouble("rewardCount");
                    if (jo.getBoolean("refreshFoodStock"))
                        foodStock = (int) jo.getDouble("finalFoodStock");
                    else
                        add2FoodStock((int) rewardCount);
                    Log.farm("通知好友📧[" + user + "]被偷吃#奖励" + rewardCount + "g");
                    return true;
                } else {
                    Log.recordLog(memo, s);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "notifyFriend err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void parseSyncAnimalStatusResponse(String resp) {
        try {
            JSONObject jo = new JSONObject(resp);
            if (!jo.has("subFarmVO")) {
                return;
            }
            JSONObject subFarmVO = jo.getJSONObject("subFarmVO");
            if (subFarmVO.has("foodStock")) {
                foodStock = subFarmVO.getInt("foodStock");
            }
            if (subFarmVO.has("manureVO")) {
                JSONArray manurePotList = subFarmVO.getJSONObject("manureVO").getJSONArray("manurePotList");
                for (int i = 0; i < manurePotList.length(); i++) {
                    JSONObject manurePot = manurePotList.getJSONObject(i);
                    if (manurePot.getInt("manurePotNum") >= 100) {
                        JSONObject joManurePot = new JSONObject(
                                AntFarmRpcCall.collectManurePot(manurePot.getString("manurePotNO")));
                        if (joManurePot.getBoolean("success")) {
                            int collectManurePotNum = joManurePot.getInt("collectManurePotNum");
                            Log.farm("打扫鸡屎🧹[" + collectManurePotNum + "g]");
                        }
                    }
                }
            }
            ownerFarmId = subFarmVO.getString("farmId");
            JSONObject farmProduce = subFarmVO.getJSONObject("farmProduce");
            benevolenceScore = farmProduce.getDouble("benevolenceScore");
            if (subFarmVO.has("rewardList")) {
                JSONArray jaRewardList = subFarmVO.getJSONArray("rewardList");
                if (jaRewardList.length() > 0) {
                    rewardList = new RewardFriend[jaRewardList.length()];
                    for (int i = 0; i < rewardList.length; i++) {
                        JSONObject joRewardList = jaRewardList.getJSONObject(i);
                        if (rewardList[i] == null)
                            rewardList[i] = new RewardFriend();
                        rewardList[i].consistencyKey = joRewardList.getString("consistencyKey");
                        rewardList[i].friendId = joRewardList.getString("friendId");
                        rewardList[i].time = joRewardList.getString("time");
                    }
                }
            }
            JSONArray jaAnimals = subFarmVO.getJSONArray("animals");
            animals = new Animal[jaAnimals.length()];
            for (int i = 0; i < animals.length; i++) {
                if (animals[i] == null)
                    animals[i] = new Animal();
                JSONObject animal = jaAnimals.getJSONObject(i);
                animals[i].animalId = animal.getString("animalId");
                animals[i].currentFarmId = animal.getString("currentFarmId");
                animals[i].masterFarmId = animal.getString("masterFarmId");
                animals[i].animalBuff = animal.getString("animalBuff");
                animals[i].subAnimalType = animal.getString("subAnimalType");
                JSONObject animalStatusVO = animal.getJSONObject("animalStatusVO");
                animals[i].animalFeedStatus = animalStatusVO.getString("animalFeedStatus");
                animals[i].animalInteractStatus = animalStatusVO.getString("animalInteractStatus");
                if (animals[i].masterFarmId.equals(ownerFarmId))
                    ownerAnimal = animals[i];
            }
        } catch (Throwable t) {
            Log.i(TAG, "parseSyncAnimalStatusResponse err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void add2FoodStock(int i) {
        foodStock += i;
        if (foodStock > foodStockLimit)
            foodStock = foodStockLimit;
        if (foodStock < 0)
            foodStock = 0;
    }

    private static void collectDailyFoodMaterial(String userId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterKitchen(userId));
            if ("SUCCESS".equals(jo.getString("memo"))) {
                boolean canCollectDailyFoodMaterial = jo.getBoolean("canCollectDailyFoodMaterial");
                if (canCollectDailyFoodMaterial) {
                    int dailyFoodMaterialAmount = jo.getInt("dailyFoodMaterialAmount");
                    jo = new JSONObject(AntFarmRpcCall.collectDailyFoodMaterial(dailyFoodMaterialAmount));
                    if ("SUCCESS".equals(jo.getString("memo"))) {
                        Log.farm("小鸡厨房👨🏻‍🍳[领取今日食材]#" + dailyFoodMaterialAmount + "g");
                    } else {
                        Log.i(TAG, jo.toString());
                    }
                } else {
                    Log.recordLog("明日可领食材", "");
                }
            } else {
                Log.i(TAG, jo.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectDailyFoodMaterial err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void collectDailyLimitedFoodMaterial() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryFoodMaterialPack());
            if ("SUCCESS".equals(jo.getString("memo"))) {
                boolean canCollectDailyLimitedFoodMaterial = jo.getBoolean("canCollectDailyLimitedFoodMaterial");
                if (canCollectDailyLimitedFoodMaterial) {
                    int dailyLimitedFoodMaterialAmount = jo.getInt("dailyLimitedFoodMaterialAmount");
                    jo = new JSONObject(AntFarmRpcCall.collectDailyLimitedFoodMaterial(dailyLimitedFoodMaterialAmount));
                    if ("SUCCESS".equals(jo.getString("memo"))) {
                        Log.farm("小鸡厨房👨🏻‍🍳[领取爱心食材店食材]#" + dailyLimitedFoodMaterialAmount + "g");
                    } else {
                        Log.i(TAG, jo.toString());
                    }
                } else {
                    Log.recordLog("已领取每日限量食材", "");
                }
            } else {
                Log.i(TAG, jo.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectDailyLimitedFoodMaterial err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void cook(String userId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterKitchen(userId));
            if ("SUCCESS".equals(jo.getString("memo"))) {
                int cookTimesAllowed = jo.getInt("cookTimesAllowed");
                if (cookTimesAllowed > 0) {
                    for (int i = 0; i < cookTimesAllowed; i++) {
                        jo = new JSONObject(AntFarmRpcCall.cook(userId));
                        if ("SUCCESS".equals(jo.getString("memo"))) {
                            JSONObject cuisineVO = jo.getJSONObject("cuisineVO");
                            Log.farm("小鸡厨房👨🏻‍🍳[" + cuisineVO.getString("name") + "]制作成功");
                        } else {
                            Log.i(TAG, jo.toString());
                        }
                        Thread.sleep(RandomUtils.delay());
                    }
                }
            } else {
                Log.i(TAG, jo.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "cook err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void useFarmFood(JSONArray cuisineList) {
        try {
            JSONObject jo = new JSONObject();
            String cookbookId = null;
            String cuisineId = null;
            String name = null;
            for (int i = 0; i < cuisineList.length(); i++) {
                jo = cuisineList.getJSONObject(i);
                if (jo.getInt("count") <= 0)
                    continue;
                cookbookId = jo.getString("cookbookId");
                cuisineId = jo.getString("cuisineId");
                name = jo.getString("name");
                jo = new JSONObject(AntFarmRpcCall.useFarmFood(cookbookId, cuisineId));
                if ("SUCCESS".equals(jo.getString("memo"))) {
                    double deltaProduce = jo.getJSONObject("foodEffect").getDouble("deltaProduce");
                    Log.farm("使用美食🍱[" + name + "]#加速" + deltaProduce + "颗爱心鸡蛋");
                } else {
                    Log.i(TAG, jo.toString());
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "useFarmFood err:");
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
                        String singleDesc = jo.getString("singleDesc");
                        int awardCount = jo.getInt("awardCount");
                        if (singleDesc.contains("饲料") && awardCount + foodStock > foodStockLimit) {
                            Log.recordLog("领取" + awardCount + "克饲料后将超过[" + foodStockLimit + "克]上限，已终止领取", "");
                            break;
                        }
                        jo = new JSONObject(AntFarmRpcCall.drawLotteryPlus());
                        if ("SUCCESS".equals(jo.getString("memo"))) {
                            Log.farm("惊喜礼包🎁[" + singleDesc + "*" + awardCount + "]");
                        } else {
                            Log.i(TAG, jo.getString("memo"));
                        }
                    } else {
                        Log.recordLog("当日奖励已领取", "");
                    }
                    break;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "drawLotteryPlus err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
