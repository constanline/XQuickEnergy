package pansong291.xposed.quickenergy.hook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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

import java.util.Map;

public class XposedHook implements IXposedHookLoadPackage {
    private static final String TAG = XposedHook.class.getCanonicalName();
    private static PowerManager.WakeLock wakeLock;
    public static Handler handler;
    private static Runnable runnable;
    private static int times;

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

    private void hookLauncherService(ClassLoader loader) {
        try {
            XposedHelpers.findAndHookMethod(
                    "com.alipay.dexaop.power.RuntimePowerService", loader, "onBind", Intent.class, new XC_MethodHook() {
                        ClassLoader loader;

                        public XC_MethodHook setData(ClassLoader cl) {
                            loader = cl;
                            return this;
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            Service service = (Service) param.thisObject;
                            AntForestToast.context = service.getApplicationContext();
                            RpcUtil.init(loader);
                            times = 0;
                            if (Config.stayAwake()) {
                                PowerManager pm = (PowerManager) service.getSystemService(Context.POWER_SERVICE);
                                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, service.getClass().getName());
                                wakeLock.acquire(10*60*1000L);
                            }
                            if (handler == null) handler = new Handler();
                            if (runnable == null) runnable = new Runnable() {
                                Service service;
                                ClassLoader loader;

                                public Runnable setData(Service s, ClassLoader cl) {
                                    service = s;
                                    loader = cl;
                                    return this;
                                }

                                @Override
                                public void run() {
                                    Config.shouldReload = true;
                                    RpcUtil.sendXEdgeProBroadcast = true;
                                    Statistics.resetToday();
                                    AntForest.checkEnergyRanking(loader, times);
                                    AntCooperate.start(loader, times);
                                    AntFarm.start(loader);
                                    AntMember.receivePoint(loader, times);

                                    FriendManager.checkUnknownId(loader);
//                                    AntSports.start(loader, times);
//                                    KBMember.start(loader);
                                    if (Config.collectEnergy() || Config.enableFarm())
                                        handler.postDelayed(this, Config.checkInterval());
                                    else AntForestNotification.stop(service, false);
                                    times = (times + 1) % (3600_000 / Config.checkInterval());
                                }
                            }.setData(service, loader);
                            if (Config.collectEnergy() || Config.enableFarm()) {
                                AntForestNotification.start(service);
                                handler.post(runnable);
                            }
                        }
                    }.setData(loader));
            Log.i(TAG, "hook " + ClassMember.onCreate + " successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook " + ClassMember.onCreate + " err:");
            Log.printStackTrace(TAG, t);
        }

        try {
            XposedHelpers.findAndHookMethod(ClassMember.com_alipay_android_launcher_service_LauncherService, loader, ClassMember.onDestroy, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
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
                        AlarmManager alarmManager = (AlarmManager) service.getSystemService(Context.ALARM_SERVICE);
                        Intent it = new Intent();
                        it.setClassName(ClassMember.com_eg_android_AlipayGphone, ClassMember.com_alipay_android_launcher_service_LauncherService);
                        PendingIntent pi = PendingIntent.getService(service, 0, it,
                                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pi);
                    }
                }
            });
            Log.i(TAG, "hook " + ClassMember.onDestroy + " successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook " + ClassMember.onDestroy + " err:");
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


}
