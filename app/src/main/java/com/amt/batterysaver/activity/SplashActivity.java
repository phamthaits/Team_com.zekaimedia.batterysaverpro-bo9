package com.amt.batterysaver.activity;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.ads.control.AdmobHelp;
import com.ads.control.AdmodAd;
import com.amt.batterysaver.MainActivity;
import com.amt.batterysaver.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class SplashActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    Handler mHandler = new Handler();
    Runnable r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        AdmobHelp.getAdmodFromFireBase();
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(AdmodAd.admod_full_splash);

        r = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };
        mHandler.postDelayed(r, 7000);

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
//                startActivity(new Intent(SplashActivity.this, BoosterMainDialog.class));
//                finish();
                if (mHandler != null && r != null)
                    mHandler.removeCallbacks(r);
                if (!isFinishing())
                    mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAdClosed() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null && r != null)
            mHandler.removeCallbacks(r);
        super.onDestroy();
    }
}
