package com.newus.battery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.control.AdControl;
import com.newus.battery.MainActivity;
import com.newus.battery.R;

public class RemoveAdsActivity extends AppCompatActivity {

    private AdControl adControl;
    private RemoveAdsHelp removeAdsHelp;
    private Context context;
    public static String TagPurchase = "purchase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_ads);
        context = this;
        Activity activity = this;
        adControl = AdControl.getInstance(context);
        removeAdsHelp = RemoveAdsHelp.getInstance(this, () -> {
            this.finish();
        });
        Button back = findViewById(R.id.bntBackHome);
        back.setOnClickListener(view -> finish());

        Button bntBuyNow = findViewById(R.id.bntBuyNow);
        bntBuyNow.setOnClickListener(view -> removeAdsHelp.Purchase_ads(activity));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!RemoveAdsHelp.bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        if (adControl.remove_ads()) {
            Intent top = new Intent(this, MainActivity.class);
            top.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(top);
            Toast.makeText(this, "Remove ads success!", Toast.LENGTH_LONG).show();
        }
        super.finish();
    }
}
