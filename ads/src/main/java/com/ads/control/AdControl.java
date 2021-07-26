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
        instance.SetNative();
        return instance;
    }

    public boolean isInit() {
        return this.pre.getBoolean("isInit", false);
    }

    public void isInit(boolean value) {
        editor.putBoolean("isInit", value);
        editor.commit();
    }

    //Full
    public String admob_full() {
        if (_isTestAds) return "ca-app-pub-3940256099942544/1033173712";
        return this.pre.getString("admob_full", "");
    }

    public void admob_full(String value) {
        editor.putString("admob_full", value);
        editor.commit();
    }

    //Native Main
    public String admob_native_main() {
        if (_isTestAds) return "/6499/example/native";
        return this.pre.getString("admob_native_main", "");
    }

    public void admob_native_main(String value) {
        editor.putString("admob_native_main", value);
        editor.commit();
    }


    //Native Setting
    public String admob_native_setting() {
        if (_isTestAds) return "/6499/example/native";
        return this.pre.getString("admob_native_setting", "");
    }

    public void admob_native_setting(String value) {
        editor.putString("admob_native_setting", value);
        editor.commit();
    }

    //Native Banner
    public String admob_native_banner() {
        if (_isTestAds) return "/6499/example/native";
        return this.pre.getString("admob_native_banner", "");
    }

    public void admob_native_banner(String value) {
        editor.putString("admob_native_banner", value);
        editor.commit();
    }

    //Banner
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

    public String version_update_url() {
        return this.pre.getString("version_update_url", "");
    }

    public void version_update_url(String value) {
        editor.putString("version_update_url", value);
        editor.commit();
    }

    public Boolean remove_ads() {
//        return false;
        return this.pre.getBoolean("remove_ads", false);
    }

    public void remove_ads(Boolean value) {
        editor.putBoolean("remove_ads", value);
        editor.commit();
    }

    public Boolean forceUpdateFirebase() {
        return this.pre.getBoolean("forceUpdateFirebase", false);
    }

    public void forceUpdateFirebase(Boolean value) {
        editor.putBoolean("forceUpdateFirebase", value);
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

    public NativeBundle native_main;
    public NativeBundle native_banner_home;
    public NativeBundle native_setting;

    private void SetNative() {
        native_main = new NativeBundle(R.layout.item_admob_native_ad, false,
                admob_native_main(), false);
        native_banner_home = new NativeBundle(R.layout.item_admob_banner_native, true,
                admob_native_banner(), false);
        native_setting = new NativeBundle(R.layout.item_admob_native_setting, false,
                admob_native_setting(), false);
    }

    public class NativeBundle {
        public int admob_layout_resource;
        public boolean is_native_banner;
        public String admob_ads;
        public boolean isAnimationButton;

        public NativeBundle(int admob_layout_resource, boolean is_native_banner, String admob_ads, boolean isAnimationButton) {
            this.admob_layout_resource = admob_layout_resource;
            this.is_native_banner = is_native_banner;
            this.admob_ads = admob_ads;
            this.isAnimationButton = isAnimationButton;
        }
    }
}