package pansong291.xposed.quickenergy.hook;

public class AntMemberRpcCall {

    /* ant member point */
    public static String queryPointCert(int page, int pageSize) {
        String args1 = "[{\"page\":" + page + ",\"pageSize\":" + pageSize + "}]";
        return RpcUtil.request("alipay.antmember.biz.rpc.member.h5.queryPointCert", args1);
    }

    public static String receivePointByUser(String certId) {
        String args1 = "[{\"certId\":" + certId + "}]";
        return RpcUtil.request("alipay.antmember.biz.rpc.member.h5.receivePointByUser", args1);
    }

    public static String memberSignIn() {
        String args1 = "[{}]";
        return RpcUtil.request("alipay.antmember.biz.rpc.member.h5.memberSignin", args1);
    }
    /* 安心豆 */

    public static String pageRender() {
        return RpcUtil.request("com.alipay.insplatformbff.common.insiopService.pageRender",
                "[\"INS_PLATFORM_BLUEBEAN\",{\"channelType\":\"insplatform_mobilesearch_anxindou\"}]");
    }

    public static String taskProcess(String appletId) {
        return RpcUtil.request("com.alipay.insmarketingbff.task.taskProcess", "[{\"appletId\":\"" + appletId + "\"}]");
    }

    public static String taskTrigger(String appletId, String scene) {
        return RpcUtil.request("com.alipay.insmarketingbff.task.taskTrigger",
                "[{\"appletId\":\"" + appletId + "\",\"scene\":\"" + scene + "\"}]");
    }

    public static String queryUserAccountInfo() {
        return RpcUtil.request("com.alipay.insmarketingbff.point.queryUserAccountInfo",
                "[{\"channel\":\"insplatform_mobilesearch_anxindou\",\"pointProdCode\":\"INS_BLUE_BEAN\",\"pointUnitType\":\"COUNT\"}]");
    }

    public static String exchangeDetail(String itemId) {
        return RpcUtil.request("com.alipay.insmarketingbff.onestop.planTrigger",
                "[{\"extParams\":{\"itemId\":\"" + itemId
                        + "\"},\"planCode\":\"bluebean_onestop\",\"planOperateCode\":\"exchangeDetail\"}]");
    }

    public static String exchange(String itemId, int pointAmount) {
        return RpcUtil.request("com.alipay.insmarketingbff.onestop.planTrigger",
                "[{\"extParams\":{\"itemId\":\"" + itemId + "\",\"pointAmount\":\"" + Integer.toString(pointAmount)
                        + "\"},\"planCode\":\"bluebean_onestop\",\"planOperateCode\":\"exchange\"}]");
    }
}
