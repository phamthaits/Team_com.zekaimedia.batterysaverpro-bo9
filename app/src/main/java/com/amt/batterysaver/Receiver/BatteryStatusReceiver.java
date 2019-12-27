package com.amt.batterysaver.Receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.amt.batterysaver.BatteryMode.BatteryInfo;
import com.amt.batterysaver.Utilsb.BatteryPref;
import com.amt.batterysaver.Utilsb.HistoryPref;
import com.amt.batterysaver.Utilsb.SharePreferenceConstant;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.notification.NotificationBattery;
import com.amt.batterysaver.service.BatteryService;


public class BatteryStatusReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryStatusReceiver";
    BatteryInfo mBatteryInfo = new BatteryInfo();
    Context mContext = null;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
//    private WifiManager wifiManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {

            mBatteryInfo.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mBatteryInfo.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            mBatteryInfo.temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            mBatteryInfo.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            mBatteryInfo.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
//            if(SharePreferenceUtils.getInstance(context).getLevelIn()==0){
//                Utils.intPowerConnected(context);
//            }

//            if (mBatteryInfo.status == BatteryManager.BATTERY_STATUS_FULL) {
//                Utils.intSound(context);
//                if () {
//                Utils.fullPower(context);
//                }
//            }
            //charging
            mBatteryInfo.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            //Day pin
            if (mBatteryInfo.status == BatteryManager.BATTERY_STATUS_FULL)
                Utils.fullPower(context);

            boolean isCharging = mBatteryInfo.status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    mBatteryInfo.status == BatteryManager.BATTERY_STATUS_FULL;
            if (isCharging) {
                mBatteryInfo.plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                boolean usbCharge = mBatteryInfo.plugged == BatteryManager.BATTERY_PLUGGED_AC;
                int time;
                if (usbCharge) {
                    time = BatteryPref.initilaze(context).getTimeChargingAc(mContext, mBatteryInfo.level);

                } else {
                    time = BatteryPref.initilaze(context).getTimeChargingUsb(mContext, mBatteryInfo.level);
                }
                mBatteryInfo.hourleft = time / 60;
                mBatteryInfo.minleft = time % 60;
            } else {
                int time = BatteryPref.initilaze(context).getTimeRemainning(mContext, mBatteryInfo.level);
                mBatteryInfo.hourleft = time / 60;
                mBatteryInfo.minleft = time % 60;
            }
            intent.setAction(BatteryService.ACTION_MAX_BATTERY_CHANGED_SEND);
            intent.putExtra(BatteryInfo.BATTERY_INFO_KEY, mBatteryInfo);
            mContext.sendBroadcast(intent);
            if (SharePreferenceUtils.getInstance(mContext).getNotification()) {
                NotificationBattery.getInstance(mContext).updateNotify(mBatteryInfo.level, mBatteryInfo.temperature, mBatteryInfo.hourleft, mBatteryInfo.minleft, isCharging);
            }
        } else if (action.equals(BatteryService.ACTION_MAX_BATTERY_NEED_UPDATE)) {
            intent.setAction(BatteryService.ACTION_MAX_BATTERY_CHANGED_SEND);
            intent.putExtra(BatteryInfo.BATTERY_INFO_KEY, mBatteryInfo);
            mContext.sendBroadcast(intent);
        } else if (action.equals(NotificationBattery.UPDATE_NOTIFICATION_ENABLE)) {

            if (SharePreferenceUtils.getInstance(mContext).getNotification()) {
                boolean isCharging = mBatteryInfo.status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        mBatteryInfo.status == BatteryManager.BATTERY_STATUS_FULL;
                NotificationBattery.getInstance(mContext).updateNotify(mBatteryInfo.level, mBatteryInfo.temperature, mBatteryInfo.hourleft, mBatteryInfo.minleft, isCharging);
            }
        } else if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
            Utils.intPowerConnected(context);

        } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            SharePreferenceConstant.full_battery_loaded = false;
            Utils.powerDisconnected(context);
        }
//        else {
//            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION) || intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
//                Utils.wifiBooster(context,wifiManager);
//            }
//        }
        HistoryPref.putLevel(context, mBatteryInfo.level);
    }

    public String formatHourMinutune(long totaltime) {
        long seconds = (totaltime / 1000) % 60;
        long minutes = (totaltime / (1000 * 60)) % 60;
        long hours = totaltime / (1000 * 60 * 60);

        StringBuilder b = new StringBuilder();
        b.append(hours == 0 ? "00" : hours < 10 ? "0" + hours :
                String.valueOf(hours));
        b.append(":");
        b.append(minutes == 0 ? "00" : minutes < 10 ? "0" + minutes :
                String.valueOf(minutes));
        return b.toString();
    }

    public static String formatHoursAndMinutes(long totalMinutes) {
        String minutes = Long.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        return (totalMinutes / 60) + ":" + minutes;
    }

    public final void OnCreate(Context context) {
        mContext = context.getApplicationContext();
//        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(BatteryService.ACTION_MAX_BATTERY_NEED_UPDATE);
        intentFilter.addAction(NotificationBattery.UPDATE_NOTIFICATION_ENABLE);
//        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        getActivity().registerReceiver(DeviceChangeReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        mContext.registerReceiver(this, intentFilter);
    }

    public final void OnDestroy(Context context) {
        if (context != null) {
            context.unregisterReceiver(this);
        }
    }
}

