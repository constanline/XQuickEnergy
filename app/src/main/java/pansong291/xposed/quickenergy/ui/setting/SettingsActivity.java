package pansong291.xposed.quickenergy.ui.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.core.common.UnifyActivity;
import pansong291.xposed.quickenergy.core.common.TagUtil;
import pansong291.xposed.quickenergy.ui.basics.BasicsActivity;
import pansong291.xposed.quickenergy.ui.farm.FarmActivity;
import pansong291.xposed.quickenergy.ui.forest.ForestActivity;
import pansong291.xposed.quickenergy.ui.other.OtherActivity;
import pansong291.xposed.quickenergy.core.activity_tool.HtmlViewerActivity;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.CooperationIdMap;
import pansong291.xposed.quickenergy.util.FriendIdMap;

public class SettingsActivity extends UnifyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setTitle(R.string.settings);
        Config.shouldReload = true;
        FriendIdMap.shouldReload = true;
        CooperationIdMap.shouldReload = true;

        initList();
        ListView lv = findViewById(R.id.list_view);
        SettingAdapter adapter = new SettingAdapter(this, list, selectedMap);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            //按钮业务
            switch (i) {
                case 0:
                    startActivity(new Intent(this, BasicsActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(this, ForestActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(this, FarmActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(this, OtherActivity.class));
                    break;
                case 4:
                    try {
                        Intent it2 = new Intent(Intent.ACTION_VIEW, Uri.parse("alipays://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Ftsx00339eflkuhhtfctcn48"));
                        startActivity(it2);
                    } catch (Exception ignored) {
                        Toast.makeText(this, "手机无支付宝", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    new AlertDialog.Builder(this).setView(R.layout.donation_view).setPositiveButton("关闭", null).create().show();
                    break;
                case 6:
                    String data = "https://github.com/constanline/XQuickEnergy";
                    Intent it = new Intent(this, HtmlViewerActivity.class);
                    it.setData(Uri.parse(data));
                    startActivity(it);
                    break;
                case 7:
                    String hint = "1.本APP是为了学习研究开发，免费提供，不得进行任何形式的转发、发布、传播。\n" + "2.请于24小时内卸载本APP。\n" + "3.如果您是购买的可能已经被骗，请联系卖家退款。";
                    Toast.makeText(this, hint, Toast.LENGTH_LONG).show();
                    break;
            }
        });

    }

    @Override
    public void initList() {
        /*待优化：*/
        list.add(R.string.base_configuration);
        selectedMap.put(list.get(0), TagUtil.SETTING);
        list.add(R.string.forest_configuration);
        selectedMap.put(list.get(1), TagUtil.SETTING);
        list.add(R.string.farm_configuration);
        selectedMap.put(list.get(2), TagUtil.SETTING);
        list.add(R.string.other_settings);
        selectedMap.put(list.get(3), TagUtil.SETTING);
        list.add(R.string.support_xqe_developer);
        selectedMap.put(list.get(4), TagUtil.SETTING);
        list.add(R.string.support_developer);
        selectedMap.put(list.get(5), TagUtil.SETTING);
        list.add(R.string.access_git_hub);
        selectedMap.put(list.get(6), TagUtil.URL);
        list.add(R.string.about_sesame_seeds);
        selectedMap.put(list.get(7), TagUtil.CONCERNING);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}

