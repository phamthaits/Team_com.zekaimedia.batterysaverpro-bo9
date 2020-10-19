package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ads.control.funtion.UtilsApp;

public class Rate {
    public static void Show(Context mContext,int Style,Activity mActivity){
        try {
            Log.v("Thaidaica","Đã vào rate");
            if(UtilsApp.isConnectionAvailable(mContext)){
                if(!PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("Show_rate",false)){
                    RateApp a = new RateApp(mContext,mContext.getString(R.string.email_feedback),mContext.getString(R.string.title_email),Style,mActivity);
                    a.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                    a.show();
                }else  ((Activity)(mContext)).finish();
            }else{
                ((Activity)(mContext)).finish();
            }

        }catch (Exception e){
            Log.v("Thaidaica","lỗi");
            e.printStackTrace();
            ((Activity)(mContext)).finish();
        }
    }
}
