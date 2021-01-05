package app.tahachi.batterydoctor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

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
import app.tahachi.batterydoctor.Utilsb.SharePreferenceUtils;
import app.tahachi.batterydoctor.Utilsb.Utils;
import app.tahachi.batterydoctor.view.HoloCircularProgressBar;
import app.tahachi.batterydoctor.R;

public class CleanResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivRocket;
    private Animation ivDoneAnim;
    private TextView tvResult, tvCleaned;
    private HoloCircularProgressBar mHoloCircularProgressBarCleanDone;
    private ImageView ivTick;
    RelativeLayout rlScan;
    FrameLayout parentAds;
    private AdControlHelp adControlHelp;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        context = this;
        adControlHelp = AdControlHelp.getInstance(context);
        setContentView(R.layout.activity_clean_result);

        View bt_RemoveAds = findViewById(R.id.remove_ads);
        bt_RemoveAds.setVisibility(AdControl.getInstance(context).remove_ads() ? View.GONE : View.VISIBLE);

        adControlHelp.loadNative(this, findViewById(R.id.native_ads_control_holder));
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
    }

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

    private void animate() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_result);
        ImageView img_rotate_result = findViewById(R.id.img_rotate_result);
        img_rotate_result.startAnimation(animation);
        AdControlHelp.AdCloseListener adCloseListener = new AdControlHelp.AdCloseListener() {
            @Override
            public void onAdClosed() {
                loadResult(mHoloCircularProgressBarCleanDone, 1.0f);
            }
        };
        adControlHelp.loadInterstitialAd(this, adCloseListener, null, false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                adControlHelp.showInterstitialAd(adCloseListener);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
//    private void animate(final HoloCircularProgressBar holoCircularProgressBar, Animator.AnimatorListener animatorListener, final float f, int i) {
//        this.mProgressBarAnimatorCleanDone = ObjectAnimator.ofFloat(holoCircularProgressBar, NotificationCompat.CATEGORY_PROGRESS, 0.0f, f);
//        this.mProgressBarAnimatorCleanDone.setDuration((long) i);
//        AdControlHelp.AdCloseListener adCloseListener = new AdControlHelp.AdCloseListener() {
//            @Override
//            public void onAdClosed() {
//                loadResult(holoCircularProgressBar, f);
//            }
//        };
//        adControlHelp.loadInterstitialAd(this,adCloseListener, null, false);
//        this.mProgressBarAnimatorCleanDone.addListener(new Animator.AnimatorListener() {
//            public void onAnimationCancel(Animator animator) {
//            }
//
//            public void onAnimationRepeat(Animator animator) {
//            }
//
//            public void onAnimationStart(Animator animator) {
//            }
//
//            public void onAnimationEnd(Animator animator) {
//                adControlHelp.showInterstitialAd(adCloseListener);
//            }
//        });
//        if (animatorListener != null) {
//            this.mProgressBarAnimatorCleanDone.addListener(animatorListener);
//        }
//        this.mProgressBarAnimatorCleanDone.reverse();
//        this.mProgressBarAnimatorCleanDone.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                holoCircularProgressBar.setProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append(valueAnimator.getAnimatedValue());
//                stringBuilder.append("");
//            }
//        });
//        holoCircularProgressBar.setMarkerProgress(f);
//        this.mProgressBarAnimatorCleanDone.start();
//    }

    private void loadResult(HoloCircularProgressBar holoCircularProgressBar, float f) {
        holoCircularProgressBar.setProgress(f);
        CleanResultActivity.this.ivRocket.setVisibility(View.INVISIBLE);
        CleanResultActivity.this.ivTick.setVisibility(View.VISIBLE);
        tvResult.setText(getString(R.string.done));
        CleanResultActivity.this.ivTick.startAnimation(CleanResultActivity.this.ivDoneAnim);
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
