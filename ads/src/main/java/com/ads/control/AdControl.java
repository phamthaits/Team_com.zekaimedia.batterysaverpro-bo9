package com.ads.control;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class AdControl {
    private static AdControl instance;
    private SharedPreferences.Editor editor;
    private SharedPreferences pre;
        public static boolean _isTestAds = false;
//    public static boolean _isTestAds = true;

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

    public long limit_showads() {
        return this.pre.getLong("limit_showads", 0);
    }

    public void limit_showads(int value) {
        editor.putLong("limit_showads", value * 60 * 1000);
        editor.commit();
    }

    public String admob_full() {
        if (_isTestAds) return "ca-app-pub-3940256099942544/1033173712";
        return this.pre.getString("admob_full", "");
    }

    public void admob_full(String value) {
        editor.putString("admob_full", value);
        editor.commit();
    }

    public String admob_native() {
        if (_isTestAds) return "/6499/example/native";
        return this.pre.getString("admob_native", "");
    }

    public void admob_native(String value) {
        editor.putString("admob_native", value);
        editor.commit();
    }

    public String admob_native_setting() {
        if (_isTestAds) return "/6499/example/native";
        return this.pre.getString("admob_native_setting", "");
    }

    public void admob_native_setting(String value) {
        editor.putString("admob_native_setting", value);
        editor.commit();
    }

    public String admob_native_banner() {
        if (_isTestAds) return "/6499/example/native";
        return this.pre.getString("admob_native_banner", "");
    }

    public void admob_native_main(String value) {
        editor.putString("admob_native_main", value);
        editor.commit();
    }

    public String admob_native_main() {
        if (_isTestAds) return "/6499/example/native";
        return this.pre.getString("admob_native_main", "");
    }

    public void admob_native_banner(String value) {
        editor.putString("admob_native_banner", value);
        editor.commit();
    }

    public String admob_native_rate_app() {
        if (_isTestAds) return "/6499/example/native";
        return this.pre.getString("admob_native_rate_app", "");
    }

    public void admob_native_rate_app(String value) {
        editor.putString("admob_native_rate_app", value);
        editor.commit();
    }

    public String admob_banner() {
        if (_isTestAds) return "/6499/example/banner";
        return this.pre.getString("admob_banner", "");
    }

    public void admob_banner(String value) {
        editor.putString("admob_banner", value);
        editor.commit();
    }

    public boolean isUpdate() {
        return this.pre.getBoolean("isUpdate", false);
    }

    public void isUpdate(int value) {
        editor.putBoolean("isUpdate", value != version);
        editor.commit();
    }

    public Boolean remove_ads() {
        if (_isTestAds) return false;
        return this.pre.getBoolean("remove_ads", false);
    }

    public void remove_ads(Boolean value) {
        editor.putBoolean("remove_ads", value);
        editor.commit();
    }

    public boolean isStillShowAds = true;
    private int version = 1;

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

    public long getLastTimeShowAds() {
        return this.pre.getLong("lastTimeShowAds", 0);
    }

    public void setLastTimeShowAds() {
        editor.putLong("lastTimeShowAds", System.currentTimeMillis());
        editor.commit();
    }
}