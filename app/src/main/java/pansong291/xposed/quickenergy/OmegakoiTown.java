package pansong291.xposed.quickenergy;

import org.json.JSONArray;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.data.RuntimeInfo;
import pansong291.xposed.quickenergy.hook.OmegakoiTownRpcCall;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.Log;

public class OmegakoiTown {
    private static final String TAG = OmegakoiTown.class.getCanonicalName();

    public enum RewardType {
        gold, diamond, dyestuff, rubber, glass, certificate, shipping, tpuPhoneCaseCertificate,
        glassPhoneCaseCertificate, canvasBagCertificate, notebookCertificate, box, paper, cotton;

        public static final CharSequence[] rewardNames = { "金币", "钻石", "颜料", "橡胶", "玻璃", "合格证", "包邮券", "TPU手机壳合格证",
                "玻璃手机壳合格证", "帆布袋合格证", "记事本合格证", "快递包装盒", "纸张", "棉花" };

        public CharSequence rewardName() {
            return rewardNames[ordinal()];
        }
    }

    public enum HouseType {
        houseTrainStation, houseStop, houseBusStation, houseGas, houseSchool, houseService, houseHospital, housePolice,
        houseBank, houseRecycle, houseWasteTreatmentPlant, houseMetro, houseKfc, houseManicureShop, housePhoto, house5g,
        houseGame, houseLucky, housePrint, houseBook, houseGrocery, houseScience, housemarket1, houseMcd,
        houseStarbucks, houseRestaurant, houseFruit, houseDessert, houseClothes, zhiketang, houseFlower, houseMedicine,
        housePet, houseChick, houseFamilyMart, houseHouse, houseFlat, houseVilla, houseResident, housePowerPlant,
        houseWaterPlant, houseDailyChemicalFactory, houseToyFactory, houseSewageTreatmentPlant, houseSports,
        houseCinema, houseCotton, houseMarket, houseStadium, houseHotel, housebusiness, houseOrchard, housePark,
        houseFurnitureFactory, houseChipFactory, houseChemicalPlant, houseThermalPowerPlant, houseExpressStation,
        houseDormitory, houseCanteen, houseAdministrationBuilding, houseGourmetPalace, housePaperMill,
        houseAuctionHouse, houseCatHouse, houseStarPickingPavilion;

        public static final CharSequence[] houseNames = { "火车站", "停车场", "公交站", "加油站", "学校", "服务大厅", "医院", "警察局", "银行",
                "回收站", "垃圾处理厂", "地铁站", "快餐店", "美甲店", "照相馆", "移动营业厅", "游戏厅", "运气屋", "打印店", "书店", "杂货店", "科普馆", "菜场",
                "汉堡店", "咖啡厅", "餐馆", "水果店", "甜品店", "服装店", "支课堂", "花店", "药店", "宠物店", "庄园", "全家便利店", "平房", "公寓", "别墅",
                "居民楼", "风力发电站", "自来水厂", "日化厂", "玩具厂", "污水处理厂", "体育馆", "电影院", "新疆棉花厂", "超市", "游泳馆", "酒店", "商场", "果园",
                "公园", "家具厂", "芯片厂", "化工厂", "火电站", "快递驿站", "宿舍楼", "食堂", "行政楼", "美食城", "造纸厂", "拍卖行", "喵小馆", "神秘研究所" };

        public CharSequence houseName() {
            return houseNames[ordinal()];
        }
    }

    public static void start() {
        if (!Config.omegakoiTown())
            return;

        long executeTime = RuntimeInfo.getInstance().getLong("omegakoiTown", 0);
        if (System.currentTimeMillis() - executeTime < 21600000) {
            return;
        }
        RuntimeInfo.getInstance().put("omegakoiTown", System.currentTimeMillis());

        new Thread() {
            @Override
            public void run() {
                try {
                    getUserTasks();
                    getSignInStatus();
                    houseProduct();
                } catch (Throwable t) {
                    Log.i(TAG, "start.run err:");
                    Log.printStackTrace(TAG, t);
                }
            }
        }.start();
    }

    private static void getUserTasks() {
        try {
            String s = OmegakoiTownRpcCall.getUserTasks();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject result = jo.getJSONObject("result");
                JSONArray tasks = result.getJSONArray("tasks");
                for (int i = 0; i < tasks.length(); i++) {
                    jo = tasks.getJSONObject(i);
                    boolean done = jo.getBoolean("done");
                    boolean hasRewarded = jo.getBoolean("hasRewarded");
                    if (done && !hasRewarded) {
                        JSONObject task = jo.getJSONObject("task");
                        String name = task.getString("name");
                        String taskId = task.getString("taskId");
                        if ("dailyBuild".equals(taskId))
                            continue;
                        int amount = task.getJSONObject("reward").getInt("amount");
                        String itemId = task.getJSONObject("reward").getString("itemId");
                        try {
                            RewardType rewardType = RewardType.valueOf(itemId);
                            jo = new JSONObject(OmegakoiTownRpcCall.triggerTaskReward(taskId));
                            if (jo.getBoolean("success")) {
                                Log.other("小镇任务🌇[" + name + "]#" + amount + "[" + rewardType.rewardName() + "]");
                            }
                        } catch (Throwable th) {
                            Log.i(TAG, "spec RewardType:" + itemId + ";未知的类型");
                        }
                    }
                }
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "getUserTasks err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void getSignInStatus() {
        try {
            String s = OmegakoiTownRpcCall.getSignInStatus();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                boolean signed = jo.getJSONObject("result").getBoolean("signed");
                if (!signed) {
                    jo = new JSONObject(OmegakoiTownRpcCall.signIn());
                    JSONObject diffItem = jo.getJSONObject("result").getJSONArray("diffItems").getJSONObject(0);
                    int amount = diffItem.getInt("amount");
                    String itemId = diffItem.getString("itemId");
                    RewardType rewardType = RewardType.valueOf(itemId);
                    Log.other("小镇签到[" + rewardType.rewardName() + "]#" + amount);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "getSignInStatus err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void houseProduct() {
        try {
            String s = OmegakoiTownRpcCall.houseProduct();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONObject result = jo.getJSONObject("result");
                JSONArray userHouses = result.getJSONArray("userHouses");
                for (int i = 0; i < userHouses.length(); i++) {
                    jo = userHouses.getJSONObject(i);
                    JSONObject extraInfo = jo.getJSONObject("extraInfo");
                    if (!extraInfo.has("toBeCollected"))
                        continue;
                    JSONArray toBeCollected = extraInfo.optJSONArray("toBeCollected");
                    if (toBeCollected != null && toBeCollected.length() > 0) {
                        double amount = toBeCollected.getJSONObject(0).getDouble("amount");
                        if (amount < 500)
                            continue;
                        String houseId = jo.getString("houseId");
                        long id = jo.getLong("id");
                        jo = new JSONObject(OmegakoiTownRpcCall.collect(houseId, id));
                        if (jo.getBoolean("success")) {
                            HouseType houseType = HouseType.valueOf(houseId);
                            String itemId = jo.getJSONObject("result").getJSONArray("rewards").getJSONObject(0)
                                    .getString("itemId");
                            RewardType rewardType = RewardType.valueOf(itemId);
                            Log.other("小镇收金🌇[" + houseType.houseName() + "]#" + String.format("%.2f", amount)
                                    + rewardType.rewardName());
                        }
                    }
                }
            } else {
                Log.recordLog(jo.getString("resultDesc"), s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "getUserTasks err:");
            Log.printStackTrace(TAG, t);
        }
    }

}
