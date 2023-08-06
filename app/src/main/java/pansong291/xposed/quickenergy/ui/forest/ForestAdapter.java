package pansong291.xposed.quickenergy.ui.forest;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import pansong291.xposed.quickenergy.core.common.Adapter;

/**
 * 森林设置适配器
 *
 * @author CLS
 * @since 2023/08/01
 */
public class ForestAdapter extends Adapter {

    public ForestAdapter(Context mContext, ArrayList<Integer> mList, Map<Integer, Object> mSelectedMap) {
        super(mContext, mList, mSelectedMap);
    }
}