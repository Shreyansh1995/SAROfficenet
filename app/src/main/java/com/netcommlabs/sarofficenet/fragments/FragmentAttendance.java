package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.MainActivity;
import com.netcommlabs.sarofficenet.adapter.AbbreviationAdapter;
import com.netcommlabs.sarofficenet.adapter.AttandanceAdapter;
import com.netcommlabs.sarofficenet.adapter.CalenderAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.AbbreviationModel;
import com.netcommlabs.sarofficenet.model.AttendanceModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAttendance extends Fragment implements ResponseListener, View.OnClickListener {
    private View viewMain;
    private MainActivity mainActivity;
    private ProjectWebRequest request;
    private MySharedPreference pref;
    private ImageView iv_prev_date;
    private ImageView iv_next_date;
  //  private ListView calender_list_view;
    private RecyclerView calender_list_view;
    private TextView tv_month_filter;
    private int inquiryMonth;
    private int inquiryYear;
    private String monthArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private ArrayList<AttendanceModel> attendanceList;
    private LinearLayout ll_row;
    private ImageView iv_calender_list_view;
    private ImageView iv_calender_view;
    private RecyclerView calender_recyclerview;
    private TextView no_data_found;
    private boolean isVisible = false;

    private RecyclerView recyclerViewAbbreviation;
    private ArrayList<AbbreviationModel> abbreviationModelArrayList;
    private AbbreviationAdapter abbreviationAdapter;

    private View calendar_header;
    private RelativeLayout rlabbriviation;
    private ImageView iv_arrow;
    private LinearLayout ll_abbriviation;
    private String UserID;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_fragment_attendance, container, false);
        UserID = getArguments().getString("UserID");
        initView();
        return viewMain;
    }

    void initView() {
        pref = MySharedPreference.getInstance(getActivity());
        tv_month_filter = (TextView) viewMain.findViewById(R.id.tv_month_filter);
        iv_prev_date = (ImageView) viewMain.findViewById(R.id.iv_prev_date);
        iv_next_date = (ImageView) viewMain.findViewById(R.id.iv_next_date);

        calender_recyclerview = (RecyclerView) viewMain.findViewById(R.id.calender_recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7, GridLayoutManager.HORIZONTAL, false);
        calender_recyclerview.setLayoutManager(layoutManager);
        calender_list_view =  viewMain.findViewById(R.id.calender_list_view);
        iv_calender_list_view = (ImageView) viewMain.findViewById(R.id.iv_calender_list_view);

        calender_list_view.setLayoutManager(new LinearLayoutManager(getContext()));
        calender_list_view.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        calender_list_view.setHasFixedSize(false);
        calender_list_view.setNestedScrollingEnabled(false);


      /*  calender_list_view.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        justifyListViewHeightBasedOnChildren(calender_list_view);*/



        no_data_found = (TextView) viewMain.findViewById(R.id.no_data_found);
        iv_calender_view = (ImageView) viewMain.findViewById(R.id.iv_calender_view);
        ll_row = (LinearLayout) viewMain.findViewById(R.id.ll_row);
        calendar_header = viewMain.findViewById(R.id.calendar_header);

        rlabbriviation = viewMain.findViewById(R.id.rl_abbriviation);
        ll_abbriviation = viewMain.findViewById(R.id.ll_abbriviation);
        iv_arrow = viewMain.findViewById(R.id.iv_arrow);
        rlabbriviation.setOnClickListener(this);
        ll_abbriviation.setOnClickListener(this);
        iv_arrow.setOnClickListener(this);


        recyclerViewAbbreviation = (RecyclerView) viewMain.findViewById(R.id.abbreviation_recyclerview);
        recyclerViewAbbreviation.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAbbreviation.setNestedScrollingEnabled(false);
        abbreviationModelArrayList = new ArrayList<>();
        abbreviationAdapter = new AbbreviationAdapter(abbreviationModelArrayList, getActivity(), getContext());


        iv_prev_date.setOnClickListener(this);
        iv_next_date.setOnClickListener(this);
        iv_calender_list_view.setOnClickListener(this);
        iv_calender_view.setOnClickListener(this);
        getCurrentDate();
    }

    void getCurrentDate() {
        Calendar c = Calendar.getInstance();
        setDate(c.get(Calendar.MONTH), c.get(Calendar.YEAR));
    }

    void setDate(int month, int year) {
        inquiryMonth = month;
        inquiryYear = year;
        if (year == Calendar.getInstance().get(Calendar.YEAR) && month == Calendar.getInstance().get(Calendar.MONTH)) {
            iv_next_date.setVisibility(View.GONE);
        } else {
            iv_next_date.setVisibility(View.VISIBLE);
        }
        tv_month_filter.setText(monthArray[month] + "-" + year);
        hitApi();
    }

    void hitApi() {
        try {
            request = new ProjectWebRequest(getActivity(), getParam(), UrlConstants.GET_ATTENDANCE, this, UrlConstants.GET_ATTENDANCE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    JSONObject getParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserId", UserID);
            object.put("Date", (inquiryMonth + 1) + "/1" + "/" + inquiryYear);
        } catch (Exception e) {
        }
        return object;
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            if (Tag == UrlConstants.GET_ATTENDANCE_TAG) {
                abbreviationModelArrayList.clear();
                no_data_found.setVisibility(View.GONE);
                if (isVisible) {
                    calender_list_view.setVisibility(View.VISIBLE);
                    ll_row.setVisibility(View.VISIBLE);
                } else {
                    calender_recyclerview.setVisibility(View.VISIBLE);
                    calendar_header.setVisibility(View.VISIBLE);
//                    ll_calender_view_layout.setVisibility(View.VISIBLE);
                }

                try {
                    JSONArray array = obj.getJSONArray("objMyAttendanceRes");
                    attendanceList = new ArrayList<AttendanceModel>();
                    AttendanceModel model;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        model = new AttendanceModel();
                        model.setStatus(object.optString("Status"));
                        model.setDateOffice2(object.optString("DateOffice2"));
                        model.setColor(object.optString("Color"));
                        model.setPayCode(object.optString("PayCode"));
//                        model.setShiftCode(object.optString("ShiftCode"));
                        model.setDateOffice(object.optString("DateOffice"));
//                        model.setShiftName(object.optString("ShiftName"));
//                        model.setStartBreak(object.optString("StartBreak"));
                        model.setId(object.optString("Id"));
                        model.setInTime(object.optString("InTime"));
                        model.setOutTime(object.optString("OutTime"));

                        attendanceList.add(model);
                    }
//
                    setCalender(attendanceList);


//                    iniCalender();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            try {
                JSONArray jsonArray = obj.getJSONArray("MyAttendanceAbbreviationRes");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                    AbbreviationModel abbreviationModel = new AbbreviationModel();
                    abbreviationModel.setColor(jsonObject1.getString("Color"));
                    abbreviationModel.setFull(jsonObject1.getString("ShortDesc"));
                    abbreviationModel.setShort(jsonObject1.getString("CalenderStatus"));
                    abbreviationModel.setCount(jsonObject1.getString("AbbreviationDayCount"));
                    abbreviationModelArrayList.add(abbreviationModel);
                }
                recyclerViewAbbreviation.setAdapter(abbreviationAdapter);
                abbreviationAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (attendanceList != null)
                attendanceList.clear();
            calender_recyclerview.setVisibility(View.GONE);
            calendar_header.setVisibility(View.GONE);
            /*   ll_calender_view_layout.setVisibility(View.GONE);*/
            calender_list_view.setVisibility(View.GONE);
            ll_row.setVisibility(View.GONE);
            no_data_found.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
    }

    private void setCalender(List<AttendanceModel> calenderModelList) {

        String date_str = calenderModelList.get(calenderModelList.size() - 1).getDateOffice();
        String splitDate[] = date_str.split("/");
        String split_year_month[] = splitDate[2].split(" ");
        int month = getStartDay(Integer.parseInt(split_year_month[0]), Integer.parseInt(splitDate[0]) - 1, 1);
        int num_of_days = numberOfDaysInMonth(Integer.parseInt(splitDate[0]) - 1, Integer.parseInt(split_year_month[0]));
        Collections.reverse(calenderModelList);

        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        int total_view_item = num_of_days + (month - 1);
        recycledViewPool.setMaxRecycledViews(0, total_view_item);

        CalenderAdapter calenderAdapter = new CalenderAdapter(getActivity(), month, num_of_days, calenderModelList);
        calender_recyclerview.setRecycledViewPool(recycledViewPool);
        calender_recyclerview.setAdapter(calenderAdapter);

       /* if (list_status == 0) {
            headerView();
        }*/
        calender_list_view.setAdapter(new AttandanceAdapter(getContext(), attendanceList));

    }

    private int numberOfDaysInMonth(int month, int year) {
        Calendar monthStart = new GregorianCalendar(year, month, 1);
        return monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private int getStartDay(int year, int month, int date) {
        Calendar calendar = new GregorianCalendar(year, month, date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_prev_date:
                decreaseMonth();
                break;
            case R.id.iv_next_date:
                increaseMonth();
                break;
            case R.id.iv_calender_list_view:
                if (attendanceList != null) {
                    if (attendanceList.size() > 0) {
                        isVisible = true;
                        iv_calender_list_view.setVisibility(View.GONE);
                        iv_calender_view.setVisibility(View.VISIBLE);
//                        ll_calender_view_layout.setVisibility(View.GONE);
                        calender_recyclerview.setVisibility(View.GONE);
                        calendar_header.setVisibility(View.GONE);
                        ll_row.setVisibility(View.VISIBLE);
                        calender_list_view.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.iv_calender_view:
                if (attendanceList != null) {
                    if (attendanceList.size() > 0) {
                        isVisible = false;
                        iv_calender_list_view.setVisibility(View.VISIBLE);
                        iv_calender_view.setVisibility(View.GONE);
//                        ll_calender_view_layout.setVisibility(View.VISIBLE);
                     /*   if(header==null)
                            list_status=0;*/
                        calender_recyclerview.setVisibility(View.VISIBLE);
                        calendar_header.setVisibility(View.VISIBLE);
                        ll_row.setVisibility(View.GONE);
                        calender_list_view.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.rl_abbriviation:
                if (ll_abbriviation.getVisibility() != View.VISIBLE){
                    ll_abbriviation.setVisibility(View.VISIBLE);
                    iv_arrow.setImageResource(R.drawable.downarrow);
                }else {
                    ll_abbriviation.setVisibility(View.GONE);
                    iv_arrow.setImageResource(R.drawable.arrowright);
                }

                break;
            case R.id.iv_arrow:
                break;
        }
    }

    void increaseMonth() {
        if (inquiryMonth == 11) {
            if (inquiryYear == Calendar.getInstance().get(Calendar.YEAR)) {
            } else {
                inquiryYear++;
                inquiryMonth = 0;
            }
        } else {
            inquiryMonth++;
        }
        setDate(inquiryMonth, inquiryYear);
    }

    void decreaseMonth() {
        if (inquiryMonth == 0) {
            inquiryMonth = monthArray.length - 1;
            inquiryYear--;
        } else {
            inquiryMonth--;
        }
        setDate(inquiryMonth, inquiryYear);
    }
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}
