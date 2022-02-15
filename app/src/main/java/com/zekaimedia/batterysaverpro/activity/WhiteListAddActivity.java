package com.zekaimedia.batterysaverpro.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zekaimedia.batterysaverpro.R;
import com.zekaimedia.batterysaverpro.Utilsb.PreferAppList;
import com.zekaimedia.batterysaverpro.Utilsb.Utils;
import com.zekaimedia.batterysaverpro.adapter.ManagerConnect;
import com.zekaimedia.batterysaverpro.adapter.WhiteListAddAdapter;
import com.zekaimedia.batterysaverpro.model.Whitelist;

import java.util.ArrayList;
import java.util.List;

public class WhiteListAddActivity extends AppCompatActivity {
    private FrameLayout mFrameLayout;
    private RecyclerView mRecyclerViewWhiteList;
    private TextView mTvNoItem;
    private LinearLayout mViewAdd;

    private List<Whitelist> mWhitelists = new ArrayList<>();
    private WhiteListAddAdapter mAdapter;

    private PackageManager mPackageManager;

    private Handler mHandlerLocal = new Handler(Looper.getMainLooper());
    CheckBox checkBoxAddWhiteList;
    PreferAppList mPreferenceUtil;
    ProgressBar mProgressBarLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_white_list_add);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_white_list));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_48dp);*/

        /* ------------------- StatusBar Navigation text dark bg white ----------------- */

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        /* ------------------------------------------------------------------ */

        intView();
        intEvent();
        intData();
        LinearLayout icBack = findViewById(R.id.lr_back);
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    public void intView() {
        mProgressBarLoading = findViewById(R.id.progressBarLoading);
        mPackageManager = getPackageManager();
        mFrameLayout = findViewById(R.id.recyclerViewWhiteList);
        mRecyclerViewWhiteList = new RecyclerView(WhiteListAddActivity.this);
        mFrameLayout.addView(mRecyclerViewWhiteList);
        mTvNoItem = findViewById(R.id.tvNoItem);
        mViewAdd = findViewById(R.id.viewAdd);
        checkBoxAddWhiteList = findViewById(R.id.checkBoxAddWhiteList);

    }

    public void intData() {

        mPreferenceUtil = new PreferAppList();
        mAdapter = new WhiteListAddAdapter(mWhitelists,
                new WhiteListAddAdapter.OnHandleItemClickListener() {
                    @Override
                    public void onClickCheckBox(boolean isCheck, int position) {
                        mWhitelists.get(position).setIsCheck(!isCheck);
                        mAdapter.notifyDataSetChanged();
                    }
                });
        mRecyclerViewWhiteList.setLayoutManager(new LinearLayoutManager(
                this));
        mRecyclerViewWhiteList.setAdapter(mAdapter);

        loadData();
    }

    public void intEvent() {
        checkBoxAddWhiteList
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        for (int i = 0; i < mWhitelists.size(); i++) {
                            mWhitelists.get(i).setIsCheck(isChecked);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
        findViewById(R.id.btnAddWhiteListAdd).

                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           for (Whitelist whitelist : mWhitelists) {
                                               if (whitelist.isCheck()) {
                                                   mPreferenceUtil.addLocked(WhiteListAddActivity.this,
                                                           whitelist.getPackageName());
                                               }
                                           }
                                           onBackPressed();
                                       }
                                   }

                );
    }

    private void loadData() {
        ManagerConnect managerConnect = new ManagerConnect();
        managerConnect.getListManager(this,
                new ManagerConnect.OnManagerConnectListener() {
                    @Override
                    public void OnResultManager(
                            final List<ApplicationInfo> result) {
                        Runnable runnableLocal = new Runnable() {
                            @Override
                            public void run() {
                                if (result.size() != 0) {
                                    mProgressBarLoading.setVisibility(View.GONE);
                                    for (ApplicationInfo applicationInfo : result) {
                                        if (Utils.isUserApp(applicationInfo)
                                                && !Utils
                                                .checkLockedItem(
                                                        WhiteListAddActivity.this,
                                                        applicationInfo.packageName)) {
                                            mWhitelists
                                                    .add(new Whitelist(
                                                            applicationInfo.packageName,
                                                            applicationInfo
                                                                    .loadLabel(
                                                                            mPackageManager)
                                                                    .toString(),
                                                            applicationInfo
                                                                    .loadIcon(mPackageManager),
                                                            false));
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }
                                if (mWhitelists.size() == 0) {
                                    mFrameLayout.setVisibility(View.GONE);
                                    mViewAdd.setVisibility(View.GONE);
                                    mTvNoItem.setVisibility(View.VISIBLE);
                                } else {
                                    mFrameLayout.setVisibility(View.VISIBLE);
                                    mViewAdd.setVisibility(View.VISIBLE);
                                    mTvNoItem.setVisibility(View.GONE);
                                }
                            }
                        };
                        mHandlerLocal.postDelayed(runnableLocal, 100);
                    }
                }

        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                break;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

}
