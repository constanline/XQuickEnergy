package pansong291.xposed.quickenergy.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class Statistics {
    public enum TimeType {
        YEAR, MONTH, DAY
    }

    public enum DataType {
        TIME, COLLECTED, HELPED, WATERED
    }

    private static class TimeStatistics {
        int time;
        int collected, helped, watered;

        TimeStatistics(int i) {
            reset(i);
        }

        public void reset(int i) {
            time = i;
            collected = 0;
            helped = 0;
            watered = 0;
        }
    }

    private static class WaterFriendLog {
        String userId;
        int waterCount = 0;

        public WaterFriendLog(String id) {
            userId = id;
        }
    }

    private static class ReserveLog {
        String projectId;
        int applyCount = 0;

        public ReserveLog(String id) {
            projectId = id;
        }
    }

    private static class BeachLog {
        String cultivationCode;
        int applyCount = 0;

        public BeachLog(String id) {
            cultivationCode = id;
        }
    }

    private static class FeedFriendLog {
        String userId;
        int feedCount = 0;

        public FeedFriendLog(String id) {
            userId = id;
        }
    }

    private static class VisitFriendLog {
        String userId;
        int visitCount = 0;

        public VisitFriendLog(String id) {
            userId = id;
        }
    }

    private static class StallShareIdLog {
        String userId;
        String shareId;

        public StallShareIdLog(String uid, String sid) {
            userId = uid;
            shareId = sid;
        }
    }

    private static class StallHelpedCountLog {
        String userId;
        int helpedCount = 0;
        int beHelpedCount = 0;

        public StallHelpedCountLog(String id) {
            userId = id;
        }
    }

    private static final String TAG = Statistics.class.getCanonicalName();
    private static final String jn_year = "year", jn_month = "month", jn_day = "day",
            jn_collected = "collected", jn_helped = "helped", jn_watered = "watered",
            jn_answerQuestionList = "answerQuestionList", jn_syncStepList = "syncStepList",
            jn_exchangeList = "exchangeList", jn_beachTodayList = "beachTodayList",
            jn_questionHint = "questionHint", jn_donationEggList = "donationEggList",
            jn_memberSignInList = "memberSignInList", jn_kbSignIn = "kbSignIn",
            jn_exchangeDoubleCard = "exchangeDoubleCard", jn_exchangeTimes = "exchangeTimes",
            jn_dailyAnswerList = "dailyAnswerList", jn_doubleTimes = "doubleTimes",
            jn_spreadManureList = "spreadManureList", jn_protectBubbleList = "protectBubbleList",
            jn_stallShareIdList = "stallShareIdList", jn_stallP2PHelpedList = "stallP2PHelpedList",
            jn_stallHelpedCountList = "stallHelpedCountList";

    private TimeStatistics year;
    private TimeStatistics month;
    private TimeStatistics day;

    // forest
    private ArrayList<WaterFriendLog> waterFriendLogList;
    private ArrayList<String> cooperateWaterList;
    private ArrayList<String> syncStepList;
    private ArrayList<ReserveLog> reserveLogList;
    private ArrayList<BeachLog> beachLogList;
    private ArrayList<String> beachTodayList;
    private ArrayList<String> ancientTreeCityCodeList;
    private ArrayList<String> exchangeList;
    private ArrayList<String> protectBubbleList;
    private int exchangeDoubleCard = 0;
    private int exchangeTimes = 0;
    private int exchangeShieldCard = 0;
    private int exchangeShieldTimes = 0;
    private int doubleTimes = 0;

    // farm
    private ArrayList<String> answerQuestionList;
    private String questionHint;
    private ArrayList<FeedFriendLog> feedFriendLogList;
    private ArrayList<VisitFriendLog> visitFriendLogList;
    private ArrayList<StallShareIdLog> stallShareIdLogList;
    private ArrayList<StallHelpedCountLog> stallHelpedCountLogList;
    private Set<String> dailyAnswerList;
    private ArrayList<String> donationEggList;
    private ArrayList<String> spreadManureList;
    private ArrayList<String> stallP2PHelpedList;

    // other
    private ArrayList<String> memberSignInList;
    private int kbSignIn = 0;

    private static Statistics statistics;

    public static void addData(DataType dt, int i) {
        Statistics stat = getStatistics();
        resetToday();
        switch (dt) {
            case COLLECTED:
                stat.day.collected += i;
                stat.month.collected += i;
                stat.year.collected += i;
                break;
            case HELPED:
                stat.day.helped += i;
                stat.month.helped += i;
                stat.year.helped += i;
                break;
            case WATERED:
                stat.day.watered += i;
                stat.month.watered += i;
                stat.year.watered += i;
                break;
        }
        save();
    }

    public static int getData(TimeType tt, DataType dt) {
        Statistics stat = getStatistics();
        int data = 0;
        TimeStatistics ts = null;
        switch (tt) {
            case YEAR:
                ts = stat.year;
                break;
            case MONTH:
                ts = stat.month;
                break;
            case DAY:
                ts = stat.day;
                break;
        }
        if (ts != null)
            switch (dt) {
                case TIME:
                    data = ts.time;
                    break;
                case COLLECTED:
                    data = ts.collected;
                    break;
                case HELPED:
                    data = ts.helped;
                    break;
                case WATERED:
                    data = ts.watered;
                    break;
            }
        return data;
    }

    public static String getText() {
        statistics = null;
        Statistics stat = getStatistics();
        StringBuilder sb = new StringBuilder(getData(TimeType.YEAR, DataType.TIME) + "年 : 收 ");
        sb.append(getData(TimeType.YEAR, DataType.COLLECTED));
        sb.append(",   帮 ").append(getData(TimeType.YEAR, DataType.HELPED));
        sb.append(",   浇 ").append(getData(TimeType.YEAR, DataType.WATERED));
        sb.append("\n").append(getData(TimeType.MONTH, DataType.TIME)).append("月 : 收 ");
        sb.append(getData(TimeType.MONTH, DataType.COLLECTED));
        sb.append(",   帮 ").append(getData(TimeType.MONTH, DataType.HELPED));
        sb.append(",   浇 ").append(getData(TimeType.MONTH, DataType.WATERED));
        sb.append("\n").append(getData(TimeType.DAY, DataType.TIME)).append("日 : 收 ");
        sb.append(getData(TimeType.DAY, DataType.COLLECTED));
        sb.append(",   帮 ").append(getData(TimeType.DAY, DataType.HELPED));
        sb.append(",   浇 ").append(getData(TimeType.DAY, DataType.WATERED));
        if (stat.questionHint != null && !stat.questionHint.isEmpty()) {
            sb.append("\nquestion hint : ").append(stat.questionHint);
        }
        return sb.toString();
    }

    public static boolean canWaterFriendToday(String id, int count) {
        id = FriendIdMap.getCurrentUid() + "-" + id;
        Statistics stat = getStatistics();
        int index = -1;
        for (int i = 0; i < stat.waterFriendLogList.size(); i++)
            if (stat.waterFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        WaterFriendLog wfl = stat.waterFriendLogList.get(index);
        return wfl.waterCount < count;
    }

    public static void waterFriendToday(String id, int count) {
        id = FriendIdMap.getCurrentUid() + "-" + id;
        Statistics stat = getStatistics();
        WaterFriendLog wfl;
        int index = -1;
        for (int i = 0; i < stat.waterFriendLogList.size(); i++)
            if (stat.waterFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            wfl = new WaterFriendLog(id);
            stat.waterFriendLogList.add(wfl);
        } else {
            wfl = stat.waterFriendLogList.get(index);
        }
        wfl.waterCount = count;
        save();
    }

    public static int getReserveTimes(String id) {
        Statistics stat = getStatistics();
        int index = -1;
        for (int i = 0; i < stat.reserveLogList.size(); i++)
            if (stat.reserveLogList.get(i).projectId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return 0;
        ReserveLog rl = stat.reserveLogList.get(index);
        return rl.applyCount;
    }

    public static boolean canReserveToday(String id, int count) {
        return getReserveTimes(id) < count;
    }

    public static void reserveToday(String id, int count) {
        Statistics stat = getStatistics();
        ReserveLog rl;
        int index = -1;
        for (int i = 0; i < stat.reserveLogList.size(); i++)
            if (stat.reserveLogList.get(i).projectId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            rl = new ReserveLog(id);
            stat.reserveLogList.add(rl);
        } else {
            rl = stat.reserveLogList.get(index);
        }
        rl.applyCount += count;
        save();
    }

    public static int getBeachTimes(String id) {
        Statistics stat = getStatistics();
        int index = -1;
        for (int i = 0; i < stat.beachLogList.size(); i++)
            if (stat.beachLogList.get(i).cultivationCode.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return 0;
        BeachLog bl = stat.beachLogList.get(index);
        return bl.applyCount;
    }

    public static boolean canBeach(String id, int count) {
        Statistics stat = getStatistics();
        int index = -1;
        for (int i = 0; i < stat.beachLogList.size(); i++)
            if (stat.beachLogList.get(i).cultivationCode.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        BeachLog bl = stat.beachLogList.get(index);
        return bl.applyCount < count;
    }

    public static void beachRecord(String id, int count) {
        Statistics stat = getStatistics();
        BeachLog bl;
        int index = -1;
        for (int i = 0; i < stat.beachLogList.size(); i++)
            if (stat.beachLogList.get(i).cultivationCode.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            bl = new BeachLog(id);
            stat.beachLogList.add(bl);
        } else {
            bl = stat.beachLogList.get(index);
        }
        bl.applyCount += count;
        save();
    }

    public static boolean canCooperateWaterToday(String uid, String coopId) {
        Statistics stat = getStatistics();
        return !stat.cooperateWaterList.contains(uid + "_" + coopId);
    }

    public static void cooperateWaterToday(String uid, String coopId) {
        Statistics stat = getStatistics();
        String v = uid + "_" + coopId;
        if (!stat.cooperateWaterList.contains(v)) {
            stat.cooperateWaterList.add(v);
            save();
        }
    }

    public static boolean canAncientTreeToday(String cityCode) {
        Statistics stat = getStatistics();
        return !stat.ancientTreeCityCodeList.contains(cityCode);
    }

    public static void ancientTreeToday(String cityCode) {
        Statistics stat = getStatistics();
        if (!stat.ancientTreeCityCodeList.contains(cityCode)) {
            stat.ancientTreeCityCodeList.add(cityCode);
            save();
        }
    }

    public static boolean canAnswerQuestionToday(String uid) {
        Statistics stat = getStatistics();
        return !stat.answerQuestionList.contains(uid);
    }

    public static void answerQuestionToday(String uid) {
        Statistics stat = getStatistics();
        if (!stat.answerQuestionList.contains(uid)) {
            stat.answerQuestionList.add(uid);
            save();
        }
    }

    public static void setQuestionHint(String s) {
        Statistics stat = getStatistics();
        if (stat.questionHint == null) {
            stat.questionHint = s;
            save();
        }
    }

    public static boolean canFeedFriendToday(String id, int count) {
        Statistics stat = getStatistics();
        int index = -1;
        for (int i = 0; i < stat.feedFriendLogList.size(); i++)
            if (stat.feedFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        FeedFriendLog ffl = stat.feedFriendLogList.get(index);
        return ffl.feedCount < count;
    }

    public static void feedFriendToday(String id) {
        Statistics stat = getStatistics();
        FeedFriendLog ffl;
        int index = -1;
        for (int i = 0; i < stat.feedFriendLogList.size(); i++)
            if (stat.feedFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            ffl = new FeedFriendLog(id);
            stat.feedFriendLogList.add(ffl);
        } else {
            ffl = stat.feedFriendLogList.get(index);
        }
        ffl.feedCount++;
        save();
    }

    public static boolean canVisitFriendToday(String id, int count) {
        id = FriendIdMap.getCurrentUid() + "-" + id;
        Statistics stat = getStatistics();
        int index = -1;
        for (int i = 0; i < stat.visitFriendLogList.size(); i++)
            if (stat.visitFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        VisitFriendLog vfl = stat.visitFriendLogList.get(index);
        return vfl.visitCount < count;
    }

    public static void visitFriendToday(String id, int count) {
        id = FriendIdMap.getCurrentUid() + "-" + id;
        Statistics stat = getStatistics();
        VisitFriendLog vfl;
        int index = -1;
        for (int i = 0; i < stat.visitFriendLogList.size(); i++)
            if (stat.visitFriendLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            vfl = new VisitFriendLog(id);
            stat.visitFriendLogList.add(vfl);
        } else {
            vfl = stat.visitFriendLogList.get(index);
        }
        vfl.visitCount = count;
        save();
    }

    public static List<String> stallP2PUserIdList(String uid) {
        List<String> p2pUserIdList = new ArrayList<>();
        Statistics stat = getStatistics();
        for (int i = 0; i < stat.stallShareIdLogList.size(); i++)
            if (!stat.stallShareIdLogList.get(i).userId.equals(uid)) {
                p2pUserIdList.add(stat.stallShareIdLogList.get(i).userId);
            }
        return p2pUserIdList;
    }

    public static void stallShareIdToday(String uid, String sid) {
        Statistics stat = getStatistics();
        StallShareIdLog ssil;
        int index = -1;
        for (int i = 0; i < stat.stallShareIdLogList.size(); i++)
            if (stat.stallShareIdLogList.get(i).userId.equals(uid)) {
                index = i;
                break;
            }
        if (index < 0) {
            ssil = new StallShareIdLog(uid, sid);
            stat.stallShareIdLogList.add(ssil);
        } else {
            ssil = stat.stallShareIdLogList.get(index);
        }
        ssil.shareId = sid;
        save();
    }

    public static String getStallShareId(String uid) {
        Statistics stat = getStatistics();
        int index = -1;
        for (int i = 0; i < stat.stallShareIdLogList.size(); i++)
            if (stat.stallShareIdLogList.get(i).userId.equals(uid)) {
                index = i;
                break;
            }
        if (index < 0) {
            return null;
        } else {
            return stat.stallShareIdLogList.get(index).shareId;
        }
    }

    public static boolean canStallHelpToday(String id) {
        Statistics stat = getStatistics();
        int index = -1;
        for (int i = 0; i < stat.stallHelpedCountLogList.size(); i++)
            if (stat.stallHelpedCountLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        StallHelpedCountLog shcl = stat.stallHelpedCountLogList.get(index);
        return shcl.helpedCount < 3;
    }

    public static void stallHelpToday(String id, boolean limited) {
        Statistics stat = getStatistics();
        StallHelpedCountLog shcl;
        int index = -1;
        for (int i = 0; i < stat.stallHelpedCountLogList.size(); i++)
            if (stat.stallHelpedCountLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            shcl = new StallHelpedCountLog(id);
            stat.stallHelpedCountLogList.add(shcl);
        } else {
            shcl = stat.stallHelpedCountLogList.get(index);
        }
        if (limited) {
            shcl.helpedCount = 3;
        } else {
            shcl.helpedCount += 1;
        }
        save();
    }

    public static boolean canStallBeHelpToday(String id) {
        Statistics stat = getStatistics();
        int index = -1;
        for (int i = 0; i < stat.stallHelpedCountLogList.size(); i++)
            if (stat.stallHelpedCountLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0)
            return true;
        StallHelpedCountLog shcl = stat.stallHelpedCountLogList.get(index);
        return shcl.beHelpedCount < 3;
    }

    public static void stallBeHelpToday(String id, boolean limited) {
        Statistics stat = getStatistics();
        StallHelpedCountLog shcl;
        int index = -1;
        for (int i = 0; i < stat.stallHelpedCountLogList.size(); i++)
            if (stat.stallHelpedCountLogList.get(i).userId.equals(id)) {
                index = i;
                break;
            }
        if (index < 0) {
            shcl = new StallHelpedCountLog(id);
            stat.stallHelpedCountLogList.add(shcl);
        } else {
            shcl = stat.stallHelpedCountLogList.get(index);
        }
        if (limited) {
            shcl.beHelpedCount = 3;
        } else {
            shcl.beHelpedCount += 1;
        }
        save();
    }

    public static boolean canMemberSignInToday(String uid) {
        Statistics stat = getStatistics();
        return !stat.memberSignInList.contains(uid);
    }

    public static void memberSignInToday(String uid) {
        Statistics stat = getStatistics();
        if (!stat.memberSignInList.contains(uid)) {
            stat.memberSignInList.add(uid);
            save();
        }
    }

    public static boolean canDonationEgg(String uid) {
        Statistics stat = getStatistics();
        return !stat.donationEggList.contains(uid);
    }

    public static void donationEgg(String uid) {
        Statistics stat = getStatistics();
        if (!stat.donationEggList.contains(uid)) {
            stat.donationEggList.add(uid);
            save();
        }
    }

    public static boolean canSpreadManureToday(String uid) {
        Statistics stat = getStatistics();
        return !stat.spreadManureList.contains(uid);
    }

    public static void spreadManureToday(String uid) {
        Statistics stat = getStatistics();
        if (!stat.spreadManureList.contains(uid)) {
            stat.spreadManureList.add(uid);
            save();
        }
    }

    public static boolean canStallP2PHelpToday(String uid) {
        uid = FriendIdMap.getCurrentUid() + "-" + uid;
        Statistics stat = getStatistics();
        return !stat.stallP2PHelpedList.contains(uid);
    }

    public static void stallP2PHelpeToday(String uid) {
        uid = FriendIdMap.getCurrentUid() + "-" + uid;
        Statistics stat = getStatistics();
        if (!stat.stallP2PHelpedList.contains(uid)) {
            stat.stallP2PHelpedList.add(uid);
            save();
        }
    }

    public static boolean canExchangeToday(String uid) {
        Statistics stat = getStatistics();
        return !stat.exchangeList.contains(uid);
    }

    public static void exchangeToday(String uid) {
        Statistics stat = getStatistics();
        if (!stat.exchangeList.contains(uid)) {
            stat.exchangeList.add(uid);
            save();
        }
    }

    public static boolean canProtectBubbleToday(String uid) {
        Statistics stat = getStatistics();
        return !stat.protectBubbleList.contains(uid);
    }

    public static void protectBubbleToday(String uid) {
        Statistics stat = getStatistics();
        if (!stat.protectBubbleList.contains(uid)) {
            stat.protectBubbleList.add(uid);
            save();
        }
    }

    public static boolean canExchangeDoubleCardToday() {
        Statistics stat = getStatistics();
        if (stat.exchangeDoubleCard < stat.day.time) {
            return true;
        } else return stat.exchangeTimes < Config.getExchangeEnergyDoubleClickCount();
    }

    public static boolean canExchangeShieldCardToday() {
        Statistics stat = getStatistics();
        if (stat.exchangeShieldCard < stat.day.time) {
            return true;
        } else return stat.exchangeShieldTimes < Config.getExchangeEnergyShieldCount();
    }

    public static void exchangeDoubleCardToday(boolean isSuccess) {
        Statistics stat = getStatistics();
        if (stat.exchangeDoubleCard != stat.day.time) {
            stat.exchangeDoubleCard = stat.day.time;
        }
        if (isSuccess) {
            stat.exchangeTimes += 1;
        } else {
            stat.exchangeTimes = Config.getExchangeEnergyDoubleClickCount();
        }
        save();
    }

    public static void exchangeShieldToday(boolean isSuccess) {
        Statistics stat = getStatistics();
        if (stat.exchangeShieldCard != stat.day.time) {
            stat.exchangeShieldCard = stat.day.time;
        }
        if (isSuccess) {
            stat.exchangeShieldTimes += 1;
        } else {
            stat.exchangeShieldTimes = Config.getExchangeEnergyShieldCount();
        }
        save();
    }

    public static int getExchangeTimes() {
        Statistics stat = getStatistics();
        return stat.exchangeTimes;
    }

    public static int getExchangeShieldTimes() {
        Statistics stat = getStatistics();
        return stat.exchangeShieldTimes;
    }

    public static boolean canDoubleToday() {
        Statistics stat = getStatistics();
        return stat.doubleTimes < Config.getDoubleCountLimit();
    }

    public static void DoubleToday() {
        Statistics stat = getStatistics();
        stat.doubleTimes += 1;
        save();
    }

    public static boolean canKbSignInToday() {
        Statistics stat = getStatistics();
        return stat.kbSignIn < stat.day.time;
    }

    public static void KbSignInToday() {
        Statistics stat = getStatistics();
        if (stat.kbSignIn != stat.day.time) {
            stat.kbSignIn = stat.day.time;
            save();
        }
    }

    public static Set<String> getDadaDailySet() {
        Statistics stat = getStatistics();
        return stat.dailyAnswerList;
    }

    public static void setDadaDailySet(Set<String> dailyAnswerList) {
        Statistics stat = getStatistics();
        stat.dailyAnswerList = dailyAnswerList;
        save();
    }

    public static boolean canSyncStepToday(String uid) {
        Statistics stat = getStatistics();
        return !stat.syncStepList.contains(uid);
    }

    public static void SyncStepToday(String uid) {
        Statistics stat = getStatistics();
        if (!stat.syncStepList.contains(uid)) {
            stat.syncStepList.add(uid);
            save();
        }
    }

    public static boolean canBeachToday(String cultivationCode) {
        Statistics stat = getStatistics();
        return !stat.beachTodayList.contains(cultivationCode);
    }

    public static void beachToday(String cultivationCode) {
        Statistics stat = getStatistics();
        if (!stat.beachTodayList.contains(cultivationCode)) {
            stat.beachTodayList.add(cultivationCode);
            save();
        }
    }

    private static Statistics getStatistics() {
        if (statistics == null) {
            String statJson = null;
            if (FileUtils.getStatisticsFile().exists())
                statJson = FileUtils.readFromFile(FileUtils.getStatisticsFile());
            statistics = json2Statistics(statJson);
        }
        return statistics;
    }

    public static void resetToday() {
        Statistics stat = getStatistics();
        String formatDate = Log.getFormatDate();
        String[] dateStr = formatDate.split("-");
        int ye = Integer.parseInt(dateStr[0]);
        int mo = Integer.parseInt(dateStr[1]);
        int da = Integer.parseInt(dateStr[2]);

        Log.recordLog("原：" + stat.year.time + "-" + stat.month.time + "-" + stat.day.time + "；新：" + formatDate);
        if (ye > stat.year.time) {
            stat.year.reset(ye);
            stat.month.reset(mo);
            stat.day.reset(da);
            dayClear();
        } else if (mo > stat.month.time) {
            stat.month.reset(mo);
            stat.day.reset(da);
            dayClear();
        } else if (da > stat.day.time) {
            stat.day.reset(da);
            dayClear();
        }
    }

    private static void dayClear() {
        Log.infoChanged(TAG,"重置 statistics.json");
        Statistics stat = getStatistics();
        stat.waterFriendLogList.clear();
        stat.cooperateWaterList.clear();
        stat.syncStepList.clear();
        stat.exchangeList.clear();
        stat.protectBubbleList.clear();
        stat.reserveLogList.clear();
        stat.beachTodayList.clear();
        stat.ancientTreeCityCodeList.clear();
        stat.answerQuestionList.clear();
        stat.feedFriendLogList.clear();
        stat.visitFriendLogList.clear();
        stat.stallHelpedCountLogList.clear();
        stat.questionHint = null;
        stat.donationEggList.clear();
        stat.spreadManureList.clear();
        stat.stallP2PHelpedList.clear();
        stat.memberSignInList.clear();
        stat.kbSignIn = 0;
        stat.exchangeDoubleCard = 0;
        stat.exchangeTimes = 0;
        stat.doubleTimes = 0;
        save();
        FileUtils.getForestLogFile().delete();
        FileUtils.getFarmLogFile().delete();
        FileUtils.getOtherLogFile().delete();
    }

    private static Statistics defInit() {
        Statistics stat = new Statistics();
        String[] date = Log.getFormatDate().split("-");
        if (stat.year == null)
            stat.year = new TimeStatistics(Integer.parseInt(date[0]));
        if (stat.month == null)
            stat.month = new TimeStatistics(Integer.parseInt(date[1]));
        if (stat.day == null)
            stat.day = new TimeStatistics(Integer.parseInt(date[2]));
        if (stat.cooperateWaterList == null)
            stat.cooperateWaterList = new ArrayList<>();
        if (stat.answerQuestionList == null)
            stat.answerQuestionList = new ArrayList<>();
        if (stat.donationEggList == null)
            stat.donationEggList = new ArrayList<>();
        if (stat.spreadManureList == null)
            stat.spreadManureList = new ArrayList<>();
        if (stat.stallP2PHelpedList == null)
            stat.stallP2PHelpedList = new ArrayList<>();
        if (stat.memberSignInList == null)
            stat.memberSignInList = new ArrayList<>();
        if (stat.feedFriendLogList == null)
            stat.feedFriendLogList = new ArrayList<>();
        if (stat.visitFriendLogList == null)
            stat.visitFriendLogList = new ArrayList<>();
        if (stat.stallShareIdLogList == null)
            stat.stallShareIdLogList = new ArrayList<>();
        if (stat.stallHelpedCountLogList == null)
            stat.stallHelpedCountLogList = new ArrayList<>();
        if (stat.ancientTreeCityCodeList == null)
            stat.ancientTreeCityCodeList = new ArrayList<>();
        if (stat.syncStepList == null)
            stat.syncStepList = new ArrayList<>();
        if (stat.beachTodayList == null)
            stat.beachTodayList = new ArrayList<>();
        if (stat.exchangeList == null)
            stat.exchangeList = new ArrayList<>();
        if (stat.protectBubbleList == null)
            stat.protectBubbleList = new ArrayList<>();
        if (stat.dailyAnswerList == null)
            stat.dailyAnswerList = new HashSet<>();
        return stat;
    }

    private static Statistics json2Statistics(String json) {
        Statistics stat;
        try {
            JSONObject jo = new JSONObject(json);
            JSONObject joo;
            stat = new Statistics();

            joo = jo.getJSONObject(jn_year);
            stat.year = new TimeStatistics(joo.getInt(jn_year));

            stat.year.collected = joo.getInt(jn_collected);

            stat.year.helped = joo.getInt(jn_helped);

            stat.year.watered = joo.getInt(jn_watered);

            joo = jo.getJSONObject(jn_month);
            stat.month = new TimeStatistics(joo.getInt(jn_month));

            stat.month.collected = joo.getInt(jn_collected);

            stat.month.helped = joo.getInt(jn_helped);

            stat.month.watered = joo.getInt(jn_watered);

            joo = jo.getJSONObject(jn_day);
            stat.day = new TimeStatistics(joo.getInt(jn_day));

            stat.day.collected = joo.getInt(jn_collected);

            stat.day.helped = joo.getInt(jn_helped);

            stat.day.watered = joo.getInt(jn_watered);

            stat.waterFriendLogList = new ArrayList<>();

            if (jo.has(Config.jn_waterFriendList)) {
                JSONArray ja = jo.getJSONArray(Config.jn_waterFriendList);
                for (int i = 0; i < ja.length(); i++) {
                    JSONArray jaa = ja.getJSONArray(i);
                    WaterFriendLog wfl = new WaterFriendLog(jaa.getString(0));
                    wfl.waterCount = jaa.getInt(1);
                    stat.waterFriendLogList.add(wfl);

                }
            }

            stat.cooperateWaterList = new ArrayList<>();

            if (jo.has(Config.jn_cooperateWaterList)) {
                JSONArray ja = jo.getJSONArray(Config.jn_cooperateWaterList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.cooperateWaterList.add(ja.getString(i));

                }
            }

            stat.ancientTreeCityCodeList = new ArrayList<>();

            if (jo.has(Config.jn_ancientTreeCityCodeList)) {
                JSONArray ja = jo.getJSONArray(Config.jn_ancientTreeCityCodeList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.ancientTreeCityCodeList.add(ja.getString(i));

                }
            }

            stat.syncStepList = new ArrayList<>();

            if (jo.has(jn_syncStepList)) {
                JSONArray ja = jo.getJSONArray(jn_syncStepList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.syncStepList.add(ja.getString(i));

                }
            }

            stat.beachTodayList = new ArrayList<>();

            if (jo.has(jn_beachTodayList)) {
                JSONArray ja = jo.getJSONArray(jn_beachTodayList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.beachTodayList.add(ja.getString(i));

                }
            }

            stat.exchangeList = new ArrayList<>();

            if (jo.has(jn_exchangeList)) {
                JSONArray ja = jo.getJSONArray(jn_exchangeList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.exchangeList.add(ja.getString(i));

                }
            }

            stat.protectBubbleList = new ArrayList<>();

            if (jo.has(jn_protectBubbleList)) {
                JSONArray ja = jo.getJSONArray(jn_protectBubbleList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.protectBubbleList.add(ja.getString(i));

                }
            }

            stat.reserveLogList = new ArrayList<>();

            if (jo.has(Config.jn_reserveList)) {
                JSONArray ja = jo.getJSONArray(Config.jn_reserveList);
                for (int i = 0; i < ja.length(); i++) {
                    JSONArray jaa = ja.getJSONArray(i);
                    ReserveLog rl = new ReserveLog(jaa.getString(0));
                    rl.applyCount = jaa.getInt(1);
                    stat.reserveLogList.add(rl);
                }
            }

            stat.beachLogList = new ArrayList<>();

            if (jo.has(Config.jn_beachList)) {
                JSONArray ja = jo.getJSONArray(Config.jn_beachList);
                for (int i = 0; i < ja.length(); i++) {
                    JSONArray jaa = ja.getJSONArray(i);
                    BeachLog bl = new BeachLog(jaa.getString(0));
                    bl.applyCount = jaa.getInt(1);
                    stat.beachLogList.add(bl);
                }
            }

            stat.answerQuestionList = new ArrayList<>();

            if (jo.has(jn_answerQuestionList)) {
                JSONArray ja = jo.getJSONArray(jn_answerQuestionList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.answerQuestionList.add(ja.getString(i));

                }
            }

            if (jo.has(jn_questionHint))
                stat.questionHint = jo.getString(jn_questionHint);

            stat.feedFriendLogList = new ArrayList<>();

            if (jo.has(Config.jn_feedFriendAnimalList)) {
                JSONArray ja = jo.getJSONArray(Config.jn_feedFriendAnimalList);
                for (int i = 0; i < ja.length(); i++) {
                    JSONArray jaa = ja.getJSONArray(i);
                    FeedFriendLog ffl = new FeedFriendLog(jaa.getString(0));
                    ffl.feedCount = jaa.getInt(1);
                    stat.feedFriendLogList.add(ffl);

                }
            }

            stat.visitFriendLogList = new ArrayList<>();
            if (jo.has(Config.jn_visitFriendList)) {
                JSONArray ja = jo.getJSONArray(Config.jn_visitFriendList);
                for (int i = 0; i < ja.length(); i++) {
                    JSONArray jaa = ja.getJSONArray(i);
                    VisitFriendLog vfl = new VisitFriendLog(jaa.getString(0));
                    vfl.visitCount = jaa.getInt(1);
                    stat.visitFriendLogList.add(vfl);

                }
            }

            stat.stallShareIdLogList = new ArrayList<>();
            if (jo.has(jn_stallShareIdList)) {
                JSONArray ja = jo.getJSONArray(jn_stallShareIdList);
                for (int i = 0; i < ja.length(); i++) {
                    JSONArray jaa = ja.getJSONArray(i);
                    StallShareIdLog ssil = new StallShareIdLog(jaa.getString(0), jaa.getString(1));
                    stat.stallShareIdLogList.add(ssil);
                }
            }

            stat.stallHelpedCountLogList = new ArrayList<>();
            if (jo.has(jn_stallHelpedCountList)) {
                JSONArray ja = jo.getJSONArray(jn_stallHelpedCountList);
                for (int i = 0; i < ja.length(); i++) {
                    JSONArray jaa = ja.getJSONArray(i);
                    StallHelpedCountLog shcl = new StallHelpedCountLog(jaa.getString(0));
                    shcl.helpedCount = jaa.getInt(1);
                    shcl.beHelpedCount = jaa.getInt(2);
                    stat.stallHelpedCountLogList.add(shcl);

                }
            }

            stat.donationEggList = new ArrayList<>();
            if (jo.has(jn_donationEggList)) {
                JSONArray ja = jo.getJSONArray(jn_donationEggList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.donationEggList.add(ja.getString(i));

                }
            }

            stat.spreadManureList = new ArrayList<>();
            if (jo.has(jn_spreadManureList)) {
                JSONArray ja = jo.getJSONArray(jn_spreadManureList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.spreadManureList.add(ja.getString(i));

                }
            }

            stat.stallP2PHelpedList = new ArrayList<>();
            if (jo.has(jn_stallP2PHelpedList)) {
                JSONArray ja = jo.getJSONArray(jn_stallP2PHelpedList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.stallP2PHelpedList.add(ja.getString(i));

                }
            }

            stat.memberSignInList = new ArrayList<>();

            if (jo.has(jn_memberSignInList)) {
                JSONArray ja = jo.getJSONArray(jn_memberSignInList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.memberSignInList.add(ja.getString(i));

                }
            }

            if (jo.has(jn_kbSignIn))
                stat.kbSignIn = jo.getInt(jn_kbSignIn);

            if (jo.has(jn_exchangeDoubleCard))
                stat.exchangeDoubleCard = jo.getInt(jn_exchangeDoubleCard);

            if (jo.has(jn_exchangeTimes))
                stat.exchangeTimes = jo.getInt(jn_exchangeTimes);

            if (jo.has(jn_doubleTimes))
                stat.doubleTimes = jo.getInt(jn_doubleTimes);

            stat.dailyAnswerList = new HashSet<>();
            if (jo.has(jn_dailyAnswerList)) {
                JSONArray ja = jo.getJSONArray(jn_dailyAnswerList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.dailyAnswerList.add(ja.getString(i));
                }
            }

        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
            if (json != null) {
                Log.i(TAG, "统计文件格式有误，已重置统计文件并备份原文件");
                Log.infoChanged(TAG, "统计文件格式有误，已重置统计文件并备份原文件");
                FileUtils.write2File(json, FileUtils.getBackupFile(FileUtils.getStatisticsFile()));
            }
            stat = defInit();
        }
        String formatted = statistics2Json(stat);
        if (!formatted.equals(json)) {
            Log.i(TAG, "重新格式化 statistics.json");
            Log.infoChanged(TAG, "重新格式化 statistics.json");
            FileUtils.write2File(formatted, FileUtils.getStatisticsFile());
        }
        return stat;
    }

    private static String statistics2Json(Statistics stat) {
        JSONObject jo = new JSONObject();
        JSONArray ja = null;
        try {
            if (stat == null)
                stat = Statistics.defInit();
            JSONObject joo = new JSONObject();
            joo.put(jn_year, stat.year.time);
            joo.put(jn_collected, stat.year.collected);
            joo.put(jn_helped, stat.year.helped);
            joo.put(jn_watered, stat.year.watered);
            jo.put(jn_year, joo);

            joo = new JSONObject();
            joo.put(jn_month, stat.month.time);
            joo.put(jn_collected, stat.month.collected);
            joo.put(jn_helped, stat.month.helped);
            joo.put(jn_watered, stat.month.watered);
            jo.put(jn_month, joo);

            joo = new JSONObject();
            joo.put(jn_day, stat.day.time);
            joo.put(jn_collected, stat.day.collected);
            joo.put(jn_helped, stat.day.helped);
            joo.put(jn_watered, stat.day.watered);
            jo.put(jn_day, joo);

            ja = new JSONArray();
            for (int i = 0; i < stat.waterFriendLogList.size(); i++) {
                WaterFriendLog wfl = stat.waterFriendLogList.get(i);
                JSONArray jaa = new JSONArray();
                jaa.put(wfl.userId);
                jaa.put(wfl.waterCount);
                ja.put(jaa);
            }
            jo.put(Config.jn_waterFriendList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.cooperateWaterList.size(); i++) {
                ja.put(stat.cooperateWaterList.get(i));
            }
            jo.put(Config.jn_cooperateWaterList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.syncStepList.size(); i++) {
                ja.put(stat.syncStepList.get(i));
            }
            jo.put(jn_syncStepList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.beachTodayList.size(); i++) {
                ja.put(stat.beachTodayList.get(i));
            }
            jo.put(jn_beachTodayList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.exchangeList.size(); i++) {
                ja.put(stat.exchangeList.get(i));
            }
            jo.put(jn_exchangeList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.protectBubbleList.size(); i++) {
                ja.put(stat.protectBubbleList.get(i));
            }
            jo.put(jn_protectBubbleList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.ancientTreeCityCodeList.size(); i++) {
                ja.put(stat.ancientTreeCityCodeList.get(i));
            }
            jo.put(Config.jn_ancientTreeCityCodeList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.reserveLogList.size(); i++) {
                ReserveLog rl = stat.reserveLogList.get(i);
                JSONArray jaa = new JSONArray();
                jaa.put(rl.projectId);
                jaa.put(rl.applyCount);
                ja.put(jaa);
            }
            jo.put(Config.jn_reserveList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.beachLogList.size(); i++) {
                BeachLog bl = stat.beachLogList.get(i);
                JSONArray jaa = new JSONArray();
                jaa.put(bl.cultivationCode);
                jaa.put(bl.applyCount);
                ja.put(jaa);
            }
            jo.put(Config.jn_beachList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.answerQuestionList.size(); i++) {
                ja.put(stat.answerQuestionList.get(i));
            }
            jo.put(jn_answerQuestionList, ja);

            if (stat.questionHint != null)
                jo.put(jn_questionHint, stat.questionHint);

            ja = new JSONArray();
            for (int i = 0; i < stat.feedFriendLogList.size(); i++) {
                FeedFriendLog ffl = stat.feedFriendLogList.get(i);
                JSONArray jaa = new JSONArray();
                jaa.put(ffl.userId);
                jaa.put(ffl.feedCount);
                ja.put(jaa);
            }
            jo.put(Config.jn_feedFriendAnimalList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.visitFriendLogList.size(); i++) {
                VisitFriendLog vfl = stat.visitFriendLogList.get(i);
                JSONArray jaa = new JSONArray();
                jaa.put(vfl.userId);
                jaa.put(vfl.visitCount);
                ja.put(jaa);
            }
            jo.put(Config.jn_visitFriendList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.stallShareIdLogList.size(); i++) {
                StallShareIdLog ssil = stat.stallShareIdLogList.get(i);
                JSONArray jaa = new JSONArray();
                jaa.put(ssil.userId);
                jaa.put(ssil.shareId);
                ja.put(jaa);
            }
            jo.put(jn_stallShareIdList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.stallHelpedCountLogList.size(); i++) {
                StallHelpedCountLog shcl = stat.stallHelpedCountLogList.get(i);
                JSONArray jaa = new JSONArray();
                jaa.put(shcl.userId);
                jaa.put(shcl.helpedCount);
                jaa.put(shcl.beHelpedCount);
                ja.put(jaa);
            }
            jo.put(jn_stallHelpedCountList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.donationEggList.size(); i++) {
                ja.put(stat.donationEggList.get(i));
            }
            jo.put(jn_donationEggList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.spreadManureList.size(); i++) {
                ja.put(stat.spreadManureList.get(i));
            }
            jo.put(jn_spreadManureList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.stallP2PHelpedList.size(); i++) {
                ja.put(stat.stallP2PHelpedList.get(i));
            }
            jo.put(jn_stallP2PHelpedList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.memberSignInList.size(); i++) {
                ja.put(stat.memberSignInList.get(i));
            }
            jo.put(jn_memberSignInList, ja);

            jo.put(jn_kbSignIn, stat.kbSignIn);

            jo.put(jn_exchangeDoubleCard, stat.exchangeDoubleCard);

            jo.put(jn_exchangeTimes, stat.exchangeTimes);

            jo.put(jn_doubleTimes, stat.doubleTimes);

            ja = new JSONArray();
            for (String item : stat.dailyAnswerList) {
                ja.put(item);
            }
            jo.put(jn_dailyAnswerList, ja);

        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
        return Config.formatJson(jo, false);
    }

    private static void save() {
        String json = statistics2Json(getStatistics());
        Log.infoChanged(TAG,"保存 statistics.json");
        FileUtils.write2File(json, FileUtils.getStatisticsFile());
    }

}
