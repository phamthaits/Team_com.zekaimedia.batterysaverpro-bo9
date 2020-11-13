package app.tahachi.batterydoctor.fragment;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.ads.control.AdControlHelp;
import app.tahachi.batterydoctor.BatteryMode.BatteryInfo;
import app.tahachi.batterydoctor.Utilsb.BatteryPref;
import app.tahachi.batterydoctor.Utilsb.SharePreferenceUtils;
import app.tahachi.batterydoctor.Utilsb.Utils;
import app.tahachi.batterydoctor.activity.BaseActivity;
import app.tahachi.batterydoctor.activity.CleanActivity;
import app.tahachi.batterydoctor.activity.PermissionActivity;
import app.tahachi.batterydoctor.service.BatteryService;
import app.tahachi.batterydoctor.task.TaskCountDoing;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.skyfishjy.library.RippleBackground;

import org.androidannotations.annotations.ViewById;

import app.tahachi.batterydoctor.R;
import app.tahachi.batterydoctor.task.BatteryTask;
import app.tahachi.batterydoctor.task.TaskCount;
import app.tahachi.batterydoctor.activity.CoolActivity;
import app.tahachi.batterydoctor.activity.BatterySaverActivity;
import app.tahachi.batterydoctor.activity.BoostActivity;
import app.tahachi.batterydoctor.view.WaveDrawable;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.Callable;

import me.itangqi.waveloadingview.WaveLoadingView;

public class fmBatterySaveMain extends Fragment implements View.OnClickListener {

    private TextView tvTemperaturePin, tvVoltage, tvCapacity;
    private WaveLoadingView waveLoadingView;
    private TextView tvPercentPin;

    private ImageView imgUsb;
    private Button btnOptimize;
    private ImageView btnWifi, btnBluetooth, btnSound, btnScreenTime, btnRotate, btnBrightness, btnMobileData, btnSync, btnGPS, btnAirPlane;

    public static final int EXTDIR_REQUEST_CODE = 1110, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 300;
    private AudioManager audioManager;
    private WifiManager wifiManager;
    private BluetoothAdapter bluetoothAdapter;
    private LocationManager locationManager;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    public int TYPE_WIFI = 1;
    public int TYPE_MOBILE = 2;
    public int TYPE_NOT_CONNECTED = 0;
    boolean flagBattery = false;
    private TextView tvPowerIssue, tvHour, tvMin;
    private View vPowerIssue;
    RelativeLayout lrIssue;
    int ON_DO_NOT_DISTURB_CALLBACK_CODE = 4300;
    public static final int WRITE_PERMISSION_REQUEST = 5000;
    private BatteryTask mBatteryTask;
    private TaskCount mTaskCount;
    private TaskCountDoing mTaskCountDoing;
    private RippleBackground rippleBackground;
    private TextView tvFullCharge;
    private LinearLayout lrTimeLeft;
    private RelativeLayout lrClean, lrBoost, lrCool;

    private ProgressBar pbQuick, pbFull, pbTrickle;
    private ImageView imgQuick, imgFull, imgTrickle;
    private Shimmer shFast, shFull, shTrickle, shOptimize;
    private ShimmerTextView tvFast, tvFull, tvTrickle;
    private View v1, v2, v3, v4, v5;
    private AdControlHelp adControlHelp;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        adControlHelp = AdControlHelp.getInstance(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lrBoost:
                SharePreferenceUtils.getInstance(getActivity()).setFlagAds(true);
                startActivity(new Intent(getActivity(), BoostActivity.class));
                break;
            case R.id.lrClean:
                SharePreferenceUtils.getInstance(getActivity()).setFlagAds(true);
                try {
                    ((BaseActivity) getActivity()).askPermissionUsageSetting(() -> {
                        startActivity(new Intent(getActivity(), CleanActivity.class));
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.lrCool:
                SharePreferenceUtils.getInstance(getActivity()).setFlagAds(true);
                startActivity(new Intent(getActivity(), CoolActivity.class));
                break;
            case R.id.btnOptimize:
                if (Utils.checkSystemWritePermission(getActivity())) {
                    SharePreferenceUtils.getInstance(getActivity()).setFlagAds(true);
                    startActivity(new Intent(getActivity(), BatterySaverActivity.class));
                } else {
                    writePermission();
                }
                break;
            case R.id.btnWifi:

                if (Utils.checkSystemWritePermission(getActivity())) {

                    if (wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(false);
                    } else {
                        wifiManager.setWifiEnabled(true);
                    }
                } else {
                    Utils.openAndroidPermissionsMenu(getActivity());
                }
                break;
            case R.id.btnBluetooth:
                if (Utils.checkSystemWritePermission(getActivity())) {

                    if (bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.disable();
                    } else {
                        bluetoothAdapter.enable();
                        Toast.makeText(getActivity(), getActivity().getString(R.string.bluetooth_is_enabling), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Utils.openAndroidPermissionsMenu(getActivity());
                    startActivity(new Intent(getActivity(), PermissionActivity.class));
                }
                break;
            case R.id.btnSound:
                requestMutePermissions();
                break;
            case R.id.btnBrightness:
                if (Utils.checkSystemWritePermission(getActivity())) {
                    try {
                        if (Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == 1) {
                            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                        } else {
                            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                        }
                    } catch (Settings.SettingNotFoundException e) {
                    }
                } else {
                    Utils.openAndroidPermissionsMenu(getActivity());
                }
                break;
            case R.id.btnRotate:
                if (Utils.checkSystemWritePermission(getActivity())) {
                    if (isAutoRotateModeOn(getActivity())) {
                        Settings.System.putInt(getActivity().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                    } else {
                        Settings.System.putInt(getActivity().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
                    }
                } else {
                    Utils.openAndroidPermissionsMenu(getActivity());
                }
                break;
            case R.id.btnSync:
                if (ContentResolver.getMasterSyncAutomatically()) {
                    ContentResolver.setMasterSyncAutomatically(false);
                    btnSync.setImageResource(R.drawable.ic_sync_false);
                } else {
                    ContentResolver.setMasterSyncAutomatically(true);
                    btnSync.setImageResource(R.drawable.ic_sync_true);
                }
                break;
            case R.id.btnScreenTime:
                if (Utils.checkSystemWritePermission(getActivity())) {

                    ScreenTimeOutImageAction();
                } else {
                    Utils.openAndroidPermissionsMenu(getActivity());
                }
                break;

            case R.id.btnGPS:
                Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(locationIntent);
                break;
            case R.id.btnMobileData:
                Intent mobile = new Intent();
                mobile.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                this.startActivity(mobile);
                break;
            case R.id.btnAirPlane:
                Intent airplaneIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                airplaneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(airplaneIntent);
                break;

//            case R.id.btnChargeHistory:
//                startActivity(new Intent(getActivity(), ChargeActivity.class));
//                break;
            default:
                break;
        }
    }

    private boolean isAutoRotateModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) != 0;
    }

    private int getScreenTimeout() {
        try {
            int time = 15000;
            try {
                time = Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            switch (time) {
                case 15000:
                    return 0;
                case 30000:
                    return 1;
                case 60000:
                    return 2;
                case 120000:
                    return 3;
                case 300000:
                    return 4;
                case 600000:
                    return 5;
                case 1800000:
                    return 6;
                default:

                    return 0;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    private void soundModeAction() {
        try {
            switch (audioManager.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    private void requestMutePermissions() {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                soundModeAction();
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            }
        } catch (SecurityException e) {

        }
    }

    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (notificationManager.isNotificationPolicyAccessGranted()) {
                soundModeAction();
            } else {
                // Open Setting screen to ask for permisssion
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, ON_DO_NOT_DISTURB_CALLBACK_CODE);
            }
        }
    }

    public void ScreenTimeOutImageAction() {
        int postion = 0;
        postion = getScreenTimeout() + 1;
        if (postion == 7) postion = 0;
        int timeout = 15000;

        switch (postion) {
            case 0:
                btnScreenTime.setImageResource(R.drawable.ic_time15s);
                timeout = 15000;
                break;
            case 1:
                timeout = 30000;
                btnScreenTime.setImageResource(R.drawable.ic_time30s);
                break;
            case 2:
                timeout = 60000;
                btnScreenTime.setImageResource(R.drawable.ic_time1m);
                break;
            case 3:
                timeout = 120000;
                btnScreenTime.setImageResource(R.drawable.ic_time2m);
                break;
            case 4:
                timeout = 300000;
                btnScreenTime.setImageResource(R.drawable.ic_time5m);
                break;
            case 5:
                timeout = 600000;
                btnScreenTime.setImageResource(R.drawable.ic_time10m);
                break;
            case 6:
                timeout = 1800000;
                btnScreenTime.setImageResource(R.drawable.ic_time30m);
                break;
        }

        Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeout);

    }

//    CardView cvFastCharge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.content_home, container, false);
        intView(view);
        intData(view);
        intEvent();
        adControlHelp.loadNativeFragment(getActivity(),view.findViewById(R.id.native_ads_control_holder));
        adControlHelp.loadBannerFragment(getActivity(), view.findViewById(R.id.banner));
//        adControlHelp.loadNativeFragment(getActivity(), view.findViewById(R.id.native_ads_control_holder2));
        return view;
    }

    public void intView(View view) {
//        cvFastCharge = view.findViewById(R.id.cvFastCharge);
        v1 = (View) view.findViewById(R.id.view1);
        v2 = (View) view.findViewById(R.id.view2);
        v3 = (View) view.findViewById(R.id.view3);
        v4 = (View) view.findViewById(R.id.view4);

        tvFast = (ShimmerTextView) view.findViewById(R.id.tvFast);
        tvFull = (ShimmerTextView) view.findViewById(R.id.tvFull);
        tvTrickle = (ShimmerTextView) view.findViewById(R.id.tvTrickle);

        pbFull = (ProgressBar) view.findViewById(R.id.pbFull);
        pbQuick = (ProgressBar) view.findViewById(R.id.pbQuick);
        pbTrickle = (ProgressBar) view.findViewById(R.id.pbTrickle);
        imgQuick = (ImageView) view.findViewById(R.id.imgQuick);
        imgFull = (ImageView) view.findViewById(R.id.imgFull);
        imgTrickle = (ImageView) view.findViewById(R.id.imgTrickle);

        lrTimeLeft = view.findViewById(R.id.view_time_left);
        tvFullCharge = view.findViewById(R.id.tvFullCharge);
        vPowerIssue = view.findViewById(R.id.vPowerIssue);
        tvHour = view.findViewById(R.id.tvHour);
        tvMin = view.findViewById(R.id.tvMin);
        waveLoadingView = view.findViewById(R.id.waveLoadingView);
        tvPercentPin = view.findViewById(R.id.tvPercentPin);


        imgUsb = view.findViewById(R.id.imgUsb);

        tvCapacity = view.findViewById(R.id.tvCapacity);
        tvTemperaturePin = view.findViewById(R.id.tvTemperaturePin);
        tvVoltage = view.findViewById(R.id.tvVoltage);

        btnOptimize = view.findViewById(R.id.btnOptimize);

        lrIssue = view.findViewById(R.id.layout_power_issue);

        btnWifi = view.findViewById(R.id.btnWifi);
        btnBluetooth = view.findViewById(R.id.btnBluetooth);
        btnSound = view.findViewById(R.id.btnSound);
        btnScreenTime = view.findViewById(R.id.btnScreenTime);
        btnRotate = view.findViewById(R.id.btnRotate);
        btnBrightness = view.findViewById(R.id.btnBrightness);
        btnMobileData = view.findViewById(R.id.btnMobileData);
        btnSync = view.findViewById(R.id.btnSync);
        btnGPS = view.findViewById(R.id.btnGPS);
        btnAirPlane = view.findViewById(R.id.btnAirPlane);

        //
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //
        tvPowerIssue = view.findViewById(R.id.tv_power_issue);

        rippleBackground = view.findViewById(R.id.content);
        lrBoost = view.findViewById(R.id.lrBoost);
        lrClean = view.findViewById(R.id.lrClean);
        lrCool = view.findViewById(R.id.lrCool);
    }

    WaveDrawable mWaveDrawable;
    private View mView;

    public void intData(View v) {
        shFast = new Shimmer();
        shFull = new Shimmer();
        shTrickle = new Shimmer();
        mWaveDrawable = new WaveDrawable(getResources().getColor(R.color.orange),
                getResources().getDimensionPixelSize(R.dimen.power_issue_width_height) / 2, 0.2f);
        vPowerIssue.setBackgroundDrawable(mWaveDrawable);
        mWaveDrawable.setWaveInterpolator(new LinearInterpolator());
        mWaveDrawable.startAnimation();
        Intent intent2 = new Intent(getActivity(), BatteryService.class);
        ContextCompat.startForegroundService(getActivity(), intent2);
        mRegisterReceiver();
        String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
    }

    public void intEvent() {
        btnOptimize.setOnClickListener(this);
        btnWifi.setOnClickListener(this);
        btnBluetooth.setOnClickListener(this);
        btnSound.setOnClickListener(this);
        btnScreenTime.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        btnBrightness.setOnClickListener(this);
        btnMobileData.setOnClickListener(this);
        btnSync.setOnClickListener(this);
        btnGPS.setOnClickListener(this);
        btnAirPlane.setOnClickListener(this);
        lrBoost.setOnClickListener(this);
        lrClean.setOnClickListener(this);
        lrCool.setOnClickListener(this);
    }

    public void mRegisterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BatteryService.ACTION_MAX_BATTERY_CHANGED_SEND);
        getActivity().registerReceiver(receiver, filter);
    }

    private void animation(ImageView imageView, Integer animation) {
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), animation);
        rotation.setFillAfter(true);
        imageView.startAnimation(rotation);
    }

    public void writePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                ((BaseActivity) getActivity()).checkdrawPermission(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        if (Utils.checkSystemWritePermission(fmBatterySaveMain.this.getActivity())) {
                            fmBatterySaveMain.this.startActivity(new Intent(fmBatterySaveMain.this.getActivity(), fmBatterySaveMain.this.getActivity().getClass()));
                        } else {
                            Utils.openAndroidPermissionsMenu(fmBatterySaveMain.this.getActivity());
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(getActivity())) {
                    SharePreferenceUtils.getInstance(getActivity()).setStatusPer(true);
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
                    startActivityForResult(intent, WRITE_PERMISSION_REQUEST);
                    startActivity(new Intent(getActivity(), PermissionActivity.class));
                }
            }
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(BatteryService.ACTION_MAX_BATTERY_CHANGED_SEND)) {
                BatteryInfo info = intent.getParcelableExtra(BatteryInfo.BATTERY_INFO_KEY);
                //Update trang thai pin
                boolean isCharging = info.status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        info.status == BatteryManager.BATTERY_STATUS_FULL;

                updateCharge(isCharging, info);
                if (isCharging) {
                    info.plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    boolean usbCharge = info.plugged == BatteryManager.BATTERY_PLUGGED_USB;
                    int time;
                    if (usbCharge) {
                        time = BatteryPref.initilaze(context).getTimeChargingUsb(context, info.level);

                    } else {
                        time = BatteryPref.initilaze(context).getTimeChargingAc(context, info.level);
                    }

                    info.hourleft = time / 60;
                    info.minleft = time % 60;

                } else {
                    int time = BatteryPref.initilaze(context).getTimeRemainning(context, info.level);
                    info.hourleft = time / 60;
                    info.minleft = time % 60;

                }
                if (Utils.getChargeFull(getActivity())) {
                    tvFullCharge.setText(getString(R.string.power_full));
                    tvFullCharge.setVisibility(View.VISIBLE);
                    lrTimeLeft.setVisibility(View.GONE);

                } else {
                    tvFullCharge.setText("");
//                    tvFullCharge.setVisibility(View.GONE);
                    lrTimeLeft.setVisibility(View.VISIBLE);
                }
                if (flagBattery)
                    updateStatus(info);

                tvHour.setText(String.format("%02d", info.hourleft));
                tvMin.setText(String.format("%02d", info.minleft));
                //  tvTimeLeft.setText(Html.fromHtml(getString(R.string.time_left_pin, info.hourleft, info.minleft)));


            }
        }
    };

    public void updateStatus(BatteryInfo info) {


        tvPercentPin.setText(info.level + "%  ");
        if (0 <= info.level && info.level <= 5) {
            waveLoadingView.setWaveColor(ContextCompat.getColor(getActivity(), R.color.battery_almost_die));
            waveLoadingView.setProgressValue(info.level);
            return;
        }
        if (5 < info.level && info.level <= 15) {
            waveLoadingView.setWaveColor(ContextCompat.getColor(getActivity(), R.color.battery_bad));
            waveLoadingView.setProgressValue(info.level);
            return;
        }
        if (15 < info.level && info.level <= 30) {
            waveLoadingView.setWaveColor(ContextCompat.getColor(getActivity(), R.color.battery_averange));
            waveLoadingView.setProgressValue(info.level);
            return;
        }
        if (30 < info.level && info.level <= 60) {
            waveLoadingView.setWaveColor(ContextCompat.getColor(getActivity(), R.color.battery_good));
            waveLoadingView.setProgressValue(info.level);
            return;
        }
        if (60 < info.level && info.level <= 100) {
            waveLoadingView.setWaveColor(ContextCompat.getColor(getActivity(), R.color.battery_good));
            waveLoadingView.setProgressValue(info.level);
            return;
        }
        //

    }

    public void updateCharge(boolean isCharge, BatteryInfo info) {
        Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.blink_charge);
        imgUsb.startAnimation(loadAnimation);
        tvTemperaturePin.setText(getTemp(info.temperature));
        tvVoltage.setText(getVol(info.voltage) + "V");
        double temp = (info.level * getBatteryCapacity()) / 100;
        tvCapacity.setText(temp + " mhA");
        String[] string = intToArray(getActivity(), info.temperature);

        if (isCharge) {

            imgUsb.setVisibility(View.VISIBLE);
            if (info.level <= 30) {
                pbQuick.setVisibility(View.VISIBLE);
                pbFull.setVisibility(View.GONE);
                pbTrickle.setVisibility(View.GONE);
                imgQuick.setBackgroundResource(R.drawable.shape_process_green);
                imgFull.setBackgroundResource(R.drawable.icon_shape_progress_cyan);
                imgTrickle.setBackgroundResource(R.drawable.icon_shape_progress_cyan);
                shFast.start(tvFast);

                v1.setBackgroundColor(Color.parseColor(getString(R.string.stringGreen)));
                v2.setBackgroundColor(Color.parseColor(getString(R.string.stringGreen)));
                v3.setBackgroundColor(Color.parseColor(getString(R.string.stringGray)));
                v4.setBackgroundColor(Color.parseColor(getString(R.string.stringGray)));


            } else if (30 < info.level && info.level < 90) {
                shFull.start(tvFull);
                shFast.cancel();

                pbQuick.setVisibility(View.GONE);
                pbFull.setVisibility(View.VISIBLE);
                pbTrickle.setVisibility(View.GONE);
                imgQuick.setBackgroundResource(R.drawable.shape_process_green);
                imgFull.setBackgroundResource(R.drawable.shape_process_green);
                imgTrickle.setBackgroundResource(R.drawable.icon_shape_progress_cyan);
                v1.setBackgroundColor(Color.parseColor(getString(R.string.stringGreen)));
                v2.setBackgroundColor(Color.parseColor(getString(R.string.stringGreen)));
                v3.setBackgroundColor(Color.parseColor(getString(R.string.stringGray)));
                v4.setBackgroundColor(Color.parseColor(getString(R.string.stringGray)));


            } else {
                v1.setBackgroundColor(Color.parseColor(getString(R.string.stringGreen)));
                v2.setBackgroundColor(Color.parseColor(getString(R.string.stringGreen)));
                v3.setBackgroundColor(Color.parseColor(getString(R.string.stringGreen)));
                v4.setBackgroundColor(Color.parseColor(getString(R.string.stringGreen)));
                shFast.cancel();
                shFull.cancel();
                shTrickle.start(tvTrickle);
                pbQuick.setVisibility(View.GONE);
                pbFull.setVisibility(View.GONE);
                pbTrickle.setVisibility(View.VISIBLE);
                imgQuick.setBackgroundResource(R.drawable.shape_process_green);
                imgFull.setBackgroundResource(R.drawable.shape_process_green);
                imgTrickle.setBackgroundResource(R.drawable.shape_process_green);

            }
        } else {
            shFast.cancel();
            shTrickle.cancel();
            shFull.cancel();
            v1.setBackgroundColor(Color.parseColor(getString(R.string.stringGray)));
            v2.setBackgroundColor(Color.parseColor(getString(R.string.stringGray)));
            v3.setBackgroundColor(Color.parseColor(getString(R.string.stringGray)));
            v4.setBackgroundColor(Color.parseColor(getString(R.string.stringGray)));
            pbQuick.setVisibility(View.GONE);
            pbFull.setVisibility(View.GONE);
            pbTrickle.setVisibility(View.GONE);
            imgQuick.setBackgroundResource(R.drawable.icon_shape_progress_cyan);
            imgFull.setBackgroundResource(R.drawable.icon_shape_progress_cyan);
            imgTrickle.setBackgroundResource(R.drawable.icon_shape_progress_cyan);
            imgUsb.clearAnimation();
            imgUsb.setVisibility(View.GONE);
        }
    }

    public String getTemp(int i) {
        if (!SharePreferenceUtils.getInstance(getActivity()).getTempFormat()) {
            double b = Math.ceil(((i / 10f) * 9 / 5 + 32) * 100) / 100;
            String r = String.valueOf(b);
            return (r + this.getString(R.string.fahrenheit));
        } else {

            String str = Double.toString(Math.ceil((i / 10f) * 100) / 100);
            return (str + this.getString(R.string.celsius));
        }
    }

    public double getVol(int i) {
        double voltage = Math.ceil((i / 1000f) * 100) / 100;
        if (voltage > 1000)
            return voltage / 1000f;
        else
            return voltage;

    }

    public double getBatteryCapacity() {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
//            Toast.makeText(getActivity(), batteryCapacity + " mah",
//                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    private String[] intToArray(Context context, int i) {
        String str = Double.toString(i / 10f);
        if (str.length() > 4)
            str = str.substring(0, 4);

        if (true) {
            // do C
            String string = context.getString(R.string.celsius);
            return new String[]{str, string};
        } else {
            // do F
            String string = context.getString(R.string.fahrenheit);
            return new String[]{str, string};
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23 && SharePreferenceUtils.getInstance(getActivity()).getStatusPer()) {
            if (Settings.System.canWrite(getActivity())) {
                SharePreferenceUtils.getInstance(getActivity()).setStatusPer(false);
                startActivity(new Intent(getActivity(), BatterySaverActivity.class));
            }
        }
        Intent intent = new Intent();
        intent.setAction(BatteryService.ACTION_MAX_BATTERY_NEED_UPDATE);
        getActivity().sendBroadcast(intent);
        cancleUIUPdate();

        lrIssue.setVisibility(View.INVISIBLE);
        tvPowerIssue.setText("");
        btnOptimize.setText(getString(R.string.optimize));
        mBatteryTask = new BatteryTask(getActivity(), tvPercentPin, waveLoadingView, new BatteryTask.OnTaskBatteryListener() {
            @Override
            public void OnResult() {
                flagBattery = true;

            }
        });
        if (Utils.checkShouldDoing(getActivity(), 5)) {
            vPowerIssue.setVisibility(View.VISIBLE);
            tvPowerIssue.setTextColor(ContextCompat.getColor(getActivity(), R.color.orange));
            mTaskCount = new TaskCount(getActivity(), new TaskCount.OnTaskCountListener() {
                @Override
                public void OnResult(int count) {
                    if (count == 0) {
                        lrIssue.setVisibility(View.INVISIBLE);
                        btnOptimize.setText(getString(R.string.optimize));
                    } else {
                        lrIssue.setVisibility(View.VISIBLE);
                        btnOptimize.setText(getString(R.string.fix_now));
                        mTaskCountDoing = new TaskCountDoing(getActivity(), tvPowerIssue, count);
                        mTaskCountDoing.execute();
                        rippleBackground.startRippleAnimation();
                    }
                }
            });
            mTaskCount.execute();
        } else {
            lrIssue.setVisibility(View.VISIBLE);
            vPowerIssue.setVisibility(View.GONE);
            tvPowerIssue.setTextColor(ContextCompat.getColor(getActivity(), R.color.battery_good));
            tvPowerIssue.setText(getString(R.string.battery_exellent));
            btnOptimize.setText(getString(R.string.optimize));
            rippleBackground.stopRippleAnimation();
            // findViewById(R.id.layout_power_issue).setVisibility(View.INVISIBLE);
        }


        mBatteryTask.execute();

        ResRecevie();
    }

    private void updateAutoRotateImage() {
        try {

            if (isAutoRotateModeOn(getActivity())) {
                btnRotate.setImageResource(R.drawable.ic_rotate_true);

            } else {
                btnRotate.setImageResource(R.drawable.ic_rotate_false);
            }

        } catch (Exception e) {
        }
    }

    private ContentObserver rotateObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            updateAutoRotateImage();
        }

    };

    private ContentObserver brightnessModeObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            updateBrightnessModeImage();

        }

    };

    public void ResRecevie() {
        // wifi
        getActivity().registerReceiver(DeviceChangeReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        getActivity().registerReceiver(DeviceChangeReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        getActivity().registerReceiver(DeviceChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        getActivity().registerReceiver(DeviceChangeReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        getActivity().registerReceiver(DeviceChangeReceiver, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
        getActivity().registerReceiver(DeviceChangeReceiver, new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION));
        getActivity().getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), true, rotateObserver);
        getActivity().getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), true, brightnessModeObserver);

        updateWifi();
        updateBluetoothImage();
        soundModeStatus();
        mobileDataImage();
        updateLocationImage();
        updateBrightnessModeImage();
        updateSyncImage();
        updateScreenTimeOutImage();
    }

    public void updateScreenTimeOutImage() {
        int postion = getScreenTimeout();

        switch (postion) {
            case 0:
                btnScreenTime.setImageResource(R.drawable.ic_time15s);
                break;
            case 1:
                btnScreenTime.setImageResource(R.drawable.ic_time30s);
                break;
            case 2:
                btnScreenTime.setImageResource(R.drawable.ic_time1m);
                break;
            case 3:
                btnScreenTime.setImageResource(R.drawable.ic_time2m);
                break;
            case 4:
                btnScreenTime.setImageResource(R.drawable.ic_time5m);
                break;
            case 5:
                btnScreenTime.setImageResource(R.drawable.ic_time10m);
                break;
            case 6:
                btnScreenTime.setImageResource(R.drawable.ic_time30m);
                break;
        }
    }

    public void updateSyncImage() {
        if (ContentResolver.getMasterSyncAutomatically()) {
            btnSync.setImageResource(R.drawable.ic_sync_true);
        } else {
            btnSync.setImageResource(R.drawable.ic_sync_false);
        }
    }

    public void updateWifi() {

        if (wifiManager.isWifiEnabled()) {
            btnWifi.setImageResource(R.drawable.ic_wifi_true);
        } else {
            btnWifi.setImageResource(R.drawable.ic_wifi_false);
        }
    }

    public void updateBrightnessModeImage() {
        try {
            if (Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == 1) {
                btnBrightness.setImageResource(R.drawable.ic_brightness_mode_auto);
            } else {
                btnBrightness.setImageResource(R.drawable.ic_brightness_mode__no_auto);
            }
        } catch (Settings.SettingNotFoundException e) {
        }
    }

    private void updateLocationImage() {
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!gps_enabled && !network_enabled) {
                btnGPS.setImageResource(R.drawable.ic_gps_false);

            } else {
                btnGPS.setImageResource(R.drawable.ic_gps_true);
            }

        } catch (Exception e) {
        }
    }

    public void updateBluetoothImage() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            btnBluetooth.setImageResource(R.drawable.ic_bluetooth_true);
        } else {
            btnBluetooth.setImageResource(R.drawable.ic_bluetooth_false);
        }
    }

    public void soundModeStatus() {
        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                btnSound.setImageResource(R.drawable.ic_volume_normal);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                btnSound.setImageResource(R.drawable.ic_vibration);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                btnSound.setImageResource(R.drawable.ic_volume_off);
                break;
            default:
                break;
        }
    }

    public void mobileDataImage() {
        try {
            int conn = getConnectivityStatus(getActivity());
            if (conn == TYPE_WIFI) {

            } else if (conn == TYPE_MOBILE) {
                btnMobileData.setImageResource(R.drawable.ic_mobile_true);
            } else if (conn == TYPE_NOT_CONNECTED) {
                btnMobileData.setImageResource(R.drawable.ic_mobile_false);
            }

        } catch (NullPointerException e) {
            btnMobileData.setImageResource(R.drawable.ic_mobile_false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    private BroadcastReceiver DeviceChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context paramContext, Intent paramIntent) {
            if (paramIntent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION) || paramIntent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                updateWifi();
                mobileDataImage();
            }
            if (paramIntent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                updateBluetoothImage();
            }
            if (paramIntent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                soundModeStatus();
            }

            if (paramIntent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                updateLocationImage();

            }

            if (paramIntent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                updateAirplaneImage();

            }
        }
    };

    @SuppressLint("InlinedApi")
    private boolean isAirplaneModeOn(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
            } else {
                return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private void updateAirplaneImage() {
        try {
            if (isAirplaneModeOn(getActivity())) {

                btnAirPlane.setImageResource(R.drawable.ic_airplane_true);
            } else {
                btnAirPlane.setImageResource(R.drawable.ic_airplane_false);
            }

        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        cancleUIUPdate();
        stopRes();
    }

    public void cancleUIUPdate() {
        if (this.mBatteryTask != null && this.mBatteryTask.getStatus() == AsyncTask.Status.RUNNING) {
            this.mBatteryTask.cancel(true);
            this.mBatteryTask = null;
        }

        if (this.mTaskCount != null && this.mTaskCount.getStatus() == AsyncTask.Status.RUNNING) {
            this.mTaskCount.cancel(true);
            this.mTaskCount = null;
        }
        if (this.mTaskCountDoing != null && this.mTaskCountDoing.getStatus() == AsyncTask.Status.RUNNING) {
            this.mTaskCountDoing.cancel(true);
            this.mTaskCountDoing = null;
        }
    }

    public void stopRes() {
        try {
            getActivity().unregisterReceiver(DeviceChangeReceiver);
        } catch (Exception e) {
        }
        try {
            getActivity().getContentResolver().unregisterContentObserver(rotateObserver);
        } catch (Exception e) {
        }
        try {
            getActivity().getContentResolver().unregisterContentObserver(brightnessModeObserver);
        } catch (Exception e) {
        }

    }

}
