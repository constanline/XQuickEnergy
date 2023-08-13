package pansong291.xposed.quickenergy.hook;

public class AntSportsRpcCall {
        private static final String chInfo = "ch_appcenter__chsub_9patch",
                        timeZone = "Asia\\/Shanghai", version = "3.0.1.2", alipayAppVersion = "0.0.852",
                        cityCode = "330100", appId = "2021002116659397";

        public static String queryCoinBubbleModule() {
                return RpcUtil.request("com.alipay.sportshealth.biz.rpc.sportsHealthHomeRpc.queryCoinBubbleModule",
                                "[{\"bubbleId\":\"\",\"canAddHome\":false,\"chInfo\":\"" + chInfo
                                                + "\",\"clientAuthStatus\":\"not_support\",\"clientOS\":\"android\",\"distributionChannel\":\"\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_AI\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"]}]");
        }

        public static String receiveCoinAsset(String assetId, int coinAmount) {
                return RpcUtil.request("com.alipay.sportshealth.biz.rpc.SportsHealthCoinCenterRpc.receiveCoinAsset",
                                "[{\"assetId\":\"" + assetId
                                                + "\",\"chInfo\":\"" + chInfo
                                                + "\",\"clientOS\":\"android\",\"coinAmount\":"
                                                + coinAmount
                                                + ",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"tracertPos\":\"首页金币收集\"}]");
        }

        public static String queryMyHomePage() {
                return RpcUtil.request("alipay.antsports.walk.map.queryMyHomePage", "[{\"alipayAppVersion\":\""
                                + alipayAppVersion + "\",\"chInfo\":\"" + chInfo
                                + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"pathListUsePage\":true,\"timeZone\":\""
                                + timeZone + "\"}]");
        }

        public static String join(String pathId) {
                return RpcUtil.request("alipay.antsports.walk.map.join", "[{\"chInfo\":\"" + chInfo
                                + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"pathId\":\""
                                + pathId + "\"}]");
        }

        public static String openAndJoinFirst() {
                return RpcUtil.request("alipay.antsports.walk.user.openAndJoinFirst", "[{\"chInfo\":\"" + chInfo
                                + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"]}]");
        }

        public static String go(String day, String rankCacheKey, int stepCount) {
                return RpcUtil.request("alipay.antsports.walk.map.go", "[{\"chInfo\":\"" + chInfo
                                + "\",\"clientOS\":\"android\",\"day\":\"" + day
                                + "\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"needAllBox\":true,\"rankCacheKey\":\""
                                + rankCacheKey + "\",\"timeZone\":\"" + timeZone + "\",\"useStepCount\":" + stepCount
                                + "}]");
        }

        public static String openTreasureBox(String boxNo, String userId) {
                return RpcUtil.request("alipay.antsports.walk.treasureBox.openTreasureBox", "[{\"boxNo\":\"" + boxNo
                                + "\",\"chInfo\":\"" + chInfo
                                + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"userId\":\""
                                + userId + "\"}]");
        }

        public static String queryBaseList() {
                return RpcUtil.request("alipay.antsports.walk.path.queryBaseList", "[{\"chInfo\":\"" + chInfo
                                + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"]}]");
        }

        public static String queryProjectList(int index) {
                return RpcUtil.request("alipay.antsports.walk.charity.queryProjectList", "[{\"chInfo\":\"" + chInfo
                                + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"index\":"
                                + index + ",\"projectListUseVertical\":true}]");
        }

        public static String donate(int donateCharityCoin, String projectId) {
                return RpcUtil.request("alipay.antsports.walk.charity.donate", "[{\"chInfo\":\"" + chInfo
                                + "\",\"clientOS\":\"android\",\"donateCharityCoin\":" + donateCharityCoin
                                + ",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"projectId\":\""
                                + projectId + "\"}]");
        }

        public static String queryWalkStep() {
                return RpcUtil.request("alipay.antsports.walk.user.queryWalkStep", "[{\"chInfo\":\"" + chInfo
                                + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"timeZone\":\""
                                + timeZone + "\"}]");
        }

        public static String walkDonateSignInfo(int count) {
                return RpcUtil.request("alipay.charity.mobile.donate.walk.walkDonateSignInfo",
                                "[{\"needDonateAction\":false,\"source\":\"walkDonateHome\",\"steps\":" + count
                                                + ",\"timezoneId\":\""
                                                + timeZone + "\"}]");
        }

        public static String donateWalkHome(int count) {
                return RpcUtil.request("alipay.charity.mobile.donate.walk.home",
                                "[{\"module\":\"3\",\"steps\":" + count + ",\"timezoneId\":\"" + timeZone + "\"}]");
        }

        public static String exchange(String actId, int count, String donateToken) {
                return RpcUtil.request("alipay.charity.mobile.donate.walk.exchange",
                                "[{\"actId\":\"" + actId + "\",\"count\":"
                                                + count + ",\"donateToken\":\"" + donateToken + "\",\"timezoneId\":\""
                                                + timeZone + "\",\"ver\":0}]");
        }

        /* 这个好像没用 */
        public static String exchangeSuccess(String exchangeId) {
                String args1 = "[{\"exchangeId\":\"" + exchangeId
                                + "\",\"timezone\":\"GMT+08:00\",\"version\":\"" + version + "\"}]";
                return RpcUtil.request("alipay.charity.mobile.donate.exchange.success", args1);
        }

        /* 文体中心 */
        public static String userTaskGroupQuery(String groupId) {
                return RpcUtil.request("alipay.tiyubiz.sports.userTaskGroup.query",
                                "[{\"cityCode\":\"" + cityCode + "\",\"groupId\":\"" + groupId + "\"}]");
        }

        public static String userTaskComplete(String bizType, String taskId) {
                return RpcUtil.request("alipay.tiyubiz.sports.userTask.complete",
                                "[{\"bizType\":\"" + bizType + "\",\"cityCode\":\"" + cityCode + "\",\"completedTime\":"
                                                + System.currentTimeMillis() + ",\"taskId\":\"" + taskId + "\"}]");
        }

        public static String userTaskRightsReceive(String taskId, String userTaskId) {
                return RpcUtil.request("alipay.tiyubiz.sports.userTaskRights.receive",
                                "[{\"taskId\":\"" + taskId + "\",\"userTaskId\":\"" + userTaskId + "\"}]");
        }

        public static String queryAccount() {
                return RpcUtil.request("alipay.tiyubiz.user.asset.query.account",
                                "[{\"accountType\":\"TIYU_SEED\"}]");
        }

        public static String queryRoundList() {
                return RpcUtil.request("alipay.tiyubiz.wenti.walk.queryRoundList",
                                "[{}]");
        }

        public static String participate(int bettingPoints, String InstanceId, String ResultId, String roundId) {
                return RpcUtil.request("alipay.tiyubiz.wenti.walk.participate",
                                "[{\"bettingPoints\":" + bettingPoints + ",\"guessInstanceId\":\"" + InstanceId
                                                + "\",\"guessResultId\":\"" + ResultId
                                                + "\",\"newParticipant\":false,\"roundId\":\"" + roundId
                                                + "\",\"stepTimeZone\":\"Asia/Shanghai\"}]");
        }

        public static String pathFeatureQuery() {
                return RpcUtil.request("alipay.tiyubiz.path.feature.query",
                                "[{\"appId\":\"" + appId
                                                + "\",\"features\":[\"USER_CURRENT_PATH_SIMPLE\"],\"sceneCode\":\"wenti_shijiebei\"}]");
        }

        public static String pathMapJoin(String pathId) {
                return RpcUtil.request("alipay.tiyubiz.path.map.join",
                                "[{\"appId\":\"" + appId + "\",\"pathId\":\"" + pathId + "\"}]");
        }

        public static String pathMapHomepage(String pathId) {
                return RpcUtil.request("alipay.tiyubiz.path.map.homepage",
                                "[{\"appId\":\"" + appId + "\",\"pathId\":\"" + pathId + "\"}]");
        }

        public static String stepQuery(String countDate, String pathId) {
                return RpcUtil.request("alipay.tiyubiz.path.map.step.query",
                                "[{\"appId\":\"" + appId + "\",\"countDate\":\"" + countDate
                                                + "\",\"pathId\":\""
                                                + pathId + "\",\"timeZone\":\"Asia/Shanghai\"}]");
        }

        public static String tiyubizGo(String countDate, int goStepCount, String pathId, String userPathRecordId) {
                return RpcUtil.request("alipay.tiyubiz.path.map.go",
                                "[{\"appId\":\"" + appId + "\",\"countDate\":\"" + countDate
                                                + "\",\"goStepCount\":"
                                                + goStepCount + ",\"pathId\":\"" + pathId
                                                + "\",\"timeZone\":\"Asia/Shanghai\",\"userPathRecordId\":\""
                                                + userPathRecordId + "\"}]");
        }

        public static String rewardReceive(String pathId, String userPathRewardId) {
                return RpcUtil.request("alipay.tiyubiz.path.map.reward.receive",
                                "[{\"appId\":\"" + appId + "\",\"pathId\":\"" + pathId + "\",\"userPathRewardId\":\""
                                                + userPathRewardId + "\"}]");
        }

}
