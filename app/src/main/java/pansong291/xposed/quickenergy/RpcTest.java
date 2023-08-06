package pansong291.xposed.quickenergy;

import pansong291.xposed.quickenergy.hook.RpcUtil;

/**
 * @author Constanline
 * @since 2023/07/26
 */
public class RpcTest {
    public static void start(String broadcastFun, String broadcastData) {
        new Thread() {
            String broadcastFun;
            String broadcastData;

            public Thread setData(String fun, String data) {
                broadcastFun = fun;
                broadcastData = data;
                return this;
            }

            @Override
            public void run() {
                test(broadcastFun, broadcastData);
            }
        }.setData(broadcastFun, broadcastData).start();
    }

    private static void test(String fun, String data) {
       String s= RpcUtil.request(fun,data);
    }
}