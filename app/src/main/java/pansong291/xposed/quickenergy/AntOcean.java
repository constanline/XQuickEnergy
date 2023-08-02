package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.AntForestRpcCall;
import pansong291.xposed.quickenergy.hook.AntOceanRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;

/**
 * @author Constanline
 * @since 2023/08/01
 */
public class AntOcean {
    private static final String TAG = AntOcean.class.getCanonicalName();

    public static void start() {
        if(!Config.antOcean())
            return;
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    String s = AntOceanRpcCall.queryOceanStatus();
                    JSONObject jo = new JSONObject(s);
                    if("SUCCESS".equals(jo.getString("resultCode"))) {
                        if (jo.getBoolean("opened")) {
                            queryHomePage();
                        } else {
                            Config.setAntOcean(false);
                            Log.recordLog("【神奇海洋】请先开启神奇海洋，并完成引导教程");
                        }
                    } else {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                } catch(Throwable t) {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }.start();
    }

    private static void queryHomePage() {
        try {
            JSONObject joHomePage = new JSONObject(AntOceanRpcCall.queryHomePage());
            if ("SUCCESS".equals(joHomePage.getString("resultCode"))) {
                if (joHomePage.has("bubbleVOList")) {
                    collectEnergy(joHomePage.getJSONArray("bubbleVOList"));
                }

                JSONObject userInfoVO = joHomePage.getJSONObject("userInfoVO");
                int rubbishNumber = userInfoVO.optInt("rubbishNumber");
                String userId = userInfoVO.getString("userId");
                cleanOcean(userId, rubbishNumber);
            } else {
                Log.i(TAG, joHomePage.getString("resultDesc"));
            }
        } catch(Throwable t) {
            Log.i(TAG, "queryHomePage err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void collectEnergy(JSONArray bubbleVOList) {
        try {
            for (int i = 0; i < bubbleVOList.length(); i++) {
                JSONObject bubble = bubbleVOList.getJSONObject(i);
                if ("AVAILABLE".equals(bubble.getString("collectStatus"))) {
                    long bubbleId = bubble.getLong("id");
                    String userId = bubble.getString("userId");
                    String s = AntForestRpcCall.collectEnergy(null, userId, bubbleId);
                    JSONObject jo = new JSONObject(s);
                    if ("SUCCESS".equals(jo.getString("resultCode"))) {
                        JSONArray retBubbles = jo.optJSONArray("bubbles");
                        if (retBubbles != null) {
                            for (int j = 0; j < retBubbles.length(); j++) {
                                JSONObject retBubble = retBubbles.optJSONObject(j);
                                if (retBubble != null) {
                                    int collectedEnergy = retBubble.getInt("collectedEnergy");
                                    Log.forest("【神奇海洋】收取了 [" + FriendIdMap.getNameById(userId) + "] 的海洋能量 " + collectedEnergy + "克");
                                }
                            }
                        }
                    } else {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                }
            }
        } catch(Throwable t) {
            Log.i(TAG, "queryHomePage err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void cleanOcean(String userId, int rubbishNumber) {
        try {
            for (int i = 0; i < rubbishNumber; i++) {
                String s = AntOceanRpcCall.cleanOcean(userId);
                JSONObject jo = new JSONObject(s);
                if ("SUCCESS".equals(jo.getString("resultCode"))) {
                    JSONArray cleanRewardVOS = jo.getJSONArray("cleanRewardVOS");
                    checkReward(cleanRewardVOS);
                } else {
                    Log.i(TAG, jo.getString("resultDesc"));
                }
            }
        } catch(Throwable t) {
            Log.i(TAG, "cleanOcean err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void checkReward(JSONArray rewards) {
        try {
            for (int i = 0; i < rewards.length(); i++) {
                JSONObject cleanReward = rewards.getJSONObject(i);
                JSONArray attachReward = cleanReward.getJSONArray("attachRewardBOList");

                if (attachReward.length() > 0) {
                    Log.forest("【神奇海洋】获取碎片奖励");
                }

            }
        } catch (Throwable t) {
            Log.i(TAG, "checkReward err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
