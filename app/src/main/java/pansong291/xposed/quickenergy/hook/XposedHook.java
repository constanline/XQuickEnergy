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
import android.os.Message;
import android.os.PowerManager;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import pansong291.xposed.quickenergy.*;
import pansong291.xposed.quickenergy.ui.MainActivity;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;
import pansong291.xposed.quickenergy.util.TimeUtil;

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

    public enum StayAwakeType {
        BROADCAST, ALARM, BROADCAST2ALARM, ALARM2BROADCAST;
        public static final CharSequence[] nickNames =
                {"广播", "闹钟", "广播转闹钟", "闹钟转广播"};
        public static final CharSequence[] names =
                {BROADCAST.nickName(), ALARM.nickName(), BROADCAST2ALARM.nickName(), ALARM2BROADCAST.nickName()};
        public CharSequence nickName() {
            return nickNames[ordinal()];
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if ("pansong291.xposed.quickenergy.repair".equals(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod(MainActivity.class.getName(), lpparam.classLoader, "setModuleActive", boolean.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    param.args[0] = true;
                }
            });
        }

        if (ClassMember.com_eg_android_AlipayGphone.equals(lpparam.packageName)) {
            Log.i(TAG, lpparam.packageName);
            hookLauncherService(lpparam.classLoader);
            hookRpcCall(lpparam.classLoader);
        }
    }

    private static void initHandler() {
        if (handler == null) handler = new Handler();
        if (runnable == null) runnable = new Runnable() {
            @Override
            public void run() {
                Config.shouldReload = true;
                RpcUtil.sendXEdgeProBroadcast = true;
                Statistics.resetToday();
                AntForest.checkEnergyRanking(XposedHook.classLoader, times);

                FriendManager.checkUnknownId(XposedHook.classLoader);
                if (Config.collectEnergy() || Config.enableFarm()) {
                    Message msg = Message.obtain(null, this);
                    msg.what = 999;
                    handler.sendMessageDelayed(msg, Config.checkInterval());
                }
                else {
                    AntForestNotification.stop(service, false);
                }
                times = (times + 1) % (3600_000 / Config.checkInterval());
            }
        };
        if (Config.collectEnergy() || Config.enableFarm()) {
            AntForestNotification.start(service);
            handler.post(runnable);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.recordLog("当前时间是" + TimeUtil.getTimeStr(), "");
                if (TimeUtil.getTimeStr().compareTo("0700") >= 0 && TimeUtil.getTimeStr().compareTo("0730") <= 0) {
                    Log.recordLog("进入其他1", "");
                    handler.postDelayed(this, 10 * 60 * 1000);
                } else {
                    Log.recordLog("进入其他2", "");
                    AntCooperate.start();
                    AntFarm.start();
                    AntMember.receivePoint();
                }
            }
        });
    }

    private void hookLauncherService(ClassLoader loader) {
        try {
            XposedHelpers.findAndHookMethod(
                    ClassMember.current_using_service, loader, "onBind", Intent.class, new XC_MethodHook() {
                        ClassLoader loader;

                        public XC_MethodHook setData(ClassLoader cl) {
                            loader = cl;
                            return this;
                        }

                        @SuppressLint("WakelockTimeout")
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            Service service = (Service) param.thisObject;
                            XposedHook.service = service;
                            XposedHook.classLoader = loader;
                            AntForestToast.context = service.getApplicationContext();
                            RpcUtil.init(loader);
                            times = 0;
                            if (Config.stayAwake()) {
                                PowerManager pm = (PowerManager) service.getSystemService(Context.POWER_SERVICE);
                                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, service.getClass().getName());
                                wakeLock.acquire();
                            }
                            registerBroadcastReceiver(service);
                            if (Config.stayAwake()) {
                                if (Config.stayAwakeType() == XposedHook.StayAwakeType.BROADCAST2ALARM) {
                                    AntForestToast.context.sendBroadcast(new Intent("com.eg.android.AlipayGphone.xqe.broadcast2alarm"));
                                } else if (Config.stayAwakeType() == XposedHook.StayAwakeType.ALARM2BROADCAST) {
                                    alarmBroadcast(AntForestToast.context);
                                }
                            }
                            initHandler();
                        }
                    }.setData(loader));
            Log.i(TAG, "hook onBind successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook onBind err:");
            Log.printStackTrace(TAG, t);
        }

        try {
            XposedHelpers.findAndHookMethod(ClassMember.current_using_service, loader,
                    "onUnbind", Intent.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    if (wakeLock != null) {
                        wakeLock.release();
                        wakeLock = null;
                    }
                    Service service = (Service) param.thisObject;
                    AntForestNotification.stop(service, false);
                    AntForestNotification.setContentText("支付宝前台服务被销毁");
                    Log.recordLog("支付宝前台服务被销毁", "");
                    handler.removeCallbacks(runnable);
                    if (Config.autoRestart()) {
                        alarmService(service, 1000 * 60 * 15);
                    }
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

    public static void alarmService(Context context, long delayTime) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent it = new Intent();
            it.setClassName(ClassMember.com_eg_android_AlipayGphone, ClassMember.current_using_activity);
            PendingIntent pi = PendingIntent.getActivity(context, 0, it,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayTime, pi);

            it.setClassName(ClassMember.com_eg_android_AlipayGphone, ClassMember.current_using_service);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pi = PendingIntent.getForegroundService(context, 0, it,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            } else {
                pi = PendingIntent.getService(context, 0, it,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayTime, pi);

            if (Config.stayAwakeType() == StayAwakeType.BROADCAST2ALARM) {
                pi = PendingIntent.getBroadcast(context, 0, new Intent("com.eg.android.AlipayGphone.xqe.broadcast2alarm"),
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 15 * 60 * 1000, pi);
            }
        } catch (Throwable th) {
            Log.printStackTrace(TAG, th);
        }
    }

    private static void restartService(Context context) {
        try {
            if (handler == null || !handler.hasMessages(999)) {

                Intent newIntent = new Intent();
                newIntent.setClassName("com.eg.android.AlipayGphone", ClassMember.current_using_activity);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(newIntent);

                newIntent = new Intent();
                newIntent.setClassName("com.eg.android.AlipayGphone", ClassMember.current_using_service);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(newIntent);
                } else {
                    context.startService(newIntent);
                }
            }
        } catch (Throwable th) {
            Log.printStackTrace(TAG, th);
        }
    }

    private static void alarmBroadcast(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent it = new Intent("com.eg.android.AlipayGphone.xqe.broadcast");
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, it,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 * 15, pi);
        } catch (Throwable th) {
            Log.printStackTrace(TAG, th);
        }
    }

    private static class AlipayBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.eg.android.AlipayGphone.xqe.broadcast".equals(intent.getAction())) {
                restartService(context);
            } else if ("com.eg.android.AlipayGphone.xqe.broadcast2alarm".equals(intent.getAction())) {
                alarmService(context, 1000);
            } else if ("com.eg.android.AlipayGphone.xqe.alarm2broadcast".equals(intent.getAction())) {
                alarmBroadcast(context);
                restartService(context);
            }
        }
    }

    private void registerBroadcastReceiver(Context context) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.eg.android.AlipayGphone.xqe.broadcast");
            intentFilter.addAction("com.eg.android.AlipayGphone.xqe.broadcast2alarm");
            intentFilter.addAction("com.eg.android.AlipayGphone.xqe.alarm2broadcast");
            context.registerReceiver(new AlipayBroadcastReceiver(), intentFilter);
            Log.recordLog("注册广播接收器成功" , context.toString());
        } catch (Throwable th) {
            Log.i(TAG, "hook registerBroadcastReceiver err:");
            Log.printStackTrace(TAG, th);
        }
    }

}
