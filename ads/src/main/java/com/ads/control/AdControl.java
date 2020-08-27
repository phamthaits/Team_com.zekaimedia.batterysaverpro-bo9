package com.ads.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.ads.control.funtion.JSONParser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

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

    //    public static boolean isInit = false;
    public boolean isInit() {
        return this.pre.getBoolean("isInit", false);
    }

    public void isInit(boolean value) {
        editor.putBoolean("isInit", value);
        editor.commit();
    }

    //    public static AdcontrolType adControl = AdcontrolType.Admob;
    public AdcontrolType adcontrolType() {
        return AdcontrolType.toMyEnum(this.pre.getString("adcontrolType", AdcontrolType.Admob.toString()));
    }

    public void adcontrolType(AdcontrolType value) {
        editor.putString("adcontrolType", value.toString());
        editor.commit();
    }
    //    public static String admod_full_splash = "";

    public String admod_full_splash() {
        return this.pre.getString("admod_full_splash", "");
    }

    public void admod_full_splash(String value) {
        editor.putString("admod_full_splash", value);
        editor.commit();
    }
    //    public static String admod_full_optimization = "";

    public String admod_full_optimization() {
        return this.pre.getString("admod_full_optimization", "");
    }

    public void admod_full_optimization(String value) {
        editor.putString("admod_full_optimization", value);
        editor.commit();
    }
    //    public static String admod_full_trashcleaner = "";

    public String admod_full_trashcleaner() {
        return this.pre.getString("admod_full_trashcleaner", "");
    }

    public void admod_full_trashcleaner(String value) {
        editor.putString("admod_full_trashcleaner", value);
        editor.commit();
    }

    //    public static String admod_full_phoneboost = "";
    public String admod_full_phoneboost() {
        return this.pre.getString("admod_full_phoneboost", "");
    }

    public void admod_full_phoneboost(String value) {
        editor.putString("admod_full_phoneboost", value);
        editor.commit();
    }

    //    public static String admod_full_phonecooler = "";
    public String admod_full_phonecooler() {
        return this.pre.getString("admod_full_phonecooler", "");
    }

    public void admod_full_phonecooler(String value) {
        editor.putString("admod_full_phonecooler", value);
        editor.commit();
    }

    //    public static String admod_full_fastcharge = "";
//    public String admod_full_fastcharge() {
//        return this.pre.getString("admod_full_fastcharge", "");
//    }

//    public void admod_full_fastcharge(String value) {
//        editor.putString("admod_full_fastcharge", value);
//        editor.commit();
//    }

    //    public static String admod_full_fullcharge = "";
//    public String admod_full_fullcharge() {
//        return this.pre.getString("admod_full_fullcharge", "");
//    }

//    public void admod_full_fullcharge(String value) {
//        editor.putString("admod_full_fullcharge", value);
//        editor.commit();
//    }

    //Calendar.getInstance().getTime()
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

    //    public static String admod_native_main = "";
    public String admod_native_main() {
        return this.pre.getString("admod_native_main", "");
    }

    public void admod_native_main(String value) {
        editor.putString("admod_native_main", value);
        editor.commit();
    }

    //    public static String admod_native_optimization = "";
    public String admod_native_optimization() {
        return this.pre.getString("admod_native_optimization", "");
    }

    public void admod_native_optimization(String value) {
        editor.putString("admod_native_optimization", value);
        editor.commit();
    }

    //    public static String admod_native_trashcleaner = "";
    public String admod_native_trashcleaner() {
        return this.pre.getString("admod_native_trashcleaner", "");
    }

    public void admod_native_trashcleaner(String value) {
        editor.putString("admod_native_trashcleaner", value);
        editor.commit();
    }

    //    public static String admod_native_phoneboost = "";
    public String admod_native_phoneboost() {
        return this.pre.getString("admod_native_phoneboost", "");
    }

    public void admod_native_phoneboost(String value) {
        editor.putString("admod_native_phoneboost", value);
        editor.commit();
    }

    //    public static String admod_native_phonecooler = "";
    public String admod_native_phonecooler() {
        return this.pre.getString("admod_native_phonecooler", "");
    }

    public void admod_native_phonecooler(String value) {
        editor.putString("admod_native_phonecooler", value);
        editor.commit();
    }

    //    public static String admod_native_fastcharge = "";
//    public String admod_native_fastcharge() {
//        return this.pre.getString("admod_native_fastcharge", "");
//    }

//    public void admod_native_fastcharge(String value) {
//        editor.putString("admod_native_fastcharge", value);
//        editor.commit();
//    }

    //    public static String admod_native_setting = "";
    public String admod_native_setting() {
        return this.pre.getString("admod_native_setting", "");
    }

    public void admod_native_setting(String value) {
        editor.putString("admod_native_setting", value);
        editor.commit();
    }

    //    public static String admod_native_fullcharge = "";
//    public String admod_native_fullcharge() {
//        return this.pre.getString("admod_native_fullcharge", "");
//    }

//    public void admod_native_fullcharge(String value) {
//        editor.putString("admod_native_fullcharge", value);
//        editor.commit();
//    }

    //    public static String admod_native_chargeresult = "";
//    public String admod_native_chargeresult() {
//        return this.pre.getString("admod_native_chargeresult", "");
//    }
//
//    public void admod_native_chargeresult(String value) {
//        editor.putString("admod_native_chargeresult", value);
//        editor.commit();
//    }

    //    public static String admod_banner_appmanager = "";
    public String admod_banner_appmanager() {
        return this.pre.getString("admod_banner_appmanager", "");
    }

    public void admod_banner_appmanager(String value) {
        editor.putString("admod_banner_appmanager", value);
        editor.commit();
    }

    //    public static String admod_banner_chargehistory = "";
    public String admod_banner_chargehistory() {
        return this.pre.getString("admod_banner_chargehistory", "");
    }

    public void admod_banner_chargehistory(String value) {
        editor.putString("admod_banner_chargehistory", value);
        editor.commit();
    }

    //    public static boolean isLoadAds = true;
//    public boolean isLoadAds() {
//        return this.pre.getBoolean("isLoadAds", false);
//    }

//    public void isLoadAds(boolean value) {
//        editor.putBoolean("isLoadAds", value);
//        editor.commit();
//    }

    //    public static String fb_full = "285555782814454_286166426086723";
    public String fb_full() {
        return this.pre.getString("fb_full", "");
    }

    public void fb_full(String value) {
        editor.putString("fb_full", value);
        editor.commit();
    }

    //    public static String fb_native = "285555782814454_285575599479139";
    public String fb_native() {
        return this.pre.getString("fb_native", "");
    }

    public void fb_native(String value) {
        editor.putString("fb_native", value);
        editor.commit();
    }

    //    public static String fb_banner = "285555782814454_286166796086686";
    public String fb_banner() {
        return this.pre.getString("fb_banner", "");
    }

    public void fb_banner(String value) {
        editor.putString("fb_banner", value);
        editor.commit();
    }

    public enum AdcontrolType {
        Admob,
        Facebook,
        Mix;

        public static AdcontrolType toMyEnum(String myEnumString) {
            try {
                return valueOf(myEnumString);
            } catch (Exception ex) {
                // For error cases
                return Admob;
            }
        }
    }
}