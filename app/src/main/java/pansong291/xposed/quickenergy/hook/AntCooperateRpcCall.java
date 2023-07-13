package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.Log;

public class AntCooperateRpcCall
{
 private static final String TAG = AntCooperateRpcCall.class.getCanonicalName();

 public static String rpcCall_queryUserCooperatePlantList(ClassLoader loader)
 {
  try
  {
   String args1 = "[{}]";
   return RpcCall.invoke(loader, "alipay.antmember.forest.h5.queryUserCooperatePlantList", args1);
  }catch(Throwable t)
  {
   Log.i(TAG, "rpcCall_queryUserCooperatePlantList err:");
   Log.printStackTrace(TAG, t);
  }
  return null;
 }

 public static String rpcCall_queryCooperatePlant(ClassLoader loader, String coopId)
 {
  try
  {
   String args1 = "[{\"cooperationId\":\"" + coopId + "\"}]";
   return RpcCall.invoke(loader, "alipay.antmember.forest.h5.queryCooperatePlant", args1);
  }catch(Throwable t)
  {
   Log.i(TAG, "rpcCall_queryCooperatePlant err:");
   Log.printStackTrace(TAG, t);
  }
  return null;
 }

 public static String rpcCall_cooperateWater(ClassLoader loader, String uid, String coopId, int count)
 {
  try
  {
   String args1 = "[{\"bizNo\":\"" + uid + "_" + coopId + "_" + System.currentTimeMillis()
    + "\",\"cooperationId\":\"" + coopId + "\",\"energyCount\":" + count + "}]";
   return RpcCall.invoke(loader, "alipay.antmember.forest.h5.cooperateWater", args1);
  }catch(Throwable t)
  {
   Log.i(TAG, "rpcCall_cooperateWater err:");
   Log.printStackTrace(TAG, t);
  }
  return null;
 }

}
