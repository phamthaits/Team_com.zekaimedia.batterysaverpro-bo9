package com.newus.batteryfastcharge.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.ads.control.AdControlHelp;
import com.ads.control.AdControl;
import com.newus.batteryfastcharge.MainActivity;
import com.newus.batteryfastcharge.R;

public class SplashActivity extends AppCompatActivity {

    private AdControl adControl;
    private AdControlHelp adControlHelp;
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean isStill_startMainActivity = true;

    private void startMainActivity() {
        Intent mIntent = new Intent(this, MainActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        adControl = AdControl.getInstance(this);
        adControlHelp = AdControlHelp.getInstance(this);
        runnable = () -> {
            isStill_startMainActivity = false;
            startMainActivity();
            finish();
        };
        AdControlHelp.AdCloseListener adCloseListener = () -> {
            startMainActivity();
        };
        AdControlHelp.AdLoadedListener adLoadedListener = new AdControlHelp.AdLoadedListener() {
            @Override
            public void onAdLoaded() {
                if (handler != null) {
                    handler.removeCallbacks(runnable);
                }
            }
        };
        Log.v("ads", "Remove_ads: " + adControl.remove_ads());
        handler.postDelayed(runnable, 8000);
        AdControlHelp.FireBaseListener fireBaseListener = new AdControlHelp.FireBaseListener() {
            @Override
            public void addOnCompleteListener() {
                adControlHelp.mobileAdsInitialize(SplashActivity.this, new AdControlHelp.MobileAdsInitialize() {
                    @Override
                    public void onInitialized() {
                        if (isStill_startMainActivity) {
                            adControlHelp.loadInterstitialAd(SplashActivity.this, adCloseListener, adLoadedListener, true);
//                            SplashActivity.this.startMainActivity();
//                            finish();
                        }
                    }
                });
            }
        };
        if (adControlHelp.is_reload_firebase()) {
            Log.v("ads", "reload Firebase: True");
            adControlHelp.getAdControlFromFireBase(fireBaseListener);
        } else fireBaseListener.addOnCompleteListener();
    }

    protected void onDestroy() {
        Log.v("test", "On Destroy");
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        Log.v("test", "On Finish");
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.finish();
    }
}
