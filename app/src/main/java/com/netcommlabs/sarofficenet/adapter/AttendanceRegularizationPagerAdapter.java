package com.netcommlabs.sarofficenet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.netcommlabs.sarofficenet.fragments.FragmentArchiveRequest;
import com.netcommlabs.sarofficenet.fragments.FragmentNewRequest;
import com.netcommlabs.sarofficenet.fragments.FragmentPendingRequest;
import com.netcommlabs.sarofficenet.fragments.FragmentRequestDetails;

public class AttendanceRegularizationPagerAdapter extends FragmentStatePagerAdapter {
    public AttendanceRegularizationPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    int tabCount;
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab1 = new FragmentNewRequest();
                return tab1;
            case 1:
                Fragment tab2 = new FragmentRequestDetails();
                return tab2;
            case 2:
                Fragment tab3 = new FragmentPendingRequest();
                return tab3;
            case 3:
                Fragment tab4 = new FragmentArchiveRequest();
                return tab4;

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
