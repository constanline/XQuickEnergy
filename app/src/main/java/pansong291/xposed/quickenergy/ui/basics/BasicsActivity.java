package pansong291.xposed.quickenergy.ui.basics;

import android.os.Bundle;
import android.widget.ListView;

import pansong291.xposed.quickenergy.R;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basics);
        this.setTitle(R.string.base_configuration);
        Config.shouldReload = true;
        FriendIdMap.shouldReload = true;
        CooperationIdMap.shouldReload = true;
        initList();

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
    }

    @Override
    public void initList() {
        list.add(R.string.immediate_effect);
        selectedMap.put(list.get(0), Config.immediateEffect());
        list.add(R.string.record_log);
        selectedMap.put(list.get(1), Config.recordLog());
        list.add(R.string.show_toast);
        selectedMap.put(list.get(2), Config.showToast());
        list.add(R.string.stay_awake);
        selectedMap.put(list.get(3), Config.stayAwake());
        list.add(R.string.stay_awake_type);
        selectedMap.put(list.get(4), TagUtil.SETTING);
        list.add(R.string.stay_awake_target);
        selectedMap.put(list.get(5), TagUtil.SETTING);
        list.add(R.string.timeout_restart_activity);
        selectedMap.put(list.get(6), Config.timeoutRestart());
        list.add(R.string.wait_when_exception);
        selectedMap.put(list.get(7), TagUtil.SETTING);
    }


}

