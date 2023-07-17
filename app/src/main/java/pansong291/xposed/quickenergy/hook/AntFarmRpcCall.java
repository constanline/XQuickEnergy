package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.Log;

public class AntFarmRpcCall
{
    private static final String TAG = AntFarmRpcCall.class.getCanonicalName();

    private static final String VERSION = "1.8.2302070202.46";

    public static String enterFarm(ClassLoader loader, String farmId, String userId) {
        return RpcUtil.request("com.alipay.antfarm.enterFarm",
                "[{\"animalId\":\"\",\"farmId\":\"" + farmId +
                        "\",\"gotoneScene\":\"\",\"gotoneTemplateId\":\"\",\"masterFarmId\":\"\",\"queryLastRecordNum\":true,\"recall\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"ANTFOREST\",\"touchRecordId\":\"\",\"userId\":\"" +
                userId + "\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String rpcCall_syncAnimalStatus(ClassLoader loader, String farmId)
    {
        try
        {
            String args1 = "[{\"farmId\":\"" + farmId +
                    "\",\"operType\":\"FEEDSYNC\",\"queryFoodStockInfo\":false,\"recall\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"userId\":\""
                    + farmId2UserId(farmId) + "\",\"version\":\"" + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.syncAnimalStatus", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_syncAnimalStatus err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rewardFriend(ClassLoader loader, String consistencyKey, String friendId, String productNum, String time)
    {
        String args1 = "[{\"canMock\":true,\"consistencyKey\":\"" + consistencyKey
                + "\",\"friendId\":\"" + friendId + "\",\"operType\":\"1\",\"productNum\":" + productNum +
                ",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"time\":"
                + time + ",\"version\":\"" + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.rewardFriend", args1);
    }

    public static String recallAnimal(ClassLoader loader, String animalId, String currentFarmId, String masterFarmId)
    {
        String args1 = "[{\"animalId\":\"" + animalId + "\",\"currentFarmId\":\""
                + currentFarmId + "\",\"masterFarmId\":\"" + masterFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.recallAnimal", args1);
    }

    public static String orchardRecallAnimal(ClassLoader loader, String animalId, String userId)
    {
        String args1 = "[{\"animalId\":\"" + animalId + "\",\"orchardUserId\":\"" + userId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"zhuangyuan_zhaohuixiaoji\",\"version\":\"\"0.1.2307042001.2\"\"}]";
        return RpcUtil.request("com.alipay.antorchard.recallAnimal", args1);
    }

    public static String sendBackAnimal(ClassLoader loader, String sendType, String animalId, String currentFarmId, String masterFarmId)
    {
        String args1 = "[{\"animalId\":\"" + animalId + "\",\"currentFarmId\":\""
                + currentFarmId + "\",\"masterFarmId\":\"" + masterFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"sendType\":\""
                + sendType + "\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.sendBackAnimal", args1);
    }

    public static String rpcCall_harvestProduce(ClassLoader loader, String farmId)
    {
        try
        {
            String args1 = "[{\"canMock\":true,\"farmId\":\"" + farmId +
                    "\",\"giftType\":\"\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.harvestProduce", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_harvestProduce err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_listActivityInfo(ClassLoader loader)
    {
        try
        {
            String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.listActivityInfo", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_listActivityInfo err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_donation(ClassLoader loader, String activityId)
    {
        try
        {
            String args1 = "[{\"activityId\":\"" + activityId +
                    "\",\"donationAmount\":5,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.donation", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_donation err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String listFarmTask(ClassLoader loader) {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.listFarmTask", args1);
    }

    public static String rpcCall_getAnswerInfo(ClassLoader loader)
    {
        try
        {
            String args1 = "[{\"answerSource\":\"foodTask\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.getAnswerInfo", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_getAnswerInfo err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_answerQuestion(ClassLoader loader, String quesId, int answer)
    {
        try
        {
            String args1 = "[{\"answers\":\"[{\\\"questionId\\\":\\\"" + quesId + "\\\",\\\"answers\\\":[" + answer +
                    "]}]\",\"bizkey\":\"ANSWER\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.doFarmTask", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_answerQuestion err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String receiveFarmTaskAward(ClassLoader loader, String taskId)
    {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskId\":\""
                + taskId + "\",\"version\":\"" + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.receiveFarmTaskAward", args1);
    }

    public static String rpcCall_listToolTaskDetails(ClassLoader loader)
    {
        try
        {
            String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.listToolTaskDetails", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_listToolTaskDetails err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_receiveToolTaskReward(ClassLoader loader, String awardType, int rewardCount, String taskType)
    {
        try
        {
            String args1 = "[{\"awardType\":\"" + awardType +
                    "\",\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"rewardCount\":"
                    + rewardCount + ",\"rewardType\":\"" + awardType +
                    "\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskType\":\""
                    + taskType + "\",\"version\":\"" + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.receiveToolTaskReward", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_receiveToolTaskReward err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_feedAnimal(ClassLoader loader, String farmId)
    {
        try
        {
            String args1 = "[{\"animalType\":\"CHICK\",\"canMock\":true,\"farmId\":\"" + farmId +
                    "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.feedAnimal", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_feedAnimal err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String listFarmTool(ClassLoader loader)
    {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.listFarmTool", args1);
    }

    public static String useFarmTool(ClassLoader loader, String targetFarmId, String toolId, String toolType)
    {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"targetFarmId\":\""
                + targetFarmId + "\",\"toolId\":\"" + toolId + "\",\"toolType\":\"" + toolType + "\",\"version\":\"" + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.useFarmTool", args1);
    }

    public static String rpcCall_rankingList(ClassLoader loader, int pageStartSum)
    {
        try
        {
            String args1 = "[{\"pageSize\":20,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"startNum\":"
                    + pageStartSum + ",\"version\":\"" + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.rankingList", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_rankingList err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_notifyFriend(ClassLoader loader, String animalId, String notifiedFarmId)
    {
        try
        {
            String args1 = "[{\"animalId\":\"" + animalId +
                    "\",\"animalType\":\"CHICK\",\"canBeGuest\":true,\"notifiedFarmId\":\"" + notifiedFarmId +
                    "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.notifyFriend", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_notifyFriend err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_feedFriendAnimal(ClassLoader loader, String friendFarmId)
    {
        try
        {
            String args1 = "[{\"animalType\":\"CHICK\",\"canMock\":true,\"friendFarmId\":\"" + friendFarmId +
                    "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]";
            return RpcUtil.request("com.alipay.antfarm.feedFriendAnimal", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_feedFriendAnimal err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String farmId2UserId(String farmId)
    {
        int l = farmId.length() / 2;
        return farmId.substring(l);
    }

    public static String collectManurePot(ClassLoader loader, String manurePotNO) {
        return RpcUtil.request("com.alipay.antfarm.collectManurePot","[{\"manurePotNOs\":\"" + manurePotNO +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION + "\"}]");
    }

}
