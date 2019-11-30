package com.amt.batterysaver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.control.AdmobHelp;
import com.amt.batterysaver.Utilsb.SharePreferenceConstant;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.MainActivity;
import com.amt.batterysaver.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler;
    Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_spash_screen);

        if (SharePreferenceConstant.admob_full == "") {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Ad")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JSONObject object = new JSONObject(document.getData());
                                for (int i = 0; i < object.names().length(); i++) {
                                    try {
                                        String key = object.names().getString(i);
                                        switch (key) {
                                            case "admob_native":
                                                SharePreferenceConstant.admob_native = object.getString(key);
                                                break;
                                            case "admob_full":
                                                SharePreferenceConstant.admob_full = object.getString(key);
                                                break;
                                        }
                                        AdmobHelp.getInstance().init(SplashActivity.this, SharePreferenceConstant.admob_full, SharePreferenceConstant.admob_native);
                                        Log.d("123", "key = " + key + ":" + object.getString(key));
                                    } catch (JSONException e) {
                                        Log.d("123", "Lỗi");
                                        e.printStackTrace();
                                    }
                                }
                                Log.d("123", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("123", "Error getting documents.", task.getException());
                        }
                    });
        } else {
            AdmobHelp.getInstance().init(SplashActivity.this, SharePreferenceConstant.admob_full, SharePreferenceConstant.admob_native);
        }

        mHandler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                AdmobHelp am = AdmobHelp.getInstance();
                if (!am.isInit)
                    am.init(SplashActivity.this, SharePreferenceConstant.admob_full, SharePreferenceConstant.admob_native);
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
        if (mHandler != null && r != null)
            mHandler.removeCallbacks(r);
        super.onDestroy();
    }
}
