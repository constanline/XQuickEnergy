package pansong291.xposed.quickenergy.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Constanline
 * @since 2023/08/28
 */
public class PluginUtils {
    private static final Map<String, AbsPlugin> pluginMap = new HashMap<>();
    public static abstract class AbsPlugin {

        protected void onInit() {

        }

        protected void onStart() {

        }

        protected void onStop() {

        }
    }

    public enum PluginAction {
        INIT, START, STOP
    }

    public static void invoke(Class<?> cls, PluginAction action) {
        String pluginName = "pansong291.xposed.quickenergy.plugin." + cls.getSimpleName() + "Plugin";
        AbsPlugin plugin = null;
        synchronized (pluginMap) {
            if (!pluginMap.containsKey(pluginName)) {
                try {
                    plugin = (AbsPlugin) Class.forName(pluginName).newInstance();
                } catch (Throwable ignored) {}
                pluginMap.put(pluginName, plugin);
            } else {
                plugin = pluginMap.get(pluginName);
            }
        }
        if (plugin == null) {
            return;
        }
        try {
            switch (action) {
                case INIT:
                    plugin.onInit();
                    break;
                case START:
                    plugin.onStart();
                    break;
                case STOP:
                    plugin.onStop();
                    break;
            }
        } catch (Throwable ignored) {}
    }
}