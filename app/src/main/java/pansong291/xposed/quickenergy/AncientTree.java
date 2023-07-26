package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.RpcUtil;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.StringUtil;

/**
 * @author Constanline
 * @since 2023/07/26
 */
public class AncientTree {

    private static boolean firstTime = true;

    public static void start() {
        if (!firstTime) {
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
        if (StringUtil.isEmpty(Config.ancientTreeAreaCode()) || Config.ancientTreeAreaCode().length() != 6) {
            return;
        }
        RpcUtil.requestRpc("alipay.greenmatrix.rpc.h5.ancienttree.homePage",
                "[{\"cityCode\":\"" + Config.ancientTreeAreaCode() + "\",\"source\":\"antforesthome\"}]",
                (resData, respArgs) -> {
                    JSONObject data = resData.getJSONObject("data");
                    JSONObject targetDistrictDetailInfo = data.getJSONObject("targetDistrictDetailInfo");
                    JSONObject districtInfo = targetDistrictDetailInfo.getJSONObject("districtInfo");
                    String cityCode = districtInfo.getString("cityCode");
                    JSONArray ancientTreeList = targetDistrictDetailInfo.getJSONArray("ancientTreeList");
                    projectDetail(cityCode, ancientTreeList);
                    firstTime = false;
                });
    }

    private static void protect(JSONObject projectDetail) throws JSONException {
        JSONObject data = projectDetail.getJSONObject("data");
        if (data.getBoolean("canProtect")) {
            int currentEnergy = data.getInt("currentEnergy");
            JSONObject ancientTree = data.getJSONObject("ancientTree");
            String activityId = ancientTree.getString("activityId");
            String projectId = ancientTree.getString("projectId");
            JSONObject ancientTreeInfo = ancientTree.getJSONObject("ancientTreeInfo");
            String name = ancientTreeInfo.getString("name");
            int protectExpense = ancientTreeInfo.getInt("protectExpense");
            String cityCode = ancientTreeInfo.getString("cityCode");
            if (currentEnergy > protectExpense) {
                RpcUtil.requestRpc("alipay.greenmatrix.rpc.h5.ancienttree.protect",
                        "[{\"ancientTreeActivityId\":\"" + activityId + "\",\"ancientTreeProjectId\":\"" +
                                projectId + "\",\"cityCode\":\"" + cityCode + "\",\"source\":\"ancientreethome\"}]",
                        (resData, respArgs) -> {
                            Log.recordLog("[保护古树]消耗" + protectExpense + "能量保护古树\"" + name + "\"成功");
                        });
            }
        }

    }

    private static void projectDetail(String cityCode, JSONArray ancientTreeList) throws JSONException {
        for (int i = 0; i < ancientTreeList.length(); i++) {
            JSONObject ancientTree = ancientTreeList.getJSONObject(i);
            if (!ancientTree.getBoolean("hasProtected")) {
                String ancientTreeProjectId = ancientTree.getString("projectId");
                RpcUtil.requestRpc("alipay.greenmatrix.rpc.h5.ancienttree.projectDetail",
                        "[{\"ancientTreeProjectId\":\"" + ancientTreeProjectId +
                                "\",\"channel\":\"ONLINE\",\"cityCode\":\"" + cityCode + "\",\"source\":\"ancientreethome\"}]",
                        (resData, respArgs) -> {
                            protect(resData);
                        });
            }
        }
    }
}
