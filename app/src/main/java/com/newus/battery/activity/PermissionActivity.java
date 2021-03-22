package com.newus.battery.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.newus.battery.R;

public class PermissionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_setting);

        getWindow().setStatusBarColor(Color.rgb(71,71,73));
        getWindow().setNavigationBarColor(Color.rgb(71,71,73));

        findViewById(R.id.na_guide_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    private void openAndroidPermissionsMenu() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);

        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {

    }
}
