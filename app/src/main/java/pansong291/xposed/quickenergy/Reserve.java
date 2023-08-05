package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.ReserveRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.ReserveIdMap;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.RandomUtils;
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
                    while(FriendIdMap.currentUid == null || FriendIdMap.currentUid.isEmpty())
                        Thread.sleep(100);
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
                            /*
                             * if (!jo.has("itemId")) {
                             * s = ReserveRpcCall.queryTreeItemsForExchange();
                             * jo = new JSONObject(s).getJSONObject("treeItems");
                             * }
                             */
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
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
                ReserveIdMap.saveIdMap();
                firstTime = false;
            }
        }.start();
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
                if (!Statistics.canReserveToday(projectId, count))
                    continue;
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
                Thread.sleep(500);
                canApply = queryTreeForExchange(projectId);
                if (!canApply) {
                    Statistics.reserveToday(projectId, count);
                    break;
                } else {
                    Thread.sleep(500);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "exchangeTree err:");
            Log.printStackTrace(TAG, t);
        }
    }

}
