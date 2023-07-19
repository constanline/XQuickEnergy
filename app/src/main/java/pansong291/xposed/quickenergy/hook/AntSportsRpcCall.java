package pansong291.xposed.quickenergy.hook;

public class AntSportsRpcCall
{
    private static final String chInfo = "antsports-account",
            timeZone = "Asia\\/Shanghai", version = "3.0.1.2";

    public static String queryMyHomePage() {
        String args1 = "[{\"chInfo\":\"" + chInfo
                + "\",\"pathListUsePage\":true,\"timeZone\":\"" + timeZone + "\"}]";
        return RpcUtil.request("alipay.antsports.walk.map.queryMyHomePage", args1);
    }

    public static String join(String pathId) {
        String args1 = "[{\"chInfo\":\"" + chInfo + "\",\"pathId\":\"" + pathId + "\"}]";
        return RpcUtil.request("alipay.antsports.walk.map.join", args1);
    }

    public static String go(String day, String rankCacheKey, int stepCount) {
        String args1 = "[{\"chInfo\":\"" + chInfo + "\",\"day\":\"" + day
                + "\",\"needAllBox\":true,\"rankCacheKey\":\"" + rankCacheKey
                + "\",\"timeZone\":\"" + timeZone + "\",\"useStepCount\":" + stepCount + "}]";
        return RpcUtil.request("alipay.antsports.walk.map.go", args1);
    }

    public static String openTreasureBox(String boxNo, String userId) {
        String args1 = "[{\"boxNo\":\"" + boxNo + "\",\"chInfo\":\""
                + chInfo + "\",\"userId\":\"" + userId + "\"}]";
        return RpcUtil.request("alipay.antsports.walk.treasureBox.openTreasureBox", args1);
    }

    public static String queryProjectList(int index) {
        String args1 = "[{\"chInfo\":\"" + chInfo + "\",\"index\":"
                + index + ",\"projectListUseVertical\":true}]";
        return RpcUtil.request("alipay.antsports.walk.charity.queryProjectList", args1);
    }

    public static String donate(int donateCharityCoin, String projectId) {
        String args1 = "[{\"chInfo\":\"" + chInfo + "\",\"donateCharityCoin\":"
                + donateCharityCoin + ",\"projectId\":\"" + projectId + "\"}]";
        return RpcUtil.request("alipay.antsports.walk.charity.donate", args1);
    }

    public static String queryWalkStep() {
        String args1 = "[{\"timeZone\":\"" + timeZone + "\"}]";
        return RpcUtil.request("alipay.antsports.walk.user.queryWalkStep", args1);
    }

    public static String exchange(int count, int ver) {
        String args1 = "[{\"actId\":\"\",\"count\":" + count
                + ",\"timezone\":\"" + timeZone + "\",\"ver\":"
                + ver + ",\"version\":\"" + version + "\"}]";
        return RpcUtil.request("alipay.charity.mobile.donate.exchange", args1);
    }

    public static String exchangeSuccess(String exchangeId) {
        String args1 = "[{\"exchangeId\":\"" + exchangeId
                + "\",\"timezone\":\"GMT+08:00\",\"version\":\"" + version + "\"}]";
        return RpcUtil.request("alipay.charity.mobile.donate.exchange.success", args1);
    }
}
