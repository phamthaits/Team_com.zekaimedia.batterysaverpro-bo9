package app.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ads.control.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.ads.AudienceNetworkAds.TAG;

public class FBHelp {
    private static FBHelp instance;
    private InterstitialAd interstitialAd;
    private static boolean isReloaded = false;
    private static Context context;
    private static AdControl adControl;

    public static FBHelp getInstance(Context value) {
        isReloaded = false;
        context = value;
        if (instance == null) {
            instance = new FBHelp();
        }
        adControl = AdControl.getInstance(context);
        return instance;
    }

    private InterstitialAdListener adListenerEmpty = new InterstitialAdListener() {
        @Override
        public void onInterstitialDisplayed(Ad ad) {
            Log.e(TAG, "Interstitial ad displayed.");
        }

        @Override
        public void onInterstitialDismissed(Ad ad) {
        }

        @Override
        public void onError(Ad ad, AdError adError) {
            Log.v("ads", "onAdFailedToLoad");
            if (isReloaded == false) {
                isReloaded = true;
                loadInterstitialAd();
            }
        }

        @Override
        public void onAdLoaded(Ad ad) {
            // Interstitial ad is loaded and ready to be displayed
            Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
            isReloaded = true;
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
    };

    public void loadInterstitialAd(AdControlHelp.AdCloseListener adCloseListener, AdControlHelp.AdLoadedListener adLoadedListener, String ads, boolean showWhenLoaded) {
        adControl.isStillShowAds = true;
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
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
                interstitialAd.buildLoadAdConfig().withAdListener(adListenerEmpty).build();
                loadInterstitialAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.v("ads", "onAdFailedToLoad");
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
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                isReloaded = true;
                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded();
                }
                if (showWhenLoaded && adControl.isStillShowAds)
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
        };
        if (canShowInterstitialAd()) {
            interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build();
            if (adLoadedListener != null) adLoadedListener.onAdLoaded();
            if (showWhenLoaded) {
                showInterstitialAd(adCloseListener);
            }
        } else {
            interstitialAd = new InterstitialAd(context, ads);
            interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build();
            loadInterstitialAd();
        }
    }

    private void loadInterstitialAd() {
        if (interstitialAd != null && !interstitialAd.isAdLoaded()) {
            interstitialAd.loadAd();
        }
    }

    public void showInterstitialAd(AdControlHelp.AdCloseListener adCloseListener) {
        Log.v("ads", "Đã gọi Show");
        if (canShowInterstitialAd()) {
            Log.v("ads", "Show");
            interstitialAd.show();

        } else {
            Log.v("ads", "NotShow");
            if (adCloseListener != null)
                adCloseListener.onAdClosed();
        }
    }

    private boolean canShowInterstitialAd() {
        return interstitialAd != null && interstitialAd.isAdLoaded();
    }

    public void destroyInterstitial() {
        if (interstitialAd != null)
            interstitialAd.destroy();
    }

    private void destroyBanner() {
        if (fbAdView != null)
            fbAdView.destroy();
    }

    private AdView fbAdView;

    public void loadBanner(final Activity mActivity, final View rootView, String ads) {
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        destroyBanner();
        fbAdView = new AdView(mActivity, ads, AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        FrameLayout adContainer = rootView.findViewById(R.id.fb_adplaceholder);
        adContainer.removeAllViews();
        adContainer.setVisibility(View.GONE);

        // Add the ad view to your activity layout
        adContainer.addView(fbAdView);

        // Request an ad
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                rootView.findViewById(R.id.fb_adplaceholder).setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                rootView.findViewById(R.id.fb_adplaceholder).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };
        fbAdView.buildLoadAdConfig().withAdListener(adListener).build();
        fbAdView.loadAd();
    }

    private void setAnimation(LinearLayout adView) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.move_up_button);
        Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.move_down_button);
        Animation animation3 = AnimationUtils.loadAnimation(context, R.anim.delay_anim_button);
        Button btnCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
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

    public void loadNative(final Activity mActivity, final LinearLayout rootView, String ads,
                           int fb_layout_resource, boolean isAnimButton, boolean is_native_banner) {
        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) mActivity.getLayoutInflater().inflate(R.layout.load_native, null);
        if (is_native_banner) {
            shimmerFrameLayout = (ShimmerFrameLayout) mActivity.getLayoutInflater().inflate(R.layout.load_banner, null);
        }
        rootView.addView(shimmerFrameLayout);
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        NativeAdLayout nativeAdLayout = rootView.findViewById(R.id.fb_adplaceholder);
        nativeAdLayout.setVisibility(View.GONE);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        NativeAd nativeAd = new NativeAd(rootView.getContext(), ads);
        nativeAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
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

                if (containerShimmer != null) {
                    containerShimmer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                FrameLayout frameLayout = rootView.findViewById(R.id.fb_adplaceholder);
                if (frameLayout != null) {
                    frameLayout.setVisibility(View.VISIBLE);
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    inflateAd(nativeAd, mActivity, nativeAdLayout, fb_layout_resource, isAnimButton);
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
        }).build();

        // Request an ad
        nativeAd.loadAd();
    }

    private void inflateAd(NativeAd nativeAd, Activity mActivity, NativeAdLayout nativeAdLayout,
                           int fb_layout_resource, boolean isAnimButton) {
        nativeAd.unregisterView();
        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(fb_layout_resource, nativeAdLayout, false);
        nativeAdLayout.removeAllViews();
        nativeAdLayout.addView(adView);
        if (isAnimButton) {
            setAnimation(adView);
        }

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = mActivity.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(mActivity, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
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
        if ((nativeAdMedia == null) || (nativeAdBody == null))
            nativeAd.registerViewForInteraction(
                    adView,
                    nativeAdIcon,
                    clickableViews);
        else nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }
}
