package app.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ads.control.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AdControlHelp {
    private static Context context;
    private static AdControlHelp instance;
    private static AdControl adControl;
    private static AdmobHelp admobHelp;
    private static FBHelp fbHelp;
    private static MopubHelp mopubHelp;

    public static AdControlHelp getInstance(Context value) {
        context = value;
        adControl = AdControl.getInstance(value);
        if (instance == null) {
            instance = new AdControlHelp();
        }
        return instance;
    }

    public boolean is_reload_firebase() {
//        return true;
        if (adControl.remove_ads())
            return false;
//        if (adControl.isTest) return true;
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

    public void getAdControlFromFireBase(FireBaseListener fireBaseListener) {
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
                                        case "rate_mopub":
                                            adControl.rate_mopub(object.getInt(key));
                                            break;
                                        case "admob_full":
                                            adControl.admob_full(getRealAdmob(object.getString(key)));
                                            break;
                                        case "admob_native":
                                            adControl.admob_native(getRealAdmob(object.getString(key)));
                                            break;
                                        case "admob_native_setting":
                                            adControl.admob_native_setting(getRealAdmob(object.getString(key)));
                                            break;
                                        case "admob_native_rate_app":
                                            adControl.admob_native_rate_app(getRealAdmob(object.getString(key)));
                                            break;
                                        case "admob_banner":
                                            adControl.admob_banner(getRealAdmob(object.getString(key)));
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

    public void loadNative(Activity mActivity, LinearLayout view) {
        loadNative(mActivity, view, R.layout.item_admob_native_ad, R.layout.item_fb_native_ad,
                R.layout.item_mopub_native_ad, true, false, adControl.admob_native());
    }

    public void loadNative(Activity mActivity, LinearLayout view, int admob_layout_resource,
                           int fb_layout_resource,
                           int mopub_layout_resource, boolean isAnimButton, boolean is_native_banner) {
        loadNative(mActivity, view, admob_layout_resource, fb_layout_resource, mopub_layout_resource,
                isAnimButton, is_native_banner, adControl.admob_native());
    }

    public void loadNative(Activity mActivity, LinearLayout view, int admob_layout_resource,
                           int fb_layout_resource, int mopub_layout_resource,
                           boolean isAnimButton, boolean is_native_banner,
                           String admob_native_ads) {
        if (adControl.remove_ads()) {
            return;
        }
        loadNetworkHelp();
        switch (adControl.adcontrolType()) {
            case Admob:
                admobHelp.loadNative(mActivity, view, admob_native_ads,
                        admob_layout_resource, isAnimButton, is_native_banner);
                break;
            case Facebook:
                fbHelp.loadNative(mActivity, view, adControl.fb_native(), fb_layout_resource,
                        isAnimButton, is_native_banner);
                break;
            case Mopub:
                mopubHelp.loadNative(mActivity, view, adControl.mopub_native(),
                        mopub_layout_resource,
                        isAnimButton, is_native_banner);
                break;
        }
    }

    public void loadBanner(Activity mActivity, View view) {
        if (adControl.remove_ads()) {
            return;
        }
        loadNetworkHelp();
        switch (adControl.adcontrolType()) {
            case Admob:
                admobHelp.loadBanner(mActivity, view, adControl.admob_banner());
                break;
            case Facebook:
                fbHelp.loadBanner(mActivity, view, adControl.fb_banner());
                break;
            case Mopub:
                mopubHelp.loadBanner(view, adControl.mopub_banner());
                break;
        }
    }


    public void loadInterstitialAd(Activity activity, AdCloseListener adCloseListener, AdLoadedListener adLoadedListener, boolean showWhenLoaded) {
        Log.v("ads", "Call ads");
//        adCloseListener.onAdClosed();
//        return;
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
            case Mopub:
                mopubHelp = MopubHelp.getInstance(context);
                break;
        }
    }

    public interface FireBaseListener {
        void addOnCompleteListener();
    }
}
