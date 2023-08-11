package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.RandomUtils;

public class ReserveRpcCall {
    private static final String VERSION = "20230501";
    private static final String VERSION2 = "20230522";
    private static final String VERSION3 = "20230701";

    private static String getUniqueId() {
        return String.valueOf(System.currentTimeMillis()) + RandomUtils.nextLong();
    }

    public static String queryTreeItemsForExchange() {
        return RpcUtil.request("alipay.antforest.forest.h5.queryTreeItemsForExchange",
                "[{\"cityCode\":\"370100\",\"itemTypes\":\"\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                        + VERSION2 + "\"}]");
    }

    public static String queryTreeForExchange(String projectId) {
        return RpcUtil.request("alipay.antforest.forest.h5.queryTreeForExchange", "[{\"projectId\":\"" + projectId
                + "\",\"version\":\"" + VERSION + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    public static String exchangeTree(String projectId) {
        int projectId_num = Integer.parseInt(projectId);
        return RpcUtil.request("alipay.antmember.forest.h5.exchangeTree",
                "[{\"projectId\":" + projectId_num + ",\"sToken\":\"" + System.currentTimeMillis() + "\",\"version\":\""
                        + VERSION + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    /* 净滩行动 */

    public static String queryCultivationList() {
        return RpcUtil.request("alipay.antocean.ocean.h5.queryCultivationList",
                "[{\"source\":\"ANT_FOREST\",\"version\":\"" + VERSION3 + "\"}]");
    }

    public static String queryCultivationDetail(String cultivationCode, String projectCode) {
        return RpcUtil.request("alipay.antocean.ocean.h5.queryCultivationDetail",
                "[{\"cultivationCode\":\"" + cultivationCode + "\",\"projectCode\":\"" + projectCode
                        + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + getUniqueId() + "\"}]");
    }

    public static String oceanExchangeTree(String cultivationCode, String projectCode) {
        return RpcUtil.request("alipay.antocean.ocean.h5.exchangeTree",
                "[{\"cultivationCode\":\"" + cultivationCode + "\",\"projectCode\":\"" + projectCode
                        + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + getUniqueId() + "\"}]");
    }

}
