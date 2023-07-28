package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntCooperateRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.CooperationIdMap;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.RandomUtils;
import pansong291.xposed.quickenergy.util.Statistics;

public class AntCooperate {
    private static final String TAG = AntCooperate.class.getCanonicalName();

    private static boolean firstTime = true;

    public static void start() {
        if (!Config.cooperateWater() || !firstTime)
            return;
        new Thread() {

            @Override
            public void run() {
                try {
                    while (FriendIdMap.currentUid == null || FriendIdMap.currentUid.isEmpty())
                        Thread.sleep(100);
                    String s = AntCooperateRpcCall.queryUserCooperatePlantList();
                    if (s == null) {
                        Thread.sleep(RandomUtils.delay());
                        s = AntCooperateRpcCall.queryUserCooperatePlantList();
                    }
                    JSONObject jo = new JSONObject(s);
                    if (jo.getString("resultCode").equals("SUCCESS")) {
                        int userCurrentEnergy = jo.getInt("userCurrentEnergy");
                        JSONArray ja = jo.getJSONArray("cooperatePlants");
                        for (int i = 0; i < ja.length(); i++) {
                            jo = ja.getJSONObject(i);
                            String cooperationId = jo.getString("cooperationId");
                            if (!jo.has("name")) {
                                s = AntCooperateRpcCall.queryCooperatePlant(cooperationId);
                                jo = new JSONObject(s).getJSONObject("cooperatePlant");
                            }
                            String name = jo.getString("name");
                            int waterDayLimit = jo.getInt("waterDayLimit");
                            CooperationIdMap.putIdMap(cooperationId, name);
                            if (!Statistics.canCooperateWaterToday(FriendIdMap.currentUid, cooperationId))
                                continue;
                            int index = -1;
                            for (int j = 0; j < Config.getCooperateWaterList().size(); j++) {
                                if (Config.getCooperateWaterList().get(j).equals(cooperationId)) {
                                    index = j;
                                    break;
                                }
                            }
                            if (index >= 0) {
                                int num = Config.getcooperateWaterNumList().get(index);
                                if (num > waterDayLimit)
                                    num = waterDayLimit;
                                if (num > userCurrentEnergy)
                                    num = userCurrentEnergy;
                                if (num > 0)
                                    cooperateWater(FriendIdMap.currentUid, cooperationId, num, name);
                            }
                        }
                    } else {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                } catch (Throwable t) {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
                CooperationIdMap.saveIdMap();
                firstTime = false;
            }
        }.start();
    }

    private static void cooperateWater(String uid, String coopId, int count, String name) {
        try {
            String s = AntCooperateRpcCall.cooperateWater(uid, coopId, count);
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                Log.forest("合种[" + name + "]" + jo.getString("barrageText"));
                Statistics.cooperateWaterToday(FriendIdMap.currentUid, coopId);
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "cooperateWater err:");
            Log.printStackTrace(TAG, t);
        }
    }

}
