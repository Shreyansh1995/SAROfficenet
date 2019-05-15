package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.MainActivity;
import com.netcommlabs.sarofficenet.adapter.FragmentLeaveManagementPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLeaveManagementSystem extends Fragment implements TabLayout.OnTabSelectedListener {
    private MainActivity mainActivity;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        View v = inflater.inflate(R.layout.fragment__leave__management__system, container, false);
        initView(v);
        return v;
    }

    private void initView(View viewMain) {
        tabLayout = (TabLayout) viewMain.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Leave"));
        tabLayout.addTab(tabLayout.newTab().setText("Short Leave"));
        tabLayout.addTab(tabLayout.newTab().setText("Attendance Regularization"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) viewMain.findViewById(R.id.pager);
        FragmentLeaveManagementPagerAdapter adapter = new FragmentLeaveManagementPagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
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
