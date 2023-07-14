package pansong291.xposed.quickenergy.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import pansong291.xposed.quickenergy.hook.AntForestRpcCall;

public class PermissionUtil {
    private static final String TAG = AntForestRpcCall.class.getCanonicalName();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
    };


    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = activity.checkSelfPermission(PERMISSIONS_STORAGE[0]);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                activity.requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            Log.printStackTrace(TAG, e);
        }
    }
}
