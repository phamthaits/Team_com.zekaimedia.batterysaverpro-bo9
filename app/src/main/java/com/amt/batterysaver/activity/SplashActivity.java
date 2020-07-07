package com.amt.batterysaver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ads.control.AdControlHelp;
import com.ads.control.AdmobHelp;
import com.ads.control.AdControl;
import com.ads.control.FBHelp;
import com.ads.control.TypeAds;
import com.ads.control.funtion.JSONParser;
import com.amt.batterysaver.MainActivity;
import com.amt.batterysaver.R;
import com.ads.control.AdControlHelp.AdCloseListener;
import com.ads.control.AdControlHelp.AdLoadedListener;

public class SplashActivity extends AppCompatActivity {

    Handler mHandlerActivity = new Handler();
    Handler mHandlerfirebase = new Handler();
    Runnable rActivity, rFirebase;
    AdControl adControl;
    AdControlHelp adControlHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        adControl = AdControl.getInstance(this);
        adControlHelp = AdControlHelp.getInstance(this);
        if (adControlHelp.is_reload_firebase())
            adControlHelp.getAdControlFromFireBase();
        new AdControlHelp.ReadConfigAsyncTask().execute();

        rActivity = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };
        mHandlerActivity.postDelayed(rActivity, !adControlHelp.is_reload_firebase() ? 5000 : 8000);
        Log.v("ads", adControl.isInit() + "");
        AdLoadedListener adLoadedListener = () -> {
            if (mHandlerActivity != null && rActivity != null)
                mHandlerActivity.removeCallbacks(rActivity);
        };
        Context context = this;
        rFirebase = new Runnable() {
            @Override
            public void run() {
                AdCloseListener adCloseListener = () -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                };
                switch (adControl.adcontrolType()) {
                    case Admob:
                        AdmobHelp.getInstance().loadInterstitialAd(context, TypeAds.admod_full_splash, adCloseListener, adLoadedListener);
                        break;
                    case Facebook:
                        FBHelp.getInstance().loadInterstitialAd(context, adCloseListener, adLoadedListener);
                        break;
                }
            }
        };
        mHandlerfirebase.postDelayed(rFirebase, !adControlHelp.is_reload_firebase() ? 1000 : 4000);
    }

    @Override
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

