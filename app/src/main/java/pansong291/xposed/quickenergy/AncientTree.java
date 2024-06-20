package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AncientTreeRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;

import java.util.List;

public class AncientTree {
    private static final String TAG = AncientTree.class.getCanonicalName();

    public static void start() {
        if (!Config.ancientTree() || !Config.isAncientTreeWeek())
            return;
        Log.recordLog("开始检测古树保护", "");
        new Thread() {

            @Override
            public void run() {
                try {
                    FriendIdMap.waitingCurrentUid();
                    ancientTree(Config.getAncientTreeCityCodeList()); // 二次检查 有时会返回繁忙漏保护
                } catch (Throwable t) {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }.start();
    }

    private static void ancientTree(List<String> ancientTreeCityCodeList) {
        try {
            for (String cityCode : ancientTreeCityCodeList) {
                if (!Statistics.canAncientTreeToday(cityCode))
                    continue;
                ancientTreeProtect(cityCode);
                Thread.sleep(500L);
            }
        } catch (Throwable th) {
            Log.i(TAG, "ancientTree err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void ancientTreeProtect(String cityCode) {
        try {
            JSONObject jo = new JSONObject(AncientTreeRpcCall.homePage(cityCode));
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONObject data = jo.getJSONObject("data");
                if (!data.has("districtBriefInfoList")) {
                    return;
                }
                JSONArray districtBriefInfoList = data.getJSONArray("districtBriefInfoList");
                for (int i = 0; i < districtBriefInfoList.length(); i++) {
                    JSONObject districtBriefInfo = districtBriefInfoList.getJSONObject(i);
                    int userCanProtectTreeNum = districtBriefInfo.optInt("userCanProtectTreeNum", 0);
                    if (userCanProtectTreeNum < 1)
                        continue;
                    JSONObject districtInfo = districtBriefInfo.getJSONObject("districtInfo");
                    String districtCode = districtInfo.getString("districtCode");
                    districtDetail(districtCode);
                    Thread.sleep(1000L);
                }
                Statistics.ancientTreeToday(cityCode);
            }
        } catch (Throwable th) {
            Log.i(TAG, "ancientTreeProtect err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void districtDetail(String districtCode) {
        try {
            JSONObject jo = new JSONObject(AncientTreeRpcCall.districtDetail(districtCode));
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONObject data = jo.getJSONObject("data");
                if (!data.has("ancientTreeList")) {
                    return;
                }
                JSONObject districtInfo = data.getJSONObject("districtInfo");
                String cityCode = districtInfo.getString("cityCode");
                String cityName = districtInfo.getString("cityName");
                String districtName = districtInfo.getString("districtName");
                JSONArray ancientTreeList = data.getJSONArray("ancientTreeList");
                for (int i = 0; i < ancientTreeList.length(); i++) {
                    JSONObject ancientTreeItem = ancientTreeList.getJSONObject(i);
                    if (ancientTreeItem.getBoolean("hasProtected"))
                        continue;
                    JSONObject ancientTreeControlInfo = ancientTreeItem.getJSONObject("ancientTreeControlInfo");
                    int quota = ancientTreeControlInfo.optInt("quota", 0);
                    int useQuota = ancientTreeControlInfo.optInt("useQuota", 0);
                    if (quota <= useQuota)
                        continue;
                    String itemId = ancientTreeItem.getString("projectId");
                    JSONObject ancientTreeDetail = new JSONObject(AncientTreeRpcCall.projectDetail(itemId, cityCode));
                    if ("SUCCESS".equals(ancientTreeDetail.getString("resultCode"))) {
                        data = ancientTreeDetail.getJSONObject("data");
                        if (data.getBoolean("canProtect")) {
                            int currentEnergy = data.getInt("currentEnergy");
                            JSONObject ancientTree = data.getJSONObject("ancientTree");
                            String activityId = ancientTree.getString("activityId");
                            String projectId = ancientTree.getString("projectId");
                            JSONObject ancientTreeInfo = ancientTree.getJSONObject("ancientTreeInfo");
                            String name = ancientTreeInfo.getString("name");
                            int age = ancientTreeInfo.getInt("age");
                            int protectExpense = ancientTreeInfo.getInt("protectExpense");
                            cityCode = ancientTreeInfo.getString("cityCode");
                            if (currentEnergy < protectExpense)
                                break;
                            Thread.sleep(200);
                            jo = new JSONObject(AncientTreeRpcCall.protect(activityId, projectId, cityCode));
                            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                                Log.forest("保护古树🎐[" + cityName + "-" + districtName
                                        + "]#" + age + "年" + name + ",消耗能量" + protectExpense + "g");
                            } else {
                                Log.recordLog(jo.getString("resultDesc"), jo.toString());
                            }
                        }
                    } else {
                        Log.recordLog(jo.getString("resultDesc"), ancientTreeDetail.toString());
                    }
                    Thread.sleep(500L);
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "districtDetail err:");
            Log.printStackTrace(TAG, th);
        }
    }
}
