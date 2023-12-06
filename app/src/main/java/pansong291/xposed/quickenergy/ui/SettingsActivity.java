package pansong291.xposed.quickenergy.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.Locale;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.entity.*;
import pansong291.xposed.quickenergy.util.*;

public class SettingsActivity extends Activity {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int MAX_TAB_INDEX = 3;

    private TabHost tabHost;
    private GestureDetector gestureDetector;
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;

    Switch sw_immediateEffect, sw_recordLog, sw_showToast, sw_stayAwake, sw_timeoutRestart, sw_startAt7, sw_language_simplified_chinese,
            sw_collectWateringBubble, sw_collectProp, sw_collectEnergy, sw_helpFriendCollect, sw_receiveForestTaskAward,
            sw_cooperateWater, sw_energyRain, sw_enableFarm, sw_rewardFriend, sw_sendBackAnimal,
            sw_receiveFarmToolReward, sw_useNewEggTool, sw_harvestProduce, sw_donation, sw_answerQuestion,
            sw_receiveFarmTaskAward, sw_feedAnimal, sw_useAccelerateTool, sw_notifyFriend, sw_receivePoint,
            sw_openTreasureBox, sw_donateCharityCoin, sw_kbSignIn, sw_limitCollect, sw_doubleCard,
            sw_ExchangeEnergyDoubleClick, sw_reserve, sw_ecoLifeTick, sw_tiyubiz, sw_insBlueBeanExchange,
            sw_ancientTree, sw_ancientTreeOnlyWeek, sw_receiveCoinAsset, sw_antdodoCollect, sw_recordFarmGame, sw_beach,
            sw_kitchen, sw_antOcean, sw_userPatrol, sw_animalConsumeProp, sw_antOrchard, sw_receiveOrchardTaskAward,
            sw_enableOnGoing, sw_backupRuntime, sw_collectSesame, sw_zcjSignIn, sw_merchantKmdk, sw_acceptGift,
            sw_enableStall, sw_stallAutoClose, sw_stallAutoOpen, sw_stallAutoTask, sw_stallReceiveAward,
            sw_stallOpenType, sw_stallDonate, sw_chickenDiary, sw_collectGiftBox, sw_stallInviteRegister,
            sw_stallThrowManure, sw_greenFinance, sw_totalCertCount, sw_batchRobEnergy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.setLocale(this);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings);

        initTabHost();

        initFlipper();

        Config.shouldReload = true;
        FriendIdMap.shouldReload = true;
        CooperationIdMap.shouldReload = true;
        ReserveIdMap.shouldReload = true;
        BeachIdMap.shouldReload = true;

        initSwitch();

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    private void setCurrentTab(int lastView, int currentView) {
        if (lastView == currentView)
            return;
        if (lastView < currentView) {
            tabHost.getCurrentView().startAnimation(slideLeftOut);
        } else {
            tabHost.getCurrentView().startAnimation(slideRightOut);
        }

        tabHost.setCurrentTab(currentView);

        if (lastView < currentView) {
            tabHost.getCurrentView().startAnimation(slideRightIn);
        } else {
            tabHost.getCurrentView().startAnimation(slideLeftIn);
        }
    }

    private void initFlipper() {
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                int lastView = tabHost.getCurrentTab();
                int currentView = lastView;
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (currentView < MAX_TAB_INDEX) {
                        currentView++;
                    }
                    setCurrentTab(lastView, currentView);
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (currentView > 0) {
                        currentView--;
                    }
                    setCurrentTab(lastView, currentView);
                }
                return true;
            }
        });
    }

    private void initTabHost() {
        tabHost = findViewById(R.id.tab_settings);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("base")
                .setIndicator(getString(R.string.base_configuration))
                .setContent(R.id.tab_base);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("forest")
                .setIndicator(getString(R.string.forest_configuration))
                .setContent(R.id.tab_forest);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("farm")
                .setIndicator(getString(R.string.farm_configuration))
                .setContent(R.id.tab_farm);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("other")
                .setIndicator(getString(R.string.other_configuration))
                .setContent(R.id.tab_other);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);
    }

    private void initSwitch() {
        sw_immediateEffect = findViewById(R.id.sw_immediateEffect);
        sw_recordLog = findViewById(R.id.sw_recordLog);
        sw_showToast = findViewById(R.id.sw_showToast);
        sw_stayAwake = findViewById(R.id.sw_stayAwake);
        sw_timeoutRestart = findViewById(R.id.sw_timeoutRestart);
        sw_startAt7 = findViewById(R.id.sw_startAt7);
        sw_enableOnGoing = findViewById(R.id.sw_enableOnGoing);
        sw_backupRuntime = findViewById(R.id.sw_backupRuntime);
        sw_language_simplified_chinese = findViewById(R.id.sw_languageSimplifiedChinese);

        sw_collectEnergy = findViewById(R.id.sw_collectEnergy);
        sw_collectWateringBubble = findViewById(R.id.sw_collectWateringBubble);
        sw_batchRobEnergy = findViewById(R.id.sw_batchRobEnergy);
        sw_collectProp = findViewById(R.id.sw_collectProp);
        sw_helpFriendCollect = findViewById(R.id.sw_helpFriendCollect);
        sw_receiveForestTaskAward = findViewById(R.id.sw_receiveForestTaskAward);
        sw_cooperateWater = findViewById(R.id.sw_cooperateWater);
        sw_ancientTree = findViewById(R.id.sw_ancientTree);
        sw_energyRain = findViewById(R.id.sw_energyRain);
        sw_reserve = findViewById(R.id.sw_reserve);
        sw_beach = findViewById(R.id.sw_beach);
        sw_enableFarm = findViewById(R.id.sw_enableFarm);
        sw_rewardFriend = findViewById(R.id.sw_rewardFriend);
        sw_sendBackAnimal = findViewById(R.id.sw_sendBackAnimal);
        sw_receiveFarmToolReward = findViewById(R.id.sw_receiveFarmToolReward);
        sw_recordFarmGame = findViewById(R.id.sw_recordFarmGame);
        sw_kitchen = findViewById(R.id.sw_kitchen);
        sw_useNewEggTool = findViewById(R.id.sw_useNewEggTool);
        sw_harvestProduce = findViewById(R.id.sw_harvestProduce);
        sw_donation = findViewById(R.id.sw_donation);
        sw_answerQuestion = findViewById(R.id.sw_answerQuestion);
        sw_receiveFarmTaskAward = findViewById(R.id.sw_receiveFarmTaskAward);
        sw_feedAnimal = findViewById(R.id.sw_feedAnimal);
        sw_useAccelerateTool = findViewById(R.id.sw_useAccelerateTool);
        sw_notifyFriend = findViewById(R.id.sw_notifyFriend);
        sw_acceptGift = findViewById(R.id.sw_acceptGift);
        sw_chickenDiary = findViewById(R.id.sw_chickenDiary);
        sw_antOrchard = findViewById(R.id.sw_antOrchard);
        sw_receiveOrchardTaskAward = findViewById(R.id.sw_receiveOrchardTaskAward);
        sw_receivePoint = findViewById(R.id.sw_receivePoint);
        sw_openTreasureBox = findViewById(R.id.sw_openTreasureBox);
        sw_receiveCoinAsset = findViewById(R.id.sw_receiveCoinAsset);
        sw_donateCharityCoin = findViewById(R.id.sw_donateCharityCoin);
        sw_kbSignIn = findViewById(R.id.sw_kbSignIn);
        sw_limitCollect = findViewById(R.id.sw_limitCollect);
        sw_doubleCard = findViewById(R.id.sw_doubleCard);
        sw_ExchangeEnergyDoubleClick = findViewById(R.id.sw_ExchangeEnergyDoubleClick);
        sw_ecoLifeTick = findViewById(R.id.sw_ecoLifeTick);
        sw_tiyubiz = findViewById(R.id.sw_tiyubiz);
        sw_insBlueBeanExchange = findViewById(R.id.sw_insBlueBeanExchange);
        sw_collectSesame = findViewById(R.id.sw_collectSesame);
        sw_zcjSignIn = findViewById(R.id.sw_zcjSignIn);
        sw_merchantKmdk = findViewById(R.id.sw_merchantKmdk);
        sw_ancientTreeOnlyWeek = findViewById(R.id.sw_ancientTreeOnlyWeek);
        sw_antdodoCollect = findViewById(R.id.sw_antdodoCollect);
        sw_antOcean = findViewById(R.id.sw_antOcean);
        sw_userPatrol = findViewById(R.id.sw_userPatrol);
        sw_animalConsumeProp = findViewById(R.id.sw_animalConsumeProp);
        sw_collectGiftBox = findViewById(R.id.sw_collectGiftBox);
        sw_totalCertCount = findViewById(R.id.sw_totalCertCount);

        sw_enableStall = findViewById(R.id.sw_enableStall);
        sw_stallAutoClose = findViewById(R.id.sw_stallAutoClose);
        sw_stallAutoOpen = findViewById(R.id.sw_stallAutoOpen);
        sw_stallAutoTask = findViewById(R.id.sw_stallAutoTask);
        sw_stallReceiveAward = findViewById(R.id.sw_stallReceiveAward);
        sw_stallOpenType = findViewById(R.id.sw_stallOpenType);
        sw_stallDonate = findViewById(R.id.sw_stallDonate);
        sw_stallInviteRegister = findViewById(R.id.sw_stallInviteRegister);
        sw_stallThrowManure = findViewById(R.id.sw_stallThrowManure);
        sw_greenFinance = findViewById(R.id.sw_greenFinance);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sw_immediateEffect.setChecked(Config.immediateEffect());
        sw_recordLog.setChecked(Config.recordLog());
        sw_showToast.setChecked(Config.showToast());
        sw_stayAwake.setChecked(Config.stayAwake());
        sw_timeoutRestart.setChecked(Config.timeoutRestart());
        sw_startAt7.setChecked(Config.startAt7());
        sw_enableOnGoing.setChecked(Config.enableOnGoing());
        sw_backupRuntime.setChecked(Config.backupRuntime());
        sw_language_simplified_chinese.setChecked(Config.languageSimplifiedChinese());

        sw_collectEnergy.setChecked(Config.collectEnergy());
        sw_collectWateringBubble.setChecked(Config.collectWateringBubble());
        sw_batchRobEnergy.setChecked(Config.batchRobEnergy());
        sw_collectProp.setChecked(Config.collectProp());
        sw_helpFriendCollect.setChecked(Config.helpFriendCollect());
        sw_receiveForestTaskAward.setChecked(Config.receiveForestTaskAward());
        sw_cooperateWater.setChecked(Config.cooperateWater());
        sw_ancientTree.setChecked(Config.ancientTree());
        sw_energyRain.setChecked(Config.energyRain());
        sw_reserve.setChecked(Config.reserve());
        sw_beach.setChecked(Config.beach());
        sw_enableFarm.setChecked(Config.enableFarm());
        sw_rewardFriend.setChecked(Config.rewardFriend());
        sw_sendBackAnimal.setChecked(Config.sendBackAnimal());
        sw_receiveFarmToolReward.setChecked(Config.receiveFarmToolReward());
        sw_recordFarmGame.setChecked(Config.recordFarmGame());
        sw_kitchen.setChecked(Config.kitchen());
        sw_useNewEggTool.setChecked(Config.useNewEggTool());
        sw_harvestProduce.setChecked(Config.harvestProduce());
        sw_donation.setChecked(Config.donation());
        sw_answerQuestion.setChecked(Config.answerQuestion());
        sw_receiveFarmTaskAward.setChecked(Config.receiveFarmTaskAward());
        sw_feedAnimal.setChecked(Config.feedAnimal());
        sw_useAccelerateTool.setChecked(Config.useAccelerateTool());
        sw_notifyFriend.setChecked(Config.notifyFriend());
        sw_acceptGift.setChecked(Config.acceptGift());
        sw_chickenDiary.setChecked(Config.chickenDiary());
        sw_antOrchard.setChecked(Config.antOrchard());
        sw_receiveOrchardTaskAward.setChecked(Config.receiveOrchardTaskAward());
        sw_receivePoint.setChecked(Config.receivePoint());
        sw_openTreasureBox.setChecked(Config.openTreasureBox());
        sw_receiveCoinAsset.setChecked(Config.receiveCoinAsset());
        sw_donateCharityCoin.setChecked(Config.donateCharityCoin());
        sw_kbSignIn.setChecked(Config.kbSginIn());
        sw_limitCollect.setChecked(Config.isLimitCollect());
        sw_doubleCard.setChecked(Config.doubleCard());
        sw_ExchangeEnergyDoubleClick.setChecked(Config.ExchangeEnergyDoubleClick());
        sw_ecoLifeTick.setChecked(Config.ecoLifeTick());
        sw_tiyubiz.setChecked(Config.tiyubiz());
        sw_insBlueBeanExchange.setChecked(Config.insBlueBeanExchange());
        sw_collectSesame.setChecked(Config.collectSesame());
        sw_zcjSignIn.setChecked(Config.zcjSignIn());
        sw_merchantKmdk.setChecked(Config.merchantKmdk());
        sw_ancientTreeOnlyWeek.setChecked(Config.ancientTreeOnlyWeek());
        sw_antdodoCollect.setChecked(Config.antdodoCollect());
        sw_antOcean.setChecked(Config.antOcean());
        sw_userPatrol.setChecked(Config.userPatrol());
        sw_animalConsumeProp.setChecked(Config.animalConsumeProp());
        sw_collectGiftBox.setChecked(Config.collectGiftBox());
        sw_totalCertCount.setChecked(Config.totalCertCount());

        sw_enableStall.setChecked(Config.enableStall());
        sw_stallAutoClose.setChecked(Config.stallAutoClose());
        sw_stallAutoOpen.setChecked(Config.stallAutoOpen());
        sw_stallAutoTask.setChecked(Config.stallAutoTask());
        sw_stallReceiveAward.setChecked(Config.stallReceiveAward());
        sw_stallOpenType.setChecked(Config.stallOpenType());
        sw_stallDonate.setChecked(Config.stallDonate());
        sw_stallInviteRegister.setChecked(Config.stallInviteRegister());
        sw_stallThrowManure.setChecked(Config.stallThrowManure());
        sw_greenFinance.setChecked(Config.greenFinance());
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        if (v instanceof Switch) {
            Switch sw = (Switch) v;
            switch (v.getId()) {
                case R.id.sw_immediateEffect:
                    Config.setImmediateEffect(sw.isChecked());
                    break;

                case R.id.sw_recordLog:
                    Config.setRecordLog(sw.isChecked());
                    break;

                case R.id.sw_showToast:
                    Config.setShowToast(sw.isChecked());
                    break;

                case R.id.sw_stayAwake:
                    Config.setStayAwake(sw.isChecked());
                    break;

                case R.id.sw_timeoutRestart:
                    Config.setTimeoutRestart(sw.isChecked());
                    break;

                case R.id.sw_startAt7:
                    Config.setStartAt7(this.getApplicationContext(), sw.isChecked());
                    break;

                case R.id.sw_enableOnGoing:
                    Config.setEnableOnGoing(sw.isChecked());
                    break;

                case R.id.sw_backupRuntime:
                    Config.setBackupRuntime(sw.isChecked());
                    break;

                case R.id.sw_languageSimplifiedChinese:
                    Config.setLanguageSimplifiedChinese(sw.isChecked());
                    // 提示需要重启 language_simplified_chinese_need_restart
                    Toast.makeText(this, R.string.language_simplified_chinese_need_restart, Toast.LENGTH_SHORT).show();
                    break;

                case R.id.sw_collectEnergy:
                    Config.setCollectEnergy(sw.isChecked());
                    break;

                case R.id.sw_collectWateringBubble:
                    Config.setCollectWateringBubble(sw.isChecked());
                    break;

                case R.id.sw_batchRobEnergy:
                    Config.setBatchRobEnergy(sw.isChecked());
                    break;

                case R.id.sw_collectProp:
                    Config.setCollectProp(sw.isChecked());
                    break;

                case R.id.sw_limitCollect:
                    Config.setLimitCollect(sw.isChecked());
                    break;

                case R.id.sw_doubleCard:
                    Config.setDoubleCard(sw.isChecked());
                    break;

                case R.id.sw_helpFriendCollect:
                    Config.setHelpFriendCollect(sw.isChecked());
                    break;

                case R.id.sw_receiveForestTaskAward:
                    Config.setReceiveForestTaskAward(sw.isChecked());
                    break;

                case R.id.sw_cooperateWater:
                    Config.setCooperateWater(sw.isChecked());
                    break;

                case R.id.sw_ancientTree:
                    Config.setAncientTree(sw.isChecked());
                    break;

                case R.id.sw_energyRain:
                    Config.setEnergyRain(sw.isChecked());
                    break;

                case R.id.sw_ExchangeEnergyDoubleClick:
                    Config.setExchangeEnergyDoubleClick(sw.isChecked());
                    break;

                case R.id.sw_reserve:
                    Config.setReserve(sw.isChecked());
                    break;

                case R.id.sw_beach:
                    Config.setBeach(sw.isChecked());
                    break;

                case R.id.sw_enableFarm:
                    Config.setEnableFarm(sw.isChecked());
                    break;

                case R.id.sw_rewardFriend:
                    Config.setRewardFriend(sw.isChecked());
                    break;

                case R.id.sw_sendBackAnimal:
                    Config.setSendBackAnimal(sw.isChecked());
                    break;

                case R.id.sw_receiveFarmToolReward:
                    Config.setReceiveFarmToolReward(sw.isChecked());
                    break;

                case R.id.sw_recordFarmGame:
                    Config.setRecordFarmGame(sw.isChecked());
                    break;

                case R.id.sw_kitchen:
                    Config.setKitchen(sw.isChecked());
                    break;

                case R.id.sw_useNewEggTool:
                    Config.setUseNewEggTool(sw.isChecked());
                    break;

                case R.id.sw_harvestProduce:
                    Config.setHarvestProduce(sw.isChecked());
                    break;

                case R.id.sw_donation:
                    Config.setDonation(sw.isChecked());
                    break;

                case R.id.sw_answerQuestion:
                    Config.setAnswerQuestion(sw.isChecked());
                    break;

                case R.id.sw_receiveFarmTaskAward:
                    Config.setReceiveFarmTaskAward(sw.isChecked());
                    break;

                case R.id.sw_feedAnimal:
                    Config.setFeedAnimal(sw.isChecked());
                    break;

                case R.id.sw_useAccelerateTool:
                    Config.setUseAccelerateTool(sw.isChecked());
                    break;

                case R.id.sw_notifyFriend:
                    Config.setNotifyFriend(sw.isChecked());
                    break;

                case R.id.sw_acceptGift:
                    Config.setAcceptGift(sw.isChecked());
                    break;

                case R.id.sw_chickenDiary:
                    Config.setChickenDiary(sw.isChecked());
                    break;

                case R.id.sw_antOrchard:
                    Config.setAntOrchard(sw.isChecked());
                    break;

                case R.id.sw_receiveOrchardTaskAward:
                    Config.setReceiveOrchardTaskAward(sw.isChecked());
                    break;

                case R.id.sw_receivePoint:
                    Config.setReceivePoint(sw.isChecked());
                    break;

                case R.id.sw_openTreasureBox:
                    Config.setOpenTreasureBox(sw.isChecked());
                    break;

                case R.id.sw_receiveCoinAsset:
                    Config.setReceiveCoinAsset(sw.isChecked());
                    break;

                case R.id.sw_donateCharityCoin:
                    Config.setDonateCharityCoin(sw.isChecked());
                    break;

                case R.id.sw_kbSignIn:
                    Config.setKbSginIn(sw.isChecked());
                    break;

                case R.id.sw_ecoLifeTick:
                    Config.setEcoLifeTick(sw.isChecked());
                    break;

                case R.id.sw_tiyubiz:
                    Config.setTiyubiz(sw.isChecked());
                    break;

                case R.id.sw_insBlueBeanExchange:
                    Config.setInsBlueBeanExchange(sw.isChecked());
                    break;

                case R.id.sw_collectSesame:
                    Config.setCollectSesame(sw.isChecked());
                    break;

                case R.id.sw_zcjSignIn:
                    Config.setZcjSignIn(sw.isChecked());
                    break;

                case R.id.sw_merchantKmdk:
                    Config.setMerchantKmdk(sw.isChecked());
                    break;

                case R.id.sw_ancientTreeOnlyWeek:
                    Config.setAncientTreeOnlyWeek(sw.isChecked());
                    break;

                case R.id.sw_antdodoCollect:
                    Config.setAntdodoCollect(sw.isChecked());
                    break;

                case R.id.sw_antOcean:
                    Config.setAntOcean(sw.isChecked());
                    break;

                case R.id.sw_userPatrol:
                    Config.setUserPatrol(sw.isChecked());
                    break;

                case R.id.sw_animalConsumeProp:
                    Config.setAnimalConsumeProp(sw.isChecked());
                    break;

                case R.id.sw_collectGiftBox:
                    Config.setCollectGiftBox(sw.isChecked());
                    break;

                case R.id.sw_totalCertCount:
                    Config.setTotalCertCount(sw.isChecked());
                    break;

                case R.id.sw_enableStall:
                    Config.setEnableStall(sw.isChecked());
                    break;

                case R.id.sw_stallAutoClose:
                    Config.setStallAutoClose(sw.isChecked());
                    break;

                case R.id.sw_stallAutoOpen:
                    Config.setStallAutoOpen(sw.isChecked());
                    break;

                case R.id.sw_stallAutoTask:
                    Config.setStallAutoTask(sw.isChecked());
                    break;

                case R.id.sw_stallReceiveAward:
                    Config.setStallReceiveAward(sw.isChecked());
                    break;

                case R.id.sw_stallOpenType:
                    Config.setStallOpenType(sw.isChecked());
                    break;

                case R.id.sw_stallDonate:
                    Config.setStallDonate(sw.isChecked());
                    break;

                case R.id.sw_stallInviteRegister:
                    Config.setStallInviteRegister(sw.isChecked());
                    break;

                case R.id.sw_stallThrowManure:
                    Config.setStallThrowManure(sw.isChecked());
                    break;

                case R.id.sw_greenFinance:
                    Config.setGreenFinance(sw.isChecked());
                    break;
            }
        } else if (v instanceof Button) {
            Button btn = (Button) v;
            switch (v.getId()) {
                case R.id.btn_toastOffsetY:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.TOAST_OFFSET_Y);
                    break;
                case R.id.btn_stayAwakeType:
                    ChoiceDialog.showStayAwakeType(this, btn.getText());
                    break;

                case R.id.btn_stayAwakeTarget:
                    ChoiceDialog.showStayAwakeTarget(this, btn.getText());
                    break;

                case R.id.btn_timeoutRestartType:
                    ChoiceDialog.showTimeoutRestartType(this, btn.getText());
                    break;

                case R.id.btn_waitWhenException:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.WAIT_WHEN_EXCEPTION);
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
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.DOUBLE_CARD_TIME,
                            this.getString(R.string.use_double_card_time_desc));
                    break;

                case R.id.btn_doubleCountLimit:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.DOUBLE_COUNT_LIMIT);
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

                case R.id.btn_ancientTreeAreaCodeList:
                    ListDialog.show(this, btn.getText(), AreaCode.getList(), Config.getAncientTreeCityCodeList(), null);
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

                case R.id.btn_farmGameTime:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.FARM_GAME_TIME);
                    break;

                case R.id.btn_feedFriendAnimalList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getFeedFriendAnimalList(),
                            Config.getFeedFriendCountList());
                    break;

                case R.id.btn_dontNotifyFriendList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getDontNotifyFriendList(), null);
                    break;

                case R.id.btn_visitFriendList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.getVisitFriendList(),
                            Config.getVisitFriendCountList());
                    break;

                case R.id.btn_animalSleepTime:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.ANIMAL_SLEEP_TIME);
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

                case R.id.btn_ExchangeEnergyDoubleClickCount:
                    EditDialog.showEditDialog(this, btn.getText(),
                            EditDialog.EditMode.EXCHANGE_ENERGY_DOUBLE_CLICK_COUNT);
                    break;

                case R.id.btn_WhoYouWantToGiveTo:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.whoYouWantGiveTo(), null,
                            ListDialog.ListType.RADIO);
                    break;

                case R.id.btn_sendFriendCard:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.sendFriendCard(), null,
                            ListDialog.ListType.RADIO);
                    break;

                case R.id.btn_orchardSpreadManureCount:
                    EditDialog.showEditDialog(this, btn.getText(),
                            EditDialog.EditMode.ORCHARD_SPREAD_MANURE_COUNT);
                    break;

                case R.id.btn_stallOpenList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.stallOpenList(), null);
                    break;

                case R.id.btn_stallWhiteList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.stallWhiteList(), null);
                    break;

                case R.id.btn_stallBlackList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.stallBlackList(), null);
                    break;

                case R.id.btn_stallAllowOpenTime:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.STALL_ALLOW_OPEN_TIME);
                    break;

                case R.id.btn_stallSelfOpenTime:
                    EditDialog.showEditDialog(this, btn.getText(), EditDialog.EditMode.STALL_SELF_OPEN_TIME);
                    break;

                case R.id.btn_stallInviteShopList:
                    ListDialog.show(this, btn.getText(), AlipayUser.getList(), Config.stallInviteShopList(), null);
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
    }

}
