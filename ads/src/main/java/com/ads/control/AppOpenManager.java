package com.ads.control;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private static final String LOG_TAG = "AppOpenManager";
    private AppOpenAd appOpenAd = null;
    private static boolean isShowingAd = false;
    private long loadTime = 0;
    private long timeshowAds = 0;
    private final long timeReshow = 30 * 1000;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private final AdsApplication myApplication;
    private Activity currentActivity;

    /**
     * Constructor
     */
    public AppOpenManager(AdsApplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
//        showAdIfAvailable();
//        Log.d(LOG_TAG, "onStart");
    }

    /**
     * Request an ad
     */
    public void fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                        AppOpenManager.this.loadTime = (new Date()).getTime();
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                    }

                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AdControl.getInstance(currentActivity).admob_open(), request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    /**
     * Shows the ad if one isn't already showing.
     */
    public void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable() && canShow()) {
            if (AdControl._isShowOpenAds) {
                Log.d(LOG_TAG, "Will show ad.");
                timeshowAds = System.currentTimeMillis();
                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Set the reference to null so isAdAvailable() returns false.
                                AppOpenManager.this.appOpenAd = null;
                                isShowingAd = false;
                                Log.d(LOG_TAG, "isShowingAd=fales");
                                fetchAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.d(LOG_TAG, "AdError: " + adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                isShowingAd = true;
                            }
                        };

                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(currentActivity);
            }


        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            Log.d(LOG_TAG, "isShowingAd: " + isShowingAd);
            Log.d(LOG_TAG, "canShow: " + canShow());
            Log.d(LOG_TAG, "isAdAvailable: " + isAdAvailable());
            fetchAd();
        }
    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
//        return true;
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable() {
        return appOpenAd != null
                && wasLoadTimeLessThanNHoursAgo(4);
    }

    public boolean canShow() {
//        return true;
        return (timeshowAds + timeReshow) < System.currentTimeMillis();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.v("LifeCycleT", "Created: " + activity.getComponentName().getClassName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.v("LifeCycleT", "Started: " + activity.getComponentName().getClassName());
        if(activity.getComponentName().getClassName().toLowerCase().contains("mainactivity"))
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.v("LifeCycleT", "Resumed: " + activity.getComponentName().getClassName());
        if(activity.getComponentName().getClassName().toLowerCase().contains("mainactivity"))
            currentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.v("LifeCycleT", "Stopped: " + activity.getComponentName().getClassName());
//        currentActivity = activity;

    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.v("LifeCycleT", "Paused: " + activity.getComponentName().getClassName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }
}
