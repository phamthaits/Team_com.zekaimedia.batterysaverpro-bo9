package com.snowshirt.batterysaver.Utilsb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;

import static android.content.Context.POWER_SERVICE;

public class BatteryPref {
    public static final String BATTERY_PREF = "battery_info"
            + "battery_info".hashCode();
    public static final String EXTRA_LEVEL = "level";
    public static final String EXTRA_TIME_REMAIN = "timeremainning";
    public static final String EXTRA_TIME_CHARGING_AC = "timecharging_ac";
    public static final String EXTRA_TIME_CHARGING_USB = "timecharging_usb";
    public static final String EXTRA_CURENT_TIME1 = "curenttime1";
    public static final String EXTRA_CURENT_TIME2 = "curenttime2";
    public static final String EXTRA_CURENT_TIME_CHARGE_AC = "time_charge_ac";
    public static final String EXTRA_CURENT_TIME_CHARGE_USB = "time_charge_USB";

    public static final String TIMEMAIN1 = "timemain1";
    public static final String TIMESCREENON = "time_Screen_on";
    public static final String TIMESCREENOFF = "time_Screen_on";
    public static long TIME_REMAIN_DEFAULT = 1000 * 3600 * 24 / 100; // 24h - 100%
    public static final long TIME_REMAIN_MIN = 1000 * 3600 * 20 / 100; // 6h - 100%



    public static final long TIME_CHARGING_AC_DEFAULT = 1000 * 3600 * 2 / 100; // 2h - 100%
    public static final long TIME_CHARGING_AC_MIN = 1000 * 3600  / 100; // 1h - 100%
    public static final long TIME_CHARGING_AC_MAX = 1000 * 3600 * 3 / 100; // 3h - 100%

    public static final long TIME_CHARGING_USB_MIN = 1000 * 3600 * 2 / 100; // 2h - 100%
    public static final long TIME_CHARGING_USB_MAX = 1000 * 3600 * 5 / 100; // 5h - 100%
    public static final long TIME_CHARGING_USB_DEFAULT = 1000 * 3600 * 3 / 100; // 3h - 100%




    public static final long TIME_REMAIN_MAX = 1000 * 3600 * 40 / 100; // 60h - 100%


    private static BatteryPref batteryPref;
    static Context mContext;

    private BatteryPref(Context context) {
        long b = Math.round(getBatteryCapacity(context)) * 24 / 3;
        TIME_REMAIN_DEFAULT = TIME_REMAIN_DEFAULT * Math.round(getBatteryCapacity(context)) / 3200;

        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);

        sharedPreferences.edit().putLong(EXTRA_TIME_REMAIN, TIME_REMAIN_DEFAULT).commit();

        if (!sharedPreferences.contains(EXTRA_TIME_REMAIN) || !sharedPreferences.contains(EXTRA_CURENT_TIME1)
                || !sharedPreferences.contains(EXTRA_TIME_CHARGING_AC)
                || !sharedPreferences.contains(EXTRA_TIME_CHARGING_USB)) {


            sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_AC, TIME_CHARGING_AC_DEFAULT).commit();
            sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_USB, TIME_CHARGING_USB_DEFAULT).commit();
        }

    }

    public static double getBatteryCapacity(Context context) {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
//            Toast.makeText(MainActivity.this, batteryCapacity + " mah",
//                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static BatteryPref initilaze(Context context) {
        mContext = context;

        if (batteryPref == null)
            batteryPref = new BatteryPref(mContext);
        return batteryPref;
    }

    public void putLevel(Context context, int level) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);
        if (level != getLevel(context)) {
            sharedPreferences.edit().putInt(EXTRA_LEVEL, level).commit();
        }
    }

    public void setScreenOn() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(TIMESCREENON, System.currentTimeMillis()).commit();
    }

    public void setScreenOff() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(TIMESCREENOFF, System.currentTimeMillis()).commit();
    }


    /**
     * @param context
     * @param level
     * @return time remain by minute
     */

    public int getTimeRemainning(Context context, int level) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);
        int time;
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();
        time = (int) (level * sharedPreferences.getLong(EXTRA_TIME_REMAIN, TIME_REMAIN_DEFAULT) / (1000 * 60));
        if (getLevel(context) == -1) {
            putLevel(context, level);

        } else {
            if (level < getLevel(context)) {
                sharedPreferences.edit().putInt(EXTRA_LEVEL, level).commit();
                if (sharedPreferences.getLong(EXTRA_CURENT_TIME1, 0) == 0 || sharedPreferences.getLong(EXTRA_CURENT_TIME2, 0) == 0) {

                    if (sharedPreferences.getLong(EXTRA_CURENT_TIME1, 0) == 0) {
                        sharedPreferences.edit().putLong(EXTRA_CURENT_TIME1, System.currentTimeMillis()).commit();
                        return time;
                    }
                    if (sharedPreferences.getLong(EXTRA_CURENT_TIME2, 0) == 0) {
                        //Tinh so 1 luu lai
                        sharedPreferences.edit().putLong(TIMEMAIN1, System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME1, System.currentTimeMillis())).commit();
                        sharedPreferences.edit().putLong(EXTRA_CURENT_TIME2, System.currentTimeMillis()).commit();


                        return time;
                    }



                    return time;
                }

                long timeRemain1 = sharedPreferences.getLong(TIMEMAIN1, 0);
                long timeRemain2 = System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME2, System.currentTimeMillis());

                long timeRemain = (timeRemain1 + timeRemain2 + TIME_REMAIN_DEFAULT+sharedPreferences.getLong(EXTRA_TIME_REMAIN, TIME_REMAIN_DEFAULT)) / 3;
                if(timeRemain > TIME_REMAIN_MIN){
                    if(timeRemain < TIME_REMAIN_MAX){
                        sharedPreferences.edit().putLong(EXTRA_TIME_REMAIN, timeRemain).commit();
                    }else{
                        sharedPreferences.edit().putLong(EXTRA_TIME_REMAIN, TIME_REMAIN_MAX/2).commit();
                    }
                }


                sharedPreferences.edit().putLong(TIMEMAIN1, timeRemain1).commit();
                sharedPreferences.edit().putLong(EXTRA_CURENT_TIME2, System.currentTimeMillis()).commit();

            }


        }
        return time;


    }
    //                }
    public  int getTimeChargingUsb(Context context,int level){
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);

        int time;
        time = (int) ((100-level) * sharedPreferences.getLong(EXTRA_TIME_CHARGING_USB,TIME_CHARGING_USB_DEFAULT)  / (1000 * 60));
        if(level>getLevel(context)){
            sharedPreferences.edit().putInt(EXTRA_LEVEL, level).commit();

            long timeRemain = System.currentTimeMillis()- sharedPreferences.getLong(EXTRA_CURENT_TIME_CHARGE_USB,	System.currentTimeMillis());
            if (timeRemain <TIME_CHARGING_USB_MAX && timeRemain > TIME_CHARGING_USB_MIN) {
                sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_USB, timeRemain).commit();
            }

        }
        return time;

    }


    public  int getTimeChargingAc(Context context,int level){
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);

        int time;
        time = (int) ((100-level) * sharedPreferences.getLong(EXTRA_TIME_CHARGING_AC,TIME_CHARGING_AC_DEFAULT) / (1000 * 60));
        if(level>getLevel(context)){
            sharedPreferences.edit().putInt(EXTRA_LEVEL, level).commit();

            long timeRemain = System.currentTimeMillis()- sharedPreferences.getLong(EXTRA_CURENT_TIME_CHARGE_AC,	System.currentTimeMillis());
            if (timeRemain <TIME_CHARGING_AC_MAX && timeRemain >TIME_CHARGING_AC_MIN) {
                sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_AC, timeRemain).commit();
            }

        }

        return time;
    }

    public  int getLevel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                BATTERY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(EXTRA_LEVEL, -1);
    }

}
