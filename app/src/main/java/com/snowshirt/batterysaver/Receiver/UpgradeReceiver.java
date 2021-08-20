package com.snowshirt.batterysaver.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.snowshirt.batterysaver.service.BatteryService;

public class UpgradeReceiver extends BroadcastReceiver {
    Context c;
    @Override
    public void onReceive(Context context, Intent intent) {
        c = context;
        Intent intent2 = new Intent(context, BatteryService.class);
        ContextCompat.startForegroundService(context, intent2);
    }





}