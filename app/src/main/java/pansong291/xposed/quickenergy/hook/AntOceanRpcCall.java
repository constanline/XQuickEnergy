package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.RandomUtils;

/**
 * @author Constanline
 * @since 2023/08/01
 */
public class AntOceanRpcCall {
        private static final String VERSION = "20230901";

        private static String getUniqueId() {
                return String.valueOf(System.currentTimeMillis()) + RandomUtils.nextLong();
        }

        public static String queryOceanStatus() {
                return RpcUtil.request("alipay.antocean.ocean.h5.queryOceanStatus",
                                "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
        }

        public static String queryHomePage() {
                return RpcUtil.request("alipay.antocean.ocean.h5.queryHomePage",
                                "[{\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + getUniqueId() + "\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String cleanOcean(String userId) {
                return RpcUtil.request("alipay.antocean.ocean.h5.cleanOcean",
                                "[{\"cleanedUserId\":\"" + userId + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\""
                                                + getUniqueId() + "\"}]");
        }

        public static String ipOpenSurprise() {
                return RpcUtil.request("alipay.antocean.ocean.h5.ipOpenSurprise",
                                "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\"" + getUniqueId()
                                                + "\"}]");
        }

        public static String collectReplicaAsset() {
                return RpcUtil.request("alipay.antocean.ocean.h5.collectReplicaAsset",
                                "[{\"replicaCode\":\"avatar\",\"source\":\"senlinzuoshangjiao\",\"uniqueId\":\""
                                                + getUniqueId() +
                                                "\",\"version\":\"" + VERSION + "\"}]");
        }

        public static String receiveTaskAward(String sceneCode, String taskType) {
                return RpcUtil.request("com.alipay.antiep.receiveTaskAward",
                                "[{\"ignoreLimit\":false,\"requestType\":\"RPC\",\"sceneCode\":\"" + sceneCode
                                                + "\",\"source\":\"ANT_FOREST\",\"taskType\":\"" +
                                                taskType + "\",\"uniqueId\":\"" + getUniqueId() + "\"}]");
        }

        public static String finishTask(String sceneCode, String taskType) {
                String outBizNo = taskType + "_" + RandomUtils.nextDouble();
                return RpcUtil.request("com.alipay.antiep.finishTask",
                                "[{\"outBizNo\":\"" + outBizNo + "\",\"requestType\":\"RPC\",\"sceneCode\":\"" +
                                                sceneCode + "\",\"source\":\"ANTFOCEAN\",\"taskType\":\"" + taskType
                                                + "\",\"uniqueId\":\"" + getUniqueId() + "\"}]");
        }

        public static String queryTaskList() {
                return RpcUtil.request("alipay.antocean.ocean.h5.queryTaskList",
                                "[{\"extend\":{},\"fromAct\":\"dynamic_task\",\"sceneCode\":\"ANTOCEAN_TASK\",\"source\":\"ANT_FOREST\",\"uniqueId\":\""
                                                +
                                                getUniqueId() + "\",\"version\":\"" + VERSION + "\"}]");
        }

        public static String unLockReplicaPhase(String replicaCode, String replicaPhaseCode) {
                return RpcUtil.request("alipay.antocean.ocean.h5.unLockReplicaPhase",
                                "[{\"replicaCode\":\"" + replicaCode + "\",\"replicaPhaseCode\":\"" + replicaPhaseCode +
                                                "\",\"source\":\"senlinzuoshangjiao\",\"uniqueId\":\"" + getUniqueId()
                                                + "\",\"version\":\"20220707\"}]");
        }

        public static String queryReplicaHome() {
                return RpcUtil.request("alipay.antocean.ocean.h5.queryReplicaHome",
                                "[{\"replicaCode\":\"avatar\",\"source\":\"senlinzuoshangjiao\",\"uniqueId\":\""
                                                + getUniqueId() + "\"}]");
        }

        public static String repairSeaArea() {
                return RpcUtil.request("alipay.antocean.ocean.h5.repairSeaArea",
                                "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\"" + getUniqueId()
                                                + "\"}]");
        }

        public static String queryOceanPropList() {
                return RpcUtil.request("alipay.antocean.ocean.h5.queryOceanPropList",
                                "[{\"propTypeList\":\"UNIVERSAL_PIECE\",\"skipPropId\":false,\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\""
                                                +
                                                getUniqueId() + "\"}]");
        }

        public static String querySeaAreaDetailList() {
                return RpcUtil.request("alipay.antocean.ocean.h5.querySeaAreaDetailList",
                                "[{\"seaAreaCode\":\"\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetUserId\":\"\",\"uniqueId\":\""
                                                +
                                                getUniqueId() + "\"}]");
        }

        public static String queryOceanChapterList() {
                return RpcUtil.request("alipay.antocean.ocean.h5.queryOceanChapterList",
                                "[{\"source\":\"chInfo_ch_url-https://2021003115672468.h5app.alipay.com/www/atlasOcean.html\",\"uniqueId\":\""
                                                + getUniqueId() + "\"}]");
        }

        public static String switchOceanChapter(String chapterCode) {
                return RpcUtil.request("alipay.antocean.ocean.h5.switchOceanChapter",
                                "[{\"chapterCode\":\"" + chapterCode
                                                + "\",\"source\":\"chInfo_ch_url-https://2021003115672468.h5app.alipay.com/www/atlasOcean.html\",\"uniqueId\":\""
                                                + getUniqueId() + "\"}]");
        }

        public static String queryMiscInfo() {
                return RpcUtil.request("alipay.antocean.ocean.h5.queryMiscInfo",
                                "[{\"queryBizTypes\":[\"HOME_TIPS_REFRESH\"],\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\""
                                                +
                                                getUniqueId() + "\"}]");
        }

        public static String combineFish(String fishId) {
                return RpcUtil.request("alipay.antocean.ocean.h5.combineFish", "[{\"fishId\":\"" + fishId +
                                "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + getUniqueId() + "\"}]");
        }

        public static String querySeaAreaDetailList(String bubbleId, String userId) {
                return RpcUtil.request("alipay.antmember.forest.h5.collectEnergy",
                                "[{\"bubbleIds\":[" + bubbleId
                                                + "],\"channel\":\"ocean\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" +
                                                getUniqueId() + "\",\"userId\":\"" + userId + "\",\"version\":\""
                                                + VERSION + "\"}]");
        }

        public static String cleanFriendOcean(String userId) {
                return RpcUtil.request("alipay.antocean.ocean.h5.cleanFriendOcean",
                                "[{\"cleanedUserId\":\"" + userId + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\""
                                                + getUniqueId() + "\"}]");
        }

        public static String queryFriendPage(String userId) {
                return RpcUtil.request("alipay.antocean.ocean.h5.queryFriendPage",
                                "[{\"friendUserId\":\"" + userId
                                                + "\",\"interactFlags\":\"T\",\"source\":\"ANT_FOREST\",\"uniqueId\":\""
                                                +
                                                getUniqueId() + "\",\"version\":\"" + VERSION + "\"}]");
        }

        public static String queryUserRanking() {
                return RpcUtil.request("alipay.antocean.ocean.h5.queryUserRanking",
                                "[{\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + getUniqueId() + "\"}]");
        }
}
