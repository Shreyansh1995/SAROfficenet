package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.MainActivity;
import com.netcommlabs.sarofficenet.adapter.FragmentLeaveManagementPagerAdapter;
import com.netcommlabs.sarofficenet.adapter.HelpDeskAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONObject;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.IS_ADMIN;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.IS_ADMIN_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_DETAIL;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_DETAIL_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHelpDesk extends Fragment implements TabLayout.OnTabSelectedListener, ResponseListener {
    private MainActivity mainActivity;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    private String isAdmin="0";
    View viewMain;

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
        viewMain = inflater.inflate(R.layout.fragment__help_desk, container, false);
        mySharedPreference = MySharedPreference.getInstance(getContext());
        hitApi();

        return viewMain;
    }

    private void hitApi() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", mySharedPreference.getUid());
            request = new ProjectWebRequest(getContext(), object, IS_ADMIN, this, IS_ADMIN_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void initView() {
        tabLayout = (TabLayout) viewMain.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("New Ticket"));
        tabLayout.addTab(tabLayout.newTab().setText("Raised Ticket"));
        if (isAdmin.equalsIgnoreCase("1")) {
            tabLayout.addTab(tabLayout.newTab().setText("Pending Ticket"));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) viewMain.findViewById(R.id.pager);
        HelpDeskAdapter adapter = new HelpDeskAdapter(getFragmentManager(), tabLayout.getTabCount());
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

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();

        if (call.optString("Status").equals("true")) {
            isAdmin = "1";
        } else {
            isAdmin = "0";
        }
        initView();
        //  Toast.makeText(mainActivity, "" + call.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        initView();
        clearRef();
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

}
