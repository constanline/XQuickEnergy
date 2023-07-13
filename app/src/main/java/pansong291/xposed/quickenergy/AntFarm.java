package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntFarmRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.RandomUtils;
import pansong291.xposed.quickenergy.util.Statistics;

public class AntFarm
{
    private static final String TAG = AntFarm.class.getCanonicalName();

    public enum SendType
    {
        HIT, NORMAL;
        public static final CharSequence[] names =
                {HIT.name(), NORMAL.name()};
        public static final CharSequence[] nickNames =
                {"攻击", "常规"};
        public SendType another()
        {
            return this == HIT ? NORMAL : HIT;
        }
        public CharSequence nickName()
        {
            return nickNames[ordinal()];
        }
    }
    public enum AnimalBuff
    { ACCELERATING, INJURED, NONE }
    public enum AnimalFeedStatus
    { HUNGRY, EATING }
    public enum AnimalInteractStatus
    { HOME, GOTOSTEAL, STEALING }
    public enum SubAnimalType
    { NORMAL, GUEST, PIRATE }
    public enum TaskStatus
    { TODO, FINISHED, RECEIVED }
    public enum ToolType
    {
        STEALTOOL, ACCELERATETOOL, SHARETOOL, FENCETOOL, NEWEGGTOOL;
        public static final CharSequence[] nickNames =
                {"蹭饭卡", "加速卡", "救济卡", "篱笆卡", "新蛋卡"};
        public CharSequence nickName()
        {
            return nickNames[ordinal()];
        }
    }

    private static class Animal
    {
        public String animalId, currentFarmId, masterFarmId,
                animalBuff, subAnimalType, animalFeedStatus, animalInteractStatus;
    }

    private static class RewardFriend
    {
        public String consistencyKey, friendId, time;
    }

    /**private static class FarmTool
     {
     public ToolType toolType;
     public String toolId;
     public int toolCount, toolHoldLimit;
     }/**/

    private static String ownerFarmId;
    private static Animal[] animals;
    private static Animal ownerAnimal;
    private static int foodStock;
    private static int foodStockLimit;
    private static String rewardProductNum;
    private static RewardFriend[] rewardList;
    private static double benevolenceScore;
    private static double harvestBenevolenceScore;
    private static int unreceiveTaskAward = 0;
    //private static FarmTool[] farmTools;

    public static void start(ClassLoader loader)
    {
        if(!Config.enableFarm()) return;
        new Thread()
        {
            private ClassLoader loader;

            public Thread setData(ClassLoader cl)
            {
                loader = cl;
                return this;
            }

            @Override
            public void run()
            {
                try
                {
                    while(FriendIdMap.currentUid == null || FriendIdMap.currentUid.isEmpty())
                        Thread.sleep(100);
                    String s = AntFarmRpcCall.rpcCall_enterFarm(loader, "", FriendIdMap.currentUid);
                    if(s == null)
                    {
                        Thread.sleep(RandomUtils.delay());
                        s = AntFarmRpcCall.rpcCall_enterFarm(loader, "", FriendIdMap.currentUid);
                    }
                    JSONObject jo = new JSONObject(s);
                    String memo = jo.getString("memo");
                    if(memo.equals("SUCCESS"))
                    {
                        rewardProductNum = jo.getJSONObject("dynamicGlobalConfig").getString("rewardProductNum");
                        JSONObject joFarmVO = jo.getJSONObject("farmVO");
                        foodStock = joFarmVO.getInt("foodStock");
                        foodStockLimit = joFarmVO.getInt("foodStockLimit");
                        harvestBenevolenceScore = joFarmVO.getDouble("harvestBenevolenceScore");
                        parseSyncAnimalStatusResponse(joFarmVO.toString());
                    }else
                    {
                        Log.recordLog(memo, s);
                    }

                    if(Config.rewardFriend())rewardFriend(loader);

                    if(Config.sendBackAnimal())sendBackAnimal(loader);

                    if(!AnimalInteractStatus.HOME.name().equals(ownerAnimal.animalInteractStatus))
                    {
                        syncAnimalStatusAtOtherFarm(loader, ownerAnimal.currentFarmId, "");
                        boolean guest = false;
                        switch(SubAnimalType.valueOf(ownerAnimal.subAnimalType))
                        {
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
                            default:
                                Log.recordLog("小鸡不在庄园", ownerAnimal.subAnimalType);
                        }

                        boolean hungry = false;
                        String userName = FriendIdMap.getNameById(AntFarmRpcCall.farmId2UserId(ownerAnimal.currentFarmId));
                        switch(AnimalFeedStatus.valueOf(ownerAnimal.animalFeedStatus))
                        {
                            case HUNGRY:
                                hungry = true;
                                Log.recordLog("小鸡在〔" + userName + "〕的庄园里挨饿", "");
                                break;

                            case EATING:
                                Log.recordLog("小鸡在〔" + userName + "〕的庄园里吃得津津有味", "");
                                break;
                        }

                        boolean recall = false;
                        switch(Config.recallAnimalType())
                        {
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
                        if(recall)
                        {
                            recallAnimal(loader, ownerAnimal.animalId, ownerAnimal.currentFarmId, ownerFarmId, userName);
                            syncAnimalStatus(loader, ownerFarmId);
                        }
                    }

                    if(Config.receiveFarmToolReward())
                    {
                        receiveToolTaskReward(loader);
                    }

                    if(Config.useNewEggTool())
                    {
                        useFarmTool(loader, ownerFarmId, ToolType.NEWEGGTOOL);
                        syncAnimalStatus(loader, ownerFarmId);
                    }

                    if(Config.harvestProduce() && benevolenceScore >= 1)
                    {
                        Log.recordLog("有可收取的爱心鸡蛋", "");
                        harvestProduce(loader, ownerFarmId);
                    }

                    if(Config.donation() && harvestBenevolenceScore >= 5)
                    {
                        Log.recordLog("爱心鸡蛋已达到可捐赠个数", "");
                        donation(loader);
                    }

                    if(Config.answerQuestion() && Statistics.canAnswerQuestionToday(FriendIdMap.currentUid))
                    {
                        answerQuestion(loader);
                    }

                    if(Config.receiveFarmTaskAward())receiveFarmTaskAward(loader);

                    if(AnimalInteractStatus.HOME.name().equals(ownerAnimal.animalInteractStatus))
                    {
                        if(Config.feedAnimal() && AnimalFeedStatus.HUNGRY.name().equals(ownerAnimal.animalFeedStatus))
                        {
                            Log.recordLog("小鸡在挨饿", "");
                            feedAnimal(loader, ownerFarmId);
                            //syncAnimalStatus(loader,ownerFarmId);
                        }

                        if(AnimalBuff.ACCELERATING.name().equals(ownerAnimal.animalBuff))
                        {
                            Log.recordLog("小鸡正双手并用着加速吃饲料", "");
                        }else if(Config.useAccelerateTool())
                        {
                            // 加速卡
                            useFarmTool(loader, ownerFarmId, ToolType.ACCELERATETOOL);
                        }

                        if(unreceiveTaskAward > 0)
                        {
                            Log.recordLog("还有待领取的饲料", "");
                            receiveFarmTaskAward(loader);
                        }

                    }

                    // 帮好友喂鸡
                    feedFriend(loader);

                    // 通知好友赶鸡
                    if(Config.notifyFriend()) notifyFriend(loader);

                }catch(Throwable t)
                {
                    Log.i(TAG, "AntFarm.start.run err:");
                    Log.printStackTrace(TAG, t);
                }
                FriendIdMap.saveIdMap();
            }
        }.setData(loader).start();

    }

    private static boolean isEnterOwnerFarm(String resp)
    {
        return resp.contains("\"relation\":\"OWNER\"");
    }

    public static boolean isEnterFriendFarm(String resp)
    {
        return resp.contains("\"relation\":\"FRIEND\"");
    }

    private static void syncAnimalStatus(ClassLoader loader, String farmId)
    {
        try
        {
            String s = AntFarmRpcCall.rpcCall_syncAnimalStatus(loader, farmId);
            parseSyncAnimalStatusResponse(s);
        }catch(Throwable t)
        {
            Log.i(TAG, "syncAnimalStatus err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void syncAnimalStatusAtOtherFarm(ClassLoader loader, String farmId, String userId)
    {
        try
        {
            String s = AntFarmRpcCall.rpcCall_enterFarm(loader, farmId, userId);
            JSONObject jo = new JSONObject(s);
            jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
            JSONArray jaAnimals = jo.getJSONArray("animals");
            for(int i = 0; i < jaAnimals.length(); i++)
            {
                jo = jaAnimals.getJSONObject(i);
                if(jo.getString("masterFarmId").equals(ownerFarmId))
                {
                    if(ownerAnimal == null) ownerAnimal = new Animal();
                    jo = jaAnimals.getJSONObject(i);
                    ownerAnimal.animalId = jo.getString("animalId");
                    ownerAnimal.currentFarmId = jo.getString("currentFarmId");
                    ownerAnimal.masterFarmId = ownerFarmId;
                    ownerAnimal.animalBuff = jo.getString("animalBuff");
                    ownerAnimal.subAnimalType = jo.getString("subAnimalType");
                    jo = jo.getJSONObject("animalStatusVO");
                    ownerAnimal.animalFeedStatus = jo.getString("animalFeedStatus");
                    ownerAnimal.animalInteractStatus = jo.getString("animalInteractStatus");
                    Log.i("owner", "animalId=" + ownerAnimal.animalId);
                    Log.i("owner", "currentFarmId=" + ownerAnimal.currentFarmId);
                    Log.i("owner", "masterFarmId=" + ownerAnimal.masterFarmId);
                    Log.i("owner", "animalBuff=" + ownerAnimal.animalBuff);
                    Log.i("owner", "subAnimalType=" + ownerAnimal.subAnimalType);
                    Log.i("owner", "animalFeedStatus=" + ownerAnimal.animalFeedStatus);
                    Log.i("owner", "animalInteractStatus=" + ownerAnimal.animalInteractStatus);
                    break;
                }
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "syncAnimalStatusAtOtherFarm err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void rewardFriend(ClassLoader loader)
    {
        try
        {
            if(rewardList != null)
            {
                for(int i = 0; i < rewardList.length; i++)
                {
                    String s = AntFarmRpcCall.rpcCall_rewardFriend(loader, rewardList[i].consistencyKey, rewardList[i].friendId, rewardProductNum, rewardList[i].time);
                    JSONObject jo = new JSONObject(s);
                    String memo = jo.getString("memo");
                    if(memo.equals("SUCCESS"))
                    {
                        double rewardCount = benevolenceScore - jo.getDouble("farmProduct");
                        benevolenceScore -= rewardCount;
                        Log.farm("打赏〔" + FriendIdMap.getNameById(rewardList[i].friendId) + "〕〔" + rewardCount + "颗〕爱心鸡蛋");
                    }else
                    {
                        Log.recordLog(memo, s);
                    }
                }
                rewardList = null;
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "rewardFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void recallAnimal(ClassLoader loader, String animalId, String currentFarmId, String masterFarmId, String user)
    {
        try
        {
            String s = AntFarmRpcCall.rpcCall_recallAnimal(loader, animalId, currentFarmId, masterFarmId);
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if(memo.equals("SUCCESS"))
            {
                double foodHaveStolen = jo.getDouble("foodHaveStolen");
                Log.farm("召回小鸡，偷吃〔" + user + "〕〔" + foodHaveStolen + "克〕");
                // 这里不需要加
                // add2FoodStock((int)foodHaveStolen);
            }else
            {
                Log.recordLog(memo, s);
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "recallAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void sendBackAnimal(ClassLoader loader)
    {
        try
        {
            for(int i = 0; i < animals.length; i++)
            {
                if(AnimalInteractStatus.STEALING.name().equals(animals[i].animalInteractStatus)
                        && !SubAnimalType.GUEST.name().equals(animals[i].subAnimalType))
                {
                    // 赶鸡
                    String user = AntFarmRpcCall.farmId2UserId(animals[i].masterFarmId);
                    if(Config.getDontSendFriendList().contains(user))
                        continue;
                    SendType sendType = Config.sendType();
                    user = FriendIdMap.getNameById(user);
                    String s = AntFarmRpcCall.rpcCall_sendBackAnimal(
                            loader, sendType.name(), animals[i].animalId,
                            animals[i].currentFarmId, animals[i].masterFarmId);
                    JSONObject jo = new JSONObject(s);
                    String memo = jo.getString("memo");
                    if(memo.equals("SUCCESS"))
                    {
                        if(sendType == SendType.HIT)
                        {
                            if(jo.has("hitLossFood"))
                            {
                                s = "胖揍〔" + user + "〕小鸡，掉落〔" + jo.getInt("hitLossFood") + "克〕";
                                if(jo.has("finalFoodStorage"))
                                    foodStock = jo.getInt("finalFoodStorage");
                            }else
                                s = "〔" + user + "〕的小鸡躲开了攻击";
                        }else
                        {
                            s = "赶走〔" + user + "〕的小鸡";
                        }
                        Log.farm(s);
                    }else
                    {
                        Log.recordLog(memo, s);
                    }
                }
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "sendBackAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveToolTaskReward(ClassLoader loader)
    {
        try
        {
            String s = AntFarmRpcCall.rpcCall_listToolTaskDetails(loader);
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if(memo.equals("SUCCESS"))
            {
                JSONArray jaList = jo.getJSONArray("list");
                for(int i = 0; i < jaList.length(); i++)
                {
                    jo = jaList.getJSONObject(i);
                    if(jo.has("taskStatus")
                            && TaskStatus.FINISHED.name().equals(jo.getString("taskStatus")))
                    {
                        int awardCount = jo.getInt("awardCount");
                        String awardType = jo.getString("awardType");
                        ToolType toolType = ToolType.valueOf(awardType);
                        String taskType = jo.getString("taskType");
                        jo = new JSONObject(jo.getString("bizInfo"));
                        String taskTitle = jo.getString("taskTitle");
                        s = AntFarmRpcCall.rpcCall_receiveToolTaskReward(loader, awardType, awardCount, taskType);
                        jo = new JSONObject(s);
                        memo = jo.getString("memo");
                        if(memo.equals("SUCCESS"))
                        {
                            Log.farm("领取〔" + awardCount + "张〕〔" + toolType.nickName() + "〕，来源：" + taskTitle);
                        }else
                        {
                            memo = memo.replace("道具", toolType.nickName());
                            Log.recordLog(memo, s);
                        }
                    }
                }
            }else
            {
                Log.recordLog(memo, s);
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "receiveToolTaskReward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void harvestProduce(ClassLoader loader, String farmId)
    {
        try
        {
            String s = AntFarmRpcCall.rpcCall_harvestProduce(loader, farmId);
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if(memo.equals("SUCCESS"))
            {
                double harvest = jo.getDouble("harvestBenevolenceScore");
                harvestBenevolenceScore = jo.getDouble("finalBenevolenceScore");
                Log.farm("收取〔" + harvest + "颗〕爱心鸡蛋，剩余〔" + harvestBenevolenceScore + "颗〕");
            }else
            {
                Log.recordLog(memo, s);
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "harvestProduce err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void donation(ClassLoader loader)
    {
        try
        {
            String s = AntFarmRpcCall.rpcCall_listActivityInfo(loader);
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if(memo.equals("SUCCESS"))
            {
                JSONArray jaActivityInfos = jo.getJSONArray("activityInfos");
                String activityId = null, activityName = null;
                for(int i = 0; i < jaActivityInfos.length(); i++)
                {
                    jo = jaActivityInfos.getJSONObject(i);
                    if(!jo.get("donationTotal").equals(jo.get("donationLimit")))
                    {
                        activityId = jo.getString("activityId");
                        activityName = jo.getString("activityName");
                        break;
                    }
                }
                if(activityId == null)
                {
                    Log.recordLog("今日已无可捐赠的活动", "");
                }else
                {
                    s = AntFarmRpcCall.rpcCall_donation(loader, activityId);
                    jo = new JSONObject(s);
                    memo = jo.getString("memo");
                    if(memo.equals("SUCCESS"))
                    {
                        jo = jo.getJSONObject("donation");
                        harvestBenevolenceScore = jo.getDouble("harvestBenevolenceScore");
                        Log.farm("捐赠活动〔" + activityName + "〕，累计捐赠〔" + jo.getInt("donationTimesStat") + "次〕");
                    }else
                    {
                        Log.recordLog(memo, s);
                    }
                }
            }else
            {
                Log.recordLog(memo, s);
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "donation err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void answerQuestion(ClassLoader loader)
    {
        try
        {
            String s = AntFarmRpcCall.rpcCall_listFarmTask(loader);
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if(memo.equals("SUCCESS"))
            {
                JSONArray jaFarmTaskList = jo.getJSONArray("farmTaskList");
                for(int i = 0; i < jaFarmTaskList.length(); i++)
                {
                    jo = jaFarmTaskList.getJSONObject(i);
                    if(jo.getString("title").equals("庄园小课堂"))
                    {
                        switch(TaskStatus.valueOf((jo.getString("taskStatus"))))
                        {
                            case TODO:
                                s = AntFarmRpcCall.rpcCall_getAnswerInfo(loader);
                                jo = new JSONObject(s);
                                memo = jo.getString("memo");
                                if(memo.equals("SUCCESS"))
                                {
                                    jo = jo.getJSONArray("answerInfoVOs").getJSONObject(0);
                                    JSONArray jaOptionContents = jo.getJSONArray("optionContents");
                                    String rightReply = jo.getString("rightReply");
                                    Log.recordLog(jo.getString("questionContent"), "");
                                    Log.recordLog(jaOptionContents.toString(), "");
                                    String questionId = jo.getString("questionId");
                                    int answer = 0;
                                    for(int j = 0; j < jaOptionContents.length(); j++)
                                    {
                                        if(rightReply.contains(jaOptionContents.getString(j)))
                                        {
                                            answer += j + 1;
                                            //break;
                                        }
                                    }
                                    if(0 < answer && answer < 3)
                                    {
                                        s = AntFarmRpcCall.rpcCall_answerQuestion(loader, questionId, answer);
                                        jo = new JSONObject(s);
                                        memo = jo.getString("memo");
                                        if(memo.equals("SUCCESS"))
                                        {
                                            s = jo.getBoolean("rightAnswer") ? "正确": "错误";
                                            Log.farm("答题" + s + "，可领取［" + jo.getInt("awardCount") + "克］");
                                            Statistics.answerQuestionToday(FriendIdMap.currentUid);
                                        }else
                                        {
                                            Log.recordLog(memo, s);
                                        }
                                        Statistics.setQuestionHint(null);
                                    }else
                                    {
                                        Statistics.setQuestionHint(rightReply);
                                        Log.farm("未找到正确答案，放弃作答。提示：" + rightReply);
                                        Statistics.answerQuestionToday(FriendIdMap.currentUid);
                                    }
                                }else
                                {
                                    Log.recordLog(memo, s);
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
            }else
            {
                Log.recordLog(memo, s);
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "answerQuestion err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveFarmTaskAward(ClassLoader loader)
    {
        try
        {
            String s = AntFarmRpcCall.rpcCall_listFarmTask(loader);
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if(memo.equals("SUCCESS"))
            {
                JSONArray jaFarmTaskList = jo.getJSONArray("farmTaskList");
                for(int i = 0; i < jaFarmTaskList.length(); i++)
                {
                    jo = jaFarmTaskList.getJSONObject(i);
                    String taskTitle = null;
                    if(jo.has("title")) taskTitle = jo.getString("title");
                    switch(TaskStatus.valueOf(jo.getString("taskStatus")))
                    {
                        case TODO:
                            break;
                        case FINISHED:
                            int awardCount = jo.getInt("awardCount");
                            if(awardCount + foodStock > foodStockLimit)
                            {
                                unreceiveTaskAward++;
                                Log.recordLog("领取" + awardCount + "克饲料后将超过〔" + foodStockLimit + "克〕上限，已终止领取", "");
                                break;
                            }

                            s = AntFarmRpcCall.rpcCall_receiveFarmTaskAward(loader, jo.getString("taskId"));
                            jo = new JSONObject(s);
                            memo = jo.getString("memo");
                            if(memo.equals("SUCCESS"))
                            {
                                foodStock = jo.getInt("foodStock");
                                Log.farm("领取〔" + jo.getInt("haveAddFoodStock") + "克〕，来源：" + taskTitle);
                                if(unreceiveTaskAward > 0)unreceiveTaskAward--;
                            }else
                            {
                                Log.recordLog(memo, s);
                            }
                        case RECEIVED:
                            if(taskTitle != null && taskTitle.equals("庄园小课堂"))
                            {
                                Statistics.setQuestionHint(null);
                            }
                            break;
                    }
                }
            }else
            {
                Log.recordLog(memo, s);
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "receiveFarmTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void feedAnimal(ClassLoader loader, String farmId)
    {
        try
        {
            if(foodStock < 180)
            {
                Log.recordLog("喂鸡饲料不足", "");
            }else
            {
                String s = AntFarmRpcCall.rpcCall_feedAnimal(loader, farmId);
                JSONObject jo = new JSONObject(s);
                String memo = jo.getString("memo");
                if(memo.equals("SUCCESS"))
                {
                    int feedFood = foodStock - jo.getInt("foodStock");
                    add2FoodStock(-feedFood);
                    Log.farm("喂小鸡［" + feedFood + "克］，剩余〔" + foodStock + "克〕");
                }else
                {
                    Log.recordLog(memo, s);
                }
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "feedAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void useFarmTool(ClassLoader loader, String targetFarmId, ToolType toolType)
    {
        try
        {
            String s = AntFarmRpcCall.rpcCall_listFarmTool(loader);
            JSONObject jo = new JSONObject(s);
            String memo = jo.getString("memo");
            if(memo.equals("SUCCESS"))
            {
                JSONArray jaToolList = jo.getJSONArray("toolList");
                for(int i = 0; i < jaToolList.length(); i++)
                {
                    jo = jaToolList.getJSONObject(i);
                    if(toolType.name().equals(jo.getString("toolType")))
                    {
                        int toolCount = jo.getInt("toolCount");
                        if(toolCount > 0)
                        {
                            String toolId = "";
                            if(jo.has("toolId")) toolId = jo.getString("toolId");
                            s = AntFarmRpcCall.rpcCall_useFarmTool(loader, targetFarmId, toolId, toolType.name());
                            jo = new JSONObject(s);
                            memo = jo.getString("memo");
                            if(memo.equals("SUCCESS"))
                                Log.farm("使用" + toolType.nickName() + "成功，剩余〔" + (toolCount - 1) + "张〕");
                            else Log.recordLog(memo, s);
                        }
                        break;
                    }
                }
            }else
            {
                Log.recordLog(memo, s);
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "useFarmTool err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void feedFriend(ClassLoader loader)
    {
        try
        {
            String s, memo;
            JSONObject jo;
            for(int i = 0; i < Config.getFeedFriendAnimalList().size(); i++)
            {
                String userId = Config.getFeedFriendAnimalList().get(i);
                if(userId.equals(FriendIdMap.currentUid))
                    continue;
                if(!Statistics.canFeedFriendToday(userId, Config.getFeedFriendCountList().get(i)))
                    continue;
                s = AntFarmRpcCall.rpcCall_enterFarm(loader, "", userId);
                jo = new JSONObject(s);
                memo = jo.getString("memo");
                if(memo.equals("SUCCESS"))
                {
                    jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
                    String friendFarmId = jo.getString("farmId");
                    JSONArray jaAnimals = jo.getJSONArray("animals");
                    for(int j = 0; j < jaAnimals.length(); j++)
                    {
                        jo = jaAnimals.getJSONObject(j);
                        String masterFarmId = jo.getString("masterFarmId");
                        if(masterFarmId.equals(friendFarmId))
                        {
                            jo = jo.getJSONObject("animalStatusVO");
                            if(AnimalInteractStatus.HOME.name().equals(jo.getString("animalInteractStatus"))
                                    && AnimalFeedStatus.HUNGRY.name().equals(jo.getString("animalFeedStatus")))
                            {
                                feedFriendAnimal(loader, friendFarmId, FriendIdMap.getNameById(userId));
                            }
                            break;
                        }
                    }
                }else
                {
                    Log.recordLog(memo, s);
                }
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "feedFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void feedFriendAnimal(ClassLoader loader, String friendFarmId, String user)
    {
        try
        {
            Log.recordLog("〔" + user + "〕的小鸡在挨饿", "");
            if(foodStock < 180)
            {
                Log.recordLog("喂鸡饲料不足", "");
                if(unreceiveTaskAward > 0)
                {
                    Log.recordLog("还有待领取的饲料", "");
                    receiveFarmTaskAward(loader);
                }
            }
            if(foodStock >= 180)
            {
                String s = AntFarmRpcCall.rpcCall_feedFriendAnimal(loader, friendFarmId);
                JSONObject jo = new JSONObject(s);
                String memo = jo.getString("memo");
                if(memo.equals("SUCCESS"))
                {
                    int feedFood = foodStock - jo.getInt("foodStock");
                    if(feedFood > 0)
                    {
                        add2FoodStock(-feedFood);
                        Log.farm("喂〔" + user + "〕的小鸡〔" + feedFood + "克〕，剩余〔" + foodStock + "克〕");
                        Statistics.feedFriendToday(AntFarmRpcCall.farmId2UserId(friendFarmId));
                    }
                }else
                {
                    Log.recordLog(memo, s);
                }
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "feedFriendAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void notifyFriend(ClassLoader loader)
    {
        if(foodStock >= foodStockLimit) return;
        try
        {
            boolean hasNext = false;
            int pageStartSum = 0;
            String s;
            JSONObject jo;
            do
            {
                s = AntFarmRpcCall.rpcCall_rankingList(loader, pageStartSum);
                jo = new JSONObject(s);
                String memo = jo.getString("memo");
                if(memo.equals("SUCCESS"))
                {
                    hasNext = jo.getBoolean("hasNext");
                    JSONArray jaRankingList = jo.getJSONArray("rankingList");
                    pageStartSum += jaRankingList.length();
                    for(int i = 0; i < jaRankingList.length(); i++)
                    {
                        jo = jaRankingList.getJSONObject(i);
                        String userId = jo.getString("userId");
                        String userName = FriendIdMap.getNameById(userId);
                        if(Config.getDontNotifyFriendList().contains(userId)
                                || userId.equals(FriendIdMap.currentUid))
                            continue;
                        boolean starve = jo.has("actionType") &&  jo.getString("actionType").equals("starve_action");
                        if(jo.getBoolean("stealingAnimal") && !starve)
                        {
                            s = AntFarmRpcCall.rpcCall_enterFarm(loader, "", userId);
                            jo = new JSONObject(s);
                            memo = jo.getString("memo");
                            if(memo.equals("SUCCESS"))
                            {
                                jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
                                String friendFarmId = jo.getString("farmId");
                                JSONArray jaAnimals = jo.getJSONArray("animals");
                                boolean notified = !Config.notifyFriend();
                                for(int j = 0; j < jaAnimals.length(); j++)
                                {
                                    jo = jaAnimals.getJSONObject(j);
                                    String animalId = jo.getString("animalId");
                                    String masterFarmId = jo.getString("masterFarmId");
                                    if(!masterFarmId.equals(friendFarmId) && !masterFarmId.equals(ownerFarmId))
                                    {
                                        if(notified) continue;
                                        jo = jo.getJSONObject("animalStatusVO");
                                        notified = notifyFriend(loader, jo, friendFarmId, animalId, userName);
                                    }
                                }
                            }else
                            {
                                Log.recordLog(memo, s);
                            }
                        }
                    }
                }else
                {
                    Log.recordLog(memo, s);
                }
            }while(hasNext);
            Log.recordLog("饲料剩余〔" + foodStock + "克〕", "");
        }catch(Throwable t)
        {
            Log.i(TAG, "notifyFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static boolean notifyFriend(ClassLoader loader, JSONObject joAnimalStatusVO, String friendFarmId, String animalId, String user)
    {
        try
        {
            if(AnimalInteractStatus.STEALING.name().equals(joAnimalStatusVO.getString("animalInteractStatus"))
                    && AnimalFeedStatus.EATING.name().equals(joAnimalStatusVO.getString("animalFeedStatus")))
            {
                String s = AntFarmRpcCall.rpcCall_notifyFriend(loader, animalId, friendFarmId);
                JSONObject jo = new JSONObject(s);
                String memo = jo.getString("memo");
                if(memo.equals("SUCCESS"))
                {
                    double rewardCount = jo.getDouble("rewardCount");
                    if(jo.getBoolean("refreshFoodStock"))
                        foodStock = (int)jo.getDouble("finalFoodStock");
                    else
                        add2FoodStock((int)rewardCount);
                    Log.farm("通知〔" + user + "〕被偷吃，奖励〔" + rewardCount + "克〕");
                    return true;
                }else
                {
                    Log.recordLog(memo, s);
                }
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "notifyFriend err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void parseSyncAnimalStatusResponse(String resp)
    {
        try
        {
            JSONObject jo = new JSONObject(resp);
            jo = jo.getJSONObject("subFarmVO");
            ownerFarmId = jo.getString("farmId");
            benevolenceScore = jo.getJSONObject("farmProduce").getDouble("benevolenceScore");
            if(jo.has("rewardList"))
            {
                JSONArray jaRewardList = jo.getJSONArray("rewardList");
                if(jaRewardList.length() > 0)
                {
                    rewardList = new RewardFriend[jaRewardList.length()];
                    for(int i = 0; i < rewardList.length; i++)
                    {
                        JSONObject joRewardList = jaRewardList.getJSONObject(i);
                        if(rewardList[i] == null)rewardList[i] = new RewardFriend();
                        rewardList[i].consistencyKey = joRewardList.getString("consistencyKey");
                        rewardList[i].friendId = joRewardList.getString("friendId");
                        rewardList[i].time = joRewardList.getString("time");
                    }
                }
            }
            JSONArray jaAnimals = jo.getJSONArray("animals");
            animals = new Animal[jaAnimals.length()];
            for(int i = 0; i < animals.length; i++)
            {
                if(animals[i] == null)animals[i] = new Animal();
                jo = jaAnimals.getJSONObject(i);
                animals[i].animalId = jo.getString("animalId");
                animals[i].currentFarmId = jo.getString("currentFarmId");
                animals[i].masterFarmId = jo.getString("masterFarmId");
                animals[i].animalBuff = jo.getString("animalBuff");
                animals[i].subAnimalType = jo.getString("subAnimalType");
                jo = jo.getJSONObject("animalStatusVO");
                animals[i].animalFeedStatus = jo.getString("animalFeedStatus");
                animals[i].animalInteractStatus = jo.getString("animalInteractStatus");
                if(animals[i].masterFarmId.equals(ownerFarmId))
                    ownerAnimal = animals[i];
                Log.i("owner", "ownerFarmId=" + ownerFarmId);
                Log.i(i + " animal", "animalId=" + animals[i].animalId);
                Log.i(i + " animal", "currentFarmId=" + animals[i].currentFarmId);
                Log.i(i + " animal", "masterFarmId=" + animals[i].masterFarmId);
                Log.i(i + " animal", "animalBuff=" + animals[i].animalBuff);
                Log.i(i + " animal", "subAnimalType=" + animals[i].subAnimalType);
                Log.i(i + " animal", "animalFeedStatus=" + animals[i].animalFeedStatus);
                Log.i(i + " animal", "animalInteractStatus=" + animals[i].animalInteractStatus);
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "parseSyncAnimalStatusResponse err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void add2FoodStock(int i)
    {
        foodStock += i;
        if(foodStock > foodStockLimit) foodStock = foodStockLimit;
        if(foodStock < 0) foodStock = 0;
    }

}
