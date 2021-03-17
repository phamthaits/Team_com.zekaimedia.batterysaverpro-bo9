package com.newus.battery.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.ads.control.AdControlHelp;
import com.ads.control.AdControlHelp.FireBaseListener;
import com.ads.control.AdControlHelp.AdCloseListener;
import com.ads.control.AdControlHelp.AdLoadedListener;
import com.ads.control.AdControl;
import com.newus.battery.MainActivity;
import com.newus.battery.R;

public class SplashActivity extends AppCompatActivity {

    private AdControl adControl;
    private AdControlHelp adControlHelp;
    private Handler handler = new Handler();
    private Runnable runnable;
    private AdCloseListener adCloseListener = () -> {
        Intent mIntent = new Intent(this, MainActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        adControl = AdControl.getInstance(this);
        adControlHelp = AdControlHelp.getInstance(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        RemoveAdsHelp.getInstance(this, null);
        AdLoadedListener adLoadedListener = new AdLoadedListener() {
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
                adControl.isStillShowAds = false;
                adCloseListener.onAdClosed();
            }
        };
        handler.postDelayed(runnable, 8000);
        FireBaseListener fireBaseListener = () -> {
            adControlHelp.loadInterstitialAd(this, adCloseListener, adLoadedListener, true);
        };
        if (adControlHelp.is_reload_firebase()) {
            Log.v("ads", "reload Firebase: True");
            adControlHelp.getAdControlFromFireBase(fireBaseListener);
        } else fireBaseListener.addOnCompleteListener();
    }

    protected void onDestroy() {
        Log.v("Thaidaica", "On Destroy");
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        Log.v("Thaidaica", "On Finish");
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.finish();
    }
}
