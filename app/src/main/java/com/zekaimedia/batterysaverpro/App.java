package com.zekaimedia.batterysaverpro;

import static androidx.core.internal.view.SupportMenu.CATEGORY_MASK;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;

import androidx.core.internal.view.SupportMenu;
import androidx.multidex.MultiDexApplication;

import com.ads.control.AdsApplication;
import com.zekaimedia.batterysaverpro.Utilsb.Utils;

public class App extends AdsApplication {
    private static App mInStance;

    public static synchronized App getmInStance() {
        return mInStance;
    }

    private static void setmInStance(App mInStance) {
        App.mInStance = mInStance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        setmInStance(this);
        if (Build.VERSION.SDK_INT >= 26) {
            createChanelID(this);
        }
    }

    @TargetApi(26)
    public static void createChanelID(Context mContext) {
        try {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence string = mContext.getString(R.string.app_name);
            String string2 = mContext.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(Utils.notification_channel_id, string, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(string2);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(0xffff0000);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        } catch (Exception e) {
        }
    }
}
