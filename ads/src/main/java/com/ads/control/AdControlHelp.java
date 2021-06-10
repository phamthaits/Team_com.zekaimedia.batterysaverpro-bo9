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

public class AdControlHelp {
    private static Context context;
    private static AdControlHelp instance;
    private static AdControl adControl;
    private static AdmobHelp admobHelp;

    public void getAdControlFromFireBase(FireBaseListener fireBaseListener) {
        Log.v("ads", "Load Firebase");
        AdControl adControl = AdControl.getInstance(context);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Ad3")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("ads", document.getId() + " => " + document.getData());
                            JSONObject object = new JSONObject(document.getData());
                            for (int i = 0; i < object.names().length(); i++) {
                                try {
                                    String key = object.names().getString(i);
                                    switch (key) {
                                        case "admob_full":
                                            adControl.admob_full(getRealAdmob(object.getString(key)));
                                            break;
                                        case "admob_native_setting":
                                            adControl.admob_native_setting(getRealAdmob(object.getString(key)));
                                            break;
                                        case "admob_native_banner":
                                            adControl.admob_native_banner(getRealAdmob(object.getString(key)));
                                            break;
                                        case "admob_native_main":
                                            adControl.admob_native_main(getRealAdmob(object.getString(key)));
                                            break;
                                        case "admob_banner":
                                            adControl.admob_banner(getRealAdmob(object.getString(key)));
                                            break;
                                        case "version":
                                            adControl.isUpdate(object.getInt(key));
                                            break;
                                        case "version_update_url":
                                            adControl.version_update_url(object.getString("version_update_url"));
                                    }
                                    Log.d("ads", "key = " + key + ":" + object.getString(key));
                                    adControl.isInit(true);
                                    adControl.old_date(-1);
                                } catch (JSONException e) {
                                    Log.d("ads", "Lỗi");
                                    e.printStackTrace();
                                }
                            }
                            Log.d("ads", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("ads", "Error getting documents.", task.getException());
                    }
                    if (fireBaseListener != null)
                        fireBaseListener.addOnCompleteListener();
                });
    }

    public static AdControlHelp getInstance(Context value) {
        context = value;
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
                    Log.d("ads", String.format("Adapter name: %s, Description: %s, Latency: %d", adapterClass, status.getDescription(), status.getLatency()));
                }
                if (mobileAdsInitialize != null)
                    mobileAdsInitialize.onInitialized();
            }
        });
    }

    public boolean is_reload_firebase() {
        if (AdControl._isTestAds || adControl.forceUpdateFirebase()) return true;
        return adControl.old_date() != Calendar.getInstance().get(Calendar.DAY_OF_MONTH) || !adControl.isInit();
    }

    private AdControlHelp() {

    }

     private String getRealAdmob(String reverse) {
        String key_reverse = context.getResources().getString(R.string.admob_app_id);
        String[] fn_reverse = key_reverse.split("~");
        String value_reverse = new StringBuffer(reverse).reverse().toString();
        String reversed = fn_reverse[0] + "/" + value_reverse;
        return reversed;
    }

    public void loadNative(Activity mActivity, LinearLayout root_view, AdControl.NativeBundle nativeBundle) {
        Log.v("ads", "Call Load Native Total");
        if (adControl.remove_ads() || !adControl.isInit()) {
            return;
        }
        loadNetworkHelp();
        if (!nativeBundle.admob_ads.equals(""))
            admobHelp.loadNative(mActivity, root_view, nativeBundle);
    }

    public void loadBanner(Activity mActivity, View view) {
        if (adControl.remove_ads() || !adControl.isInit()) {
            return;
        }
        loadNetworkHelp();
        if (!adControl.admob_banner().equals(""))
            admobHelp.loadBanner(mActivity, view, adControl.admob_banner());
    }

    public void loadInterstitialAd(Activity activity, AdCloseListener adCloseListener, AdLoadedListener adLoadedListener, boolean showWhenLoaded) {
        Log.v("ads", "Call ads");
        if (adControl.remove_ads() || !adControl.isInit()) {
            if (showWhenLoaded)
                if (adCloseListener != null)
                    adCloseListener.onAdClosed();
            return;
        }
            loadNetworkHelp();

        if (!adControl.admob_full().equals(""))
            admobHelp.loadInterstitialAd(activity, adCloseListener, adLoadedListener, showWhenLoaded);
        else {
            if (showWhenLoaded)
                if (adCloseListener != null)
                    adCloseListener.onAdClosed();
        }
    }

    public void showInterstitialAd(Activity activity, AdCloseListener adCloseListener) {
        if (adControl.remove_ads() || !adControl.isInit()) {
            if (adCloseListener != null)
                adCloseListener.onAdClosed();
            return;
        }
        loadNetworkHelp();
        admobHelp.showInterstitialAd(activity, adCloseListener, adControl.admob_full());
    }

    private void loadNetworkHelp() {
        admobHelp = AdmobHelp.getInstance(context);
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
