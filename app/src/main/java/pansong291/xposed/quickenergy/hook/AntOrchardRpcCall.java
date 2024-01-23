package pansong291.xposed.quickenergy.hook;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import pansong291.xposed.quickenergy.util.RandomUtils;

import java.util.List;

public class AntOrchardRpcCall {
        private static final String VERSION = "0.1.2401111000.31";

        public static String orchardIndex() {
                return RpcUtil.request("com.alipay.antfarm.orchardIndex",
                                "[{\"inHomepage\":\"true\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String mowGrassInfo() {
                return RpcUtil.request("com.alipay.antorchard.mowGrassInfo",
                                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"showRanking\":true,\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String batchHireAnimalRecommend(String orchardUserId) {
                return RpcUtil.request("com.alipay.antorchard.batchHireAnimalRecommend",
                                "[{\"orchardUserId\":\"" + orchardUserId
                                                + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"sceneType\":\"weed\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String batchHireAnimal(List<String>  recommendGroupList) {
                return RpcUtil.request("com.alipay.antorchard.batchHireAnimal",
                                "[{\"recommendGroupList\":[" + String.join(",", recommendGroupList)
                                                + "],\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"sceneType\":\"weed\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String extraInfoGet() {
                return RpcUtil.request("com.alipay.antorchard.extraInfoGet",
                                "[{\"from\":\"entry\",\"requestType\":\"NORMAL\",\"sceneCode\":\"FUGUO\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String extraInfoSet() {
                return RpcUtil.request("com.alipay.antorchard.extraInfoSet",
                                "[{\"bizCode\":\"fertilizerPacket\",\"bizParam\":{\"action\":\"queryCollectFertilizerPacket\"},\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String querySubplotsActivity(String treeLevel) {
                return RpcUtil.request("com.alipay.antorchard.querySubplotsActivity",
                                "[{\"activityType\":[\"WISH\",\"BATTLE\",\"HELP_FARMER\",\"DEFOLIATION\",\"CAMP_TAKEOVER\"],\"inHomepage\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"treeLevel\":\""
                                                + treeLevel + "\",\"version\":\"" + VERSION + "\"}]");
        }

        public static String triggerSubplotsActivity(String activityId, String activityType, String optionKey) {
                return RpcUtil.request("com.alipay.antorchard.triggerSubplotsActivity",
                                "[{\"activityId\":\"" + activityId + "\",\"activityType\":\"" + activityType
                                                + "\",\"optionKey\":\"" + optionKey
                                                + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String receiveOrchardRights(String activityId, String activityType) {
                return RpcUtil.request("com.alipay.antorchard.receiveOrchardRights",
                                "[{\"activityId\":\"" + activityId + "\",\"activityType\":\"" + activityType
                                                + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        /* 七日礼包 */
        public static String drawLottery() {
                return RpcUtil.request("com.alipay.antorchard.drawLottery",
                                "[{\"lotteryScene\":\"receiveLotteryPlus\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String orchardSyncIndex() {
                return RpcUtil.request("com.alipay.antorchard.orchardSyncIndex",
                                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"syncIndexTypes\":\"QUERY_MAIN_ACCOUNT_INFO\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String orchardSpreadManure(String wua) {
                return RpcUtil.request("com.alipay.antfarm.orchardSpreadManure",
                                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"useWua\":true,\"version\":\""
                                                + VERSION + "\",\"wua\":\"" + wua + "\"}]");
        }

        public static String receiveTaskAward(String sceneCode, String taskType) {
                return RpcUtil.request("com.alipay.antiep.receiveTaskAward",
                                "[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"" + sceneCode
                                                + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"taskType\":\""
                                                + taskType + "\",\"version\":\"" + VERSION + "\"}]");
        }

        public static String orchardListTask() {
                return RpcUtil.request("com.alipay.antfarm.orchardListTask",
                                "[{\"plantHiddenMMC\":\"false\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String orchardSign() {
                return RpcUtil.request("com.alipay.antfarm.orchardSign",
                                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"signScene\":\"ANTFARM_ORCHARD_SIGN_V2\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String finishTask(String userId, String sceneCode, String taskType) {
                return RpcUtil.request("com.alipay.antiep.finishTask",
                                "[{\"outBizNo\":\"" + userId + System.currentTimeMillis()
                                                + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"" + sceneCode
                                                + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"taskType\":\""
                                                + taskType + "\",\"userId\":\"" + userId + "\",\"version\":\"" + VERSION
                                                + "\"}]");
        }

        public static String triggerTbTask(String taskId, String taskPlantType) {
                return RpcUtil.request("com.alipay.antfarm.triggerTbTask",
                                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"taskId\":\""
                                                + taskId + "\",\"taskPlantType\":\"" + taskPlantType
                                                + "\",\"version\":\"" + VERSION + "\"}]");
        }

        public static String orchardSelectSeed() {
                return RpcUtil.request("com.alipay.antfarm.orchardSelectSeed",
                                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"seedCode\":\"rp\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        /* 砸金蛋 */
        public static String queryGameCenter() {
                return RpcUtil.request("com.alipay.antorchard.queryGameCenter",
                                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String noticeGame(String appId) {
                return RpcUtil.request("com.alipay.antorchard.noticeGame",
                                "[{\"appId\":\"" + appId
                                                + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String submitUserAction(String gameId) {
                return RpcUtil.request("com.alipay.gamecenteruprod.biz.rpc.v3.submitUserAction",
                                "[{\"actionCode\":\"enterGame\",\"gameId\":\"" + gameId
                                                + "\",\"paladinxVersion\":\"2.0.13\",\"source\":\"gameFramework\"}]");
        }

        public static String submitUserPlayDurationAction(String gameAppId, String source) {
                return RpcUtil.request("com.alipay.gamecenteruprod.biz.rpc.v3.submitUserPlayDurationAction",
                                "[{\"gameAppId\":\"" + gameAppId + "\",\"playTime\":32,\"source\":\"" + source
                                                + "\",\"statisticTag\":\"\"}]");
        }

        public static String smashedGoldenEgg() {
                return RpcUtil.request("com.alipay.antorchard.smashedGoldenEgg",
                                "[{\"requestType\":\"NORMAL\",\"seneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                                                + VERSION
                                                + "\"}]");
        }

}
