package pansong291.xposed.quickenergy.util;

import java.util.ArrayList;
import java.util.HashSet;
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

    private static final String TAG = Statistics.class.getCanonicalName();
    private static final String jn_year = "year", jn_month = "month", jn_day = "day",
            jn_collected = "collected", jn_helped = "helped", jn_watered = "watered",
            jn_answerQuestionList = "answerQuestionList", jn_syncStepList = "syncStepList",
            jn_exchangeList = "exchangeList", jn_beachTodayList = "beachTodayList",
            jn_questionHint = "questionHint", jn_donationEggList = "donationEggList",
            jn_memberSignInList = "memberSignInList", jn_kbSignIn = "kbSignIn",
            jn_exchangeDoubleCard = "exchangeDoubleCard", jn_exchangeTimes = "exchangeTimes",
            jn_dailyAnswerList = "dailyAnswerList", jn_doubleTimes = "doubleTimes",
            jn_SpreadManureList = "SpreadManureList";

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
    private int exchangeDoubleCard = 0;
    private int exchangeTimes = 0;
    private int doubleTimes = 0;

    // farm
    private ArrayList<String> answerQuestionList;
    private String questionHint;
    private ArrayList<FeedFriendLog> feedFriendLogList;
    private Set<String> dailyAnswerList;
    private ArrayList<String> donationEggList;
    private ArrayList<String> SpreadManureList;

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
        id = FriendIdMap.currentUid + "-" + id;
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
        id = FriendIdMap.currentUid + "-" + id;
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
        return !stat.SpreadManureList.contains(uid);
    }

    public static void spreadManureToday(String uid) {
        Statistics stat = getStatistics();
        if (!stat.SpreadManureList.contains(uid)) {
            stat.SpreadManureList.add(uid);
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

    public static boolean canExchangeDoubleCardToday() {
        Statistics stat = getStatistics();
        if (stat.exchangeDoubleCard < stat.day.time) {
            return true;
        } else return stat.exchangeTimes < Config.getExchangeEnergyDoubleClickCount();
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

    public static int getExchangeTimes() {
        Statistics stat = getStatistics();
        return stat.exchangeTimes;
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
        Statistics stat = getStatistics();
        stat.waterFriendLogList.clear();
        stat.cooperateWaterList.clear();
        stat.syncStepList.clear();
        stat.exchangeList.clear();
        stat.reserveLogList.clear();
        stat.beachTodayList.clear();
        stat.ancientTreeCityCodeList.clear();
        stat.answerQuestionList.clear();
        stat.feedFriendLogList.clear();
        stat.questionHint = null;
        stat.donationEggList.clear();
        stat.SpreadManureList.clear();
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
        if (stat.SpreadManureList == null)
            stat.SpreadManureList = new ArrayList<>();
        if (stat.memberSignInList == null)
            stat.memberSignInList = new ArrayList<>();
        if (stat.feedFriendLogList == null)
            stat.feedFriendLogList = new ArrayList<>();
        if (stat.ancientTreeCityCodeList == null)
            stat.ancientTreeCityCodeList = new ArrayList<>();
        if (stat.syncStepList == null)
            stat.syncStepList = new ArrayList<>();
        if (stat.beachTodayList == null)
            stat.beachTodayList = new ArrayList<>();
        if (stat.exchangeList == null)
            stat.exchangeList = new ArrayList<>();
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

            stat.donationEggList = new ArrayList<>();
            if (jo.has(jn_donationEggList)) {
                JSONArray ja = jo.getJSONArray(jn_donationEggList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.donationEggList.add(ja.getString(i));

                }
            }

            stat.SpreadManureList = new ArrayList<>();
            if (jo.has(jn_SpreadManureList)) {
                JSONArray ja = jo.getJSONArray(jn_SpreadManureList);
                for (int i = 0; i < ja.length(); i++) {
                    stat.SpreadManureList.add(ja.getString(i));

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
                Log.infoChanged("统计文件格式有误，已重置统计文件并备份原文件", json);
                FileUtils.write2File(json, FileUtils.getBackupFile(FileUtils.getStatisticsFile()));
            }
            stat = defInit();
        }
        String formatted = statistics2Json(stat);
        if (!formatted.equals(json)) {
            Log.i(TAG, "重新格式化 statistics.json");
            Log.infoChanged("重新格式化 statistics.json", json);
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
            for (int i = 0; i < stat.donationEggList.size(); i++) {
                ja.put(stat.donationEggList.get(i));
            }
            jo.put(jn_donationEggList, ja);

            ja = new JSONArray();
            for (int i = 0; i < stat.SpreadManureList.size(); i++) {
                ja.put(stat.SpreadManureList.get(i));
            }
            jo.put(jn_SpreadManureList, ja);

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
        Log.infoChanged("保存 statistics.json", json);
        FileUtils.write2File(json, FileUtils.getStatisticsFile());
    }

}
