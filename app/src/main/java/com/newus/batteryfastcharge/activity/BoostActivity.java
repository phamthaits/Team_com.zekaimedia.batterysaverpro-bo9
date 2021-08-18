package com.newus.batteryfastcharge.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ads.control.AdControl;
import com.ads.control.AdControlHelp;
import com.ads.control.AdControlHelp.AdCloseListener;
import com.newus.batteryfastcharge.R;
import com.newus.batteryfastcharge.Utilsb.SharePreferenceUtils;
import com.newus.batteryfastcharge.Utilsb.Utils;
import com.newus.batteryfastcharge.billing.RemoveAdsActivity;
import com.newus.batteryfastcharge.notification.NotificationDevice;
import com.newus.batteryfastcharge.task.TaskBoost;
import com.newus.batteryfastcharge.view.HoloCircularProgressBar;

public class BoostActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivRocket;
    private Animation ivDoneAnim;
    private TextView tvResult, tv_optimized_info;
    private HoloCircularProgressBar mHoloCircularProgressBarCleanDone;
    private ObjectAnimator mProgressBarAnimatorCleanDone;
    private ImageView ivTick;
    RelativeLayout rlScan;
    FrameLayout parentAds;
    TaskBoost mTaskBoost;
    private long totalRam;
    private long useRam;
    private long useRam2;
    private AdControlHelp adControlHelp;
    private AdControl adControl;
    private View cv_trash_cleaner;
    private Activity activity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_speed_booster);
        intView();
        activity = this;
        adControlHelp = AdControlHelp.getInstance(activity);
        adControl = AdControl.getInstance(activity);

        /* ------------------- StatusBar Navigation text dark bg white ----------------- */
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        /* ------------------------------------------------------------------ */

        NotificationDevice.cancle(activity, NotificationDevice.ID_NOTIFICATTION_BOOST);
        View bt_RemoveAds = findViewById(R.id.remove_ads);
        bt_RemoveAds.setVisibility(AdControl.getInstance(activity).remove_ads() ? View.GONE : View.VISIBLE);
        adControlHelp.loadNative(this, findViewById(R.id.native_ads_control_holder), adControl.native_main);
        adControlHelp.loadInterstitialAd(this,  null);
    }

    AdCloseListener adCloseListener = new AdCloseListener() {
        @Override
        public void onAdClosed() {
            loadResult();
        }
    };

    public void intView() {
        rlScan = findViewById(R.id.rlScanning);
        parentAds = findViewById(R.id.fmResult);
        tv_optimized_info = findViewById(R.id.tv_optimized_info);
        cv_trash_cleaner = findViewById(R.id.cv_trash_cleaner);
//        if (Utils.checkShouldDoing(this, 6)) {
        this.tvResult = findViewById(R.id.tvOptimize);
        AnimationUtils.loadAnimation(this, R.anim.delay_anim);
        this.ivRocket = findViewById(R.id.clean_done_iv_rocket);
        this.mHoloCircularProgressBarCleanDone = findViewById(R.id.ivDoneHoloCirular);
        /* animate(this.mHoloCircularProgressBarCleanDone, null, 1.0f, 3000);*/
        animate();
        /*this.mHoloCircularProgressBarCleanDone.setMarkerProgress(1.0f);*/
        this.ivTick = findViewById(R.id.clean_done_iv_tick);
        this.ivDoneAnim = AnimationUtils.loadAnimation(this, R.anim.ic_done_anim);
        this.ivDoneAnim.setAnimationListener(new CustomAnimationListener());
        intData();
        ((ImageView) findViewById(R.id.clean_done_iv_tick)).setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.MULTIPLY);
        ((ImageView) findViewById(R.id.iv_arrow)).setColorFilter(getResources().getColor(R.color.description), PorterDuff.Mode.MULTIPLY);
        if (!Utils.checkShouldDoing(activity, 3)) {
            cv_trash_cleaner.setVisibility(View.GONE);
        }
    }

    private long getAvaiableRam(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            activityManager.getMemoryInfo(mi);
        }
        return mi.availMem;
    }

    public void intData() {
        totalRam = Utils.getTotalRAM();
        long availableRam = getAvaiableRam(this);
        useRam = totalRam - availableRam;
        mTaskBoost = new TaskBoost(this, new TaskBoost.OnBoostListener() {
            @Override
            public void OnResult() {
                long availableRam = getAvaiableRam(BoostActivity.this);
                useRam2 = totalRam - availableRam;
                if (useRam2 > 0) {
                    tv_optimized_info.setText(BoostActivity.this.getString(R.string.ram_result) + " " + Utils.formatSize(useRam - useRam2));
                }
            }
        });
        mTaskBoost.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lr_back:
                finish();
                return;
            case R.id.lrClean:
                startActivity(new Intent(this, CleanActivity.class));
                finish();
                return;
            case R.id.lrCool:
                startActivity(new Intent(this, CoolActivity.class));
                finish();
                return;
            case R.id.lrHistory:
                startActivity(new Intent(this, ChartActivity.class));
                finish();
                return;
            case R.id.lrManager:
                startActivity(new Intent(this, AppManagerActivity.class));
                finish();
                return;
            case R.id.lrSettings:
                startActivity(new Intent(this, SettingActivity.class));
                finish();
                return;
            case R.id.lrRemove:
                startActivity(new Intent(this, RemoveAdsActivity.class));
                finish();
                return;
            default:
                return;
        }
    }

    class CustomAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        CustomAnimationListener() {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            adControlHelp.showInterstitialAd(activity, adCloseListener);
        }
    }

    private void animate() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_result);
        ImageView img_rotate_result = findViewById(R.id.img_rotate_result);
        img_rotate_result.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                BoostActivity.this.ivRocket.setVisibility(View.INVISIBLE);
                BoostActivity.this.ivTick.setVisibility(View.VISIBLE);
                tvResult.setText(getString(R.string.done));
                BoostActivity.this.ivTick.startAnimation(BoostActivity.this.ivDoneAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void cancleUIUPdate() {
        if (this.mTaskBoost != null && this.mTaskBoost.getStatus() == AsyncTask.Status.RUNNING) {
            this.mTaskBoost.cancel(true);
            this.mTaskBoost = null;
        }
    }

    private void loadResult() {
        Animation slideUp = AnimationUtils.loadAnimation(BoostActivity.this, R.anim.zoom_in);
        rlScan.startAnimation(slideUp);
        BoostActivity.this.rlScan.setVisibility(View.GONE);
        getWindow().setStatusBarColor(Color.rgb(251, 93, 96));
        getWindow().setNavigationBarColor(Color.rgb(251, 93, 96));
        LinearLayout lrBack = findViewById(R.id.lr_back);
        lrBack.setBackgroundColor(Color.rgb(251, 93, 96));
        BoostActivity.this.parentAds.setAlpha(0.0f);
        BoostActivity.this.parentAds.setVisibility(View.VISIBLE);
        BoostActivity.this.parentAds.animate().alpha(1.0f).start();
        Animation downtoup = AnimationUtils.loadAnimation(BoostActivity.this, R.anim.downtoup);
        parentAds.startAnimation(downtoup);
        SharePreferenceUtils.getInstance(BoostActivity.this).setBoostTime(System.currentTimeMillis());
        SharePreferenceUtils.getInstance(BoostActivity.this).setBoostTimeMain(System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancleUIUPdate();
        SharePreferenceUtils.getInstance(this).setFlagAds(false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}