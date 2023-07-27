package pansong291.xposed.quickenergy.hook;

public class AncientTreeRpcCall
{

    public static String homePage(String cityCode) {
        return RpcUtil.request("alipay.greenmatrix.rpc.h5.ancienttree.homePage",
                        "[{\"cityCode\":\"" + cityCode + "\",\"source\":\"antforesthome\"}]");
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
