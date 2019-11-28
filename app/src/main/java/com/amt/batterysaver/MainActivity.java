package com.amt.batterysaver;

import com.ads.control.Rate;
import com.ads.control.funtion.UtilsApp;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import com.amt.batterysaver.Utilsb.SharePreferenceUtils;
import com.amt.batterysaver.fragment.fmAppManager;
import com.amt.batterysaver.fragment.fmBatterySaveMain;
import com.amt.batterysaver.fragment.fmChart;
import com.amt.batterysaver.fragment.fmSetting;
import com.amt.fastcharging.batterysaver.R;




public class MainActivity extends AppCompatActivity  {
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
                                UtilsApp.OpenBrower(MainActivity.this,getString(R.string.link_policy));
                                break;
                            case R.id.nav_send:
                                UtilsApp.SendFeedBack(MainActivity.this,"email@gmail.com","MASTER BATTERY feedback");
                                break;
                                default:
                                    break;

                        }

                        mDrawerLayout.closeDrawers();


                        return true;


                    }
                });
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new fmBatterySaveMain());

    }

    @Override
    public void onBackPressed() {
        Rate.Show(this,1);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    fragment = new fmBatterySaveMain();
                    loadFragment(fragment);
                    toolbar.setTitle(getString(R.string.app_name));
                    return true;
                case R.id.navigation_gifts:
                    fragment = new fmChart();
                    loadFragment(fragment);
                    toolbar.setTitle(getString(R.string.title_activity_charge_history));
                    return true;
                case R.id.navigation_app:
                    fragment = new fmAppManager();
                    loadFragment(fragment);
                    toolbar.setTitle(getString(R.string.app_manager));
                    return true;
                case R.id.navigation_cart:
                    fragment = new fmSetting();
                    loadFragment(fragment);
                    toolbar.setTitle(getString(R.string.title_activity_setting));
                    return true;

            }
            return false;
        }
    };
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

}

