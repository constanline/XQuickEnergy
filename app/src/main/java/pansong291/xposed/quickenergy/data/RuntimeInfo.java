package pansong291.xposed.quickenergy.data;

import org.json.JSONObject;
import pansong291.xposed.quickenergy.util.FileUtils;
import pansong291.xposed.quickenergy.util.FriendIdMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author Constanline
 * @since 2023/08/18
 */
public class RuntimeInfo {

    private static RuntimeInfo instance;

    private final String userId;

    private final Map<String, Object> map;

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
        map = new HashMap<>();
        try {
            JSONObject jo = new JSONObject(content);
            if (jo.has(userId)) {
                JSONObject userInfo = jo.getJSONObject(userId);
                for (Iterator<String> it = userInfo.keys(); it.hasNext(); ) {
                    String key = it.next();
                    map.put(key, userInfo.get(key));
                }
            }
        } catch (Exception ignored) { }
    }

    public Object get(RuntimeInfoKey key) {
        if (!map.containsKey(key.name())) {
            return null;
        }
        return map.get(key.name());
    }

    public String getString(RuntimeInfoKey key) {
        if (!map.containsKey(key.name())) {
            return null;
        }
        return (String) map.get(key.name());
    }

    public Long getLong(RuntimeInfoKey key) {
        if (!map.containsKey(key.name())) {
            return 0L;
        }
        return (Long) map.get(key.name());
    }

    public void put(RuntimeInfoKey key, Object value) {
        map.put(key.name(), value);
    }
}
