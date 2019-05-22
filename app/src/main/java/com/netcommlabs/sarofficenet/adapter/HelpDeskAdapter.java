package com.netcommlabs.sarofficenet.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.netcommlabs.sarofficenet.fragments.FragmentAttendanceRegularization;
import com.netcommlabs.sarofficenet.fragments.FragmentHelpDeskForm;
import com.netcommlabs.sarofficenet.fragments.FragmentHelpDeskPendingTickets;
import com.netcommlabs.sarofficenet.fragments.FragmentHelpDeskTicketList;
import com.netcommlabs.sarofficenet.fragments.FragmentLeave;
import com.netcommlabs.sarofficenet.fragments.FragmentShortLeave;

public class HelpDeskAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    public HelpDeskAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount =tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab1 = new FragmentHelpDeskForm();
                return tab1;
            case 1:
                Fragment tab2 = new FragmentHelpDeskTicketList();
                return tab2;
            case 2:
                Fragment tab3 = new FragmentHelpDeskPendingTickets();
                return tab3;

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
