package com.amt.batterysaver.Utilsb;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.ads.control.AdmobHelp;
import com.ads.control.AdmodAd;
import com.ads.control.LocationAds;
import com.ads.control.TypeAds;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

public class AdmodRef {

    private static Context _context;
    private static Activity _activity;
    private static TypeAds _typeAds;
    private static LocationAds _locationAds;
    private static View _rootView;

    private static void getAdmodFromFireBase() {
        if (!AdmodAd.isInit) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Ad")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JSONObject object = new JSONObject(document.getData());
                                for (int i = 0; i < object.names().length(); i++) {
                                    try {
                                        String key = object.names().getString(i);
                                        switch (key) {
                                            case "admod_full_splash":
                                                AdmodAd.admod_full_splash = object.getString(key);
                                                break;
                                            case "admod_full_optimization":
                                                AdmodAd.admod_full_optimization = object.getString(key);
                                                break;
                                            case "admod_full_trashcleaner":
                                                AdmodAd.admod_full_trashcleaner = object.getString(key);
                                                break;
                                            case "admod_full_phoneboost":
                                                AdmodAd.admod_full_phoneboost = object.getString(key);
                                                break;
                                            case "admod_full_phonecooler":
                                                AdmodAd.admod_full_phonecooler = object.getString(key);
                                                break;
//                                            case "admod_full_fullcharge":
//                                                AdmodAd.admod_full_fullcharge = object.getString(key);
//                                                break;
                                            case "admod_full_fastcharge":
                                                AdmodAd.admod_full_fastcharge = object.getString(key);
                                                break;
                                            case "admod_native_main":
                                                AdmodAd.admod_native_main = object.getString(key);
                                                break;
                                            case "admod_native_optimization":
                                                AdmodAd.admod_native_optimization = object.getString(key);
                                                break;
                                            case "admod_native_trashcleaner":
                                                AdmodAd.admod_native_trashcleaner = object.getString(key);
                                                break;
                                            case "admod_native_phoneboost":
                                                AdmodAd.admod_native_phoneboost = object.getString(key);
                                                break;
                                            case "admod_native_phonecooler":
                                                AdmodAd.admod_native_phonecooler = object.getString(key);
                                                break;
//                                            case "admod_native_fullcharge":
//                                                AdmodAd.admod_native_fullcharge = object.getString(key);
//                                                break;
                                            case "admod_native_fastcharge":
                                                AdmodAd.admod_native_fastcharge = object.getString(key);
                                                break;
                                            case "admod_native_setting":
                                                AdmodAd.admod_native_setting = object.getString(key);
                                                break;
//                                            case "admod_native_chargeresult":
//                                                AdmodAd.admod_native_chargeresult = object.getString(key);
//                                                break;
                                            case "admod_banner_appmanager":
                                                AdmodAd.admod_banner_appmanager = object.getString(key);
                                                break;
                                            case "admod_banner_chargehistory":
                                                AdmodAd.admod_banner_chargehistory = object.getString(key);
                                                break;
                                        }
                                        loadAds();
                                        Log.d("123", "key = " + key + ":" + object.getString(key));
                                        AdmodAd.isInit = true;
                                    } catch (JSONException e) {
                                        Log.d("123", "Lỗi");
                                        e.printStackTrace();
                                    }
                                }
                                Log.d("123", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("123", "Error getting documents.", task.getException());
                        }
                    });
        } else {
            loadAds();
        }
    }

    private static void loadAds() {
        switch (_locationAds) {
            case full:
                AdmobHelp.getInstance().init(_context, _typeAds);
                break;
            case loadNative:
                AdmobHelp.getInstance().loadNative(_activity, _typeAds);
                break;

            case loadBannerFragment:
                AdmobHelp.getInstance().loadBannerFragment(_rootView, _typeAds);
                break;
            case loadNativeFragment:
                AdmobHelp.getInstance().loadNativeFragment(_activity, _rootView, _typeAds);
                break;
        }
    }

    public static void initInterstitialAd(Context context, TypeAds typeAds) {
        _context = context;
        _typeAds = typeAds;
        _locationAds = LocationAds.full;
        getAdmodFromFireBase();
    }

    public static void loadNative(Activity activity, TypeAds typeAds) {
        _activity = activity;
        _typeAds = typeAds;
        _locationAds = LocationAds.loadNative;
        getAdmodFromFireBase();
    }

    public static void loadBannerFragment(View rootView, TypeAds typeAds) {
        _rootView = rootView;
        _typeAds = typeAds;
        _locationAds = LocationAds.loadBannerFragment;
        getAdmodFromFireBase();
    }

    public static void loadNativeFragment(Activity activity, View rootView, TypeAds typeAds) {
        _activity = activity;
        _rootView = rootView;
        _typeAds = typeAds;
        _locationAds = LocationAds.loadNativeFragment;
        getAdmodFromFireBase();
    }
}
