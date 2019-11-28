package com.amt.batterysaver.task;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ServiceInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.TextView;

import com.amt.batterysaver.Utilsb.Utils;
import com.amt.batterysaver.model.TaskInfo;
import com.amt.fastcharging.batterysaver.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskCharge extends AsyncTask<Void, TaskInfo, ArrayList<TaskInfo>> {
    private Context mContext;
    private PackageManager mPackageManager;
    private OnTaskListListener mOnTaskListListener;
    private long mTotal;
    TextView titleApp;
    ActivityManager mActivityManager;


    public TaskCharge(Context context, TextView titleApp, OnTaskListListener onTaskListListener) {
        mContext = context;
        mOnTaskListListener = onTaskListListener;
        mPackageManager = context.getPackageManager();
        this.titleApp = titleApp;
        mActivityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ArrayList<TaskInfo> doInBackground(Void... arg0) {
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
                    if (!packagename.contains(mContext.getPackageName()) && applicationInfo != null&& Utils.isUserApp(applicationInfo)&&!Utils.checkLockedItem(mContext, packagename)) {
                        TaskInfo info = new TaskInfo(mContext, applicationInfo);
                        mActivityManager.killBackgroundProcesses(info.getAppinfo().packageName);
                        publishProgress(info);
                        try {

                            Thread.sleep(150);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
                        if (!packageInfo.packageName.contains(mContext.getPackageName()) && applicationInfo != null &&Utils.isUserApp(applicationInfo)&&!Utils.checkLockedItem(mContext, packageInfo.packageName)) {
                            TaskInfo info = new TaskInfo(mContext, applicationInfo);
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
                                if (!packageInfo.packageName.contains(mContext.getPackageName()) && applicationInfo != null &&Utils.isUserApp(applicationInfo)&&!Utils.checkLockedItem(mContext, packageInfo.packageName)) {
                                    TaskInfo info = new TaskInfo(mContext, applicationInfo);
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
        super.onProgressUpdate(values);
        getImageApp(values[0]);

    }

    @Override
    protected void onPostExecute(ArrayList<TaskInfo> taskInfos) {

        if (null != mOnTaskListListener) {
            mOnTaskListListener.OnResult();
        }
    }

    public interface OnTaskListListener {
        void OnResult();
    }
    public void getImageApp(TaskInfo info){
        titleApp.setText(mContext.getString(R.string.pc_scanning)+": " +info.getTitle());

    }




}

