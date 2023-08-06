package pansong291.xposed.quickenergy.ui.forest;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.core.activity_tool.AlipayBeach;
import pansong291.xposed.quickenergy.core.activity_tool.AlipayReserve;
import pansong291.xposed.quickenergy.core.activity_tool.AlipayUser;
import pansong291.xposed.quickenergy.core.activity_tool.AreaCode;
import pansong291.xposed.quickenergy.core.activity_tool.CooperateUser;
import pansong291.xposed.quickenergy.core.activity_tool.EditDialog;
import pansong291.xposed.quickenergy.core.activity_tool.ListDialog;
import pansong291.xposed.quickenergy.core.common.JsonUtil;
import pansong291.xposed.quickenergy.core.common.TagUtil;
import pansong291.xposed.quickenergy.core.common.UnifyActivity;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.CooperationIdMap;
import pansong291.xposed.quickenergy.util.FriendIdMap;

/**
 * 森林设置
 *
 * @author CLS
 * @since 2023/08/01
 */
public class ForestActivity extends UnifyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forest);
            this.setTitle(R.string.forest_configuration);
            Config.shouldReload = true;
            FriendIdMap.shouldReload = true;
            CooperationIdMap.shouldReload = true;

            init();
            ListView forestLv = findViewById(R.id.forestLv);
            ForestAdapter adapter = new ForestAdapter(this, list, selectedMap);
            forestLv.setAdapter(adapter);
            forestLv.setOnItemClickListener((adapterView, view, i, l) -> {
                //按钮业务
                String string = this.getString(list.get(i));
                switch (i) {
                    case 1:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.CHECK_INTERVAL, null);
                        break;
                    case 3:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.ADVANCE_TIME, null);
                        break;
                    case 4:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.COLLECT_INTERVAL, null);
                        break;
                    case 5:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.COLLECT_TIMEOUT, null);
                        break;
                    case 7:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.LIMIT_COUNT, null);
                        break;
                    case 9:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.DOUBLE_CARD_TIME, this.getString(R.string.use_double_card_time));
                        break;
                    case 10:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.RETURN_WATER_10, null);
                        break;
                    case 11:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.RETURN_WATER_20, null);
                        break;
                    case 12:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.RETURN_WATER_30, null);
                        break;
                    case 14:
                        ListDialog.show(this, string, AlipayUser.getList(), Config.getDontCollectList(), null);
                        break;
                    case 15:
                        ListDialog.show(this, string, AlipayUser.getList(), Config.getDontHelpCollectList(), null);
                        break;
                    case 17:
                        ListDialog.show(this, string, AlipayUser.getList(), Config.getWaterFriendList(), Config.getWaterCountList());
                        break;
                    case 18:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.WATER_FRIEND_COUNT, null);
                        break;
                    case 20:
                        ListDialog.show(this, string, CooperateUser.getList(), Config.getCooperateWaterList(), Config.getcooperateWaterNumList());
                        break;
                    case 22:
                        ListDialog.show(this, string, AlipayUser.getList(), Config.getGiveEnergyRainList(), null);
                        break;
                    case 24:
                        EditDialog.showEditDialog(this, string, EditDialog.EditMode.EXCHANGE_ENERGY_DOUBLE_CLICK_COUNT, null);
                        break;
                    case 26:
                        ListDialog.show(this, string, AlipayReserve.getList(), Config.getReserveList(), Config.getReserveCountList());
                        break;
                    case 28:
                        ListDialog.show(this, string, AreaCode.getList(), Config.getAncientTreeAreaCodeList(), null);
                        break;
                    case 33:
                        ListDialog.show(this, string, AlipayBeach.getList(), Config.getBeachList(), Config.getBeachCountList());
                        break;
                }
            });
            adapter.OnSetOnClickDialogListener((position, boolClick) -> {
                //Switch业务
                selectedMap.put(list.get(position), boolClick);
                switch (position) {
                    case 0:
                        Config.setCollectEnergy(boolClick);
                        break;
                    case 2:
                        Config.setCollectWateringBubble(boolClick);
                        break;
                    case 6:
                        Config.setLimitCollect(boolClick);
                        break;
                    case 8:
                        Config.setDoubleCard(boolClick);
                        break;
                    case 13:
                        Config.setHelpFriendCollect(boolClick);
                        break;
                    case 16:
                        Config.setReceiveForestTaskAward(boolClick);
                        break;
                    case 19:
                        Config.setCooperateWater(boolClick);
                        break;
                    case 21:
                        Config.setEnergyRain(boolClick);
                        break;
                    case 23:
                        Config.setExchangeEnergyDoubleClick(boolClick);
                        break;
                    case 25:
                        Config.setReserve(boolClick);
                        break;
                    case 27:
                        Config.setAncientTree(boolClick);
                        break;
                    case 29:
                        Config.setAncientTreeOnlyWeek(boolClick);
                        break;
                    case 30:
                        Config.setAntdodoCollect(boolClick);
                        break;
                    case 31:
                        Config.setAntOcean(boolClick);
                        break;
                    case 32:
                        Config.setBeach(boolClick);
                        break;
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void init() throws JSONException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "menu.json");
        JSONArray basics = JsonUtil.fileJsonArray(is, "forest");
        initList(basics);
    }
}