package com.tforlive.batterysaverpro.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.tforlive.batterysaverpro.MainActivity;
import com.tforlive.batterysaverpro.R;
import com.tforlive.batterysaverpro.Utilsb.SharePreferenceUtils;
import com.tforlive.batterysaverpro.Utilsb.Utils;
import com.tforlive.batterysaverpro.activity.BoostActivity;
import com.tforlive.batterysaverpro.activity.CleanActivity;
import com.tforlive.batterysaverpro.activity.CoolActivity;

import java.util.Arrays;

public class NotificationBattery extends NotificationCompat.Builder {
    public static final int NOTIFYCATION_BATTERY_ID = 1000;
    public static final String UPDATE_NOTIFICATION_ENABLE = "update_notification_enable";
    private int[] iconRes = {
            R.drawable.ic_p0, R.drawable.ic_p1,
            R.drawable.ic_p2, R.drawable.ic_p3,
            R.drawable.ic_p4, R.drawable.ic_p5,
            R.drawable.ic_p6, R.drawable.ic_p7,
            R.drawable.ic_p8, R.drawable.ic_p9,
            R.drawable.ic_p10, R.drawable.ic_p11,
            R.drawable.ic_p12, R.drawable.ic_p13,
            R.drawable.ic_p14, R.drawable.ic_p15,
            R.drawable.ic_p16, R.drawable.ic_p17,
            R.drawable.ic_p18, R.drawable.ic_p19,
            R.drawable.ic_p20, R.drawable.ic_p21,
            R.drawable.ic_p22, R.drawable.ic_p23,
            R.drawable.ic_p24, R.drawable.ic_p25,
            R.drawable.ic_p26, R.drawable.ic_p27,
            R.drawable.ic_p28, R.drawable.ic_p29,
            R.drawable.ic_p30, R.drawable.ic_p31,
            R.drawable.ic_p32, R.drawable.ic_p33,
            R.drawable.ic_p34, R.drawable.ic_p35,
            R.drawable.ic_p36, R.drawable.ic_p37,
            R.drawable.ic_p38, R.drawable.ic_p39,
            R.drawable.ic_p40, R.drawable.ic_p41,
            R.drawable.ic_p42, R.drawable.ic_p43,
            R.drawable.ic_p44, R.drawable.ic_p45,
            R.drawable.ic_p46, R.drawable.ic_p47,
            R.drawable.ic_p48, R.drawable.ic_p49,
            R.drawable.ic_p50, R.drawable.ic_p51,
            R.drawable.ic_p52, R.drawable.ic_p53,
            R.drawable.ic_p54, R.drawable.ic_p55,
            R.drawable.ic_p56, R.drawable.ic_p57,
            R.drawable.ic_p58, R.drawable.ic_p59,
            R.drawable.ic_p60, R.drawable.ic_p61,
            R.drawable.ic_p62, R.drawable.ic_p63,
            R.drawable.ic_p64, R.drawable.ic_p65,
            R.drawable.ic_p66, R.drawable.ic_p67,
            R.drawable.ic_p68, R.drawable.ic_p69,
            R.drawable.ic_p70, R.drawable.ic_p71,
            R.drawable.ic_p72, R.drawable.ic_p73,
            R.drawable.ic_p74, R.drawable.ic_p75,
            R.drawable.ic_p76, R.drawable.ic_p77,
            R.drawable.ic_p78, R.drawable.ic_p79,
            R.drawable.ic_p80, R.drawable.ic_p81,
            R.drawable.ic_p82, R.drawable.ic_p83,
            R.drawable.ic_p84, R.drawable.ic_p85,
            R.drawable.ic_p86, R.drawable.ic_p87,
            R.drawable.ic_p88, R.drawable.ic_p89,
            R.drawable.ic_p90, R.drawable.ic_p91,
            R.drawable.ic_p92, R.drawable.ic_p93,
            R.drawable.ic_p94, R.drawable.ic_p95,
            R.drawable.ic_p96, R.drawable.ic_p97,
            R.drawable.ic_p98, R.drawable.ic_p99,
            R.drawable.ic_p100

    };

    NotificationManager notificationManager;
    private static NotificationBattery notifycationBattery;
    Context mContext;

    public NotificationBattery(@NonNull Context context, @NonNull String channelId) {
        super(context, channelId);
        this.mContext = context.getApplicationContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public static NotificationBattery getInstance(Context context) {
        if (notifycationBattery == null)
            notifycationBattery = new NotificationBattery(context, Utils.notification_channel_id);
        return notifycationBattery;
    }

    public void updateNotify(int lv, int temp, int hourleft, int minleft, boolean ischarging) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("LOCATION_SHORTCUT");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        Intent iCleaner = new Intent(mContext, CleanActivity.class);
        iCleaner.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        iCleaner.setAction("LOCATION_SHORTCUT");

        Intent iOptimize = new Intent(mContext, BoostActivity.class);
        iOptimize.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        iOptimize.setAction("LOCATION_SHORTCUT");
        PendingIntent pOptipimize = PendingIntent.getActivity(mContext, 0, iOptimize, 0);

        Intent iCooler = new Intent(mContext, CoolActivity.class);
        iCooler.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        iCooler.setAction("LOCATION_SHORTCUT");

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification);
        int currentNightMode = mContext.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                Log.d("Test", "UI_MODE_NIGHT_NO");
                remoteViews.setTextColor(R.id.tvBattery, mContext.getResources().getColor(R.color.primary_light));
                remoteViews.setTextColor(R.id.des_noti, mContext.getResources().getColor(R.color.secondary_light));
                remoteViews.setTextColor(R.id.tv_status, mContext.getResources().getColor(R.color.secondary_light));
                remoteViews.setTextColor(R.id.tvHour, mContext.getResources().getColor(R.color.secondary_light));
                remoteViews.setTextColor(R.id.tv_time_h_unit, mContext.getResources().getColor(R.color.secondary_light));
                remoteViews.setTextColor(R.id.tvMin, mContext.getResources().getColor(R.color.secondary_light));
                remoteViews.setTextColor(R.id.tv_time_m_unit, mContext.getResources().getColor(R.color.secondary_light));
                remoteViews.setImageViewResource(R.id.line_noti, R.drawable.img_noti_line_light);
                remoteViews.setImageViewResource(R.id.ic_noti_time, R.drawable.ic_noti_time_light);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                remoteViews.setTextColor(R.id.tvBattery, mContext.getResources().getColor(R.color.primary_dark));
                remoteViews.setTextColor(R.id.des_noti, mContext.getResources().getColor(R.color.secondary_dark));
                remoteViews.setTextColor(R.id.tv_status, mContext.getResources().getColor(R.color.secondary_dark));
                remoteViews.setTextColor(R.id.tv_status, mContext.getResources().getColor(R.color.primary_dark));
                remoteViews.setTextColor(R.id.tvHour, mContext.getResources().getColor(R.color.primary_dark));
                remoteViews.setTextColor(R.id.tv_time_h_unit, mContext.getResources().getColor(R.color.primary_dark));
                remoteViews.setTextColor(R.id.tvMin, mContext.getResources().getColor(R.color.primary_dark));
                remoteViews.setTextColor(R.id.tv_time_m_unit, mContext.getResources().getColor(R.color.primary_dark));
                remoteViews.setImageViewResource(R.id.line_noti, R.drawable.img_noti_line_dark);
                remoteViews.setImageViewResource(R.id.ic_noti_time, R.drawable.ic_noti_time_dark);
                break;
        }

//        remoteViews.setOnClickPendingIntent(R.id.img_clean, pOptipimize);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, "NOTIFYCATION_BATTERY_ID");
        remoteViews.setTextViewText(R.id.tvBattery, lv + "%");
        if (lv == 100) {
            remoteViews.setImageViewResource(R.id.img_battery, R.drawable.ic_noti_battery);
            remoteViews.setTextViewText(R.id.des_noti, mContext.getString(R.string.des_noti_100));
            notificationBuilder.setContentIntent(pendingIntent);
        }
        if (lv < 20) {
            remoteViews.setImageViewResource(R.id.img_battery, R.drawable.ic_noti_low);
            remoteViews.setTextViewText(R.id.des_noti, mContext.getString(R.string.des_noti_20));
            notificationBuilder.setContentIntent(pOptipimize);
        }
        if (lv >= 20 & lv < 100) {
            remoteViews.setImageViewResource(R.id.img_battery, R.drawable.ic_noti_boost);
            remoteViews.setTextViewText(R.id.des_noti, mContext.getString(R.string.des_noti));
            notificationBuilder.setContentIntent(pOptipimize);
        }
        //temperature
        remoteViews.setTextViewText(R.id.tvTemp, getTemp(temp));
        remoteViews.setTextViewText(R.id.tvHour, String.format("%02d", hourleft));
        remoteViews.setTextViewText(R.id.tvMin, String.format("%02d", minleft));
        remoteViews.setTextViewText(R.id.tv_status, getStatusTime(ischarging));

        if (ischarging) {
            if (Utils.getChargeFull(mContext)) {
                remoteViews.setViewVisibility(R.id.view_time_left, View.GONE);
            }
        } else {
            remoteViews.setViewVisibility(R.id.view_time_left, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.tvFullCharge, View.GONE);
        }

        ShortcutManager shortcutManager;
        if (android.os.Build.VERSION.SDK_INT >= 25) {
            shortcutManager = mContext.getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut_junk_clean, shortcut_boost, shortcut_cool;
            if (android.os.Build.VERSION.SDK_INT >= 25) {

                shortcut_junk_clean = new ShortcutInfo.Builder(mContext, "shortcut_junk_clean")
                        .setShortLabel(mContext.getString(R.string.junk_clean_nav))
                        .setLongLabel(mContext.getString(R.string.junk_clean_nav))
                        .setIcon(Icon.createWithResource(mContext, R.drawable.ic_clean_trash_info))
                        .setIntent(iCleaner)
                        .build();
                shortcut_boost = new ShortcutInfo.Builder(mContext, "shortcut_boost")
                        .setShortLabel(mContext.getString(R.string.phone_boost_nav))
                        .setLongLabel(mContext.getString(R.string.phone_boost_nav))
                        .setIcon(Icon.createWithResource(mContext, R.drawable.ic_memory_boost_info))
                        .setIntent(iOptimize)
                        .build();
                shortcut_cool = new ShortcutInfo.Builder(mContext, "shortcut_cool")
                        .setShortLabel(mContext.getString(R.string.phone_cool_nav))
                        .setLongLabel(mContext.getString(R.string.phone_cool_nav))
                        .setIcon(Icon.createWithResource(mContext, R.drawable.ic_temperature_info))
                        .setIntent(iCooler)
                        .build();
                shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut_junk_clean, shortcut_boost, shortcut_cool));
            }
        }

//            if (android.os.Build.VERSION.SDK_INT < 25) {
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, "NOTIFYCATION_BATTERY_ID");
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        notificationBuilder.setSmallIcon(iconRes[lv]);
        notificationBuilder.setTicker(null);
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setContentTitle(mContext.getString(R.string.battery_level));
        notificationBuilder.setContentText(null);
        notificationBuilder.setDefaults(0);
//                    if (Build.VERSION.SDK_INT >= 23) {
        notificationBuilder.setChannelId(Utils.notification_channel_id);
//        }
        notificationBuilder.setCustomContentView(remoteViews);
        Notification notification = notificationBuilder.build();
        this.notificationManager.notify(NOTIFYCATION_BATTERY_ID, notification);
//            }

    }

    private String getStatusTime(boolean isCharging) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isCharging) {
            stringBuilder.append(mContext.getResources().getString(R.string.noti_battery_charging_timer_left));
        } else {
            stringBuilder.append(mContext.getResources().getString(R.string.time_left));
        }

        return stringBuilder.toString();
    }

    private String convertTime(int hour, int min) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" ");
        stringBuilder.append(hour);
        stringBuilder.append("h");
        stringBuilder.append(min);
        stringBuilder.append("m");

        return stringBuilder.toString();
    }

    public String getTemp(int i) {
        if (!SharePreferenceUtils.getInstance(mContext).getTempFormat()) {
            double b = Math.ceil(((i / 10f) * 9 / 5 + 32) * 100) / 100;
            String r = String.valueOf(b);
            return (r + mContext.getString(R.string.fahrenheit));
        } else {

            String str = Double.toString(Math.ceil((i / 10f) * 100) / 100);
            return (str + mContext.getString(R.string.celsius));
        }
    }

    public String getTempF() {
        return null;
    }

    public void cancel() {
        notificationManager.cancel(NOTIFYCATION_BATTERY_ID);
    }

}


