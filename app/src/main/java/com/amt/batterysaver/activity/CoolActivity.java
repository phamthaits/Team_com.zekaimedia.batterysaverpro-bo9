package com.amt.batterysaver.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ServiceInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ads.control.AdControlHelp;
import com.ads.control.AdControlHelp.AdCloseListener;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.model.TaskInfo;
import com.skyfishjy.library.RippleBackground;
import com.amt.batterysaver.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CoolActivity extends AppCompatActivity implements View.OnClickListener {

    private int curIndex = 0;
    private int[] arrGravity1 = new int[]{49, 19, 83};
    private int[] arrGravity2 = new int[]{51, 49, 21};
    private int[] arrGravity3 = new int[]{53, 21, 81};
    private int[] arrGravity4 = new int[]{85, 81, 19};
    private int[][] arrGravitys = new int[][]{this.arrGravity1, this.arrGravity2, this.arrGravity3, this.arrGravity4};
    private FrameLayout.LayoutParams layoutParams;
    private FrameLayout[] chargeBoostContainers = new FrameLayout[4];
    private PackageManager mPackageManager;
    ActivityManager mActivityManager;
    ImageView rocketImage, rocketImageOut;

    private TextView tvResult;

    private ImageView ivDone;
    private Animation ivDoneAnim;

    private ViewGroup parentAds;
    private LinearLayout lrScan;
    private Context context;
    private AdControlHelp adControlHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooler);
        Utils.setLocate(this);
        new CoolActivity.LoadRunningTask().execute();
        intView();
        context = this;
//        checkTask();
        adControlHelp=AdControlHelp.getInstance(context);
        adControlHelp.loadNative(this);
        adControlHelp.loadInterstitialAd(this, adCloseListener, null, false);
//        SharePreferenceUtils.getInstance(this).setFlagAds(true);
    }

    private AdCloseListener adCloseListener = new AdCloseListener() {
        @Override
        public void onAdClosed() {
            loadResult();
        }
    };

    public void checkTask() {
        if (!Utils.checkShouldDoing(this, 6)) {
            findViewById(R.id.cvBoost).setVisibility(View.GONE);
        }
        if (!Utils.checkShouldDoing(this, 7)) {
            findViewById(R.id.cvCool).setVisibility(View.GONE);
        }
        if (!Utils.checkShouldDoing(this, 3)) {
            findViewById(R.id.cvClean).setVisibility(View.GONE);
        }
//        if (!Utils.checkShouldDoing(this, 8)) {
//            findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//
//        } else {
//            if (SharePreferenceUtils.getInstance(this).getFsAutoRun())
//                findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lr_back:
                finish();
                return;
//            case R.id.ivClose:
//                SharePreferenceUtils.getInstance(this).setHideChargeView(System.currentTimeMillis());
//                findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//                return;
//
//            case R.id.btnOk:
//                SharePreferenceUtils.getInstance(this).setFsAutoRun(true);
//                findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//
//                return;
            case R.id.rlBoost:
                startActivity(new Intent(this, BoostActivity.class));
                finish();
                return;
            case R.id.rlClean:
                startActivity(new Intent(this, CleanActivity.class));
                finish();
                return;
            case R.id.rlCool:
                startActivity(new Intent(this, CoolActivity.class));
                finish();
                return;
            default:
                return;
        }
    }

    public void intView() {
        lrScan = findViewById(R.id.lrScan);
        parentAds = findViewById(R.id.fmResult);
        this.chargeBoostContainers[0] = findViewById(R.id.fm_scan_container_1);
        this.chargeBoostContainers[1] = findViewById(R.id.fm_scan_container_2);
        this.chargeBoostContainers[2] = findViewById(R.id.fm_scan_container_3);
        this.chargeBoostContainers[3] = findViewById(R.id.fm_scan_container_4);
        this.rocketImage = findViewById(R.id.ivScan);
        this.rocketImageOut = findViewById(R.id.ivDoneHoloCirular);
        this.tvResult = findViewById(R.id.clean_up_done_tv_result);
        this.ivDone = findViewById(R.id.clean_done_iv_done);
        this.ivDoneAnim = AnimationUtils.loadAnimation(this, R.anim.ic_done_anim);
//        if (Utils.checkShouldDoing(this, 7)) {
        int size = (int) getResources().getDimension(R.dimen.icon_size);
        this.layoutParams = new FrameLayout.LayoutParams(-2, -2);
        this.layoutParams.height = size;
        this.layoutParams.width = size;
        this.layoutParams.gravity = 17;
        Animation animationRotate = AnimationUtils.loadAnimation(this, R.anim.rote_charge_anim);
        this.rocketImage.startAnimation(animationRotate);
        animationRotate.start();
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.rote_charge_anim_out);
        this.rocketImageOut.startAnimation(loadAnimation);
        loadAnimation.start();
        initRippleBackgound();
        this.tvResult.setVisibility(View.VISIBLE);
        animationRotate.setAnimationListener(new CoolActivity.anmRotate());
        this.ivDoneAnim.setAnimationListener(new CoolActivity.anmDone());
        findViewById(R.id.iv_bg_snow).startAnimation(AnimationUtils.loadAnimation(this, R.anim.snow_fall));
//        } else {
//            findViewById(R.id.lrScan).setVisibility(View.GONE);
//            this.parentAds.setAlpha(0.0f);
//            this.parentAds.setVisibility(View.VISIBLE);
//            this.parentAds.animate().alpha(1.0f).start();
//            Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
//            parentAds.startAnimation(downtoup);
//        }

        ((ImageView) findViewById(R.id.iv_arrow)).setColorFilter(getResources().getColor(R.color.dark_icon_color), PorterDuff.Mode.MULTIPLY);
        ((ImageView) findViewById(R.id.iv_bg_snow)).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        this.ivDone.setColorFilter(getResources().getColor(R.color.progress_color), PorterDuff.Mode.MULTIPLY);
        this.rocketImageOut.setColorFilter(getResources().getColor(R.color.progress_color), PorterDuff.Mode.MULTIPLY);
        this.rocketImage.setColorFilter(getResources().getColor(R.color.progress_color), PorterDuff.Mode.MULTIPLY);
    }

    class anmRotate implements Animation.AnimationListener {
        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        anmRotate() {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            CoolActivity.this.rocketImageOut.setImageResource(R.drawable.rocket_12);
            ((View) CoolActivity.this.rocketImage.getParent()).setVisibility(View.GONE);
            CoolActivity.this.ivDone.setVisibility(View.VISIBLE);
            CoolActivity.this.ivDone.startAnimation(CoolActivity.this.ivDoneAnim);
            CoolActivity.this.tvResult.setText(CoolActivity.this.getResources().getString(R.string.done));
            CoolActivity.this.tvResult.startAnimation(CoolActivity.this.ivDoneAnim);
        }
    }

    private void initRippleBackgound() {
        ((RippleBackground) findViewById(R.id.charge_boost_ripple_background)).startRippleAnimation();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        Collection arrayList = new ArrayList();
        arrayList.add(ObjectAnimator.ofFloat(this.rocketImageOut, "ScaleX", 0.0f, 1.2f, 1.0f));
        arrayList.add(ObjectAnimator.ofFloat(this.rocketImageOut, "ScaleY", 0.0f, 1.2f, 1.0f));
        animatorSet.playTogether(arrayList);
        animatorSet.start();
    }

    private class LoadRunningTask extends AsyncTask<Void, Drawable, Void> {
        int temp = 0;

        private LoadRunningTask() {
            mPackageManager = getPackageManager();
            mActivityManager = (ActivityManager) getSystemService(
                    Context.ACTIVITY_SERVICE);
        }

        @Override
        protected Void doInBackground(Void... voidArr) {

            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
            ArrayList<TaskInfo> arrList = new ArrayList<>();
            int mem = 0;
            if (Build.VERSION.SDK_INT <= 21) {
                Iterator<ActivityManager.RunningAppProcessInfo> iterator = list.iterator();
                do {
                    if (!iterator.hasNext()) {
                        break;
                    }
                    try {
                        if (mPackageManager == null) break;
                        ActivityManager.RunningAppProcessInfo runproInfo = iterator.next();
                        String packagename = runproInfo.processName;
                        ApplicationInfo applicationInfo;
                        applicationInfo = mPackageManager.getApplicationInfo(packagename, 0);
                        if (applicationInfo == null) continue;
                        if (!packagename.contains(getPackageName()) && applicationInfo != null && Utils.isUserApp(applicationInfo) && !Utils.checkLockedItem(CoolActivity.this, packagename)) {
                            TaskInfo info = new TaskInfo(CoolActivity.this, applicationInfo);
                            mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                            Drawable d = getPackageManager().getApplicationIcon(info.getPackageName());
                            if (d != null) {
                                publishProgress(d);
                            }
                            try {
                                Thread.sleep(150);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }
                } while (true);
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    for (ActivityManager.RunningServiceInfo runningServiceInfo : am.getRunningServices(Integer.MAX_VALUE)) {
                        try {
                            if (mPackageManager == null) break;
                            PackageInfo packageInfo = mPackageManager.getPackageInfo(runningServiceInfo.service.getPackageName(), PackageManager.GET_ACTIVITIES);
                            if (packageInfo == null) continue;
                            ApplicationInfo applicationInfo;
                            applicationInfo = mPackageManager.getApplicationInfo(packageInfo.packageName, 0);
                            if (applicationInfo == null) continue;
                            if (!packageInfo.packageName.contains(CoolActivity.this.getPackageName()) && applicationInfo != null && Utils.isUserApp(applicationInfo) && !Utils.checkLockedItem(CoolActivity.this, packageInfo.packageName)) {
                                TaskInfo info = new TaskInfo(CoolActivity.this, applicationInfo);
                                mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                                Drawable d = CoolActivity.this.getPackageManager().getApplicationIcon(info.getPackageName());
                                if (d != null) {
                                    publishProgress(d);
                                }
                                try {
                                    Thread.sleep(150);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                } else {
                    int flags = PackageManager.GET_ACTIVITIES
                            | PackageManager.GET_CONFIGURATIONS
                            | PackageManager.GET_DISABLED_COMPONENTS
                            | PackageManager.GET_GIDS | PackageManager.GET_INSTRUMENTATION
                            | PackageManager.GET_INTENT_FILTERS
                            | PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS
                            | PackageManager.GET_RECEIVERS | PackageManager.GET_SERVICES
                            | PackageManager.GET_SIGNATURES;
                    PackageManager packageManager = CoolActivity.this.getPackageManager();
                    List<PackageInfo> installedPackages = packageManager.getInstalledPackages(flags);
                    for (PackageInfo packageInfo : installedPackages) {
                        if (mPackageManager == null) break;
                        try {
                            ApplicationInfo applicationInfo;
                            applicationInfo = mPackageManager.getApplicationInfo(packageInfo.packageName, 0);

                            ServiceInfo[] services = packageInfo.services;
                            if (services != null) {
                                if (services.length > 0) {
                                    if (!packageInfo.packageName.contains(CoolActivity.this.getPackageName()) && applicationInfo != null && Utils.isUserApp(applicationInfo) && !Utils.checkLockedItem(CoolActivity.this, packageInfo.packageName)) {
                                        TaskInfo info = new TaskInfo(CoolActivity.this, applicationInfo);
                                        mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                                        Drawable d = CoolActivity.this.getPackageManager().getApplicationIcon(info.getPackageName());
                                        if (d != null) {
                                            publishProgress(d);
                                        }
                                        try {
                                            Thread.sleep(150);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            PermissionInfo[] permissions = packageInfo.permissions;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Drawable... drawableArr) {
            Animation loadAnimation;
            int nextInt = new Random().nextInt((CoolActivity.this.arrGravity1.length - 1) + 1) + 0;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RANDOM: ");
            stringBuilder.append(nextInt);
            stringBuilder.append(" curIndex: ");
            stringBuilder.append(CoolActivity.this.curIndex);
            int dimension = (int) CoolActivity.this.getResources().getDimension(R.dimen.icon_size);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.height = dimension;
            layoutParams.width = dimension;
            layoutParams.gravity = CoolActivity.this.arrGravitys[CoolActivity.this.curIndex][nextInt];
            if (CoolActivity.this.curIndex == 0) {
                loadAnimation = AnimationUtils.loadAnimation(CoolActivity.this, R.anim.anim_item_boost_1);
            } else if (CoolActivity.this.curIndex == 1) {
                loadAnimation = AnimationUtils.loadAnimation(CoolActivity.this, R.anim.anim_item_boost_2);
            } else if (CoolActivity.this.curIndex == 2) {
                loadAnimation = AnimationUtils.loadAnimation(CoolActivity.this, R.anim.anim_item_boost_3);
            } else if (CoolActivity.this.curIndex == 3) {
                loadAnimation = AnimationUtils.loadAnimation(CoolActivity.this, R.anim.anim_item_boost_4);
            } else {
                loadAnimation = AnimationUtils.loadAnimation(CoolActivity.this, R.anim.anim_item_boost_0);
            }
            final ImageView imageView = new ImageView(CoolActivity.this);

            CoolActivity.this.chargeBoostContainers[CoolActivity.this.curIndex].addView(imageView, layoutParams);
            CoolActivity.this.curIndex = CoolActivity.this.curIndex + 1;
            if (CoolActivity.this.curIndex >= CoolActivity.this.chargeBoostContainers.length) {
                CoolActivity.this.curIndex = 0;
            }
            imageView.setImageDrawable(drawableArr[0]);
            imageView.startAnimation(loadAnimation);
            loadAnimation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    imageView.setVisibility(View.GONE);
                }
            });
            super.onProgressUpdate(drawableArr);
        }

        @Override
        protected void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
        }

        public void doPublishProgress(Drawable drawable) {
            publishProgress(drawable);
        }
    }

    class anmDone implements Animation.AnimationListener {

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        anmDone() {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
//            if (SharePreferenceUtils.getInstance(CoolActivity.this).getFlagAds()) {
//                SharePreferenceUtils.getInstance(CoolActivity.this).setFlagAds(false);
//                    AdmobHelp.getInstance().loadInterstitialAd   (this, TypeAds.admod_full_phonecooler,);

            adControlHelp.showInterstitialAd(adCloseListener);
//            AdmobHelp.getInstance().showInterstitialAd(new AdmobHelp.AdCloseListener() {
//                @Override
//                public void onAdClosed() {
//                    loadResult();
//                }
//            });
//            } else {
//                loadResult();
//            }
        }
    }

    private void loadResult() {
        Animation slideUp = AnimationUtils.loadAnimation(CoolActivity.this, R.anim.zoom_in);
        lrScan.startAnimation(slideUp);
        CoolActivity.this.lrScan.setVisibility(View.GONE);

        CoolActivity.this.parentAds.setAlpha(0.0f);
        CoolActivity.this.parentAds.setVisibility(View.VISIBLE);
        CoolActivity.this.parentAds.animate().alpha(1.0f).start();
        Animation downtoup = AnimationUtils.loadAnimation(CoolActivity.this, R.anim.downtoup);
        parentAds.startAnimation(downtoup);
        SharePreferenceUtils.getInstance(CoolActivity.this).setCoolerTime(System.currentTimeMillis());
        SharePreferenceUtils.getInstance(CoolActivity.this).setCoolerTimeMain(System.currentTimeMillis());
    }

    @Override
    public void onBackPressed() {
//        if (SharePreferenceUtils.getInstance(this).getFlagAds()) {
//            SharePreferenceUtils.getInstance(this).setFlagAds(false);
//            AdmobHelp.getInstance().showInterstitialAd(new AdmobHelp.AdCloseListener() {
//                @Override
//                public void onAdClosed()
//                {
//                    finish();
//                }
//            });
//        } else {
//            finish();
//        }
        finish();
    }

    @Override
    protected void onDestroy() {
        SharePreferenceUtils.getInstance(this).setFlagAds(false);
        super.onDestroy();
    }
}
