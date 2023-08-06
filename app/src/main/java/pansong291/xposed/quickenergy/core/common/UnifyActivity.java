package pansong291.xposed.quickenergy.core.common;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.CooperationIdMap;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.ReserveIdMap;

/**
 * 统一Activity继承
 *
 * @author CLS
 * @since 2023/08/03
 */
public abstract class UnifyActivity extends Activity {

    /**
     * 存 ListView 文本
     */
    public final ArrayList<Integer> list = new ArrayList<>();

    /**
     * 存 Switch
     * 设置参照物的hashmap
     * 来保证SwitchButton不会出现错乱
     * map和list长度一致
     */
    public final Map<Integer, Object> selectedMap = new HashMap<>();

    /**
     * 初始 ListView 数据
     */
    public void initList() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "menu.json");
        JSONArray basics = JsonUtil.fileJsonArray(is, "basics");
        if (basics == null) return;
        for (int i = 0; i < basics.length(); i++) {
            try {
                JSONObject data = new JSONObject(basics.get(i).toString());
                Resources res = getBaseContext().getResources();
                String name = data.get("name").toString();
                int string = res.getIdentifier(name, "string", "pansong291.xposed.quickenergy.repair");
                list.add(string);
                /*这一块：待适配Config存储类再优化*/
                if (!data.isNull("switch")) {
                    switch (name) {
                        case "immediate_effect":
                            selectedMap.put(list.get(i), Config.immediateEffect());
                            break;
                        case "record_log":
                            selectedMap.put(list.get(i), Config.recordLog());
                            break;
                        case "show_toast":
                            selectedMap.put(list.get(i), Config.showToast());
                            break;
                        case "stay_awake":
                            selectedMap.put(list.get(i), Config.stayAwake());
                            break;
                        case "timeout_restart_activity":
                            selectedMap.put(list.get(i), Config.timeoutRestart());
                            break;
                    }
                }
                /*这一块：待适配Config存储类再优化*/
            } catch (Exception ignored) {
            }

        }
    }

    /**
     * 保存配置
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (Config.hasChanged) {
            Config.hasChanged = !Config.saveConfigFile();
            Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
        }
        FriendIdMap.saveIdMap();
        CooperationIdMap.saveIdMap();
        ReserveIdMap.saveIdMap();
    }
}
