package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.StringUtil;

/**
 * @author Constanline
 * @since 2023/08/22
 */
public class ReadingDadaRpcCall {
    private static final String VERSION = "1";

    public static String submitAnswer(String activityId, String outBizId, String questionId, String answer) {
        return RpcUtil.request("com.alipay.reading.game.dada.openDailyAnswer.submitAnswer",
                "[{\"activityId\":\"" + activityId + "\",\"answer\":\"" + answer + "\",\"dadaVersion\":\"1.3.0\"," +
                        (StringUtil.isEmpty(outBizId)?"":"\"outBizId\":\"" + outBizId + "\",") +
                        "\"questionId\":\"" + questionId + "\",\"version\":" + VERSION + "}]");
    }

    public static String getQuestion(String activityId) {
        return RpcUtil.request("com.alipay.reading.game.dada.openDailyAnswer.getQuestion",
                "[{\"activityId\":\"" + activityId + "\",\"dadaVersion\":\"1.3.0\",\"version\":" + VERSION + "}]");
    }

}
