package app.tahachi.batterydoctor.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.AlarmManagerCompat;

import app.tahachi.batterydoctor.Receiver.AlarmReceiver;

import java.util.Calendar;

public class AlarmUtils {
    public static final String ACTION_AUTOSTART_ALARM = "com.app.action.alarmmanager";
    public static final int DEFAULT_CODE = 1000;

    public static final String ACTION_REPEAT_SERVICE = "action_repeat_service";
    public static final int TIME_REPREAT_SERVICE = 1000;
    public static final int REPREAT_SERVICE_CODE = 1001;



    public static final String ACTION_CHECK_DEVICE_STATUS="action_check_devic_status";
    public static final int TIME_CHECK_DEVICE_STATUS = 5*60*1000;
    public static final int CHECK_DEVICE_STATUS_CODE = 1002;



    public static final int TIME_SHOULD_DOING_OPTIMIZE = 6*60*60*1000; //3 gio
    public static final int TIME_SHOULD_DOING_COOLER = 7*60*60*1000;// 1 gio
    public static final int TIME_SHOULD_DOING_BOOSTER = 5*60*60*1000;//1 gio

    public static final int TIME_SHOULD_DOING_OPTIMIZE_MAIN = 5*60*1000; //30 phut
    public static final int TIME_SHOULD_DOING_COOLER_MAIN = 5*60*1000;// 30 phut
    public static final int TIME_SHOULD_DOING_BOOSTER_MAIN = 5*60*1000;//30 phut
    public static final int TIME_SHOULD_DOING_SHOW_DIALOG_FAST_CHARGE = 3*60*1000;

    public static final int TIME_SHOULD_DOING_CLEAN = 30*60*1000;// 3 Gio
    public static final int TIME_SHOULD_FAST_CHARGE = 5*60*1000;









    public static void cancel(Context context, String mAction){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_AUTOSTART_ALARM);
        PendingIntent sender = PendingIntent.getBroadcast(context, getRequestCode(mAction), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }



    public static Calendar getCalendar(int tempTime){
        int hourOfDay = tempTime/100;
        int minute = tempTime%100;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis())
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar;

    }

    public static void setAlarm( Context ctx,String mAction,int mTime)  {
        Calendar calendar = getCalendar(mTime);
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        intent.setAction(ACTION_AUTOSTART_ALARM);
        intent.putExtra(mAction, Boolean.TRUE);
        PendingIntent sender = PendingIntent.getBroadcast(ctx, getRequestCode(mAction), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);
        long when = System.currentTimeMillis()+mTime;

         AlarmManagerCompat.setExactAndAllowWhileIdle(alarm, AlarmManager.RTC_WAKEUP, when, sender);


    }


    public static int getRequestCode(String mAction){
        if(mAction.equals(ACTION_REPEAT_SERVICE)){
            return REPREAT_SERVICE_CODE;
        }
        if(mAction.equals(ACTION_CHECK_DEVICE_STATUS)){
            return CHECK_DEVICE_STATUS_CODE;
        }

        return DEFAULT_CODE;

    }


}
