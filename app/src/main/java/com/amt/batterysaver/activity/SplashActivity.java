package com.amt.batterysaver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ads.control.AdmobHelp;
import com.ads.control.AdControl;
import com.ads.control.FBHelp;
import com.ads.control.TypeAds;
import com.ads.control.funtion.JSONParser;
import com.amt.batterysaver.MainActivity;
import com.amt.batterysaver.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.ads.control.AdControl.AdCloseListener;
import com.ads.control.AdControl.AdLoadedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    Handler mHandlerActivity = new Handler();
    Handler mHandlerfirebase = new Handler();
    Runnable rActivity, rFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        AdControl.getAdControlFromFireBase();
       new AdControl.ReadConfigAsyncTask().execute();
        rActivity = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };
        mHandlerActivity.postDelayed(rActivity, 7000);
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
                switch (AdControl.adControl) {
                    case Admob:
                        AdmobHelp.getInstance().loadInterstitialAd(context, TypeAds.admod_full_splash, adCloseListener, adLoadedListener);
                        break;
                    case Facebook:
                        FBHelp.getInstance().loadInterstitialAd(context, TypeAds.admod_full_splash, adCloseListener, adLoadedListener);
                        break;
                }
            }
        };
        mHandlerfirebase.postDelayed(rFirebase, 3000);
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

