package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.Log;

public class KBMemberRpcCall
{
    private static final String TAG = KBMemberRpcCall.class.getCanonicalName();
    private static final String version = "2.0";

    public static String rpcCall_signIn(ClassLoader loader)
    {
        try
        {
            String args1 = "[{\"sceneCode\":\"KOUBEI_INTEGRAL\",\"source\":\"ALIPAY_TAB\",\"version\":\"" + version + "\"}]";
            return RpcCall.invoke(loader, "alipay.kbmemberprod.action.signIn", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_signIn err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

}
