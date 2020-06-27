package com.amt.batterysaver.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.amt.batterysaver.R;
import com.amt.batterysaver.Utilsb.SharePreferenceConstant;
import com.amt.batterysaver.Utilsb.Utils;

public class FullChargeActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout fmResult;
    Boolean flagExit = false;
    Handler mHandler;
    Runnable r;
    Handler mHandler1;
    Runnable r1;
//    Boolean isLoadAds = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_charge_full);
        intView();

//        isLoadAds = !AdmodAd.admod_native_fullcharge.equals("");
//        if (isLoadAds)
//            AdmodRef.initInterstitialAd(FullChargeActivity.this, TypeAds.admod_full_fullcharge);
//
//        r = new Runnable() {
//            @Override
//            public void run() {
//                AdmodRef.loadNative(FullChargeActivity.this, TypeAds.admod_native_fullcharge);
//            }
//        };
//        r1 = new Runnable() {
//            @Override
//            public void run() {
//                AdmobHelp.getInstance().showInterstitialAd(new AdmobHelp.AdCloseListener() {
//                    @Override
//                    public void onAdClosed() {
//                    }
//                });
//            }
//        };
//        mHandler = new Handler();
//        mHandler1 = new Handler();
//        if (isLoadAds) {
//            mHandler.postDelayed(r, 2000);
//            mHandler1.postDelayed(r1, 5000);
//        }
        loadResult();
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
        fmResult = findViewById(R.id.fmResult);
        ((ImageView) findViewById(R.id.iv_arrow)).setColorFilter(getResources().getColor(R.color.dark_icon_color), PorterDuff.Mode.MULTIPLY);
    }

    private void loadResult() {
        fmResult.setVisibility(View.VISIBLE);
        Animation downtoup = AnimationUtils.loadAnimation(FullChargeActivity.this, R.anim.downtoup);
        fmResult.startAnimation(downtoup);
        flagExit = true;
        SharePreferenceConstant.full_battery_loaded = true;
//        AdmobHelp.getInstance().showInterstitialAd(new AdmobHelp.AdCloseListener() {
//            @Override
//            public void onAdClosed() {
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null && r != null)
            mHandler.removeCallbacks(r);
        if (mHandler1 != null && r1 != null)
            mHandler.removeCallbacks(r1);
        super.onDestroy();
    }
}
