package pansong291.xposed.quickenergy.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import pansong291.xposed.quickenergy.*;
import pansong291.xposed.quickenergy.data.RuntimeInfo;
import pansong291.xposed.quickenergy.ui.MainActivity;
import pansong291.xposed.quickenergy.util.*;

import java.util.Calendar;
import java.util.Map;

public class XposedHook implements IXposedHookLoadPackage {
    private static final String TAG = XposedHook.class.getCanonicalName();

    @SuppressLint("StaticFieldLeak")
    private static Service service;
    public static ClassLoader classLoader;
    private static PowerManager.WakeLock wakeLock;
    public static Handler handler;
    private static Runnable runnable;

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

        if (ClassMember.PACKAGE_NAME.equals(lpparam.processName) && ClassMember.PACKAGE_NAME.equals(lpparam.packageName)) {
            if (!isHooked) {
                RuntimeInfo.process = lpparam.packageName;
                isHooked = true;
                Log.i(TAG, lpparam.packageName);
                classLoader = lpparam.classLoader;
                hookRpcCall();
                hookStep();
                hookService(lpparam.classLoader);
                PluginUtils.invoke(XposedHook.class, PluginUtils.PluginAction.INIT);
            }
        }
    }

    private static void initHandler() {
        Log.recordLog("尝试初始化");
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    PluginUtils.invoke(XposedHook.class, PluginUtils.PluginAction.START);
                    String targetUid = RpcUtil.getUserId(XposedHook.classLoader);
                    if (targetUid != null) {
                        FriendIdMap.setCurrentUid(targetUid);

                        Statistics.resetToday();
                        AntForest.checkEnergyRanking(XposedHook.classLoader);
// lzw add begin
                        if (false == Config.isOnlyCollectEnergyTime()) {
                            Log.other("非只收能量时间段");
// lzw add end
                            AntCooperate.start();
                            AntFarm.start();
                            Reserve.start();
                            if (TimeUtil.getTimeStr().compareTo("0700") >= 0) {
                                AncientTree.start();
                                AntBookRead.start();
                            }
                            AntSports.start(XposedHook.classLoader);
                            AntMember.receivePoint();
                            AntOcean.start();
                            AntOrchard.start();
                            AntStall.start();
                            GreenFinance.start();
                            OmegakoiTown.start();
                            ConsumeGold.start();
                        }
                    }
                    if (Config.collectEnergy() || Config.enableFarm()) {
                        AntForestNotification.setNextScanTime(System.currentTimeMillis() + Config.checkInterval());
                        handler.postDelayed(this, Config.checkInterval());
                    } else {
                        AntForestNotification.stop(service, false);
                    }

                    PluginUtils.invoke(XposedHook.class, PluginUtils.PluginAction.STOP);
                }
            };
        }
        try {
            if (handler == null) {
                handler = new Handler();
                if (Config.startAt7()) {
                    Config.setAlarm7(AntForestToast.context);
                }
            }
            AntForestToast.show("芝麻粒加载成功");
            handler.removeCallbacks(runnable);
            AntForest.stop();
            AntForestNotification.stop(service, false);
            AntForestNotification.start(service);
            handler.post(runnable);
        } catch (Throwable th) {
            Log.i(TAG, "initHandler err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private void hookService(ClassLoader loader) {
        try {
            XposedHelpers.findAndHookMethod("com.alipay.mobile.quinox.LauncherActivity", loader,
                    "onResume", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            Log.i(TAG, "Activity onResume");
                            RpcUtil.isInterrupted = false;
                            PermissionUtil.requestPermissions((Activity) param.thisObject);
                            AntForestNotification.setContentText("运行中...");
                            String targetUid = RpcUtil.getUserId(loader);
                            if (targetUid == null || targetUid.equals(FriendIdMap.getCurrentUid())) {
                                return;
                            }
                            FriendIdMap.setCurrentUid(targetUid);
                            if (handler != null) {
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

                        @SuppressLint("WakelockTimeout")
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            Service service = (Service) param.thisObject;
                            if (!ClassMember.CURRENT_USING_SERVICE.equals(service.getClass().getCanonicalName())) {
                                return;
                            }
                            Log.i(TAG, "Service onCreate");
                            RpcUtil.isInterrupted = false;
                            AntForestNotification.setContentText("运行中...");
                            registerBroadcastReceiver(service);
                            XposedHook.service = service;
                            AntForestToast.context = service.getApplicationContext();
                            RpcUtil.init(XposedHook.classLoader);
                            if (Config.stayAwake()) {
                                PowerManager pm = (PowerManager) service.getSystemService(Context.POWER_SERVICE);
                                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, service.getClass().getName());
                                wakeLock.acquire();

                                if (Config.stayAwakeType() == StayAwakeType.BROADCAST) {
                                    alarmBroadcast(AntForestToast.context, 30 * 60 * 1000, false);
                                } else if (Config.stayAwakeType() == StayAwakeType.ALARM) {
                                    alarmHook(AntForestToast.context, 30 * 60 * 1000, false);
                                }
                            }
                            initHandler();
                        }
                    });
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
                    alarmHook(AntForestToast.context, 3000, false);
                }
            });
            Log.i(TAG, "hook onDestroy successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook onDestroy err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void hookRpcCall() {
        try {
            Class<?> clazz = classLoader.loadClass(ClassMember.com_alipay_mobile_nebulaappproxy_api_rpc_H5AppRpcUpdate);
            Class<?> H5PageClazz = classLoader.loadClass(ClassMember.com_alipay_mobile_h5container_api_H5Page);
            XposedHelpers.findAndHookMethod(
                    clazz, ClassMember.matchVersion, H5PageClazz, Map.class, String.class,
                    XC_MethodReplacement.returnConstant(false));
            Log.i(TAG, "hook " + ClassMember.matchVersion + " successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook " + ClassMember.matchVersion + " err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void hookStep() {
        try {
            XposedHelpers.findAndHookMethod("com.alibaba.health.pedometer.core.datasource.PedometerAgent", classLoader,
                    "readDailyStep", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            int originStep = (Integer) param.getResult();
                            int step = Config.tmpStepCount();
                            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 6 || originStep >= step) {
                                return;
                            }
                            param.setResult(step);

                        }
                    });
        } catch (Throwable t) {
            Log.i(TAG, "hookStep err:");
            Log.printStackTrace(TAG, t);
        }

    }

    public static void restartHook(Context context, boolean force) {
        try {
            Intent intent;
            if (force || Config.stayAwakeTarget() == StayAwakeTarget.ACTIVITY) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName(ClassMember.PACKAGE_NAME, ClassMember.CURRENT_USING_ACTIVITY);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                intent = new Intent();
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
                Log.recordLog("收到测试消息");
//                alarmHook(AntForestToast.context, 3000, true);
            } else if ("com.eg.android.AlipayGphone.xqe.cancelAlarm7".equals(action)) {
                Config.cancelAlarm7(AntForestToast.context, false);
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
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
