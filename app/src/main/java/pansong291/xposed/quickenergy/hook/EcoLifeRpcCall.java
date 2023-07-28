package pansong291.xposed.quickenergy.hook;

public class EcoLifeRpcCall {

    public static String queryHomePage() {
        return RpcUtil.request("alipay.ecolife.rpc.h5.queryHomePage",
                "[{\"channel\":\"ALIPAY\",\"source\":\"search_brandbox\"}]");
    }

    public static String tick(String actionId, String channel, String dayPoint, boolean photoguangpan) {
        String args1 = null;
        if (photoguangpan) {
            args1 = "[{\"actionId\":\"photoguangpan\",\"channel\":\"" + channel + "\",\"dayPoint\":\"" + dayPoint
                    + "\",\"source\":\"search_brandbox\"}]";
        } else {
            args1 = "[{\"actionId\":\"" + actionId + "\",\"channel\":\""
                    + channel + "\",\"dayPoint\":\"" + dayPoint
                    + "\",\"generateEnergy\":false,\"source\":\"search_brandbox\"}]";
        }
        return RpcUtil.request("alipay.ecolife.rpc.h5.tick", args1);
    }

}
