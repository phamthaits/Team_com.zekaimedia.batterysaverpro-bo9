package com.ads.control;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ads.control.AdControlHelp.AdCloseListener;
import com.ads.control.AdControlHelp.AdLoadedListener;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.ads.mediation.facebook.FacebookExtras;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.AdChoicesView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;


public class AdmobHelp {
    private static AdmobHelp instance;
    private AdManagerInterstitialAd mAdManagerInterstitialAd;//Full
    private static AdControl adControl;
    private boolean loaddingInterstitialAd = false;
    public boolean canShowInterstitialAd = false;
    private boolean isReloadedFull = false;

    public static AdmobHelp getInstance(Activity value) {
        adControl = AdControl.getInstance(value);
        if (instance == null) {
            instance = new AdmobHelp();
        }
        return instance;
    }

    public AdmobHelp() {
    }

    AdManagerInterstitialAdLoadCallback adListenerEmpty = new AdManagerInterstitialAdLoadCallback() {
        @Override
        public void onAdLoaded(@NonNull AdManagerInterstitialAd adManagerInterstitialAd) {
            mAdManagerInterstitialAd = adManagerInterstitialAd;
            canShowInterstitialAd = true;
            loaddingInterstitialAd = false;
            Log.v("ads", "Ads Loaded");
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            loaddingInterstitialAd = false;
            canShowInterstitialAd = false;
        }
    };

    public void loadInterstitialAd(Activity activity, AdLoadedListener adLoadedListener) {
        Log.v("ads", "Call ads");
        AdManagerInterstitialAdLoadCallback adListener = new AdManagerInterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AdManagerInterstitialAd adManagerInterstitialAd) {
                mAdManagerInterstitialAd = adManagerInterstitialAd;
                loaddingInterstitialAd = false;
                canShowInterstitialAd = true;
                Log.v("ads", "Interstitial ad is loaded and ready to be displayed!");
                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded();
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.v("ads", "ads Fail");
                loaddingInterstitialAd = false;
                canShowInterstitialAd = false;
                if (!isReloadedFull) {
                    isReloadedFull = true;
                    loadInterstitialAd(activity, adControl.admob_full(), adListenerEmpty);
                }
                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded();
                }
            }
        };
        if (canShowInterstitialAd) {
            if (adLoadedListener != null) adLoadedListener.onAdLoaded();
        } else {
            isReloadedFull = false;
            loadInterstitialAd(activity, adControl.admob_full(), adListener);
        }
    }

    public void showInterstitialAd(Activity activity, AdCloseListener adCloseListener, String ads) {
        Log.v("ads", "Ads Call show ads");
        if (canShowInterstitialAd) {
            canShowInterstitialAd = false;
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    if (adCloseListener != null) {
                        adCloseListener.onAdClosed();
                    }
                    loadInterstitialAd(activity, ads, adListenerEmpty);
                }
            };
            mAdManagerInterstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
            mAdManagerInterstitialAd.show(activity);
        } else {
            loadInterstitialAd(activity, ads, adListenerEmpty);
            if (adCloseListener != null) {
                adCloseListener.onAdClosed();
            }
        }
    }

    private void loadInterstitialAd(Activity activity, String ads, AdManagerInterstitialAdLoadCallback adManagerInterstitialAdLoadCallback) {
        if (!canShowInterstitialAd && !loaddingInterstitialAd) {
            loaddingInterstitialAd = true;
            Log.v("ads", "Interstitial ad Loadding.");
            AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
            AdManagerInterstitialAd.load(activity, ads, adRequest, adManagerInterstitialAdLoadCallback);
        }
    }

    public void goneBanner(final View rootView) {
        FrameLayout frameLayout = rootView.findViewById(R.id.admob_adplaceholder);
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        containerShimmer.stopShimmer();
        containerShimmer.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        rootView.setVisibility(View.GONE);
    }

    public void loadBanner(Activity activity, final View rootView, String ads) {
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        AdView adView_Banner = new AdView(rootView.getContext());
        AdSize adSize = getAdSize(activity);
        adView_Banner.setAdSize(adSize);

        adView_Banner.setAdUnitId(ads);
        FrameLayout frameLayout = rootView.findViewById(R.id.admob_adplaceholder);
        frameLayout.setVisibility(View.GONE);
        frameLayout.removeAllViews();
        frameLayout.addView(adView_Banner);
        adView_Banner.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                goneBanner(rootView);
            }

            @Override
            public void onAdLoaded() {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });
        adView_Banner.loadAd(new AdRequest.Builder().build());
    }

    public void goneNative(  final LinearLayout rootView) {
        rootView.setVisibility(View.GONE);
    }

    public void loadNative(final Activity mActivity, final LinearLayout rootView, AdControl.NativeBundle nativeBundle) {
        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) mActivity.getLayoutInflater().inflate(nativeBundle.loading_layout_resource, null);
        rootView.addView(shimmerFrameLayout);
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        FrameLayout frameLayout = rootView.findViewById(R.id.admob_adplaceholder);
        frameLayout.setVisibility(View.GONE);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        // You must call destroy on old ads when you are done with them,
        // otherwise you will have a memory leak.

        NativeAdView adView = (NativeAdView) mActivity.getLayoutInflater().inflate(nativeBundle.admob_layout_resource, null);
        AdLoader.Builder builder = new AdLoader.Builder(mActivity, nativeBundle.admob_ads);
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd ad) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                try {
                    populateNativeAdView(mActivity, ad, adView, nativeBundle.is_native_banner);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                    frameLayout.setVisibility(View.VISIBLE);
                    if (nativeBundle.isAnimationButton)
                        setAnimation(mActivity, adView);
                } catch (Exception e) {
                    frameLayout.setVisibility(View.GONE);
                }
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError errorCode) {
                ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
               goneNative(rootView);
            }
        }).build();
        Bundle extras = new FacebookExtras()
                .setNativeBanner(nativeBundle.is_native_banner)
                .build();

        AdRequest request = new AdRequest.Builder()
                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                .build();
        adLoader.loadAd(request);
    }

    private void populateNativeAdView(Activity activity, NativeAd nativeAd, NativeAdView adView, boolean is_native_banner) {
        // Set the media view.
        if (!is_native_banner)
            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setAdChoicesView(adView.findViewById(R.id.adchoices_view));
        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (!is_native_banner)
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setBackground(activity.getResources().getDrawable(R.drawable.ic_home_ads));
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdChoicesInfo() == null) {
            adView.getAdChoicesView().setVisibility(View.GONE);
        } else {
            AdChoicesView choicesView = new AdChoicesView(adView.getContext());
            adView.setAdChoicesView(choicesView);
            adView.getAdChoicesView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        // Updates the UI to say whether or not this ad has a video asset.
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

    private void setAnimation(Activity mActivity, NativeAdView adView) {
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
}
