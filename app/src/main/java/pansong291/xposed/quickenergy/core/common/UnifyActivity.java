package pansong291.xposed.quickenergy.core.common;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import pansong291.xposed.quickenergy.ui.basics.BasicsActivity;
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
     *
     * @param menu 菜单
     */
    public void initList(JSONArray menu) {
        try {
            if (menu == null) throw new NullPointerException();
            for (int i = 0; i < menu.length(); i++) {
                JSONObject data = new JSONObject(menu.get(i).toString());

                //动态获取资源id
                Resources res = getBaseContext().getResources();
                int string = res.getIdentifier(data.get("titleName").toString(), "string", "pansong291.xposed.quickenergy.repair");
                list.add(string);
                if (!data.isNull("api")) {
                    JSONObject config = JsonUtil.getConfig();
                    if (config == null) throw new NullPointerException();
                    String api = data.get("api").toString();
                    if (!config.isNull(api)) {
                        Object o = config.get(api);
                        selectedMap.put(list.get(i), o);
                    } else {
                        /*
                         * 特殊的列表：Config文件中没有该参数
                         * 理论上配置文件不应该没有的
                         * 待处理
                         */
                        switch (api) {
                            case "kbSginIn":
                                selectedMap.put(list.get(i), Config.kbSginIn());
                                break;
                            case "isLimitCollect":
                                selectedMap.put(list.get(i), Config.isLimitCollect());
                                break;
                            case "ExchangeEnergyDoubleClick":
                                selectedMap.put(list.get(i), Config.ExchangeEnergyDoubleClick());
                                break;
                            default:
                                throw new RuntimeException();
                        }

                    }

                }
                if (!data.isNull("tag")) {
                    selectedMap.put(list.get(i), data.get("tag"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于初始视图列表
     */
    public abstract void init() throws JSONException;

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
