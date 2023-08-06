package pansong291.xposed.quickenergy.util;

import java.util.Random;

public class RandomUtils
{
    private static final Random rnd = new Random();

    public static int delay()
    {
        return nextInt(100, 300);
    }

    public static int nextInt(int min, int max)
    {
        return rnd.nextInt(max - min) + min;
    }

    public static long nextLong() {
        return rnd.nextLong();
    }
    public static double nextDouble() {
        return rnd.nextDouble();
    }

    public static String getRandom(int len) {
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < len; i++) {
            rs.append(rnd.nextInt(10));
        }
        return rs.toString();
    }
}
