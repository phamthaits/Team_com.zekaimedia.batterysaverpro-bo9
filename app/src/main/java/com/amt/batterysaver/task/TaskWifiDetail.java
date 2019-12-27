package com.amt.batterysaver.task;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.widget.TextView;

import com.amt.batterysaver.R;
import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.Utilsb.Utils;

import java.util.ArrayList;
import java.util.List;

public class TaskWifiDetail extends AsyncTask<Void, Integer, Void> {

    private ActivityManager mActivityManager;
    private OnTaskBoostListener mOnTaskBoostListener;
    private Context mContext;
    boolean flag = false;
    TextView titleApp;
    private WifiManager wifiManager;

    public TaskWifiDetail(Context context, TextView titleApp, WifiManager wifiManager, OnTaskBoostListener onTaskBoostListener) {

        mOnTaskBoostListener = onTaskBoostListener;
        mActivityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        mContext = context;
        this.titleApp = titleApp;
        this.wifiManager = wifiManager;
        // mProgressBar.setProgress(0);
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int j = 0; j < 11; j++) {
            publishProgress(j);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        switch (values[0]) {
            case 0:
                titleApp.setText("is5GHzBandSupported: " + wifiManager.is5GHzBandSupported());
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    titleApp.setText("isEasyConnectSupported: " + wifiManager.isEasyConnectSupported());
                }
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    titleApp.setText("isEnhancedOpenSupported: " + wifiManager.isEnhancedOpenSupported());
                }
                break;
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    titleApp.setText("getMaxNumberOfNetworkSuggestionsPerApp: " + wifiManager.getMaxNumberOfNetworkSuggestionsPerApp());
                }
                break;
            case 4:
                titleApp.setText("isEnhancedPowerReportingSupported: " + wifiManager.isEnhancedPowerReportingSupported());
                break;
            case 5:
                titleApp.setText("isP2pSupported: " + wifiManager.isP2pSupported());
                break;
            case 6:
                titleApp.setText("isPreferredNetworkOffloadSupported: " + wifiManager.isPreferredNetworkOffloadSupported());
                break;
            case 7:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    titleApp.setText("isWpa3SaeSupported: " + wifiManager.isWpa3SaeSupported());
                }
                break;
            case 8:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    titleApp.setText("isWpa3SuiteBSupported: " + wifiManager.isWpa3SuiteBSupported());
                }
                break;
            case 9:
                titleApp.setText("isTdlsSupported: " + wifiManager.isTdlsSupported());
                break;
            case 10:
                titleApp.setText("getNetworkId: " + wifiManager.getConnectionInfo().getNetworkId());
                break;
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