package pansong291.xposed.quickenergy.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pansong291.xposed.quickenergy.util.BeachIdMap;

public class AlipayBeach extends IdAndName {
    private static List<AlipayBeach> list;

    public AlipayBeach(String i, String n) {
        id = i;
        name = n;
    }

    public static List<AlipayBeach> getList() {
        if (list == null || BeachIdMap.shouldReload) {
            list = new ArrayList<>();
            Set<Map.Entry<String, String>> idSet = BeachIdMap.getIdMap().entrySet();
            for (Map.Entry<String, String> entry: idSet) {
                list.add(new AlipayBeach(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    public static void remove(String id) {
        getList();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).id.equals(id)) {
                list.remove(i);
                break;
            }
        }
    }

}
