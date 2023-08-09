package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntMemberRpcCall;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;
import pansong291.xposed.quickenergy.util.Config;

public class AntMember {
    private static final String TAG = AntMember.class.getCanonicalName();

    private static boolean firstTime = true;

    public static void receivePoint() {
        if (!Config.receivePoint() || !firstTime)
            return;

        new Thread() {
            @Override
            public void run() {
                try {
                    if (Statistics.canMemberSignInToday()) {
                        String s = AntMemberRpcCall.memberSignIn();
                        JSONObject jo = new JSONObject(s);
                        if (jo.getString("resultCode").equals("SUCCESS")) {
                            Log.other("每日签到📅[" + jo.getString("signinPoint") + "积分]#已签到" + jo.getString("signinSumDay")
                                    + "天");
                            Statistics.memberSignInToday();
                        } else {
                            Log.recordLog(jo.getString("resultDesc"), s);
                        }
                    }
                    queryPointCert(1, 8);

                    anXinDou();
                } catch (Throwable t) {
                    Log.i(TAG, "receivePoint.run err:");
                    Log.printStackTrace(TAG, t);
                }
                firstTime = false;
            }
        }.start();
    }

    private static void anXinDou() {
        try {
            String appletId = "AP16150326";
            String s = AntMemberRpcCall.taskProcess(appletId);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject result = jo.getJSONObject("result");
                if (result.getBoolean("canPush")) {
                    s = AntMemberRpcCall.taskTrigger(appletId, "insportal-marketing");
                    JSONObject joTrigger = new JSONObject(s);
                    if (joTrigger.getBoolean("success")) {
                        Log.other("安心豆任务触发成功");
                    }
                }
            } else {
                Log.recordLog("anXinDou", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "anXinDou err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void queryPointCert(int page, int pageSize) {
        try {
            String s = AntMemberRpcCall.queryPointCert(page, pageSize);
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                boolean hasNextPage = jo.getBoolean("hasNextPage");
                JSONArray jaCertList = jo.getJSONArray("certList");
                for (int i = 0; i < jaCertList.length(); i++) {
                    jo = jaCertList.getJSONObject(i);
                    String bizTitle = jo.getString("bizTitle");
                    String id = jo.getString("id");
                    int pointAmount = jo.getInt("pointAmount");
                    s = AntMemberRpcCall.receivePointByUser(id);
                    jo = new JSONObject(s);
                    if (jo.getString("resultCode").equals("SUCCESS")) {
                        Log.other("领取奖励🎖️[" + bizTitle + "]#" + pointAmount + "积分");
                    } else {
                        Log.recordLog(jo.getString("resultDesc"), s);
                    }
                }
                if (hasNextPage)
                    queryPointCert(page + 1, pageSize);
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryPointCert err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
