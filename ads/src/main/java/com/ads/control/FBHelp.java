package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ads.control.AdControlHelp.AdCloseListener;
import com.ads.control.AdControlHelp.AdLoadedListener;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.ads.AudienceNetworkAds.TAG;

public class FBHelp {
    private static FBHelp instance;
    private InterstitialAd interstitialAd;
    AdView fbAdView;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;
    private NativeAd nativeAd;
    private AdControl adControl;
    private NativeBannerAd nativeBannerAd;

    public static FBHelp getInstance() {
        if (instance == null) {
            AdSettings.addTestDevice("8b0c27e6-7060-4f8f-b620-14cbe4367da7");
            instance = new FBHelp();
        }
        return instance;
    }

    private FBHelp() {
    }

    public void loadInterstitialAd(Context context, AdCloseListener adCloseListener, AdLoadedListener adLoadedListener) {
        adControl = AdControl.getInstance(context);
        destroyFullScreen();
        interstitialAd = new InterstitialAd(context, adControl.fb_full());
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                if (adCloseListener != null) {
                    adCloseListener.onAdClosed();
                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
//                if (isReloaded == false) {
//                    isReloaded = true;
////                    AdmobHelp.getInstance().init(context);
//                } else {
                if (adCloseListener != null)
                    adCloseListener.onAdClosed();
//                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
//                isReloaded = true;
                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded();
                }
                showInterstitialAd(adCloseListener);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });
        interstitialAd.loadAd();
    }

    private void destroyFullScreen() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
    }

    private void showInterstitialAd(AdCloseListener adCloseListener) {
        if (canShowInterstitialAd()) {
            interstitialAd.show();
        } else {
            adCloseListener.onAdClosed();
        }
    }

    private boolean canShowInterstitialAd() {
        return interstitialAd != null && interstitialAd.isAdLoaded();
    }

//    public void loadBanner(final Activity mActivity) {
//        adControl = AdControl.getInstance(mActivity.getBaseContext());
//        final ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) mActivity.findViewById(R.id.shimmer_container);
//
//        containerShimmer.setVisibility(View.VISIBLE);
//        containerShimmer.startShimmer();
//        fbAdView = new AdView(mActivity, adControl.fb_banner(), AdSize.BANNER_HEIGHT_50);
//
//        // Find the Ad Container
//        FrameLayout adContainer = (FrameLayout) mActivity.findViewById(R.id.fl_adplaceholder);
//        adContainer.setVisibility(View.GONE);
//
//        // Add the ad view to your activity layout
//        adContainer.addView(fbAdView);
//
//        // Request an ad
//
//        fbAdView.setAdListener(new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                containerShimmer.stopShimmer();
//                containerShimmer.setVisibility(View.GONE);
//                mActivity.findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
//                //Load admob
//                //     AMHelper.initAdm(mActivity);
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                containerShimmer.stopShimmer();
//                containerShimmer.setVisibility(View.GONE);
//                mActivity.findViewById(R.id.fl_adplaceholder).setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Ad clicked callback
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Ad impression logged callback
//            }
//        });
//        fbAdView.loadAd();
//    }

//    public void loadBannerFragment(final Activity mActivity, final View rootView) {
//        destroyBanner();
//        adControl = AdControl.getInstance(mActivity.getBaseContext());
//        final ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
//        containerShimmer.setVisibility(View.VISIBLE);
//        containerShimmer.startShimmer();
//        fbAdView = new AdView(mActivity, adControl.fb_banner(), AdSize.BANNER_HEIGHT_50);
//
//        // Find the Ad Container
//        FrameLayout adContainer = rootView.findViewById(R.id.fl_adplaceholder);
//        adContainer.setVisibility(View.GONE);
//
//        // Add the ad view to your activity layout
//        adContainer.addView(fbAdView);
//
//        // Request an ad
//
//        fbAdView.setAdListener(new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                containerShimmer.stopShimmer();
//                containerShimmer.setVisibility(View.GONE);
//                rootView.findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
//                //Load admob
//                //     AMHelper.initAdm(mActivity);
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                containerShimmer.stopShimmer();
//                containerShimmer.setVisibility(View.GONE);
//                rootView.findViewById(R.id.fl_adplaceholder).setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Ad clicked callback
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Ad impression logged callback
//            }
//        });
//        fbAdView.loadAd();
//    }

    private void destroyBanner() {
        if (fbAdView != null) {
            fbAdView.destroy();
        }
    }

    public void loadNative(final Activity mActivity) {
        adControl = AdControl.getInstance(mActivity.getBaseContext());
        nativeAd = new NativeAd(mActivity, adControl.fb_native());
        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                FrameLayout frameLayout =
                        mActivity.findViewById(R.id.native_ad_container);
                if (frameLayout != null) {
                    frameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                FrameLayout frameLayout = mActivity.findViewById(R.id.native_ad_container);
                if (frameLayout != null) {
                    frameLayout.setVisibility(View.VISIBLE);
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    inflateAd(nativeAd, mActivity);
                }
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });

        // Request an ad
        nativeAd.loadAd();
    }

    public void loadNativeFrament(final Activity mActivity, final View rootView) {
        final ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        final NativeAdLayout nativeAdLayout = rootView.findViewById(R.id.native_ad_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        adControl = AdControl.getInstance(mActivity.getBaseContext());
        nativeAd = new NativeAd(mActivity, adControl.fb_native());

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);

                if (nativeAdLayout != null) {
                    nativeAdLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                FrameLayout frameLayout = mActivity.findViewById(R.id.native_ad_container);
                if (frameLayout != null) {
                    frameLayout.setVisibility(View.VISIBLE);
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    inflateAd(nativeAd, mActivity);
                }
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });

        // Request an ad
        nativeAd.loadAd();
    }

    public void loadNativeBannerFragment(View rootView, Activity mActivity) {
        AudienceNetworkAds.initialize(mActivity.getBaseContext());
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        NativeAdLayout nativeAdLayout = rootView.findViewById(R.id.native_banner_ad_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        adControl = AdControl.getInstance(mActivity.getBaseContext());
        nativeBannerAd = new NativeBannerAd(mActivity, adControl.fb_banner());
        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);

                if (nativeAdLayout != null) {
                    nativeAdLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                if (nativeAdLayout != null) {
                    nativeAdLayout.setVisibility(View.VISIBLE);
                    if (nativeBannerAd == null || nativeBannerAd != ad) {
                        return;
                    }
                    inflateAdaptiveBannerAd(nativeBannerAd, mActivity);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });
        nativeBannerAd.loadAd();
    }

    private void inflateAdaptiveBannerAd(NativeBannerAd nativeBannerAd, Activity mActivity) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = mActivity.findViewById(R.id.native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(mActivity.getBaseContext());
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_banner_fb_ad_unit, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(mActivity.getBaseContext(), nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    private void inflateAd(NativeAd nativeAd, Activity mActivity) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = mActivity.findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_fb_ad_unit, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = mActivity.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(mActivity, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }

}
