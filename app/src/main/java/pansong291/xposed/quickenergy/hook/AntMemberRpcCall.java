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

    public static String taskProcess(String appletId) {
        return RpcUtil.request("com.alipay.insmarketingbff.task.taskProcess", "[{\"appletId\":\"" + appletId + "\"}]");
    }

    public static String taskTrigger(String appletId, String scene) {
        return RpcUtil.request("com.alipay.insmarketingbff.task.taskTrigger", "[{\"appletId\":\"" + appletId + "\",\"scene\":\"" + scene + "\"}]");
    }
}
