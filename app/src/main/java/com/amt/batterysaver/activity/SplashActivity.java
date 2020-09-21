package com.amt.batterysaver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ads.control.AdmobHelp;
import com.ads.control.AdControl;
import com.amt.batterysaver.MainActivity;
import com.amt.batterysaver.R;

public class SplashActivity extends AppCompatActivity {

    Handler mHandlerActivity = new Handler();
    Handler mHandlerfirebase = new Handler();
    Runnable rActivity, rFirebase;
    AdmobHelp admobHelp;
    AdControl adControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        admobHelp = AdmobHelp.getInstance(this);
        adControl = AdControl.getInstance(this);

        if (admobHelp.is_reload_firebase())
            admobHelp.getAdControlFromFireBase();

        rActivity = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };

        AdmobHelp.AdCloseListener adCloseListener = () -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//            finish();
        };
        AdmobHelp.AdLoadedListener adLoadedListener = () -> {
            if (mHandlerActivity != null)
                mHandlerActivity.removeCallbacks(rActivity);
            if (!isFinishing()) {
                admobHelp.showInterstitialAd(adCloseListener);
            }
        };

        rFirebase = new Runnable() {
            @Override
            public void run() {
                admobHelp.loadInterstitialAd(adCloseListener, adLoadedListener, adControl.admob_full(), false);
                if (adControl.remove_ads()) adCloseListener.onAdClosed();
                else
                    mHandlerActivity.postDelayed(rActivity, 5000);
            }
        };
        mHandlerfirebase.postDelayed(rFirebase, !admobHelp.is_reload_firebase() ? 1000 : 4000);
    }

    protected void onDestroy() {
        if (mHandlerfirebase != null && rFirebase != null)
            mHandlerfirebase.removeCallbacks(rFirebase);
        if (mHandlerActivity != null && rActivity != null)
            mHandlerActivity.removeCallbacks(rActivity);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
