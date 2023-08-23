package pansong291.xposed.quickenergy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import pansong291.xposed.quickenergy.data.RuntimeInfo;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.TimeUtil;

import java.text.DateFormat;

public class AntForestNotification {
    public static final int NOTIFICATION_ID = 99;
    public static final String CHANNEL_ID = "pansong291.xposed.quickenergy.repair.ANTFOREST_NOTIFY_CHANNEL";
    private static NotificationManager mNotifyManager;
    private static Notification mNotification;
    private static Notification.Builder builder;
    private static boolean isStart = false;

    private static long nextScanTime = 0;
    private static CharSequence contentText = "";

    private AntForestNotification() {
    }

    public static void start(Context context) {
        initNotification(context);
        if (!isStart) {
            if (context instanceof Service)
                ((Service) context).startForeground(NOTIFICATION_ID, mNotification);
            else
                getNotificationManager(context).notify(NOTIFICATION_ID, mNotification);
            isStart = true;
        }
        setContentText("运行中...");
    }

    public static void stop(Context context, boolean remove) {
        if (isStart) {
            if (context instanceof Service)
                ((Service) context).stopForeground(remove);
            else
                getNotificationManager(context).cancel(NOTIFICATION_ID);
            isStart = false;
            mNotification = null;
        }
    }

    private static NotificationManager getNotificationManager(Context context) {
        if (mNotifyManager == null)
            mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return mNotifyManager;
    }

    private static void initNotification(Context context) {
        if (mNotification == null) {
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse("alipays://platformapi/startapp?appId="));
            PendingIntent pi = PendingIntent.getActivity(context, 0, it,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "芝麻粒能量提醒",
                        NotificationManager.IMPORTANCE_LOW);
                notificationChannel.enableLights(false);
                notificationChannel.enableVibration(false);
                notificationChannel.setShowBadge(false);
                getNotificationManager(context).createNotificationChannel(notificationChannel);
                builder = new Notification.Builder(context, CHANNEL_ID);
            } else {
                getNotificationManager(context);
                builder = new Notification.Builder(context)
                        .setPriority(Notification.PRIORITY_LOW);
            }
            builder
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentTitle("芝麻粒")
                    .setAutoCancel(false)
                    .setContentIntent(pi);
            if (Config.enableOnGoing()) {
                builder.setOngoing(true);
            }
            mNotification = builder.build();
        }
    }

    public static void setNextScanTime(long nextScanTime) {
        AntForestNotification.nextScanTime = nextScanTime;
        if (isStart) {
            innerSetContentText();
        }
    }

    private static void innerSetContentText() {
        Notification.InboxStyle style = new Notification.InboxStyle();
//        String preContent = (nextScanTime > 0) ? "下次扫描时间" + TimeUtil.getTimeStr(nextScanTime) + "\n" : "";
        if (nextScanTime > 0) {
            style.addLine("下次扫描时间" + TimeUtil.getTimeStr(nextScanTime));
        }
        style.addLine(contentText);
        builder.setStyle(style);
        mNotification = builder.build();
        if (mNotifyManager != null)
            mNotifyManager.notify(NOTIFICATION_ID, mNotification);
    }

    public static void setContentText(CharSequence cs) {
        if (isStart) {
            long forestPauseTime = RuntimeInfo.getInstance().getLong(RuntimeInfo.RuntimeInfoKey.ForestPauseTime);
            if (forestPauseTime > System.currentTimeMillis()) {
                cs = "触发异常,等待至" + DateFormat.getDateTimeInstance().format(forestPauseTime);
            }
            contentText = cs;
            innerSetContentText();
        }
    }

}
