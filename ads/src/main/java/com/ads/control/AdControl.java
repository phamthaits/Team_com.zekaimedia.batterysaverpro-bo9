package com.ads.control;

import android.os.AsyncTask;
import android.util.Log;

import com.ads.control.funtion.JSONParser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdControl {
    public static boolean isInit = false;
    public static AdcontrolType adControl = AdcontrolType.Admob;
    public static String admod_full_splash = "";
    public static String admod_full_optimization = "";
    public static String admod_full_trashcleaner = "";
    public static String admod_full_phoneboost = "";
    public static String admod_full_phonecooler = "";
    public static String admod_full_fastcharge = "";
    public static String admod_full_fullcharge = "";

    public static String admod_native_main = "";
    public static String admod_native_optimization = "";
    public static String admod_native_trashcleaner = "";
    public static String admod_native_phoneboost = "";
    public static String admod_native_phonecooler = "";
    public static String admod_native_fastcharge = "";
    public static String admod_native_setting = "";
    public static String admod_native_fullcharge = "";
    public static String admod_native_chargeresult = "";

    public static String admod_banner_appmanager = "";
    public static String admod_banner_chargehistory = "";
    public static boolean isLoadAds = true;
    public static String fb_full_splash = "285555782814454_286166426086723";
    public static String fb_native = "285555782814454_285575599479139";
    public static String fb_banner = "285555782814454_286166796086686";

    public enum AdcontrolType {
        Admob,
        Facebook,
        Mix
    }

    public static void getAdControlFromFireBase() {
        if (!AdControl.isInit) {
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
                                                    AdControl.adControl = AdcontrolType.Admob;
                                                else if (adc.equals("fb"))
                                                    AdControl.adControl = AdcontrolType.Facebook;
                                                break;
                                            case "admod_full_splash":
                                                AdControl.admod_full_splash = object.getString(key);
                                                break;
                                            case "admod_full_optimization":
                                                AdControl.admod_full_optimization = object.getString(key);
                                                break;
                                            case "admod_full_trashcleaner":
                                                AdControl.admod_full_trashcleaner = object.getString(key);
                                                break;
                                            case "admod_full_phoneboost":
                                                AdControl.admod_full_phoneboost = object.getString(key);
                                                break;
                                            case "admod_full_phonecooler":
                                                AdControl.admod_full_phonecooler = object.getString(key);
                                                break;
                                            case "admod_full_fullcharge":
                                                AdControl.admod_full_fullcharge = object.getString(key);
                                                break;
                                            case "admod_full_fastcharge":
                                                AdControl.admod_full_fastcharge = object.getString(key);
                                                break;
                                            case "admod_native_main":
                                                AdControl.admod_native_main = object.getString(key);
                                                break;
                                            case "admod_native_optimization":
                                                AdControl.admod_native_optimization = object.getString(key);
                                                break;
                                            case "admod_native_trashcleaner":
                                                AdControl.admod_native_trashcleaner = object.getString(key);
                                                break;
                                            case "admod_native_phoneboost":
                                                AdControl.admod_native_phoneboost = object.getString(key);
                                                break;
                                            case "admod_native_phonecooler":
                                                AdControl.admod_native_phonecooler = object.getString(key);
                                                break;
                                            case "admod_native_fullcharge":
                                                AdControl.admod_native_fullcharge = object.getString(key);
                                                break;
                                            case "admod_native_fastcharge":
                                                AdControl.admod_native_fastcharge = object.getString(key);
                                                break;
                                            case "admod_native_setting":
                                                AdControl.admod_native_setting = object.getString(key);
                                                break;
                                            case "admod_native_chargeresult":
                                                AdControl.admod_native_chargeresult = object.getString(key);
                                                break;
                                            case "admod_banner_appmanager":
                                                AdControl.admod_banner_appmanager = object.getString(key);
                                                break;
                                            case "admod_banner_chargehistory":
                                                AdControl.admod_banner_chargehistory = object.getString(key);
                                                break;
                                        }
                                        Log.d("ads", "key = " + key + ":" + object.getString(key));
                                        AdControl.isInit = true;
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
                    AdControl.isLoadAds= o.getBoolean("isShow");
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