package app.tahachi.batterydoctor.activity;

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

import app.ads.control.AdControl;
import app.ads.control.AdControlHelp;
import app.ads.control.AdControlHelp.AdCloseListener;
import app.tahachi.batterydoctor.Utilsb.SharePreferenceUtils;
import app.tahachi.batterydoctor.Utilsb.Utils;
import app.tahachi.batterydoctor.notification.NotificationDevice;
import app.tahachi.batterydoctor.task.TaskBoost;
import app.tahachi.batterydoctor.view.HoloCircularProgressBar;
import app.tahachi.batterydoctor.R;

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
    private AdControlHelp adControlHelp;
    private View cv_trash_cleaner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_speed_booster);
        intView();
        context = this;
        adControlHelp = AdControlHelp.getInstance(context);
        NotificationDevice.cancle(context, NotificationDevice.ID_NOTIFICATTION_BOOST);
        View bt_RemoveAds = findViewById(R.id.remove_ads);
        bt_RemoveAds.setVisibility(AdControl.getInstance(context).remove_ads() ? View.GONE : View.VISIBLE);
        adControlHelp.loadNative(this, findViewById(R.id.native_ads_control_holder),
                R.layout.item_admob_native_ad, R.layout.item_fb_native_ad,
                R.layout.item_mopub_native_ad, true, false);
        adControlHelp.loadInterstitialAd(this, adCloseListener, null, false);
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
        if (!Utils.checkShouldDoing(context, 3)) {
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
            adControlHelp.showInterstitialAd(adCloseListener);
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