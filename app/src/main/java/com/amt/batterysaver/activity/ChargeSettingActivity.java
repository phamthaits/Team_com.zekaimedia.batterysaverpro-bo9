package com.amt.batterysaver.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.R;

public class ChargeSettingActivity extends AppCompatActivity implements View.OnClickListener{

    SwitchCompat swWifi,swAutoBrightness,swAutoRun,swBluetooth,swAutoSync;
    private TextView tvWifi,tvWifiDes,tvBluetooth,tvBluetoothDes,tvBrightness,tvBrightnessDes,tvSync,tvSyncDes,tvEnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_fast_charge);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.fast_charging));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intView();
        intEvent();
        intData();
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.disable_fast_charging) );
        builder.setMessage(getString(R.string.disable_fast_charging_des));

        String positiveText = getString(R.string.auto_disable);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvEnable.setText(R.string.auto_disable);
                        setColorText(false);
                        swAutoRun.setChecked(false);
                        SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoRun(false);
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvEnable.setText(R.string.enabled);
                        swAutoRun.setChecked(true);
                        SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoRun(true);
                        intStatus();
                    }
                });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
    public void intView(){
        swWifi = findViewById(R.id.swWifi);
        swBluetooth = findViewById(R.id.swBluetooth);
        swAutoBrightness= findViewById(R.id.swAutoBrightness);
        swAutoRun = findViewById(R.id.swAutoRun);
        swAutoSync = findViewById(R.id.swAutoSync);
        tvWifi = findViewById(R.id.tvWifi);
        tvWifiDes = findViewById(R.id.tvWifiDes);
        tvBluetooth = findViewById(R.id.tvBluetooth);
        tvBluetoothDes = findViewById(R.id.tvBluetoothDes);
        tvBrightness = findViewById(R.id.tvBrightness);
        tvBrightnessDes = findViewById(R.id.tvBrightnessDes);
        tvSync = findViewById(R.id.tvSync);
        tvSyncDes = findViewById(R.id.tvSyncDes);
        tvEnable = findViewById(R.id.tvEnable);
    }
    public void intEvent(){
        findViewById(R.id.lrWifi).setOnClickListener(this);
        findViewById(R.id.lrBluetooth).setOnClickListener(this);
        findViewById(R.id.lrAutoBrightness).setOnClickListener(this);
        findViewById(R.id.lrAutoRun).setOnClickListener(this);
        findViewById(R.id.lrAutoSync).setOnClickListener(this);
    }
    private void setColorText(boolean isChecked) {
        if (isChecked) {
            tvEnable.setText(getString(R.string.enabled));
            tvEnable.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            swAutoRun.setChecked(isChecked);
            findViewById(R.id.lrWifi).setEnabled(true);
            findViewById(R.id.lrBluetooth).setEnabled(true);
            findViewById(R.id.lrAutoBrightness).setEnabled(true);
            findViewById(R.id.lrAutoSync).setEnabled(true);


            tvWifi.setTextColor(ContextCompat.getColor(this, R.color.text_color));
            if(SharePreferenceUtils.getInstance(this).getFsWifi()){
                swWifi.setChecked(true);
                tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                tvWifiDes.setText(getString(R.string.on_status));

            }else{
                swWifi.setChecked(false);
                tvWifiDes.setText(getString(R.string.off_status));
                tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            }

            if(SharePreferenceUtils.getInstance(this).getFsBluetooth()){
                swBluetooth.setChecked(true);
                tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                tvBluetoothDes.setText(getString(R.string.on_status));
            }else{
                swBluetooth.setChecked(false);
                tvBluetoothDes.setText(getString(R.string.off_status));
                tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            }
            tvBluetooth.setTextColor(ContextCompat.getColor(this, R.color.text_color));

            if(SharePreferenceUtils.getInstance(this).getFsAutoBrightness()){
                tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            }else{
                tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            }
            tvBrightness.setTextColor(ContextCompat.getColor(this, R.color.text_color));


            if(SharePreferenceUtils.getInstance(this).getFsAutoSync()){
                tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            }else{
                tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            }
            tvSync.setTextColor(ContextCompat.getColor(this, R.color.text_color));
        } else {
            tvEnable.setText(getString(R.string.auto_disable));
            tvEnable.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            swAutoRun.setChecked(isChecked);
            findViewById(R.id.lrWifi).setEnabled(false);
            findViewById(R.id.lrBluetooth).setEnabled(false);
            findViewById(R.id.lrAutoBrightness).setEnabled(false);
            findViewById(R.id.lrAutoSync).setEnabled(false);
            tvWifi.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvBluetooth.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvBrightness.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvSync.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            swWifi.setChecked(false);
            swBluetooth.setChecked(false);
            swAutoBrightness.setChecked(false);
            swAutoSync.setChecked(false);
            tvBluetoothDes.setText(R.string.off_status);
            tvWifiDes.setText(R.string.off_status);
        }
    }
    public void intData(){
        swWifi.setChecked(SharePreferenceUtils.getInstance(this).getFsWifi());
        swBluetooth.setChecked(SharePreferenceUtils.getInstance(this).getFsBluetooth());
        swAutoBrightness.setChecked(SharePreferenceUtils.getInstance(this).getAutoBrightness());
        swAutoRun.setChecked(SharePreferenceUtils.getInstance(this).getFsAutoRun());
        swAutoSync.setChecked(SharePreferenceUtils.getInstance(this).getFsAutoSync());
        intStatus();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                break;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lrWifi:

                if(SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsWifi()){
                    tvWifiDes.setText(R.string.off_status);
                    tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
                    swWifi.setChecked(false);
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsWifi(false);
                }else{
                    tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                    tvWifiDes.setText(R.string.on_status);
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsWifi(true);
                    swWifi.setChecked(true);
                }
                break;
            case R.id.lrBluetooth:

                if(SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsBluetooth()){
                    tvBluetoothDes.setText(R.string.off_status);
                    swBluetooth.setChecked(false);
                    tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsBluetooth(false);
                }else{
                    tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                    tvBluetoothDes.setText(R.string.on_status);
                    swBluetooth.setChecked(true);
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsBluetooth(true);
                }
                break;
            case R.id.lrAutoBrightness:

                if(SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoBrightness()){
                    tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
                    swAutoBrightness.setChecked(false);
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoBrightness(false);
                }else{
                    tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                    swAutoBrightness.setChecked(true);
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoBrightness(true);
                }
                break;
            case R.id.lrAutoRun:

                if(SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoRun()){
                    showDialog();
                }else{

                    tvEnable.setText(R.string.enabled);
                    swAutoRun.setChecked(true);
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoRun(true);
                    intStatus();
                }
                break;
            case R.id.lrAutoSync:

                if(SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoSync()){
                    tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
                    swAutoSync.setChecked(false);
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoSync(false);

                }else{
                    tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                    swAutoSync.setChecked(true);
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoSync(true);


                }
                break;

            default:
                break;
        }
    }
    public void intStatus(){



        if(SharePreferenceUtils.getInstance(this).getFsAutoRun()){
            if(SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsWifi()){
                tvWifiDes.setText(R.string.on_status);
                swWifi.setChecked(true);
            }else{
                tvWifiDes.setText(R.string.off_status);
                swWifi.setChecked(false);
            }
            if(SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoSync()){
                swAutoSync.setChecked(true);

            }else{
                swAutoSync.setChecked(false);


            }
            if(SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoBrightness()){

                swAutoBrightness.setChecked(true);
            }else{

                swAutoBrightness.setChecked(false);
            }
            if(SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsBluetooth()){
                tvBluetoothDes.setText(R.string.on_status);
                swBluetooth.setChecked(true);
            }else{

                tvBluetoothDes.setText(R.string.off_status);
                swBluetooth.setChecked(false);
            }
            setColorText(true);
        }else{
            setColorText(false);
        }

    }
}
