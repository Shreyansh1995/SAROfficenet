package com.netcommlabs.sarofficenet.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.adapter.HolidayAdapter;
import com.netcommlabs.sarofficenet.adapter.NewJoineeAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.HolidayModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.DateManagerUtility;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.HOLIDAY_LIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.LOCATION_LIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.NEW_JOINEE_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHoliday extends Fragment implements ResponseListener {
    private FrameActivity activity;
    private ProjectWebRequest request;
    private RecyclerView recyclerView;
    private ArrayList<HolidayModel> holidayModelArrayList;
    private HolidayAdapter holidayAdapter;
    private MySharedPreference mySharedPreference;
    private int CurrentYear;
    private Spinner spyear, splocation;
    private ArrayList<String> yearList, locationList, locationId;
    private String LocationID;
    private int pos = -1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filtermenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                showDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_layout_loaction);
        Button btn_search = dialog.findViewById(R.id.btn_search);
        spyear = dialog.findViewById(R.id.spinneryear);
        splocation = dialog.findViewById(R.id.spinnerlocation);

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, yearList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spyear.setAdapter(aa);

        spyear.setSelection(1);

        splocation.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, locationList));


        for (int i = 0; i < locationId.size(); i++) {
            if (locationId.get(i).equalsIgnoreCase(LocationID)) {
                pos = i;
                break;
            }
        }
        splocation.setSelection(pos);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationID = locationId.get(splocation.getSelectedItemPosition());
                CurrentYear = Integer.parseInt(spyear.getSelectedItem().toString());
                if (NetworkUtils.isConnected(activity)) {
                    dialog.dismiss();
                    hitApi();
                } else {
                    Toast.makeText(activity, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_holiday, container, false);
        setHasOptionsMenu(true);
        mySharedPreference = MySharedPreference.getInstance(activity);
        Calendar calendar = Calendar.getInstance();
        CurrentYear = calendar.get(Calendar.YEAR);
        yearList = new ArrayList<String>();
        locationList = new ArrayList<String>();
        locationId = new ArrayList<String>();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        Date preDate = c.getTime();
        String previousDate = dateFormat.format(preDate);


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Date nxtDate = cal.getTime();
        String nextDate = dateFormat.format(nxtDate);

        yearList.add(previousDate);
        yearList.add(String.valueOf(CurrentYear));
        yearList.add(nextDate);

        LocationID = mySharedPreference.getLocationCode();

        findViewByid(v);
        return v;
    }

    private void findViewByid(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        holidayModelArrayList = new ArrayList<>();
        holidayAdapter = new HolidayAdapter(holidayModelArrayList, getContext(), getActivity());

        if (NetworkUtils.isConnected(activity)) {
            hitApi();
            hitgetLocationApi();
        } else {
            Toast.makeText(activity, "Please check your intenet connection!", Toast.LENGTH_SHORT).show();
        }


    }

    private void hitgetLocationApi() {
        try {
            request = new ProjectWebRequest(activity, getLocationParam(), UrlConstants.LOCATION_LIST, this, LOCATION_LIST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getLocationParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
        } catch (Exception e) {
        }
        return object;
    }
    private void hitApi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), UrlConstants.HOLIDAY_LIST, this, HOLIDAY_LIST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("LocationID", LocationID);
            object.put("HolidayYear", CurrentYear);
            LogUtils.showLog("request :", "Tag :" + NEW_JOINEE_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        try {
            if (Tag == HOLIDAY_LIST_TAG) {
                if (holidayModelArrayList.size() > 0) {
                    holidayModelArrayList.clear();
                }
                if (call.getString("Message").equalsIgnoreCase("Success")) {
                    JSONArray jsonArray = call.getJSONArray("HolidayCalList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HolidayModel holidayModel = new HolidayModel();
                        holidayModel.setDate(jsonObject.getString("Date"));
                        holidayModel.setDay(jsonObject.getString("Day"));
                        holidayModel.setDayStatus(jsonObject.getString("DayStatus"));
                        holidayModel.setHolidayType(jsonObject.getString("HolidayType"));
                        holidayModel.setIsCurrentMonthHoliday(jsonObject.getString("IsCurrentMonthHoliday"));
                        holidayModel.setOccation(jsonObject.getString("Occation"));
                        holidayModelArrayList.add(holidayModel);
                    }
                    recyclerView.setAdapter(holidayAdapter);
                    holidayAdapter.notifyDataSetChanged();
                } else {
                    recyclerView.setAdapter(holidayAdapter);
                    AppAlertDialog.showDialogSelfFinish(activity, "", call.optString("Message"));
                }
            } else if (Tag == LOCATION_LIST_TAG) {
                LogUtils.showLog("Locationsss", "" + call.toString());
                if (locationList.size() > 0) {
                    locationList.clear();
                }
                JSONArray jsonArrayLoc = call.getJSONArray("LocationList");
                int si = jsonArrayLoc.length();
                for (int j = 0; j < jsonArrayLoc.length(); j++) {
                    JSONObject jsonObject = jsonArrayLoc.getJSONObject(j);
                    String locationlist = jsonObject.getString("LocationName");
                    locationList.add(locationlist);
                    locationId.add(jsonObject.getString("LocationID"));
                }
//                int si = locationList.size();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {

    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }
}
