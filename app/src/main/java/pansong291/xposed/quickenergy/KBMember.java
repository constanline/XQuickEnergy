package pansong291.xposed.quickenergy;

import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.KBMemberRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;

public class KBMember {
    private static final String TAG = KBMember.class.getCanonicalName();

    public static void start(ClassLoader loader) {
        if (!Config.kbSginIn() || !Statistics.canKbSignInToday())
            return;

        new Thread() {
            private ClassLoader loader;

            public Thread setData(ClassLoader cl) {
                loader = cl;
                return this;
            }

            @Override
            public void run() {
                try {
                    signIn(loader);
                } catch (Throwable t) {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }.setData(loader).start();
    }

    private static void signIn(ClassLoader loader) {
        try {
            String s = KBMemberRpcCall.rpcCall_signIn();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                jo = jo.getJSONObject("data");
                Log.other("口碑签到📅[第" + jo.getString("dayNo") + "天]#获得" + jo.getString("value") + "积分");
                Statistics.KbSignInToday();
            } else if (s.contains("\"HAS_SIGN_IN\"")) {
                Statistics.KbSignInToday();
            } else {
                Log.i(TAG, jo.getString("errorMessage"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "signIn err:");
            Log.printStackTrace(TAG, t);
        }
    }

}
