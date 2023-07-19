package pansong291.xposed.quickenergy.hook;

public class AntCooperateRpcCall
{

    public static String queryUserCooperatePlantList() {
        return RpcUtil.request("alipay.antmember.forest.h5.queryUserCooperatePlantList", "[{}]");
    }

    public static String queryCooperatePlant(String coopId) {
        String args1 = "[{\"cooperationId\":\"" + coopId + "\"}]";
        return RpcUtil.request("alipay.antmember.forest.h5.queryCooperatePlant", args1);
    }

    public static String cooperateWater(String uid, String coopId, int count) {
        String args1 = "[{\"bizNo\":\"" + uid + "_" + coopId + "_" + System.currentTimeMillis()
                + "\",\"cooperationId\":\"" + coopId + "\",\"energyCount\":" + count + "}]";
        return RpcUtil.request("alipay.antmember.forest.h5.cooperateWater", args1);
    }

}
