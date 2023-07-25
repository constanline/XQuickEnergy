package pansong291.xposed.quickenergy.util;


import java.util.Calendar;

/**
 * @author Constanline
 * @since 2023/07/17
 */
public class TimeUtil {
    public static String getTimeStr() {
        return getTimeStr(0);
    }
    public static String getTimeStr(int ms) {
        Calendar c = Calendar.getInstance();
        if (ms != 0) {
            c.add(Calendar.MILLISECOND, ms);
        }
        return StringUtil.padLeft(c.get(Calendar.HOUR_OF_DAY), 2, '0') +
                StringUtil.padLeft(c.get(Calendar.MINUTE), 2, '0');
    }

    public static boolean checkInTime(int ms, String strTime) {
        String min = TimeUtil.getTimeStr(ms);
        String max = TimeUtil.getTimeStr();
        return min.compareTo(strTime) <= 0 && max.compareTo(strTime) > 0;
    }
}
