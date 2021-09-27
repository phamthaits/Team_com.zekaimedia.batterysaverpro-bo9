package com.qamob.batterysaver.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.qamob.batterysaver.R;
import com.qamob.batterysaver.Utilsb.SharePreferenceUtils;
import com.qamob.batterysaver.Utilsb.Utils;
import com.qamob.batterysaver.billing.RemoveAdsActivity;
import com.qamob.batterysaver.view.HoloCircularProgressBar;

public class CleanResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivRocket;
    private Animation ivDoneAnim;
    private TextView tvResult, tvCleaned;
    private HoloCircularProgressBar mHoloCircularProgressBarCleanDone;
    private ImageView ivTick;
    RelativeLayout rlScan;
    FrameLayout parentAds;
    private AdControlHelp adControlHelp;
    private AdControl adControl;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        activity = this;
        adControlHelp = AdControlHelp.getInstance(activity);
        adControl = AdControl.getInstance(activity);
        setContentView(R.layout.activity_clean_result);

        View bt_RemoveAds = findViewById(R.id.remove_ads);
        bt_RemoveAds.setVisibility(AdControl.getInstance(activity).remove_ads() ? View.GONE : View.VISIBLE);

        adControlHelp.loadNative(this, findViewById(R.id.native_ads_control_holder), adControl.native_main);
        SharePreferenceUtils.getInstance(this).setFlagAds(true);

        rlScan = findViewById(R.id.rlScanning);
        parentAds = findViewById(R.id.fmResult);
        this.tvResult = findViewById(R.id.tvOptimize);
        this.tvCleaned = findViewById(R.id.tv_optimized_info);
        AnimationUtils.loadAnimation(this, R.anim.delay_anim);
        this.ivRocket = findViewById(R.id.clean_done_iv_rocket);
        this.mHoloCircularProgressBarCleanDone = findViewById(R.id.ivDoneHoloCirular);
        animate();
        this.ivTick = findViewById(R.id.clean_done_iv_tick);
        this.ivDoneAnim = AnimationUtils.loadAnimation(this, R.anim.ic_done_anim);
        this.ivDoneAnim.setAnimationListener(new CleanResultActivity.C06741());
        tvCleaned.setText(String.format(getString(R.string.cleaned),
                Utils.formatSize(SharePreferenceUtils.getInstance(this).getTotalJunk())));
        ((ImageView) findViewById(R.id.iv_arrow)).setColorFilter(getResources().getColor(R.color.description), PorterDuff.Mode.MULTIPLY);

        adControlHelp.loadInterstitialAd(this,  null );
    }

    AdControlHelp.AdCloseListener adCloseListener = new AdControlHelp.AdCloseListener() {
        @Override
        public void onAdClosed() {
            loadResult(mHoloCircularProgressBarCleanDone, 1.0f);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lr_back:
                finish();
                return;
            case R.id.lrBoost:
                startActivity(new Intent(this, BoostActivity.class));
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
                /* ------------------- StatusBar Navigation text dark bg white ----------------- */
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.white));
                getWindow().setNavigationBarColor(ContextCompat.getColor(activity, R.color.white));
                /* ------------------------------------------------------------------ */

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                CleanResultActivity.this.ivRocket.setVisibility(View.INVISIBLE);
                CleanResultActivity.this.ivTick.setVisibility(View.VISIBLE);
                tvResult.setText(getString(R.string.done));
                CleanResultActivity.this.ivTick.startAnimation(CleanResultActivity.this.ivDoneAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void loadResult(HoloCircularProgressBar holoCircularProgressBar, float f) {
        holoCircularProgressBar.setProgress(f);
        getWindow().setStatusBarColor(Color.rgb(113, 126, 238));
        getWindow().setNavigationBarColor(Color.rgb(113, 126, 238));
        LinearLayout lrBack = findViewById(R.id.lr_back);
        lrBack.setBackgroundColor(Color.rgb(113, 126, 238));
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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        SharePreferenceUtils.getInstance(this).setFlagAds(false);
        super.onDestroy();
    }

}
