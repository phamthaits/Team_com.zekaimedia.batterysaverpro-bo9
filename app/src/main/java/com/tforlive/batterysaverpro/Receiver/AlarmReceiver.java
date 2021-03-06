package com.tforlive.batterysaverpro.Receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.core.content.ContextCompat;

import com.tforlive.batterysaverpro.notification.NotificationDevice;
import com.tforlive.batterysaverpro.Alarm.AlarmUtils;
import com.tforlive.batterysaverpro.Utilsb.SharePreferenceUtils;
import com.tforlive.batterysaverpro.Utilsb.Utils;
import com.tforlive.batterysaverpro.service.BatteryService;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(AlarmUtils.ACTION_AUTOSTART_ALARM)) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , "AppName:NAG");
            wl.acquire();
            Bundle extras = intent.getExtras();
            // Kiem tra Server de reboot server
            if (extras != null && extras.getBoolean(AlarmUtils.ACTION_REPEAT_SERVICE, Boolean.FALSE)) {

                if (isMyServiceRunning(BatteryService.class, context)) {
                } else {
                    Intent intent2 = new Intent(context, BatteryService.class);
                    ContextCompat.startForegroundService(context, intent2);
                }
            }

            if (extras != null && extras.getBoolean(AlarmUtils.ACTION_CHECK_DEVICE_STATUS, Boolean.FALSE)) {
                // Pin <15 thi tiet kiem Pin
                // Pin cos sut nhanh khong
                // Cool voi speedbooster pin sut nhanh , neu dung tren 10 phut
                boolean flag = false;
                if (Utils.checkDNDDoing(context) && Utils.checkShouldDoing(context, 0) && Utils.checkShouldDoing(context, 1) && Utils.checkShouldDoing(context, 2)) {

                    if (Utils.getBatteryLevel(context) <= 15) {
                        if (Utils.isScreenOn(context) && Utils.checkShouldDoing(context, 0) && SharePreferenceUtils.getInstance(context).getLowBatteryReminder()) {
                            NotificationDevice.showNotificationLowBattery(context);
                            Utils.intSound(context);
                            flag = true;
                        }
                    } else {
                        if (Utils.isScreenOn(context)) {
                            int random = Utils.getRamdom(2);
                            switch (random) {
                                case 0:
                                    if (Utils.checkMemory(context)
                                            && SharePreferenceUtils.getInstance(context).getBoostReminder()) {
                                        NotificationDevice.showNotificationMemory(context);
                                        Utils.intSound(context);
                                        flag = true;
                                    }
                                    break;
                                case 1:
                                    if (Utils.checkTemp(context) && SharePreferenceUtils.getInstance(context).getCoolDownReminder()) {
                                        NotificationDevice.showNotificationTemp(context);
                                        Utils.intSound(context);
                                        flag = true;
                                    }
                                    break;
                                case 2:
                                    if (!Utils.getChargeStatus(context) && SharePreferenceUtils.getInstance(context).getLowBatteryReminder()) {
                                        int batteryLevelCrese = Utils.getBatteryLevel(context) - SharePreferenceUtils.getInstance(context).getLevelScreenOn();
                                        if (batteryLevelCrese > 1) {
                                            NotificationDevice.showNotificationOptimize(context);
                                            Utils.intSound(context);
                                            flag = true;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    if (!flag) {
                        AlarmUtils.setAlarm(context, AlarmUtils.ACTION_CHECK_DEVICE_STATUS, AlarmUtils.TIME_CHECK_DEVICE_STATUS);
                    }
                }
            }
            wl.release();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

