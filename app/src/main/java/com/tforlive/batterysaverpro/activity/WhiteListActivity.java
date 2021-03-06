package com.tforlive.batterysaverpro.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tforlive.batterysaverpro.R;
import com.tforlive.batterysaverpro.Utilsb.PreferAppList;
import com.tforlive.batterysaverpro.Utilsb.Utils;
import com.tforlive.batterysaverpro.adapter.WhiteListAdapter;
import com.tforlive.batterysaverpro.model.Whitelist;

import java.util.ArrayList;
import java.util.List;

public class WhiteListActivity extends AppCompatActivity {


    private FrameLayout mFrameLayout;
    private RecyclerView mRecyclerViewWhiteList;
    private TextView mTvNoItem;

    private List<Whitelist> mWhitelists = new ArrayList<>();
    private WhiteListAdapter mAdapter;

    private PackageManager mPackageManager;
    PreferAppList mPreferenceUtil;
    ProgressBar mProgressBarLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_white_list);
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

    public void intView(){
        mProgressBarLoading = findViewById(R.id.progressBarLoading);
        mPackageManager = getPackageManager();
        mFrameLayout = findViewById(R.id.recyclerViewWhiteList);
        mRecyclerViewWhiteList = new RecyclerView(this);
        mFrameLayout.addView(mRecyclerViewWhiteList);
        mTvNoItem = findViewById(R.id.tvNoItem);

    }
    public void intEvent(){
        findViewById(R.id.btnAddWhiteList).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(WhiteListActivity.this,WhiteListAddActivity.class));

                    }
                });
    }
    public void intData(){

        mPreferenceUtil = new PreferAppList();
        mAdapter = new WhiteListAdapter(mWhitelists,

                new WhiteListAdapter.OnHandleItemClickListener() {
                    @Override
                    public void onClickRemove(int position) {
                        mPreferenceUtil.removeLocked(WhiteListActivity.this, mWhitelists
                                .get(position).getPackageName());
                        mWhitelists.remove(position);
                        mAdapter.notifyDataSetChanged();
                        if (mWhitelists.size() == 0) {
                            mTvNoItem.setVisibility(View.VISIBLE);
                            mRecyclerViewWhiteList.setVisibility(View.GONE);
                        } else {
                            mTvNoItem.setVisibility(View.GONE);
                            mRecyclerViewWhiteList.setVisibility(View.VISIBLE);
                        }
                    }
                });
        mRecyclerViewWhiteList.setLayoutManager(new LinearLayoutManager(
                this));
        mRecyclerViewWhiteList.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {

        mWhitelists.clear();
        List<String> whitelistSaves = mPreferenceUtil.getLocked(this);
        if (whitelistSaves == null || whitelistSaves.size() == 0) {
            mTvNoItem.setVisibility(View.VISIBLE);
            mFrameLayout.setVisibility(View.GONE);
            mProgressBarLoading.setVisibility(View.GONE);
        } else {
            mTvNoItem.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
            mProgressBarLoading.setVisibility(View.GONE);
            for (String packageName : whitelistSaves) {
                try {
                    ApplicationInfo applicationInfo = mPackageManager
                            .getApplicationInfo(packageName, 0);
                    mWhitelists.add(new Whitelist(packageName, applicationInfo
                            .loadLabel(mPackageManager).toString(),
                            applicationInfo.loadIcon(mPackageManager), false));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            mAdapter.notifyDataSetChanged();
        }
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
