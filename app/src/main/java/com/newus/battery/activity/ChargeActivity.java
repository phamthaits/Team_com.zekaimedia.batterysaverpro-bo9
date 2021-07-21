package com.newus.battery.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

import com.ads.control.AdmobHelp;
import com.newus.battery.R;
import com.newus.battery.Utilsb.Utils;
import com.newus.battery.task.TaskCharge;
import com.newus.battery.task.TaskChargeDetail;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Collection;

import com.newus.battery.Utilsb.SharePreferenceUtils;

public class ChargeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvScan, tvChargeStatus, tvOptimize;
    RelativeLayout rlDone;
    FrameLayout fmResult, imgOutCircle;
    TaskCharge mTaskCharge;
    TaskChargeDetail mTaskChargeDetail;
    Boolean flagExit = false;
    ImageView rocketImage, rocketImageOut, ic_fan_white;

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
        if (!Utils.checkShouldDoing(this, 6)) {
            findViewById(R.id.phone_boost).setVisibility(View.GONE);
        }
        if (!Utils.checkShouldDoing(this, 7)) {
            findViewById(R.id.phone_cooler).setVisibility(View.GONE);
        }
        if (!Utils.checkShouldDoing(this, 3)) {
            findViewById(R.id.trash_cleaner).setVisibility(View.GONE);
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

                return;
//            case R.id.rlBoost:
//                startActivity(new Intent(this, BoostActivity.class));
//                finish();
//                return;
//            case R.id.rlClean:
//                startActivity(new Intent(this, CleanActivity.class));
//                finish();
//                return;
//            case R.id.rlCool:
//                startActivity(new Intent(this, CoolActivity.class));
//                finish();
//                return;
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
        animationRotate.start();

        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.rote_charge_anim_out);
        this.rocketImageOut.startAnimation(loadAnimation);
        loadAnimation.start();

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