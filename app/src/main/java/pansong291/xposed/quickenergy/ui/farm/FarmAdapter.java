package pansong291.xposed.quickenergy.ui.farm;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import pansong291.xposed.quickenergy.core.common.Adapter;

/**
 * 农场设置适配器
 *
 * @author CLS
 * @since 2023/08/01
 */
public class FarmAdapter extends Adapter {

    public FarmAdapter(Context mContext, ArrayList<Integer> mList, Map<Integer, Object> mSelectedMap) {
        super(mContext, mList, mSelectedMap);
    }
}