package pansong291.xposed.quickenergy.ui.setting;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import pansong291.xposed.quickenergy.core.common.Adapter;

/**
 * 基础设置适配器
 *
 * @author CLS
 * @since 2023/08/01
 */
public class SettingAdapter extends Adapter {

    public SettingAdapter(Context mContext, ArrayList<Integer> mList, Map<Integer, Object> mSelectedMap) {
        super(mContext, mList, mSelectedMap);
    }
}