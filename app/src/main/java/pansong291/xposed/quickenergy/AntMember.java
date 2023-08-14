package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntMemberRpcCall;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;

public class AntMember {
    private static final String TAG = AntMember.class.getCanonicalName();

    public static void receivePoint() {
        if (!Config.receivePoint())
            return;

        new Thread() {
            @Override
            public void run() {
                try {
                    while (FriendIdMap.currentUid == null || FriendIdMap.currentUid.isEmpty())
                    Thread.sleep(100);
                    if (Statistics.canMemberSignInToday(FriendIdMap.currentUid)) {
                        String s = AntMemberRpcCall.memberSignIn();
                        JSONObject jo = new JSONObject(s);
                        if ("SUCCESS".equals(jo.getString("resultCode"))) {
                            Log.other("每日签到📅[" + jo.getString("signinPoint") + "积分]#已签到" + jo.getString("signinSumDay")
                                    + "天");
                            Statistics.memberSignInToday(FriendIdMap.currentUid);
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
            }
        }.start();
    }

    private static void queryPointCert(int page, int pageSize) {
        try {
            String s = AntMemberRpcCall.queryPointCert(page, pageSize);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                boolean hasNextPage = jo.getBoolean("hasNextPage");
                JSONArray jaCertList = jo.getJSONArray("certList");
                for (int i = 0; i < jaCertList.length(); i++) {
                    jo = jaCertList.getJSONObject(i);
                    String bizTitle = jo.getString("bizTitle");
                    String id = jo.getString("id");
                    int pointAmount = jo.getInt("pointAmount");
                    s = AntMemberRpcCall.receivePointByUser(id);
                    jo = new JSONObject(s);
                    if ("SUCCESS".equals(jo.getString("resultCode"))) {
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

    private static void anXinDou() {
        try {
            String s = AntMemberRpcCall.pageRender();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject result = jo.getJSONObject("result");
                    JSONArray modules = result.getJSONArray("modules");
                    for (int i = 0; i < modules.length(); i++) {
                        jo = modules.getJSONObject(i);
                        if ("签到配置".equals(jo.getString("name"))) {
                            String appletId = jo.getJSONObject("content").getJSONObject("signConfig")
                                    .getString("appletId");
                            insBlueBeanSign(appletId);
                        } else if ("兑换时光加速器".equals(jo.getString("name"))) {
                            String oneStopId = jo.getJSONObject("content").getJSONObject("beanDeductBanner")
                                    .getString("oneStopId");
                            if (Config.insBlueBeanExchange())
                                insBlueBeanExchange(oneStopId);
                        }
                    }
            } else {
                Log.recordLog("pageRender", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "anXinDou err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void insBlueBeanSign(String appletId) {
        try {
            String s = AntMemberRpcCall.taskProcess(appletId);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject result = jo.getJSONObject("result");
                if (result.getBoolean("canPush")) {
                    s = AntMemberRpcCall.taskTrigger(appletId, "insportal-marketing");
                    JSONObject joTrigger = new JSONObject(s);
                    if (joTrigger.getBoolean("success")) {
                        Log.other("安心豆🥔[签到成功]");
                    }
                }
            } else {
                Log.recordLog("taskProcess", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "insBlueBeanSign err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void insBlueBeanExchange(String itemId) {
        try {
            String s = AntMemberRpcCall.queryUserAccountInfo();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject result = jo.getJSONObject("result");
                int userCurrentPoint = result.optInt("userCurrentPoint");
                if (userCurrentPoint > 0) {
                    jo = new JSONObject(AntMemberRpcCall.exchangeDetail(itemId));
                    if (jo.getBoolean("success")) {
                        JSONObject exchangeDetail = jo.getJSONObject("result").getJSONObject("rspContext")
                                .getJSONObject("params").getJSONObject("exchangeDetail");
                        if ("ITEM_GOING".equals(exchangeDetail.getString("status"))) {
                            JSONObject itemExchangeConsultDTO = exchangeDetail.getJSONObject("itemExchangeConsultDTO");
                            int pointAmount = itemExchangeConsultDTO.getInt("realConsumePointAmount");
                            if (itemExchangeConsultDTO.getBoolean("canExchange") && userCurrentPoint >= pointAmount) {
                                jo = new JSONObject(AntMemberRpcCall.exchange(itemId, pointAmount));
                                if (jo.getBoolean("success")) {
                                    Log.other("安心豆🥔[兑换" + exchangeDetail.getString("itemName") + "]");
                                } else {
                                    Log.recordLog("exchange", jo.toString());
                                }
                            }
                        }
                    } else {
                        Log.recordLog("exchangeDetail", jo.toString());
                    }
                }
            } else {
                Log.recordLog("queryUserAccountInfo", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "insBlueBeanExchange err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
