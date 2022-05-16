package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

public class AdControlHelp {
    private static AdControlHelp instance;
    private static AdControl adControl;
    private static AdmobHelp admobHelp;
    private static Random random = new Random();

    public void getAdControlFromFireBase(FireBaseListener fireBaseListener, Activity activity) {
        Log.v("ads", "Load Firebase");
        AdControl adControl = AdControl.getInstance(activity);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Ad1")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.v("ads", document.getId() + " => " + document.getData());
                            JSONObject object = new JSONObject(document.getData());
                            for (int i = 0; i < object.names().length(); i++) {
                                try {
                                    String key = object.names().getString(i);
                                    switch (key) {
                                        case "admob_full":
                                            adControl.admob_full(getRealAdmob(object.getString(key), activity));
                                            break;
                                        case "admob_full_rate":
                                            adControl.admob_full_rate(object.getInt(key));
                                            break;
                                        case "admob_native_setting":
                                            adControl.admob_native_setting(getRealAdmob(object.getString(key), activity));
                                            break;
                                        case "admob_native_setting_rate":
                                            adControl.admob_native_setting_rate(object.getInt(key));
                                            break;
                                        case "admob_native_banner":
                                            adControl.admob_native_banner(getRealAdmob(object.getString(key), activity));
                                            break;
                                        case "admob_native_banner_rate":
                                            adControl.admob_native_banner_rate(object.getInt(key));
                                            break;
                                        case "admob_native_main":
                                            adControl.admob_native_main(getRealAdmob(object.getString(key), activity));
                                            break;
                                        case "admob_native_main_rate":
                                            adControl.admob_native_main_rate(object.getInt(key));
                                            break;
                                        case "admob_native_result":
                                            adControl.admob_native_result(getRealAdmob(object.getString(key), activity));
                                            break;
                                        case "admob_native_result_rate":
                                            adControl.admob_native_result_rate(object.getInt(key));
                                            break;
                                        case "admob_banner":
                                            adControl.admob_banner(getRealAdmob(object.getString(key), activity));
                                            break;
                                        case "admob_banner_rate":
                                            adControl.admob_banner_rate(object.getInt(key));
                                            break;
                                        case "admob_open":
                                            adControl.admob_open(getRealAdmob(object.getString(key), activity));
                                            break;
                                        case "admob_open_rate":
                                            adControl.admob_open_rate(object.getInt(key));
                                            break;
                                        case "version":
                                            adControl.isUpdate(object.getInt(key));
                                            break;
                                        case "version_update_url":
                                            adControl.version_update_url(object.getString("version_update_url"));
                                    }
                                    Log.v("ads", "key = " + key + ":" + object.getString(key));
                                    adControl.isInit(true);
                                    adControl.old_date(-1);
                                } catch (JSONException e) {
                                    Log.v("ads", "Lỗi");
                                    e.printStackTrace();
                                }
                            }
                            Log.v("ads", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.v("ads", "Error getting documents.", task.getException());
                    }
                    if (fireBaseListener != null)
                        fireBaseListener.addOnCompleteListener();
                });
    }

    public static AdControlHelp getInstance(Activity value) {
        adControl = AdControl.getInstance(value);
        if (instance == null) {
            instance = new AdControlHelp();
        }
        return instance;
    }

    public void mobileAdsInitialize(Activity activity, MobileAdsInitialize mobileAdsInitialize) {
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.v("ads", String.format("Adapter name: %s, Description: %s, Latency: %d", adapterClass, status.getDescription(), status.getLatency()));
                }
                if (mobileAdsInitialize != null)
                    mobileAdsInitialize.onInitialized();
            }
        });
    }

    private AdControlHelp() {
    }

    private boolean delivery_rate(int rate) {
        int r = random.nextInt(100);
        Log.v("ads", "Rate: " + r + "/" + rate);
        return rate > r;
    }

    private String getRealAdmob(String reverse, Activity activity) {
        String key_reverse = activity.getResources().getString(R.string.admob_app_id);
        String[] fn_reverse = key_reverse.split("~");
        String value_reverse = new StringBuffer(reverse).reverse().toString();
        String reversed = fn_reverse[0] + "/" + value_reverse;
        return reversed;
    }

    public void loadNative(Activity mActivity, LinearLayout root_view, AdControl.NativeBundle nativeBundle) {
        loadNetworkHelp(mActivity);
        if (adControl.remove_ads() || !adControl.isInit() || !delivery_rate(nativeBundle.rate)) {
            admobHelp.goneNative(root_view);
            return;
        }
        loadNetworkHelp(mActivity);
        if (!nativeBundle.admob_ads.equals(""))
            admobHelp.loadNative(mActivity, root_view, nativeBundle);
    }

    public void loadBanner(Activity mActivity, View view) {
        loadNetworkHelp(mActivity);
        if (adControl.remove_ads() || !adControl.isInit() || !delivery_rate(adControl.admob_banner_rate())) {
            admobHelp.goneBanner(view);
            return;
        }

        if (!adControl.admob_banner().equals(""))
            admobHelp.loadBanner(mActivity, view, adControl.admob_banner());
    }

    public void loadInterstitialAd(Activity activity, AdLoadedListener adLoadedListener) {
        if (adControl.remove_ads() || !adControl.isInit()) {
            if (adLoadedListener != null)
                adLoadedListener.onAdLoaded();
            return;
        }
        loadNetworkHelp(activity);
        if (!adControl.admob_full().equals(""))
            admobHelp.loadInterstitialAd(activity, adLoadedListener);
        else {
            if (adLoadedListener != null)
                adLoadedListener.onAdLoaded();
        }
    }

    public void showInterstitialAd(Activity activity, AdCloseListener adCloseListener) {
        if (adControl.remove_ads() || !adControl.isInit() || !delivery_rate(adControl.admob_full_rate())) {
            if (adCloseListener != null)
                adCloseListener.onAdClosed();
            return;
        }
        loadNetworkHelp(activity);
        admobHelp.showInterstitialAd(activity, adCloseListener, adControl.admob_full());
    }

    private void loadNetworkHelp(Activity activity) {
        admobHelp = AdmobHelp.getInstance(activity);
    }

    public interface FireBaseListener {
        void addOnCompleteListener();
    }

    public interface AdCloseListener {
        void onAdClosed();
    }

    public interface AdLoadedListener {
        void onAdLoaded();
    }

    public interface MobileAdsInitialize {
        void onInitialized();
    }
}

