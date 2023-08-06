package pansong291.xposed.quickenergy.ui.other;

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
public class OtherAdapter extends Adapter {

    public OtherAdapter(Context mContext, ArrayList<Integer> mList, Map<Integer, Object> mSelectedMap) {
        super(mContext, mList, mSelectedMap);
    }
}