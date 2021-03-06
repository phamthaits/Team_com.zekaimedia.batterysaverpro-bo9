package com.tforlive.batterysaverpro.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.ads.control.AdControl;
import com.ads.control.AdControlHelp;
import com.tforlive.batterysaverpro.R;
import com.tforlive.batterysaverpro.Utilsb.SharePreferenceUtils;
import com.tforlive.batterysaverpro.Utilsb.Utils;

public class ChargeSettingActivity extends BaseActivity implements View.OnClickListener {

    SwitchCompat swWifi, swAutoBrightness, swAutoRun, swBluetooth, swAutoSync;
    private TextView tvWifi, tvWifiDes, tvBluetooth, tvBluetoothDes, tvBrightness, tvBrightnessDes, tvSync, tvSyncDes, tvEnable;
    AdControl adControl;
    AdControlHelp adControlHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_fast_charge);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.fast_charging));
        setSupportActionBar(toolbar);*/
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_48dp);

        /* ------------------- StatusBar Navigation text dark bg white ----------------- */

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        /* ------------------------------------------------------------------ */

        intView();
        intEvent();
        intData();
        checkPer();
        LinearLayout icBack = findViewById(R.id.lr_back);
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    private void checkPer() {
        if (Utils.checkSystemWritePermission(this)) {
            SharePreferenceUtils.getInstance(this).setFlagAds(true);
            tvEnable.setText(R.string.enabled);
            swAutoRun.setChecked(true);
            SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoRun(true);
            intStatus();
        } else {
            ((BaseActivity) this).writePermission(this, ChargeSettingActivity.class);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle(getString(R.string.disable_fast_charging));
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

    public void intView() {
        adControl = AdControl.getInstance(this);
        adControlHelp = AdControlHelp.getInstance(this);
        swWifi = findViewById(R.id.swWifi);
        swBluetooth = findViewById(R.id.swBluetooth);
        swAutoBrightness = findViewById(R.id.swAutoBrightness);
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
        adControlHelp.loadNative(this, findViewById(R.id.native_ads_control_holder), adControl.native_setting);
    }

    public void intEvent() {
        findViewById(R.id.lrWifi).setOnClickListener(this);
        findViewById(R.id.lrBluetooth).setOnClickListener(this);
        findViewById(R.id.lrAutoBrightness).setOnClickListener(this);
        findViewById(R.id.lrAutoRun).setOnClickListener(this);
        findViewById(R.id.lrAutoSync).setOnClickListener(this);
    }

    private void setColorText(boolean isChecked) {
        if (isChecked) {
            tvEnable.setText(getString(R.string.enabled));
            tvEnable.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
            swAutoRun.setChecked(isChecked);
            findViewById(R.id.lrWifi).setEnabled(true);
            findViewById(R.id.lrBluetooth).setEnabled(true);
            findViewById(R.id.lrAutoBrightness).setEnabled(true);
            findViewById(R.id.lrAutoSync).setEnabled(true);


            tvWifi.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
            if (SharePreferenceUtils.getInstance(this).getFsWifi()) {
                swWifi.setChecked(true);
                tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
                tvWifiDes.setText(getString(R.string.on_status));
            } else {
                swWifi.setChecked(false);
                tvWifiDes.setText(getString(R.string.off_status));
                tvWifi.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
                tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
            }

            if (SharePreferenceUtils.getInstance(this).getFsAutoBrightness()) {
                tvBrightness.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
                tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
            } else {
                tvBrightness.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
                tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
            }

            if (SharePreferenceUtils.getInstance(this).getFsBluetooth()) {
                swBluetooth.setChecked(true);
                tvBluetooth.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
                tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
                tvBluetoothDes.setText(getString(R.string.on_status));
            } else {
                swBluetooth.setChecked(false);
                tvBluetoothDes.setText(getString(R.string.off_status));
                tvBluetooth.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
                tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
            }

            if (SharePreferenceUtils.getInstance(this).getFsAutoSync()) {
                tvSync.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
                tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
            } else {
                tvSync.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
                tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
            }
        } else {
            tvEnable.setText(getString(R.string.auto_disable));
            tvEnable.setTextColor(ContextCompat.getColor(this, R.color.color_uncheck));
            tvEnable.getBackground().setAlpha(20);
            swAutoRun.setChecked(isChecked);
            findViewById(R.id.lrWifi).setEnabled(false);
            findViewById(R.id.lrBluetooth).setEnabled(false);
            findViewById(R.id.lrAutoBrightness).setEnabled(false);
            findViewById(R.id.lrAutoSync).setEnabled(false);
            tvWifi.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
            tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
            tvBluetooth.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
            tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
            tvBrightness.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
            tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
            tvSync.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
            tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
            swWifi.setChecked(false);
            swBluetooth.setChecked(false);
            swAutoBrightness.setChecked(false);
            swAutoSync.setChecked(false);
            tvBluetoothDes.setText(R.string.off_status);
            tvWifiDes.setText(R.string.off_status);
        }
    }

    public void intData() {
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

                if (SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsWifi()) {
                    tvWifiDes.setText(R.string.off_status);
                    tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
                    swWifi.setChecked(false);
                    tvWifi.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsWifi(false);
                } else {
                    tvWifiDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
                    tvWifiDes.setText(R.string.on_status);
                    tvWifi.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsWifi(true);
                    swWifi.setChecked(true);
                }
                break;

            case R.id.lrAutoBrightness:

                if (SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoBrightness()) {
                    tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
                    swAutoBrightness.setChecked(false);
                    tvBrightness.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoBrightness(false);
                } else {
                    tvBrightnessDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
                    swAutoBrightness.setChecked(true);
                    tvBrightness.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoBrightness(true);
                }
                break;

            case R.id.lrBluetooth:

                if (SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsBluetooth()) {
                    tvBluetoothDes.setText(R.string.off_status);
                    swBluetooth.setChecked(false);
                    tvBluetooth.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
                    tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsBluetooth(false);
                } else {
                    tvBluetoothDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
                    tvBluetoothDes.setText(R.string.on_status);
                    tvBluetooth.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
                    swBluetooth.setChecked(true);
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsBluetooth(true);
                }
                break;

            case R.id.lrAutoRun:

                if (SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoRun()) {
                    showDialog();
                } else {
                    checkPer();
                }
                break;
            case R.id.lrAutoSync:

                if (SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoSync()) {
                    tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_uncheck));
                    swAutoSync.setChecked(false);
                    tvSync.setTextColor(ContextCompat.getColor(this, R.color.primary_uncheck));
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoSync(false);

                } else {
                    tvSyncDes.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
                    swAutoSync.setChecked(true);
                    tvSync.setTextColor(ContextCompat.getColor(this, R.color.primary_text));
                    SharePreferenceUtils.getInstance(ChargeSettingActivity.this).setFsAutoSync(true);
                }
                break;

            default:
                break;
        }
    }

    public void intStatus() {


        if (SharePreferenceUtils.getInstance(this).getFsAutoRun()) {
            if (SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsWifi()) {
                tvWifiDes.setText(R.string.on_status);
                swWifi.setChecked(true);
            } else {
                tvWifiDes.setText(R.string.off_status);
                swWifi.setChecked(false);
            }
            if (SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoSync()) {
                swAutoSync.setChecked(true);

            } else {
                swAutoSync.setChecked(false);


            }
            if (SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsAutoBrightness()) {

                swAutoBrightness.setChecked(true);
            } else {

                swAutoBrightness.setChecked(false);
            }
            if (SharePreferenceUtils.getInstance(ChargeSettingActivity.this).getFsBluetooth()) {
                tvBluetoothDes.setText(R.string.on_status);
                swBluetooth.setChecked(true);
            } else {

                tvBluetoothDes.setText(R.string.off_status);
                swBluetooth.setChecked(false);
            }
            setColorText(true);
        } else {
            setColorText(false);
        }

    }
}
