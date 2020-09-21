package com.amt.batterysaver.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ads.control.AdControl;
import com.ads.control.AdmobHelp;
import com.ads.control.AdmobHelp.AdLoadedListener;
import com.ads.control.AdmobHelp.AdCloseListener;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.notification.NotificationDevice;
import com.amt.batterysaver.task.TaskBoost;
import com.amt.batterysaver.view.HoloCircularProgressBar;
import com.amt.batterysaver.R;

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
    private Context context;
    private AdControl adControl;
    private AdmobHelp admobHelp;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_speed_booster);
        intView();
        context = this;
        admobHelp = AdmobHelp.getInstance(context);
        NotificationDevice.cancle(context, NotificationDevice.ID_NOTIFICATTION_BOOST);
        adControl = AdControl.getInstance(context);
        admobHelp.loadNative(this, adControl.admob_native());
        admobHelp.loadInterstitialAd(adCloseListener, null, adControl.admob_full(), false);
    }

    AdCloseListener adCloseListener = new AdCloseListener() {
        @Override
        public void onAdClosed() {
            loadResult();
        }
    };

    public void checkTask() {
        if (!Utils.checkShouldDoing(this, 6)) {
            findViewById(R.id.cvBoost).setVisibility(View.GONE);
        }
        if (!Utils.checkShouldDoing(this, 7)) {
            findViewById(R.id.cvCool).setVisibility(View.GONE);

        }
        if (!Utils.checkShouldDoing(this, 3)) {
            findViewById(R.id.cvClean).setVisibility(View.GONE);

        }
//        if (!Utils.checkShouldDoing(this, 8)) {
//            findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//
//        } else {
//            if (SharePreferenceUtils.getInstance(this).getFsAutoRun())
//                findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//        }

    }

    public void intView() {
        rlScan = findViewById(R.id.rlScanning);
        parentAds = findViewById(R.id.fmResult);
        tv_optimized_info = findViewById(R.id.tv_optimized_info);
//        if (Utils.checkShouldDoing(this, 6)) {
        this.tvResult = findViewById(R.id.tvOptimize);
        AnimationUtils.loadAnimation(this, R.anim.delay_anim);
        this.ivRocket = findViewById(R.id.clean_done_iv_rocket);
        this.mHoloCircularProgressBarCleanDone = findViewById(R.id.ivDoneHoloCirular);
        animate(this.mHoloCircularProgressBarCleanDone, null, 1.0f, 3000);
        this.mHoloCircularProgressBarCleanDone.setMarkerProgress(1.0f);
        this.ivTick = findViewById(R.id.clean_done_iv_tick);
        this.ivDoneAnim = AnimationUtils.loadAnimation(this, R.anim.ic_done_anim);
        this.ivDoneAnim.setAnimationListener(new CustomAnimationListener());
        intData();

//        } else {
//            findViewById(R.id.rlScanning).setVisibility(View.GONE);
//            BoostActivity.this.parentAds.setAlpha(0.0f);
//            BoostActivity.this.parentAds.setVisibility(View.VISIBLE);
//            BoostActivity.this.parentAds.animate().alpha(1.0f).start();
//            Animation downtoup = AnimationUtils.loadAnimation(BoostActivity.this, R.anim.downtoup);
//            parentAds.startAnimation(downtoup);
//        }
//        ((ImageView) findViewById(R.id.clean_done_iv_rocket)).setColorFilter(getResources().getColor(R.color.progress_color), PorterDuff.Mode.MULTIPLY);
        ((ImageView) findViewById(R.id.clean_done_iv_tick)).setColorFilter(getResources().getColor(R.color.progress_color), PorterDuff.Mode.MULTIPLY);
        ((ImageView) findViewById(R.id.iv_arrow)).setColorFilter(getResources().getColor(R.color.dark_icon_color), PorterDuff.Mode.MULTIPLY);
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
//            case R.id.ivClose:
//                SharePreferenceUtils.getInstance(this).setHideChargeView(System.currentTimeMillis());
//                findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//                return;
//            case R.id.btnOk:
//                SharePreferenceUtils.getInstance(this).setFsAutoRun(true);
//                findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//                return;
            case R.id.rlBoost:
                startActivity(new Intent(this, BoostActivity.class));
                finish();
                return;
            case R.id.rlClean:
                startActivity(new Intent(this, CleanActivity.class));
                finish();
                return;
            case R.id.rlCool:
                startActivity(new Intent(this, CoolActivity.class));
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
            admobHelp.showInterstitialAd(adCloseListener);
        }
    }

    private void animate(final HoloCircularProgressBar holoCircularProgressBar, Animator.AnimatorListener animatorListener, final float f, int i) {
        this.mProgressBarAnimatorCleanDone = ObjectAnimator.ofFloat(holoCircularProgressBar, NotificationCompat.CATEGORY_PROGRESS, 0.0f, f);
        this.mProgressBarAnimatorCleanDone.setDuration((long) i);
        this.mProgressBarAnimatorCleanDone.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                holoCircularProgressBar.setProgress(f);
                BoostActivity.this.ivRocket.setVisibility(View.INVISIBLE);
                BoostActivity.this.ivTick.setVisibility(View.VISIBLE);
                tvResult.setText(getString(R.string.done));
                BoostActivity.this.ivTick.startAnimation(BoostActivity.this.ivDoneAnim);
            }
        });
        if (animatorListener != null) {
            this.mProgressBarAnimatorCleanDone.addListener(animatorListener);
        }
        this.mProgressBarAnimatorCleanDone.reverse();
        this.mProgressBarAnimatorCleanDone.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                holoCircularProgressBar.setProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(valueAnimator.getAnimatedValue());
                stringBuilder.append("");
            }
        });
        holoCircularProgressBar.setMarkerProgress(f);
        this.mProgressBarAnimatorCleanDone.start();
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
//        if (SharePreferenceUtils.getInstance(this).getFlagAds()) {
//            SharePreferenceUtils.getInstance(this).setFlagAds(false);
//            AdmobHelp.getInstance().showInterstitialAd(new AdmobHelp.AdCloseListener() {
//                @Override
//                public void onAdClosed() {
//                    finish();
//                }
//            });
//        } else {
        finish();
//        }
    }
}