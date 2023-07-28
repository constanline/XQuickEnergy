package pansong291.xposed.quickenergy.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pansong291.xposed.quickenergy.util.CityCodeMap;

public class CityCode extends IdAndName {

    private static List<CityCode> list;

    public CityCode(String i, String n) {
        id = i;
        name = n;
    }

    public static List<CityCode> getList() {
        if (list == null || CityCodeMap.shouldReload) {
            list = new ArrayList<>();
            Set<Map.Entry<String, String>> idSet = CityCodeMap.getIdMap().entrySet();
            for (Map.Entry<String, String> entry : idSet) {
                list.add(new CityCode(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    public static void remove(String id) {
        getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id.equals(id)) {
                list.remove(i);
                break;
            }
        }
    }

}
