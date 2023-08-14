package pansong291.xposed.quickenergy.hook;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Bundle;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import pansong291.xposed.quickenergy.*;
import pansong291.xposed.quickenergy.ui.MainActivity;
import pansong291.xposed.quickenergy.util.*;

import java.util.Map;

public class XposedHook implements IXposedHookLoadPackage {
    private static final String TAG = XposedHook.class.getCanonicalName();

    @SuppressLint("StaticFieldLeak")
    private static Service service;
    private static ClassLoader classLoader;
    private static PowerManager.WakeLock wakeLock;
    public static Handler handler;
    private static Runnable runnable;
    private static int times;

    private static boolean isHooked = false;

    public enum StayAwakeType {
        BROADCAST, ALARM, NONE;

        public static final CharSequence[] nickNames = { "广播", "闹钟", "不重启" };
    }

    public enum StayAwakeTarget {
        SERVICE, ACTIVITY;

        public static final CharSequence[] nickNames = { "Service", "Activity" };
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if ("pansong291.xposed.quickenergy.repair".equals(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod(MainActivity.class.getName(), lpparam.classLoader, "setModuleActive",
                    boolean.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            param.args[0] = true;
                        }
                    });
        }

        if (!isHooked && ClassMember.PACKAGE_NAME.equals(lpparam.packageName)) {
            isHooked = true;
            Log.i(TAG, lpparam.packageName);
            classLoader = lpparam.classLoader;
            hookRpcCall(lpparam.classLoader);
            hookService(lpparam.classLoader);
        }
    }

    private static void initHandler() {
        if (handler == null) {
            handler = new Handler();
            Config.setAlarm7(AntForestToast.context);
        }
        if (runnable == null) {
            FriendManager.fillUser(XposedHook.classLoader);

            runnable = new Runnable() {
                @Override
                public void run() {
                    Config.shouldReload = true;
                    Statistics.resetToday();
                    AntForest.checkEnergyRanking(XposedHook.classLoader, times);

                    if (TimeUtil.getTimeStr().compareTo("0700") < 0 || TimeUtil.getTimeStr().compareTo("0730") > 0) {
                        AntCooperate.start();
                        AntFarm.start();
                        Reserve.start();
                        if (TimeUtil.getTimeStr().compareTo("0800") >= 0) {
                            AncientTree.start();
                        }
                        AntSports.start(XposedHook.classLoader, times);
                        AntMember.receivePoint();
                        AntOcean.start();
                    }

                    if (Config.collectEnergy() || Config.enableFarm()) {
                        handler.postDelayed(this, Config.checkInterval());
                    } else {
                        AntForestNotification.stop(service, false);
                    }
                    times = (times + 1) % (3600_000 / Config.checkInterval());
                }
            };
        }
        handler.removeCallbacks(runnable);
        AntForest.stop();
        AntForestNotification.stop(service, false);
        AntForestNotification.start(service);
        handler.post(runnable);
    }

    private void hookService(ClassLoader loader) {
        try {
            XposedHelpers.findAndHookMethod("com.alipay.mobile.quinox.LauncherActivity", loader,
                    "onResume", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            String targetUid = RpcUtil.getUserId(loader);
                            if (targetUid == null || targetUid.equals(FriendIdMap.currentUid)) {
                                return;
                            }
                            FriendIdMap.currentUid = targetUid;
                            if (handler != null) {
                                Log.recordLog("尝试初始化");
                                times = 0;
                                initHandler();
                            }
                        }
                    });
            Log.i(TAG, "hook login successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook login err:");
            Log.printStackTrace(TAG, t);
        }
        try {
            XposedHelpers.findAndHookMethod(
                    "android.app.Service", loader, "onCreate", new XC_MethodHook() {
                        ClassLoader loader;

                        public XC_MethodHook setData(ClassLoader cl) {
                            loader = cl;
                            return this;
                        }

                        @SuppressLint("WakelockTimeout")
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Service service = (Service) param.thisObject;
                            if (!ClassMember.CURRENT_USING_SERVICE.equals(service.getClass().getCanonicalName())) {
                                return;
                            }
                            RpcUtil.isInterrupted = false;
                            registerBroadcastReceiver(service);
                            XposedHook.service = service;
                            AntForestToast.context = service.getApplicationContext();
                            RpcUtil.init(loader);
                            times = 0;
                            if (Config.stayAwake()) {
                                PowerManager pm = (PowerManager) service.getSystemService(Context.POWER_SERVICE);
                                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, service.getClass().getName());
                                wakeLock.acquire();
                            }
                            if (Config.stayAwake()) {
                                if (Config.stayAwakeType() == StayAwakeType.BROADCAST) {
                                    alarmBroadcast(AntForestToast.context, 30 * 60 * 1000, false);
                                } else if (Config.stayAwakeType() == StayAwakeType.ALARM) {
                                    alarmHook(AntForestToast.context, 30 * 60 * 1000, false);
                                }
                            }
                            initHandler();
                            super.afterHookedMethod(param);
                        }
                    }.setData(loader));
            Log.i(TAG, "hook onCreate successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook onCreate err:");
            Log.printStackTrace(TAG, t);
        }
        try {
            XposedHelpers.findAndHookMethod("android.app.Service", loader, "onDestroy", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    Service service = (Service) param.thisObject;
                    if (!ClassMember.CURRENT_USING_SERVICE.equals(service.getClass().getCanonicalName())) {
                        return;
                    }
                    if (wakeLock != null) {
                        wakeLock.release();
                        wakeLock = null;
                    }
                    AntForestNotification.stop(service, false);
                    AntForestNotification.setContentText("支付宝前台服务被销毁");
                    Log.recordLog("支付宝前台服务被销毁", "");
                    handler.removeCallbacks(runnable);
                    alarmHook(AntForestToast.context, 3000, false);
                }
            });
            Log.i(TAG, "hook onDestroy successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook onDestroy err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void hookRpcCall(ClassLoader loader) {
        try {
            Class<?> clazz = loader.loadClass(ClassMember.com_alipay_mobile_nebulaappproxy_api_rpc_H5AppRpcUpdate);
            Class<?> H5PageClazz = loader.loadClass(ClassMember.com_alipay_mobile_h5container_api_H5Page);
            XposedHelpers.findAndHookMethod(
                    clazz, ClassMember.matchVersion, H5PageClazz, Map.class, String.class,
                    XC_MethodReplacement.returnConstant(false));
            Log.i(TAG, "hook " + ClassMember.matchVersion + " successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook " + ClassMember.matchVersion + " err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public static void restartHook(Context context, boolean force) {
        try {
            Intent intent = new Intent();
            if (force || Config.stayAwakeTarget() == StayAwakeTarget.ACTIVITY) {
                intent.setClassName(ClassMember.PACKAGE_NAME, ClassMember.CURRENT_USING_ACTIVITY);
                if (force) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            } else {
                intent.setClassName(ClassMember.PACKAGE_NAME, ClassMember.CURRENT_USING_SERVICE);
                context.startService(intent);
            }
        } catch (Throwable t) {
            Log.i(TAG, "restartHook err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static int getPendingIntentFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
        } else {
            return PendingIntent.FLAG_UPDATE_CURRENT;
        }
    }

    public static void alarmBroadcast(Context context, long delayTime, boolean force) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent("com.eg.android.AlipayGphone.xqe.broadcast");
            intent.putExtra("force", force);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, getPendingIntentFlag());
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayTime, pi);
        } catch (Throwable th) {
            Log.printStackTrace(TAG, th);
        }
    }

    public static void alarmHook(Context context, long delayTime, boolean force) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi;
            if (force || Config.stayAwakeTarget() == StayAwakeTarget.ACTIVITY) {
                Intent it = new Intent();
                it.setClassName(ClassMember.PACKAGE_NAME, ClassMember.CURRENT_USING_ACTIVITY);
                pi = PendingIntent.getActivity(context, 1, it, getPendingIntentFlag());
            } else {
                Intent it = new Intent();
                it.setClassName(ClassMember.PACKAGE_NAME, ClassMember.CURRENT_USING_SERVICE);
                pi = PendingIntent.getService(context, 2, it, getPendingIntentFlag());
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayTime, pi);
        } catch (Throwable th) {
            Log.printStackTrace(TAG, th);
        }
    }

    private static class AlipayBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.eg.android.AlipayGphone.xqe.broadcast".equals(action)) {
                boolean force = intent.getBooleanExtra("force", false);
                restartHook(AntForestToast.context, force);
            } else if ("com.eg.android.AlipayGphone.xqe.test".equals(action)) {
                Log.recordLog("收到测试消息:");
                // XposedHook.restartHook(false);
            } else if ("com.eg.android.AlipayGphone.xqe.cancelAlarm7".equals(action)) {
                Config.cancelAlarm7(AntForestToast.context);
            }
        }
    }

    private void registerBroadcastReceiver(Context context) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.eg.android.AlipayGphone.xqe.broadcast");
            intentFilter.addAction("com.eg.android.AlipayGphone.xqe.test");
            intentFilter.addAction("com.eg.android.AlipayGphone.xqe.cancelAlarm7");
            context.registerReceiver(new AlipayBroadcastReceiver(), intentFilter);
            Log.recordLog("注册广播接收器成功", context.toString());
        } catch (Throwable th) {
            Log.i(TAG, "hook registerBroadcastReceiver err:");
            Log.printStackTrace(TAG, th);
        }
    }

}
