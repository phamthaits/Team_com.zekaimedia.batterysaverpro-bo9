package com.ads.control;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Calendar;

public class AdControl {
    private static AdControl instance;
    private SharedPreferences.Editor editor;
    private SharedPreferences pre;

    public AdControl(Context context) {
        this.pre = context.getSharedPreferences("app_data", Context.MODE_MULTI_PROCESS);
        this.editor = this.pre.edit();
    }

    public static AdControl getInstance(Context context) {
        if (instance == null) {
            instance = new AdControl(context);
        }
        return instance;
    }

    public boolean isInit() {
        return this.pre.getBoolean("isInit", false);
    }

    public void isInit(boolean value) {
        editor.putBoolean("isInit", value);
        editor.commit();
    }
    public String admob_full() {
        return this.pre.getString("admob_full", "");
    }

    public void admob_full(String value) {
        editor.putString("admob_full", value);
        editor.commit();
    }
    public int old_date() {
        return this.pre.getInt("old_date", -1);
    }

    public void old_date(int value) {
        if (value == -1) {
            value = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        editor.putInt("old_date", value);
        editor.commit();
    }

    public String admob_native() {
        return this.pre.getString("admob_native", "");
    }

    public void admob_native(String value) {
        editor.putString("admob_native", value);
        editor.commit();
    }

    public String admob_banner() {
        return this.pre.getString("admob_banner", "");
    }

    public void admob_banner(String value) {
        editor.putString("admob_banner", value);
        editor.commit();
    }

    public Boolean remove_ads() {
        return this.pre.getBoolean("bg_remove_ads", false);
    }

    public void remove_ads(Boolean value) {
        editor.putBoolean("bg_remove_ads", value);
        editor.commit();
    }
}