package pansong291.xposed.quickenergy.hook;

/**
 * @author Constanline
 * @since 2023/08/22
 */
public class AntStallRpcCall {
    private static final String ANT_STALL_VERSION = "0.1.2307121418.54";

    public static String home() {
        return RpcUtil.request("com.alipay.antstall.self.home",
                "[{\"arouseAppParams\":{},\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                        ANT_STALL_VERSION + "\"}]");
    }

    public static String settle(String assetId, int settleCoin) {
        return RpcUtil.request("com.alipay.antstall.self.settle",
                "[{\"assetId\":\"" + assetId + "\",\"coinType\":\"MASTER\",\"settleCoin\":" + settleCoin +
                        ",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                        + ANT_STALL_VERSION + "\"}]");
    }

    public static String shopList() {
        return RpcUtil.request("com.alipay.antstall.shop.list",
                "[{\"freeTop\":false,\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                        ANT_STALL_VERSION + "\"}]");
    }

    public static String preOneKeyClose() {
        return RpcUtil.request("com.alipay.antstall.user.shop.close.preOneKey",
                "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                        + ANT_STALL_VERSION + "\"}]");
    }

    public static String oneKeyClose() {
        return RpcUtil.request("com.alipay.antstall.user.shop.oneKeyClose",
                "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                        + ANT_STALL_VERSION + "\"}]");
    }

    public static String oneKeyOpen() {
        return RpcUtil.request("com.alipay.antstall.user.shop.oneKeyOpen",
                "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                        + ANT_STALL_VERSION + "\"}]");
    }

    public static String taskList() {
        return RpcUtil.request("com.alipay.antstall.task.list",
                "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                        ANT_STALL_VERSION + "\"}]");
    }

    public static String signToday() {
        return RpcUtil.request("com.alipay.antstall.sign.today",
                "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                        ANT_STALL_VERSION + "\"}]");
    }

    public static String finishTask(String outBizNo, String taskType) {
        return RpcUtil.request("com.alipay.antiep.finishTask",
                "[{\"outBizNo\":\"" + outBizNo +
                        "\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTSTALL_TASK\",\"source\":\"AST\",\"systemType\":\"android\",\"taskType\":\"" +
                        taskType + "\",\"version\":\"" + ANT_STALL_VERSION + "\"}]");
    }

    public static String receiveTaskAward(String taskType) {
        return RpcUtil.request("com.alipay.antiep.receiveTaskAward",
                "[{\"ignoreLimit\":true,\"requestType\":\"RPC\",\"sceneCode\":\"ANTSTALL_TASK\",\"source\":\"AST\",\"systemType\":\"android\",\"taskType\":\"" +
                        taskType + "\",\"version\":\"" + ANT_STALL_VERSION + "\"}]");
    }

    public static String taskFinish(String taskType) {
        return RpcUtil.request("com.alipay.antstall.task.finish",
                "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"taskType\":\"" + taskType + "\",\"version\":\"" +
                        ANT_STALL_VERSION + "\"}]");
    }

    public static String taskAward(String amount, String prizeId, String taskType) {
        return RpcUtil.request("com.alipay.antstall.task.award",
                "[{\"amount\":" + amount + ",\"prizeId\":\"" + prizeId +
                        "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"taskType\":\""
                        + taskType + "\",\"version\":\"" + ANT_STALL_VERSION + "\"}]");
    }

    public static String taskBenefit() {
        return RpcUtil.request("com.alipay.antstall.task.benefit",
                "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                        ANT_STALL_VERSION + "\"}]");
    }

    public static String collectManure() {
        return RpcUtil.request("com.alipay.antstall.manure.collectManure",
                "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                        ANT_STALL_VERSION + "\"}]");
    }

    public static String queryManureInfo() {
        return RpcUtil.request("com.alipay.antstall.manure.queryManureInfo",
                "[{\"queryManureType\":\"ANTSTALL\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                        ANT_STALL_VERSION + "\"}]");
    }
}
