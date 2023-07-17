package pansong291.xposed.quickenergy.util;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String padLeft(int str, int totalWidth, char padChar) {
        return padLeft(String.valueOf(str), totalWidth, padChar);
    }

    public static String padRight(int str, int totalWidth, char padChar) {
        return padRight(String.valueOf(str), totalWidth, padChar);
    }

    public static String padLeft(String str, int totalWidth, char padChar) {
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < totalWidth) {
            sb.insert(0, padChar);
        }
        return sb.toString();
    }

    public static String padRight(String str, int totalWidth, char padChar) {
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < totalWidth) {
            sb.append(padChar);
        }
        return sb.toString();
    }
}
