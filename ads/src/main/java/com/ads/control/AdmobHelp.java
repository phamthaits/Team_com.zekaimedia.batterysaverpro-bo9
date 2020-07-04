package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ads.control.AdControl.AdCloseListener;
import com.ads.control.AdControl.AdLoadedListener;

import org.json.JSONException;
import org.json.JSONObject;

public class AdmobHelp {
    private static AdmobHelp instance;
    PublisherInterstitialAd mPublisherInterstitialAd;
    private AdCloseListener adCloseListener;
    private boolean isReloaded = false;
    private UnifiedNativeAd nativeAd;
    public static boolean isInit = false;
//    private TypeAds typeAds;

    public static AdmobHelp getInstance() {
        if (instance == null) {
            instance = new AdmobHelp();
        }
        return instance;
    }

    private AdmobHelp() {
    }

    public void loadInterstitialAd(Context context, TypeAds typeAds, AdCloseListener adCloseListener, AdLoadedListener adLoadedListener) {
        isInit = true;
        MobileAds.initialize(context, context.getString(R.string.admob_app_id));
        mPublisherInterstitialAd = new PublisherInterstitialAd(context);
        String ads = "";
        switch (typeAds) {
            case admod_full_splash:
                ads = AdControl.admod_full_splash;
                break;
            case admod_full_optimization:
                ads = AdControl.admod_full_optimization;
                break;
            case admod_full_trashcleaner:
                ads = AdControl.admod_full_trashcleaner;
                break;
            case admod_full_phoneboost:
                ads = AdControl.admod_full_phoneboost;
                break;
            case admod_full_phonecooler:
                ads = AdControl.admod_full_phonecooler;
                break;
            case admod_full_fullcharge:
                ads = AdControl.admod_full_fullcharge;
                break;
            case admod_full_fastcharge:
                ads = AdControl.admod_full_fastcharge;
                break;
            case admod_banner_appmanager:
                ads = AdControl.admod_banner_appmanager;
                break;
            case admod_banner_chargehistory:
                ads = AdControl.admod_banner_chargehistory;
                break;
        }
        mPublisherInterstitialAd.setAdUnitId(ads);
        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded();
                }
                // Code to be executed when an ad finishes loading.
                showInterstitialAd(adCloseListener);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if (isReloaded == false) {
                    isReloaded = true;
                    loadInterstitialAd();
                } else {
                    if (adCloseListener != null)
                        adCloseListener.onAdClosed();
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                if (adCloseListener != null) {
                    adCloseListener.onAdClosed();
                }
            }
        });
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        if (mPublisherInterstitialAd != null && !mPublisherInterstitialAd.isLoading() && !mPublisherInterstitialAd.isLoaded()) {
            PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
            mPublisherInterstitialAd.loadAd(adRequest);
        }
    }

    private void showInterstitialAd(AdCloseListener adCloseListener) {
        if (canShowInterstitialAd()) {
            this.adCloseListener = adCloseListener;
            mPublisherInterstitialAd.show();
        } else {
            adCloseListener.onAdClosed();
        }
    }

    private boolean canShowInterstitialAd() {
        return mPublisherInterstitialAd != null && mPublisherInterstitialAd.isLoaded();
    }



    private void setAnimation(Activity mActivity, UnifiedNativeAdView adView) {
        Animation animation = AnimationUtils.loadAnimation(mActivity.getBaseContext(), R.anim.move_up_button);
        Animation animation2 = AnimationUtils.loadAnimation(mActivity.getBaseContext(), R.anim.move_down_button);
        Animation animation3 = AnimationUtils.loadAnimation(mActivity.getBaseContext(), R.anim.delay_anim_button);
        Button btnCallToAction = adView.findViewById(R.id.ad_call_to_action);
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

    public void loadNative(final Activity mActivity, TypeAds typeAds) {

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        String ads = "";
        switch (typeAds) {
            case admod_native_main:
                ads = AdControl.admod_native_main;
                break;
            case admod_native_optimization:
                ads = AdControl.admod_native_optimization;
                break;
            case admod_native_trashcleaner:
                ads = AdControl.admod_native_trashcleaner;
                break;
            case admod_native_phoneboost:
                ads = AdControl.admod_native_phoneboost;
                break;
            case admod_native_phonecooler:
                ads = AdControl.admod_native_phonecooler;
                break;
            case admod_native_fullcharge:
                ads = AdControl.admod_native_fullcharge;
                break;
            case admod_native_fastcharge:
                ads = AdControl.admod_native_fastcharge;
                break;
            case admod_native_setting:
                ads = AdControl.admod_native_setting;
                break;
            case admod_native_chargeresult:
                ads = AdControl.admod_native_chargeresult;
                break;
        }

        AdLoader adLoader = new AdLoader.Builder(mActivity, ads)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }

                        nativeAd = unifiedNativeAd;
                        FrameLayout frameLayout =
                                mActivity.findViewById(R.id.fl_adplaceholder);
                        if (frameLayout != null) {
                            frameLayout.setVisibility(View.VISIBLE);
                            UnifiedNativeAdView adView = (UnifiedNativeAdView) mActivity.getLayoutInflater()
                                    .inflate(R.layout.native_admob_ad, null);
                            populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                            setAnimation(mActivity, adView);
                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new PublisherAdRequest.Builder().build());
    }

    public void loadNativeFragment(final Activity mActivity, final View rootView, TypeAds typeAds) {
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();
        String ads = "";
        switch (typeAds) {
            case admod_native_main:
                ads = AdControl.admod_native_main;
                break;
            case admod_native_optimization:
                ads = AdControl.admod_native_optimization;
                break;
            case admod_native_trashcleaner:
                ads = AdControl.admod_native_trashcleaner;
                break;
            case admod_native_phoneboost:
                ads = AdControl.admod_native_phoneboost;
                break;
            case admod_native_phonecooler:
                ads = AdControl.admod_native_phonecooler;
                break;
            case admod_native_fullcharge:
                ads = AdControl.admod_native_fullcharge;
                break;
            case admod_native_fastcharge:
                ads = AdControl.admod_native_fastcharge;
                break;
            case admod_native_setting:
                ads = AdControl.admod_native_setting;
                break;
        }
        AdLoader adLoader = new AdLoader.Builder(mActivity, ads).forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // Show the ad.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout =
                        rootView.findViewById(R.id.fl_adplaceholder);
                if (frameLayout != null) {
                    frameLayout.setVisibility(View.VISIBLE);
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) mActivity.getLayoutInflater()
                            .inflate(R.layout.native_admob_ad, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                    setAnimation(mActivity, adView);
                }
            }
        })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new PublisherAdRequest.Builder().build());
    }

    public void loadBannerFragment(final View rootView, TypeAds typeAds, Activity activity) {
        String ads = "";
        try {
            FrameLayout frameLayout = rootView.findViewById(R.id.fl_adplaceholder);
            AdView adView = new AdView(rootView.getContext());
            switch (typeAds) {
                case admod_banner_appmanager:
                    ads = AdControl.admod_banner_appmanager;
                    break;
                case admod_banner_chargehistory:
                    ads = AdControl.admod_banner_chargehistory;
                    break;
            }
            AdSize adSize = getAdSize(activity);
            adView.setAdSize(adSize);

            adView.setAdUnitId(ads);
            frameLayout.addView(adView);
            adView.loadAd(new AdRequest.Builder().build());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    frameLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    frameLayout.setVisibility(View.VISIBLE);
                }
            });

        } catch (Exception e) {
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        Button btnCallToAction = adView.findViewById(R.id.ad_call_to_action);
        adView.setCallToActionView(btnCallToAction);
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

    }

    public void destroyNative() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
    }

    private AdSize getAdSize(Activity activity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }
}
