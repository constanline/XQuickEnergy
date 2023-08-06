package pansong291.xposed.quickenergy.ui.other;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.core.activity_tool.EditDialog;
import pansong291.xposed.quickenergy.core.common.JsonUtil;
import pansong291.xposed.quickenergy.core.common.UnifyActivity;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.CooperationIdMap;
import pansong291.xposed.quickenergy.util.FriendIdMap;

/**
 * 其它配置
 *
 * @author CLS
 * @since 2023/08/01
 */
public class OtherActivity extends UnifyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_other);
            this.setTitle(R.string.other_settings);
            Config.shouldReload = true;
            FriendIdMap.shouldReload = true;
            CooperationIdMap.shouldReload = true;
            init();

            ListView lv = findViewById(R.id.otherLv);
            OtherAdapter adapter = new OtherAdapter(this, list, selectedMap);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener((adapterView, view, i, l) -> {
                //按钮业务
                String string = this.getString(list.get(i));
                switch (i) {
                    case 4:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.MIN_EXCHANGE_COUNT, null);
                        break;
                    case 5:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.LATEST_EXCHANGE_TIME, null);
                        break;
                    case 6:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.SYNC_STEP_COUNT, null);
                        break;
                }
            });
            adapter.OnSetOnClickDialogListener((position, boolClick) -> {
                selectedMap.put(list.get(position), boolClick);
                switch (position) {
                    case 0:
                        Config.setReceivePoint(boolClick);
                        break;
                    case 1:
                        Config.setOpenTreasureBox(boolClick);
                        break;
                    case 2:
                        Config.setReceiveCoinAsset(boolClick);
                        break;
                    case 3:
                        Config.setDonateCharityCoin(boolClick);
                        break;
                    case 7:
                        Config.setKbSginIn(boolClick);
                        break;
                    case 8:
                        Config.setEcoLifeTick(boolClick);
                        break;
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void init() throws JSONException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "menu.json");
        JSONArray basics = JsonUtil.fileJsonArray(is, "other");
        initList(basics);
    }
}