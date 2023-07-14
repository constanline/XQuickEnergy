package pansong291.xposed.quickenergy.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
import pansong291.xposed.quickenergy.util.Config;

public class EditDialog
{
    private static AlertDialog editDialog;
    private static EditText edt;
    public enum EditMode
    { CHECK_INTERVAL, THREAD_COUNT, ADVANCE_TIME, COLLECT_INTERVAL, LIMIT_COUNT,
        COLLECT_TIMEOUT, RETURN_WATER_30, RETURN_WATER_20, RETURN_WATER_10,
        MIN_EXCHANGE_COUNT, LATEST_EXCHANGE_TIME }
    private static EditMode mode;

    public static void showEditDialog(Context c, CharSequence title, EditMode em)
    {
        mode = em;
        try
        {
            getEditDialog(c).show();
        }catch(Throwable t)
        {
            editDialog = null;
            getEditDialog(c).show();
        }
        editDialog.setTitle(title);
    }

    private static AlertDialog getEditDialog(Context c)
    {
        if(editDialog == null)
        {
            edt = new EditText(c);
            editDialog = new AlertDialog.Builder(c)
                    .setTitle("title")
                    .setView(edt)
                    .setPositiveButton(
                            "OK",
                            new OnClickListener()
                            {
                                Context context;

                                public OnClickListener setData(Context c)
                                {
                                    context = c;
                                    return this;
                                }

                                @Override
                                public void onClick(DialogInterface p1, int p2)
                                {
                                    try
                                    {
                                        int i = Integer.parseInt(edt.getText().toString());
                                        switch(mode)
                                        {
                                            case CHECK_INTERVAL:
                                                if(i > 0)
                                                    Config.setCheckInterval(i * 60_000);
                                                break;

                                            case THREAD_COUNT:
                                                if(i >= 0)
                                                    Config.setThreadCount(i);
                                                break;

                                            case ADVANCE_TIME:
                                                Config.setAdvanceTime(i);
                                                break;

                                            case COLLECT_INTERVAL:
                                                if(i >= 0)
                                                    Config.setCollectInterval(i);
                                                break;

                                            case LIMIT_COUNT:
                                                if (i > 0) {
                                                    Config.setLimitCount(i);
                                                }
                                                break;

                                            case COLLECT_TIMEOUT:
                                                if(i > 0)
                                                    Config.setCollectTimeout(i * 1_000);
                                                break;

                                            case RETURN_WATER_30:
                                                if(i >= 0)
                                                    Config.setReturnWater30(i);
                                                break;

                                            case RETURN_WATER_20:

                                                if(i >= 0)
                                                    Config.setReturnWater20(i);
                                                break;

                                            case RETURN_WATER_10:
                                                if(i >= 0)
                                                    Config.setReturnWater10(i);
                                                break;

                                            case MIN_EXCHANGE_COUNT:
                                                if(i >= 0)
                                                    Config.setMinExchangeCount(i);
                                                break;

                                            case LATEST_EXCHANGE_TIME:
                                                if(i >= 0 && i < 24)
                                                    Config.setLatestExchangeTime(i);
                                                break;

                                        }
                                    }catch(Throwable t)
                                    {}
                                }
                            }.setData(c))
                    .create();
        }
        String str = "";
        switch(mode)
        {
            case CHECK_INTERVAL:
                str = String.valueOf(Config.checkInterval() / 60_000);
                break;

            case THREAD_COUNT:
                str = String.valueOf(Config.threadCount());
                break;

            case ADVANCE_TIME:
                str = String.valueOf(Config.advanceTime());
                break;

            case COLLECT_INTERVAL:
                str = String.valueOf(Config.collectInterval());
                break;

            case LIMIT_COUNT:
                str = String.valueOf(Config.getLimitCount());
                break;

            case COLLECT_TIMEOUT:
                str = String.valueOf(Config.collectTimeout() / 1_000);
                break;

            case RETURN_WATER_30:
                str = String.valueOf(Config.returnWater30());
                break;

            case RETURN_WATER_20:
                str = String.valueOf(Config.returnWater20());
                break;

            case RETURN_WATER_10:
                str = String.valueOf(Config.returnWater10());
                break;

            case MIN_EXCHANGE_COUNT:
                str = String.valueOf(Config.minExchangeCount());
                break;

            case LATEST_EXCHANGE_TIME:
                str = String.valueOf(Config.latestExchangeTime());
                break;

        }
        edt.setText(str);
        return editDialog;
    }

}
