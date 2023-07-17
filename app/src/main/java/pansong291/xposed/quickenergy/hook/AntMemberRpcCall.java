package pansong291.xposed.quickenergy.hook;

import pansong291.xposed.quickenergy.util.Log;

public class AntMemberRpcCall
{
    private static final String TAG = AntMemberRpcCall.class.getCanonicalName();

    private static final String appVersion = "3.0.0";

    /* ant member point */
    public static String rpcCall_queryPointCert(ClassLoader loader, int page, int pageSize)
    {
        try
        {
            String args1 = "[{\"page\":" + page + ",\"pageSize\":" + pageSize + "}]";
            return RpcUtil.request("alipay.antmember.biz.rpc.member.h5.queryPointCert", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_queryPointCert err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_receivePointByUser(ClassLoader loader, String certId)
    {
        try
        {
            String args1 = "[{\"certId\":" + certId + "}]";
            return RpcUtil.request("alipay.antmember.biz.rpc.member.h5.receivePointByUser", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_receivePointByUser err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_memberSignin(ClassLoader loader)
    {
        try
        {
            String args1 = "[{}]";
            return RpcUtil.request("alipay.antmember.biz.rpc.member.h5.memberSignin", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_memberSignin err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    /* family point*/
    public static String rpcCall_familySignin(ClassLoader loader)
    {
        try
        {
            String args1 = "[{\"appVersion\": \"" + appVersion +
                    "\",\"clientTraceId\": \"\",\"source\": \"JTHYJGW\"}]";
            return RpcUtil.request("alipay.peerpayprod.family.signin", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_familySignin err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_familyHomepage(ClassLoader loader)
    {
        try
        {
            String args1 = "[{\"appVersion\": \"" + appVersion +
                    "\",\"clientTraceId\": \"\",\"source\": \"JTHYJGW\"}]";
            return RpcUtil.request("alipay.peerpayprod.family.homepage", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_familyHomepage err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_queryFamilyPointCert(ClassLoader loader, String familyId)
    {
        try
        {
            String args1 = "[{\"familyId\":\"" + familyId +
                    "\",\"limit\":20,\"needQueryOtherMemberCert\":false}]";
            return RpcUtil.request("com.alipay.alipaymember.biz.rpc.family.h5.queryFamilyPointCert", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_queryFamilyPointCert err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    public static String rpcCall_claimFamilyPointCert(ClassLoader loader, long certId, String familyId)
    {
        try
        {
            String args1 = "[{\"certId\":" + certId + ",\"familyId\":\""
                    + familyId + "\"}]";
            return RpcUtil.request("com.alipay.alipaymember.biz.rpc.family.h5.claimFamilyPointCert", args1);
        }catch(Throwable t)
        {
            Log.i(TAG, "rpcCall_claimFamilyPointCert err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

}
