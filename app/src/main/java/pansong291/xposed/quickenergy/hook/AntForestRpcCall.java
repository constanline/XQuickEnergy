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
        return RpcCall.invoke(loader, "alipay.antforest.forest.h5.queryFriendHomePage",
                "[{\"canRobFlags\":\"F,F,F,F,F\",\"configVersionMap\":{\"redPacketConfig\":0,\"wateringBubbleConfig\":\"10\"},\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"userId\":\"" +
                        userId + "\",\"version\":\"20230501\"}]");
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
        return RpcCall.invoke(loader, "alipay.antmember.forest.h5.transferEnergy", "[{\"bizNo\":\"" +
                bizNo + UUID.randomUUID().toString() + "\",\"energyId\":" + energyId +
                ",\"extInfo\":{\"sendChat\":\"N\"},\"from\":\"friendIndex\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetUser\":\"" +
                targetUser + "\",\"transferType\":\"WATERING\",\"version\":\"20230501\"}]");
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

    public static String vitalitySign(ClassLoader loader) {
        return RpcCall.invoke(loader, "alipay.antforest.forest.h5.vitalitySign",
                "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    public static String queryTaskList(ClassLoader loader) {
        return RpcCall.invoke(loader, "alipay.antforest.forest.h5.queryTaskList",
                "[{\"extend\":{},\"fromAct\":\"home_task_list\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"20230501\"}]");
    }

    public static String queryEnergyRainHome(ClassLoader classLoader) {
        return RpcCall.invoke(classLoader, "alipay.antforest.forest.h5.queryEnergyRainHome", "[{}]");
    }

    public static String queryEnergyRainCanGrantList(ClassLoader classLoader) {
        return RpcCall.invoke(classLoader, "alipay.antforest.forest.h5.queryEnergyRainCanGrantList", "[{}]");
    }

    public static String grantEnergyRainChance(ClassLoader classLoader, String targetUserId) {
        return RpcCall.invoke(classLoader, "alipay.antforest.forest.h5.grantEnergyRainChance",
                "[{\"targetUserId\":" + targetUserId + "}]");
    }

    public static String startEnergyRain(ClassLoader classLoader) {
        return RpcCall.invoke(classLoader, "alipay.antforest.forest.h5.startEnergyRain", "[{}]");
    }
    public static String energyRainSettlement(ClassLoader classLoader, int saveEnergy, String token) {
        return RpcCall.invoke(classLoader, "alipay.antforest.forest.h5.energyRainSettlement",
                "[{\"activityPropNums\":0,\"saveEnergy\":" + saveEnergy + ",\"token\":\"" + token + "\"}]");
    }

    public static String receiveTaskAward(ClassLoader loader, String sceneCode, String taskType) {
        return RpcCall.invoke(loader, "com.alipay.antiep.receiveTaskAward",
                "[{\"ignoreLimit\":false,\"requestType\":\"H5\",\"sceneCode\":\"" + sceneCode +
                        "\",\"source\":\"ANTFOREST\",\"taskType\":\"" + taskType + "\"}]");
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
