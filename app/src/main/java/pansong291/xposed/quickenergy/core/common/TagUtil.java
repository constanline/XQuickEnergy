package pansong291.xposed.quickenergy.core.common;

import android.graphics.Color;
import android.view.View;

import pansong291.xposed.quickenergy.R;

/**
 * 声明标签工具类
 *
 * @author CLS
 * @since 2023/08/05
 */
public class TagUtil {
    //URL标签
    public static final String URL = "url";
    //关于标签
    public static final String CONCERNING = "concerning";
    //switch标签
    public static final String SETTING_TRUE = "true", SETTING_FALSE = "false";
    //按钮标签
    public static final String BUTTON = "button";
    //设置标签
    public static final String SETTING = "setting";


    /**
     * 设置标签
     *
     * @param holder   ViewHolder
     * @param tagTitle 标签内容
     * @param onOff    是否启用Switch开关
     */
    public static void showSingleTag(ViewHolder holder, String tagTitle, boolean onOff) {
        if (!onOff) {
            holder.getSwitch_item().setVisibility(View.GONE);
        }
        holder.getTagName().setText(tagTitle);
    }

    /**
     * 设置标签字体和背景颜色
     *
     * @param tag_setting_background 背景颜色
     * @param parseColor             字体颜色
     */
    public static void setTagSizeBackgroundColor(ViewHolder holder, int tag_setting_background, int parseColor) {
        holder.getTagName().setBackgroundResource(tag_setting_background);
        holder.getTagName().setTextColor(parseColor);
    }
}
