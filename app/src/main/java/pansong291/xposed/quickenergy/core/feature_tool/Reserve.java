package pansong291.xposed.quickenergy.core.feature_tool;

import org.json.JSONArray;
import org.json.JSONObject;

import pansong291.xposed.quickenergy.hook.ReserveRpcCall;
import pansong291.xposed.quickenergy.util.BeachIdMap;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.RandomUtils;
import pansong291.xposed.quickenergy.util.ReserveIdMap;
import pansong291.xposed.quickenergy.util.Statistics;

public class Reserve {
    private static final String TAG = Reserve.class.getCanonicalName();

    private static boolean firstTime = true;

    public static void start() {
        if (!Config.reserve() || !firstTime)
            return;
        Log.recordLog("开始检测保护地", "");
        new Thread() {

            @Override
            public void run() {
                try {
                    while (FriendIdMap.currentUid == null || FriendIdMap.currentUid.isEmpty())
                        Thread.sleep(100);
                    if (Config.reserve() && firstTime) {
                        animalReserve();
                    }

                    if (Config.beach() ) {
                        protectBeach();
                    }

                } catch (Throwable t) {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }.start();
    }

    private static void animalReserve() {
        try {
            String s = ReserveRpcCall.queryTreeItemsForExchange();
            if (s == null) {
                Thread.sleep(RandomUtils.delay());
                s = ReserveRpcCall.queryTreeItemsForExchange();
            }
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                JSONObject userBaseInfo = jo.getJSONObject("userBaseInfo");
                JSONArray ja = jo.getJSONArray("treeItems");
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    if (!jo.has("projectType"))
                        continue;
                    if (!jo.getString("projectType").equals("RESERVE"))
                        continue;
                    if (!jo.getString("applyAction").equals("AVAILABLE"))
                        continue;
                    String projectId = jo.getString("itemId");
                    String itemName = jo.getString("itemName");
                    int energy = jo.getInt("energy");
                    ReserveIdMap.putIdMap(projectId, itemName + "(" + energy + "g)");
                    int index = -1;
                    for (int j = 0; j < Config.getReserveList().size(); j++) {
                        if (Config.getReserveList().get(j).equals(projectId)) {
                            index = j;
                            break;
                        }
                    }
                    if (index < 0)
                        continue;
                    int reserveCount = Config.getReserveCountList().get(index);
                    if (reserveCount <= 0)
                        continue;
                    if (!Statistics.canReserveToday(projectId, reserveCount))
                        continue;
                    exchangeTree(projectId, itemName, reserveCount);
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "animalReserve err:");
            Log.printStackTrace(TAG, t);
        }
        ReserveIdMap.saveIdMap();
        firstTime = false;
    }

    private static boolean queryTreeForExchange(String projectId) {
        try {
            String s = ReserveRpcCall.queryTreeForExchange(projectId);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                String applyAction = jo.getString("applyAction");
                int currentEnergy = jo.getInt("currentEnergy");
                jo = jo.getJSONObject("exchangeableTree");
                if ("AVAILABLE".equals(applyAction)) {
                    if (currentEnergy >= jo.getInt("energy")) {
                        return true;
                    } else {
                        Log.forest("能量不够申请保护地[" + jo.getString("projectName") + "]，停止申请！");
                        return false;
                    }
                } else {
                    Log.forest("保护地[" + jo.getString("projectName") + "]似乎没有了！");
                    return false;
                }
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryTreeForExchange err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void exchangeTree(String projectId, String itemName, int count) {
        int appliedTimes = 0;
        try {
            String s;
            JSONObject jo;
            boolean canApply = queryTreeForExchange(projectId);
            if (!canApply)
                return;
            for (int applyCount = 1; applyCount <= count; applyCount++) {
                s = ReserveRpcCall.exchangeTree(projectId);
                jo = new JSONObject(s);
                if ("SUCCESS".equals(jo.getString("resultCode"))) {
                    int vitalityAmount = jo.getInt("vitalityAmount");
                    appliedTimes = Statistics.getReserveTimes(projectId) + 1;
                    String str = "申请保护地[" + itemName + "]成功！[第" + appliedTimes + "次]"
                            + (vitalityAmount > 0 ? "获得活力值[" + vitalityAmount + "]" : "");
                    Log.forest(str);
                    Statistics.reserveToday(projectId, 1);
                } else {
                    Log.recordLog(jo.getString("resultDesc"), jo.toString());
                    Log.forest("申请保护地发生未知错误，停止申请！");
                    Statistics.reserveToday(projectId, count);
                    break;
                }
                Thread.sleep(200);
                canApply = queryTreeForExchange(projectId);
                if (!canApply) {
                    Statistics.reserveToday(projectId, count);
                    break;
                } else {
                    Thread.sleep(200);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "exchangeTree err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /* 净滩行动 */

    private static void protectBeach() {
        try {
            String s = ReserveRpcCall.queryCultivationList();
            if (s == null) {
                Thread.sleep(RandomUtils.delay());
                s = ReserveRpcCall.queryCultivationList();
            }
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                JSONArray ja = jo.getJSONArray("cultivationItemVOList");
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    if (!jo.has("templateSubType"))
                        continue;
                    if (!jo.getString("templateSubType").equals("BEACH"))
                        continue;
                    if (!jo.getString("applyAction").equals("AVAILABLE"))
                        continue;
                    String cultivationName = jo.getString("cultivationName");
                    String templateCode = jo.getString("templateCode");
                    int energy = jo.getInt("energy");
                    JSONObject projectConfig = jo.getJSONObject("projectConfigVO");
                    String projectCode = projectConfig.getString("code");
                    BeachIdMap.putIdMap(templateCode, cultivationName + "(" + energy + "g)");
                    int index = -1;
                    for (int j = 0; j < Config.getBeachList().size(); j++) {
                        if (Config.getBeachList().get(j).equals(templateCode)) {
                            index = j;
                            break;
                        }
                    }
                    if (index < 0)
                        continue;
                    int BeachCount = Config.getBeachCountList().get(index);
                    if (BeachCount <= 0)
                        continue;
                    if (!Statistics.canBeach(templateCode, BeachCount) || !Statistics.canBeachToday(templateCode))
                        continue;
                    oceanExchangeTree(templateCode, projectCode, cultivationName, BeachCount);
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "protectBeach err:");
            Log.printStackTrace(TAG, t);
        }
        BeachIdMap.saveIdMap();
    }

    private static boolean queryCultivationDetail(String cultivationCode, String projectCode) {
        try {
            String s = ReserveRpcCall.queryCultivationDetail(cultivationCode, projectCode);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONObject userInfo = jo.getJSONObject("userInfoVO");
                int currentEnergy = userInfo.getInt("currentEnergy");
                jo = jo.getJSONObject("cultivationDetailVO");
                String applyAction = jo.getString("applyAction");
                if ("AVAILABLE".equals(applyAction)) {
                    if (currentEnergy >= jo.getInt("energy")) {
                        return true;
                    } else {
                        Log.forest("能量不够申请[" + jo.getString("cultivationName") + "]，停止申请！");
                        return false;
                    }
                } else {
                    Log.forest("净滩行动[" + jo.getString("cultivationName") + "]似乎没有了！");
                    return false;
                }
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryCultivationDetail err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void oceanExchangeTree(String cultivationCode, String projectCode, String itemName, int count) {
        int appliedTimes = 0;
        try {
            String s;
            JSONObject jo;
            boolean canApply = queryCultivationDetail(cultivationCode, projectCode);
            if (!canApply)
                return;
            for (int applyCount = 1; applyCount <= count; applyCount++) {
                s = ReserveRpcCall.oceanExchangeTree(cultivationCode, projectCode);
                jo = new JSONObject(s);
                if ("SUCCESS".equals(jo.getString("resultCode"))) {
                    JSONArray awardInfos = jo.getJSONArray("rewardItemVOs");
                    String award = "";
                    for (int i = 0; i < awardInfos.length(); i++) {
                        jo = awardInfos.getJSONObject(i);
                        award = award + jo.getString("name") + "*" + jo.getInt("num");
                    }
                    appliedTimes = Statistics.getBeachTimes(cultivationCode) + 1;
                    String str = "净滩行动[" + itemName + "]，[第" + appliedTimes + "次]"
                            + "获得奖励[" + award + "]";
                    Log.forest(str);
                    Statistics.beachRecord(cultivationCode, 1);
                } else {
                    Log.recordLog(jo.getString("resultDesc"), jo.toString());
                    Log.forest("净滩行动发生未知错误，停止申请！");
                    Statistics.beachToday(cultivationCode);
                    break;
                }
                Thread.sleep(200);
                canApply = queryCultivationDetail(cultivationCode, projectCode);
                if (!canApply) {
                    Statistics.beachToday(cultivationCode);
                    break;
                } else {
                    Thread.sleep(200);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "oceanExchangeTree err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
