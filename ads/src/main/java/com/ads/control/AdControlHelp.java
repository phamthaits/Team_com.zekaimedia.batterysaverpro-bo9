package com.ads.control;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ads.control.funtion.JSONParser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AdControlHelp {
    private Context context;
    private static AdControlHelp instance;
    private static AdControl adControl;

    public static AdControlHelp getInstance(Context context) {
        if (instance == null) {
            instance = new AdControlHelp(context);

        }
        return instance;
    }

    public static boolean is_reload_firebase() {
        return adControl.old_date() != Calendar.getInstance().get(Calendar.DAY_OF_MONTH) || !adControl.isInit();
    }

    private AdControlHelp(Context value) {
        context = value;
        adControl = AdControl.getInstance(value);
    }

    public void getAdControlFromFireBase() {
        AdControl adControl = AdControl.getInstance(context);
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
                                                adControl.adcontrolType(AdControl.AdcontrolType.Admob);
                                            else if (adc.equals("fb"))
                                                adControl.adcontrolType(AdControl.AdcontrolType.Facebook);
                                            break;
                                        case "admod_full_splash":
                                            adControl.admod_full_splash(object.getString(key));
                                            break;
                                        case "admod_full_optimization":
                                            adControl.admod_full_optimization(object.getString(key));
                                            break;
                                        case "admod_full_trashcleaner":
                                            adControl.admod_full_trashcleaner(object.getString(key));
                                            break;
                                        case "admod_full_phoneboost":
                                            adControl.admod_full_phoneboost(object.getString(key));
                                            break;
                                        case "admod_full_phonecooler":
                                            adControl.admod_full_phonecooler(object.getString(key));
                                            break;
                                        case "admod_full_fullcharge":
                                            adControl.admod_full_fullcharge(object.getString(key));
                                            break;
                                        case "admod_full_fastcharge":
                                            adControl.admod_full_fastcharge(object.getString(key));
                                            break;
                                        case "admod_native_main":
                                            adControl.admod_native_main(object.getString(key));
                                            break;
                                        case "admod_native_optimization":
                                            adControl.admod_native_optimization(object.getString(key));
                                            break;
                                        case "admod_native_trashcleaner":
                                            adControl.admod_native_trashcleaner(object.getString(key));
                                            break;
                                        case "admod_native_phoneboost":
                                            adControl.admod_native_phoneboost(object.getString(key));
                                            break;
                                        case "admod_native_phonecooler":
                                            adControl.admod_native_phonecooler(object.getString(key));
                                            break;
                                        case "admod_native_fullcharge":
                                            adControl.admod_native_fullcharge(object.getString(key));
                                            break;
                                        case "admod_native_fastcharge":
                                            adControl.admod_native_fastcharge(object.getString(key));
                                            break;
                                        case "admod_native_setting":
                                            adControl.admod_native_setting(object.getString(key));
                                            break;
                                        case "admod_native_chargeresult":
                                            adControl.admod_native_chargeresult(object.getString(key));
                                            break;
                                        case "admod_banner_appmanager":
                                            adControl.admod_banner_appmanager(object.getString(key));
                                            break;
                                        case "admod_banner_chargehistory":
                                            adControl.admod_banner_chargehistory(object.getString(key));
                                            break;
                                        case "fb_full":
                                            adControl.fb_full(object.getString(key));
                                            break;
                                        case "fb_banner":
                                            adControl.fb_banner(object.getString(key));
                                            break;
                                        case "fb_native":
                                            adControl.fb_native(object.getString(key));
                                            break;
                                    }
                                    Log.d("ads", "key = " + key + ":" + object.getString(key));
                                    adControl.isInit(true);
                                    adControl.old_date(-1);
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

    public static class ReadConfigAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            String configUrl = "https://www.dropbox.com/s/2grklnd4svp62dz/com.amt.batterysaver.json?dl=1";
            try {
                JSONObject o = JSONParser.getJSONFromUrl(configUrl);
                if (o != null) {
                    adControl.isLoadAds(o.getBoolean("isShow"));
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
