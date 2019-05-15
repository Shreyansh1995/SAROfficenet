package com.netcommlabs.sarofficenet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.netcommlabs.sarofficenet.fragments.FragmentLeaveArchive;
import com.netcommlabs.sarofficenet.fragments.FragmentLeaveCanceled;
import com.netcommlabs.sarofficenet.fragments.FragmentLeaveForm;
import com.netcommlabs.sarofficenet.fragments.FragmentLeavePending;
import com.netcommlabs.sarofficenet.fragments.FragmentLeaveRequestDetails;
import com.netcommlabs.sarofficenet.fragments.FragmentShortLeaveAchive;


public class LeavePagerAdapter  extends FragmentStatePagerAdapter {
    int tabCount;

    public LeavePagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab1 = new FragmentLeaveForm();
                return tab1;
            case 1:
                Fragment tab2 = new FragmentLeaveRequestDetails();
                return tab2;
            case 2:
                Fragment tab3 = new FragmentLeavePending();
                return tab3;
            case 3:
                Fragment tab4 = new FragmentLeaveArchive();
                return tab4;
            case 4:
                Fragment tab5 = new FragmentLeaveCanceled();
                return tab5;

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
