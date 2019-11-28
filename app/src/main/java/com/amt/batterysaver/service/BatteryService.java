package com.amt.batterysaver.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import com.amt.batterysaver.notification.NotificationBattery;
import com.amt.batterysaver.Alarm.AlarmUtils;
import com.amt.batterysaver.Receiver.BatteryStatusReceiver;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.task.TaskScreenOff;

public class BatteryService  extends Service

    {
        public static final String ACTION_MAX_BATTERY_CHANGED = "ACTION_MAX_BATTERY_CHANGED";
        /**
         * khi có thông báo từ Intent.ACTION_MAX_BATTERY_CHANGED , dữ liệu sẽ được gửi tới activity bằng cách sử dụng
         * ACTION_MAX_BATTERY_CHANGED_SEND
         */
        public static final String ACTION_MAX_BATTERY_CHANGED_SEND = "ACTION_MAX_BATTERY_CHANGED_SEND";
        /**
         * được sử dụng khi ứng dụng bị ngư�?i dùng nhấn back. kill activity chính đi. khi đó, nếu ngư�?i
         * dùng mở lại ứng dụng, thì nếu ko có ACTION_MAX_BATTERY_NEED_UPDATE thì ứng dụng sẽ mất th�?i gian ch�?
         * cho tới khi Intent.ACTION_MAX_BATTERY_CHANGED có thông báo mới v?.
         */
        public static final String ACTION_MAX_BATTERY_NEED_UPDATE = "ACTION_MAX_BATTERY_NEED_UPDATE";


        public static final String NOTIFY_HOME = "com.maxbattery.main";

        BatteryStatusReceiver mBatteryStatusReceiver;
        TaskScreenOff mTaskScreenOff ;






        @Override
        public void onCreate() {
        super.onCreate();
//            if (Build.VERSION.SDK_INT >= 26) {
//                String str = "my_channel_01";
//                ((NotificationManager) getSystemService("notification")).createNotificationChannel(new NotificationChannel(str, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT));
//                startForeground(1,new NotificationCompat.Builder(this,"Channel human readable title").setOngoing(true).build());
//            }

            if(SharePreferenceUtils.getInstance(this ).getNotification()){
                startForeground(NotificationBattery.NOTIFYCATION_BATTERY_ID, NotificationBattery.getInstance(BatteryService.this).build() );
            }else{
                String str = "notification_channel_id";
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"notification_channel_id");
                    Notification mNotification = mBuilder.setOngoing(false)
                            .setCategory(NotificationCompat.CATEGORY_SERVICE)
                            .setSmallIcon(android.R.color.transparent)
                            .build();
                  //  BatteryService.this.startForeground(1100, new Builder(BatteryService.this.getApplicationContext(), "notification_channel_id").build());
                    BatteryService.this.startForeground(1100, mNotification);


                }
            }
        resScreen();
        mBatteryStatusReceiver = new BatteryStatusReceiver();
        mBatteryStatusReceiver.OnCreate(this);
        AlarmUtils.setAlarm(this,AlarmUtils.ACTION_CHECK_DEVICE_STATUS, AlarmUtils.TIME_CHECK_DEVICE_STATUS);

    }

        public void cancelNotification(int i) {
            try {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                stopForeground(true);
                notificationManager.cancel(i);
            } catch (Exception e) {
            }
        }

//        @TargetApi(26)
//public void createChannel(){
//        NotificationChannel notificationChannel = new NotificationChannel("notification_channel_id", getString(R.string.app_name), 2);
//        notificationChannel.setDescription("description");
//        ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
//    }



        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            return START_STICKY;
    }
        @Override
        public void onTaskRemoved(Intent rootIntent) {
        AlarmUtils.setAlarm(this,AlarmUtils.ACTION_REPEAT_SERVICE,AlarmUtils.TIME_REPREAT_SERVICE);
        super.onTaskRemoved(rootIntent);
    }
        @Override
        public void onDestroy() {
        super.onDestroy();
        if(mBatteryStatusReceiver!=null){
            mBatteryStatusReceiver.OnDestroy(getApplicationContext());
            mBatteryStatusReceiver = null;
        }
        cancleTask();
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
            AlarmUtils.setAlarm(this,AlarmUtils.ACTION_REPEAT_SERVICE,AlarmUtils.TIME_REPREAT_SERVICE);


    }


        @Override
        public IBinder onBind(Intent intent) {
        return null;
    }
        @Override
        public boolean onUnbind(Intent intent) {
            return false;
        }
        private BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {

                if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                    if(SharePreferenceUtils.getInstance(context).getKillApp()){
                        mTaskScreenOff = new TaskScreenOff(context);
                        mTaskScreenOff.execute();

                    }
                    AlarmUtils.cancel(context,AlarmUtils.ACTION_CHECK_DEVICE_STATUS);

                }else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){

                    SharePreferenceUtils.getInstance(context).setLevelScreenOn(Utils.getBatteryLevel(context));
                    AlarmUtils.setAlarm(context,AlarmUtils.ACTION_CHECK_DEVICE_STATUS,AlarmUtils.TIME_CHECK_DEVICE_STATUS);
                }
                else if(intent.getAction().equals(NotificationBattery.UPDATE_NOTIFICATION_ENABLE)){

                    switch (intent.getIntExtra("NOTIFICATION_UPDATE_MODE", 0)) {
                        case 0:
                            startForeground(NotificationBattery.NOTIFYCATION_BATTERY_ID, NotificationBattery.getInstance(context).build() );
                            Intent i = new Intent();
                            intent.setAction(BatteryService.ACTION_MAX_BATTERY_NEED_UPDATE);
                            sendBroadcast(i);
                            break;
                        case 1:
                            cancelNotification(NotificationBattery.NOTIFYCATION_BATTERY_ID);

                            break;
                        default:
                            break;
                    }
                }

            }
        };
        public void cancleTask() {
        if (mTaskScreenOff != null
                && mTaskScreenOff.getStatus() == AsyncTask.Status.RUNNING) {
            mTaskScreenOff.cancel(true);
            mTaskScreenOff = null;
        }
    }
        public void  resScreen(){
            IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
            filter.addAction("android.intent.action.SCREEN_OFF");
            filter.addAction(NotificationBattery.UPDATE_NOTIFICATION_ENABLE);

        this.registerReceiver(mReceiver, filter);
    }
    }
