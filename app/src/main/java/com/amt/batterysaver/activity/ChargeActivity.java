package com.amt.batterysaver.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.ads.control.AdmobHelp;
import com.ads.control.AdControl;
import com.ads.control.AdmobHelp;
import com.ads.control.FBHelp;
import com.ads.control.TypeAds;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.R;
import com.amt.batterysaver.task.TaskCharge;
import com.amt.batterysaver.task.TaskChargeDetail;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Collection;

import com.ads.control.AdControlHelp.AdCloseListener;
import com.ads.control.AdControlHelp.AdLoadedListener;

public class ChargeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvScan, tvChargeStatus, tvOptimize;
    RelativeLayout rlDone;
    FrameLayout fmResult, imgOutCircle;
    TaskCharge mTaskCharge;
    TaskChargeDetail mTaskChargeDetail;
    Boolean flagExit = false;
    ImageView rocketImage, rocketImageOut, ic_fan_white;

    private ImageView ivDone;
    private AdControl adControl;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_charge_optimize);
        intView();
        intData();
        context = this;
        adControl = AdControl.getInstance(context);
//        checkTask();
        if (adControl.isLoadAds()) {
            switch (adControl.adcontrolType()) {
                case Admob:
                    AdmobHelp.getInstance().loadNative(this, TypeAds.admod_native_fastcharge);
                    break;
                case Facebook:
                    FBHelp.getInstance().loadNative(this);
                    break;
            }
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
//        if (!Utils.checkShouldDoing(this, 8)) {
//            findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//        } else {
//            if (SharePreferenceUtils.getInstance(this).getFsAutoRun())
//                findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lr_back:
                finish();
                return;

            case R.id.ivSetting:
                startActivity(new Intent(this, ChargeSettingActivity.class));

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

    private Animation animationRotate;

    public void intView() {
        this.rocketImageOut = findViewById(R.id.ivDoneHoloCirular);
        this.ivDone = findViewById(R.id.clean_done_iv_done);
        tvScan = findViewById(R.id.tvScan);
        tvChargeStatus = findViewById(R.id.tvScan);
//        rlScan = findViewById(R.id.rlScanning);
        fmResult = findViewById(R.id.fmResult);
        rlDone = findViewById(R.id.rlDone);
        rocketImage = findViewById(R.id.ivScan);
        imgOutCircle = findViewById(R.id.imgOutCircle);
        tvOptimize = findViewById(R.id.tvOptimize);
        ic_fan_white = findViewById(R.id.ic_fan_white);
        ((ImageView) findViewById(R.id.iv_arrow)).setColorFilter(getResources().getColor(R.color.dark_icon_color), PorterDuff.Mode.MULTIPLY);
        this.ivDone.setColorFilter(getResources().getColor(R.color.progress_color), PorterDuff.Mode.MULTIPLY);
        this.rocketImageOut.setColorFilter(getResources().getColor(R.color.progress_color), PorterDuff.Mode.MULTIPLY);
        this.rocketImage.setColorFilter(getResources().getColor(R.color.progress_color), PorterDuff.Mode.MULTIPLY);

        initRippleBackgound();

        animationRotate = AnimationUtils.loadAnimation(this, R.anim.rote_charge_anim_infi);
        rocketImage.startAnimation(animationRotate);
//        animationRotate.start();

        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.rote_charge_anim_out);
        this.rocketImageOut.startAnimation(loadAnimation);
//        loadAnimation.start();

    }

    private Animation ivDoneAnim;

    public void intData() {
//        rlScan.setVisibility(View.VISIBLE);
        fmResult.setVisibility(View.GONE);
        rlDone.setVisibility(View.GONE);
        mTaskCharge = new TaskCharge(this, tvScan, new TaskCharge.OnTaskListListener() {
            @Override
            public void OnResult() {
                mTaskChargeDetail = new TaskChargeDetail(ChargeActivity.this, tvScan, new TaskChargeDetail.OnTaskBoostListener() {
                    @Override
                    public void OnResult() {
//                        rocketImage.animate().cancel();
//                        animationRotate.cancel();
                        rocketImage.clearAnimation();

//                        rlScan.setVisibility(View.GONE);
//                        imgDone.playAnimation();
                        tvOptimize.setText(getString(R.string.done));

                        rocketImage.setVisibility(View.GONE);
                        tvScan.setVisibility(View.GONE);
                        rlDone.setVisibility(View.VISIBLE);
                        ic_fan_white.setVisibility(View.GONE);

                        ivDone.setVisibility(View.VISIBLE);
                        ivDoneAnim = AnimationUtils.loadAnimation(ChargeActivity.this, R.anim.ic_done_anim);
                        ivDone.startAnimation(ivDoneAnim);
                        rocketImageOut.setImageResource(R.drawable.rocket_12);

                        Animation zoom_out = AnimationUtils.loadAnimation(ChargeActivity.this, R.anim.zoom_out);
                        rlDone.startAnimation(zoom_out);
                        AdCloseListener adCloseListener = new AdCloseListener() {
                            @Override
                            public void onAdClosed() {
                                loadResult();
                            }
                        };
                        if (adControl.isLoadAds()) {
                            switch (adControl.adcontrolType()) {
                                case Facebook:
                                    FBHelp.getInstance().loadInterstitialAd(context, adCloseListener, null);
                                    break;
                                case Admob:
                                    AdmobHelp.getInstance().loadInterstitialAd(context, TypeAds.admod_full_fastcharge, adCloseListener, null);
                                    break;
                            }
                        } else loadResult();
                    }
                });
                mTaskChargeDetail.execute();
            }
        });
        mTaskCharge.execute();
    }

    private void initRippleBackgound() {
        ((RippleBackground) findViewById(R.id.charge_boost_ripple_background)).startRippleAnimation();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        Collection arrayList = new ArrayList();
        arrayList.add(ObjectAnimator.ofFloat(this.rocketImageOut, "ScaleX", 0.0f, 1.2f, 1.0f));
        arrayList.add(ObjectAnimator.ofFloat(this.rocketImageOut, "ScaleY", 0.0f, 1.2f, 1.0f));
        animatorSet.playTogether(arrayList);
        animatorSet.start();
    }

    private void loadResult() {
        Animation slideUp = AnimationUtils.loadAnimation(ChargeActivity.this, R.anim.zoom_in);
        rlDone.startAnimation(slideUp);
        rlDone.setVisibility(View.GONE);
        fmResult.setVisibility(View.VISIBLE);
        Animation downtoup = AnimationUtils.loadAnimation(ChargeActivity.this, R.anim.downtoup);
        fmResult.startAnimation(downtoup);
        flagExit = true;
        SharePreferenceUtils.getInstance(ChargeActivity.this).setOptimizeTime(System.currentTimeMillis());

    }

    public void cancleUIUPdate() {
        if (this.mTaskCharge != null && this.mTaskCharge.getStatus() == AsyncTask.Status.RUNNING) {
            this.mTaskCharge.cancel(true);
            this.mTaskCharge = null;
        }
        if (this.mTaskChargeDetail != null && this.mTaskChargeDetail.getStatus() == AsyncTask.Status.RUNNING) {
            this.mTaskChargeDetail.cancel(true);
            this.mTaskChargeDetail = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancleUIUPdate();
    }
}
