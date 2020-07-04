package com.amt.batterysaver.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.ads.control.AdControl;
import com.ads.control.AdmobHelp;
import com.ads.control.FBHelp;
import com.ads.control.TypeAds;
import com.amt.batterysaver.activity.BaseActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.amt.batterysaver.Utilsb.SharePreferenceConstant;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.activity.DoNotDisturbActivity;
import com.amt.batterysaver.activity.LanguageActivity;
import com.amt.batterysaver.activity.WhiteListActivity;
import com.amt.batterysaver.notification.NotificationBattery;
import com.amt.batterysaver.MainActivity;
import com.amt.batterysaver.R;
import com.amt.batterysaver.activity.ChargeSettingActivity;

import java.util.concurrent.Callable;

import static android.app.Activity.RESULT_OK;

public class fmSetting extends Fragment implements View.OnClickListener {

    SwitchCompat swKillApp, swLowBattery, swBatteryFull, swCoolDown, swBoost, swTemp, swEnableNotification;
    TextView tvTempertureDes, tvLanguageDes, tvDNDDes;
    Boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    LineChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);
        intView(view);
        intData();
//        AdmobHelp.getInstance().loadBannerFragment(getActivity(),view);
        switch (AdControl.adControl) {
            case Admob:
                AdmobHelp.getInstance().loadNativeFragment(getActivity(), view, TypeAds.admod_native_setting);
                break;
            case Facebook:
                FBHelp.getInstance().loadNativeFrament(getActivity(), view);
                break;
        }
//        AdmobHelp.getInstance().loadNativeFragment(getActivity(),view);
        return view;
    }

    public void intView(View v) {
        v.findViewById(R.id.lrEnableNotification).setOnClickListener(this);
        v.findViewById(R.id.lrFastCharge).setOnClickListener(this);
        v.findViewById(R.id.lrKillApp).setOnClickListener(this);
        v.findViewById(R.id.lrIngoreList).setOnClickListener(this);
        v.findViewById(R.id.lrTemperture).setOnClickListener(this);
        v.findViewById(R.id.lrLanguage).setOnClickListener(this);
        v.findViewById(R.id.lrDnd).setOnClickListener(this);
        v.findViewById(R.id.lrBatteryFull).setOnClickListener(this);
        v.findViewById(R.id.lrCoolDown).setOnClickListener(this);
        v.findViewById(R.id.lrLowPower).setOnClickListener(this);
        v.findViewById(R.id.lrBoost).setOnClickListener(this);
        swEnableNotification = v.findViewById(R.id.swEnableNotification);
        swKillApp = v.findViewById(R.id.swKillApp);
        swLowBattery = v.findViewById(R.id.swLowPower);
        swBatteryFull = v.findViewById(R.id.swBatteryFull);
        swCoolDown = v.findViewById(R.id.swCoolDown);
        swBoost = v.findViewById(R.id.swBoost);
        swTemp = v.findViewById(R.id.swTemp);

        tvLanguageDes = v.findViewById(R.id.tvLanguageDes);
        tvTempertureDes = v.findViewById(R.id.tvTempertureDes);
        tvDNDDes = v.findViewById(R.id.tvDNDDes);
    }

    public void intEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lrEnableNotification:
                Intent intent = new Intent();
                intent.setAction(NotificationBattery.UPDATE_NOTIFICATION_ENABLE);
                if (SharePreferenceUtils.getInstance(getActivity()).getNotification()) {
                    SharePreferenceUtils.getInstance(getActivity()).setNotification(false);
                    swEnableNotification.setChecked(false);
                    intent.putExtra("NOTIFICATION_UPDATE_MODE", 1);
                } else {
                    SharePreferenceUtils.getInstance(getActivity()).setNotification(true);
                    swEnableNotification.setChecked(true);
                    intent.putExtra("NOTIFICATION_UPDATE_MODE", 0);
                }

                getActivity().sendBroadcast(intent);
                break;
            case R.id.lrFastCharge:
                try {
                    ((BaseActivity)getActivity()).checkdrawPermission(() -> {
                        if (Utils.checkSystemWritePermission(getActivity())) {
                            startActivity(new Intent(getActivity(), ChargeSettingActivity.class));
                        } else {
                            Utils.openAndroidPermissionsMenu(getActivity());
                        }
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.lrKillApp:
                if (SharePreferenceUtils.getInstance(getActivity()).getKillApp()) {
                    swKillApp.setChecked(false);
                    SharePreferenceUtils.getInstance(getActivity()).setKillApp(false);
                } else {
                    swKillApp.setChecked(true);
                    SharePreferenceUtils.getInstance(getActivity()).setKillApp(true);
                }

                break;
            case R.id.lrIngoreList:

                startActivity(new Intent(getActivity(), WhiteListActivity.class));
                break;
            case R.id.lrCoolDown:
                if (SharePreferenceUtils.getInstance(getActivity()).getCoolDownReminder()) {
                    SharePreferenceUtils.getInstance(getActivity()).setCoolDownReminder(false);
                    swCoolDown.setChecked(false);
                } else {
                    SharePreferenceUtils.getInstance(getActivity()).setCoolDownReminder(true);
                    swCoolDown.setChecked(true);
                }

                break;
            case R.id.lrBoost:
                if (SharePreferenceUtils.getInstance(getActivity()).getBoostReminder()) {
                    SharePreferenceUtils.getInstance(getActivity()).setBoostRemindert(false);
                    swBoost.setChecked(false);
                } else {
                    SharePreferenceUtils.getInstance(getActivity()).setBoostRemindert(true);
                    swBoost.setChecked(true);
                }

                break;
            case R.id.lrDnd:
                startActivity(new Intent(getActivity(), DoNotDisturbActivity.class));


                break;
            case R.id.lrBatteryFull:
                if (SharePreferenceUtils.getInstance(getActivity()).getChargeFullReminder()) {
                    SharePreferenceUtils.getInstance(getActivity()).setChargeFullReminder(false);
                    swBatteryFull.setChecked(false);
                } else {
                    SharePreferenceUtils.getInstance(getActivity()).setChargeFullReminder(true);
                    swBatteryFull.setChecked(true);
                }


                break;
            case R.id.lrLowPower:
                if (SharePreferenceUtils.getInstance(getActivity()).getLowBatteryReminder()) {
                    SharePreferenceUtils.getInstance(getActivity()).setLowBatteryReminder(false);
                    swLowBattery.setChecked(false);
                } else {
                    SharePreferenceUtils.getInstance(getActivity()).setLowBatteryReminder(true);
                    swLowBattery.setChecked(true);
                }


                break;
            case R.id.lrTemperture:
                if (SharePreferenceUtils.getInstance(getActivity()).getTempFormat()) {
                    tvTempertureDes.setText(getString(R.string.fahrenheit));
                    swTemp.setChecked(false);
                    SharePreferenceUtils.getInstance(getActivity()).setTempFormat(false);

                } else {
                    tvTempertureDes.setText(getString(R.string.celsius));
                    swTemp.setChecked(true);
                    SharePreferenceUtils.getInstance(getActivity()).setTempFormat(true);
                }
                Intent i = new Intent();
                i.setAction(NotificationBattery.UPDATE_NOTIFICATION_ENABLE);
                getActivity().sendBroadcast(i);
                break;
            case R.id.lrLanguage:
                SharePreferenceUtils.getInstance(getActivity()).saveLanguageChange(false);
                startActivityForResult(new Intent(getActivity(), LanguageActivity.class), SharePreferenceConstant.LANGUAGE_REQUEST);

                break;
            default:
                break;
        }
    }

    public void intData() {

        swEnableNotification.setChecked(SharePreferenceUtils.getInstance(getActivity()).getNotification());
        swKillApp.setChecked(SharePreferenceUtils.getInstance(getActivity()).getKillApp());
        swBatteryFull.setChecked(SharePreferenceUtils.getInstance(getActivity()).getChargeFullReminder());
        swLowBattery.setChecked(SharePreferenceUtils.getInstance(getActivity()).getLowBatteryReminder());
        swBoost.setChecked(SharePreferenceUtils.getInstance(getActivity()).getBoostReminder());
        swCoolDown.setChecked(SharePreferenceUtils.getInstance(getActivity()).getCoolDownReminder());

        if (SharePreferenceUtils.getInstance(getActivity()).getTempFormat()) {
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
        tvDNDDes.setText(intTimeOff(SharePreferenceUtils.getInstance(getActivity()).getDndStart()) + " - " + intTimeOn(SharePreferenceUtils.getInstance(getActivity()).getDndStop()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SharePreferenceConstant
                    .LANGUAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                break;
        }
    }

}


