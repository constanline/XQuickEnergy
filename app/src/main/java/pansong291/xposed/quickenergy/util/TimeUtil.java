package pansong291.xposed.quickenergy.util;

import java.util.Calendar;
import java.text.DateFormat;

/**
 * @author Constanline
 * @since 2023/07/17
 */
public class TimeUtil {

    public static final DateFormat DATE_FORMAT = DateFormat.getDateInstance();
    public static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance();

    public static String getTimeStr() {
        return getTimeStr(0);
    }

    public static String getTimeStr(long ts) {
        return TIME_FORMAT.format(new java.util.Date(ts));
    }

    public static String getTimeStr(int plusMs) {
        Calendar c = Calendar.getInstance();
        if (plusMs != 0) {
            c.add(Calendar.MILLISECOND, plusMs);
        }
        return StringUtil.padLeft(c.get(Calendar.HOUR_OF_DAY), 2, '0') +
                StringUtil.padLeft(c.get(Calendar.MINUTE), 2, '0');
    }

    public static String getDateStr() {
        return getDateStr(0);
    }

    public static String getDateStr(int plusDay) {
        Calendar c = Calendar.getInstance();
        if (plusDay != 0) {
            c.add(Calendar.DATE, plusDay);
        }
        return DATE_FORMAT.format(c.getTime());
    }

    public static boolean checkInTime(int ms, String strTime) {
        String min = TimeUtil.getTimeStr(ms);
        String max = TimeUtil.getTimeStr();
        return min.compareTo(strTime) <= 0 && max.compareTo(strTime) > 0;
    }

}
