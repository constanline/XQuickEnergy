package pansong291.xposed.quickenergy;

import org.json.JSONObject;
import pansong291.xposed.quickenergy.hook.ReadingDadaRpcCall;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.StringUtil;

/**
 * @author Constanline
 * @since 2023/08/22
 */
public class ReadingDada {
    private static final String TAG = ReadingDada.class.getCanonicalName();

    public static boolean answerQuestion(JSONObject bizInfo) {
        try {
            String taskJumpUrl = bizInfo.optString("taskJumpUrl");
            if (StringUtil.isEmpty(taskJumpUrl)) {
                taskJumpUrl = bizInfo.getString("targetUrl");
            }
            String activityId = taskJumpUrl.split("activityId%3D")[1].split("%26")[0];
            String outBizId;
            if (taskJumpUrl.contains("outBizId%3D")) {
                outBizId = taskJumpUrl.split("outBizId%3D")[1].split("%26")[0];
            } else {
                outBizId = "";
            }
            String s = ReadingDadaRpcCall.getQuestion(activityId);
            JSONObject jo = new JSONObject(s);
            if ("200".equals(jo.getString("resultCode"))) {
                String answer = jo.getJSONArray("options").getString(0);
                s = ReadingDadaRpcCall.submitAnswer(activityId, outBizId, jo.getString("questionId"), answer);
                jo = new JSONObject(s);
                if ("200".equals(jo.getString("resultCode"))) {
                    Log.recordLog("答题完成");
                    return true;
                } else {
                    Log.recordLog("答题失败");
                }
            } else {
                Log.recordLog("获取问题失败");
            }
        } catch (Throwable e) {
            Log.i(TAG, "answerQuestion err:");
            Log.printStackTrace(TAG, e);
        }
        return false;
    }
}
