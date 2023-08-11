package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AncientTreeRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.Log;

import java.util.List;

/**
 * @author Constanline
 * @since 2023/07/26
 */
public class AncientTree {
    private static final String TAG = AncientTree.class.getCanonicalName();

    private static boolean firstTime = true;

    public static void start() {
        if (!Config.ancientTree() || !firstTime || !Config.isAncientTreeWeek()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                home();
            }
        }.start();
    }
    private static void home() {
        List<String> list = Config.getAncientTreeAreaCodeList();
        if (list.isEmpty()) {
            return;
        }
        for (String code : list) {
            String s = AncientTreeRpcCall.homePage(code);
            try {
                JSONObject jo = new JSONObject(s);
                if (jo.getString("resultCode").equals("SUCCESS")) {
                    JSONObject data = jo.getJSONObject("data");
                    if (data.has("targetDistrictDetailInfo")) {
                        JSONObject targetDistrictDetailInfo = data.getJSONObject("targetDistrictDetailInfo");
                        JSONObject districtInfo = targetDistrictDetailInfo.getJSONObject("districtInfo");
                        String cityCode = districtInfo.getString("cityCode");
                        JSONArray ancientTreeList = targetDistrictDetailInfo.getJSONArray("ancientTreeList");
                        projectDetail(cityCode, ancientTreeList);
                    } else {
                        Log.recordLog("targetDistrictDetailInfo不存在", s);
                    }
                    firstTime = false;
                }
            } catch (Throwable t) {
                Log.i(TAG, "home err:");
                Log.printStackTrace(TAG, t);
            }
        }
    }

    private static void protect(JSONObject projectDetail) throws JSONException {
        JSONObject data = projectDetail.getJSONObject("data");
        if (data.getBoolean("canProtect") && !data.getBoolean("hasProtected")) {
            int currentEnergy = data.getInt("currentEnergy");
            JSONObject districtInfo = data.getJSONObject("districtInfo");
            String cityName = districtInfo.getString("cityName");
            String districtName = districtInfo.getString("districtName");
            JSONObject ancientTree = data.getJSONObject("ancientTree");
            String activityId = ancientTree.getString("activityId");
            String projectId = ancientTree.getString("projectId");
            JSONObject ancientTreeInfo = ancientTree.getJSONObject("ancientTreeInfo");
            String name = ancientTreeInfo.getString("name");
            int age = ancientTreeInfo.getInt("age");
            int protectExpense = ancientTreeInfo.getInt("protectExpense");
            String cityCode = ancientTreeInfo.getString("cityCode");
            if (currentEnergy > protectExpense) {
                String s = AncientTreeRpcCall.protect(activityId, projectId, cityCode);
                try {
                    JSONObject jo = new JSONObject(s);
                    if (jo.getString("resultCode").equals("SUCCESS")) {
                        Log.forest("保护古树🎐[" + cityName + "-" + districtName
                                + "]#" + age + "年" + name + ",消耗能量" + protectExpense + "g");
                    }
                } catch (Throwable t) {
                    Log.i(TAG, "protect err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }

    }

    private static void projectDetail(String cityCode, JSONArray ancientTreeList) throws JSONException {
        for (int i = 0; i < ancientTreeList.length(); i++) {
            JSONObject ancientTree = ancientTreeList.getJSONObject(i);
            if (!ancientTree.getBoolean("hasProtected")) {
                String ancientTreeProjectId = ancientTree.getString("projectId");
                String s = AncientTreeRpcCall.projectDetail(ancientTreeProjectId, cityCode);
                try {
                    JSONObject jo = new JSONObject(s);
                    if (jo.getString("resultCode").equals("SUCCESS")) {
                        protect(jo);
                    }
                } catch (Throwable t) {
                    Log.i(TAG, "projectDetail err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }
    }
}
