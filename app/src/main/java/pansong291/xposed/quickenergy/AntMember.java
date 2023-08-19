package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntMemberRpcCall;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.Statistics;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.TimeUtil;

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

                    insBlueBean();

                    if (Config.collectSesame())
                        zmxy();

                    if (Config.merchantKmdk() || Config.zcjSignIn()) {
                        JSONObject jo = new JSONObject(AntMemberRpcCall.transcodeCheck());
                        if (jo.getBoolean("success")) {
                            JSONObject data = jo.getJSONObject("data");
                            if (data.optBoolean("isOpened")) {
                                if (Config.merchantKmdk())
                                    merchantKmdk();

                                if (Config.zcjSignIn())
                                    zcjSignIn();
                            } else {
                                Log.recordLog("商家服务未开通！");
                            }
                        }
                    }
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

    private static void insBlueBean() {
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
                int userCurrentPoint = result.optInt("userCurrentPoint", 0);
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

    private static void zmxy() {
        try {
            String s = AntMemberRpcCall.queryHome();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject entrance = jo.getJSONObject("entrance");
                if (entrance.optBoolean("openApp")) {
                    jo = new JSONObject(AntMemberRpcCall.queryCreditFeedback());
                    if (jo.getBoolean("success")) {
                        JSONArray creditFeedbackVOS = jo.getJSONArray("creditFeedbackVOS");
                        for (int i = 0; i < creditFeedbackVOS.length(); i++) {
                            jo = creditFeedbackVOS.getJSONObject(i);
                            if ("UNCLAIMED".equals(jo.getString("status"))) {
                                String title = jo.getString("title");
                                String creditFeedbackId = jo.getString("creditFeedbackId");
                                String potentialSize = jo.getString("potentialSize");
                                jo = new JSONObject(AntMemberRpcCall.collectCreditFeedback(creditFeedbackId));
                                if (jo.getBoolean("success")) {
                                    Log.other("收芝麻粒🙇🏻‍♂️[" + title + "]#" + potentialSize + "粒");
                                } else {
                                    Log.recordLog(jo.getString("resultView"), jo.toString());
                                }
                            }
                        }
                    } else {
                        Log.recordLog(jo.getString("resultView"), jo.toString());
                    }
                } else {
                    Log.recordLog("芝麻信用未开通！");
                }
            } else {
                Log.recordLog("zmxy", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "zmxy err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void merchantKmdk() {
        try {
            String s = AntMemberRpcCall.queryActivity();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                if (TimeUtil.getTimeStr().compareTo("0600") > 0 && TimeUtil.getTimeStr().compareTo("1200") < 0) {
                    if ("SIGN_IN_ENABLE".equals(jo.getString("signInStatus"))) {
                        String activityNo = jo.getString("activityNo");
                        JSONObject joSignIn = new JSONObject(AntMemberRpcCall.signIn(activityNo));
                        if (joSignIn.getBoolean("success")) {
                            Log.other("商家服务🕴🏻[开门打卡签到成功]");
                            merchantKmdk();
                        } else {
                            Log.recordLog(joSignIn.getString("errorMsg"), joSignIn.toString());
                        }
                    }
                }
                if (jo.optBoolean("signUpEnable") && "UN_SIGN_UP".equals(jo.getString("signUpStatus"))) {
                    String activityNo = jo.getString("activityNo");
                    String activityPeriodName = jo.getString("activityPeriodName");
                    JSONObject joSignUp = new JSONObject(AntMemberRpcCall.signUp(activityNo));
                    if (joSignUp.getBoolean("success")) {
                        Log.other("商家服务🕴🏻[" + activityPeriodName + "开门打卡报名]");
                    } else {
                        Log.recordLog(joSignUp.getString("errorMsg"), joSignUp.toString());
                    }
                }
            } else {
                Log.recordLog("queryActivity", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "signUp err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void zcjSignIn() {
        try {
            String s = AntMemberRpcCall.zcjSignInQuery();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject button = jo.getJSONObject("data").getJSONObject("button");
                if ("UNRECEIVED".equals(button.getString("status"))) {
                    jo = new JSONObject(AntMemberRpcCall.zcjSignInExecute());
                    if (jo.getBoolean("success")) {
                        JSONObject data = jo.getJSONObject("data");
                        int todayReward = data.getInt("todayReward");
                        String widgetName = data.getString("widgetName");
                        Log.other("商家服务🕴🏻[" + widgetName + "]#" + todayReward + "积分");
                    }
                }
            } else {
                Log.recordLog("zcjSignInQuery", s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "zcjSignIn err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
