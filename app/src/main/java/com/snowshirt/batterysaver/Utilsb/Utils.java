package com.snowshirt.batterysaver.Utilsb;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.snowshirt.batterysaver.activity.ChargeActivity;
import com.snowshirt.batterysaver.activity.PermissionActivity;
import com.snowshirt.batterysaver.notification.NotificationDevice;
import com.snowshirt.batterysaver.Alarm.AlarmUtils;
import com.snowshirt.batterysaver.R;
import com.snowshirt.batterysaver.model.DeviceStatus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.POWER_SERVICE;

public class Utils {
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static boolean checkLockedItem(Context context, String checkApp) {
        PreferAppList mPreferAppList = new PreferAppList();
        boolean check = false;
        List<String> locked = mPreferAppList.getLocked(context);
        if (locked != null) {
            for (String lock : locked) {
                if (lock.equals(checkApp)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    public static void fullPower(Context context) {
//        Utils.checkDNDDoing(context)
        long i = SharePreferenceUtils.getInstance(context).getTime();
        if (i == 0) {
            SharePreferenceUtils.getInstance(context).setTime(System.currentTimeMillis());
            boolean doing = Utils.checkDNDDoing(context);
            boolean reminer = SharePreferenceUtils.getInstance(context).getChargeFullReminder();
            if (!SharePreferenceConstant.full_battery_loaded) {
                SharePreferenceConstant.full_battery_loaded = true;
//                Intent ii = new Intent(context, FullChargeActivity.class);
//                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(ii);
            }
            if (doing && reminer) {
                NotificationDevice.showNotificationBatteryFull(context);
                Utils.intSound(context);
            }
        }
    }

    public static void intPowerConnected(Context context) {
        //Open activity sac nhanh
        if (SharePreferenceUtils.getInstance(context).getFsAutoRun() && !Utils.getChargeFull(context)) {
            Intent i = new Intent(context, ChargeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        //Loai sac
        SharePreferenceUtils.getInstance(context).setChargeType(Utils.getChargeType(context));
        //Pin vao
        SharePreferenceUtils.getInstance(context).setLevelIn(Utils.getBatteryLevel(context));
        //Thoi gian bat dau sac
        SharePreferenceUtils.getInstance(context).setTimeIn(System.currentTimeMillis());
        // Thoi gian dau tien khi Pin day
        SharePreferenceUtils.getInstance(context).setTime(0);
    }

    public static boolean powerIsConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        return false;
    }

    public static void powerDisconnected(Context context) {
        // Dem cac trang thai sac Pin
        if (Utils.getBatteryLevel(context) == 100) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(6);
            if (SharePreferenceUtils.getInstance(context).getTime() != 0) {
                DateFormat df = new SimpleDateFormat("HH:mm, d MMM yyyy");
                SharePreferenceUtils.getInstance(context).setChargeFull(df.format(SharePreferenceUtils.getInstance(context).getTime()));
            }
            if (SharePreferenceUtils.getInstance(context).getTime() != 0
                    && ((System.currentTimeMillis() - SharePreferenceUtils.getInstance(context).getTimeIn()) > (30 * 60 * 1000))
                    && SharePreferenceUtils.getInstance(context).getTimeIn() != 0) {

                //Sac qua tai
                long count = SharePreferenceUtils.getInstance(context).getChargeOver() + 1;
                SharePreferenceUtils.getInstance(context).setChargeOver(count);
            } else {
                //Sac hieu qua
                long count = SharePreferenceUtils.getInstance(context).getChargeHealthy() + 1;
                SharePreferenceUtils.getInstance(context).setChargeHealthy(count);
            }
        } else {
            //Sac binh thuong
            long count = SharePreferenceUtils.getInstance(context).getChargeNormal() + 1;
            SharePreferenceUtils.getInstance(context).setChargeNormal(count);
        }
        // Luu % Pin khi rut sac
        SharePreferenceUtils.getInstance(context).setChargeQuantity(Utils.getBatteryLevel(context) - SharePreferenceUtils.getInstance(context).getLevelIn());

        // Thoi gian sac Pin
        if (SharePreferenceUtils.getInstance(context).getTimeIn() != 0) {
            long timeCharge = System.currentTimeMillis() - SharePreferenceUtils.getInstance(context).getTimeIn();
            SharePreferenceUtils.getInstance(context).setTimeCharge(timeCharge);
        }
        SharePreferenceUtils.getInstance(context).setLevelIn(0);
        SharePreferenceUtils.getInstance(context).setTimeIn(0);
//Rút sạc
//        if (SharePreferenceUtils.getInstance(context).getFsAutoRun()) {
//            Intent i = new Intent(context, ChargeResultActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        }
    }

    public static String formatSize(long size) {
        if (size <= 0)
            return "";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static void setTextFromSize(long size, TextView tvNumber, TextView tvType) {
        if (size <= 0) {
            tvNumber.setText(String.valueOf(0.00));
            tvType.setText("MB");
            return;
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        tvNumber.setText(String.valueOf(new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups))));

        tvType.setText(units[digitGroups]);
    }

    public static void setTextScreenTimeOut(Context context, TextView textView, int time) {
        if (time < 60000) {
            textView.setText(String.format(context.getString(R.string.seconds), time / 1000));
        } else {
            textView.setText(String.format(context.getString(R.string.minutes), time / (1000 * 60)));
        }
    }

    public static boolean isUserApp(ApplicationInfo ai) {
        int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
        return (ai.flags & mask) == 0;
    }

    public static float getCpuTemp() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            Log.i("cpu Temp", line + "\t");
            return Float.parseFloat(line);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static long getTotalRam() {
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            //total Memory
            initial_memory = Integer.valueOf(arrayOfString[1]) * 1024;
            localBufferedReader.close();
            return initial_memory;
        } catch (IOException e) {
            return -1;
        }
    }

    public static long getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        long totRam = 0;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Integer.valueOf(value);
            // totRam = totRam / 1024;


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return (totRam * 1024);
    }

    public static long getAvaiableRam(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            activityManager.getMemoryInfo(mi);
        }
        return mi.availMem;
    }

    public static void rateApp(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="
                            + context.getPackageName())));
        }
    }

    public static void removeAds(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store")));
    }

    public static void shareApp(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id="
                        + context.getPackageName());
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void setBatterySaverSelected(Context context, DeviceStatus batterySaver) {
        //set screen timeout
        android.provider.Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT, batterySaver.getLenghtScreenTimeOut());

        //set screen brightness
        if (batterySaver.isAutoScreenBrightness()) {
            int brightness = batterySaver.getLenghtScreenBrightness() * 255 / 100;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                android.provider.Settings.System.putInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        brightness);
            } else {
                android.provider.Settings.System.putInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        brightness);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                android.provider.Settings.System.putInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            } else {
                android.provider.Settings.System.putInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            }
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                android.provider.Settings.System.putInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            } else {
                android.provider.Settings.System.putInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
            int brightness = batterySaver.getLenghtScreenBrightness() * 255 / 100;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                android.provider.Settings.System.putInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        brightness);
            } else {
                android.provider.Settings.System.putInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        brightness);
            }
        }

        //set wifi
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            if (batterySaver.isWifi()) {

            } else {
                wifiManager.setWifiEnabled(false);
            }

        }

        //set bluetooth
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null != bluetoothAdapter) {
            if (batterySaver.isBluetooth()) {

            } else {
                bluetoothAdapter.disable();
            }
        }


        //set auto sync
        ContentResolver.setMasterSyncAutomatically(batterySaver.isAutoSync());

        //set vibrate
        if (batterySaver.isVibration()) {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            } else {
                try {
                    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

                } catch (Exception e) {
                }
            }

        } else {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else {
                try {
                    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                } catch (Exception e) {
                }
            }


        }
    }

    private static void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);
        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
    }

    public static boolean isAndroid26() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean checkTemp(Context mContext) {
        float cpu = Utils.getCpuTemp() / 1000;
        return cpu >= 38;
    }

    public void checkJunk() {

    }

    public static boolean checkMemory(Context mContext) {
        long totalRam = getTotalRam();
        long availableRam = getAvaiableRam(mContext);
        long useRam = totalRam - availableRam;
        return useRam > 68;
    }

    public static boolean checkSystemWritePermission(Context mContext) {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(mContext);
        }
        return retVal;
    }

    public static void openAndroidPermissionsMenu(Context mContext) {
        try {

            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + mContext.getPackageName()));
            mContext.startActivity(intent);
            mContext.startActivity(new Intent(mContext, PermissionActivity.class));

        } catch (Exception e) {
        }
    }

    public static boolean checkShouldDoing(Context mContext, int flag) {
        switch (flag) {
            case 0:
                return SharePreferenceUtils.getInstance(mContext).getOptimizeTime() + AlarmUtils.TIME_SHOULD_DOING_OPTIMIZE < System.currentTimeMillis();
            case 1:
                return SharePreferenceUtils.getInstance(mContext).getBoostTime() + AlarmUtils.TIME_SHOULD_DOING_BOOSTER < System.currentTimeMillis();

            case 2:
                return SharePreferenceUtils.getInstance(mContext).getCoolerTime() + AlarmUtils.TIME_SHOULD_DOING_COOLER < System.currentTimeMillis();
            case 3:
                return SharePreferenceUtils.getInstance(mContext).getCleanTime() + AlarmUtils.TIME_SHOULD_DOING_CLEAN < System.currentTimeMillis();
            case 4:
                return SharePreferenceUtils.getInstance(mContext).getCleanTime() + AlarmUtils.TIME_SHOULD_FAST_CHARGE < System.currentTimeMillis();
            case 5:
                return SharePreferenceUtils.getInstance(mContext).getOptimizeTimeMain() + AlarmUtils.TIME_SHOULD_DOING_OPTIMIZE_MAIN < System.currentTimeMillis();
            case 6:
                return SharePreferenceUtils.getInstance(mContext).getBoostTimeMain() + AlarmUtils.TIME_SHOULD_DOING_BOOSTER_MAIN < System.currentTimeMillis();
            case 7:
                return SharePreferenceUtils.getInstance(mContext).getCoolerTimeMain() + AlarmUtils.TIME_SHOULD_DOING_COOLER_MAIN < System.currentTimeMillis();
            case 8:
                return SharePreferenceUtils.getInstance(mContext).getHideChargeView() + AlarmUtils.TIME_SHOULD_DOING_SHOW_DIALOG_FAST_CHARGE < System.currentTimeMillis();

            default:
                return false;


        }


    }

    public static boolean checkDNDDoing(Context mContext) {
        SharePreferenceUtils sharePreferenceUtils = SharePreferenceUtils.getInstance(mContext);
        Calendar instance = Calendar.getInstance();
        boolean b = true;
        int now = (instance.get(Calendar.HOUR_OF_DAY) * 100) + instance.get(Calendar.MINUTE);
        int dNDStartTime = sharePreferenceUtils.getDndStart();
        int dNDStopTime = sharePreferenceUtils.getDndStop();
        if (dNDStopTime < dNDStartTime) {
            dNDStopTime += 2400;
        }
        if (now < dNDStartTime) {
            now += 2400;
        }
        return now < dNDStartTime || now >= dNDStopTime || !sharePreferenceUtils.getDnd();
    }


    public static boolean isScreenOn(Context mContext) {
        PowerManager powerManager = (PowerManager) mContext.getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();
        return isScreenOn;
    }

    public static boolean getChargeStatus(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        return isCharging;
    }

    public static boolean getChargeFull(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;
    }

    public static String getChargeType(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if (usbCharge) return "USB";
        if (acCharge) return "AC";
        return "AC";
    }

    public static int getTempleCpu(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        return status;
    }

    public static int getBatteryLevel(Context context) {
        try {
            Intent intent = context.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int intExtra = intent.getIntExtra("level", -1);
            int scale = intent.getIntExtra("scale", -1);
            if (intExtra != -1) {
                if (scale != -1) {
                    return (int) ((((float) intExtra) / ((float) scale)) * 100.0f);
                }
            }
        } catch (Exception e) {
        }
        return 50;
    }

    public static void setLocate(Context mContext) {
        String language = SharePreferenceUtils.getInstance(mContext).getLanguage();

        if (SharePreferenceUtils.getInstance(mContext).getFirstRun()) {
            language = Locale.getDefault().getLanguage();
            if (language.equalsIgnoreCase("ar")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("de")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("es")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("fr")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("in")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("it")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("ja")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("ko")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("pt")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("ru")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("th")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("tr")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("vi")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguage(language);
            }
        }
        Locale myLocale = new Locale(language);
        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static void setLocale(Context mContext, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        SharePreferenceUtils.getInstance(mContext).saveLanguage(lang);
    }

    public static int getRamdom(int numer) {
        Random rn = new Random();
        int answer = rn.nextInt(numer);
        return answer;
    }

    public static void intSound(Context mContext) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, notification);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToastMode(Context mContext, int poistion, String text) {
        if (poistion == 0) {

            Toast.makeText(mContext, mContext.getString(R.string.toast_mode_title) + " " + mContext.getString(R.string.super_saving), Toast.LENGTH_LONG).show();
        } else if (poistion == 1) {
            Toast.makeText(mContext, mContext.getString(R.string.toast_mode_title) + " " + mContext.getString(R.string.normal), Toast.LENGTH_LONG).show();
        } else if (poistion == 2) {
            Toast.makeText(mContext, mContext.getString(R.string.toast_mode_title) + " " + mContext.getString(R.string.custom), Toast.LENGTH_LONG).show();
        } else if (poistion == 3) {
            Toast.makeText(mContext, mContext.getString(R.string.toast_mode_title) + " " + mContext.getString(R.string.my_mode), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.toast_mode_title) + " " + text, Toast.LENGTH_LONG).show();
        }
    }
}
