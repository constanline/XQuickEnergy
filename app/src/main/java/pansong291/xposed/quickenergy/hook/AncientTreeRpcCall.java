package pansong291.xposed.quickenergy.hook;

public class AncientTreeRpcCall {
    private static final String VERSION = "20230522";

    public static String homePage(String cityCode) {
        return RpcUtil.request("alipay.greenmatrix.rpc.h5.ancienttree.homePage",
                        "[{\"cityCode\":\"" + cityCode + "\",\"source\":\"antforesthome\"}]");
    }

    public static String queryTreeItemsForExchange(String cityCode) {
        return RpcUtil.request("alipay.antforest.forest.h5.queryTreeItemsForExchange",
                "[{\"cityCode\":\"" + cityCode
                        + "\",\"itemTypes\":\"\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String protect(String activityId, String projectId, String cityCode) {
        return RpcUtil.request("alipay.greenmatrix.rpc.h5.ancienttree.protect",
                "[{\"ancientTreeActivityId\":\"" + activityId + "\",\"ancientTreeProjectId\":\"" +
                        projectId + "\",\"cityCode\":\"" + cityCode + "\",\"source\":\"ancientreethome\"}]");
    }

    public static String projectDetail(String ancientTreeProjectId, String cityCode) {
        return RpcUtil.request("alipay.greenmatrix.rpc.h5.ancienttree.projectDetail",
                "[{\"ancientTreeProjectId\":\"" + ancientTreeProjectId +
                        "\",\"channel\":\"ONLINE\",\"cityCode\":\"" + cityCode + "\",\"source\":\"ancientreethome\"}]");
    }

}
