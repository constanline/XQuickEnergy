package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.StringUtil;

import pansong291.xposed.quickenergy.util.RandomUtils;

import java.util.UUID;

public class AntForestRpcCall {

    private static final String VERSION = "20230925";

    private static String getUniqueId() {
            return String.valueOf(System.currentTimeMillis()) + RandomUtils.nextLong();
    }

    public static String fillUserRobFlag(String userIdList) {
            return RpcUtil.request("alipay.antforest.forest.h5.fillUserRobFlag",
                            "[{\"userIdList\":" + userIdList + "}]");
    }

    public static String queryEnergyRanking() {
        return RpcUtil.request("alipay.antmember.forest.h5.queryEnergyRanking",
                "[{\"periodType\":\"day\",\"rankType\":\"energyRank\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String queryHomePage() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryHomePage",
                "[{\"configVersionMap\":{\"wateringBubbleConfig\":\"0\"},\"skipWhackMole\":false,\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String queryFriendHomePage(String userId) {
        return RpcUtil.request("alipay.antforest.forest.h5.queryFriendHomePage",
                "[{\"canRobFlags\":\"F,F,F,F,F\",\"configVersionMap\":{\"redPacketConfig\":0,\"wateringBubbleConfig\":\"10\"},\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"userId\":\""
                        + userId + "\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String collectEnergy(String bizType, String userId, long bubbleId) {
        String args1;
        if (StringUtil.isEmpty(bizType)) {
            args1 = "[{\"bizType\":\"\",\"bubbleIds\":[" + bubbleId
                    + "],\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"userId\":\"" + userId + "\",\"version\":\""
                    + VERSION + "\"}]";
        } else {
            args1 = "[{\"bizType\":\"" + bizType + "\",\"bubbleIds\":[" + bubbleId
                    + "],\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"userId\":\"" + userId + "\"}]";
        }
        return RpcUtil.request("alipay.antmember.forest.h5.collectEnergy", args1);
    }

    public static String transferEnergy(String targetUser, String bizNo, int energyId) {
        return RpcUtil.request("alipay.antmember.forest.h5.transferEnergy", "[{\"bizNo\":\"" +
                bizNo + UUID.randomUUID().toString() + "\",\"energyId\":" + energyId +
                ",\"extInfo\":{\"sendChat\":\"N\"},\"from\":\"friendIndex\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetUser\":\""
                + targetUser + "\",\"transferType\":\"WATERING\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String forFriendCollectEnergy(String targetUserId, long bubbleId) {
        String args1 = "[{\"bubbleIds\":[" + bubbleId + "],\"targetUserId\":\"" + targetUserId + "\"}]";
        return RpcUtil.request("alipay.antmember.forest.h5.forFriendCollectEnergy", args1);
    }

    public static String vitalitySign() {
        return RpcUtil.request("alipay.antforest.forest.h5.vitalitySign",
                "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    public static String queryTaskList() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryTaskList",
                "[{\"extend\":{},\"fromAct\":\"home_task_list\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String queryEnergyRainHome() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryEnergyRainHome", "[{\"version\":\"" + VERSION + "\"}]");
    }

    public static String queryEnergyRainCanGrantList() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryEnergyRainCanGrantList", "[{}]");
    }

    public static String grantEnergyRainChance(String targetUserId) {
        return RpcUtil.request("alipay.antforest.forest.h5.grantEnergyRainChance",
                "[{\"targetUserId\":" + targetUserId + "}]");
    }

    public static String startEnergyRain() {
        return RpcUtil.request("alipay.antforest.forest.h5.startEnergyRain", "[{\"version\":\"" + VERSION + "\"}]");
    }

    public static String energyRainSettlement(int saveEnergy, String token) {
        return RpcUtil.request("alipay.antforest.forest.h5.energyRainSettlement",
                "[{\"activityPropNums\":0,\"saveEnergy\":" + saveEnergy + ",\"token\":\"" + token + "\",\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String receiveTaskAward(String sceneCode, String taskType) {
        return RpcUtil.request("com.alipay.antiep.receiveTaskAward",
                "[{\"ignoreLimit\":false,\"requestType\":\"H5\",\"sceneCode\":\"" + sceneCode +
                        "\",\"source\":\"ANTFOREST\",\"taskType\":\"" + taskType + "\"}]");
    }

    public static String finishTask(String sceneCode, String taskType) {
        String outBizNo = taskType + "_" + RandomUtils.nextDouble();
        return RpcUtil.request("com.alipay.antiep.finishTask",
                "[{\"outBizNo\":\"" + outBizNo + "\",\"requestType\":\"H5\",\"sceneCode\":\"" +
                        sceneCode + "\",\"source\":\"ANTFOREST\",\"taskType\":\"" + taskType + "\"}]");
    }

    public static String popupTask() {
        return RpcUtil.request("alipay.antforest.forest.h5.popupTask",
                "[{\"fromAct\":\"pop_task\",\"needInitSign\":false,\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"statusList\":[\"TODO\",\"FINISHED\"],\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String antiepSign(String entityId, String userId) {
        return RpcUtil.request("com.alipay.antiep.sign",
                "[{\"entityId\":\"" + entityId
                        + "\",\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_ENERGY_SIGN\",\"source\":\"ANTFOREST\",\"userId\":\""
                        + userId + "\"}]");
    }

    public static String queryPropList(boolean onlyGive) {
        return RpcUtil.request("alipay.antforest.forest.h5.queryPropList",
                "[{\"onlyGive\":\"" + (onlyGive ? "Y" : "")
                        + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String giveProp(String giveConfigId, String propId, String targetUserId) {
        return RpcUtil.request("alipay.antforest.forest.h5.giveProp",
                "[{\"giveConfigId\":\"" + giveConfigId + "\",\"propId\":\"" + propId
                        + "\",\"source\":\"self_corner\",\"targetUserId\":\"" + targetUserId + "\"}]");
    }

    public static String collectProp(String giveConfigId, String giveId) {
        return RpcUtil.request("alipay.antforest.forest.h5.collectProp",
                "[{\"giveConfigId\":\"" + giveConfigId + "\",\"giveId\":\"" + giveId
                        + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    public static String consumeProp(String propId, String propType) {
        return RpcUtil.request("alipay.antforest.forest.h5.consumeProp",
                "[{\"propId\":\"" + propId + "\",\"propType\":\"" + propType +
                        "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"timezoneId\":\"Asia/Shanghai\",\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String itemList(String labelType) {
        return RpcUtil.request("com.alipay.antiep.itemList",
                "[{\"extendInfo\":\"{}\",\"labelType\":\"" + labelType
                        + "\",\"pageSize\":20,\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"source\":\"afEntry\",\"startIndex\":0}]");
    }

    public static String itemDetail(String spuId) {
        return RpcUtil.request("com.alipay.antiep.itemDetail",
                "[{\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"source\":\"afEntry\",\"spuId\":\""
                        + spuId + "\"}]");
    }

    public static String queryVitalityStoreIndex() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryVitalityStoreIndex",
                "[{\"source\":\"afEntry\"}]");
    }

    public static String exchangeBenefit(String spuId, String skuId) {
        return RpcUtil.request("com.alipay.antcommonweal.exchange.h5.exchangeBenefit",
                "[{\"sceneCode\":\"ANTFOREST_VITALITY\",\"requestId\":\"" + System.currentTimeMillis()
                        + "_" + RandomUtils.getRandom(17) + "\",\"spuId\":\"" +
                        spuId + "\",\"skuId\":\"" + skuId + "\",\"source\":\"GOOD_DETAIL\"}]");
    }

    public static String testH5Rpc(String operationTpye, String requestDate) {
        return RpcUtil.request(operationTpye, requestDate);
    }

    /* 神奇物种 */

    public static String queryAnimalStatus() {
        return RpcUtil.request("alipay.antdodo.rpc.h5.queryAnimalStatus",
                "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    public static String antdodoHomePage() {
        return RpcUtil.request("alipay.antdodo.rpc.h5.homePage",
                "[{}]");
    }

    public static String taskEntrance() {
        return RpcUtil.request("alipay.antdodo.rpc.h5.taskEntrance",
                "[{\"statusList\":[\"TODO\",\"FINISHED\"]}]");
    }

    public static String antdodoCollect() {
        return RpcUtil.request("alipay.antdodo.rpc.h5.collect",
                "[{}]");
    }

    public static String antdodoTaskList() {
        return RpcUtil.request("alipay.antdodo.rpc.h5.taskList",
                "[{}]");
    }

    public static String antdodoFinishTask(String sceneCode, String taskType) {
        String uniqueId = getUniqueId();
        return RpcUtil.request("com.alipay.antiep.finishTask",
                "[{\"outBizNo\":\"" + uniqueId + "\",\"requestType\":\"rpc\",\"sceneCode\":\""
                        + sceneCode + "\",\"source\":\"af-biodiversity\",\"taskType\":\""
                        + taskType + "\",\"uniqueId\":\"" + uniqueId + "\"}]");
    }

    public static String antdodoReceiveTaskAward(String sceneCode, String taskType) {
        return RpcUtil.request("com.alipay.antiep.receiveTaskAward",
                "[{\"ignoreLimit\":0,\"requestType\":\"rpc\",\"sceneCode\":\"" + sceneCode
                        + "\",\"source\":\"af-biodiversity\",\"taskType\":\"" + taskType
                        + "\"}]");
    }

    public static String antdodoPropList() {
        return RpcUtil.request("alipay.antdodo.rpc.h5.propList",
                "[{}]");
    }

    public static String antdodoConsumeProp(String propId, String propType) {
            return RpcUtil.request("alipay.antdodo.rpc.h5.consumeProp",
                            "[{\"propId\":\"" + propId + "\",\"propType\":\"" + propType + "\"}]");
    }

    public static String queryBookInfo(String bookId) {
            return RpcUtil.request("alipay.antdodo.rpc.h5.queryBookInfo",
                            "[{\"bookId\":\"" + bookId + "\"}]");
    }

    // 送卡片给好友
    public static String antdodoSocial(String targetAnimalId, String targetUserId) {
            return RpcUtil.request("alipay.antdodo.rpc.h5.social",
                            "[{\"actionCode\":\"GIFT_TO_FRIEND\",\"source\":\"GIFT_TO_FRIEND_FROM_CC\",\"targetAnimalId\":\""
                                            + targetAnimalId + "\",\"targetUserId\":\"" + targetUserId
                                            + "\",\"triggerTime\":\"" + System.currentTimeMillis() + "\"}]");
    }

    /* 巡护保护地 */
    public static String queryUserPatrol() {
            return RpcUtil.request("alipay.antforest.forest.h5.queryUserPatrol",
                            "[{\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]");
    }

    public static String patrolGo(int nodeIndex, int patrolId) {
        return RpcUtil.request("alipay.antforest.forest.h5.patrolGo",
                "[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                        + ",\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]");
    }

    public static String patrolKeepGoing(int nodeIndex, int patrolId, String eventType) {
        String args = null;
        switch (eventType) {
            case "video":
                args = "[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                        + ",\"reactParam\":{\"viewed\":\"Y\"},\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]";
                break;
            case "chase":
                args = "[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                        + ",\"reactParam\":{\"sendChat\":\"Y\"},\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]";
                break;
            case "quiz":
                args = "[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                        + ",\"reactParam\":{\"answer\":\"correct\"},\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]";
                break;
            default:
                args = "[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                        + ",\"reactParam\":{},\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]";
                break;
        }
        return RpcUtil.request("alipay.antforest.forest.h5.patrolKeepGoing", args);
    }

    public static String exchangePatrolChance(int costStep) {
        return RpcUtil.request("alipay.antforest.forest.h5.exchangePatrolChance",
                "[{\"costStep\":" + costStep + ",\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]");
    }

    public static String queryAnimalAndPiece(int animalId) {
        String args = null;
        if (animalId != 0) {
            args = "[{\"animalId\":" + animalId + ",\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]";
        } else {
            args = "[{\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\",\"withDetail\":\"N\",\"withGift\":true}]";
        }
        return RpcUtil.request("alipay.antforest.forest.h5.queryAnimalAndPiece", args);
    }

    public static String combineAnimalPiece(int animalId, String piecePropIds) {
        return RpcUtil.request("alipay.antforest.forest.h5.combineAnimalPiece",
                "[{\"animalId\":" + animalId + ",\"piecePropIds\":" + piecePropIds
                        + ",\"timezoneId\":\"Asia/Shanghai\",\"source\":\"ant_forest\"}]");
    }

    public static String AnimalConsumeProp(String propGroup, String propId, String propType) {
        return RpcUtil.request("alipay.antforest.forest.h5.consumeProp",
                "[{\"propGroup\":\"" + propGroup + "\",\"propId\":\"" + propId + "\",\"propType\":\"" + propType
                        + "\",\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]");
    }

    public static String collectAnimalRobEnergy(String propId, String propType, String shortDay) {
        return RpcUtil.request("alipay.antforest.forest.h5.collectAnimalRobEnergy",
                "[{\"propId\":\"" + propId + "\",\"propType\":\"" + propType + "\",\"shortDay\":\"" + shortDay
                        + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    /* 复活能量 */
    public static String protectBubble(String targetUserId) {
        return RpcUtil.request("alipay.antforest.forest.h5.protectBubble",
                "[{\"source\":\"ANT_FOREST_H5\",\"targetUserId\":\"" + targetUserId + "\",\"version\":\"" + VERSION
                        + "\"}]");
    }

    /* 森林礼盒 */
    public static String collectFriendGiftBox(String targetId, String targetUserId) {
        return RpcUtil.request("alipay.antforest.forest.h5.collectFriendGiftBox",
                "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetId\":\"" + targetId
                        + "\",\"targetUserId\":\"" + targetUserId + "\"}]");
    }
}
