package pansong291.xposed.quickenergy.hook;

import org.json.JSONArray;
import pansong291.xposed.quickenergy.util.FriendIdMap;

public class GreenFinanceRpcCall {

    public static String greenFinanceIndex() {
        return RpcUtil.request(
                "com.alipay.mcaplatformunit.common.mobile.newservice.GreenFinancePageQueryService.indexV2",
                "[{\"clientVersion\":\"VERSION2\",\"custType\":\"MERCHANT\"}]");
    }

    public static String batchSelfCollect(JSONArray bsnIds) {
        return RpcUtil.request(
                "com.alipay.mcaplatformunit.common.mobile.service.GreenFinancePointCollectService.batchSelfCollect",
                "[{\"bsnIds\":" + bsnIds + ",\"clientVersion\":\"VERSION2\",\"custType\":\"MERCHANT\",\"uid\":\""
                        + FriendIdMap.currentUid + "\"}]");
    }

    public static String signInQuery(String sceneId) {
        return RpcUtil.request("com.alipay.loanpromoweb.promo.signin.query",
                "[{\"cycleCount\":7,\"cycleType\":\"d\",\"extInfo\":{},\"needContinuous\":1,\"sceneId\":\"" + sceneId
                        + "\"}]");
    }

    public static String signInTrigger(String sceneId) {
        return RpcUtil.request("com.alipay.loanpromoweb.promo.signin.trigger",
                "[{\"extInfo\":{},\"sceneId\":\"" + sceneId + "\"}]");
    }

    public static String taskQuery(String appletId) {
        return RpcUtil.request("com.alipay.loanpromoweb.promo.task.taskQuery",
                "[{\"appletId\":\"" + appletId + "\",\"completedBottom\":true}]");
    }

    public static String taskTrigger(String appletId, String stageCode, String taskCenId) {
        return RpcUtil.request("com.alipay.loanpromoweb.promo.task.taskTrigger",
                "[{\"appletId\":\"" + appletId + "\",\"stageCode\":\"" + stageCode + "\",\"taskCenId\":\"" + taskCenId
                        + "\"}]");
    }

}