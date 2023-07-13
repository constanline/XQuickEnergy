package pansong291.xposed.quickenergy.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import pansong291.xposed.quickenergy.AntFarm.SendType;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.Config.RecallAnimalType;

public class ChoiceDialog
{
    private static AlertDialog
            sendTypeDialog, recallAnimalTypeDialog;

    public static void showSendType(Context c, CharSequence title)
    {
        try
        {
            getSendTypeDialog(c, title).show();
        }catch(Throwable t)
        {
            sendTypeDialog = null;
            getSendTypeDialog(c, title).show();
        }
    }

    private static AlertDialog getSendTypeDialog(Context c, CharSequence title)
    {
        if(sendTypeDialog == null)
            sendTypeDialog = new AlertDialog.Builder(c)
                    .setTitle(title)
                    .setSingleChoiceItems(SendType.names, Config.sendType().ordinal(),
                            new OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface p1, int p2)
                                {
                                    Config.setSendType(p2);
                                }
                            })
                    .setPositiveButton("OK", null)
                    .create();
        return sendTypeDialog;
    }

    public static void showRecallAnimalType(Context c, CharSequence title)
    {
        try
        {
            getRecallAnimalTypeDialog(c, title).show();
        }catch(Throwable t)
        {
            recallAnimalTypeDialog = null;
            getRecallAnimalTypeDialog(c, title).show();
        }
    }

    private static AlertDialog getRecallAnimalTypeDialog(Context c, CharSequence title)
    {
        if(recallAnimalTypeDialog == null)
            recallAnimalTypeDialog = new AlertDialog.Builder(c)
                    .setTitle(title)
                    .setSingleChoiceItems(RecallAnimalType.names, Config.recallAnimalType().ordinal(),
                            new OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface p1, int p2)
                                {
                                    Config.setRecallAnimalType(p2);
                                }
                            })
                    .setPositiveButton("OK", null)
                    .create();
        return recallAnimalTypeDialog;
    }

}
