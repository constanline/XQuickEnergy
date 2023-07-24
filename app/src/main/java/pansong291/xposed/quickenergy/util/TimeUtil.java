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
    public static String getTimeStr(int plusMin) {
        Calendar c = Calendar.getInstance();
        if (plusMin != 0) {
            c.add(Calendar.MINUTE, plusMin);
        }
        return StringUtil.padLeft(c.get(Calendar.HOUR_OF_DAY), 2, '0') +
                StringUtil.padLeft(c.get(Calendar.MINUTE), 2, '0');
    }
}
