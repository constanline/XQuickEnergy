package pansong291.xposed.quickenergy.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.util.Config;

public class EditDialog {
    public enum EditMode {
        TOAST_OFFSET_Y, CHECK_INTERVAL, THREAD_COUNT, ADVANCE_TIME, COLLECT_INTERVAL, LIMIT_COUNT, DOUBLE_CARD_TIME,
        DOUBLE_COUNT_LIMIT, COLLECT_TIMEOUT, RETURN_WATER_30, RETURN_WATER_20, RETURN_WATER_10, WATER_FRIEND_COUNT,
        FARM_GAME_TIME,
        ANIMAL_SLEEP_TIME,
        MIN_EXCHANGE_COUNT, LATEST_EXCHANGE_TIME, SYNC_STEP_COUNT, WAIT_WHEN_EXCEPTION,
        EXCHANGE_ENERGY_DOUBLE_CLICK_COUNT
    }

    private static EditMode mode;

    public static void showEditDialog(Context c, CharSequence title, EditMode em) {
        showEditDialog(c, title, em, null);
    }

    public static void showEditDialog(Context c, CharSequence title, EditMode em, String msg) {
        mode = em;
        AlertDialog editDialog = getEditDialog(c);
        if (msg != null) {
            editDialog.setTitle(title);
            editDialog.setMessage(msg);
        } else {
            editDialog.setTitle(title);
        }
        editDialog.show();
    }

    private static AlertDialog getEditDialog(Context c) {
        EditText edt = new EditText(c);
        AlertDialog editDialog = new AlertDialog.Builder(c)
                .setTitle("title")
                .setView(edt)
                .setPositiveButton(
                        c.getString(R.string.ok),
                        new OnClickListener() {
                            Context context;

                            public OnClickListener setData(Context c) {
                                context = c;
                                return this;
                            }

                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                try {
                                    int i = 0;
                                    try {
                                        i = Integer.parseInt(edt.getText().toString());
                                    } catch (Throwable ignored) {
                                    }
                                    switch (mode) {
                                        case TOAST_OFFSET_Y:
                                            Config.setToastOffsetY(i);
                                            break;

                                        case CHECK_INTERVAL:
                                            if (i > 0)
                                                Config.setCheckInterval(i * 60_000);
                                            break;

                                        case ADVANCE_TIME:
                                            Config.setAdvanceTime(i);
                                            break;

                                        case COLLECT_INTERVAL:
                                            if (i >= 0)
                                                Config.setCollectInterval(i);
                                            break;

                                        case LIMIT_COUNT:
                                            if (i > 0) {
                                                Config.setLimitCount(i);
                                            }
                                            break;
                                        case DOUBLE_CARD_TIME:
                                            Config.setDoubleCardTime(edt.getText().toString());
                                            break;

                                        case DOUBLE_COUNT_LIMIT:
                                            if (i < 0)
                                                i = 0;
                                            Config.setDoubleCountLimit(i);
                                            break;

                                        case COLLECT_TIMEOUT:
                                            if (i > 0)
                                                Config.setCollectTimeout(i * 1_000);
                                            break;

                                        case RETURN_WATER_30:
                                            if (i >= 0)
                                                Config.setReturnWater33(i);
                                            break;

                                        case RETURN_WATER_20:

                                            if (i >= 0)
                                                Config.setReturnWater18(i);
                                            break;

                                        case RETURN_WATER_10:
                                            if (i >= 0)
                                                Config.setReturnWater10(i);
                                            break;

                                        case WATER_FRIEND_COUNT:
                                            if (i >= 0)
                                                Config.setWaterFriendCount(i);
                                            break;

                                        case FARM_GAME_TIME:
                                            Config.setFarmGameTime(edt.getText().toString());
                                            break;

                                        case ANIMAL_SLEEP_TIME:
                                            Config.setAnimalSleepTime(edt.getText().toString());
                                            break;

                                        case MIN_EXCHANGE_COUNT:
                                            if (i >= 0)
                                                Config.setMinExchangeCount(i);
                                            break;

                                        case LATEST_EXCHANGE_TIME:
                                            if (i >= 0 && i < 24)
                                                Config.setLatestExchangeTime(i);
                                            break;
                                        case SYNC_STEP_COUNT:
                                            if (i > 100000)
                                                i = 100000;
                                            if (i < 0)
                                                i = 0;
                                            Config.setSyncStepCount(i);
                                            break;
                                        case WAIT_WHEN_EXCEPTION:
                                            if (i < 0)
                                                i = 0;
                                            Config.setWaitWhenException(i * 60 * 1000);
                                            break;

                                        case EXCHANGE_ENERGY_DOUBLE_CLICK_COUNT:
                                            if (i < 0)
                                                i = 0;
                                            Config.setExchangeEnergyDoubleClickCount(i);
                                            break;

                                    }
                                } catch (Throwable ignored) {
                                }
                            }
                        }.setData(c))
                .create();
        String str = "";
        switch (mode) {
            case TOAST_OFFSET_Y:
                str = String.valueOf(Config.toastOffsetY());
                break;

            case CHECK_INTERVAL:
                str = String.valueOf(Config.checkInterval() / 60_000);
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
            case DOUBLE_CARD_TIME:
                str = Config.doubleCardTime();
                break;

            case DOUBLE_COUNT_LIMIT:
                str = String.valueOf(Config.getDoubleCountLimit());
                break;

            case COLLECT_TIMEOUT:
                str = String.valueOf(Config.collectTimeout() / 1_000);
                break;

            case RETURN_WATER_30:
                str = String.valueOf(Config.returnWater33());
                break;

            case RETURN_WATER_20:
                str = String.valueOf(Config.returnWater18());
                break;

            case RETURN_WATER_10:
                str = String.valueOf(Config.returnWater10());
                break;

            case WATER_FRIEND_COUNT:
                str = String.valueOf(Config.waterFriendCount());
                break;

            case FARM_GAME_TIME:
                str = Config.farmGameTime();
                break;

            case ANIMAL_SLEEP_TIME:
                str = Config.animalSleepTime();
                break;

            case MIN_EXCHANGE_COUNT:
                str = String.valueOf(Config.minExchangeCount());
                break;

            case LATEST_EXCHANGE_TIME:
                str = String.valueOf(Config.latestExchangeTime());
                break;

            case SYNC_STEP_COUNT:
                str = String.valueOf(Config.syncStepCount());
                break;

            case WAIT_WHEN_EXCEPTION:
                str = String.valueOf(Config.waitWhenException() / 60 / 1000);
                break;

            case EXCHANGE_ENERGY_DOUBLE_CLICK_COUNT:
                str = String.valueOf(Config.getExchangeEnergyDoubleClickCount());
                break;
        }
        edt.setText(str);
        return editDialog;
    }

}
