package pansong291.xposed.quickenergy.ui.forest;

import android.os.Bundle;
import android.widget.ListView;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.core.activity_tool.AlipayBeach;
import pansong291.xposed.quickenergy.core.activity_tool.AlipayReserve;
import pansong291.xposed.quickenergy.core.activity_tool.AlipayUser;
import pansong291.xposed.quickenergy.core.activity_tool.AreaCode;
import pansong291.xposed.quickenergy.core.activity_tool.CooperateUser;
import pansong291.xposed.quickenergy.core.activity_tool.EditDialog;
import pansong291.xposed.quickenergy.core.activity_tool.ListDialog;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forest);
        this.setTitle(R.string.forest_configuration);
        Config.shouldReload = true;
        FriendIdMap.shouldReload = true;
        CooperationIdMap.shouldReload = true;

        initList();
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
    }

    @Override
    public void initList() {
        /*待优化*/
        list.add(R.string.collect_energy);
        selectedMap.put(list.get(0), Config.collectEnergy());
        list.add(R.string.check_interval_minutes);
        selectedMap.put(list.get(1), TagUtil.SETTING);
        list.add(R.string.collect_watering_bubble);
        selectedMap.put(list.get(2), Config.collectWateringBubble());
        list.add(R.string.advance_time_milliseconds);
        selectedMap.put(list.get(3), TagUtil.SETTING);
        list.add(R.string.collect_interval_milliseconds);
        selectedMap.put(list.get(4), TagUtil.SETTING);
        list.add(R.string.collect_timeout_seconds);
        selectedMap.put(list.get(5), TagUtil.SETTING);
        list.add(R.string.limit_collect);
        selectedMap.put(list.get(6), Config.isLimitCollect());
        list.add(R.string.limit_count_per_minute);
        selectedMap.put(list.get(7), TagUtil.SETTING);
        list.add(R.string.use_double_card);
        selectedMap.put(list.get(8), Config.doubleCard());
        list.add(R.string.use_double_click_card_time);
        selectedMap.put(list.get(9), TagUtil.SETTING);
        list.add(R.string.return_water_10);
        selectedMap.put(list.get(10), TagUtil.SETTING);
        list.add(R.string.return_water_18);
        selectedMap.put(list.get(11), TagUtil.SETTING);
        list.add(R.string.return_water_33);
        selectedMap.put(list.get(12), TagUtil.SETTING);
        list.add(R.string.help_friend_collect);
        selectedMap.put(list.get(13), Config.helpFriendCollect());
        list.add(R.string.don_t_collect_list);
        selectedMap.put(list.get(14), TagUtil.SETTING);
        list.add(R.string.don_t_help_collect_list);
        selectedMap.put(list.get(15), TagUtil.SETTING);
        list.add(R.string.receive_forest_task_award);
        selectedMap.put(list.get(16), Config.receiveForestTaskAward());
        list.add(R.string.water_friend_list);
        selectedMap.put(list.get(17), TagUtil.SETTING);
        list.add(R.string.water_friend_count);
        selectedMap.put(list.get(18), TagUtil.SETTING);
        list.add(R.string.cooperate_water);
        selectedMap.put(list.get(19), Config.cooperateWater());
        list.add(R.string.cooperate_water_list);
        selectedMap.put(list.get(20), TagUtil.SETTING);
        list.add(R.string.energy_rain);
        selectedMap.put(list.get(21), Config.energyRain());
        list.add(R.string.give_energy_rain_list);
        selectedMap.put(list.get(22), TagUtil.SETTING);
        list.add(R.string.exchange_energy_double_click);
        selectedMap.put(list.get(23), Config.ExchangeEnergyDoubleClick());
        list.add(R.string.exchange_energy_double_click_count);
        selectedMap.put(list.get(24), TagUtil.SETTING);
        list.add(R.string.reserve);
        selectedMap.put(list.get(25), Config.reserve());
        list.add(R.string.reserve_list);
        selectedMap.put(list.get(26), TagUtil.SETTING);
        list.add(R.string.ancient_tree);
        selectedMap.put(list.get(27), Config.ancientTree());
        list.add(R.string.ancient_tree_area_code_list);
        selectedMap.put(list.get(28), TagUtil.SETTING);
        list.add(R.string.ancient_tree_only_week);
        selectedMap.put(list.get(29), Config.ancientTreeOnlyWeek());
        list.add(R.string.antdodo_collect);
        selectedMap.put(list.get(30), Config.antdodoCollect());
        list.add(R.string.ant_ocean);
        selectedMap.put(list.get(31), Config.antOcean());
        list.add(R.string.beach);
        selectedMap.put(list.get(32), Config.beach());
        list.add(R.string.beach_list);
        selectedMap.put(list.get(33), TagUtil.SETTING);
    }
}