package pansong291.xposed.quickenergy;

import de.robv.android.xposed.XposedHelpers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.AntFarm.TaskStatus;
import pansong291.xposed.quickenergy.hook.AntForestRpcCall;
import pansong291.xposed.quickenergy.util.*;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 蚂蚁森林
 * @author Constanline
 */
public class AntForest {
    private static final String TAG = AntForest.class.getCanonicalName();
    private static String selfId;
    private static int collectedEnergy = 0;
    private static int helpCollectedEnergy = 0;
    private static int totalCollected = 0;
    private static int totalHelpCollected = 0;
    private static int collectTaskCount = 0;
    private static long serverTime = -1;
    private static long offsetTime = -1;
    private static long laterTime = -1;

    private static boolean isScanning = false;

    /**
     * 记录收集能量时间戳的队列
     */
    private static final Queue<Long> collectedQueue = new ArrayDeque<>();

    private static final Lock limitLock = new ReentrantLock();

    private static final Lock collectLock = new ReentrantLock();

    private static volatile long lastCollectTime = 0;

    private static volatile long doubleEndTime = 0;

    private static final HashSet<Long> waitCollectBubbleIds = new HashSet<>();

    /**
     * 检查是否到达一分钟内收取限制
     *
     * <p>如果启用一分钟收取限制 {@link pansong291.xposed.quickenergy.util.Config#isLimitCollect}，
     * 则清理 {@link #collectedQueue} 中超过1分钟的项，之后检查剩余条目是否多余一分钟收取限制数量
     * {@link pansong291.xposed.quickenergy.util.Config#getLimitCount}。
     *
     * @return  如果到达上限，则返回True，否则返回False
     */
    private static boolean checkCollectLimited() {
        if (Config.isLimitCollect()) {
            limitLock.lock();
            try {
                Long ts;
                long dropTime = System.currentTimeMillis() - 60000;
                while ((ts = collectedQueue.peek()) != null && ts < dropTime) {
                    collectedQueue.poll();
                }
                return collectedQueue.size() >= Config.getLimitCount();
            } finally {
                limitLock.unlock();
            }
        }
        return false;
    }

    private static void offerCollectQueue() {
        if (Config.isLimitCollect()) {
            limitLock.lock();
            collectedQueue.offer(System.currentTimeMillis());
            limitLock.unlock();
        }
    }

    /**
     * Check energy ranking.
     *
     * @param loader the loader
     * @param times  the times
     */
    public static void checkEnergyRanking(ClassLoader loader, int times) {
        if (Config.forestPauseTime() > System.currentTimeMillis()) {
            Log.recordLog("异常等待中，暂不执行检测！", "");
            return;
        }
        if (isScanning) {
            Log.recordLog("之前的检测未结束，本次暂停", "");
            return;
        } else {
            Log.recordLog("定时检测开始", "");
            isScanning = true;
        }
        new Thread() {
            ClassLoader loader;
            int times;

            public Thread setData(ClassLoader cl, int i) {
                loader = cl;
                times = i;
                return this;
            }

            @Override
            public void run() {
                canCollectSelfEnergy(loader, times);
                queryEnergyRanking(loader);
                isScanning = false;
                if (TimeUtil.getTimeStr().compareTo("0700") < 0 || TimeUtil.getTimeStr().compareTo("0730") > 0) {
                    popupTask();
                    if (Config.energyRain()) {
                        energyRain();
                    }
                    for (int i = 0; i < Config.getWaterFriendList().size(); i++) {
                        String uid = Config.getWaterFriendList().get(i);
                        if (selfId.equals(uid)) continue;
                        int waterCount = Config.getWaterCountList().get(i);
                        if (waterCount <= 0) continue;
                        if (waterCount > 3) waterCount = 3;
                        if (Statistics.canWaterFriendToday(uid, waterCount)) {
                            waterFriendEnergy(uid, waterCount);
                        }
                    }
                    if (Statistics.canSyncStepToday() && TimeUtil.getTimeStr().compareTo("0800") >= 0) {
                        new StepTask(loader).start();
                    }
                }
            }
        }.setData(loader, times).start();
    }

    private static void fillUserRobFlag(ClassLoader loader, List<String> idList) {
        if (Config.forestPauseTime() > System.currentTimeMillis()) {
            return;
        }
        try {
            String strList = new JSONArray(idList).toString();
            String s = AntForestRpcCall.fillUserRobFlag(strList);
            JSONObject jo = new JSONObject(s);
            checkCanCollectEnergy(loader, jo);
            Thread.sleep(500);
        } catch (Throwable t) {
            Log.i(TAG, "fillUserRobFlag err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void popupTask() {
        try {
            JSONObject resData = new JSONObject(AntForestRpcCall.popupTask());
            if ("SUCCESS".equals(resData.getString("resultCode"))) {
                JSONArray forestSignVOList = resData.optJSONArray("forestSignVOList");
                if (forestSignVOList != null) {
                    for (int i = 0; i < forestSignVOList.length(); i++) {
                        JSONObject forestSignVO = forestSignVOList.getJSONObject(i);
                        String signId = forestSignVO.getString("signId");
                        String currentSignKey = forestSignVO.getString("currentSignKey");
                        JSONArray signRecords = forestSignVO.getJSONArray("signRecords");
                        for (int j = 0; j < signRecords.length(); j++) {
                            JSONObject signRecord = signRecords.getJSONObject(j);
                            String signKey = signRecord.getString("signKey");
                            if (signKey.equals(currentSignKey)) {
                                if (!signRecord.getBoolean("signed")) {
                                    int awardCount = signRecord.getInt("awardCount");
                                    JSONObject resData2 = new JSONObject(AntForestRpcCall.antiepSign(signId, FriendIdMap.currentUid));
                                    if ("100000000".equals(resData2.getString("code"))) {
                                        collectedEnergy += awardCount;
                                        Log.forest("收取过期能量［" + awardCount + "克");
                                        onForestEnd();
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            } else {
                Log.recordLog(resData.getString("resultDesc"), resData.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "popupTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void queryEnergyRanking(ClassLoader loader) {
        if (!Config.collectEnergy()) {
            return;
        }
        try {
            String s = AntForestRpcCall.queryEnergyRanking();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                checkCanCollectEnergy(loader, jo);
                int pos = 20;
                List<String> idList = new ArrayList<>();
                JSONArray totalDatas = jo.getJSONArray("totalDatas");
                while (pos < totalDatas.length()) {
                    JSONObject friend = totalDatas.getJSONObject(pos);
                    idList.add(friend.getString("userId"));
                    pos++;
                    if (pos % 20 == 0) {
                        fillUserRobFlag(loader, idList);
                        idList.clear();
                    }
                }
                if (idList.size() > 0) {
                    fillUserRobFlag(loader, idList);
                }
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryEnergyRanking err:");
            Log.printStackTrace(TAG, t);
        }
        onForestEnd();
    }

    private static void checkCanCollectEnergy(ClassLoader loader, JSONObject jo) throws JSONException {
        JSONArray jaFriendRanking = jo.getJSONArray("friendRanking");
        for (int i = 0; i < jaFriendRanking.length(); i++) {
            jo = jaFriendRanking.getJSONObject(i);
            boolean optBoolean = jo.getBoolean("canCollectEnergy") || jo.getBoolean("canHelpCollect")
                    || (jo.getLong("canCollectLaterTime") > 0 && jo.getLong("canCollectLaterTime") - System.currentTimeMillis() < Config.checkInterval());
            String userId = jo.getString("userId");
            if (optBoolean && !userId.equals(selfId)) {
                canCollectEnergy(loader, userId, true);
            } else {
                FriendIdMap.getNameById(userId);
            }
        }
    }

    private static void canCollectSelfEnergy(ClassLoader loader, int times) {
        try {
            long start = System.currentTimeMillis();
            String s = AntForestRpcCall.queryHomePage();
            long end = System.currentTimeMillis();
            if (s == null) {
                Thread.sleep(RandomUtils.delay());
                start = System.currentTimeMillis();
                s = AntForestRpcCall.queryHomePage();
                end = System.currentTimeMillis();
            }
            JSONObject joHomePage = new JSONObject(s);
            if (joHomePage.getString("resultCode").equals("SUCCESS")) {
                serverTime = joHomePage.getLong("now");
                offsetTime = (start + end) / 2 - serverTime;
                Log.i(TAG, "服务器时间：" + serverTime + "，本地减服务器时间差：" + offsetTime);

                JSONArray loginUserUsingPropNew = joHomePage.getJSONArray("loginUserUsingPropNew");
                for (int i = 0; i < loginUserUsingPropNew.length(); i++) {
                    JSONObject userUsingProp = loginUserUsingPropNew.getJSONObject(i);
                    String propType = userUsingProp.getString("propType");
                    if ("ENERGY_DOUBLE_CLICK".equals(propType) || "LIMIT_TIME_ENERGY_DOUBLE_CLICK".equals(propType)) {
                        doubleEndTime = userUsingProp.getLong("endTime");
                        Log.forest("双倍卡剩余时间" + (doubleEndTime - System.currentTimeMillis()) / 1000);
                    }
                }
                JSONArray jaBubbles = joHomePage.getJSONArray("bubbles");
                JSONObject userEnergy = joHomePage.getJSONObject("userEnergy");
                selfId = userEnergy.getString("userId");
                FriendIdMap.currentUid = selfId;
                String selfName = userEnergy.getString("displayName");
                if (selfName.isEmpty())
                    selfName = "我";
                FriendIdMap.putIdMapIfEmpty(selfId, selfName);
                FriendIdMap.saveIdMap();

                if (Config.collectEnergy()) {
                    Log.recordLog("进入【" + selfName + "】的蚂蚁森林", "");
                    for (int i = 0; i < jaBubbles.length(); i++) {
                        JSONObject bubble = jaBubbles.getJSONObject(i);
                        long bubbleId = bubble.getLong("id");
                        switch (CollectStatus.valueOf(bubble.getString("collectStatus"))) {
                            case AVAILABLE:
                                if (Config.getDontCollectList().contains(selfId))
                                    Log.recordLog("不偷取【" + selfName + "】", ", userId=" + selfId);
                                else
                                    collectedEnergy += collectEnergy(selfId, bubbleId, selfName, null);
                                break;

                            case WAITING:
                                if (Config.getDontCollectList().contains(selfId))
                                    break;
                                long produceTime = bubble.getLong("produceTime");
                                if (produceTime - serverTime < Config.checkInterval())
                                    execute(loader, selfName, selfId, null, bubbleId, produceTime);
                                else
                                    setLaterTime(produceTime);
                                break;
                        }
                    }
                }
                if (Config.collectWateringBubble()) {
                    JSONArray wateringBubbles = joHomePage.has("wateringBubbles") ? joHomePage.getJSONArray("wateringBubbles") : new JSONArray();
                    if (wateringBubbles.length() > 0) {
                        int collected = 0;
                        for (int i = 0; i < wateringBubbles.length(); i++) {
                            String str = AntForestRpcCall.collectEnergy("jiaoshui", selfId,
                                    wateringBubbles.getJSONObject(i).getLong("id"));
                            JSONObject joEnergy = new JSONObject(str);
                            if ("SUCCESS".equals(joEnergy.getString("resultCode"))) {
                                JSONArray bubbles = joEnergy.getJSONArray("bubbles");
                                for (int j = 0; j < bubbles.length(); j++) {
                                    collected = bubbles.getJSONObject(j).getInt("collectedEnergy");
                                }
                                if (collected > 0) {
                                    totalCollected += collected;
                                    Statistics.addData(Statistics.DataType.COLLECTED, collected);
                                    String msg = "收取【我】的金球【" + collected + "克】";
                                    Log.forest(msg);
                                    AntForestToast.show(msg);
                                } else {
                                    Log.recordLog("收取【我】的金球失败", "");
                                }
                            } else {
                                Log.recordLog("收取【我】的金球失败:" + joEnergy.getString("resultDesc"), str);
                            }
                            Thread.sleep(1000L);
                        }
                    }
                }
            } else {
                Log.recordLog(joHomePage.getString("resultDesc"), s);
            }
            if (times == 0) {
                receiveTaskAward();
            }
        } catch (Throwable t) {
            Log.i(TAG, "canCollectSelfEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        onForestEnd();
    }

    private static int getEnergyId(int waterEnergy) {
        if (waterEnergy <= 0) return 0;
        if (waterEnergy >= 66) return 42;
        if (waterEnergy >= 33) return 41;
        if (waterEnergy <= 18) return 40;
        return 39;
    }

    private static void canCollectEnergy(ClassLoader loader, String userId, boolean laterCollect) {
        if (Config.forestPauseTime() > System.currentTimeMillis()) {
            Log.recordLog("异常等待中，暂不执行检测！", "");
            return;
        }
        try {
            long start = System.currentTimeMillis();
            String s = AntForestRpcCall.queryFriendHomePage(userId);
            long end = System.currentTimeMillis();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                serverTime = jo.getLong("now");
                offsetTime = (start + end) / 2 - serverTime;
                Log.i(TAG, "服务器时间：" + serverTime + "，本地减服务器时间差：" + offsetTime);
                String bizNo = jo.getString("bizNo");
                JSONArray jaBubbles = jo.getJSONArray("bubbles");
                JSONObject userEnergy = jo.getJSONObject("userEnergy");
                String userName = userEnergy.getString("displayName");
                if (userName.isEmpty())
                    userName = "*null*";
                String loginId = userName;
                if (userEnergy.has("loginId"))
                    loginId += "(" + userEnergy.getString("loginId") + ")";
                FriendIdMap.putIdMapIfEmpty(userId, loginId);
                Log.recordLog("进入【" + loginId + "】的蚂蚁森林", "");
                FriendIdMap.saveIdMap();
                JSONArray jaProps = jo.optJSONArray("usingUserProps");
                if (jaProps != null) {
                    for (int i = 0; i < jaProps.length(); i++) {
                        JSONObject joProps = jaProps.getJSONObject(i);
                        if ("energyShield".equals(joProps.getString("type"))) {
                            if (joProps.getLong("endTime") > serverTime) {
                                Log.recordLog("【" + userName + "】被能量罩保护着哟", "");
                                return;
                            }
                        }
                    }
                }
                int collected = 0;
                int helped = 0;
                for (int i = 0; i < jaBubbles.length(); i++) {
                    JSONObject bubble = jaBubbles.getJSONObject(i);
                    long bubbleId = bubble.getLong("id");
                    switch (CollectStatus.valueOf(bubble.getString("collectStatus"))) {
                        case AVAILABLE:
                            if (Config.getDontCollectList().contains(userId))
                                Log.recordLog("不偷取【" + userName + "】", ", userId=" + userId);
                            else
                                collected += collectEnergy(userId, bubbleId, userName, bizNo);
                            break;

                        case WAITING:
                            if (!laterCollect || Config.getDontCollectList().contains(userId))
                                break;
                            long produceTime = bubble.getLong("produceTime");
                            if (produceTime - serverTime < Config.checkInterval())
                                execute(loader, userName, userId, bizNo, bubbleId, produceTime);
                            else
                                setLaterTime(produceTime);
                            break;
                    }
                    if (bubble.getBoolean("canHelpCollect")) {
                        if (Config.helpFriendCollect()) {
                            if (Config.getDontHelpCollectList().contains(userId))
                                Log.recordLog("不帮收【" + userName + "】", ", userId=" + userId);
                            else
                                helped += forFriendCollectEnergy(userId, bubbleId, userName);
                        } else
                            Log.recordLog("不帮收【" + userName + "】", ", userId=" + userId);
                    }
                }
                if (helped > 0) {
                    canCollectEnergy(loader, userId, false);
                }
                collectedEnergy += collected;
                onForestEnd();
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "canCollectEnergy err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static int collectEnergy(String userId, long bubbleId, String userName, String bizNo) {
        return collectEnergy(userId, bubbleId, userName, bizNo, null);
    }

    private static int collectEnergy(String userId, long bubbleId, String userName, String bizNo, String extra) {
        if (Config.forestPauseTime() > System.currentTimeMillis()) {
            Log.recordLog("异常等待中，暂不执行检测！", "");
            return 0;
        }
        int collected = 0;
        try {
            while (checkCollectLimited()) {
                Thread.sleep(1000);
            }
        } catch (Throwable th) {
            Log.printStackTrace("到达分钟限制，等待失败！", th);
            return 0;
        }
//        if (checkCollectLimited()) {
//            return 0;
//        }
        try {
            String s = "{\"resultCode\": \"FAILED\"}";
            if (Config.collectInterval() > 0) {
                synchronized (collectLock) {
                    while (System.currentTimeMillis() - lastCollectTime < Config.collectInterval()) {
                        Thread.sleep(System.currentTimeMillis() - lastCollectTime);
                    }
                    if (Config.forestPauseTime() > System.currentTimeMillis()) {
                        Log.recordLog("异常等待中，暂不执行检测！", "");
                        return 0;
                    }
                    if (Config.doubleCard() && doubleEndTime < System.currentTimeMillis()) {
                        if (Config.isDoubleCardTime()) {
                            useDoubleCard();
                        }
                    }
                    s = AntForestRpcCall.collectEnergy(null, userId, bubbleId);
                    lastCollectTime = System.currentTimeMillis();
                }
            }
            waitCollectBubbleIds.remove(bubbleId);
            JSONObject jo = new JSONObject(s);
            if (jo.getString("resultCode").equals("SUCCESS")) {
                offerCollectQueue();
                JSONArray jaBubbles = jo.getJSONArray("bubbles");
                jo = jaBubbles.getJSONObject(0);
                collected += jo.getInt("collectedEnergy");
                if (collected > 0) {
                    totalCollected += collected;
                    Statistics.addData(Statistics.DataType.COLLECTED, collected);
                    String str = "偷取【" + userName + "】的能量【" + collected + "克】" + (StringUtil.isEmpty(extra) ? "" : "【" + extra + "】");
                    Log.forest(str);
                    AntForestToast.show(str);
                } else {
                    Log.recordLog("偷取【" + userName + "】的能量失败", "，UserID：" + userId + "，BubbleId：" + bubbleId);
                }
                if (jo.getBoolean("canBeRobbedAgain")) {
                    collected += collectEnergy(userId, bubbleId, userName, null, "双击卡");
                }
                if (bizNo == null || bizNo.isEmpty())
                    return collected;
                int returnCount = 0;
                if (Config.returnWater33() > 0 && collected >= Config.returnWater33())
                    returnCount = 33;
                else if (Config.returnWater18() > 0 && collected >= Config.returnWater18())
                    returnCount = 18;
                else if (Config.returnWater10() > 0 && collected >= Config.returnWater10())
                    returnCount = 10;
                if (returnCount > 0)
                    returnFriendWater(userId, userName, bizNo, 1, returnCount);
            } else {
                Log.recordLog("【" + userName + "】" + jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        return collected;
    }

    private static int forFriendCollectEnergy(String targetUserId, long bubbleId, String userName) {
        int helped = 0;
        try {
            String s = AntForestRpcCall.forFriendCollectEnergy(targetUserId, bubbleId);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray jaBubbles = jo.getJSONArray("bubbles");
                for (int i = 0; i < jaBubbles.length(); i++) {
                    jo = jaBubbles.getJSONObject(i);
                    helped += jo.getInt("collectedEnergy");
                }
                if (helped > 0) {
                    Log.forest("帮【" + userName + "】收取【" + helped + "克】");
                    helpCollectedEnergy += helped;
                    totalHelpCollected += helped;
                    Statistics.addData(Statistics.DataType.HELPED, helped);
                } else {
                    Log.recordLog("帮【" + userName + "】收取失败", "，UserID：" + targetUserId + "，BubbleId" + bubbleId);
                }
            } else {
                Log.recordLog("【" + userName + "】" + jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "forFriendCollectEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        onForestEnd();
        return helped;
    }

    private static void waterFriendEnergy(String userId, int count) {
        try {
            String s = AntForestRpcCall.queryFriendHomePage(userId);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                String bizNo = jo.getString("bizNo");
                jo = jo.getJSONObject("userEnergy");
                String userName = jo.getString("displayName");
                Log.recordLog("尝试对【" + userName + "】浇水66g" + count + "次【" + bizNo + "】");
                count = returnFriendWater(userId, userName, bizNo, count, 66);
                if (count > 0) Statistics.waterFriendToday(userId, count);
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "waterFriendEnergy err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static int returnFriendWater(String userId, String userName, String bizNo, int count, int waterEnergy) {
        if (bizNo == null || bizNo.isEmpty()) return 0;
        int wateredTimes = 0;
        try {
            String s;
            JSONObject jo;
            int energyId = getEnergyId(waterEnergy);
            for (int waterCount = 1; waterCount <= count; waterCount++) {
                s = AntForestRpcCall.transferEnergy(userId, bizNo, energyId);
                jo = new JSONObject(s);
                if ("SUCCESS".equals(jo.getString("resultCode"))) {
                    String currentEnergy = jo.getJSONObject("treeEnergy").getString("currentEnergy");
                    Log.forest("给【" + userName + "】浇水成功，剩余能量【" + currentEnergy + "克】");
                    wateredTimes++;
                    Statistics.addData(Statistics.DataType.WATERED, waterEnergy);
                } else if ("WATERING_TIMES_LIMIT".equals(s)) {
                    Log.recordLog("今日给【" + userName + "】浇水已达上限", "");
                    wateredTimes = 3;
                    break;
                } else {
                    Log.recordLog(jo.getString("resultDesc"), jo.toString());
                }
                Thread.sleep(2000);
            }
        } catch (Throwable t) {
            Log.i(TAG, "returnFriendWater err:");
            Log.printStackTrace(TAG, t);
        }
        return wateredTimes;
    }

    private static void receiveTaskAward() {
        try {
            String s = AntForestRpcCall.queryTaskList();
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray forestSignVOList = jo.getJSONArray("forestSignVOList");
                JSONObject forestSignVO = forestSignVOList.getJSONObject(0);
                String currentSignKey = forestSignVO.getString("currentSignKey");
                JSONArray signRecords = forestSignVO.getJSONArray("signRecords");
                for (int i = 0; i < signRecords.length(); i++) {
                    JSONObject signRecord = signRecords.getJSONObject(i);
                    String signKey = signRecord.getString("signKey");
                    if (signKey.equals(currentSignKey)) {
                        if (!signRecord.getBoolean("signed")) {
                            jo = new JSONObject(AntForestRpcCall.vitalitySign());
                            if ("SUCCESS".equals(jo.getString("desc")))
                                Log.forest("签到成功");
                        }
                        break;
                    }
                }
                JSONArray forestTasksNew = jo.getJSONArray("forestTasksNew");
                for (int i = 0; i < forestTasksNew.length(); i++) {
                    JSONObject forestTask = forestTasksNew.getJSONObject(i);
                    JSONArray taskInfoList = forestTask.getJSONArray("taskInfoList");
                    for (int j = 0; j < taskInfoList.length(); j++) {
                        JSONObject taskInfo = taskInfoList.getJSONObject(j);
                        JSONObject taskBaseInfo = taskInfo.getJSONObject("taskBaseInfo");
                        JSONObject bizInfo = new JSONObject(taskBaseInfo.getString("bizInfo"));
                        String taskType = taskBaseInfo.getString("taskType");
                        String awardName = bizInfo.optString("awardName", taskType);
                        String awardCount = bizInfo.optString("awardCount", "1");
                        String sceneCode = taskBaseInfo.getString("sceneCode");
                        if (TaskStatus.FINISHED.name().equals(taskBaseInfo.getString("taskStatus"))) {
                            JSONObject joTaskAward = new JSONObject(AntForestRpcCall.receiveTaskAward(sceneCode, taskType));
                            if ("SUCCESS".equals(joTaskAward.getString("desc")))
                                Log.forest("已领取【" + awardCount + "个】【" + awardName + "】");
                            else
                                Log.recordLog("领取失败，" + s, forestTask.toString());
                        }
                    }
                }
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void startEnergyRain() {
        try {
            JSONObject jSONObject = new JSONObject(AntForestRpcCall.startEnergyRain());
            if ("SUCCESS".equals(jSONObject.getString("resultCode"))) {
                String token = jSONObject.getString("token");
                JSONArray bubbleEnergyList = jSONObject.getJSONObject("difficultyInfo").getJSONArray("bubbleEnergyList");
                int sum = 0;
                for (int i = 0; i < bubbleEnergyList.length(); i++) {
                    sum += bubbleEnergyList.getInt(i);
                }
                Thread.sleep(5000L);
                if ("SUCCESS".equals(new JSONObject(AntForestRpcCall.energyRainSettlement(sum, token)).getString("resultCode"))) {
                    AntForestToast.show("获得了【" + sum + "g】能量【能量雨】");
                    Log.forest("获得了【" + sum + "g】能量【能量雨】");
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "startEnergyRain err:");
            Log.printStackTrace(TAG, th);
        }
    }


    private static void energyRain() {
        try {
            JSONObject joEnergyRainHome = new JSONObject(AntForestRpcCall.queryEnergyRainHome());
            if ("SUCCESS".equals(joEnergyRainHome.getString("resultCode"))) {
                if (joEnergyRainHome.getBoolean("canPlayToday")) {
                    startEnergyRain();
                }
                if (joEnergyRainHome.getBoolean("canGrantStatus")) {
                    JSONObject joEnergyRainCanGrantList = new JSONObject(AntForestRpcCall.queryEnergyRainCanGrantList());
                    JSONArray grantInfos = joEnergyRainCanGrantList.getJSONArray("grantInfos");
                    List<String> list = Config.getGiveEnergyRainList();
                    String userId = null;
                    for (int j = 0; j < grantInfos.length(); j++) {
                        JSONObject grantInfo = grantInfos.getJSONObject(j);
                        if (grantInfo.getBoolean("canGrantedStatus")) {
                            userId = grantInfo.getString("userId");
                            if (list.contains(userId)) {
                                JSONObject joEnergyRainChance = new JSONObject(AntForestRpcCall.grantEnergyRainChance(userId));
                                // 20230724能量雨调整为列表中没有可赠送的好友则不赠送
                                if ("SUCCESS".equals(joEnergyRainChance.getString("resultCode"))) {
                                    Log.forest("给【" + FriendIdMap.getNameById(userId) + "】赠送机会成功【" + FriendIdMap.getNameById(FriendIdMap.currentUid) + "】");
                                    startEnergyRain();
                                }
                                break;
                            }
                        }
                    }
//                    if (userId != null) {
//                        JSONObject joEnergyRainChance = new JSONObject(AntForestRpcCall.grantEnergyRainChance(userId));
//                        if ("SUCCESS".equals(joEnergyRainChance.getString("resultCode"))) {
//                            Log.forest("给【" + FriendIdMap.getNameById(userId) + "】赠送机会成功【" + FriendIdMap.getNameById(FriendIdMap.currentUid) + "】");
//                            startEnergyRain();
//                        }
//                    }
                }
            }
            joEnergyRainHome = new JSONObject(AntForestRpcCall.queryEnergyRainHome());
            if ("SUCCESS".equals(joEnergyRainHome.getString("resultCode")) && joEnergyRainHome.getBoolean("canPlayToday")) {
                startEnergyRain();
            }
        } catch (Throwable th) {
            Log.i(TAG, "energyRain err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void setLaterTime(long time) {
        Log.i(TAG, "能量成熟时间：" + time);
        if (time > serverTime && serverTime > 0
                && (laterTime < 0 || time < laterTime)) {
            laterTime = time;
            Log.i(TAG, laterTime - serverTime + "ms 后能量成熟");
        }
    }

    private static void onForestEnd() {
        Log.recordLog(
                "收【" + collectedEnergy + "克】，帮【"
                        + helpCollectedEnergy + "克】，"
                        + collectTaskCount + "个蹲点任务", "");
        FriendIdMap.saveIdMap();
        collectedEnergy = 0;
        helpCollectedEnergy = 0;
        if (Config.collectEnergy()) {
            StringBuilder sb = new StringBuilder();
            sb.append("  收：").append(totalCollected).append("，帮：").append(totalHelpCollected);
            if (laterTime > 0) {
                sb.append("，下个：");
                long second = (laterTime - serverTime) / 1000;
                long minute = second / 60;
                second %= 60;
                long hour = minute / 60;
                minute %= 60;
                if (hour > 0) sb.append(hour).append("时");
                if (minute > 0) sb.append(minute).append("分");
                sb.append(second).append("秒");
            }
            Log.recordLog(sb.toString(), "");
            AntForestNotification.setContentText(Log.getFormatTime() + sb);
        }
        laterTime = -1;
    }

    private static void useDoubleCard() {
        try {
            JSONObject jo = new JSONObject(AntForestRpcCall.queryPropList());
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray forestPropVOList = jo.getJSONArray("forestPropVOList");
                String propId = null;
                String propType = null;
                for (int i = 0; i < forestPropVOList.length(); i++) {
                    JSONObject forestPropVO = forestPropVOList.getJSONObject(i);
                    String tmpPropType = forestPropVO.getString("propType");
                    if ("LIMIT_TIME_ENERGY_DOUBLE_CLICK".equals(tmpPropType)) {
                        JSONArray propIdList = forestPropVO.getJSONArray("propIdList");
                        propId = propIdList.getString(0);
                        propType = tmpPropType;
                        Log.forest("检测到【限时双击卡】" + propId);
                        break;
                    }
                    if ("ENERGY_DOUBLE_CLICK".equals(tmpPropType)) {
                        JSONArray propIdList = forestPropVO.getJSONArray("propIdList");
                        propId = propIdList.getString(0);
                        propType = tmpPropType;
                        Log.forest("检测到【双击卡】" + propId);
                    }
                }
                if (!StringUtil.isEmpty(propId)) {
                    Log.forest("尝试使用【双击卡】【" + propType + "】" + propId);
                    jo = new JSONObject(AntForestRpcCall.consumeProp(propId, propType));
                    if ("SUCCESS".equals(jo.getString("resultCode"))) {
                        doubleEndTime = System.currentTimeMillis() + 1000 * 60 * 5;
                        Log.forest("使用【双击卡】成功");
                    } else {
                        Log.recordLog(jo.getString("resultDesc"), jo.toString());
                    }
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "useDoubleCard err:");
            Log.printStackTrace(TAG, th);
        }
    }

    /**
     * Execute.
     *
     * @param loader      the loader
     * @param username    the username
     * @param userId      the user id
     * @param bizNo       the biz no
     * @param bubbleId    the bubble id
     * @param produceTime the produce time
     */
    public static void execute(ClassLoader loader, String username, String userId, String bizNo, long bubbleId, long produceTime) {
        if (waitCollectBubbleIds.contains(bubbleId)) {
            return;
        }
        waitCollectBubbleIds.add(bubbleId);
        BubbleTimerTask btt = new BubbleTimerTask(loader, username, userId, bizNo, bubbleId, produceTime);
        long delay = btt.getDelayTime();
        btt.start();
        collectTaskCount++;
        Log.recordLog(delay / 1000 + "秒后尝试收取能量", "");
    }

    /**
     * The enum Collect status.
     */
    public enum CollectStatus {
        /**
         * Available collect status.
         */
        AVAILABLE,
        /**
         * Waiting collect status.
         */
        WAITING,
        /**
         * Insufficient collect status.
         */
        INSUFFICIENT,
        /**
         * Robbed collect status.
         */
        ROBBED}

    /**
     * The type Step task.
     */
    public static class StepTask extends Thread {

        /**
         * The Loader.
         */
        ClassLoader loader;

        /**
         * Instantiates a new Step task.
         *
         * @param cl the cl
         */
        public StepTask(ClassLoader cl) {
            this.loader = cl;
        }
        @Override
        public void run() {
            int step = Config.syncStepCount();
            if (step == 0) {
                return;
            }
            step = RandomUtils.nextInt(step, step + 2000);
            if (step > 100000) {
                step = 100000;
            }
            try {
                boolean booleanValue = (Boolean)
                        XposedHelpers.callMethod(
                                XposedHelpers.callStaticMethod(
                                        loader.loadClass("com.alibaba.health.pedometer.intergation.rpc.RpcManager"),
                                        "a"), "a", new Object[]{ step, Boolean.FALSE, "system" });
                if (booleanValue) {
                    Log.recordLog("记录运动步数成功:" + step, "");
                } else {
                    Log.recordLog("修改运动步数失败:" + step, "");
                }
                Statistics.SyncStepToday();
            } catch (Throwable t) {
                Log.i(TAG, "StepTask.run err:");
                Log.printStackTrace(TAG, t);
            }
        }
    }

    /**
     * The type Bubble timer task.
     */
    public static class BubbleTimerTask extends Thread {
        /**
         * The Loader.
         */
        ClassLoader loader;
        /**
         * The User name.
         */
        String userName;
        /**
         * The User id.
         */
        String userId;
        /**
         * The Biz no.
         */
        String bizNo;
        /**
         * The Bubble id.
         */
        long bubbleId;
        /**
         * The Produce time.
         */
        long produceTime;
        /**
         * The Sleep.
         */
        long sleep = 0;

        /**
         * Instantiates a new Bubble timer task.
         *
         * @param cl the cl
         * @param un the un
         * @param ui the ui
         * @param bn the bn
         * @param bi the bi
         * @param pt the pt
         */
        BubbleTimerTask(ClassLoader cl, String un, String ui, String bn, long bi, long pt) {
            loader = cl;
            userName = un;
            bizNo = bn;
            userId = ui;
            bubbleId = bi;
            produceTime = pt;
        }

        /**
         * Gets delay time.
         *
         * @return the delay time
         */
        public long getDelayTime() {
            sleep = produceTime + offsetTime - System.currentTimeMillis() - Config.advanceTime();
            return sleep;
        }

        @Override
        public void run() {
            try {
                if (sleep > 0) sleep(sleep);
                Log.recordLog("【" + userName + "】蹲点收取开始" + collectTaskCount, "");
                collectTaskCount--;
                // 20230725收取失败不再继续尝试
                collectEnergy(userId, bubbleId, userName, bizNo);

//                long time = System.currentTimeMillis();
//                while (System.currentTimeMillis() - time < Config.collectTimeout()) {
//                    if (collectEnergy(userId, bubbleId, userName, bizNo) > 0)
//                        break;
//                    sleep(500);
//                }
            } catch (Throwable t) {
                Log.i(TAG, "BubbleTimerTask.run err:");
                Log.printStackTrace(TAG, t);
            }
            String s = "  收：" + totalCollected + "，帮：" + totalHelpCollected;
            Log.recordLog(s, "");
            AntForestNotification.setContentText(Log.getFormatTime() + s);
        }

    }

}
