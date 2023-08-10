package pansong291.xposed.quickenergy.entity;

import org.json.JSONObject;
import pansong291.xposed.quickenergy.util.FileUtils;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.Log;
import pansong291.xposed.quickenergy.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Constanline
 * @since 2023/08/08
 */
public class FriendWatch extends IdAndName {
    private static final String TAG = FriendWatch.class.getCanonicalName();

    public String startTime;

    public int allGet;

    public int weekGet;

    public FriendWatch(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<FriendWatch> getList() {
        ArrayList<FriendWatch> list = new ArrayList<>();
        String strFriendWatch = FileUtils.readFromFile(FileUtils.getFriendWatchFile());
        try {
            JSONObject joFriendWatch;
            if (StringUtil.isEmpty(strFriendWatch)) {
                joFriendWatch = new JSONObject();
            } else {
                joFriendWatch = new JSONObject(strFriendWatch);
            }
            for (String id : FriendIdMap.getFriendIds()) {
                JSONObject friend = joFriendWatch.optJSONObject(id);
                if (friend == null) {
                    friend = new JSONObject();
                }
                String name = FriendIdMap.getNameById(id);
                FriendWatch friendWatch = new FriendWatch(id, name);
                friendWatch.startTime = friend.optString("startTime", "无");
                friendWatch.weekGet = friend.optInt("weekGet" ,0);
                friendWatch.allGet = friend.optInt("allGet",0) + friendWatch.weekGet;
                String showText = name + "(开始统计时间:" + friendWatch.startTime + ")\n\n";
                showText = showText + "周收:" + friendWatch.weekGet + " 总收:" + friendWatch.allGet;
                friendWatch.name = showText;
                list.add(friendWatch);
            }
        } catch (Throwable t) {
            Log.i(TAG, "FriendWatch getList: ");
            Log.printStackTrace(TAG, t);
        }

        return list;
    }
}