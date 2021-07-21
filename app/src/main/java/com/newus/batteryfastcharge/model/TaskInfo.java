package com.newus.batteryfastcharge.model;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;

public class TaskInfo implements Parcelable {

    private ApplicationInfo appinfo;
    public long mem;
    private PackagesInfo pkgInfo;

    private PackageManager pm;
    private ActivityManager.RunningAppProcessInfo runinfo;
    private String title;
    private CheckBox chkTask;
    private boolean chceked;
    private int pid;

    public TaskInfo() {

    }

    public TaskInfo(Context context,
                    ActivityManager.RunningAppProcessInfo runinfo) {

        this.appinfo = null;
        this.pkgInfo = null;
        this.title = null;
        this.runinfo = runinfo;
        this.pm = context.getApplicationContext()
                .getPackageManager();
    }

    public TaskInfo(Context context, ApplicationInfo appinfo) {

        this.appinfo = null;
        this.pkgInfo = null;
        this.runinfo = null;
        this.title = null;
        this.appinfo = appinfo;
        this.pm = context.getApplicationContext()
                .getPackageManager();
    }

    protected TaskInfo(Parcel in) {
        appinfo = in.readParcelable(ApplicationInfo.class.getClassLoader());
        mem = in.readLong();
        runinfo = in.readParcelable(ActivityManager.RunningAppProcessInfo.class.getClassLoader());
        title = in.readString();
        chceked = in.readByte() != 0;
        pid = in.readInt();
    }

    public static final Creator<TaskInfo> CREATOR = new Creator<TaskInfo>() {
        @Override
        public TaskInfo createFromParcel(Parcel in) {
            return new TaskInfo(in);
        }

        @Override
        public TaskInfo[] newArray(int size) {
            return new TaskInfo[size];
        }
    };

//    public void getAppInfo() {
//        if (appinfo == null) {
//            try {
//                String s = runinfo.processName;
//                this.appinfo = pm.getApplicationInfo(s, 128);
//            } catch (Exception e) {
//            }
//        }
//    }

    public int getIcon() {
        return appinfo.icon;
    }

    public String getPackageName() {
        return appinfo.packageName;
    }

    public String getTitle() {

        if (title == null) {
            try {
                this.title = appinfo.loadLabel(pm).toString();
            } catch (Exception e) {

            }
        }
        return title;
    }

    public void setMem(long mem) {
        this.mem = mem;
    }

    public long getMem() {
        return mem;
    }

    public boolean isGoodProcess() {

        return appinfo != null;
    }

    public ApplicationInfo getAppinfo() {
        return appinfo;
    }

    public void setAppinfo(ApplicationInfo appinfo) {
        this.appinfo = appinfo;
    }

    public PackagesInfo getPkgInfo() {
        return pkgInfo;
    }

    public void setPkgInfo(PackagesInfo pkgInfo) {
        this.pkgInfo = pkgInfo;
    }

    public ActivityManager.RunningAppProcessInfo getRuninfo() {
        return runinfo;
    }

    public void setRuninfo(ActivityManager.RunningAppProcessInfo runinfo) {
        this.runinfo = runinfo;
    }

    public boolean isChceked() {
        return chceked;
    }

    public void setChceked(boolean chceked) {
        this.chceked = chceked;
    }

    public void setChkTask(CheckBox chkTask) {
        this.chkTask = chkTask;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(appinfo, flags);
        dest.writeLong(mem);
        dest.writeParcelable(runinfo, flags);
        dest.writeString(title);
        dest.writeByte((byte) (chceked ? 1 : 0));
        dest.writeInt(pid);
    }
}
