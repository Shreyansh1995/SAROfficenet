package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.MainActivity;
import com.netcommlabs.sarofficenet.adapter.AttendanceRegularizationPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAttendanceRegularizationTab extends Fragment implements TabLayout.OnTabSelectedListener {
    private MainActivity mainActivity;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String CurrentTab;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_attendance_regularization_tab, container, false);
        initView(v);
        return v;
    }

    private void initView(View viewMain) {

        CurrentTab = getArguments().getString("tab");

        tabLayout = (TabLayout) viewMain.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("New Request"));
        tabLayout.addTab(tabLayout.newTab().setText("Request Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending Request"));
        tabLayout.addTab(tabLayout.newTab().setText("Archive Request"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) viewMain.findViewById(R.id.pager);
        AttendanceRegularizationPagerAdapter adapter = new AttendanceRegularizationPagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);



        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                tab.select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectedListener(this);

        if (!TextUtils.isEmpty(CurrentTab)) {
            viewPager.setCurrentItem(Integer.parseInt(CurrentTab));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        }else {
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
