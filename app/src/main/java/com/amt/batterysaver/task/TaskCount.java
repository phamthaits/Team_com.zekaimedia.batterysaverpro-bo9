package com.amt.batterysaver.task;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ServiceInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;

import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.model.TaskInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskCount extends AsyncTask<Void, Integer, Void> {

    private ActivityManager mActivityManager;
    private OnTaskCountListener mOnTaskCountListener;
    private Context mContext;
    boolean flag = false;
    private PackageManager mPackageManager;
    int count;

    public TaskCount(Context context ,OnTaskCountListener onTaskCountListener) {

        mOnTaskCountListener = onTaskCountListener;
        mActivityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        mContext = context;
        mActivityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        mPackageManager = context.getPackageManager();
        count = 0 ;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
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
                    if (!packagename.contains(mContext.getPackageName()) && applicationInfo != null&& Utils.isUserApp(applicationInfo) ) {
                        TaskInfo info = new TaskInfo(mContext, applicationInfo);
                        mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                        count++;
                        publishProgress(count);

                    }
                } catch (Exception e){
                    continue;
                }
            } while (true);
        } else {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                for (ActivityManager.RunningServiceInfo runningServiceInfo : am.getRunningServices(Integer.MAX_VALUE)) {
                    try {
                        if (mPackageManager == null) break;
                        PackageInfo packageInfo = mPackageManager.getPackageInfo(runningServiceInfo.service.getPackageName(), PackageManager.GET_ACTIVITIES);
                        if (packageInfo == null) continue;
                        ApplicationInfo applicationInfo;
                        applicationInfo = mPackageManager.getApplicationInfo(packageInfo.packageName, 0);
                        if (applicationInfo == null) continue;
                        if (!packageInfo.packageName.contains(mContext.getPackageName()) && applicationInfo != null &&Utils.isUserApp(applicationInfo)) {
                            TaskInfo info = new TaskInfo(mContext, applicationInfo);
                            mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                            count++;
                            publishProgress(count);


                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }else{
                int flags = PackageManager.GET_ACTIVITIES
                        | PackageManager.GET_CONFIGURATIONS
                        | PackageManager.GET_DISABLED_COMPONENTS
                        | PackageManager.GET_GIDS | PackageManager.GET_INSTRUMENTATION
                        | PackageManager.GET_INTENT_FILTERS
                        | PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS
                        | PackageManager.GET_RECEIVERS | PackageManager.GET_SERVICES
                        | PackageManager.GET_SIGNATURES;
                PackageManager packageManager = mContext.getPackageManager();
                List<PackageInfo> installedPackages = packageManager
                        .getInstalledPackages(flags);
                for (PackageInfo packageInfo : installedPackages) {
                    if (mPackageManager == null) break;
                    try {
                        ApplicationInfo applicationInfo;
                        applicationInfo = mPackageManager.getApplicationInfo(packageInfo.packageName, 0);

                        ServiceInfo[] services = packageInfo.services;
                        if(services!=null){
                            if(services.length>0){
                                if (!packageInfo.packageName.contains(mContext.getPackageName()) && applicationInfo != null&&Utils.isUserApp(applicationInfo) ) {
                                    TaskInfo info = new TaskInfo(mContext, applicationInfo);
                                    mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                                    count++;
                                    publishProgress(count);

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
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            count++;
            publishProgress(count);

        }

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter!=null){
            if (bluetoothAdapter.isEnabled()) {
                count++;
                publishProgress(count);


            }

        }
        if(ContentResolver.getMasterSyncAutomatically()){
            count++;
            publishProgress(count);

        }






        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {


        if (null != mOnTaskCountListener) {
            mOnTaskCountListener.OnResult(count);
        }
    }

    public interface OnTaskCountListener {
        void OnResult(int count);
    }


}