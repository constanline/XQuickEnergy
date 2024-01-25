package pansong291.xposed.quickenergy.hook;

import java.util.UUID;
import pansong291.xposed.quickenergy.util.RandomUtils;

public class OmegakoiTownRpcCall {
    private static final String version = "2.0";

    private static String getUuid() {
        StringBuilder sb = new StringBuilder();
        for (String str : UUID.randomUUID().toString().split("-")) {
            sb.append(str.substring(str.length() / 2));
        }
        return sb.toString();
    }

    public static String houseProduct() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.house.product",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\",\"shouldScoreReward\":true}]");
    }

    public static String houseBuild(String groundId, String houseId) {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.house.build",
                "[{\"groundId\":\"" + groundId + "\",\"houseId\":\"" + houseId
                        + "\",\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getUserScore() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.user.getUserScore",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getBalloonsReadyToCollect() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.balloon.getBalloonsReadyToCollect",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getUserQuests() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.scenario.getUserQuests",
                "[{\"disableQuests\":true,\"outBizNo\":\"" + UUID.randomUUID().toString()
                        + "\",\"scenarioId\":\"shopNewestTips\"}]");
    }

    public static String completeQuest(String questId, String scenarioId) {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.scenario.completeQuest",
                "[{\"optionIndex\":0,\"outBizNo\":\"" + UUID.randomUUID().toString() + "\",\"questId\":\"" + questId
                        + "\",\"scenarioId\":\"" + scenarioId + "\",\"showType\":\"mayor\"}]");
    }

    public static String groundBuy(String groundId) {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.ground.buy",
                "[{\"groundId\":\"" + groundId + "\",\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getCurrentBalloonsByTarget(String groundId) {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.balloon.getCurrentBalloonsByTarget",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getUserTasks() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.task.getUserTasks",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String queryAppInfo(String app_id) {
        return RpcUtil.request("alipay.mappconfig.queryAppInfo",
                "[{\"baseInfoReq\":{\"appIds\":[\"" + app_id
                        + "\"],\"platform\":\"ANDROID\",\"pre\":false,\"width\":0},\"packInfoReq\":{\"bundleid\":\"com.alipay.alipaywallet\",\"channel\":\"offical\",\"client\":\"10.5.36.8100\",\"env\":\"production\",\"platform\":\"android\",\"protocol\":\"1.0\",\"query\":\"{\\\""
                        + app_id + "\\\":{\\\"app_id\\\":\\\"" + app_id
                        + "\\\",\\\"version\\\":\\\"*\\\",\\\"isTarget\\\":\\\"YES\\\"}}\",\"reqmode\":\"async\",\"sdk\":\"1.3.0.0\",\"system\":\"10\"},\"reqType\":2}]");
    }

    public static String triggerTaskReward(String taskId) {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.task.triggerTaskReward",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\",\"taskId\":\"" + taskId + "\"}]");
    }

    public static String getShareId() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.user.getShareId",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getFengdieData() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.user.getFengdieData",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getSignInStatus() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.signIn.getSignInStatus",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String signIn() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.signIn.signIn",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getProduct() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.shop.getProduct",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getUserGrounds() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.ground.getUserGrounds",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String getUserHouses() {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.house.getUserHouses",
                "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String collect(String houseId, long id) {
        return RpcUtil.request("com.alipay.omegakoi.town.v2.house.collect",
                "[{\"houseId\":\"" + houseId + "\",\"id\":" + id
                        + ",\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]");
    }

    public static String matchCrowd() {
        return RpcUtil.request("com.alipay.omegakoi.common.user.matchCrowd",
                "[{\"crowdCodes\":[\"OUW7WQPH7\",\"OM9K933XZ\"],\"outBizNo\":\"60123460-b6ac-11ee-95b2-3be423343437\"}]");
    }
}
