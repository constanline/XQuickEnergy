package pansong291.xposed.quickenergy.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.entity.FriendWatch;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FileUtils;
import pansong291.xposed.quickenergy.util.PermissionUtil;
import pansong291.xposed.quickenergy.util.Statistics;
import pansong291.xposed.quickenergy.util.RandomUtils;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private static String[] strArray;
    TextView tvStatistics;
    Button btnHelp;
    TextView tv_version;

    public static String version = "";

    private static boolean isExpModuleActive(Context context) {
        boolean isExp = false;
        if (context == null)
            throw new IllegalArgumentException("context must not be null!!");

        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.parse("content://me.weishu.exposed.CP/");
            Bundle result = null;
            try {
                result = contentResolver.call(uri, "active", null, null);
            } catch (RuntimeException e) {
                // TaiChi is killed, try invoke
                try {
                    Intent intent = new Intent("me.weishu.exp.ACTION_ACTIVE");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Throwable e1) {
                    return false;
                }
            }
            if (result == null)
                result = contentResolver.call(uri, "active", null, null);

            if (result == null)
                return false;
            isExp = result.getBoolean("active", false);
        } catch (Throwable ignored) {
        }
        return isExp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setModuleActive(isExpModuleActive(this));
        PermissionUtil.requestPermissions(this);

        tvStatistics = findViewById(R.id.tv_statistics);
        btnHelp = findViewById(R.id.btn_help);
        tv_version = findViewById(R.id.tv_version);
        if (strArray == null)
            strArray = getResources().getStringArray(R.array.sentences);
        if (strArray != null)
            btnHelp.setText(strArray[RandomUtils.nextInt(0, strArray.length)]);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
            tv_version.setText("v" + version);
        }catch (PackageManager.NameNotFoundException ignored) {
        }
        this.setTitle(this.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvStatistics.setText(Statistics.getText());
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        String data = "file://";
        switch (v.getId()) {
            case R.id.btn_forest_log:
                data += FileUtils.getForestLogFile().getAbsolutePath();
                break;

            case R.id.btn_farm_log:
                data += FileUtils.getFarmLogFile().getAbsolutePath();
                break;

            case R.id.btn_other_log:
                data += FileUtils.getOtherLogFile().getAbsolutePath();
                break;

            case R.id.btn_help:
                data = "https://github.com/pansong291/XQuickEnergy/wiki";
                break;

            case R.id.btn_github:
                data = "https://github.com/constanline/XQuickEnergy";
                break;

            case R.id.btn_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return;

            case R.id.btn_friend_watch:
                ListDialog.show(this, getString(R.string.friend_watch), FriendWatch.getList(), new ArrayList<>(), null);
                return;

        }

        Intent it = new Intent(this, HtmlViewerActivity.class);
        it.setData(Uri.parse(data));
        startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int state = getPackageManager()
                .getComponentEnabledSetting(new ComponentName(this, getClass().getCanonicalName() + "Alias"));
        menu.add(0, 1, 0, R.string.hide_the_application_icon)
                .setCheckable(true)
                .setChecked(state > PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        menu.add(0, 2, 0, R.string.export_the_statistic_file);
        menu.add(0, 3, 0, R.string.import_the_statistic_file);
        menu.add(0, 4, 0, R.string.settings);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                int state = item.isChecked() ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                getPackageManager()
                        .setComponentEnabledSetting(new ComponentName(this, getClass().getCanonicalName() + "Alias"),
                                state, PackageManager.DONT_KILL_APP);
                item.setChecked(!item.isChecked());
                break;

            case 2:
                if (FileUtils.copyTo(FileUtils.getStatisticsFile(), FileUtils.getExportedStatisticsFile()))
                    Toast.makeText(this, "导出成功！", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                if (FileUtils.copyTo(FileUtils.getExportedStatisticsFile(), FileUtils.getStatisticsFile())) {
                    tvStatistics.setText(Statistics.getText());
                    Toast.makeText(this, "导入成功！", Toast.LENGTH_SHORT).show();
                }
                break;

            case 4:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setModuleActive(boolean b) {
        TextView tvUnActive = findViewById(R.id.tv_unactive);
        tvUnActive.setVisibility(b ? View.GONE : View.VISIBLE);
    }

}
