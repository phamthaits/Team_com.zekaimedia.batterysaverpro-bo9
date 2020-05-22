package com.amt.batterysaver.task;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.TextView;

import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.R;

public class TaskChargeDetail extends AsyncTask<Void, Integer, Void> {

    private ActivityManager mActivityManager;
    private OnTaskBoostListener mOnTaskBoostListener;
    private Context mContext;
    boolean flag = false;
    TextView titleApp;

    public TaskChargeDetail(Context context, TextView titleApp, OnTaskBoostListener onTaskBoostListener) {

        mOnTaskBoostListener = onTaskBoostListener;
        mActivityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        mContext = context;
        this.titleApp = titleApp;
        // mProgressBar.setProgress(0);
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int j = 0; j < 100; j++) {
            publishProgress(j);
            try {
                if(j<=40){
                    Thread.sleep(40);
                } else  {
                    Thread.sleep(25);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(values[0]==15){
            titleApp.setText(mContext.getString(R.string.wifi));
            if(!SharePreferenceUtils.getInstance(mContext).getFsWifi()){
                WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    wifiManager.setWifiEnabled(false);
                }

            }
            return;
        }
        if(values[0]==30){
            titleApp.setText(mContext.getString(R.string.bluetooth));
            if(!SharePreferenceUtils.getInstance(mContext).getFsBluetooth()){
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(bluetoothAdapter!=null)
                    bluetoothAdapter.disable();
            }

            return;
        }
        if(values[0]==60){
            titleApp.setText(mContext.getString(R.string.auto_sync));
            if(!SharePreferenceUtils.getInstance(mContext).getFsAutoSync()){
                ContentResolver.setMasterSyncAutomatically(false);
            }


            return;
        }
        if(values[0]==70){
            titleApp.setText(mContext.getString(R.string.screen_brightness));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if(Utils.checkSystemWritePermission(mContext)){
                    android.provider.Settings.System.putInt(mContext.getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                }

            } else {
                android.provider.Settings.System.putInt(mContext.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            }
            return;
        }

        if(values[0]==85){
            titleApp.setText(mContext.getString(R.string.optimize)+" "+mContext.getString(R.string.cpugpuram));
            return;
        }




    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {



        if (null != mOnTaskBoostListener) {
            mOnTaskBoostListener.OnResult();
        }
    }

    public interface OnTaskBoostListener {
        void OnResult();
    }


}