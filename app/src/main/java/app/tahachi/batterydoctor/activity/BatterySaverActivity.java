package app.tahachi.batterydoctor.activity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.ads.control.AdControlHelp;
import app.ads.control.AdControlHelp.AdCloseListener;
import app.tahachi.batterydoctor.Utilsb.SharePreferenceUtils;
import app.tahachi.batterydoctor.Utilsb.Utils;
import app.tahachi.batterydoctor.model.TaskInfo;
import com.skyfishjy.library.RippleBackground;
import app.tahachi.batterydoctor.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BatterySaverActivity extends AppCompatActivity implements OnClickListener {
    private int curIndex = 0;
    private int[] arrGravity1 = new int[]{49, 19, 83};
    private int[] arrGravity2 = new int[]{51, 49, 21};
    private int[] arrGravity3 = new int[]{53, 21, 81};
    private int[] arrGravity4 = new int[]{85, 81, 19};
    private int[][] arrGravitys = new int[][]{this.arrGravity1, this.arrGravity2, this.arrGravity3, this.arrGravity4};
    private LayoutParams layoutParams;
    private FrameLayout[] chargeBoostContainers = new FrameLayout[4];
    private PackageManager mPackageManager;
    ActivityManager mActivityManager;
    ImageView rocketImage, rocketImageOut;
    private TextView tvResult, tvScan;

    private ImageView ivDone;
    private Animation ivDoneAnim;
    private ViewGroup parentAds;
    private LinearLayout lrScan;
    private Context context;
    private AdControlHelp adControlHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utils.setLocate(context);
        setContentView(R.layout.activity_do_optimize);
        adControlHelp = AdControlHelp.getInstance(context);
        intView();
        adControlHelp.loadNative(this);
        adControlHelp.loadInterstitialAd(this,adCloseListener, null, false);
    }

    private AdCloseListener adCloseListener = new AdCloseListener() {
        @Override
        public void onAdClosed() {
            loadResult();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lr_back:
                finish();
                return;
            case R.id.lrBoost:
                startActivity(new Intent(BatterySaverActivity.this, BoostActivity.class));
                finish();
                return;
            case R.id.lrClean:
                startActivity(new Intent(BatterySaverActivity.this, CleanActivity.class));
                finish();
                return;
            case R.id.lrCool:
                startActivity(new Intent(BatterySaverActivity.this, CoolActivity.class));
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
        this.tvResult = findViewById(R.id.clean_up_done_tv_result);
        tvScan = findViewById(R.id.tvScan);
        this.ivDone = findViewById(R.id.clean_done_iv_done);
        this.rocketImageOut = findViewById(R.id.ivDoneHoloCirular);
        this.rocketImage = findViewById(R.id.ivScan);
//        if (Utils.checkShouldDoing(this, 5)) {
        new LoadRunningTask().execute();
        int size = (int) getResources().getDimension(R.dimen.icon_size);
        this.layoutParams = new LayoutParams(-2, -2);
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

        this.ivDoneAnim = AnimationUtils.loadAnimation(this, R.anim.ic_done_anim);
        animationRotate.setAnimationListener(new anmRotate());
        this.ivDoneAnim.setAnimationListener(new anmDone());
        ((ImageView) findViewById(R.id.iv_arrow)).setColorFilter(getResources().getColor(R.color.description), Mode.MULTIPLY);
        this.ivDone.setColorFilter(getResources().getColor(R.color.color_white), Mode.MULTIPLY);
        this.rocketImageOut.setColorFilter(getResources().getColor(R.color.progress_color), Mode.MULTIPLY);
        this.rocketImage.setColorFilter(getResources().getColor(R.color.progress_color), Mode.MULTIPLY);
    }

    class anmRotate implements AnimationListener {
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
            tvScan.setVisibility(View.GONE);
            BatterySaverActivity.this.rocketImageOut.setImageResource(R.drawable.rocket_12);
            ((View) BatterySaverActivity.this.rocketImage.getParent()).setVisibility(View.GONE);
            BatterySaverActivity.this.ivDone.setVisibility(View.VISIBLE);
            BatterySaverActivity.this.ivDone.startAnimation(BatterySaverActivity.this.ivDoneAnim);
            BatterySaverActivity.this.tvResult.setText(BatterySaverActivity.this.getResources().getString(R.string.done));
            BatterySaverActivity.this.tvResult.startAnimation(BatterySaverActivity.this.ivDoneAnim);
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

    private class LoadRunningTask extends AsyncTask<Void, TaskInfo, Void> {
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
                        if (!packagename.contains(getPackageName()) && applicationInfo != null && Utils.isUserApp(applicationInfo) && !Utils.checkLockedItem(BatterySaverActivity.this, packagename)) {
                            TaskInfo info = new TaskInfo(BatterySaverActivity.this, applicationInfo);
                            mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                            publishProgress(info);
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
                            if (!packageInfo.packageName.contains(BatterySaverActivity.this.getPackageName()) && applicationInfo != null && Utils.isUserApp(applicationInfo) && !Utils.checkLockedItem(BatterySaverActivity.this, packageInfo.packageName)) {
                                TaskInfo info = new TaskInfo(BatterySaverActivity.this, applicationInfo);
                                mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                                Drawable d = BatterySaverActivity.this.getPackageManager().getApplicationIcon(info.getPackageName());
                                publishProgress(info);
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
                    PackageManager packageManager = BatterySaverActivity.this.getPackageManager();
                    List<PackageInfo> installedPackages = packageManager
                            .getInstalledPackages(flags);
                    for (PackageInfo packageInfo : installedPackages) {
                        if (mPackageManager == null) break;
                        try {
                            ApplicationInfo applicationInfo;
                            applicationInfo = mPackageManager.getApplicationInfo(packageInfo.packageName, 0);

                            ServiceInfo[] services = packageInfo.services;
                            if (services != null) {
                                if (services.length > 0) {
                                    if (!packageInfo.packageName.contains(BatterySaverActivity.this.getPackageName()) && applicationInfo != null && Utils.isUserApp(applicationInfo) && !Utils.checkLockedItem(BatterySaverActivity.this, packageInfo.packageName)) {
                                        TaskInfo info = new TaskInfo(BatterySaverActivity.this, applicationInfo);
                                        mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                                        publishProgress(info);
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
        protected void onProgressUpdate(TaskInfo... values) {
            getImageApp(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
        }
    }

    public void getImageApp(TaskInfo info) {
        tvScan.setText(BatterySaverActivity.this.getString(R.string.pc_scanning) + ": " + info.getTitle());
        try {
            Drawable d = BatterySaverActivity.this.getPackageManager().getApplicationIcon(info.getPackageName());
            if (d != null) {
                Animation loadAnimation;
                int nextInt = new Random().nextInt((BatterySaverActivity.this.arrGravity1.length - 1) + 1) + 0;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("RANDOM: ");
                stringBuilder.append(nextInt);
                stringBuilder.append(" curIndex: ");
                stringBuilder.append(BatterySaverActivity.this.curIndex);
                int dimension = (int) BatterySaverActivity.this.getResources().getDimension(R.dimen.icon_size);
                LayoutParams layoutParams = new LayoutParams(-2, -2);
                layoutParams.height = dimension;
                layoutParams.width = dimension;
                layoutParams.gravity = BatterySaverActivity.this.arrGravitys[BatterySaverActivity.this.curIndex][nextInt];
                if (BatterySaverActivity.this.curIndex == 0) {
                    loadAnimation = AnimationUtils.loadAnimation(BatterySaverActivity.this, R.anim.anim_item_boost_1);
                } else if (BatterySaverActivity.this.curIndex == 1) {
                    loadAnimation = AnimationUtils.loadAnimation(BatterySaverActivity.this, R.anim.anim_item_boost_2);
                } else if (BatterySaverActivity.this.curIndex == 2) {
                    loadAnimation = AnimationUtils.loadAnimation(BatterySaverActivity.this, R.anim.anim_item_boost_3);
                } else if (BatterySaverActivity.this.curIndex == 3) {
                    loadAnimation = AnimationUtils.loadAnimation(BatterySaverActivity.this, R.anim.anim_item_boost_4);
                } else {
                    loadAnimation = AnimationUtils.loadAnimation(BatterySaverActivity.this, R.anim.anim_item_boost_0);
                }
                final ImageView imageView = new ImageView(BatterySaverActivity.this);

                BatterySaverActivity.this.chargeBoostContainers[BatterySaverActivity.this.curIndex].addView(imageView, layoutParams);
                BatterySaverActivity.this.curIndex = BatterySaverActivity.this.curIndex + 1;
                if (BatterySaverActivity.this.curIndex >= BatterySaverActivity.this.chargeBoostContainers.length) {
                    BatterySaverActivity.this.curIndex = 0;
                }
                imageView.setImageDrawable(d);
                imageView.startAnimation(loadAnimation);
                loadAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        imageView.setVisibility(View.GONE);
                    }
                });
            }

        } catch (PackageManager.NameNotFoundException e) {
            return;
        }
    }

    class anmDone implements AnimationListener {
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
            adControlHelp.showInterstitialAd(adCloseListener);
        }
    }

//    public void checkTask() {
//        if (!Utils.checkShouldDoing(this, 6)) {
//            findViewById(R.id.cvBoost).setVisibility(View.GONE);
//        }
//        if (!Utils.checkShouldDoing(this, 7)) {
//            findViewById(R.id.cvCool).setVisibility(View.GONE);
//
//        }
//        if (!Utils.checkShouldDoing(this, 3)) {
//            findViewById(R.id.cvClean).setVisibility(View.GONE);
//
//        }
//        if (!Utils.checkShouldDoing(this, 8)) {
//            findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//
//        } else {
//            if (SharePreferenceUtils.getInstance(this).getFsAutoRun())
//                findViewById(R.id.cvFastCharge).setVisibility(View.GONE);
//        }

//    }

    private void loadResult() {
        Animation slideUp = AnimationUtils.loadAnimation(BatterySaverActivity.this, R.anim.zoom_in);
        lrScan.startAnimation(slideUp);
        BatterySaverActivity.this.lrScan.setVisibility(View.GONE);

        BatterySaverActivity.this.parentAds.setAlpha(0.0f);
        BatterySaverActivity.this.parentAds.setVisibility(View.VISIBLE);
        BatterySaverActivity.this.parentAds.animate().alpha(1.0f).start();
        Animation downtoup = AnimationUtils.loadAnimation(BatterySaverActivity.this, R.anim.downtoup);
        parentAds.startAnimation(downtoup);
        SharePreferenceUtils.getInstance(BatterySaverActivity.this).setOptimizeTime(System.currentTimeMillis());
        SharePreferenceUtils.getInstance(BatterySaverActivity.this).setOptimizeTimeMain(System.currentTimeMillis());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        SharePreferenceUtils.getInstance(this).setFlagAds(false);
        super.onDestroy();
    }
}
