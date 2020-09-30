package com.amt.batterysaver.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import com.ads.control.AdmobHelp.AdCloseListener;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.view.HoloCircularProgressBar;
import com.amt.batterysaver.R;

public class CleanResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivRocket;
    private Animation ivDoneAnim;
    private TextView tvResult, tvCleaned;
    private HoloCircularProgressBar mHoloCircularProgressBarCleanDone;
    private ObjectAnimator mProgressBarAnimatorCleanDone;
    private ImageView ivTick;
    RelativeLayout rlScan;
    FrameLayout parentAds;
    private AdControl adControl;
    private AdmobHelp admobHelp;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        context = this;
        admobHelp = AdmobHelp.getInstance(context);
        setContentView(R.layout.activity_clean_result);
        adControl = AdControl.getInstance(this);

//        AdmobHelp.getInstance().init(this, SharePreferenceConstant.admob_full, SharePreferenceConstant.admob_native);
        admobHelp.loadNative(this, adControl.admob_native());
        SharePreferenceUtils.getInstance(this).setFlagAds(true);

        rlScan = findViewById(R.id.rlScanning);
        parentAds = findViewById(R.id.fmResult);
        this.tvResult = findViewById(R.id.tvOptimize);
        this.tvCleaned = findViewById(R.id.tv_optimized_info);
        AnimationUtils.loadAnimation(this, R.anim.delay_anim);
        this.ivRocket = findViewById(R.id.clean_done_iv_rocket);
        this.mHoloCircularProgressBarCleanDone = findViewById(R.id.ivDoneHoloCirular);
        animate(this.mHoloCircularProgressBarCleanDone, null, 1.0f, 3000);
        this.mHoloCircularProgressBarCleanDone.setMarkerProgress(1.0f);
        this.ivTick = findViewById(R.id.clean_done_iv_tick);
        this.ivDoneAnim = AnimationUtils.loadAnimation(this, R.anim.ic_done_anim);
        this.ivDoneAnim.setAnimationListener(new CleanResultActivity.C06741());
        tvCleaned.setText(String.format(getString(R.string.cleaned),
                Utils.formatSize(SharePreferenceUtils.getInstance(this).getTotalJunk())));
        ((ImageView) findViewById(R.id.iv_arrow)).setColorFilter(getResources().getColor(R.color.dark_icon_color), PorterDuff.Mode.MULTIPLY);
        checkTask();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lr_back:
                finish();
                return;

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
    }

    class C06741 implements Animation.AnimationListener {

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        C06741() {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Animation slideUp = AnimationUtils.loadAnimation(CleanResultActivity.this, R.anim.zoom_in);
            rlScan.startAnimation(slideUp);
            CleanResultActivity.this.rlScan.setVisibility(View.GONE);
            CleanResultActivity.this.parentAds.setAlpha(0.0f);
            CleanResultActivity.this.parentAds.setVisibility(View.VISIBLE);
            CleanResultActivity.this.parentAds.animate().alpha(1.0f).start();
            Animation downtoup = AnimationUtils.loadAnimation(CleanResultActivity.this, R.anim.downtoup);
            parentAds.startAnimation(downtoup);
            SharePreferenceUtils.getInstance(CleanResultActivity.this).setCleanTime(System.currentTimeMillis());
        }
    }

    private void animate(final HoloCircularProgressBar holoCircularProgressBar, Animator.AnimatorListener animatorListener, final float f, int i) {
        this.mProgressBarAnimatorCleanDone = ObjectAnimator.ofFloat(holoCircularProgressBar, NotificationCompat.CATEGORY_PROGRESS, 0.0f, f);
        this.mProgressBarAnimatorCleanDone.setDuration((long) i);
        AdCloseListener adCloseListener = new AdCloseListener() {
            @Override
            public void onAdClosed() {
                loadResult(holoCircularProgressBar, f);
            }
        };
        admobHelp.loadInterstitialAd(adCloseListener, null, adControl.admob_full(), false);
        this.mProgressBarAnimatorCleanDone.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
//                if (SharePreferenceUtils.getInstance(CleanResultActivity.this).getFlagAds()) {
//                    SharePreferenceUtils.getInstance(CleanResultActivity.this).setFlagAds(false);

                admobHelp.showInterstitialAd(adCloseListener);
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

    private void loadResult(HoloCircularProgressBar holoCircularProgressBar, float f) {
        holoCircularProgressBar.setProgress(f);
        CleanResultActivity.this.ivRocket.setVisibility(View.INVISIBLE);
        CleanResultActivity.this.ivTick.setVisibility(View.VISIBLE);
        tvResult.setText(getString(R.string.done));
        CleanResultActivity.this.ivTick.startAnimation(CleanResultActivity.this.ivDoneAnim);
    }

    @Override
    public void onBackPressed() {
//        if(SharePreferenceUtils.getInstance(this).getFlagAds()){
//            SharePreferenceUtils.getInstance(this).setFlagAds(false);
//            AdmobHelp.getInstance().showInterstitialAd(new AdmobHelp.AdCloseListener() {
//                @Override
//                public void onAdClosed() {
//                    finish();
//                }
//            });
//        }else{
//            finish();
//        }
        finish();
    }

    @Override
    protected void onDestroy() {
        SharePreferenceUtils.getInstance(this).setFlagAds(false);
        super.onDestroy();
    }
}
