package pansong291.xposed.quickenergy.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class OptionsAdapter extends BaseAdapter
{
    @SuppressLint("StaticFieldLeak")
    private static OptionsAdapter adapter;

    public static OptionsAdapter get(Context c)
    {
        if(adapter == null)
            adapter = new OptionsAdapter(c);
        return adapter;
    }

    Context context;
    ArrayList<String> list;

    private OptionsAdapter(Context c)
    {
        context = c;
        list = new ArrayList<>();
        list.add("View the forest");
        list.add("View the farm");
        list.add("Delete");
    }

    @Override
    public int getCount()
    {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int p1)
    {
        return list.get(p1);
    }

    @Override
    public long getItemId(int p1)
    {
        return p1;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int p1, View p2, ViewGroup p3)
    {
        if(p2 == null)
        {
            p2 = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
        }
        TextView txt = (TextView) p2;
        txt.setText(getItem(p1).toString());
        return p2;
    }

}
