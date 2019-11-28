package com.amt.batterysaver.model;

import android.content.pm.ApplicationInfo;

public class AppManager {

    public static final int TYPE_APP_USER = 0;
    public static final int TYPE_APP_SYSTEM = 1;

    private ApplicationInfo applicationInfo;
    private String title;
    private int type;

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
