package pansong291.xposed.quickenergy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FriendIdMap {
    private static final String TAG = FriendIdMap.class.getCanonicalName();

    public static boolean shouldReload = false;

    public static String currentUid = null;

    private static Map<String, String> idMap;
    private static boolean hasChanged = false;

    public static void putIdMapIfEmpty(String key, String value) {
        if (key == null || key.isEmpty()) return;
        if (!getIdMap().containsKey(key)) {
            getIdMap().put(key, value);
            hasChanged = true;
        }
    }

    public static void putIdMap(String key, String value) {
        if (key == null || key.isEmpty()) return;
        if (getIdMap().containsKey(key)) {
            if (!getIdMap().get(key).equals(value)) {
                getIdMap().remove(key);
                getIdMap().put(key, value);
                hasChanged = true;
            }
        } else {
            getIdMap().put(key, value);
            hasChanged = true;
        }
    }

    public static void removeIdMap(String key) {
        if (key == null || key.isEmpty()) return;
        if (getIdMap().containsKey(key)) {
            getIdMap().remove(key);
            hasChanged = true;
        }
    }

    public static void saveIdMap() {
        if (hasChanged) {
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, String>> idSet = getIdMap().entrySet();
            for (Map.Entry<String, String> entry : idSet) {
                sb.append(entry.getKey());
                sb.append(':');
                sb.append(entry.getValue());
                sb.append('\n');
            }
            hasChanged = !FileUtils.write2File(sb.toString(), FileUtils.getFriendIdMapFile());
        }
    }

    public static String getNameById(String id) {
        if (id == null || id.isEmpty()) return id;
        if (getIdMap().containsKey(id)) {
            String n = getIdMap().get(id);
            int ind = n.lastIndexOf('(');
            if (ind > 0) n = n.substring(0, ind);
            if (!n.equals("*")) return n;
        } else {
            putIdMap(id, "*(*)");
        }
        return id;
    }

    public static List<String> getIncompleteUnknownIds() {
        List<String> idList = new ArrayList<>();
        for (Map.Entry<String, String> entry : getIdMap().entrySet()) {
            if ("我".equals(entry.getValue())) {
                continue;
            }
            if (entry.getValue().split("\\|").length < 2) {
                idList.add(entry.getKey());
                Log.i(TAG, "未知id: " + entry.getKey());
            }
        }
        return idList;
    }

    public static Map<String, String> getIdMap() {
        if (idMap == null || shouldReload) {
            shouldReload = false;
            idMap = new ConcurrentHashMap<>();
            String str = FileUtils.readFromFile(FileUtils.getFriendIdMapFile());
            if (str != null && str.length() > 0) {
                try {
                    String[] idSet = str.split("\n");
                    for (String s : idSet) {
                        Log.i(TAG, s);
                        int ind = s.indexOf(":");
                        idMap.put(s.substring(0, ind), s.substring(ind + 1));
                    }
                } catch (Throwable t) {
                    Log.printStackTrace(TAG, t);
                    idMap.clear();
                }
            }
        }
        return idMap;
    }

}
