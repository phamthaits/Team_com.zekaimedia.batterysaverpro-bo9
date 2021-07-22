package com.newus.batteryfastcharge;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ads.control.AdControl;
import com.ads.control.Rate;
import com.ads.control.funtion.UtilsApp;
import com.google.android.material.navigation.NavigationView;
import com.newus.batteryfastcharge.Utilsb.SharePreferenceUtils;
import com.newus.batteryfastcharge.activity.BaseActivity;
import com.newus.batteryfastcharge.activity.BoostActivity;
import com.newus.batteryfastcharge.activity.CleanActivity;
import com.newus.batteryfastcharge.activity.CoolActivity;
import com.newus.batteryfastcharge.billing.RemoveAdsActivity;
import com.newus.batteryfastcharge.fragment.fmBatterySaveMain;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    Fragment fragment;
    private Button bt_RemoveAds;
    SharedPreferences appPreferences;
    boolean isAppInstalled = false;
    private Context context;
    private AdControl adControl;

    private void addShourcut() {
        /**
         * check if application is running first time, only then create shorcut
         */
        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAppInstalled = appPreferences.getBoolean("isAppInstalled", false);
        if (isAppInstalled == false) {
            /**
             * create short code
             */
            Intent shortcutIntentMain = new Intent(getApplicationContext(), MainActivity.class);
            shortcutIntentMain.setAction(Intent.ACTION_MAIN);
            Intent intentMain = new Intent();
            intentMain.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntentMain);
            intentMain.putExtra(Intent.EXTRA_SHORTCUT_NAME, this.getString(R.string.app_name));
            intentMain.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
            intentMain.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intentMain);

            Intent shortcutIntentCleaner = new Intent(getApplicationContext(), CleanActivity.class);
            shortcutIntentCleaner.setAction(Intent.ACTION_MAIN);
            Intent intentCleaner = new Intent();
            intentCleaner.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntentCleaner);
            intentCleaner.putExtra(Intent.EXTRA_SHORTCUT_NAME, this.getString(R.string.junk_clean_nav));
            intentCleaner.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_clean_trash_info));
            intentCleaner.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intentCleaner);

            Intent shortcutIntentBoost = new Intent(getApplicationContext(), BoostActivity.class);
            shortcutIntentBoost.setAction(Intent.ACTION_MAIN);
            Intent intentBoost = new Intent();
            intentBoost.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntentBoost);
            intentBoost.putExtra(Intent.EXTRA_SHORTCUT_NAME, this.getString(R.string.phone_boost_nav));
            intentBoost.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_memory_boost_info));
            intentBoost.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intentBoost);

            Intent shortcutIntentCooler = new Intent(getApplicationContext(), CoolActivity.class);
            shortcutIntentCooler.setAction(Intent.ACTION_MAIN);
            Intent intentCooler = new Intent();
            intentCooler.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntentCooler);
            intentCooler.putExtra(Intent.EXTRA_SHORTCUT_NAME, this.getString(R.string.phone_cool_nav));
            intentCooler.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_temperature_info));
            intentCooler.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intentCooler);
            /**
             * Make preference true
             */
            SharedPreferences.Editor editor = appPreferences.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        adControl = AdControl.getInstance(this);
        bt_RemoveAds = findViewById(R.id.bt_removeads);
        bt_RemoveAds.setVisibility(AdControl.getInstance(this).remove_ads() ? View.GONE : View.VISIBLE);
        toolbar.setTitle(getString(R.string.app_name));

        /* --------- Dialog Update Version ------------------- */
        Dialog tv_version = new Dialog(this);
        tv_version.setCanceledOnTouchOutside(false);
        tv_version.setCancelable(false);

        tv_version.setContentView(R.layout.tips_new_version);
        tv_version.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (adControl.isUpdate()) {
            adControl.forceUpdateFirebase(true);
            Log.v("isUpdate", "đã gọi dialog");
            tv_version.show();
            TextView btnUpdate = tv_version.findViewById(R.id.btn_ok);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    if (!adControl.version_update_url().equals(""))
                        uri = Uri.parse(adControl.version_update_url());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        } else adControl.forceUpdateFirebase(false);
        /*--------------------------------------------------------*/

        bt_RemoveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển  đến trang mua ứng dụng.
                Intent mIntent = new Intent(context, RemoveAdsActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
            }
        });
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_share:
                                UtilsApp.shareApp(MainActivity.this);
                                break;
                            case R.id.nav_policy:
                                UtilsApp.OpenBrower(MainActivity.this, getString(R.string.link_policy));
                                break;
                            case R.id.nav_send:
                                UtilsApp.OpenEmail(MainActivity.this);
                                break;
                            case R.id.nav_boost:
//                                SharePreferenceUtils.getInstance(MainActivity.this).setFlagAds(true);
                                startActivity(new Intent(MainActivity.this, BoostActivity.class));
                                break;
                            case R.id.nav_cool:
//                                SharePreferenceUtils.getInstance(MainActivity.this).setFlagAds(true);
                                startActivity(new Intent(MainActivity.this, CoolActivity.class));
                                break;
                            case R.id.nav_junk_clean:
//                                SharePreferenceUtils.getInstance(MainActivity.this).setFlagAds(true);
                                startActivity(new Intent(MainActivity.this, CleanActivity.class));
                                break;
                            case R.id.nav_more_app:
                                String url = getString(R.string.nav_more_app);
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                break;
                            default:
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        addShourcut();
//        BottomNavigationView navigation = findViewById(R.id.navigation);
//        navigation.setItemIconTintList(null);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new fmBatterySaveMain());
    }

    @Override
    public void onBackPressed() {
        Log.v("Thaidaica", "Đã gọi show");
        Rate.Show(this, 1, this);
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_shop:
//                    fragment = new fmBatterySaveMain();
//                    loadFragment(fragment);
//                    toolbar.setTitle(getString(R.string.app_name));
//                    return true;
//                case R.id.navigation_gifts:
//                    fragment = new ChartActivity();
//                    loadFragment(fragment);
//                    toolbar.setTitle(getString(R.string.title_activity_charge_history));
//                    return true;
//                case R.id.navigation_app:
//                    fragment = new AppManagerActivity();
//                    loadFragment(fragment);
//                    toolbar.setTitle(getString(R.string.app_manager));
//                    return true;
//                case R.id.navigation_cart:
//                    fragment = new fmSetting();
//                    loadFragment(fragment);
//                    toolbar.setTitle(getString(R.string.title_activity_setting));
//                    return true;
//            }
//            return false;
//        }
//    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        SharePreferenceUtils.getInstance(this).setFlagAds(false);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
