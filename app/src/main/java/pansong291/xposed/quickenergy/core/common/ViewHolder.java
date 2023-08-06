package pansong291.xposed.quickenergy.core.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suke.widget.SwitchButton;

import pansong291.xposed.quickenergy.R;

/**
 * ListView Holder
 *
 * @author CLS
 * @since 2023/08/03
 */
public class ViewHolder {
    /*Switch*/
    private SwitchButton switch_item;
    /*列表标题*/
    private TextView title;
    /*标签名称*/
    private TextView tagName;

    /**
     * 动态初始化列表视图
     *
     * @param context 上下文环境
     * @param parent  视图组
     */
    public View init(Context context, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.list_item_switch, parent, false);
        this.switch_item = convertView.findViewById(R.id.switch_item);
        this.title = convertView.findViewById(R.id.title);
        this.tagName = convertView.findViewById(R.id.tagName);
        return convertView;
    }

    public SwitchButton getSwitch_item() {
        return switch_item;
    }

    public void setSwitch_item(SwitchButton switch_item) {
        this.switch_item = switch_item;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getTagName() {
        return tagName;
    }

    public void setTagName(TextView tagName) {
        this.tagName = tagName;
    }
}
