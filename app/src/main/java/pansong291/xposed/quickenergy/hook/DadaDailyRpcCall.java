package pansong291.xposed.quickenergy.hook;

/**
 * @author Constanline
 * @since 2023/08/04
 */
public class DadaDailyRpcCall {

    public static String home(String activityId) {
        return RpcUtil.request("com.alipay.reading.game.dadaDaily.home",
                "[{\"activityId\":" + activityId + ",\"dadaVersion\":\"1.3.0\",\"version\":1}]");
    }

    public static String submit(String activityId, String answer, Long questionId) {
        return RpcUtil.request("com.alipay.reading.game.dadaDaily.submit",
                "[{\"activityId\":" + activityId + ",\"answer\":\"" + answer + "\",\"dadaVersion\":\"1.3.0\",\"questionId\":" +
                        questionId + ",\"version\":1}]");
    }
}
