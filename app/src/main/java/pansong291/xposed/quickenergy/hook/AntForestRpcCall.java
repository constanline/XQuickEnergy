package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.Log;

import java.util.UUID;

public class AntForestRpcCall {
    private static final String TAG = AntForestRpcCall.class.getCanonicalName();


    public static String fillUserRobFlag(ClassLoader classLoader, String userIdList) {
        try {
            return RpcCall.invoke(classLoader, "alipay.antforest.forest.h5.fillUserRobFlag",
                    "[{\"userIdList\":" + userIdList + "}]");
        } catch (Throwable th) {
            Log.i(TAG, "fillUserRobFlag err:");
            Log.printStackTrace(TAG, th);
            return "{}";
        }
    }

    public static String queryEnergyRanking(ClassLoader loader) {
        try {
            return RpcCall.invoke(loader, "alipay.antmember.forest.h5.queryEnergyRanking",
                    "[{\"periodType\":\"day\",\"rankType\":\"energyRank\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"20230501\"}]");
        } catch (Throwable t) {
            Log.i(TAG, "queryEnergyRanking err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String queryHomePage(ClassLoader loader) {
        try {
            return RpcCall.invoke(loader, "alipay.antforest.forest.h5.queryHomePage",
                    "[{\"configVersionMap\":{\"redPacketConfig\":0,\"wateringBubbleConfig\":\"10\"},\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"20230501\"}]");
        } catch (Throwable t) {
            Log.i(TAG, "queryHomePage err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String queryFriendHomePage(ClassLoader loader, String userId) {
        try {
            return RpcCall.invoke(loader, "alipay.antforest.forest.h5.queryFriendHomePage",
                    "[{\"canRobFlags\":\"F,F,F,F,F\",\"configVersionMap\":{\"redPacketConfig\":0,\"wateringBubbleConfig\":\"10\"},\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"userId\":\"" +
                            userId + "\",\"version\":\"20230501\"}]");
        } catch (Throwable t) {
            Log.i(TAG, "queryHomePage err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String collectEnergy(ClassLoader loader, String userId, long bubbleId) {
        try {
            String args1 = "[{\"bubbleIds\":[" + bubbleId + "],\"userId\":\"" + userId + "\"}]";
            return RpcCall.invoke(loader, "alipay.antmember.forest.h5.collectEnergy", args1);
        } catch (Throwable t) {
            Log.i(TAG, "collectEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String transferEnergy(ClassLoader loader, String targetUser, String bizNo, int energyId) {
        try {
            return RpcCall.invoke(loader, "alipay.antmember.forest.h5.transferEnergy", "[{\"bizNo\":\"" +
                    bizNo + UUID.randomUUID().toString() + "\",\"energyId\":" + energyId +
                    ",\"extInfo\":{\"sendChat\":\"N\"},\"from\":\"friendIndex\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetUser\":\"" +
                    targetUser + "\",\"transferType\":\"WATERING\",\"version\":\"20230501\"}]");
        } catch (Throwable t) {
            Log.i(TAG, "transferEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String forFriendCollectEnergy(ClassLoader loader, String targetUserId, long bubbleId) {
        try {
            String args1 = "[{\"bubbleIds\":[" + bubbleId + "],\"targetUserId\":\"" + targetUserId + "\"}]";
            return RpcCall.invoke(loader, "alipay.antmember.forest.h5.forFriendCollectEnergy", args1);
        } catch (Throwable t) {
            Log.i(TAG, "forFriendCollectEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String queryTaskList(ClassLoader loader) {
        try {
            return RpcCall.invoke(loader, "alipay.antforest.forest.h5.queryTaskList",
                    "[{\"extend\":{},\"fromAct\":\"home_task_list\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"20230501\"}]");
        } catch (Throwable t) {
            Log.i(TAG, "queryTaskList err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String receiveTaskAward(ClassLoader loader, String sceneCode, String taskType) {
        try {
            return RpcCall.invoke(loader, "com.alipay.antiep.receiveTaskAward",
                    "[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"" + sceneCode +
                            "\",\"source\":\"ch_appcenter__chsub_9patch\",\"taskType\":\"" + taskType +
                            "\",\"version\":\"0.1.2307042001.2\"}]");
        } catch (Throwable t) {
            Log.i(TAG, "receiveTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String popupTask(ClassLoader loader) {
        return RpcCall.invoke(loader, "alipay.antforest.forest.h5.popupTask",
                "[{\"fromAct\":\"pop_task\",\"needInitSign\":false,\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"statusList\":[\"TODO\",\"FINISHED\"],\"version\":\"20230501\"}]");
    }

    public static String antiepSign(ClassLoader loader, String entityId, String userId) {
        return RpcCall.invoke(loader, "com.alipay.antiep.sign",
                "[{\"entityId\":\"" + entityId +
                        "\",\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_ENERGY_SIGN\",\"source\":\"ANTFOREST\",\"userId\":\"" +
                        userId + "\"}]");
    }

}
