package app.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.ads.control.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

import static com.mopub.common.logging.MoPubLog.LogLevel.NONE;

public class MopubHelp {
    private static MopubHelp instance;
    private static Context context;
    private static AdControl adControl;
    private MoPubInterstitial mInterstitial;

    public static MopubHelp getInstance(Context value) {
        context = value;
        adControl = AdControl.getInstance(value);
        if (instance == null) {
            instance = new MopubHelp();
        }
        return instance;
    }

    public MopubHelp() {
    }

    private MoPubInterstitial.InterstitialAdListener adListenerEmpty = new MoPubInterstitial.InterstitialAdListener() {
        @Override
        public void onInterstitialLoaded(MoPubInterstitial interstitial) {

        }

        @Override
        public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {

        }

        @Override
        public void onInterstitialShown(MoPubInterstitial interstitial) {

        }

        @Override
        public void onInterstitialClicked(MoPubInterstitial interstitial) {

        }

        @Override
        public void onInterstitialDismissed(MoPubInterstitial interstitial) {

        }
    };

    public void loadInterstitialAd(Activity activity, AdControlHelp.AdCloseListener adCloseListener, AdControlHelp.AdLoadedListener adLoadedListener, String ads, boolean showWhenLoaded) {
        MoPubInterstitial.InterstitialAdListener interstitialAdListener = new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded();
                }
                if (showWhenLoaded && adControl.isStillShowAds) {
                    showInterstitialAd(adCloseListener);
                }
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                if (showWhenLoaded && adControl.isStillShowAds) {
                    if (adCloseListener != null)
                        adCloseListener.onAdClosed();
                }
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial) {

            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {

            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial) {
                if (adCloseListener != null) {
                    adCloseListener.onAdClosed();
                }
                mInterstitial.setInterstitialAdListener(adListenerEmpty);
                loadInterstitialAd();
            }
        };

        if (canShowInterstitialAd()) {
            mInterstitial.setInterstitialAdListener(interstitialAdListener);
            if (adLoadedListener != null) adLoadedListener.onAdLoaded();
            if (showWhenLoaded) {
                showInterstitialAd(adCloseListener);
            }
        } else {
            SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(ads)
                    .withLogLevel(MoPubLog.LogLevel.DEBUG)
                    .withLegitimateInterestAllowed(false)
                    .build();

            MoPub.initializeSdk(context, sdkConfiguration, new SdkInitializationListener() {
                @Override
                public void onInitializationFinished() {
                    Log.d("Mopub", "SDK initialized");
                    mInterstitial = new MoPubInterstitial(activity, ads);
                    mInterstitial.setInterstitialAdListener(interstitialAdListener);
                    loadInterstitialAd();
                }
            });
        }
    }

    private boolean canShowInterstitialAd() {
        return mInterstitial != null && mInterstitial.isReady();
    }

    private void loadInterstitialAd() {

        Log.v("ads", "ads get Request");
        if (mInterstitial != null && !mInterstitial.isReady()) {
            mInterstitial.load();
        }
    }

    public void showInterstitialAd(AdControlHelp.AdCloseListener adCloseListener) {
        Log.v("ads", "Ads Call show ads");
        if (mInterstitial.isReady()) {
            mInterstitial.show();
        } else {
            if (adCloseListener != null) {
                adCloseListener.onAdClosed();
            }
        }
    }

    public void loadBanner(View rootView, String ads) {
        FrameLayout frameLayout = rootView.findViewById(R.id.mopub_adplaceholder);
        frameLayout.setVisibility(View.GONE);
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder(ads);
        configBuilder.withLogLevel(NONE);
        MoPub.initializeSdk(context, configBuilder.build(), new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                MoPubView moPubView  =  rootView.findViewById(R.id.adview);
                moPubView.setAdUnitId(ads);
                moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
                    @Override
                    public void onBannerLoaded(@NonNull MoPubView banner) {
                        containerShimmer.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                        containerShimmer.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onBannerClicked(MoPubView banner) {

                    }

                    @Override
                    public void onBannerExpanded(MoPubView banner) {

                    }

                    @Override
                    public void onBannerCollapsed(MoPubView banner) {

                    }
                });
                moPubView.loadAd();
            }
        });
    }

    public void loadNative(final View rootView, String ads) {
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        FrameLayout frameLayout = rootView.findViewById(R.id.mopub_adplaceholder);
        frameLayout.removeAllViews();
        frameLayout.setVisibility(View.GONE);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(NativeAd nativeAd) {
                AdapterHelper adapterHelper = new AdapterHelper(context, 0, 3);
                View v = adapterHelper.getAdView(null, frameLayout, nativeAd, new ViewBinder.Builder(0).build());
                // Set the native event listeners (onImpression, and onClick).
                nativeAd.setMoPubNativeEventListener(new NativeAd.MoPubNativeEventListener() {
                    @Override
                    public void onImpression(View view) {
                    }

                    @Override
                    public void onClick(View view) {
                    }
                });
                frameLayout.addView(v);
                frameLayout.setVisibility(View.VISIBLE);
                containerShimmer.setVisibility(View.GONE);
                setAnimation(frameLayout);
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                frameLayout.setVisibility(View.GONE);
                containerShimmer.setVisibility(View.GONE);
            }
        };
        SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder(ads);
        configBuilder.withLogLevel(NONE);
        MoPub.initializeSdk(context, configBuilder.build(), new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                MoPubNative moPubNative = new MoPubNative(context, ads, moPubNativeNetworkListener);
                ViewBinder viewBinder = new ViewBinder.Builder(R.layout.item_mopub_native_ad)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image)
                        .titleId(R.id.native_ad_title)
                        .textId(R.id.native_text)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .sponsoredTextId(R.id.native_sponsored_text_view)
                        .callToActionId(R.id.native_cta)
                        .build();
                MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
                moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);
                moPubNative.makeRequest();
            }
        });
    }

    private void setAnimation(FrameLayout adView) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.move_up_button);
        Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.move_down_button);
        Animation animation3 = AnimationUtils.loadAnimation(context, R.anim.delay_anim_button);
        Button btnCallToAction = adView.findViewById(R.id.native_cta);
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
