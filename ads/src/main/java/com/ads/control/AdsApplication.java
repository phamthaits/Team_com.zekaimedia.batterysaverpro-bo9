package com.ads.control;


import com.google.android.gms.ads.MobileAds;

import org.litepal.LitePalApplication;

public abstract class AdsApplication extends LitePalApplication {

    public AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(
                this,
                initializationStatus -> {
                });
        appOpenManager = new AppOpenManager(this);
    }
}
