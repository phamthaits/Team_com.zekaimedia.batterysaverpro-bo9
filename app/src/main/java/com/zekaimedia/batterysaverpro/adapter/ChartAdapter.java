package com.zekaimedia.batterysaverpro.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zekaimedia.batterysaverpro.fragment.fragment1;
import com.zekaimedia.batterysaverpro.fragment.fragment2;
import com.zekaimedia.batterysaverpro.fragment.fragment3;

public class ChartAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 3;
    public ChartAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return fragment1.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return fragment2.newInstance(0, "Page # 1");
            case 2: // Fragment # 1 - This will show SecondFragment
                return fragment3.newInstance(0, "Page # 1");
            default:
                return null;
        }
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}