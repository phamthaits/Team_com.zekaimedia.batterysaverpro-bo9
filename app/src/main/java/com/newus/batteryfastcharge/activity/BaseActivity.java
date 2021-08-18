package com.newus.batteryfastcharge.activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.newus.batteryfastcharge.Utilsb.SharePreferenceUtils;
import com.newus.batteryfastcharge.Utilsb.Utils;
import com.newus.batteryfastcharge.fragment.fmBatterySaveMain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.newus.batteryfastcharge.fragment.fmBatterySaveMain.WRITE_PERMISSION_REQUEST;

public class BaseActivity extends AppCompatActivity {

    private static final int PERMISSIONS_DRAW_APPICATION = 1000;
    private static final int PERMISSIONS_USAGE = 1001;
    private List<Callable<Void>> callables = new ArrayList<>();

    public void checkdrawPermission(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));

            startActivityForResult(intent, PERMISSIONS_DRAW_APPICATION);
            Intent intent2 = new Intent(this,PermissionActivity.class);
            intent2.putExtra("text_permission","Allow appear on top");
            startActivity(intent2);
        } else {
            callable.call();
        }
    }

    public void askPermissionUsageSetting(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) && !isUsageAccessAllowed(this)) {
            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), PERMISSIONS_USAGE);
            Intent intent=new Intent(this,PermissionActivity.class);
            intent.putExtra("text_permission","Allow usage data access");
            startActivity(intent);
        } else {
            callable.call();
        }
    }

    public static final boolean isUsageAccessAllowed(Context mContext) {
        boolean granted = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                AppOpsManager appOps = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), mContext.getPackageName());
                if (mode == AppOpsManager.MODE_DEFAULT) {
                    String permissionUsage = "android.permission.PACKAGE_USAGE_STATS";
                    granted = (mContext.checkCallingOrSelfPermission(permissionUsage) == PackageManager.PERMISSION_GRANTED);
                } else {
                    granted = (mode == AppOpsManager.MODE_ALLOWED);
                }
            } catch (Throwable e) {
            }
        } else {
            return true;
        }
        return granted;
    }

    public void writePermission(Context activity, Class aClass) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                checkdrawPermission(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        if (Utils.checkSystemWritePermission(activity)) {
                            activity.startActivity(new Intent(activity, aClass));
                        } else {
                            Utils.openAndroidPermissionsMenu(activity);
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(activity)) {
                    SharePreferenceUtils.getInstance(activity).setStatusPer(true);
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
                    startActivityForResult(intent, WRITE_PERMISSION_REQUEST);
                    startActivity(new Intent(activity, PermissionActivity.class));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSIONS_DRAW_APPICATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this))
                    callListener();
                break;
            case PERMISSIONS_USAGE:
                if (isUsageAccessAllowed(this)) {
                    callListener();
                }
                break;
        }
    }

    private void callListener() {
        for (Callable callable : callables) {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
