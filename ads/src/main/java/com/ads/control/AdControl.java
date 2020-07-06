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

    private AdControl(Context context) {
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
    public String admod_full_fastcharge() {
        return this.pre.getString("admod_full_fastcharge", "");
    }

    public void admod_full_fastcharge(String value) {
        editor.putString("admod_full_fastcharge", value);
        editor.commit();
    }

    //    public static String admod_full_fullcharge = "";
    public String admod_full_fullcharge() {
        return this.pre.getString("admod_full_fullcharge", "");
    }

    public void admod_full_fullcharge(String value) {
        editor.putString("admod_full_fullcharge", value);
        editor.commit();
    }

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
    public String admod_native_fastcharge() {
        return this.pre.getString("admod_native_fastcharge", "");
    }

    public void admod_native_fastcharge(String value) {
        editor.putString("admod_native_fastcharge", value);
        editor.commit();
    }

    //    public static String admod_native_setting = "";
    public String admod_native_setting() {
        return this.pre.getString("admod_native_setting", "");
    }

    public void admod_native_setting(String value) {
        editor.putString("admod_native_setting", value);
        editor.commit();
    }

    //    public static String admod_native_fullcharge = "";
    public String admod_native_fullcharge() {
        return this.pre.getString("admod_native_fullcharge", "");
    }

    public void admod_native_fullcharge(String value) {
        editor.putString("admod_native_fullcharge", value);
        editor.commit();
    }

    //    public static String admod_native_chargeresult = "";
    public String admod_native_chargeresult() {
        return this.pre.getString("admod_native_chargeresult", "");
    }

    public void admod_native_chargeresult(String value) {
        editor.putString("admod_native_chargeresult", value);
        editor.commit();
    }

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
    public boolean isLoadAds() {
        return this.pre.getBoolean("isLoadAds", false);
    }

    public void isLoadAds(boolean value) {
        editor.putBoolean("isLoadAds", value);
        editor.commit();
    }

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

    public void getAdControlFromFireBase() {
//        if (!instance.isInit()) {
        if (instance.old_date() != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("AdTest")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JSONObject object = new JSONObject(document.getData());
                                for (int i = 0; i < object.names().length(); i++) {
                                    try {
                                        String key = object.names().getString(i);
                                        switch (key) {
                                            case "adControl":
                                                String adc = object.getString(key);
                                                if (adc.equals("admob"))
                                                    instance.adcontrolType(AdcontrolType.Admob);
                                                else if (adc.equals("fb"))
                                                    instance.adcontrolType(AdcontrolType.Facebook);
                                                break;
                                            case "admod_full_splash":
                                                instance.admod_full_splash(object.getString(key));
                                                break;
                                            case "admod_full_optimization":
                                                instance.admod_full_optimization(object.getString(key));
                                                break;
                                            case "admod_full_trashcleaner":
                                                instance.admod_full_trashcleaner(object.getString(key));
                                                break;
                                            case "admod_full_phoneboost":
                                                instance.admod_full_phoneboost(object.getString(key));
                                                break;
                                            case "admod_full_phonecooler":
                                                instance.admod_full_phonecooler(object.getString(key));
                                                break;
                                            case "admod_full_fullcharge":
                                                instance.admod_full_fullcharge(object.getString(key));
                                                break;
                                            case "admod_full_fastcharge":
                                                instance.admod_full_fastcharge(object.getString(key));
                                                break;
                                            case "admod_native_main":
                                                instance.admod_native_main(object.getString(key));
                                                break;
                                            case "admod_native_optimization":
                                                instance.admod_native_optimization(object.getString(key));
                                                break;
                                            case "admod_native_trashcleaner":
                                                instance.admod_native_trashcleaner(object.getString(key));
                                                break;
                                            case "admod_native_phoneboost":
                                                instance.admod_native_phoneboost(object.getString(key));
                                                break;
                                            case "admod_native_phonecooler":
                                                instance.admod_native_phonecooler(object.getString(key));
                                                break;
                                            case "admod_native_fullcharge":
                                                instance.admod_native_fullcharge(object.getString(key));
                                                break;
                                            case "admod_native_fastcharge":
                                                instance.admod_native_fastcharge(object.getString(key));
                                                break;
                                            case "admod_native_setting":
                                                instance.admod_native_setting(object.getString(key));
                                                break;
                                            case "admod_native_chargeresult":
                                                instance.admod_native_chargeresult(object.getString(key));
                                                break;
                                            case "admod_banner_appmanager":
                                                instance.admod_banner_appmanager(object.getString(key));
                                                break;
                                            case "admod_banner_chargehistory":
                                                instance.admod_banner_chargehistory(object.getString(key));
                                                break;
                                        }
                                        Log.d("ads", "key = " + key + ":" + object.getString(key));
                                        instance.isInit(true);
                                        instance.old_date(-1);
                                    } catch (JSONException e) {
                                        Log.d("ads", "Lỗi");
                                        e.printStackTrace();
                                    }
                                }
                                Log.d("123", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("123", "Error getting documents.", task.getException());
                        }
                    });
        }
    }

    public static class ReadConfigAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            String configUrl = "https://www.dropbox.com/s/2grklnd4svp62dz/com.amt.batterysaver.json?dl=1";
            try {
                JSONObject o = JSONParser.getJSONFromUrl(configUrl);
                if (o != null) {
                    instance.isLoadAds(o.getBoolean("isShow"));
                    Log.v("JobManager", o.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public interface AdCloseListener {
        void onAdClosed();
    }

    public interface AdLoadedListener {
        void onAdLoaded();
    }
}