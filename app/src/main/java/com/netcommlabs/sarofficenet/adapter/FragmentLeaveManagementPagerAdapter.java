package com.netcommlabs.sarofficenet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.netcommlabs.sarofficenet.fragments.FragmentAttendanceRegularization;
import com.netcommlabs.sarofficenet.fragments.FragmentAttendanceRegularizationTab;
import com.netcommlabs.sarofficenet.fragments.FragmentLeave;
import com.netcommlabs.sarofficenet.fragments.FragmentShortLeave;

public class FragmentLeaveManagementPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public FragmentLeaveManagementPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab1 = new FragmentLeave();
                return tab1;
            case 1:
                Fragment tab2 = new FragmentShortLeave();
                return tab2;
            case 2:
                Fragment tab3 = new FragmentAttendanceRegularization();
                return tab3;

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
