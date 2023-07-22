package pansong291.xposed.quickenergy.hook;

import android.content.Intent;
import pansong291.xposed.quickenergy.AntForestNotification;
import pansong291.xposed.quickenergy.AntForestToast;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;

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
                String msg = t.getCause().getMessage();
                if (!StringUtil.isEmpty(msg)) {
                    if (msg.contains("登录超时")) {
                        AntForestNotification.setContentText("登录超时");
                        if(AntForestToast.context != null) {
                            if (sendXEdgeProBroadcast) {
                                sendXEdgeProBroadcast = false;
                                Intent it = new Intent("com.jozein.xedgepro.PERFORM");
                                it.putExtra("data", Config.xEdgeProData());
                                AntForestToast.context.sendBroadcast(it);
                                Log.recordLog("发送XposedEdgePro广播", Config.xEdgeProData());
                            }
                            if (Config.timeoutRestart()) {
                                if (Config.stayAwakeType() == XposedHook.StayAwakeType.BROADCAST) {
                                    XposedHook.restartHook(true);
                                } else {
                                    XposedHook.alarmHook(AntForestToast.context, 3000, true);
                                }
                            }
                        }
                    } else if (msg.contains("请求不合法")) {
                        if (Config.waitWhenException() > 0) {
                            long waitTime = System.currentTimeMillis() + Config.waitWhenException();
                            AntForestNotification.setContentText("请求不合法,等待至" + DateFormat.getDateTimeInstance().format(waitTime));
                            Config.setForestPauseTime(waitTime);
                        }
                    }
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
