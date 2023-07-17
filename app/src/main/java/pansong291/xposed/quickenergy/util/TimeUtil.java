package pansong291.xposed.quickenergy.util;


import java.util.Calendar;

/**
 * @author Constanline
 * @since 2023/07/17
 */
public class TimeUtil {
    public static String getTimeStr() {
        Calendar c = Calendar.getInstance();
        return StringUtil.padLeft(c.get(Calendar.HOUR_OF_DAY), 2, '0') +
                StringUtil.padLeft(c.get(Calendar.MINUTE), 2, '0');
    }
}
