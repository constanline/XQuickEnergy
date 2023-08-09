package pansong291.xposed.quickenergy.entity;

import pansong291.xposed.quickenergy.util.HanziToPinyin;

import java.util.ArrayList;
import java.util.List;

public class IdAndName {

    public String name;

    public String id;

    private ArrayList<String> pinyin;

    public ArrayList<String> getPinyin() {
        if (pinyin != null) {
            return pinyin;
        }
        ArrayList<HanziToPinyin.Token> list = HanziToPinyin.getInstance().get(name);
        pinyin = new ArrayList<>(list.size());
        for (HanziToPinyin.Token token : list) {
            pinyin.add(token.target);
        }
        return pinyin;
    }

    public static int Compare(IdAndName o1, IdAndName o2) {
        List<String> list1 = o1.getPinyin();
        List<String> list2 = o2.getPinyin();
        int i = 0;
        while (i < list1.size() && i < list2.size()) {
            if (list1.get(i).compareTo(list2.get(i)) != 0) {
                return list1.get(i).compareTo(list2.get(i));
            }
            i++;
        }
        return list1.size() - list2.size();
    }
}
