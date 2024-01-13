package pansong291.xposed.quickenergy.hook;

import de.robv.android.xposed.XposedHelpers;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.AntForestNotification;
import pansong291.xposed.quickenergy.AntForestToast;
import pansong291.xposed.quickenergy.data.RuntimeInfo;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;

public class RpcUtil {
    private static final String TAG = RpcUtil.class.getCanonicalName();
    private static Method rpcCallMethod;
    private static Method getResponseMethod;
    private static Object curH5PageImpl;

    public static volatile boolean isInterrupted = false;

    public static void init(ClassLoader loader) {
        if (rpcCallMethod == null) {
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
                Log.printStackTrace(TAG, t);
            }
        }
    }

    public static Object getMicroApplicationContext(ClassLoader classLoader) {
        return XposedHelpers.callMethod(
                XposedHelpers.callStaticMethod(
                        XposedHelpers.findClass("com.alipay.mobile.framework.AlipayApplication", classLoader),
                        "getInstance"), "getMicroApplicationContext");
    }

    public static String getUserId(ClassLoader classLoader) {
        try {
            Object callMethod =
                    XposedHelpers.callMethod(XposedHelpers.callMethod(getMicroApplicationContext(classLoader),
                            "findServiceByInterface",
                            XposedHelpers.findClass("com.alipay.mobile.personalbase.service.SocialSdkContactService",
                                    classLoader).getName()), "getMyAccountInfoModelByLocal");
            return (String) XposedHelpers.getObjectField(callMethod, "userId");
        } catch (Throwable th) {
            Log.i(TAG, "getUserId err");
            Log.printStackTrace(TAG, th);
        }
        return null;
    }

    public static String request(String args0, String args1) {
        if (isInterrupted) {
            return null;
        }
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
            try {
                JSONObject jo = new JSONObject(str);
                if (jo.optString("memo", "").contains("系统繁忙")) {
                    isInterrupted = true;
                    AntForestNotification.setContentText("系统繁忙，可能需要滑动验证");
                    Log.recordLog("系统繁忙，可能需要滑动验证");
                    return str;
                }
            } catch (Throwable ignored) { }
            return str;
        } catch (Throwable t) {
            Log.i(TAG, "invoke err:");
            Log.printStackTrace(TAG, t);
            if (t instanceof InvocationTargetException) {
                String msg = t.getCause().getMessage();
                if (!StringUtil.isEmpty(msg)) {
                    if (msg.contains("登录超时")) {
                        isInterrupted = true;
                        AntForestNotification.setContentText("登录超时");
                        if (AntForestToast.context != null) {
                            if (Config.timeoutRestart()) {
                                Log.recordLog("尝试重启！");
                                if (Config.timeoutType() == XposedHook.StayAwakeType.ALARM) {
                                    XposedHook.alarmHook(AntForestToast.context, 3000, true);
                                } else {
                                    XposedHook.alarmBroadcast(AntForestToast.context, 3000, true);
                                }
                            }
                        }
                    } else if (msg.contains("[1004]") && "alipay.antmember.forest.h5.collectEnergy".equals(args0)) {
                        if (Config.waitWhenException() > 0) {
                            long waitTime = System.currentTimeMillis() + Config.waitWhenException();
                            RuntimeInfo.getInstance().put(RuntimeInfo.RuntimeInfoKey.ForestPauseTime, waitTime);
                            AntForestNotification.setContentText("触发异常,等待至" + DateFormat.getDateTimeInstance().format(waitTime));
                            Log.recordLog("触发异常,等待至" + DateFormat.getDateTimeInstance().format(waitTime));
                        }
                    } else if (msg.contains("MMTPException")) {
                        return "{\"resultCode\":\"FAIL\",\"memo\":\"MMTPException\",\"resultDesc\":\"MMTPException\"}";
                    }
                }
            }
        }
        return null;
    }

    public static String getResponse(Object resp) throws Throwable {
        if (getResponseMethod == null)
            getResponseMethod = resp.getClass().getMethod(ClassMember.getResponse);

        return (String) getResponseMethod.invoke(resp);
    }

}
