package pansong291.xposed.quickenergy.data;

import org.json.JSONException;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.util.FileUtils;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;

import java.util.Objects;

/**
 * @author Constanline
 * @since 2023/08/18
 */
public class RuntimeInfo {
    private static final String TAG = RuntimeInfo.class.getCanonicalName();

    private static RuntimeInfo instance;

    private final String userId;

    private JSONObject joAll;

    private JSONObject joCurrent;

    public enum RuntimeInfoKey {
        ForestPauseTime
    }

    public static RuntimeInfo getInstance() {
        if (instance == null || !Objects.equals(instance.userId, FriendIdMap.currentUid)) {
            instance = new RuntimeInfo();
        }
        return instance;
    }

    private RuntimeInfo() {
        userId = FriendIdMap.currentUid;
        String content = FileUtils.readFromFile(FileUtils.runtimeInfoFile());
        try {
            joAll = new JSONObject(content);
        } catch (Exception ignored) {
            joAll = new JSONObject();
        }
        try {
            if (!joAll.has(userId)) {
                joAll.put(userId, new JSONObject());
            }
        } catch (Exception ignored) {
        }
        try {
            joCurrent = joAll.getJSONObject(userId);
        } catch (Exception ignored) {
            joCurrent = new JSONObject();
        }
    }

    public void save() {
        FileUtils.write2File(joAll.toString(), FileUtils.runtimeInfoFile());
    }

    public Object get(RuntimeInfoKey key) throws JSONException {
        return joCurrent.opt(key.name());
    }

    public String getString(RuntimeInfoKey key) {
        return joCurrent.optString(key.name());
    }

    public Long getLong(RuntimeInfoKey key) {
        return joCurrent.optLong(key.name(), 0L);
    }

    public void put(RuntimeInfoKey key, Object value) {
        try {
            joCurrent.put(key.name(), value);
            joAll.put(userId, joCurrent);
        } catch (JSONException e) {
            Log.i(TAG, "put err:");
            Log.printStackTrace(TAG, e);
        }
        save();
    }
}