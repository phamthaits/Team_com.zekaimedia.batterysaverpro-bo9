package app.ads.control;

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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ads.control.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdmobHelp {
    private static AdmobHelp instance;
    private PublisherInterstitialAd mPublisherInterstitialAd;//Full
    private static boolean isReloaded = false;
    private static Context context;
    private static AdControl adControl;

    public static AdmobHelp getInstance(Context value) {
        isReloaded = false;
        context = value;
        adControl = AdControl.getInstance(value);
        if (instance == null) {
            instance = new AdmobHelp();
        }
        return instance;
    }

    public AdmobHelp() {
    }

    private AdListener adListenerEmpty = new AdListener() {
        @Override
        public void onAdLoaded() {
            Log.v("ads", "Ads Loaded");
            isReloaded = true;
        }
    };

    public void loadInterstitialAd(AdControlHelp.AdCloseListener adCloseListener, AdControlHelp.AdLoadedListener adLoadedListener, String ads, boolean showWhenLoaded) {
        Log.v("ads", "Call ads");
        adControl.isStillShowAds = true;
        AdListener adListener = new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.v("ads", "Ads Loaded");
                isReloaded = true;
                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded();
                }
                if (showWhenLoaded && adControl.isStillShowAds) {
                    showInterstitialAd(adCloseListener);
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.v("ads", "ads Fail");
                if (showWhenLoaded && adControl.isStillShowAds) {
                    if (adCloseListener != null)
                        adCloseListener.onAdClosed();
                } else {
                    if (isReloaded == false) {
                        isReloaded = true;
                        loadInterstitialAd();
                    }
                }
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                if (adCloseListener != null) {
                    adCloseListener.onAdClosed();
                }
                mPublisherInterstitialAd.setAdListener(adListenerEmpty);
                loadInterstitialAd();
            }
        };

        if (canShowInterstitialAd()) {
            mPublisherInterstitialAd.setAdListener(adListener);
            if (adLoadedListener != null) adLoadedListener.onAdLoaded();
            if (showWhenLoaded) {
                showInterstitialAd(adCloseListener);
            }
        } else {
            MobileAds.initialize(context);
            mPublisherInterstitialAd = new PublisherInterstitialAd(context);
            mPublisherInterstitialAd.setAdListener(adListener);
            mPublisherInterstitialAd.setAdUnitId(ads);
            loadInterstitialAd();
        }
    }

    public void showInterstitialAd(AdControlHelp.AdCloseListener adCloseListener) {
        Log.v("ads", "Ads Call show ads");
        if (canShowInterstitialAd()) {
            mPublisherInterstitialAd.show();
        } else {
            if (adCloseListener != null) {
                adCloseListener.onAdClosed();
            }
        }
    }

    private void loadInterstitialAd() {
        Log.v("ads", "ads get Request");
        if (mPublisherInterstitialAd != null && !mPublisherInterstitialAd.isLoading() && !mPublisherInterstitialAd.isLoaded()) {
            PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
            mPublisherInterstitialAd.loadAd(adRequest);
        }
    }

    private boolean canShowInterstitialAd() {
        return mPublisherInterstitialAd != null && mPublisherInterstitialAd.isLoaded();
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
        adView_Banner.loadAd(new AdRequest.Builder().build());
        adView_Banner.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });

    }

    public void loadNative(final Activity mActivity, final LinearLayout rootView, String ads,
                           int admob_layout_resource, boolean isAnimButton, boolean is_native_banner) {

        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) mActivity.getLayoutInflater().inflate(R.layout.load_native, null);
        if (is_native_banner) {
            shimmerFrameLayout = (ShimmerFrameLayout) mActivity.getLayoutInflater().inflate(R.layout.load_banner, null);
        }
        rootView.addView(shimmerFrameLayout);

        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        FrameLayout frameLayout = rootView.findViewById(R.id.admob_adplaceholder);
        frameLayout.setVisibility(View.GONE);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdLoader.Builder builder = new AdLoader.Builder(context, ads);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                UnifiedNativeAdView adView = (UnifiedNativeAdView) mActivity.getLayoutInflater()
                        .inflate(admob_layout_resource, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
                frameLayout.setVisibility(View.VISIBLE);
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                if (isAnimButton)
                    setAnimation(mActivity, adView);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError errorCode) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
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

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

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
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
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

//    public void destroyNative() {
//        if (nativeAd != null) {
//            nativeAd.destroy();
//        }
//    }
}
