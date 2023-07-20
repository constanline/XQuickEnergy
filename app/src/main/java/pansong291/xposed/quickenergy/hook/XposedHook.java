package pansong291.xposed.quickenergy.hook;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
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
        BROADCAST, ALARM, NONE;
        public static final CharSequence[] nickNames =
                {"广播", "闹钟", "不重启"};
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

        if (ClassMember.PACKAGE_NAME.equals(lpparam.packageName)) {
            Log.i(TAG, lpparam.packageName);
            hookRpcCall(lpparam.classLoader);
            hookService(lpparam.classLoader);
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

                FriendManager.fillUser(XposedHook.classLoader);
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
                if (TimeUtil.getTimeStr().compareTo("0700") >= 0 && TimeUtil.getTimeStr().compareTo("0730") <= 0) {
                    handler.postDelayed(this, 10 * 60 * 1000);
                } else {
                    AntCooperate.start();
                    AntFarm.start();
                    AntMember.receivePoint();
                }
            }
        });
    }

    private void hookService(ClassLoader loader) {
        try {
            XposedHelpers.findAndHookMethod(
                    ClassMember.CURRENT_USING_SERVICE, loader, "onBind", Intent.class, new XC_MethodHook() {
                        ClassLoader loader;

                        public XC_MethodHook setData(ClassLoader cl) {
                            loader = cl;
                            return this;
                        }

                        @SuppressLint("WakelockTimeout")
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            Service service = (Service) param.thisObject;
                            if (!ClassMember.CURRENT_USING_SERVICE.equals(service.getClass().getCanonicalName())) {
                                return;
                            }
                            registerBroadcastReceiver(service);
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
                            if (Config.stayAwake()) {
                                if (Config.stayAwakeType() == StayAwakeType.BROADCAST) {
                                    AntForestToast.context.sendBroadcast(new Intent("com.eg.android.AlipayGphone.xqe.broadcast"));
                                } else if (Config.stayAwakeType() == StayAwakeType.ALARM) {
                                    alarmActivity(AntForestToast.context, 30 * 60 * 1000);
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

    public static void restartActivity(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(ClassMember.PACKAGE_NAME, ClassMember.CURRENT_USING_ACTIVITY));
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.startActivity(intent);
    }

    public static void alarmActivity(Context context, long delayTime) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent it = new Intent();
            it.setClassName(ClassMember.PACKAGE_NAME, ClassMember.CURRENT_USING_ACTIVITY);
            PendingIntent pi = PendingIntent.getActivity(context, 0, it,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
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
                restartActivity(context);
            }
        }
    }

    private void registerBroadcastReceiver(Context context) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.eg.android.AlipayGphone.xqe.broadcast");
            context.registerReceiver(new AlipayBroadcastReceiver(), intentFilter);
            Log.recordLog("注册广播接收器成功" , context.toString());
        } catch (Throwable th) {
            Log.i(TAG, "hook registerBroadcastReceiver err:");
            Log.printStackTrace(TAG, th);
        }
    }

}
