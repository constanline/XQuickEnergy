package pansong291.xposed.quickenergy.hook;

public class AntMemberRpcCall
{

    /* ant member point */
    public static String queryPointCert(int page, int pageSize) {
        String args1 = "[{\"page\":" + page + ",\"pageSize\":" + pageSize + "}]";
        return RpcUtil.request("alipay.antmember.biz.rpc.member.h5.queryPointCert", args1);
    }

    public static String receivePointByUser(String certId) {
        String args1 = "[{\"certId\":" + certId + "}]";
        return RpcUtil.request("alipay.antmember.biz.rpc.member.h5.receivePointByUser", args1);
    }

    public static String memberSignin()
    {
        String args1 = "[{}]";
        return RpcUtil.request("alipay.antmember.biz.rpc.member.h5.memberSignin", args1);
    }
}
