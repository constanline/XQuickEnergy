package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntMemberRpcCall;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;
import pansong291.xposed.quickenergy.util.Config;

public class AntMember
{
    public static final String TAG = AntMember.class.getCanonicalName();

    public static void receivePoint(ClassLoader loader, int times)
    {
        if(!Config.receivePoint() || times != 0)
            return;

        new Thread()
        {
            ClassLoader loader;

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
                    if(Statistics.canMemberSignInToday())
                    {
                        String s = AntMemberRpcCall.rpcCall_memberSignin(loader);
                        JSONObject jo = new JSONObject(s);
                        if(jo.getString("resultCode").equals("SUCCESS"))
                        {
                            Log.other(
                                    "领取〈每日签到〉〈" + jo.getString("signinPoint") +
                                            "积分〉，已签到〈" + jo.getString("signinSumDay") + "天〉");
                            Statistics.memberSignInToday();
                        }else
                        {
                            Log.recordLog(jo.getString("resultDesc"), s);
                        }
                    }
                    queryPointCert(loader, 1, 8);
                    claimFamilyPoint(loader);
                }catch(Throwable t)
                {
                    Log.i(TAG, "receivePoint.run err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }.setData(loader).start();
    }

    private static void queryPointCert(ClassLoader loader, int page, int pageSize)
    {
        try
        {
            String s = AntMemberRpcCall.rpcCall_queryPointCert(loader, page, pageSize);
            JSONObject jo = new JSONObject(s);
            if(jo.getString("resultCode").equals("SUCCESS"))
            {
                boolean hasNextPage = jo.getBoolean("hasNextPage");
                JSONArray jaCertList = jo.getJSONArray("certList");
                for(int i = 0; i < jaCertList.length(); i++)
                {
                    jo = jaCertList.getJSONObject(i);
                    String bizTitle = jo.getString("bizTitle");
                    String id = jo.getString("id");
                    int pointAmount = jo.getInt("pointAmount");
                    s = AntMemberRpcCall.rpcCall_receivePointByUser(loader, id);
                    jo = new JSONObject(s);
                    if(jo.getString("resultCode").equals("SUCCESS"))
                    {
                        Log.other("领取〈" + bizTitle + "〉〈" + pointAmount + "积分〉");
                    }else
                    {
                        Log.recordLog(jo.getString("resultDesc"), s);
                    }
                }
                if(hasNextPage)
                    queryPointCert(loader, page + 1, pageSize);
            }else
            {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "queryPointCert err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void claimFamilyPoint(ClassLoader loader)
    {
        try
        {
            AntMemberRpcCall.rpcCall_familySignin(loader);
            String s = AntMemberRpcCall.rpcCall_familyHomepage(loader);
            JSONObject jo = new JSONObject(s);
            if(jo.getBoolean("success"))
            {
                jo = jo.getJSONObject("data");
                if(jo.getBoolean("success"))
                {
                    jo = jo.getJSONObject("familyInfoView");
                    String familyId = jo.getString("familyId");
                    s = AntMemberRpcCall.rpcCall_queryFamilyPointCert(loader, familyId);
                    jo = new JSONObject(s);
                    if(jo.getBoolean("success"))
                    {
                        JSONArray ja = jo.getJSONArray("familyPointCertInfos");
                        for(int i = 0; i < ja.length(); i++)
                        {
                            jo = ja.getJSONObject(i);
                            String bizTitle = jo.getString("bizTitle");
                            long certId = jo.getLong("certId");
                            s = AntMemberRpcCall.rpcCall_claimFamilyPointCert(loader, certId, familyId);
                            jo = new JSONObject(s);
                            if(jo.getBoolean("success"))
                            {
                                Log.other("领取〈" + bizTitle + "〉〈" + jo.getInt("realPoint") + "家庭积分〉");
                            }else
                            {
                                Log.recordLog(jo.getString("resultDesc"), s);
                            }
                        }
                    }else
                    {
                        Log.recordLog(jo.getString("resultDesc"), s);
                    }
                }
            }
        }catch(Throwable t)
        {
            Log.i(TAG, "claimFamilyPoint err:");
            Log.printStackTrace(TAG, t);
        }
    }

}
