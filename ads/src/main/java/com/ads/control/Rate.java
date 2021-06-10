package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ads.control.funtion.UtilsApp;

public class Rate {
    private static Rate instance;
    private SharedPreferences.Editor editor;
    private SharedPreferences pre;

    public static Rate getInstance(Context context) {
        if (instance == null) {
            instance = new Rate(context);
        }
        return instance;
    }

    public Rate(Context context) {
        this.pre = context.getSharedPreferences("app_data", Context.MODE_MULTI_PROCESS);
        this.editor = this.pre.edit();
    }

    public boolean show_rate() {
        return this.pre.getBoolean("show_rate", false);
    }

    public void show_rate(boolean value) {
        editor.putBoolean("show_rate", value);
        editor.commit();
    }

    public static void Show(Context mContext, int Style, Activity mActivity) {
        try {
            if (UtilsApp.isConnectionAvailable(mContext)) {
                RateApp a = new RateApp(mContext, mContext.getString(R.string.email_feedback), mContext.getString(R.string.title_email), Style, mActivity);
                a.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                a.show();
            } else {
                ((Activity) (mContext)).finish();
            }

        } catch (Exception e) {
            Log.v("rate", "lỗi");
            e.printStackTrace();
            ((Activity) (mContext)).finish();
        }
    }
}