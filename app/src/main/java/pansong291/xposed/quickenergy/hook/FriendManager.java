package pansong291.xposed.quickenergy.hook;

import de.robv.android.xposed.XposedHelpers;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.StringUtil;

import java.util.List;

public class FriendManager {
    private static final String TAG = FriendManager.class.getCanonicalName();
    public static void checkUnknownId(ClassLoader loader) {
        List<String> unknownIds = FriendIdMap.getIncompleteUnknownIds();
        if (unknownIds.size() > 0) {
            new Thread() {
                ClassLoader loader;
                List<String> unknownIds;

                public Thread setData(ClassLoader cl, List<String> ss) {
                    loader = cl;
                    unknownIds = ss;
                    return this;
                }

                @Override
                public void run() {
                    try {
                        Class<?> clsUserIndependentCache = loader.loadClass("com.alipay.mobile.socialcommonsdk.bizdata.UserIndependentCache");
                        Class<?> clsAliAccountDaoOp = loader.loadClass("com.alipay.mobile.socialcommonsdk.bizdata.contact.data.AliAccountDaoOp");
                        Object aliAccountDaoOp = XposedHelpers.callStaticMethod(clsUserIndependentCache, "getCacheObj", clsAliAccountDaoOp);
                        List<?> allFriends = (List<?>) XposedHelpers.callMethod(aliAccountDaoOp, "getAllFriends", new Object[0]);
                        Log.i(TAG, "开始检查" + unknownIds.size() + "个未知id");
                        for (Object friend : allFriends) {
                            String userId = (String) XposedHelpers.findField(friend.getClass(), "userId").get(friend);
                            if (!unknownIds.contains(userId)) {
                                continue;
                            }
                            String account = (String) XposedHelpers.findField(friend.getClass(), "account").get(friend);
                            String name = (String)XposedHelpers.findField(friend.getClass(), "name").get(friend);
                            String nickName = (String)XposedHelpers.findField(friend.getClass(), "nickName").get(friend);
                            String remarkName = (String)XposedHelpers.findField(friend.getClass(), "remarkName").get(friend);
                            if (StringUtil.isEmpty(remarkName)) {
                                remarkName = nickName;
                            }
                            FriendIdMap.putIdMap(userId, remarkName + "|" + name + "(" + account + ")");
                            FriendIdMap.saveIdMap();
                        }
                    } catch (Throwable t) {
                        Log.i(TAG, "checkUnknownId.run err:");
                        Log.printStackTrace(TAG, t);
                    }
                }
            }.setData(loader, unknownIds).start();
        }
    }
}
