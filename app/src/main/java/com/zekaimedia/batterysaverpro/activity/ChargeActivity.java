package com.zekaimedia.batterysaverpro.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.zekaimedia.batterysaverpro.R;
import com.zekaimedia.batterysaverpro.Utilsb.SharePreferenceUtils;
import com.zekaimedia.batterysaverpro.Utilsb.Utils;
import com.zekaimedia.batterysaverpro.task.TaskCharge;
import com.zekaimedia.batterysaverpro.task.TaskChargeDetail;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Collection;

public class ChargeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvScan, tvChargeStatus, tvOptimize;
    RelativeLayout rlDone;
    FrameLayout fmResult, imgOutCircle;
    TaskCharge mTaskCharge;
    TaskChargeDetail mTaskChargeDetail;
    Boolean flagExit = false;
    ImageView rocketImage, rocketImageOut, ic_fan_white;

    LottieAnimationView animationProgress;

    private ImageView ivDone;
    Handler mHandler1;
    Runnable r1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_charge_optimize);
        intView();
        intData();
        checkTask();
    }

    public void checkTask() {
        if (!Utils.checkShouldDoing(this, 3)) {
            findViewById(R.id.cv_trash_cleaner).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lr_back:
                finish();
                return;
            case R.id.ivSetting:
                startActivity(new Intent(this, ChargeSettingActivity.class));
                finish();
                return;
            case R.id.lrBoost:
                startActivity(new Intent(this, BoostActivity.class));
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
        this.ivDone.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);

        animationProgress = findViewById(R.id.av_progress);


        initRippleBackgound();

        animationRotate = AnimationUtils.loadAnimation(this, R.anim.rote_charge_anim_infi);
        rocketImage.startAnimation(animationRotate);
        animationRotate.start();

        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.rote_charge_anim_out);
        this.rocketImageOut.startAnimation(loadAnimation);
        loadAnimation.start();
        animationProgress.playAnimation();

        mHandler1 = new Handler();
        r1 = new Runnable() {
            @Override
            public void run() {
                loadResult();
            }
        };
    }

    private Animation ivDoneAnim;

    public void intData() {
        fmResult.setVisibility(View.GONE);
        rlDone.setVisibility(View.GONE);
        mTaskCharge = new TaskCharge(this, tvScan, new TaskCharge.OnTaskListListener() {
            @Override
            public void OnResult() {
                mTaskChargeDetail = new TaskChargeDetail(ChargeActivity.this, tvScan, new TaskChargeDetail.OnTaskBoostListener() {
                    @Override
                    public void OnResult() {
                        rocketImage.clearAnimation();

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
                        mHandler1.postDelayed(r1, 2000);
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
        if (mHandler1 != null && r1 != null)
            mHandler1.removeCallbacks(r1);
        super.onDestroy();
        cancleUIUPdate();
    }
}
