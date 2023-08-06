package pansong291.xposed.quickenergy.ui.other;

import android.os.Bundle;
import android.widget.ListView;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.core.activity_tool.EditDialog;
import pansong291.xposed.quickenergy.core.common.TagUtil;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        this.setTitle(R.string.other_settings);
        Config.shouldReload = true;
        FriendIdMap.shouldReload = true;
        CooperationIdMap.shouldReload = true;
        initList();

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
    }

    @Override
    public void initList() {
        /*待优化：*/
        list.add(R.string.receive_point);
        selectedMap.put(list.get(0), Config.receivePoint());
        list.add(R.string.open_treasure_box);
        selectedMap.put(list.get(1), Config.openTreasureBox());
        list.add(R.string.receive_coin_asset);
        selectedMap.put(list.get(2), Config.receiveCoinAsset());
        list.add(R.string.donate_charity_coin);
        selectedMap.put(list.get(3), Config.donateCharityCoin());
        list.add(R.string.min_exchange_count);
        selectedMap.put(list.get(4), TagUtil.SETTING);
        list.add(R.string.latest_exchange_time_24_hour_system);
        selectedMap.put(list.get(5), TagUtil.SETTING);
        list.add(R.string.sync_step_count);
        selectedMap.put(list.get(6), TagUtil.SETTING);
        list.add(R.string.koubei_sign_in);
        selectedMap.put(list.get(7), Config.kbSginIn());
        list.add(R.string.eco_life_tick);
        selectedMap.put(list.get(8), Config.ecoLifeTick());
    }
}