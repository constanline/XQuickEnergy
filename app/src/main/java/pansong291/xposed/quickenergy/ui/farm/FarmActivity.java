package pansong291.xposed.quickenergy.ui.farm;

import android.os.Bundle;
import android.widget.ListView;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.core.common.TagUtil;
import pansong291.xposed.quickenergy.core.common.UnifyActivity;
import pansong291.xposed.quickenergy.core.activity_tool.AlipayUser;
import pansong291.xposed.quickenergy.core.activity_tool.ChoiceDialog;
import pansong291.xposed.quickenergy.core.activity_tool.EditDialog;
import pansong291.xposed.quickenergy.core.activity_tool.ListDialog;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.CooperationIdMap;
import pansong291.xposed.quickenergy.util.FriendIdMap;

/**
 * 农场设置
 *
 * @author CLS
 * @since 2023/08/01
 */
public class FarmActivity extends UnifyActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        this.setTitle(R.string.farm_configuration);
        Config.shouldReload = true;
        FriendIdMap.shouldReload = true;
        CooperationIdMap.shouldReload = true;
        initList();
        ListView lv = findViewById(R.id.farmLv);
        FarmAdapter adapter = new FarmAdapter(this, list, selectedMap);
        lv.setAdapter(adapter);
        /*待优化方向：改造存储方式*/
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            String string = this.getString(list.get(i));
            switch (i) {
                case 3:
                    ChoiceDialog.showSendType(this, string);
                    break;
                case 4:
                    ListDialog.show(this, string, AlipayUser.getList(), Config.getDontSendFriendList(), null);
                    break;
                case 5:
                    ChoiceDialog.showRecallAnimalType(this, string);
                    break;
                case 14:
                    ListDialog.show(this, string, AlipayUser.getList(), Config.getFeedFriendAnimalList(), Config.getFeedFriendCountList());
                    break;
                case 15:
                    EditDialog.showEditDialog(this, string, EditDialog.EditMode.ANIMAL_SLEEP_TIME, null);
                    break;
                case 17:
                    ListDialog.show(this, string, AlipayUser.getList(), Config.getDontNotifyFriendList(), null);
                    break;

            }
        });
        adapter.OnSetOnClickDialogListener((position, boolClick) -> {
            selectedMap.put(list.get(position), boolClick);
            switch (position) {
                case 0:
                    Config.setEnableFarm(boolClick);
                    break;
                case 1:
                    Config.setRewardFriend(boolClick);
                    break;
                case 2:
                    Config.setSendBackAnimal(boolClick);
                    break;
                case 6:
                    Config.setReceiveFarmToolReward(boolClick);
                    break;
                case 7:
                    Config.setUseNewEggTool(boolClick);
                    break;
                case 8:
                    Config.setHarvestProduce(boolClick);
                    break;
                case 9:
                    Config.setDonation(boolClick);
                    break;
                case 10:
                    Config.setAnswerQuestion(boolClick);
                    break;
                case 11:
                    Config.setReceiveFarmTaskAward(boolClick);
                    break;
                case 12:
                    Config.setFeedAnimal(boolClick);
                    break;
                case 13:
                    Config.setUseAccelerateTool(boolClick);
                    break;
                case 16:
                    Config.setNotifyFriend(boolClick);
                    break;
            }
        });

    }

    @Override
    public void initList() {
        /*待优化：*/
        list.add(R.string.enable_farm);
        selectedMap.put(list.get(0), Config.enableFarm());
        list.add(R.string.reward_friend);
        selectedMap.put(list.get(1), Config.rewardFriend());
        list.add(R.string.send_back_animal);
        selectedMap.put(list.get(2), Config.sendBackAnimal());
        list.add(R.string.send_type);
        selectedMap.put(list.get(3), TagUtil.SETTING);
        list.add(R.string.don_t_send_friend_list);
        selectedMap.put(list.get(4), TagUtil.SETTING);
        list.add(R.string.recall_animal_type);
        selectedMap.put(list.get(5), TagUtil.SETTING);
        list.add(R.string.receive_farm_tool_reward);
        selectedMap.put(list.get(6), Config.receiveFarmToolReward());
        list.add(R.string.use_new_egg_tool);
        selectedMap.put(list.get(7), Config.useNewEggTool());
        list.add(R.string.harvest_produce);
        selectedMap.put(list.get(8), Config.harvestProduce());
        list.add(R.string.donation);
        selectedMap.put(list.get(9), Config.donation());
        list.add(R.string.answer_question);
        selectedMap.put(list.get(10), Config.answerQuestion());
        list.add(R.string.receive_farm_task_award);
        selectedMap.put(list.get(11), Config.receiveFarmTaskAward());
        list.add(R.string.feed_animal);
        selectedMap.put(list.get(12), Config.feedAnimal());
        list.add(R.string.use_accelerate_tool);
        selectedMap.put(list.get(13), Config.useAccelerateTool());
        list.add(R.string.feed_friend_animal_list);
        selectedMap.put(list.get(14), TagUtil.SETTING);
        list.add(R.string.animal_sleep_time);
        selectedMap.put(list.get(15), TagUtil.SETTING);
        list.add(R.string.notify_friend);
        selectedMap.put(list.get(16), Config.notifyFriend());
        list.add(R.string.don_t_notify_friend_list);
        selectedMap.put(list.get(17), TagUtil.SETTING);
    }
}