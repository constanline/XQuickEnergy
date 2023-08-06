package pansong291.xposed.quickenergy.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pansong291.xposed.quickenergy.util.FileUtils;
import pansong291.xposed.quickenergy.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AreaCode extends IdAndName {
    private static final String TAG = AreaCode.class.getCanonicalName();
    private static List<AreaCode> list;

    public AreaCode(String i, String n) {
        id = i;
        name = n;
    }

    public static List<AreaCode> getList() {
        if (list == null) {
            String cityCode = FileUtils.readFromFile(FileUtils.getCityCodeFile());
            JSONArray ja;
            try {
                ja = new JSONArray(cityCode);
            } catch (Throwable e) {
                cityCode = "[" +
                        "{\"cityCode\":\"320100\",\"cityName\":\"南京市\"}," +
                        "{\"cityCode\":\"330100\",\"cityName\":\"杭州市\"}," +
                        "{\"cityCode\":\"350100\",\"cityName\":\"福州市\"}," +
                        "{\"cityCode\":\"370100\",\"cityName\":\"济南市\"}," +
                        "{\"cityCode\":\"430100\",\"cityName\":\"长沙市\"}," +
                        "{\"cityCode\":\"440100\",\"cityName\":\"广州市\"}" +
                        "]";
                try {
                    ja = new JSONArray(cityCode);
                } catch (JSONException ex) {
                    ja = new JSONArray();
                }
            }

            list = new ArrayList<>();
            try {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    list.add(new AreaCode(jo.getString("cityCode"), jo.getString("cityName")));
                }
            } catch (Throwable th) {
                Log.printStackTrace(TAG, th);
            }
        }
        return list;
    }

}
