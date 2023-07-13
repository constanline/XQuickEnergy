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

public class AntCooperate
{
    private static final String TAG = AntCooperate.class.getCanonicalName();

    public static void start(ClassLoader loader, int times)
    {
        if(!Config.cooperateWater() || times != 0)
            return;
        new Thread()
        {
            private ClassLoader loader;

            public Thread setData(ClassLoader cl)
            {
                loader = cl;
                return this;
            }

            @Override
            public void run()
            {
                try
                {
                    while(FriendIdMap.currentUid == null || FriendIdMap.currentUid.isEmpty())
                        Thread.sleep(100);
                    String s = AntCooperateRpcCall.rpcCall_queryUserCooperatePlantList(loader);
                    if(s == null)
                    {
                        Thread.sleep(RandomUtils.delay());
                        s = AntCooperateRpcCall.rpcCall_queryUserCooperatePlantList(loader);
                    }
                    JSONObject jo = new JSONObject(s);
                    if(jo.getString("resultCode").equals("SUCCESS"))
                    {
                        int userCurrentEnergy = jo.getInt("userCurrentEnergy");
                        JSONArray ja = jo.getJSONArray("cooperatePlants");
                        for(int i = 0; i < ja.length(); i++)
                        {
                            jo = ja.getJSONObject(i);
                            String cooperationId = jo.getString("cooperationId");
                            if(!jo.has("name"))
                            {
                                s = AntCooperateRpcCall.rpcCall_queryCooperatePlant(loader, cooperationId);
                                jo = new JSONObject(s).getJSONObject("cooperatePlant");
                            }
                            String name = jo.getString("name");
                            int waterDayLimit = jo.getInt("waterDayLimit");
                            CooperationIdMap.putIdMap(cooperationId, name);
                            if(!Statistics.canCooperateWaterToday(FriendIdMap.currentUid, cooperationId))
                                continue;
                            int index = -1;
                            for(int j = 0; j < Config.getCooperateWaterList().size(); j++)
                            {
                                if(Config.getCooperateWaterList().get(j).equals(cooperationId))
                                {
                                    index = j;
                                    break;
                                }
                            }
                            if(index >= 0)
                            {
                                int num = Config.getcooperateWaterNumList().get(index);
                                if(num > waterDayLimit)
                                    num = waterDayLimit;
                                if(num > userCurrentEnergy)
                                    num = userCurrentEnergy;
                                if(num > 0)
                                    cooperateWater(loader, FriendIdMap.currentUid, cooperationId, num, name);
                            }
                        }
                    }else
                    {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                }catch(Throwable t)
                {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
                CooperationIdMap.saveIdMap();
            }
        }.setData(loader).start();
    }

    private static void cooperateWater(ClassLoader loader, String uid, String coopId, int count, String name)
    {
        try
        {
            String s = AntCooperateRpcCall.rpcCall_cooperateWater(loader, uid, coopId, count);
            JSONObject jo = new JSONObject(s);
            if(jo.getString("resultCode").equals("SUCCESS"))
            {
                Log.forest("合种【" + name + "】" + jo.getString("barrageText"));
                Statistics.cooperateWaterToday(FriendIdMap.currentUid, coopId);
            }else
            {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "cooperateWater err:");
            Log.printStackTrace(TAG, t);
        }
    }

}
