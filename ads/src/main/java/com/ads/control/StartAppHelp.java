package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ads.control.AdControlHelp.AdCloseListener;
import com.ads.control.AdControlHelp.AdLoadedListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.ads.nativead.NativeAdDetails;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

public class StartAppHelp {
    private static StartAppHelp instance;
    private static Context context;
    private static AdControl adControl;
    private StartAppNativeAd startAppNativeAd;

    public static StartAppHelp getInstance(Context value) {
        context = value;
        if (instance == null) {
            instance = new StartAppHelp();
        }
        adControl = AdControl.getInstance(context);
//        StartAppSDK.setTestAdsEnabled(true);
        StartAppSDK.init(context, "209309969", false);
        startAppAd = new StartAppAd(context);
               return instance;
    }

    private static StartAppAd startAppAd;
    AdEventListener adEventListenerEmpty = new AdEventListener() {
        @Override
        public void onReceiveAd(Ad ad) {
        }

        @Override
        public void onFailedToReceiveAd(Ad ad) {
        }
    };

    public void loadInterstitialAd(AdCloseListener adCloseListener, AdLoadedListener adLoadedListener, boolean showWhenLoaded) {
        adControl.isStillShowAds = true;
        if (adControl.remove_ads()) {
            if (showWhenLoaded)
                adCloseListener.onAdClosed();
            return;
        }
        startAppAd.loadAd(new AdEventListener() {
            @Override
            public void onReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded();
                }
                if (showWhenLoaded && adControl.isStillShowAds)
                    showInterstitialAd(adCloseListener);
            }

            @Override
            public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
                Log.v("ads", "onAdFailedToLoad");
                if (showWhenLoaded && adControl.isStillShowAds) {
                    if (adCloseListener != null)
                        adCloseListener.onAdClosed();
                }
            }
        });
    }

    public void showInterstitialAd(AdCloseListener adCloseListener) {
        Log.v("ads", "Đã gọi Show");
        if (adControl.remove_ads()) {
            adCloseListener.onAdClosed();
            return;
        }
        startAppAd.showAd(new AdDisplayListener() {
            @Override
            public void adHidden(Ad ad) {
                // Interstitial dismissed callback
                if (adCloseListener != null) {
                    adCloseListener.onAdClosed();
                }
                startAppAd.loadAd(adEventListenerEmpty);
            }

            @Override
            public void adDisplayed(Ad ad) {

            }

            @Override
            public void adClicked(Ad ad) {

            }

            @Override
            public void adNotDisplayed(Ad ad) {
                if (adCloseListener != null)
                    adCloseListener.onAdClosed();
            }
        });
    }

    public void loadBannerFragment(final Activity mActivity, final View rootView) {
        if (adControl.remove_ads()) {
            return;
        }
        ShimmerFrameLayout containerShimmer = rootView.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        FrameLayout frameLayout = rootView.findViewById(R.id.statapp_adplaceholder);
        frameLayout.removeAllViews();
        frameLayout.setVisibility(View.GONE);
        Banner startAppBanner = new Banner(mActivity, new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                frameLayout.setVisibility(View.VISIBLE);
                containerShimmer.setVisibility(View.GONE);
            }

            @Override
            public void onFailedToReceiveAd(View view) {
                frameLayout.setVisibility(View.GONE);
                containerShimmer.setVisibility(View.GONE);
            }

            @Override
            public void onImpression(View view) {

            }

            @Override
            public void onClick(View view) {

            }
        });
        startAppBanner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.addView(startAppBanner);
    }

    public void loadNative(final Activity mActivity) {
        if (adControl.remove_ads()) {
            return;
        }
        FrameLayout frameLayout = mActivity.findViewById(R.id.startapp_adplaceholder);
        frameLayout.setVisibility(View.GONE);
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) mActivity.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        startAppNativeAd = new StartAppNativeAd(context);
        NativeAdPreferences nativePrefs = new NativeAdPreferences()
                .setAdsNumber(1)                // Load 3 Native Ads
                .setAutoBitmapDownload(true)    // Retrieve Images object
                .setPrimaryImageSize(4);
        AdEventListener adListener = new AdEventListener() {     // Callback Listener
            @Override
            public void onReceiveAd(Ad arg0) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                FrameLayout adView = (FrameLayout) mActivity.getLayoutInflater()
                        .inflate(R.layout.item_start_app_native_ad, null);
                populateNativeAdView(startAppNativeAd.getNativeAds().get(0), adView);
                setAnimation(mActivity, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
                frameLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailedToReceiveAd(Ad arg0) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
            }
        };

        // Load Native Ads
        startAppNativeAd.loadAd(nativePrefs, adListener);
    }

    public void loadNativeFragment(final Activity mActivity, final View rootView) {
        if (adControl.remove_ads()) {
            return;
        }
        FrameLayout frameLayout = rootView.findViewById(R.id.startapp_adplaceholder);
        frameLayout.setVisibility(View.GONE);
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        startAppNativeAd = new StartAppNativeAd(context);
        NativeAdPreferences nativePrefs = new NativeAdPreferences()
                .setAdsNumber(1)                // Load 3 Native Ads
                .setAutoBitmapDownload(true)    // Retrieve Images object
                .setPrimaryImageSize(4);
        AdEventListener adListener = new AdEventListener() {     // Callback Listener
            @Override
            public void onReceiveAd(Ad arg0) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                FrameLayout adView = (FrameLayout) mActivity.getLayoutInflater()
                        .inflate(R.layout.item_start_app_native_ad, null);
                populateNativeAdView(startAppNativeAd.getNativeAds().get(0), adView);
                setAnimation(mActivity, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
                frameLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailedToReceiveAd(Ad arg0) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
            }
        };

        // Load Native Ads
        startAppNativeAd.loadAd(nativePrefs, adListener);
    }

    private void populateNativeAdView(NativeAdDetails nativeAd, FrameLayout adView) {
        ImageView icon;
        TextView title;
        TextView description;
        TextView button;

        icon = adView.findViewById(R.id.icon);
        title = adView.findViewById(R.id.title);
        description = adView.findViewById(R.id.description);
        button = adView.findViewById(R.id.ad_call_to_action);

        icon.setImageBitmap(nativeAd.getImageBitmap());
        title.setText(nativeAd.getTitle());
        description.setText(nativeAd.getDescription());
        button.setText(nativeAd.isApp() ? "Install" : "Open");
//        nativeAd.getRating()
        nativeAd.registerViewForInteraction(adView);
    }

    private void setAnimation(Activity mActivity, FrameLayout adView) {
        Animation animation = AnimationUtils.loadAnimation(mActivity.getBaseContext(), R.anim.move_up_button);
        Animation animation2 = AnimationUtils.loadAnimation(mActivity.getBaseContext(), R.anim.move_down_button);
        Animation animation3 = AnimationUtils.loadAnimation(mActivity.getBaseContext(), R.anim.delay_anim_button);
        TextView btnCallToAction = adView.findViewById(R.id.ad_call_to_action);
//        btnCallToAction.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnCallToAction.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        btnCallToAction.startAnimation(animation);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation1) {

            }

            @Override
            public void onAnimationEnd(Animation animation1) {
                btnCallToAction.startAnimation(animation3);
            }

            @Override
            public void onAnimationRepeat(Animation animation1) {
            }
        });
        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation1) {
            }

            @Override
            public void onAnimationEnd(Animation animation1) {
                btnCallToAction.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation1) {
            }
        });
    }
}
