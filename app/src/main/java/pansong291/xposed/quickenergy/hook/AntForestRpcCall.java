package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.Log;

import java.util.UUID;

public class AntForestRpcCall {
    private static final String TAG = AntForestRpcCall.class.getCanonicalName();


    public static String fillUserRobFlag(String userIdList) {
        return RpcUtil.request("alipay.antforest.forest.h5.fillUserRobFlag",
                "[{\"userIdList\":" + userIdList + "}]");
    }

    public static String queryEnergyRanking() {
        return RpcUtil.request("alipay.antmember.forest.h5.queryEnergyRanking",
                "[{\"periodType\":\"day\",\"rankType\":\"energyRank\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"20230501\"}]");
    }

    public static String queryHomePage() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryHomePage",
                "[{\"configVersionMap\":{\"redPacketConfig\":0,\"wateringBubbleConfig\":\"10\"},\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"20230501\"}]");
    }

    public static String queryFriendHomePage(String userId) {
        return RpcUtil.request("alipay.antforest.forest.h5.queryFriendHomePage",
                "[{\"canRobFlags\":\"F,F,F,F,F\",\"configVersionMap\":{\"redPacketConfig\":0,\"wateringBubbleConfig\":\"10\"},\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"userId\":\"" +
                        userId + "\",\"version\":\"20230501\"}]");
    }

    public static String collectEnergy(String userId, long bubbleId) {
        String args1 = "[{\"bubbleIds\":[" + bubbleId + "],\"userId\":\"" + userId + "\"}]";
        return RpcUtil.request("alipay.antmember.forest.h5.collectEnergy", args1);
    }

    public static String transferEnergy(String targetUser, String bizNo, int energyId) {
        return RpcUtil.request("alipay.antmember.forest.h5.transferEnergy", "[{\"bizNo\":\"" +
                bizNo + UUID.randomUUID().toString() + "\",\"energyId\":" + energyId +
                ",\"extInfo\":{\"sendChat\":\"N\"},\"from\":\"friendIndex\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetUser\":\"" +
                targetUser + "\",\"transferType\":\"WATERING\",\"version\":\"20230501\"}]");
    }

    public static String forFriendCollectEnergy(String targetUserId, long bubbleId) {
        try {
            String args1 = "[{\"bubbleIds\":[" + bubbleId + "],\"targetUserId\":\"" + targetUserId + "\"}]";
            return RpcUtil.request("alipay.antmember.forest.h5.forFriendCollectEnergy", args1);
        } catch (Throwable t) {
            Log.i(TAG, "forFriendCollectEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String vitalitySign() {
        return RpcUtil.request("alipay.antforest.forest.h5.vitalitySign",
                "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    public static String queryTaskList() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryTaskList",
                "[{\"extend\":{},\"fromAct\":\"home_task_list\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"20230501\"}]");
    }

    public static String queryEnergyRainHome() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryEnergyRainHome", "[{}]");
    }

    public static String queryEnergyRainCanGrantList() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryEnergyRainCanGrantList", "[{}]");
    }

    public static String grantEnergyRainChance(String targetUserId) {
        return RpcUtil.request("alipay.antforest.forest.h5.grantEnergyRainChance",
                "[{\"targetUserId\":" + targetUserId + "}]");
    }

    public static String startEnergyRain() {
        return RpcUtil.request("alipay.antforest.forest.h5.startEnergyRain", "[{}]");
    }
    public static String energyRainSettlement(int saveEnergy, String token) {
        return RpcUtil.request("alipay.antforest.forest.h5.energyRainSettlement",
                "[{\"activityPropNums\":0,\"saveEnergy\":" + saveEnergy + ",\"token\":\"" + token + "\"}]");
    }

    public static String receiveTaskAward(String sceneCode, String taskType) {
        return RpcUtil.request("com.alipay.antiep.receiveTaskAward",
                "[{\"ignoreLimit\":false,\"requestType\":\"H5\",\"sceneCode\":\"" + sceneCode +
                        "\",\"source\":\"ANTFOREST\",\"taskType\":\"" + taskType + "\"}]");
    }

    public static String popupTask() {
        return RpcUtil.request("alipay.antforest.forest.h5.popupTask",
                "[{\"fromAct\":\"pop_task\",\"needInitSign\":false,\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"statusList\":[\"TODO\",\"FINISHED\"],\"version\":\"20230501\"}]");
    }

    public static String antiepSign(String entityId, String userId) {
        return RpcUtil.request("com.alipay.antiep.sign",
                "[{\"entityId\":\"" + entityId +
                        "\",\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_ENERGY_SIGN\",\"source\":\"ANTFOREST\",\"userId\":\"" +
                        userId + "\"}]");
    }

}
