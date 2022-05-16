package com.tforlive.batterysaverpro.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ads.control.AdControl;
import com.tforlive.batterysaverpro.R;

public class PermissionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdControl._isShowOpenAds=false;
        setContentView(R.layout.activity_guide_setting);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        /*getWindow().setStatusBarColor(Color.rgb(71, 71, 73));
        getWindow().setNavigationBarColor(Color.rgb(71, 71, 73));*/
        String text_permission = getIntent().getStringExtra("text_permission");
        if(text_permission==null||text_permission.isEmpty())
            text_permission=getString(R.string.setting_permission_request_title);
        TextView textView = findViewById(R.id.text_permission);
        textView.setText(text_permission);
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
