package pansong291.xposed.quickenergy.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pansong291.xposed.quickenergy.util.CooperationIdMap;

public class CooperateUser extends IdAndName {
    private static List<CooperateUser> list;

    public CooperateUser(String i, String n) {
        id = i;
        name = n;
    }

    public static List<CooperateUser> getList() {
        if(list == null || CooperationIdMap.shouldReload) {
            list = new ArrayList<>();
            Set<Map.Entry<String, String>> idSet = CooperationIdMap.getIdMap().entrySet();
            for (Map.Entry<String, String> entry: idSet) {
                list.add(new CooperateUser(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    public static void remove(String id) {
        getList();
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).id.equals(id)) {
                list.remove(i);
                break;
            }
        }
    }

}
