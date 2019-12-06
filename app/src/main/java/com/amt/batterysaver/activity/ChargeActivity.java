package com.amt.batterysaver.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ads.control.AdmobHelp;
import com.airbnb.lottie.LottieAnimationView;
import com.amt.batterysaver.Utilsb.SharePreferenceConstant;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.R;
import com.amt.batterysaver.task.TaskCharge;
import com.amt.batterysaver.task.TaskChargeDetail;

public class ChargeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvScan, tvChargeStatus, tvDone;
    RelativeLayout rlScan, rlDone;
    FrameLayout fmResult;
    TaskCharge mTaskCharge;
    TaskChargeDetail mTaskChargeDetail;
    LottieAnimationView imgDone;
    Boolean flagExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_charge_optimize);
        intView();
        intData();
        checkTask();
        AdmobHelp.getInstance().loadNative(ChargeActivity.this);
        AdmobHelp.getInstance().init(ChargeActivity.this, SharePreferenceConstant.admob_full,SharePreferenceConstant.admob_native);
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
        if (!Utils.checkShouldDoing(this, 8)) {
            findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
        } else {
            if (SharePreferenceUtils.getInstance(this).getFsAutoRun())
                findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
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

    public void intView() {
        tvScan = findViewById(R.id.tvScan);
        tvChargeStatus = findViewById(R.id.tvScan);
        rlScan = findViewById(R.id.rlScanning);
        fmResult = findViewById(R.id.fmResult);
        rlDone = findViewById(R.id.rlDone);
        imgDone = findViewById(R.id.imgDone);
        ((ImageView) findViewById(R.id.iv_arrow)).setColorFilter(getResources().getColor(R.color.dark_icon_color), PorterDuff.Mode.MULTIPLY);

    }

    public void intData() {
        rlScan.setVisibility(View.VISIBLE);
        fmResult.setVisibility(View.GONE);
        rlDone.setVisibility(View.GONE);
        mTaskCharge = new TaskCharge(this, tvScan, new TaskCharge.OnTaskListListener() {
            @Override
            public void OnResult() {
                mTaskChargeDetail = new TaskChargeDetail(ChargeActivity.this, tvScan, new TaskChargeDetail.OnTaskBoostListener() {
                    @Override
                    public void OnResult() {
                        rlScan.setVisibility(View.GONE);
                        imgDone.playAnimation();
                        rlDone.setVisibility(View.VISIBLE);
                        Animation zoom_out = AnimationUtils.loadAnimation(ChargeActivity.this, R.anim.zoom_out);
                        rlDone.startAnimation(zoom_out);
                        Runnable runnable = new Runnable() {
                            public void run() {
                                AdmobHelp.getInstance().showInterstitialAd(new AdmobHelp.AdCloseListener() {
                                    @Override
                                    public void onAdClosed() {
                                        loadResult();
                                    }
                                });
                            }
                        };
                        new Handler().postDelayed(runnable, 2000);
                    }
                });
                mTaskChargeDetail.execute();
            }
        });
        mTaskCharge.execute();
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
