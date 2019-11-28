package com.amt.batterysaver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.control.AdmobHelp;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.MainActivity;
import com.amt.fastcharging.batterysaver.R;


public class SplashActivity extends AppCompatActivity {

    Handler mHandler;
    Runnable r ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_spash_screen);
        AdmobHelp.getInstance().init(SplashActivity.this);

        mHandler =new Handler();
        r =  new Runnable() {
            @Override
            public void run() {
                AdmobHelp.getInstance().showInterstitialAd(new AdmobHelp.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        };

        mHandler.postDelayed(r, 4000);
    }

    @Override
    protected void onDestroy() {
        if(mHandler!=null&&r!=null)
            mHandler.removeCallbacks(r);
        super.onDestroy();
    }
}
