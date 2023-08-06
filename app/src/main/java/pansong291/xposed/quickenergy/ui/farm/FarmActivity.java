package pansong291.xposed.quickenergy.ui.farm;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.core.common.JsonUtil;
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
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_farm);
            this.setTitle(R.string.farm_configuration);
            Config.shouldReload = true;
            FriendIdMap.shouldReload = true;
            CooperationIdMap.shouldReload = true;
            init();
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
        } catch (Exception ignored) {
        }
    }

    @Override
    public void init() throws JSONException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "menu.json");
        JSONArray basics = JsonUtil.fileJsonArray(is, "farm");
        initList(basics);
    }
}