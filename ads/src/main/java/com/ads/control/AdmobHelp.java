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

import com.facebook.shimmer.ShimmerFrameLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

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

    public void loadInterstitialAd(AdCloseListener adCloseListener, AdLoadedListener adLoadedListener, String ads, boolean showWhenLoaded) {
        Log.v("ads", "Call ads");
        if (adControl.remove_ads()) {
            if (showWhenLoaded)
                adCloseListener.onAdClosed();
            return;
        }
        AdListener adListener = new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.v("ads", "Ads Loaded");
                isReloaded = true;
                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded();
                }
                if (showWhenLoaded)
                    showInterstitialAd(adCloseListener);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.v("ads", "ads Fail");
                if (showWhenLoaded) {
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
            if (showWhenLoaded) {
                Log.v("ads", "showWhenLoaded Call show ads");
                showInterstitialAd(adCloseListener);
            } else {
                mPublisherInterstitialAd.setAdListener(adListener);
            }
            return;
        }

        MobileAds.initialize(context);
        mPublisherInterstitialAd = new PublisherInterstitialAd(context);
        mPublisherInterstitialAd.setAdUnitId(ads);
        mPublisherInterstitialAd.setAdListener(adListener);
        loadInterstitialAd();
    }

    public void showInterstitialAd(AdCloseListener adCloseListener) {
        Log.v("ads", "Ads Call show ads");
        if (canShowInterstitialAd()) {
            mPublisherInterstitialAd.show();
        } else {
            if (adCloseListener != null) {
                adCloseListener.onAdClosed();
            }
        }
    }

    public void loadBannerFragment(Activity activity, final View rootView, String ads) {
        if (adControl.remove_ads()) {
            return;
        }
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
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
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

    public void loadNative(final Activity mActivity, String ads) {
        if (adControl.remove_ads()) {
            return;
        }
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) mActivity.findViewById(R.id.shimmer_container);
        FrameLayout frameLayout = mActivity.findViewById(R.id.admob_adplaceholder);
        frameLayout.setVisibility(View.GONE);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        AdLoader adLoader = new AdLoader.Builder(mActivity, ads)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
//                        if (nativeAd != null) {
//                            nativeAd.destroy();
//                        }

//                        nativeAd = unifiedNativeAd;
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                        if (frameLayout != null) {
                            frameLayout.setVisibility(View.VISIBLE);
                            UnifiedNativeAdView adView = (UnifiedNativeAdView) mActivity.getLayoutInflater()
                                    .inflate(R.layout.native_admob_ad_unit, null);
                            populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
//                            setAnimation(mActivity, adView);
                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(adOptions)
                .build();

        adLoader.loadAd(new PublisherAdRequest.Builder().build());
    }

    public void loadNativeFragment(final Activity mActivity, final View rootView, String ads) {
        if (adControl.remove_ads()) {
            return;
        }
        ShimmerFrameLayout containerShimmer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        FrameLayout frameLayout = rootView.findViewById(R.id.admob_adplaceholder);
        frameLayout.setVisibility(View.GONE);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
//        destroyNative();
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        AdLoader adLoader = new AdLoader.Builder(mActivity, ads).forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (frameLayout != null) {
                    frameLayout.setVisibility(View.VISIBLE);
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) mActivity.getLayoutInflater()
                            .inflate(R.layout.native_admob_ad_unit, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                    frameLayout.setVisibility(View.VISIBLE);
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    setAnimation(mActivity, adView);
                }
            }
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                // Handle the failure by logging, altering the UI, and so on.
            }
        }).withNativeAdOptions(adOptions).build();
        adLoader.loadAd(new PublisherAdRequest.Builder().build());
    }

    public boolean is_reload_firebase() {
           return true;
//        return adControl.old_date() != Calendar.getInstance().get(Calendar.DAY_OF_MONTH) || !adControl.isInit();
    }

    public void getAdControlFromFireBase() {
        Log.v("ads","Load Firebase");
        AdControl adControl = AdControl.getInstance(context);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("AdTest")
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
                                        case "admob_full":
                                            adControl.admob_full(object.getString(key));
                                            break;
                                        case "admob_native":
                                            adControl.admob_native(object.getString(key));
                                            break;
                                        case "admob_banner":
                                            adControl.admob_banner(object.getString(key));
                                            break;
                                    }
                                    Log.d("ads", "key = " + key + ":" + object.getString(key));
                                    adControl.isInit(true);
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
                });
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

    public interface AdCloseListener {
        void onAdClosed();
    }

    public interface AdLoadedListener {
        void onAdLoaded();
    }

//    public void destroyNative() {
//        if (nativeAd != null) {
//            nativeAd.destroy();
//        }
//    }
}
