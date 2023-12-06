package pansong291.xposed.quickenergy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.hook.XposedHook;

public class AntForestToast {
    private static final String TAG = AntForestToast.class.getCanonicalName();

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public static void show(CharSequence cs) {
        show(cs, false);
    }

    public static void show(CharSequence cs, boolean force) {
        try {
            if (context != null && (force || Config.showToast())) {
                XposedHook.handler.post(
                        new Runnable() {
                            CharSequence cs;

                            public Runnable setData(CharSequence c) {
                                cs = c;
                                return this;
                            }

                            @Override
                            public void run() {
                                try {
                                    Toast toast = Toast.makeText(context, cs, Toast.LENGTH_SHORT);
                                    toast.setGravity(toast.getGravity(), toast.getXOffset(), Config.toastOffsetY());
                                    toast.show();
                                } catch (Throwable t) {
                                    Log.i(TAG, "show.run err:");
                                    Log.printStackTrace(TAG, t);
                                }
                            }
                        }.setData(cs));
            }
        } catch (Throwable t) {
            Log.i(TAG, "show err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
