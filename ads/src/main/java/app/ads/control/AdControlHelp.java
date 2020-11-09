package app.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AdControlHelp {
    private static Context context;
    private static AdControlHelp instance;
    private static AdControl adControl;
    private static AdmobHelp admobHelp;
    private static FBHelp fbHelp;
    private static StartAppHelp startAppHelp;
    private static MopubHelp mopubHelp;

    public static AdControlHelp getInstance(Context value) {
        context = value;
        adControl = AdControl.getInstance(value);
        StartAppAd.disableSplash();
        StartAppAd.disableAutoInterstitial();
        StartAppSDK.enableReturnAds(false);
        StartAppSDK.setUserConsent(context, "pas", System.currentTimeMillis(), true);
        if (instance == null) {
            instance = new AdControlHelp();
        }
        return instance;
    }

    public boolean is_reload_firebase() {
        if (adControl.remove_ads())
            return false;
        if (adControl.isTest) return true;
        return adControl.old_date() != Calendar.getInstance().get(Calendar.DAY_OF_MONTH) || !adControl.isInit();
    }

    private AdControlHelp() {

    }

    public void getAdControlFromFireBase(AdmobHelp.FireBaseListener fireBaseListener) {
        Log.v("ads", "Load Firebase");
        AdControl adControl = AdControl.getInstance(context);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Ad1")
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
                                        case "rate_admob":
                                            adControl.rate_admob(object.getInt(key));
                                            break;
                                        case "rate_fb":
                                            adControl.rate_fb(object.getInt(key));
                                            break;
                                        case "rate_startapp":
                                            adControl.rate_startapp(object.getInt(key));
                                            break;
                                        case "rate_mopub":
                                            adControl.rate_mopub(object.getInt(key));
                                            break;
                                        case "admob_full":
                                            adControl.admob_full(object.getString(key));
                                            break;
                                        case "admob_native":
                                            adControl.admob_native(object.getString(key));
                                            break;
                                        case "admob_banner":
                                            adControl.admob_banner(object.getString(key));
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
                                        case "mopub_full":
                                            adControl.mopub_full(object.getString(key));
                                            break;
                                        case "mopub_banner":
                                            adControl.mopub_banner(object.getString(key));
                                            break;
                                        case "mopub_native":
                                            adControl.mopub_native(object.getString(key));
                                            break;
                                    }
                                    Log.d("ads", "key = " + key + ":" + object.getString(key));
                                    adControl.isInit(true);
                                    AdControl.AdcontrolType.setControlType();
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
                    fireBaseListener.addOnCompleteListener();
                });
    }

    public interface AdCloseListener {
        void onAdClosed();
    }

    public interface AdLoadedListener {
        void onAdLoaded();
    }

    public void loadNative(Activity mActivity) {
        if (adControl.remove_ads()) {
            return;
        }
        loadNetworkHelp();
        switch (adControl.adcontrolType()) {
            case Admob:
                admobHelp.loadNative(mActivity, adControl.admob_native());
                break;
            case Facebook:
                fbHelp.loadNative(mActivity, adControl.fb_native());
                break;
            case StartApp:
                startAppHelp.loadNative(mActivity);
                break;
            case Mopub:
                mopubHelp.loadNative(mActivity, adControl.mopub_native());
                break;
        }
    }

    public void loadNativeFragment(Activity mActivity, View view) {
        if (adControl.remove_ads()) {
            return;
        }
        loadNetworkHelp();
        switch (adControl.adcontrolType()) {
            case Admob:
                admobHelp.loadNativeFragment(mActivity, view, adControl.admob_native());
                break;
            case Facebook:
                fbHelp.loadNativeFrament(mActivity, view, adControl.fb_native());
                break;
            case StartApp:
                startAppHelp.loadNativeFragment(mActivity, view);
                break;
            case Mopub:
                mopubHelp.loadNativeFragment(view, adControl.mopub_native());
                break;
        }
    }

    public void loadBannerFragment(Activity mActivity, View view) {
        if (adControl.remove_ads()) {
            return;
        }
        loadNetworkHelp();
        switch (adControl.adcontrolType()) {
            case Admob:
                admobHelp.loadBannerFragment(mActivity, view, adControl.admob_banner());
                break;
            case Facebook:
                fbHelp.loadBannerFragment(mActivity, view, adControl.fb_banner());
                break;
            case StartApp:
                startAppHelp.loadBannerFragment(mActivity, view);
                break;
            case Mopub:
                mopubHelp.loadBannerFragment(view, adControl.mopub_banner());
                break;
        }
    }


    public void loadInterstitialAd(Activity activity, AdCloseListener adCloseListener, AdLoadedListener adLoadedListener, boolean showWhenLoaded) {
        Log.v("ads", "Call ads");
        if (adControl.remove_ads()) {
            if (showWhenLoaded)
                adCloseListener.onAdClosed();
            return;
        }
        loadNetworkHelp();
        switch (adControl.adcontrolType()) {
            case Admob:
                admobHelp.loadInterstitialAd(adCloseListener, adLoadedListener, adControl.admob_full(), showWhenLoaded);
                break;
            case Facebook:
                fbHelp.loadInterstitialAd(adCloseListener, adLoadedListener, adControl.fb_full(), showWhenLoaded);
                break;
            case StartApp:
                startAppHelp.loadInterstitialAd(adCloseListener, adLoadedListener, showWhenLoaded);
                break;
            case Mopub:
                mopubHelp.loadInterstitialAd(activity, adCloseListener, adLoadedListener, adControl.mopub_full(), showWhenLoaded);
                break;
        }
    }

    public void showInterstitialAd(AdCloseListener adCloseListener) {
        if (adControl.remove_ads()) {
            adCloseListener.onAdClosed();
            return;
        }
        loadNetworkHelp();
        switch (adControl.adcontrolType()) {
            case Admob:
                admobHelp.showInterstitialAd(adCloseListener);
                break;
            case Facebook:
                fbHelp.showInterstitialAd(adCloseListener);
                break;
            case StartApp:
                startAppHelp.showInterstitialAd(adCloseListener);
                break;
            case Mopub:
                mopubHelp.showInterstitialAd(adCloseListener);
                break;
        }
    }

    private void loadNetworkHelp() {
        switch (adControl.adcontrolType()) {
            case Admob:
                admobHelp = AdmobHelp.getInstance(context);
                break;
            case Facebook:
                fbHelp = FBHelp.getInstance(context);
                break;
            case StartApp:
                startAppHelp = StartAppHelp.getInstance(context);
                break;
            case Mopub:
                mopubHelp = MopubHelp.getInstance(context);
                break;
        }
    }
}
