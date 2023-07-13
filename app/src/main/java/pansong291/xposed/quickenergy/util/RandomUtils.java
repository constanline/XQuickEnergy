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
}
