package app.tahachi.batterydoctor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import app.ads.control.AdControlHelp;
import app.tahachi.batterydoctor.MainActivity;
import app.tahachi.batterydoctor.R;
import app.tahachi.batterydoctor.Utilsb.SharePreferenceConstant;
import app.tahachi.batterydoctor.Utilsb.SharePreferenceUtils;
import app.tahachi.batterydoctor.notification.NotificationBattery;

import static android.app.Activity.RESULT_OK;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    SwitchCompat swKillApp, swLowBattery, swBatteryFull, swCoolDown, swBoost, swTemp, swEnableNotification;
    TextView tvTempertureDes, tvLanguageDes, tvDNDDes;
    Boolean flag = false;
    private AdControlHelp adControlHelp;
    private Context context;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context = this;
        activity = this;
        adControlHelp = AdControlHelp.getInstance(context);
        adControlHelp.loadNative(this, findViewById(R.id.native_ads_control_holder),
                R.layout.item_admob_native_setting, R.layout.item_fb_native_setting, R.layout.item_mopub_native_setting,
                false,false);
        intView();
        intData();
    }

    public void intView() {
        findViewById(R.id.lrEnableNotification).setOnClickListener(this);
//        findViewById(R.id.lrFastCharge).setOnClickListener(this);
        findViewById(R.id.lrKillApp).setOnClickListener(this);
        findViewById(R.id.lrIngoreList).setOnClickListener(this);
        findViewById(R.id.lrTemperture).setOnClickListener(this);
        findViewById(R.id.lrLanguage).setOnClickListener(this);
        findViewById(R.id.lrDnd).setOnClickListener(this);
        findViewById(R.id.lrBatteryFull).setOnClickListener(this);
        findViewById(R.id.lrCoolDown).setOnClickListener(this);
        findViewById(R.id.lrLowPower).setOnClickListener(this);
        findViewById(R.id.lrBoost).setOnClickListener(this);
        swEnableNotification = findViewById(R.id.swEnableNotification);
        swKillApp = findViewById(R.id.swKillApp);
        swLowBattery = findViewById(R.id.swLowPower);
        swBatteryFull = findViewById(R.id.swBatteryFull);
        swCoolDown = findViewById(R.id.swCoolDown);
        swBoost = findViewById(R.id.swBoost);
        swTemp = findViewById(R.id.swTemp);

        tvLanguageDes = findViewById(R.id.tvLanguageDes);
        tvTempertureDes = findViewById(R.id.tvTempertureDes);
        tvDNDDes = findViewById(R.id.tvDNDDes);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lrEnableNotification:
                Intent intent = new Intent();
                intent.setAction(NotificationBattery.UPDATE_NOTIFICATION_ENABLE);
                if (SharePreferenceUtils.getInstance(activity).getNotification()) {
                    SharePreferenceUtils.getInstance(activity).setNotification(false);
                    swEnableNotification.setChecked(false);
                    intent.putExtra("NOTIFICATION_UPDATE_MODE", 1);
                } else {
                    SharePreferenceUtils.getInstance(activity).setNotification(true);
                    swEnableNotification.setChecked(true);
                    intent.putExtra("NOTIFICATION_UPDATE_MODE", 0);
                }

                activity.sendBroadcast(intent);
                break;
            case R.id.lrKillApp:
                if (SharePreferenceUtils.getInstance(activity).getKillApp()) {
                    swKillApp.setChecked(false);
                    SharePreferenceUtils.getInstance(activity).setKillApp(false);
                } else {
                    swKillApp.setChecked(true);
                    SharePreferenceUtils.getInstance(activity).setKillApp(true);
                }
                break;
            case R.id.lrIngoreList:

                startActivity(new Intent(activity, WhiteListActivity.class));
                break;
            case R.id.lrCoolDown:
                if (SharePreferenceUtils.getInstance(activity).getCoolDownReminder()) {
                    SharePreferenceUtils.getInstance(activity).setCoolDownReminder(false);
                    swCoolDown.setChecked(false);
                } else {
                    SharePreferenceUtils.getInstance(activity).setCoolDownReminder(true);
                    swCoolDown.setChecked(true);
                }

                break;
            /*case R.id.lrBoost:
                if (SharePreferenceUtils.getInstance(activity).getBoostReminder()) {
                    SharePreferenceUtils.getInstance(activity).setBoostRemindert(false);
                    swBoost.setChecked(false);
                } else {
                    SharePreferenceUtils.getInstance(activity).setBoostRemindert(true);
                    swBoost.setChecked(true);
                }
                break;*/
            case R.id.lrDnd:
                startActivity(new Intent(activity, DoNotDisturbActivity.class));
                break;
            case R.id.lrBatteryFull:
                if (SharePreferenceUtils.getInstance(activity).getChargeFullReminder()) {
                    SharePreferenceUtils.getInstance(activity).setChargeFullReminder(false);
                    swBatteryFull.setChecked(false);
                } else {
                    SharePreferenceUtils.getInstance(activity).setChargeFullReminder(true);
                    swBatteryFull.setChecked(true);
                }


                break;
            case R.id.lrLowPower:
                if (SharePreferenceUtils.getInstance(activity).getLowBatteryReminder()) {
                    SharePreferenceUtils.getInstance(activity).setLowBatteryReminder(false);
                    swLowBattery.setChecked(false);
                } else {
                    SharePreferenceUtils.getInstance(activity).setLowBatteryReminder(true);
                    swLowBattery.setChecked(true);
                }


                break;
            case R.id.lrTemperture:
                if (SharePreferenceUtils.getInstance(activity).getTempFormat()) {
                    tvTempertureDes.setText(getString(R.string.fahrenheit));
                    swTemp.setChecked(false);
                    SharePreferenceUtils.getInstance(activity).setTempFormat(false);

                } else {
                    tvTempertureDes.setText(getString(R.string.celsius));
                    swTemp.setChecked(true);
                    SharePreferenceUtils.getInstance(activity).setTempFormat(true);
                }
                Intent i = new Intent();
                i.setAction(NotificationBattery.UPDATE_NOTIFICATION_ENABLE);
                activity.sendBroadcast(i);
                break;
            case R.id.lrLanguage:
                SharePreferenceUtils.getInstance(activity).saveLanguageChange(false);
                startActivityForResult(new Intent(activity, LanguageActivity.class), SharePreferenceConstant.LANGUAGE_REQUEST);
                break;
            case R.id.lr_back:
                finish();
                return;
            default:
                break;
        }
    }

    public void intData() {

        swEnableNotification.setChecked(SharePreferenceUtils.getInstance(activity).getNotification());
        swKillApp.setChecked(SharePreferenceUtils.getInstance(activity).getKillApp());
        swBatteryFull.setChecked(SharePreferenceUtils.getInstance(activity).getChargeFullReminder());
        swLowBattery.setChecked(SharePreferenceUtils.getInstance(activity).getLowBatteryReminder());
        swBoost.setChecked(SharePreferenceUtils.getInstance(activity).getBoostReminder());
        swCoolDown.setChecked(SharePreferenceUtils.getInstance(activity).getCoolDownReminder());

        if (SharePreferenceUtils.getInstance(activity).getTempFormat()) {
            tvTempertureDes.setText(getString(R.string.celsius));
            swTemp.setChecked(true);
        } else {
            tvTempertureDes.setText(getString(R.string.fahrenheit));
            swTemp.setChecked(false);
        }
    }

    public String intTimeOn(int time) {
        int dNDStartTime = time;
        int i = dNDStartTime / 100;
        dNDStartTime %= 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%02d", Integer.valueOf(dNDStartTime)));
        return stringBuilder.toString();
    }

    public String intTimeOff(int time) {
        int dNDEndTime = time;
        int i = dNDEndTime / 100;
        dNDEndTime %= 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%02d", Integer.valueOf(dNDEndTime)));
        return stringBuilder.toString();
    }


    @Override
    public void onResume() {
        super.onResume();
        tvDNDDes.setText(intTimeOff(SharePreferenceUtils.getInstance(activity).getDndStart()) + " - " + intTimeOn(SharePreferenceUtils.getInstance(activity).getDndStop()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SharePreferenceConstant
                    .LANGUAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    activity.finish();
                    startActivity(new Intent(activity, MainActivity.class));
                }
                break;
        }
    }

}


