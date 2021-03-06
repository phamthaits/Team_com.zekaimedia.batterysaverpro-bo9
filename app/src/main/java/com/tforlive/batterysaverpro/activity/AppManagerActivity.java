package com.tforlive.batterysaverpro.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.ProgressBar;

import com.ads.control.AdControlHelp;
import com.tforlive.batterysaverpro.R;
import com.tforlive.batterysaverpro.Utilsb.Utils;
import com.tforlive.batterysaverpro.adapter.AppManagerAdapter;
import com.tforlive.batterysaverpro.adapter.ManagerConnect;
import com.tforlive.batterysaverpro.model.AppManager;
import com.tforlive.batterysaverpro.model.GroupItemAppManager;
import com.tforlive.batterysaverpro.view.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends AppCompatActivity {
    private static final int UNINSTALL_REQUEST_CODE = 1;
    protected ProgressBar mProgressBarLoading;

    private Handler mHandlerLocal = new Handler(Looper.getMainLooper());
    private List<AppManager> mAppManagers = new ArrayList<>();
    private List<GroupItemAppManager> mGroupItems = new ArrayList<>();

    private AppManagerAdapter mAdapter;
    private int mGroupPosition;
    private int mChildPosition;
    private AnimatedExpandableListView mRecyclerView;
    Runnable runnableLocal;
    private AdControlHelp adControlHelp;

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lr_back:
                finish();
                return;
            default:
                return;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        /* ------------------- StatusBar Navigation text dark bg white ----------------- */

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        /* ------------------------------------------------------------------ */

        mProgressBarLoading = findViewById(R.id.progressBarLoading);
        mRecyclerView = findViewById(R.id.recyclerView);
        initAdapter();
        loadData();
        adControlHelp = AdControlHelp.getInstance(this);
        adControlHelp.loadBanner(this, findViewById(R.id.banner));
    }

    private void initAdapter() {
        mAdapter = new AppManagerAdapter(this, mGroupItems, new AppManagerAdapter.OnClickItemListener() {
            @Override
            public void onUninstallApp(int groupPosition, int childPosition) {
                mGroupPosition = groupPosition;
                mChildPosition = childPosition;
                ApplicationInfo app = mGroupItems.get(groupPosition).getItems().get(childPosition);
                Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                intent.setData(Uri.parse("package:" + app.packageName));
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
            }

            @Override
            public void onClickItem(int groupPosition, int childPosition) {
                if (mGroupItems.get(groupPosition).getItems().get(childPosition).packageName != null) {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + mGroupItems.get(groupPosition).getItems().get(childPosition).packageName));
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        ManagerConnect managerConnect = new ManagerConnect();
        managerConnect.getListManager(this, new ManagerConnect.OnManagerConnectListener() {
            @Override
            public void OnResultManager(final List<ApplicationInfo> result) {
                runnableLocal = new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarLoading.setVisibility(View.GONE);
                        if (result.size() != 0) {
                            //set app user
                            GroupItemAppManager groupItemAppManagerUser = new GroupItemAppManager();
                            groupItemAppManagerUser.setTitle(getString(R.string.user_app));
                            groupItemAppManagerUser.setType(GroupItemAppManager.TYPE_USER_APPS);
                            int countUser = 0;
                            List<ApplicationInfo> applicationInfosUser = new ArrayList<>();
                            for (ApplicationInfo applicationInfo : result) {
                                if (!applicationInfo.packageName.equals(getPackageName())) {
                                    if (Utils.isUserApp(applicationInfo)) {
                                        countUser++;
                                        applicationInfosUser.add(applicationInfo);
                                    }
                                }
                            }
                            groupItemAppManagerUser.setItems(applicationInfosUser);
                            groupItemAppManagerUser.setTotal(countUser);
                            mGroupItems.add(groupItemAppManagerUser);
                            //set app system
                            GroupItemAppManager groupItemAppManagerSystem = new GroupItemAppManager();
                            groupItemAppManagerSystem.setTitle(getString(R.string.system_app));
                            groupItemAppManagerSystem.setType(GroupItemAppManager.TYPE_SYSTEM_APPS);
                            int countSystem = 0;
                            List<ApplicationInfo> applicationInfosSystem = new ArrayList<>();
                            for (ApplicationInfo applicationInfo : result) {
                                if (!Utils.isUserApp(applicationInfo)) {
                                    countSystem++;
                                    applicationInfosSystem.add(applicationInfo);
                                }
                            }
                            groupItemAppManagerSystem.setItems(applicationInfosSystem);
                            groupItemAppManagerSystem.setTotal(countSystem);
                            mGroupItems.add(groupItemAppManagerSystem);
                            mAdapter.notifyDataSetChanged();
                            if (mRecyclerView.isGroupExpanded(0)) {
                                mRecyclerView.collapseGroupWithAnimation(0);
                            } else {
                                mRecyclerView.expandGroupWithAnimation(0);
                            }
                        }
                    }
                };
                mHandlerLocal.postDelayed(runnableLocal, 100);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandlerLocal != null)
            mHandlerLocal.removeCallbacks(runnableLocal);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mGroupItems.get(mGroupPosition).getItems().remove(mChildPosition);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
