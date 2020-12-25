package app.ads.control;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Random;

public class AdControl {
    private static AdControl instance;
    private SharedPreferences.Editor editor;
    private SharedPreferences pre;
    public boolean isTest = false;

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

    public String admob_native_setting() {
        return this.pre.getString("admob_native_setting", "");
    }

    public void admob_native_setting(String value) {
        editor.putString("admob_native_setting", value);
        editor.commit();
    }
    public String admob_native_rate_app() {
        return this.pre.getString("admob_native_rate_app", "");
    }

    public void admob_native_rate_app(String value) {
        editor.putString("admob_native_rate_app", value);
        editor.commit();
    }
    public String admob_banner() {
        return this.pre.getString("admob_banner", "");
    }

    public void admob_banner(String value) {
        editor.putString("admob_banner", value);
        editor.commit();
    }

    public String fb_full() {
        return this.pre.getString("fb_full", "");
    }

    public void fb_full(String value) {
        editor.putString("fb_full", value);
        editor.commit();
    }

    public String fb_native() {
        return this.pre.getString("fb_native", "");
    }

    public void fb_native(String value) {
        editor.putString("fb_native", value);
        editor.commit();
    }

    public String fb_banner() {
        return this.pre.getString("fb_banner", "");
    }

    public void fb_banner(String value) {
        editor.putString("fb_banner", value);
        editor.commit();
    }

    public String mopub_full() {
        return this.pre.getString("mopub_full", "");
    }

    public void mopub_full(String value) {
        editor.putString("mopub_full", value);
        editor.commit();
    }

    public String mopub_native() {
        return this.pre.getString("mopub_native", "");
    }

    public void mopub_native(String value) {
        editor.putString("mopub_native", value);
        editor.commit();
    }

    public String mopub_banner() {
        return this.pre.getString("mopub_banner", "");
    }

    public void mopub_banner(String value) {
        editor.putString("mopub_banner", value);
        editor.commit();
    }

    public Boolean remove_ads() {
        if (isTest) return false;
//        return true;
        return this.pre.getBoolean("remove_ads", false);
    }

    public void remove_ads(Boolean value) {
        editor.putBoolean("remove_ads", value);
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

    public int rate_mopub() {
        return this.pre.getInt("rate_startapp", 0);
    }

    public void rate_mopub(int value) {
        editor.putInt("rate_mopub", value);
        editor.commit();
    }

    public AdcontrolType adcontrolType() {
        return AdcontrolType.toMyEnum(this.pre.getString("adcontrolType", AdcontrolType.Admob.toString()));
    }

    private void adcontrolType(AdcontrolType value) {
        editor.putString("adcontrolType", value.toString());
        editor.commit();
    }

    public enum AdcontrolType {
        Admob,
        Facebook,
        Mopub;

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
            else if (rate <= instance.rate_fb() + instance.rate_admob())
                instance.adcontrolType(Facebook);
            else instance.adcontrolType(Mopub);
        }
    }

    private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public boolean isStillShowAds = true;
}