package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.RandomUtils;

/**
 * @author Constanline
 * @since 2023/08/01
 */
public class AntOceanRpcCall {
    private static final String VERSION = "20220707";
    private static String getUniqueId() {
        return String.valueOf(System.currentTimeMillis()) + RandomUtils.nextLong();
    }

    public static String queryOceanStatus() {
        return RpcUtil.request("alipay.antocean.ocean.h5.queryOceanStatus",
                "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }

    public static String queryHomePage() {
        return RpcUtil.request("alipay.antocean.ocean.h5.queryHomePage",
                "[{\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + getUniqueId() + "\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String cleanOcean(String userId) {
        return RpcUtil.request("alipay.antocean.ocean.h5.cleanOcean",
                "[{\"cleanedUserId\":\"" + userId + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + getUniqueId() + "\"}]");
    }
}
