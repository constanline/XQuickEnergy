package pansong291.xposed.quickenergy.util;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BeachIdMap {
    private static final String TAG = BeachIdMap.class.getCanonicalName();

    public static boolean shouldReload = false;

    private static Map<String, String> idMap;
    private static boolean hasChanged = false;

    public static void putIdMap(String key, String value) {
        if (key == null || key.isEmpty())
            return;
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
        if (key == null || key.isEmpty())
            return;
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
            hasChanged = !FileUtils.write2File(sb.toString(), FileUtils.getBeachIdMapFile());
        }
    }

    public static Map<String, String> getIdMap() {
        if (idMap == null || shouldReload) {
            shouldReload = false;
            idMap = new TreeMap<>();
            String str = FileUtils.readFromFile(FileUtils.getBeachIdMapFile());
            if (str != null && str.length() > 0) {
                try {
                    String[] idSet = str.split("\n");
                    for (String s : idSet) {
                        // Log.i(TAG, s);
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
