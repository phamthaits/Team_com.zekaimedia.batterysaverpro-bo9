package com.qamob.batterysaver.billing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ads.control.AdControl;
import com.qamob.batterysaver.MainActivity;
import com.qamob.batterysaver.R;

public class RemoveAdsActivity extends AppCompatActivity {

    private AdControl adControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_ads);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg_ads));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.bg_ads));
        setContentView(R.layout.activity_remove_ads);
        adControl = AdControl.getInstance(this);
        ImageView back = findViewById(R.id.bntBackHome);
        back.setOnClickListener(view -> finish());
        Button bntBuyNow = findViewById(R.id.bntBuyNow);
        BillingClientHelp.initBillingClient(this, this, new BillingClientHelp.BillingInitListener() {
            @Override
            public void onBillingInitialized() {
                if (adControl.remove_ads())
                    finish();
            }
        });
        bntBuyNow.setOnClickListener(view -> BillingClientHelp.buyProduct(this));
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
