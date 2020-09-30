package com.amt.batterysaver.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.ads.control.AdmobHelp;
import com.ads.control.AdmobHelp.FireBaseListener;
import com.ads.control.AdmobHelp.AdCloseListener;
import com.ads.control.AdControl;
import com.amt.batterysaver.MainActivity;
import com.amt.batterysaver.R;

public class SplashActivity extends AppCompatActivity {

    private AdmobHelp admobHelp;
    private AdControl adControl;
    private Handler handler = new Handler();
    private Runnable runnable;
    private AdCloseListener adCloseListener = () -> {
        startActivity(new Intent(this, MainActivity.class));
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        admobHelp = AdmobHelp.getInstance(this);
        adControl = AdControl.getInstance(this);
        RemoveAdsHelp.getInstance(this, null);
        AdmobHelp.AdLoadedListener adLoadedListener = new AdmobHelp.AdLoadedListener() {
            @Override
            public void onAdLoaded() {
                if (handler != null) {
                    handler.removeCallbacks(runnable);
                }
            }
        };
        runnable = new Runnable() {
            @Override
            public void run() {
                admobHelp.isStillShowAds = false;
                adCloseListener.onAdClosed();
            }
        };
        handler.postDelayed(runnable, 8000);
        FireBaseListener fireBaseListener = () -> admobHelp.loadInterstitialAd(adCloseListener, adLoadedListener, adControl.admob_full(), true);
        if (admobHelp.is_reload_firebase()) {
            Log.v("ads", "reload Firebase: True");
            admobHelp.getAdControlFromFireBase(fireBaseListener);
        } else fireBaseListener.addOnCompleteListener();
    }

    protected void onDestroy() {
        Log.v("Thaidaica", "On Destroy");
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
