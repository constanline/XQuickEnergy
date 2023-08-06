package pansong291.xposed.quickenergy.core.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Map;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.ui.forest.ForestAdapter;

/**
 * 适配器
 *
 * @author CLS
 * @since 2023/08/03
 */
public class Adapter extends BaseAdapter {
    public final Context context;
    public final ArrayList<Integer> list;
    public final Map<Integer, Object> selectedMap;
    public ForestAdapter.SetOnClickDialogListener mSetOnClickDialogListener;

    public void OnSetOnClickDialogListener(ForestAdapter.SetOnClickDialogListener listener) {
        this.mSetOnClickDialogListener = listener;
    }

    public interface SetOnClickDialogListener {
        void onClickDialogListener(int type, boolean boolClick);
    }

    public Adapter(Context mContext, ArrayList<Integer> mList, Map<Integer, Object> mSelectedMap) {
        this.context = mContext;
        this.list = mList;
        this.selectedMap = mSelectedMap;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String valueOf = String.valueOf(selectedMap.get(list.get(position)));
        //动态初始化视图
        holder = new ViewHolder();
        convertView = holder.init(context, parent);
        switch (valueOf) {
            case TagUtil.SETTING_TRUE:
            case TagUtil.SETTING_FALSE:
                //选择标签
                TagUtil.showSingleTag(holder, "选择", true);
                TagUtil.setTagSizeBackgroundColor(holder, R.drawable.tag_switch_background, Color.parseColor("#67c23a"));
                holder.getSwitch_item().setChecked((Boolean) selectedMap.get(list.get(position)));
                holder.getSwitch_item().setOnCheckedChangeListener((view, isChecked) -> {
                    if (mSetOnClickDialogListener != null) {
                        mSetOnClickDialogListener.onClickDialogListener(position, isChecked);
                    }
                });
                break;
            case TagUtil.URL:
                TagUtil.showSingleTag(holder, "URL", false);
                TagUtil.setTagSizeBackgroundColor(holder, R.drawable.tag_url_background, Color.parseColor("#43C699"));
                break;
            case TagUtil.CONCERNING:
                TagUtil.showSingleTag(holder, "关于", false);
                TagUtil.setTagSizeBackgroundColor(holder, R.drawable.tag_concerning_background, Color.parseColor("#B6B6B6"));
                break;
            case TagUtil.BUTTON:
                TagUtil.showSingleTag(holder, "按钮", false);
                TagUtil.setTagSizeBackgroundColor(holder, R.drawable.tag_button_background, Color.parseColor("#40E9FF"));
                break;
            case TagUtil.SETTING:
                TagUtil.showSingleTag(holder, "设置", false);
                TagUtil.setTagSizeBackgroundColor(holder, R.drawable.tag_setting_background, Color.parseColor("#409eff"));
                break;
            default:
                TagUtil.showSingleTag(holder, "其它", false);
                TagUtil.setTagSizeBackgroundColor(holder, R.drawable.tag_other_background, Color.parseColor("#9CA1B5"));
                break;
        }
        convertView.setTag(holder);
        if (holder.getTitle() != null) if (holder.getTitle().getText() != null) {
            holder.getTitle().setText(list.get(position));
        }
        return convertView;
    }


}

