package pansong291.xposed.quickenergy.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Log
{
    private static final String TAG = Log.class.getCanonicalName();
    private static SimpleDateFormat sdf;

    public static void i(String tag, String s) {
        FileUtils.append2RuntimeLogFile(tag + ", " + s);
    }

    public static void printStackTrace(String tag, Throwable t) {
        Log.i(tag, android.util.Log.getStackTraceString(t));
    }

    public static boolean forest(String s) {
        recordLog(s, "");
        return FileUtils.append2File(getFormatTime() + " " + s + "\n", FileUtils.getForestLogFile());
    }

    public static void farm(String s) {
        recordLog(s, "");
        FileUtils.append2File(getFormatTime() + " " + s + "\n", FileUtils.getFarmLogFile());
    }

    public static void other(String s) {
        recordLog(s, "");
        FileUtils.append2File(getFormatTime() + " " + s + "\n", FileUtils.getOtherLogFile());
    }

    public static void recordLog(String str) {
        recordLog(str, "");
    }

    public static void recordLog(String str, String str2) {
        Log.i(TAG, str + str2);
        if(!Config.recordLog()) return;
        FileUtils.append2SimpleLogFile(str);
    }

    public static String getFormatDateTime() {
        if(sdf == null) sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getFormatDate() {
        return getFormatDateTime().split(" ")[0];
    }

    public static String getFormatTime() {
        return getFormatDateTime().split(" ")[1];
    }

}
