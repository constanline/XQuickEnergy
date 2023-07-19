package pansong291.xposed.quickenergy.hook;

public class KBMemberRpcCall
{
    private static final String version = "2.0";

    public static String rpcCall_signIn() {
        String args1 = "[{\"sceneCode\":\"KOUBEI_INTEGRAL\",\"source\":\"ALIPAY_TAB\",\"version\":\"" + version + "\"}]";
        return RpcUtil.request("alipay.kbmemberprod.action.signIn", args1);
    }

}
