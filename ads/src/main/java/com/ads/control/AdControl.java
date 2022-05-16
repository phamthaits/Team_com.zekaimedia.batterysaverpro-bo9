package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class AdControl {
    private static AdControl instance;
    private SharedPreferences.Editor editor;
    private SharedPreferences pre;
//        public static boolean _isTestAds = false;
    public static boolean _isTestAds = true;
    private static int test_rate = 50;
    public static Boolean _isShowOpenAds = true;

    public AdControl(Context context) {
        this.pre = context.getSharedPreferences("app_data", Context.MODE_MULTI_PROCESS);
        this.editor = this.pre.edit();
    }

    public static AdControl getInstance(Activity value) {
        if (instance == null) {
            instance = new AdControl(value);
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

    public boolean is_reload_firebase() {
//        return true;
        if (_isTestAds || forceUpdateFirebase()) return true;
        return old_date() != Calendar.getInstance().get(Calendar.DAY_OF_MONTH) || !isInit();
    }

    //Full
    public String admob_full() {
        if (_isTestAds) return "/6499/example/interstitial";
        return this.pre.getString("admob_full", "");
    }

    public void admob_full(String value) {
        editor.putString("admob_full", value);
        editor.commit();
    }

    //Full Rate
    public int admob_full_rate() {
        if (_isTestAds) return test_rate;
        return this.pre.getInt("admob_full_rate", 0);
    }

    public void admob_full_rate(int value) {
        editor.putInt("admob_full_rate", value);
        editor.commit();
    }

    //Open
    public String admob_open() {
        if (_isTestAds) return "ca-app-pub-3940256099942544/3419835294";
        return this.pre.getString("admob_open", "");
    }

    public void admob_open(String value) {
        editor.putString("admob_open", value);
        editor.commit();
    }

    //Open Rate
    public int admob_open_rate() {
        if (_isTestAds) return test_rate;
        return this.pre.getInt("admob_open_rate", 0);
    }

    public void admob_open_rate(int value) {
        editor.putInt("admob_open_rate", value);
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

    //Native Main Ratte
    public int admob_native_main_rate() {
        if (_isTestAds) return test_rate;
        return this.pre.getInt("admob_native_main_rate", 0);
    }

    public void admob_native_main_rate(int value) {
        editor.putInt("admob_native_main_rate", value);
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

    //Native Setting Rate
    public int admob_native_setting_rate() {
        if (_isTestAds) return test_rate;
        return this.pre.getInt("admob_native_setting_rate", 0);
    }

    public void admob_native_setting_rate(int value) {
        editor.putInt("admob_native_setting_rate", value);
        editor.commit();
    }

    //Native result
    public String admob_native_result() {
        if (_isTestAds) return "/6499/example/native";
        return this.pre.getString("admob_native_result", "");
    }

    public void admob_native_result(String value) {
        editor.putString("admob_native_result", value);
        editor.commit();
    }

    //Native result
    public int admob_native_result_rate() {
        if (_isTestAds) return test_rate;
        return this.pre.getInt("admob_native_result_rate", 0);
    }

    public void admob_native_result_rate(int value) {
        editor.putInt("admob_native_result_rate", value);
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

    //Native Banner
    public int admob_native_banner_rate() {
        if (_isTestAds) return test_rate;
        return this.pre.getInt("admob_native_banner_rate", 0);
    }

    public void admob_native_banner_rate(int value) {
        editor.putInt("admob_native_banner_rate", value);
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

    //Banner Rate
    public int admob_banner_rate() {
        if (_isTestAds) return test_rate;
        return this.pre.getInt("admob_banner_rate", 0);
    }

    public void admob_banner_rate(int value) {
        editor.putInt("admob_banner_rate", value);
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
//        return true;
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
    public NativeBundle native_result;

    private void SetNative() {
        native_main = new NativeBundle(R.layout.item_admob_native_setting, R.layout.load_native_setting, false,
                admob_native_main(), false, admob_native_main_rate());
        native_banner_home = new NativeBundle(R.layout.item_admob_banner_native, R.layout.load_native_banner, true,
                admob_native_banner(), false, admob_native_banner_rate());
        native_setting = new NativeBundle(R.layout.item_admob_native_setting, R.layout.load_native_setting, false,
                admob_native_setting(), false, admob_native_setting_rate());
        native_result = new NativeBundle(R.layout.item_admob_native_setting, R.layout.load_native_setting, false,
                admob_native_result(), false, admob_native_result_rate());
    }

    public class NativeBundle {
        public int admob_layout_resource;
        public boolean is_native_banner;
        public String admob_ads;
        public int loading_layout_resource;
        public boolean isAnimationButton;
        public int rate;

        public NativeBundle(int admob_layout_resource, int loading_layout_resource, boolean is_native_banner, String admob_ads, boolean isAnimationButton, int rate) {
            this.admob_layout_resource = admob_layout_resource;
            this.is_native_banner = is_native_banner;
            this.admob_ads = admob_ads;
            this.loading_layout_resource = loading_layout_resource;
            this.isAnimationButton = isAnimationButton;
            this.rate = rate;
        }
    }
}