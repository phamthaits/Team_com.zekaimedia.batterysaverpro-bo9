package com.ads.control;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Random;

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

    public long limit_showads() {
        return this.pre.getLong("limit_showads", 0);
    }

    public void limit_showads(int value) {
        editor.putLong("limit_showads", value * 60 * 1000);
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

    public String fb_full() {
        return this.pre.getString("fb_full", "");
    }

    public void fb_full(String value) {
        editor.putString("fb_full", value);
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

    public String fb_native_main() {
        return this.pre.getString("fb_native_main", "");
    }

    public void fb_native_main(String value) {
        editor.putString("fb_native_main", value);
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
    public String fb_native_setting() {
        return this.pre.getString("fb_native_setting", "");
    }

    public void fb_native_setting(String value) {
        editor.putString("fb_native_setting", value);
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
    public String fb_native_banner() {
        return this.pre.getString("fb_native_banner", "");
    }

    public void fb_native_banner(String value) {
        editor.putString("fb_native_banner", value);
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
    public String fb_banner() {
        return this.pre.getString("fb_banner", "");
    }

    public void fb_banner(String value) {
        editor.putString("fb_banner", value);
        editor.commit();
    }
    public int rate_admob() {
        return this.pre.getInt("rate_admob", 100);
    }

    public void rate_admob(int value) {
        editor.putInt("rate_admob", value);
        editor.commit();
    }

    public int rate_fb() {
        return this.pre.getInt("rate_fb", 0);
    }

    public void rate_fb(int value) {
        editor.putInt("rate_fb", value);
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
    private int version = 3;

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

    public enum AdcontrolType {
        Admob,
        Facebook;

        public static AdcontrolType toMyEnum(String myEnumString) {
            try {
                return valueOf(myEnumString);
            } catch (Exception ex) {
                return Admob;
            }
        }

        public static void setControlType() {
            int rate = getRandomNumberInRange(1, 100);
            if (rate <= instance.rate_admob()) instance.adcontrolType(Admob);
            else instance.adcontrolType(Facebook);
        }
    }

    public AdcontrolType adcontrolType() {
        return AdcontrolType.toMyEnum(this.pre.getString("adcontrolType", AdcontrolType.Admob.toString()));
    }

    private void adcontrolType(AdcontrolType value) {
        editor.putString("adcontrolType", value.toString());
        editor.commit();
    }

    private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public NativeBundle native_main;
    public NativeBundle native_banner_home;

    private void SetNative() {
        native_main = new NativeBundle(R.layout.item_admob_native_ad, R.layout.item_fb_native_ad, false,
                admob_native_main(), fb_native_main(), false);
        native_banner_home = new NativeBundle(R.layout.item_admob_native_banner_home, R.layout.item_fb_native_banner_home, true,
                admob_native_banner_home(), fb_native_banner_home(), false);
    }

    public class NativeBundle {
        public int admob_layout_resource;
        public int fb_layout_resource;
        public boolean is_native_banner;
        public String admob_ads;
        public String fb_ads;
        public boolean isAnimationButton;

        public NativeBundle(int admob_layout_resource, int fb_layout_resource, boolean is_native_banner, String admob_ads, String fb_ads, boolean isAnimationButton) {
            this.admob_layout_resource = admob_layout_resource;
            this.fb_layout_resource = fb_layout_resource;
            this.is_native_banner = is_native_banner;
            this.admob_ads = admob_ads;
            this.fb_ads = fb_ads;
            this.isAnimationButton = isAnimationButton;
        }
    }
}