package pansong291.xposed.quickenergy.hook;

import de.robv.android.xposed.XposedHelpers;
import org.json.JSONException;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.util.*;

import java.util.Calendar;
import java.util.List;

public class FriendManager {
    private static final String TAG = FriendManager.class.getCanonicalName();

    public static void fillUser(ClassLoader loader) {
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
                        for (Object friend : allFriends) {
                            String userId = (String) XposedHelpers.findField(friend.getClass(), "userId").get(friend);
                            String account = (String) XposedHelpers.findField(friend.getClass(), "account").get(friend);
                            String name = (String)XposedHelpers.findField(friend.getClass(), "name").get(friend);
                            String nickName = (String)XposedHelpers.findField(friend.getClass(), "nickName").get(friend);
                            String remarkName = (String)XposedHelpers.findField(friend.getClass(), "remarkName").get(friend);
                            if (StringUtil.isEmpty(remarkName)) {
                                remarkName = nickName;
                            }
                            remarkName += "|" + name;
                            FriendIdMap.putIdMap(userId, remarkName + "(" + account + ")");
                        }
                        FriendIdMap.saveIdMap();
                    } catch (Throwable t) {
                        Log.i(TAG, "checkUnknownId.run err:");
                        Log.printStackTrace(TAG, t);
                    }
                }
            }.setData(loader, unknownIds).start();
        }
    }

    public static boolean needUpdateAll(long last) {
        if (last == 0L) {
            return true;
        }
        Calendar cLast = Calendar.getInstance();
        cLast.setTimeInMillis(last);
        Calendar cNow = Calendar.getInstance();
        if (cLast.get(Calendar.DAY_OF_YEAR) == cNow.get(Calendar.DAY_OF_YEAR)) {
            return false;
        }
        return cNow.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
    }

    private static JSONObject joFriendWatch;

    public static void friendWatch(String id, int collectedEnergy) {
        if (id.equals(FriendIdMap.currentUid)) {
            return;
        }
        try {
            if (joFriendWatch == null) {
                String strFriendWatch = FileUtils.readFromFile(FileUtils.getFriendWatchFile());
                if (!"".equals(strFriendWatch)) {
                    joFriendWatch = new JSONObject(strFriendWatch);
                } else {
                    joFriendWatch = new JSONObject();
                }
            }
            if (needUpdateAll(FileUtils.getFriendWatchFile().lastModified())) {
                friendWatchNewWeek();
            }
            friendWatchSingle(id, collectedEnergy);
        } catch (Throwable th) {
            Log.i(TAG, "friendWatch err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void friendWatchSingle(String id, int collectedEnergy) throws JSONException {
        JSONObject joSingle = joFriendWatch.getJSONObject(id);
        joSingle.put("weekGet", joSingle.optInt("weekGet", 0) + collectedEnergy);
        FileUtils.write2File(joFriendWatch.toString(), FileUtils.getFriendWatchFile());
    }

    private static void friendWatchNewWeek() {
        JSONObject joSingle;
        try {
            String dateStr = TimeUtil.getDateStr();
            List<String> friendIds = FriendIdMap.getFriendIds();
            for (String id : friendIds) {
                if (joFriendWatch.has(id)) {
                    joSingle = joFriendWatch.getJSONObject(id);
                } else {
                    joSingle = new JSONObject();
                }
                joSingle.put("name", FriendIdMap.getNameById(id));
                joSingle.put("allGet", joSingle.optInt("allGet", 0) + joSingle.optInt("weekGet", 0));
                joSingle.put("weekGet", 0);
                if (joSingle.has("startTime")) {
                    joSingle.put("startTime", dateStr);
                }
                joFriendWatch.put(id, joSingle);
            }
            FileUtils.write2File(joFriendWatch.toString(), FileUtils.getFriendWatchFile());
        } catch (Throwable th) {
            Log.i(TAG, "friendWatchNewWeek err:");
            Log.printStackTrace(TAG, th);
        }
    }
}
