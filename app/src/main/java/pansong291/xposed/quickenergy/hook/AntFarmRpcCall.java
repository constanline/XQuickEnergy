package pansong291.xposed.quickenergy.hook;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import pansong291.xposed.quickenergy.util.RandomUtils;

public class AntFarmRpcCall {
    private static final String VERSION = "1.8.2302070202.46";

    public static String enterFarm(String farmId, String userId) {
        return RpcUtil.request("com.alipay.antfarm.enterFarm",
                "[{\"animalId\":\"\",\"farmId\":\"" + farmId +
                        "\",\"gotoneScene\":\"\",\"gotoneTemplateId\":\"\",\"masterFarmId\":\"\",\"queryLastRecordNum\":true,\"recall\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"ANTFOREST\",\"touchRecordId\":\"\",\"userId\":\""
                        + userId + "\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String syncAnimalStatus(String farmId) {
        String args1 = "[{\"farmId\":\"" + farmId +
                "\",\"operType\":\"FEEDSYNC\",\"queryFoodStockInfo\":false,\"recall\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"userId\":\""
                + farmId2UserId(farmId) + "\",\"version\":\"" + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.syncAnimalStatus", args1);
    }

    public static String sleep() {
        String args1 = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"LOVECABIN\",\"version\":\"unknown\"}]";
        return RpcUtil.request("com.alipay.antfarm.sleep", args1);
    }

    public static String queryLoveCabin(String userId) {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"ENTERFARM\",\"userId\":\"" +
                userId + "\",\"version\":\"unknown\"}]";
        return RpcUtil.request("com.alipay.antfarm.queryLoveCabin", args1);
    }

    public static String rewardFriend(String consistencyKey, String friendId, String productNum, String time) {
        String args1 = "[{\"canMock\":true,\"consistencyKey\":\"" + consistencyKey
                + "\",\"friendId\":\"" + friendId + "\",\"operType\":\"1\",\"productNum\":" + productNum +
                ",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"time\":"
                + time + ",\"version\":\"" + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.rewardFriend", args1);
    }

    public static String recallAnimal(String animalId, String currentFarmId, String masterFarmId) {
        String args1 = "[{\"animalId\":\"" + animalId + "\",\"currentFarmId\":\""
                + currentFarmId + "\",\"masterFarmId\":\"" + masterFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.recallAnimal", args1);
    }

    public static String orchardRecallAnimal(String animalId, String userId) {
        String args1 = "[{\"animalId\":\"" + animalId + "\",\"orchardUserId\":\"" + userId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"zhuangyuan_zhaohuixiaoji\",\"version\":\"\"0.1.2307042001.2\"\"}]";
        return RpcUtil.request("com.alipay.antorchard.recallAnimal", args1);
    }

    public static String sendBackAnimal(String sendType, String animalId, String currentFarmId, String masterFarmId) {
        String args1 = "[{\"animalId\":\"" + animalId + "\",\"currentFarmId\":\""
                + currentFarmId + "\",\"masterFarmId\":\"" + masterFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"sendType\":\""
                + sendType + "\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.sendBackAnimal", args1);
    }

    public static String harvestProduce(String farmId) {
        String args1 = "[{\"canMock\":true,\"farmId\":\"" + farmId +
                "\",\"giftType\":\"\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.harvestProduce", args1);
    }

    public static String listActivityInfo() {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.listActivityInfo", args1);
    }

    public static String donation(String activityId, int donationAmount) {
        String args1 = "[{\"activityId\":\"" + activityId + "\",\"donationAmount\":" + donationAmount +
                ",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.donation", args1);
    }

    public static String listFarmTask() {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.listFarmTask", args1);
    }

    public static String getAnswerInfo() {
        String args1 = "[{\"answerSource\":\"foodTask\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.getAnswerInfo", args1);
    }

    public static String answerQuestion(String quesId, int answer) {
        String args1 = "[{\"answers\":\"[{\\\"questionId\\\":\\\"" + quesId + "\\\",\\\"answers\\\":[" + answer +
                "]}]\",\"bizkey\":\"ANSWER\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.doFarmTask", args1);
    }

    public static String receiveFarmTaskAward(String taskId) {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskId\":\""
                + taskId + "\",\"version\":\"" + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.receiveFarmTaskAward", args1);
    }

    public static String listToolTaskDetails() {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.listToolTaskDetails", args1);
    }

    public static String receiveToolTaskReward(String awardType, int rewardCount, String taskType) {
        String args1 = "[{\"awardType\":\"" + awardType +
                "\",\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"rewardCount\":"
                + rewardCount + ",\"rewardType\":\"" + awardType +
                "\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskType\":\""
                + taskType + "\",\"version\":\"" + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.receiveToolTaskReward", args1);
    }

    public static String feedAnimal(String farmId) {
        String args1 = "[{\"animalType\":\"CHICK\",\"canMock\":true,\"farmId\":\"" + farmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.feedAnimal", args1);
    }

    public static String listFarmTool() {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.listFarmTool", args1);
    }

    public static String useFarmTool(String targetFarmId, String toolId, String toolType) {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"targetFarmId\":\""
                + targetFarmId + "\",\"toolId\":\"" + toolId + "\",\"toolType\":\"" + toolType + "\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.useFarmTool", args1);
    }

    public static String rankingList(int pageStartSum) {
        String args1 = "[{\"pageSize\":20,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"startNum\":"
                + pageStartSum + ",\"version\":\"" + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.rankingList", args1);
    }

    public static String notifyFriend(String animalId, String notifiedFarmId) {
        String args1 = "[{\"animalId\":\"" + animalId +
                "\",\"animalType\":\"CHICK\",\"canBeGuest\":true,\"notifiedFarmId\":\"" + notifiedFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.notifyFriend", args1);
    }

    public static String feedFriendAnimal(String friendFarmId) {
        String args1 = "[{\"animalType\":\"CHICK\",\"canMock\":true,\"friendFarmId\":\"" + friendFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return RpcUtil.request("com.alipay.antfarm.feedFriendAnimal", args1);
    }

    public static String farmId2UserId(String farmId) {
        int l = farmId.length() / 2;
        return farmId.substring(l);
    }

    public static String collectManurePot(String manurePotNO) {
        return RpcUtil.request("com.alipay.antfarm.collectManurePot", "[{\"manurePotNOs\":\"" + manurePotNO +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION
                + "\"}]");
    }

    public static String sign() {
        return RpcUtil.request("com.alipay.antfarm.sign",
                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION
                        + "\"}]");
    }

    public static String initFarmGame(String gameType) {
        return RpcUtil.request("com.alipay.antfarm.initFarmGame",
                "[{\"gameType\":\"" + gameType
                        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"toolTypes\":\"STEALTOOL,ACCELERATETOOL,SHARETOOL\"}]");
    }

    public static int RandomScore(String str) {
        if ("starGame".equals(str)) {
            return RandomUtils.nextInt(200, 300);

        } else if ("jumpGame".equals(str)) {
            return RandomUtils.nextInt(150, 170) * 10;
        } else {
            return 210;
        }
    }

    public static String recordFarmGame(String gameType) {
        String uuid = getuuid();
        String md5String = getMD5(uuid);
        int score = RandomScore(gameType);
        return RpcUtil.request("com.alipay.antfarm.recordFarmGame",
                "[{\"gameType\":\"" + gameType + "\",\"md5\":\"" + md5String
                        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"score\":" + score
                        + ",\"source\":\"H5\",\"toolTypes\":\"STEALTOOL,ACCELERATETOOL,SHARETOOL\",\"uuid\":\"" + uuid
                        + "\"}]");
    }

    private static String getuuid() {
        String[] split;
        StringBuilder sb = new StringBuilder();
        for (String str : UUID.randomUUID().toString().split("-")) {
            sb.append(str.substring(str.length() / 2, str.length()));
        }
        return sb.toString();
    }

    public static String getMD5(String password) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把没一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 小鸡厨房 */

    public static String enterKitchen(String userId) {
        return RpcUtil.request("com.alipay.antfarm.enterKitchen",
                "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"userId\":\""
                        + userId + "\",\"version\":\"unknown\"}]");
    }

    public static String collectDailyFoodMaterial(int dailyFoodMaterialAmount) {
        return RpcUtil.request("com.alipay.antfarm.collectDailyFoodMaterial",
                "[{\"collectDailyFoodMaterialAmount\":" + dailyFoodMaterialAmount
                        + ",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"version\":\"unknown\"}]");
    }

    public static String queryFoodMaterialPack() {
        return RpcUtil.request("com.alipay.antfarm.queryFoodMaterialPack",
                "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"kitchen\",\"version\":\"unknown\"}]");
    }

    public static String collectDailyLimitedFoodMaterial(int dailyLimitedFoodMaterialAmount) {
        return RpcUtil.request("com.alipay.antfarm.collectDailyLimitedFoodMaterial",
                "[{\"collectDailyLimitedFoodMaterialAmount\":" + dailyLimitedFoodMaterialAmount
                        + ",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"kitchen\",\"version\":\"unknown\"}]");
    }

    public static String cook(String userId) {
        return RpcUtil.request("com.alipay.antfarm.cook",
                "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"userId\":\""
                        + userId + "\",\"version\":\"unknown\"}]");
    }

    public static String useFarmFood(String cookbookId, String cuisineId) {
        return RpcUtil.request("com.alipay.antfarm.useFarmFood",
                "[{\"cookbookId\":\"" + cookbookId + "\",\"cuisineId\":\"" + cuisineId
                        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"useCuisine\":true,\"version\":\""
                        + VERSION + "\"}]");
    }

    /* 日常任务 */

    public static String doFarmTask(String bizKey) {
        return RpcUtil.request("com.alipay.antfarm.doFarmTask",
                "[{\"bizKey\":\"" + bizKey
                        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String queryTabVideoUrl() {
        return RpcUtil.request("com.alipay.antfarm.queryTabVideoUrl",
                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION
                        + "\"}]");
    }

    public static String videoDeliverModule(String bizId) {
        return RpcUtil.request("alipay.content.reading.life.deliver.module",
                "[{\"bizId\":\"" + bizId
                        + "\",\"bizType\":\"CONTENT\",\"chInfo\":\"ch_antFarm\",\"refer\":\"antFarm\",\"timestamp\":\""
                        + System.currentTimeMillis() + "\"}]");
    }

    public static String videoTrigger(String bizId) {
        return RpcUtil.request("alipay.content.reading.life.prize.trigger",
                "[{\"bizId\":\"" + bizId
                        + "\",\"bizType\":\"CONTENT\",\"prizeFlowNum\":\"VIDEO_TASK\",\"prizeType\":\"farmFeed\"}]");
    }

}
