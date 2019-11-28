package com.amt.batterysaver;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.internal.view.SupportMenu;
import androidx.multidex.MultiDexApplication;

import com.facebook.ads.AudienceNetworkAds;
import com.amt.fastcharging.batterysaver.R;

public class App  extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            createChanelID(this);
        }
        AudienceNetworkAds.initialize(this);
        if (AudienceNetworkAds.isInAdsProcess(this)) {


            return;
        }

    }
    @TargetApi(26)
    public static void createChanelID(Context mContext) {
        try {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            String str = "notification_channel_id";
            CharSequence string = mContext.getString(R.string.app_name);
            String string2 = mContext.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(str, string, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(string2);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        } catch (Exception e){}
    }
}
