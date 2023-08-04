package pansong291.xposed.quickenergy.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.util.Config;
import pansong291.xposed.quickenergy.util.CooperationIdMap;
import pansong291.xposed.quickenergy.util.FriendIdMap;
import pansong291.xposed.quickenergy.util.ReserveIdMap;
import pansong291.xposed.quickenergy.util.BeachIdMap;
import pansong291.xposed.quickenergy.util.CityCodeMap;

public class SettingsActivity extends Activity {
    Switch cb_immediateEffect, cb_recordLog, cb_showToast,
            cb_stayAwake, cb_timeoutRestart, cb_collectWateringBubble,
            cb_collectEnergy, cb_helpFriendCollect, cb_receiveForestTaskAward,
            cb_cooperateWater, cb_energyRain,
            cb_enableFarm, cb_rewardFriend, cb_sendBackAnimal,
            cb_receiveFarmToolReward, cb_useNewEggTool, cb_harvestProduce,
            cb_donation, cb_answerQuestion, cb_receiveFarmTaskAward,
            cb_feedAnimal, cb_useAccelerateTool, cb_notifyFriend,
            cb_receivePoint, cb_openTreasureBox, cb_donateCharityCoin,
            cb_kbSignIn, cb_limitCollect, cb_doubleCard, cb_ExchangeEnergyDoubleClick, cb_reserve, cb_ecoLifeTick,
            cb_ancientTree, cb_ancientTreeOnlyWeek, cb_receiveCoinAsset, cb_antdodoCollect, cb_recordFarmGame, sw_beach,
            cb_kitchen, cb_antOcean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Config.shouldReload = true;
        FriendIdMap.shouldReload = true;
        CooperationIdMap.shouldReload = true;
        ReserveIdMap.shouldReload = true;
        BeachIdMap.shouldReload = true;
        CityCodeMap.shouldReload = true;

        cb_immediateEffect = findViewById(R.id.cb_immediateEffect);
        cb_recordLog = findViewById(R.id.cb_recordLog);
        cb_showToast = findViewById(R.id.cb_showToast);
        cb_stayAwake = findViewById(R.id.cb_stayAwake);
        cb_timeoutRestart = findViewById(R.id.cb_timeoutRestart);
        cb_collectEnergy = findViewById(R.id.cb_collectEnergy);
        cb_collectWateringBubble = findViewById(R.id.cb_collectWateringBubble);
        cb_helpFriendCollect = findViewById(R.id.cb_helpFriendCollect);
        cb_receiveForestTaskAward = findViewById(R.id.cb_receiveForestTaskAward);
        cb_cooperateWater = findViewById(R.id.cb_cooperateWater);
        cb_ancientTree = findViewById(R.id.cb_ancientTree);
        cb_energyRain = findViewById(R.id.cb_energyRain);
        cb_reserve = findViewById(R.id.cb_reserve);
        sw_beach = findViewById(R.id.sw_beach);
        cb_enableFarm = findViewById(R.id.cb_enableFarm);
        cb_rewardFriend = findViewById(R.id.cb_rewardFriend);
        cb_sendBackAnimal = findViewById(R.id.cb_sendBackAnimal);
        cb_receiveFarmToolReward = findViewById(R.id.cb_receiveFarmToolReward);
        cb_recordFarmGame = findViewById(R.id.cb_recordFarmGame);
        cb_kitchen = findViewById(R.id.cb_kitchen);
        cb_useNewEggTool = findViewById(R.id.cb_useNewEggTool);
        cb_harvestProduce = findViewById(R.id.cb_harvestProduce);
        cb_donation = findViewById(R.id.cb_donation);
        cb_answerQuestion = findViewById(R.id.cb_answerQuestion);
        cb_receiveFarmTaskAward = findViewById(R.id.cb_receiveFarmTaskAward);
        cb_feedAnimal = findViewById(R.id.cb_feedAnimal);
        cb_useAccelerateTool = findViewById(R.id.cb_useAccelerateTool);
        cb_notifyFriend = findViewById(R.id.cb_notifyFriend);
        cb_receivePoint = findViewById(R.id.cb_receivePoint);
        cb_openTreasureBox = findViewById(R.id.cb_openTreasureBox);
        cb_receiveCoinAsset = findViewById(R.id.cb_receiveCoinAsset);
        cb_donateCharityCoin = findViewById(R.id.cb_donateCharityCoin);
        cb_kbSignIn = findViewById(R.id.cb_kbSignIn);
        cb_limitCollect = findViewById(R.id.cb_limitCollect);
        cb_doubleCard = findViewById(R.id.cb_doubleCard);
        cb_ExchangeEnergyDoubleClick = findViewById(R.id.cb_ExchangeEnergyDoubleClick);
        cb_ecoLifeTick = findViewById(R.id.cb_ecoLifeTick);
        cb_ancientTreeOnlyWeek = findViewById(R.id.cb_ancientTreeOnlyWeek);
        cb_antdodoCollect = findViewById(R.id.cb_antdodoCollect);
        cb_antOcean = findViewById(R.id.cb_antOcean);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cb_immediateEffect.setChecked(Config.immediateEffect());
        cb_recordLog.setChecked(Config.recordLog());
        cb_showToast.setChecked(Config.showToast());
        cb_stayAwake.setChecked(Config.stayAwake());
        cb_timeoutRestart.setChecked(Config.timeoutRestart());
        cb_collectEnergy.setChecked(Config.collectEnergy());
        cb_collectWateringBubble.setChecked(Config.collectWateringBubble());
        cb_helpFriendCollect.setChecked(Config.helpFriendCollect());
        cb_receiveForestTaskAward.setChecked(Config.receiveForestTaskAward());
        cb_cooperateWater.setChecked(Config.cooperateWater());
        cb_ancientTree.setChecked(Config.ancientTree());
        cb_energyRain.setChecked(Config.energyRain());
        cb_reserve.setChecked(Config.reserve());
        sw_beach.setChecked(Config.beach());
        cb_enableFarm.setChecked(Config.enableFarm());
        cb_rewardFriend.setChecked(Config.rewardFriend());
        cb_sendBackAnimal.setChecked(Config.sendBackAnimal());
        cb_receiveFarmToolReward.setChecked(Config.receiveFarmToolReward());
        cb_recordFarmGame.setChecked(Config.recordFarmGame());
        cb_kitchen.setChecked(Config.kitchen());
        cb_useNewEggTool.setChecked(Config.useNewEggTool());
        cb_harvestProduce.setChecked(Config.harvestProduce());
        cb_donation.setChecked(Config.donation());
        cb_answerQuestion.setChecked(Config.answerQuestion());
        cb_receiveFarmTaskAward.setChecked(Config.receiveFarmTaskAward());
        cb_feedAnimal.setChecked(Config.feedAnimal());
        cb_useAccelerateTool.setChecked(Config.useAccelerateTool());
        cb_notifyFriend.setChecked(Config.notifyFriend());
        cb_receivePoint.setChecked(Config.receivePoint());
        cb_openTreasureBox.setChecked(Config.openTreasureBox());
        cb_receiveCoinAsset.setChecked(Config.receiveCoinAsset());
        cb_donateCharityCoin.setChecked(Config.donateCharityCoin());
        cb_kbSignIn.setChecked(Config.kbSginIn());
        cb_limitCollect.setChecked(Config.isLimitCollect());
        cb_doubleCard.setChecked(Config.doubleCard());
        cb_ExchangeEnergyDoubleClick.setChecked(Config.ExchangeEnergyDoubleClick());
        cb_ecoLifeTick.setChecked(Config.ecoLifeTick());
        cb_ancientTreeOnlyWeek.setChecked(Config.ancientTreeOnlyWeek());
        cb_antdodoCollect.setChecked(Config.antdodoCollect());
        cb_antOcean.setChecked(Config.antOcean());
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        if (v instanceof Switch) {
            Switch cb = (Switch) v;
            switch (v.getId()) {
                case R.id.cb_immediateEffect:
                    Config.setImmediateEffect(cb.isChecked());
                    break;

                case R.id.cb_recordLog:
                    Config.setRecordLog(cb.isChecked());
                    break;

                case R.id.cb_showToast:
                    Config.setShowToast(cb.isChecked());
                    break;

                case R.id.cb_stayAwake:
                    Config.setStayAwake(cb.isChecked());
                    break;

                case R.id.cb_timeoutRestart:
                    Config.setTimeoutRestart(cb.isChecked());
                    break;

                case R.id.cb_collectEnergy:
                    Config.setCollectEnergy(cb.isChecked());
                    break;

                case R.id.cb_collectWateringBubble:
                    Config.setCollectWateringBubble(cb.isChecked());
                    break;

                case R.id.cb_limitCollect:
                    Config.setLimitCollect(cb.isChecked());
                    break;

                case R.id.cb_doubleCard:
                    Config.setDoubleCard(cb.isChecked());
                    break;

                case R.id.cb_helpFriendCollect:
                    Config.setHelpFriendCollect(cb.isChecked());
                    break;

                case R.id.cb_receiveForestTaskAward:
                    Config.setReceiveForestTaskAward(cb.isChecked());
                    break;

                case R.id.cb_cooperateWater:
                    Config.setCooperateWater(cb.isChecked());
                    break;

                case R.id.cb_ancientTree:
                    Config.setAncientTree(cb.isChecked());
                    break;

                case R.id.cb_energyRain:
                    Config.setEnergyRain(cb.isChecked());
                    break;

                case R.id.cb_ExchangeEnergyDoubleClick:
                    Config.setExchangeEnergyDoubleClick(cb.isChecked());
                    break;

                case R.id.cb_reserve:
                    Config.setReserve(cb.isChecked());
                    break;

                case R.id.sw_beach:
                    Config.setBeach(cb.isChecked());
                    break;

                case R.id.cb_enableFarm:
                    Config.setEnableFarm(cb.isChecked());
                    break;

                case R.id.cb_rewardFriend:
                    Config.setRewardFriend(cb.isChecked());
                    break;

                case R.id.cb_sendBackAnimal:
                    Config.setSendBackAnimal(cb.isChecked());
                    break;

                case R.id.cb_receiveFarmToolReward:
                    Config.setReceiveFarmToolReward(cb.isChecked());
                    break;

                case R.id.cb_recordFarmGame:
                    Config.setRecordFarmGame(cb.isChecked());
                    break;

                case R.id.cb_kitchen:
                    Config.setKitchen(cb.isChecked());
                    break;

                case R.id.cb_useNewEggTool:
                    Config.setUseNewEggTool(cb.isChecked());
                    break;

                case R.id.cb_harvestProduce:
                    Config.setHarvestProduce(cb.isChecked());
                    break;

                case R.id.cb_donation:
                    Config.setDonation(cb.isChecked());
                    break;

                case R.id.cb_answerQuestion:
                    Config.setAnswerQuestion(cb.isChecked());
                    break;

                case R.id.cb_receiveFarmTaskAward:
                    Config.setReceiveFarmTaskAward(cb.isChecked());
                    break;

                case R.id.cb_feedAnimal:
                    Config.setFeedAnimal(cb.isChecked());
                    break;

                case R.id.cb_useAccelerateTool:
                    Config.setUseAccelerateTool(cb.isChecked());
                    break;

                case R.id.cb_notifyFriend:
                    Config.setNotifyFriend(cb.isChecked());
                    break;

                case R.id.cb_receivePoint:
                    Config.setReceivePoint(cb.isChecked());
                    break;

                case R.id.cb_openTreasureBox:
                    Config.setOpenTreasureBox(cb.isChecked());
                    break;

                case R.id.cb_receiveCoinAsset:
                    Config.setReceiveCoinAsset(cb.isChecked());
                    break;

                case R.id.cb_donateCharityCoin:
                    Config.setDonateCharityCoin(cb.isChecked());
                    break;

                case R.id.cb_kbSignIn:
                    Config.setKbSginIn(cb.isChecked());
                    break;

                case R.id.cb_ecoLifeTick:
                    Config.setEcoLifeTick(cb.isChecked());
                    break;

                case R.id.cb_ancientTreeOnlyWeek:
                    Config.setAncientTreeOnlyWeek(cb.isChecked());
                    break;

                case R.id.cb_antdodoCollect:
                    Config.setAntdodoCollect(cb.isChecked());
                    break;

                case R.id.cb_antOcean:
                    Config.setAntOcean(cb.isChecked());
                    break;
            }
        } else if (v instanceof Button) {
            Button btn = (Button) v;
            switch (v.getId()) {
                case R.id.btn_stayAwakeType:
                    ChoiceDialog.showStayAwakeType(this, btn.getText());
                    break;

                case R.id.btn_stayAwakeTarget:
                    ChoiceDialog.showStayAwakeTarget(this, btn.getText());
                    break;

                case R.id.btn_checkInterval:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.CHECK_INTERVAL);
                    break;

                case R.id.btn_advanceTime:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.ADVANCE_TIME);
                    break;

                case R.id.btn_collectInterval:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.COLLECT_INTERVAL);
                    break;

                case R.id.btn_collectTimeout:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.COLLECT_TIMEOUT);
                    break;

                case R.id.btn_limitCount:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.LIMIT_COUNT);
                    break;

                case R.id.btn_doubleCardTime:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.DOUBLE_CARD_TIME);
                    break;

                case R.id.btn_returnWater30:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.RETURN_WATER_30);
                    break;

                case R.id.btn_returnWater20:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.RETURN_WATER_20);
                    break;

                case R.id.btn_returnWater10:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.RETURN_WATER_10);
                    break;

                case R.id.btn_dontCollectList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getDontCollectList(), null);
                    break;

                case R.id.btn_dontHelpCollectList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getDontHelpCollectList(), null);
                    break;

                case R.id.btn_waterFriendList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getWaterFriendList(),
                            Config.getWaterCountList());
                    break;

                case R.id.btn_waterFriendCount:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.WATER_FRIEND_COUNT);
                    break;

                case R.id.btn_cooperateWaterList:
                    ListDialog.show(this, btn.getText(), CooperateUser.getList(), Config.getCooperateWaterList(),
                            Config.getcooperateWaterNumList());
                    break;

                case R.id.btn_ancientTreeCityCodeList:
                    ListDialog.show(this, btn.getText(), CityCode.getList(), Config.getAncientTreeCityCodeList(),
                            null);
                    break;

                case R.id.btn_giveEnergyRainList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getGiveEnergyRainList(), null);
                    break;

                case R.id.btn_reserveList:
                    ListDialog.show(this, btn.getText(), AlipayReserve.getList(), Config.getReserveList(),
                            Config.getReserveCountList());
                    break;

                case R.id.btn_beachList:
                    ListDialog.show(this, btn.getText(), AlipayBeach.getList(), Config.getBeachList(),
                            Config.getBeachCountList());
                    break;

                case R.id.btn_sendType:
                    ChoiceDialog.showSendType(this, btn.getText());
                    break;

                case R.id.btn_dontSendFriendList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getDontSendFriendList(), null);
                    break;

                case R.id.btn_recallAnimalType:
                    ChoiceDialog.showRecallAnimalType(this, btn.getText());
                    break;

                case R.id.btn_feedFriendAnimalList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getFeedFriendAnimalList(),
                            Config.getFeedFriendCountList());
                    break;

                case R.id.btn_dontNotifyFriendList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getDontNotifyFriendList(), null);
                    break;

                case R.id.btn_animalSleepTime:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.ANIMAL_SLEEP_TIME);
                    break;

                case R.id.btn_donation_developer:
                    new AlertDialog.Builder(this)
                            .setView(R.layout.donation_view)
                            .setPositiveButton("关闭", null)
                            .create().show();
                    break;

                case R.id.btn_donation_xqe_developer:
                    Intent it2 = new Intent(Intent.ACTION_VIEW, Uri.parse(
                            "alipays://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Ftsx00339eflkuhhtfctcn48"));
                    startActivity(it2);
                    break;

                case R.id.btn_minExchangeCount:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.MIN_EXCHANGE_COUNT);
                    break;

                case R.id.btn_latestExchangeTime:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.LATEST_EXCHANGE_TIME);
                    break;

                case R.id.btn_syncStepCount:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.SYNC_STEP_COUNT);
                    break;

                case R.id.btn_waitWhenException:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.WAIT_WHEN_EXCEPTION);
                    break;

                case R.id.btn_ExchangeEnergyDoubleClickCount:
                    EditDialog.showEditDialog(this, btn.getText(),
                            EditDialog.EditMode.EXCHANGE_ENERGY_DOUBLE_CLICK_COUNT);
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Config.hasChanged) {
            Config.hasChanged = !Config.saveConfigFile();
            Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
        }
        FriendIdMap.saveIdMap();
        CooperationIdMap.saveIdMap();
        ReserveIdMap.saveIdMap();
        BeachIdMap.saveIdMap();
        CityCodeMap.saveIdMap();
    }

}
