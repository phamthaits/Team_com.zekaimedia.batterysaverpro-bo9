package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;

import com.ads.control.funtion.UtilsApp;

public class Rate {
    public static void Show(Context mContext,int Style,Activity mActivity, String admob_native,String admob_full){
        try {
            if(UtilsApp.isConnectionAvailable(mContext)){
                if(!PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("Show_rate",false)){
                    RateApp a = new RateApp(mContext,mContext.getString(R.string.email_feedback),mContext.getString(R.string.title_email),Style,mActivity,admob_native,admob_full);
                    a.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                    a.show();
                }else  ((Activity)(mContext)).finish();
            }else{
                ((Activity)(mContext)).finish();
            }

        }catch (Exception e){
            e.printStackTrace();
            ((Activity)(mContext)).finish();
        }

    }
}
