package com.newus.batteryfastcharge.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import android.os.Handler;
import android.widget.RemoteViews;

import com.newus.batteryfastcharge.MainActivity;
import com.newus.batteryfastcharge.R;
import com.newus.batteryfastcharge.Utilsb.SharePreferenceUtils;
import com.newus.batteryfastcharge.Utilsb.Utils;
import com.newus.batteryfastcharge.activity.CleanActivity;
import com.newus.batteryfastcharge.activity.CoolActivity;
import com.newus.batteryfastcharge.activity.BatterySaverActivity;
import com.newus.batteryfastcharge.activity.BoostActivity;

public class NotificationDevice {
    public final static int ID_NOTIFICATTION_BOOST = 2;
    public final static int ID_NOTIFICATTION_CLEAN_JUNK = 3;
    public final static int ID_NOTIFICATTION_OPTIMIZE = 4;
    public final static int ID_NOTIFICATTION_COOLER = 5;
    public final static int ID_NOTIFICATTION_FULL_BATTERY = 6;
    public final static int ID_NOTIFICATTION_LOW_BATTERY = 7;

    public static void showNotificationTemp(Context mContext) {
        try {
            Intent intent = new Intent(mContext, CoolActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent activity = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_cool_down);
            if (Utils.getRamdom(2) == 0) {
                remoteViews.setTextViewText(R.id.tvNotiTitle, mContext.getString(R.string.cool_down_title_2) + " " + getTemp(mContext, Utils.getTempleCpu(mContext)));
            } else {
                remoteViews.setTextViewText(R.id.tvNotiTitle, mContext.getString(R.string.cool_down_title));
            }
            remoteViews.setTextViewText(R.id.tvNotiTitleDes, mContext.getString(R.string.cool_down_des));
            remoteViews.setTextViewText(R.id.btnAction, mContext.getString(R.string.cool_down_button));


            NotificationCompat.Builder ongoing = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_noti_small_temp)
                    .setOngoing(true)
                    .setVibrate(new long[]{0L})
                    .setOnlyAlertOnce(true)
                    .setContentIntent(activity)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCustomContentView(remoteViews);
            if (Utils.isAndroid26()) {

                ongoing.setChannelId("notification_channel_id");
            }
            Notification noti = ongoing.build();
            noti.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(ID_NOTIFICATTION_COOLER, noti);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mNotificationManager.cancel(ID_NOTIFICATTION_COOLER);
                }
            };
            Handler  mHandler = new Handler();
            mHandler.postDelayed(runnable,10000);
        } catch (Exception e) {
        }
    }

    public static String getTemp(Context c, int i) {
        if (!SharePreferenceUtils.getInstance(c).getTempFormat()) {
            double b = Math.ceil(((i / 10f) * 9 / 5 + 32) * 100) / 100;
            String r = String.valueOf(b);
            return (r + c.getString(R.string.fahrenheit));
        } else {
            String str = Double.toString(Math.ceil((i / 10f) * 100) / 100);
            return (str + c.getString(R.string.celsius));
        }
    }

    public static void showNotificationMemory(Context mContext) {
        try {
            Intent intent = new Intent(mContext, BoostActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent activity = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.boost_notification);
            if (Utils.getRamdom(2) == 0) {
                remoteViews.setTextViewText(R.id.tvNotiTitle, mContext.getString(R.string.boost_title));
            } else {
                remoteViews.setTextViewText(R.id.tvNotiTitle, mContext.getString(R.string.boost_title2));
            }
            if (Utils.getRamdom(2) == 0) {
                remoteViews.setTextViewText(R.id.tvNotiTitleDes, mContext.getString(R.string.boost_des));
            } else {
                remoteViews.setTextViewText(R.id.tvNotiTitleDes, mContext.getString(R.string.boost_des_2));
            }
            remoteViews.setTextViewText(R.id.btnAction, mContext.getString(R.string.boost_action));

            NotificationCompat.Builder ongoing = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_noti_small_low_boost)
                    .setOngoing(true)
                    .setVibrate(new long[]{0L})
                    .setOnlyAlertOnce(true)
                    .setContentIntent(activity)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCustomContentView(remoteViews);
            if (Utils.isAndroid26()) {
                ongoing.setChannelId("notification_channel_id");
            }
            Notification noti = ongoing.build();
            noti.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(ID_NOTIFICATTION_BOOST, noti);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mNotificationManager.cancel(ID_NOTIFICATTION_BOOST);
                }
            };
            Handler  mHandler = new Handler();
            mHandler.postDelayed(runnable,10000);
        } catch (Exception e) {
        }
    }

    public static void showNotificationLowBattery(Context mContext) {
        try {
            Intent intent = new Intent(mContext, BatterySaverActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent activity = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_low);
            if (Utils.getRamdom(2) == 0) {
                remoteViews.setTextViewText(R.id.tvNotiTitle, mContext.getString(R.string.low_battery));
            } else {
                remoteViews.setTextViewText(R.id.tvNotiTitle, mContext.getString(R.string.low_battery));
            }
            if (Utils.getRamdom(2) == 0) {
                remoteViews.setTextViewText(R.id.tvNotiTitleDes, mContext.getString(R.string.low_battery_des));
            } else {
                remoteViews.setTextViewText(R.id.tvNotiTitleDes, mContext.getString(R.string.low_battery_des));
            }
            remoteViews.setTextViewText(R.id.btnAction, mContext.getString(R.string.optimize));

            NotificationCompat.Builder ongoing = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_noti_small_low_battery)
                    .setOngoing(true)
                    .setVibrate(new long[]{0L})
                    .setOnlyAlertOnce(true)
                    .setContentIntent(activity)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCustomContentView(remoteViews);
            if (Utils.isAndroid26()) {

                ongoing.setChannelId("notification_channel_id");
            }
            Notification noti = ongoing.build();
            noti.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(ID_NOTIFICATTION_LOW_BATTERY, noti);


            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mNotificationManager.cancel(ID_NOTIFICATTION_LOW_BATTERY);
                }
            };
            Handler  mHandler = new Handler();
            mHandler.postDelayed(runnable,10000);
        } catch (Exception e) {
        }
    }

    public static void showNotificationOptimize(Context mContext) {
        try {
            Intent intent = new Intent(mContext, BatterySaverActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent activity = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_optimize);
            if (Utils.getRamdom(2) == 0) {
                remoteViews.setTextViewText(R.id.tvNotiTitle, mContext.getString(R.string.low_power_30));
            } else {
                remoteViews.setTextViewText(R.id.tvNotiTitle, mContext.getString(R.string.low_power_30));
            }
            if (Utils.getRamdom(2) == 0) {
                remoteViews.setTextViewText(R.id.tvNotiTitleDes, mContext.getString(R.string.low_battery_des));
            } else {
                remoteViews.setTextViewText(R.id.tvNotiTitleDes, mContext.getString(R.string.low_battery_des));
            }
            if (Utils.getRamdom(2) == 0) {
                remoteViews.setTextViewText(R.id.btnAction, mContext.getString(R.string.saving_now));

            } else {
                remoteViews.setTextViewText(R.id.btnAction, mContext.getString(R.string.optimize));
            }

            NotificationCompat.Builder ongoing = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_noti_small_low_battery)
                    .setOngoing(true)
                    .setVibrate(new long[]{0L})
                    .setOnlyAlertOnce(true)
                    .setContentIntent(activity)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCustomContentView(remoteViews);
            if (Utils.isAndroid26()) {

                ongoing.setChannelId("notification_channel_id");
            }
            Notification noti = ongoing.build();
            noti.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(ID_NOTIFICATTION_OPTIMIZE, noti);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mNotificationManager.cancel(ID_NOTIFICATTION_OPTIMIZE);
                }
            };
            Handler  mHandler = new Handler();
            mHandler.postDelayed(runnable,10000);
        } catch (Exception e) {
        }
    }

    public static void cancle(Context mContext, int ID) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ID);
    }

    public static void showNotificationJunk(Context mContext) {
        try {
            Intent intent = new Intent(mContext, CleanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent activity = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.fragment_clean);
            NotificationCompat.Builder ongoing = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setContentIntent(activity)
                    .setAutoCancel(true)
                    .setCustomContentView(remoteViews);
            if (Utils.isAndroid26()) {
                ongoing.setPriority(NotificationManager.IMPORTANCE_HIGH);
                ongoing.setChannelId("notification_channel_id");
            } else {
                ongoing.setPriority(Notification.PRIORITY_MAX);
            }
            Notification noti = ongoing.build();
            noti.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(ID_NOTIFICATTION_CLEAN_JUNK, noti);
        } catch (Exception e) {
        }
    }

    public static void showNotificationBatteryFull(Context mContext) {
        try {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent activity = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_battery_full);
            NotificationCompat.Builder ongoing = new NotificationCompat.Builder(mContext,"notification_channel_id")
                    .setSmallIcon(R.drawable.ic_noti_small_full)
                    .setOngoing(true)
                    .setVibrate(new long[]{0L})
                    .setOnlyAlertOnce(true)
                    .setContentIntent(activity)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCustomContentView(remoteViews);

            if (Utils.isAndroid26()) {
                ongoing.setChannelId("notification_channel_id");
            }
            Notification noti = ongoing.build();
            noti.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(ID_NOTIFICATTION_FULL_BATTERY, noti);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mNotificationManager.cancel(ID_NOTIFICATTION_FULL_BATTERY);
                }
            };
            Handler mHandler = new Handler();
            mHandler.postDelayed(runnable, 6000);

        } catch (Exception e) {
        }
    }
}