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

    public static String uploadDishImageBeforeMeals(String channel, String dayPoint) {
        return RpcUtil.request("alipay.ecolife.rpc.h5.uploadDishImage",
                "[{\"channel\":\"" + channel + "\",\"dayPoint\":\"" + dayPoint
                        + "\",\"source\":\"photo-comparison\",\"uploadParamMap\":{\"AIResult\":[{\"conf\":0.016113121,\"kvPair\":false,\"label\":\"other\",\"pos\":[1,0.2213781,0.0012539185,0.7786219],\"value\":\"\"},{\"conf\":0.00046864362,\"kvPair\":false,\"label\":\"guangpan\",\"pos\":[1,0.2213781,0.0012539185,0.7786219],\"value\":\"\"},{\"conf\":0.9834183,\"kvPair\":false,\"label\":\"feiguangpan\",\"pos\":[1,0.2213781,0.0012539185,0.7786219],\"value\":\"\"}],\"existAIResult\":true,\"imageId\":\"A*khE0QbDOJTsAAAAAAAAAAAAAAQAAAQ\",\"imageUrl\":\"https://mdn.alipayobjects.com/afts/img/A*khE0QbDOJTsAAAAAAAAAAAAAAQAAAQ/original?bz=APM_20000067\",\"operateType\":\"BEFORE_MEALS\"}}]");
    }

    public static String uploadDishImageAfterMeals(String channel, String dayPoint) {
        return RpcUtil.request("alipay.ecolife.rpc.h5.uploadDishImage",
                "[{\"channel\":\"" + channel + "\",\"dayPoint\":\"" + dayPoint
                        + "\",\"source\":\"photo-comparison\",\"uploadParamMap\":{\"AIResult\":[{\"conf\":0.00018646188,\"kvPair\":false,\"label\":\"other\",\"pos\":[1,0.2213781,0.0012539185,0.7786219],\"value\":\"\"},{\"conf\":0.9997553,\"kvPair\":false,\"label\":\"guangpan\",\"pos\":[1,0.2213781,0.0012539185,0.7786219],\"value\":\"\"},{\"conf\":0.00005811434,\"kvPair\":false,\"label\":\"feiguangpan\",\"pos\":[1,0.2213781,0.0012539185,0.7786219],\"value\":\"\"}],\"existAIResult\":true,\"imageId\":\"A*-r7VRbaQS_kAAAAAAAAAAAAAAQAAAQ\",\"imageUrl\":\"https://mdn.alipayobjects.com/afts/img/A*-r7VRbaQS_kAAAAAAAAAAAAAAQAAAQ/original?bz=APM_20000067\",\"operateType\":\"AFTER_MEALS\"}}]");
    }

    public static String queryDish(String channel, String dayPoint) {
        return RpcUtil.request("alipay.ecolife.rpc.h5.queryDish",
                "[{\"channel\":\"" + channel + "\",\"dayPoint\":\"" + dayPoint
                        + "\",\"source\":\"photo-comparison\"}]");
    }

}
