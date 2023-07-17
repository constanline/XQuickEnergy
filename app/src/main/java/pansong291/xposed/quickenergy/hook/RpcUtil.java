package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.AntForestToast;
import pansong291.xposed.quickenergy.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcUtil
{
    private static final String TAG = RpcUtil.class.getCanonicalName();
    private static Method rpcCallMethod;
    private static Method getResponseMethod;
    private static Object curH5PageImpl;
    public static boolean sendXEdgeProBroadcast;

    public static void init(ClassLoader loader) {
        if(rpcCallMethod == null) {
            try {
                Class<?> h5PageClazz = loader.loadClass(ClassMember.com_alipay_mobile_h5container_api_H5Page);
                Class<?> jsonClazz = loader.loadClass(ClassMember.com_alibaba_fastjson_JSONObject);
                Class<?> rpcClazz = loader.loadClass(ClassMember.com_alipay_mobile_nebulaappproxy_api_rpc_H5RpcUtil);
                rpcCallMethod = rpcClazz.getMethod(
                        ClassMember.rpcCall, String.class, String.class, String.class,
                        boolean.class, jsonClazz, String.class, boolean.class, h5PageClazz,
                        int.class, String.class, boolean.class, int.class, String.class);
                Log.i(TAG, "get RpcCallMethod successfully");
            } catch (Throwable t) {
                Log.i(TAG, "get RpcCallMethod err:");
                //Log.printStackTrace(TAG, t);
            }
        }
    }

    public static String request(String args0, String args1) {
        try {
            Object o;
            if (rpcCallMethod.getParameterTypes().length == 12) {
                o = rpcCallMethod.invoke(
                        null, args0, args1, "", true, null, null, false, curH5PageImpl, 0, "", false, -1);
            } else {
                o = rpcCallMethod.invoke(
                        null, args0, args1, "", true, null, null, false, curH5PageImpl, 0, "", false, -1, "");
            }
            String str = getResponse(o);
            Log.i(TAG, "argument: " + args0 + ", " + args1);
            Log.i(TAG, "response: " + str);
            return str;
        } catch(Throwable t) {
            Log.i(TAG, "invoke err:");
            Log.printStackTrace(TAG, t);
            if(t instanceof InvocationTargetException) {
                if(AntForestToast.context != null && sendXEdgeProBroadcast)
                {
                    sendXEdgeProBroadcast = false;
//                    Intent it = new Intent("com.jozein.xedgepro.PERFORM");
//                    it.putExtra("data", Config.xedgeproData());
//                    AntForestToast.context.sendBroadcast(it);
                    Log.recordLog(t.getCause().getMessage(), "");
                }
            }
        }
        return null;
    }

    public static String getResponse(Object resp) {
        try {
            if(getResponseMethod == null)
                getResponseMethod = resp.getClass().getMethod(ClassMember.getResponse);

            return (String) getResponseMethod.invoke(resp);
        } catch(Throwable t) {
            Log.i(TAG, "getResponse err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

}
