package pansong291.xposed.quickenergy.ui.basics;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.core.common.JsonUtil;
import pansong291.xposed.quickenergy.core.common.TagUtil;
import pansong291.xposed.quickenergy.core.common.UnifyActivity;
import pansong291.xposed.quickenergy.core.activity_tool.ChoiceDialog;
import pansong291.xposed.quickenergy.core.activity_tool.EditDialog;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.CooperationIdMap;
import pansong291.xposed.quickenergy.util.FriendIdMap;

/**
 * 基础设置
 *
 * @author CLS
 * @since 2023/08/01
 */
public class BasicsActivity extends UnifyActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_basics);
            this.setTitle(R.string.base_configuration);
            Config.shouldReload = true;
            FriendIdMap.shouldReload = true;
            CooperationIdMap.shouldReload = true;
            init();
            ListView lv = findViewById(R.id.basicsLv);
            BasicsAdapter adapter = new BasicsAdapter(this, list, selectedMap);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener((adapterView, view, i, l) -> {
                //按钮业务
                String string = this.getString(list.get(i));
                if (i == 4) {
                    ChoiceDialog.showStayAwakeType(this, string);
                } else if (i == 5) {
                    ChoiceDialog.showStayAwakeTarget(this, string);
                } else if (i == 7) {
                    EditDialog.showEditDialog(this, string, EditDialog.EditMode.WAIT_WHEN_EXCEPTION, null);
                }

            });
            adapter.OnSetOnClickDialogListener((position, boolClick) -> {
                selectedMap.put(list.get(position), boolClick);
                switch (position) {
                    case 0:
                        Config.setImmediateEffect(boolClick);
                        break;
                    case 1:
                        Config.setRecordLog(boolClick);
                        break;
                    case 2:
                        Config.setShowToast(boolClick);
                        break;
                    case 3:
                        Config.setStayAwake(boolClick);
                        break;
                    case 6:
                        Config.setTimeoutRestart(boolClick);
                        break;
                }
            });
        } catch (Exception ignored) {
        }

    }

    @Override
    public void init() throws JSONException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "menu.json");
        JSONArray basics = JsonUtil.fileJsonArray(is, "basics");
        initList(basics);
    }
}

