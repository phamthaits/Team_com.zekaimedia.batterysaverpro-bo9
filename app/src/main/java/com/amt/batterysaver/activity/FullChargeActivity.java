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

import com.ads.control.AdControl;
import com.ads.control.AdmobHelp;
import com.ads.control.FBHelp;
import com.ads.control.TypeAds;
import com.amt.batterysaver.R;
import com.amt.batterysaver.Utilsb.SharePreferenceConstant;
import com.amt.batterysaver.Utilsb.Utils;
import com.facebook.ads.Ad;

public class FullChargeActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout fmResult;
    Boolean flagExit = false;
    Handler mHandler;
    Runnable r;
    Handler mHandler1;
    Runnable r1;
    private AdControl adControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_charge_full);
        intView();
        adControl = AdControl.getInstance(this);
        if (adControl.isLoadAds()) {
            switch (adControl.adcontrolType()) {
                case Admob:
                    AdmobHelp.getInstance().loadNative(this, TypeAds.admod_native_fullcharge);
                    break;
                case Facebook:
                    FBHelp.getInstance().loadNative(this);
                    break;
            }
        }
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
        AdControl.AdCloseListener adCloseListener = new AdControl.AdCloseListener() {
            @Override
            public void onAdClosed() {

            }
        };
        if (adControl.isLoadAds()) {
            switch (adControl.adcontrolType()) {
                case Facebook:
                    FBHelp.getInstance().loadInterstitialAd(this, adCloseListener, null);
                    break;
                case Admob:
                    AdmobHelp.getInstance().loadInterstitialAd(this, TypeAds.admod_full_fullcharge, adCloseListener, null);
                    break;
            }
        }
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
