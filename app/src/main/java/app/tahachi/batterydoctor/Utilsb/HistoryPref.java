package app.tahachi.batterydoctor.Utilsb;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class HistoryPref {

    public static final int NUMBER_POINT_IN_PER_4_HOUR = 4; // 60 minute 1 điểm

    private static final String HISTORY_PREF = "history_info" + "history_info".hashCode();
    public static final int DEFAULT_LEVEL = -1;

    public static void putLevel(Context context, int level) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int minute = calendar1.get(Calendar.MINUTE);
        if (minute > 3 && minute < 57){
            calendar1.add(Calendar.HOUR_OF_DAY, 1);
            int day = calendar1.get(Calendar.DAY_OF_MONTH);
            int hour = calendar1.get(Calendar.HOUR_OF_DAY);
            putTimeNow(context, day, hour, level);
            SharedPreferences sharedPreferences = context.getSharedPreferences(HISTORY_PREF, Context.MODE_PRIVATE);
            calendar2.add(Calendar.HOUR_OF_DAY, -2);
            int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
            int hour2 = calendar2.get(Calendar.HOUR_OF_DAY);
            removeLevel(context,getKeyFromTimeNow(day2,hour2));

            return;
        }
        //  putTimeNow()
        //luu lai o cho nay


        if (minute >= 57)
            calendar1.add(Calendar.HOUR_OF_DAY, 1);
        int day = calendar1.get(Calendar.DAY_OF_MONTH);
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        putTimeNow(context, day, hour, level);
        SharedPreferences sharedPreferences = context.getSharedPreferences(HISTORY_PREF, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(getKeyFromTime(day, hour))) {
            return;
        }
        putLevel(context, day, hour, level);
    }

    public static void putTimeNow(Context context, int date, int hour, int level) {
        SharedPreferences.Editor editor = context.getSharedPreferences(HISTORY_PREF, Context.MODE_PRIVATE).edit();
        editor.putInt("bat_time_now_" + date + "_" + hour, level);

        editor.apply();
    }


    public static void putLevel(Context context, int date, int hour, int level) {
        SharedPreferences.Editor editor = context.getSharedPreferences(HISTORY_PREF, Context.MODE_PRIVATE).edit();
        editor.putInt("bat_time_" + date + "_" + hour, level);

        editor.apply();
    }

    public static int getLevel(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HISTORY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, DEFAULT_LEVEL);
    }

    public static void removeLevel(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HISTORY_PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).apply();
    }

    public static String getKeyFromTime(int date, int hour) {
        return "bat_time_" + date + "_" + hour;
    }
    public static String getKeyFromTimeNow(int date, int hour) {
        return "bat_time_now_" + date + "_" + hour;
    }
}

