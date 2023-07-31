package pansong291.xposed.quickenergy.hook;

public class ReserveRpcCall {
    private static final String VERSION = "20230501";
    private static final String VERSION2 = "20230522";

    public static String queryTreeItemsForExchange() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryTreeItemsForExchange",
                "[{\"cityCode\":\"370100\",\"itemTypes\":\"\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                        + VERSION2 + "\"}]");
    }

    public static String queryTreeForExchange(String projectId) {
        return RpcUtil.request("alipay.antforest.forest.h5.queryTreeForExchange", "[{\"projectId\":\"" + projectId
                + "\",\"version\":\"" + VERSION + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    public static String exchangeTree(String projectId) {
        int projectId_num = Integer.parseInt(projectId);
        return RpcUtil.request("alipay.antmember.forest.h5.exchangeTree",
                "[{\"projectId\":" + projectId_num + ",\"sToken\":\"" + System.currentTimeMillis() + "\",\"version\":\""
                        + VERSION + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

}
