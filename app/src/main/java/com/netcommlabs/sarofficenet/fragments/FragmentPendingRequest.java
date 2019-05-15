package com.netcommlabs.sarofficenet.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.adapter.AttendancePendingRequestAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.AttendancePendingDetailModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.DateManagerUtility;
import com.netcommlabs.sarofficenet.utils.MyCalenderUtil;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPendingRequest extends Fragment implements View.OnClickListener, ResponseListener {
    private View viewMain;

    private ListView lv_pending_request;
    private MySharedPreference pref;
    private ProjectWebRequest request;
    private LinearLayout ll_pending_from_date;
    private LinearLayout ll_pending_to_date;
    private TextView tv_pending_from_date;
    private TextView tv_pending_to_date;
    private LinearLayout ll_submit;
    private ArrayList<AttendancePendingDetailModel> pendingList;
    private TextView error_text;
    private TextView no_data_found;
    private DateManagerUtility utility;
    private FrameActivity activity;
    private Spinner spinner;
    private EditText et_reqno;
    String[] spinnerData = {"Applied Date", "Req. Date"};
    private String FromDate = "", ToDate = "";
    private int myear, mmonth, mstartdate = 1, enddate;
    String DateType = "1";
    private String ReqNo = "";
    private TextView tv_short_leave_list_from_date;
    private TextView tv_short_leave_list_to_date;
    private boolean isViewShownPending = false;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_pending_request, container, false);
        setHasOptionsMenu(true);


        return viewMain;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isViewShownPending) {
            initView();
        }
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_dialog_layout);
        tv_short_leave_list_from_date = dialog.findViewById(R.id.tv_short_leave_list_from_date);
        tv_short_leave_list_to_date = dialog.findViewById(R.id.tv_short_leave_list_to_date);
        LinearLayout ll_short_leave_list_from_date = dialog.findViewById(R.id.ll_short_leave_list_from_date);
        LinearLayout ll_short_leave_list_to_date = dialog.findViewById(R.id.ll_short_leave_list_to_date);
        et_reqno = dialog.findViewById(R.id.et_reqno);
        Button btn_search = dialog.findViewById(R.id.btn_search);
        spinner = dialog.findViewById(R.id.spinner);

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        utility = new DateManagerUtility();
      //  utility.setCurrentDate(getContext(), tv_short_leave_list_from_date, tv_short_leave_list_to_date);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        String from_date = sdf.format(calendar.getTime());
        tv_short_leave_list_from_date.setText(from_date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 30);
        String to_date = sdf.format(calendar1.getTime());
        tv_short_leave_list_to_date.setText(to_date);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateNow(dialog);
            }
        });

        ll_short_leave_list_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utility.FromDatePicker(getContext(),tv_short_leave_list_from_date);
            }
        });
        ll_short_leave_list_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utility.ToDatePicker(getContext(),tv_short_leave_list_to_date);
            }
        });
    }

    private void initView() {
        pref = MySharedPreference.getInstance(getActivity());
        utility = new DateManagerUtility();
        lv_pending_request = (ListView) viewMain.findViewById(R.id.lv_pending_request);
        ll_pending_from_date = (LinearLayout) viewMain.findViewById(R.id.ll_pending_from_date);
        ll_pending_to_date = (LinearLayout) viewMain.findViewById(R.id.ll_pending_to_date);
        tv_pending_from_date = (TextView) viewMain.findViewById(R.id.tv_pending_from_date);
        tv_pending_to_date = (TextView) viewMain.findViewById(R.id.tv_pending_to_date);
        no_data_found = (TextView) viewMain.findViewById(R.id.no_data_found);
        error_text = (TextView) viewMain.findViewById(R.id.error_text);
        ll_submit = (LinearLayout) viewMain.findViewById(R.id.ll_submit);
        ll_pending_from_date.setOnClickListener(this);
        ll_pending_to_date.setOnClickListener(this);
        ll_submit.setOnClickListener(this);
        utility.setCurrentDate(activity, tv_pending_from_date, tv_pending_to_date);
        tv_pending_from_date.setText("");
        tv_pending_to_date.setText("");
        if (getUserVisibleHint()) {
            hitApiForPendingAttendanceDetail();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            isViewShownPending = true;
            // fetchdata() contains logic to show data when page is selected mostly asynctask to fill the data
            initView();
        } else {
            isViewShownPending = false;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ll_submit:
                //validateNow();
                break;
            case R.id.ll_pending_from_date:
                utility.FromDatePicker(getContext(),tv_pending_from_date);
                break;
            case R.id.ll_pending_to_date:
                utility.ToDatePicker(getContext(),tv_pending_to_date);
                break;

        }
    }

    private void validateNow(Dialog dialog) {
        if (!tv_pending_from_date.getText().toString().equals("")) {
            if (tv_pending_to_date.getText().toString().equals("")) {
                Toast.makeText(activity, "please choose to date", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!tv_pending_to_date.getText().toString().equals("")) {
            if (tv_pending_from_date.getText().toString().equals("")) {
                Toast.makeText(activity, "please choose  from date", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        ReqNo = et_reqno.getText().toString();
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("Applied Date")) {
            DateType = "2";
        } else {
            DateType = "1";
        }
        FromDate = MyCalenderUtil.getDateInServerSendFormat(tv_short_leave_list_from_date.getText().toString());
        ToDate = MyCalenderUtil.getDateInServerSendFormat(tv_short_leave_list_to_date.getText().toString());
        dialog.dismiss();

        hitApiForPendingAttendanceDetail();
    }

    private void hitApiForPendingAttendanceDetail() {
        if (MyCalenderUtil.validDate(activity, tv_pending_from_date.getText().toString(), tv_pending_to_date.getText().toString(), error_text)) {
            error_text.setVisibility(View.GONE);
            try {
                request = new ProjectWebRequest(getActivity(), getParamPendingAttendanceList(), UrlConstants.PENDING_ATTENDANCE_REQUEST, this, UrlConstants.PENDING_ATTENDANCE_REQUEST_TAG);
                request.execute();
            } catch (Exception e) {
                clearRef();
                e.printStackTrace();
            }
        }
    }

    private JSONObject getParamPendingAttendanceList() {

        Calendar c = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        //   myear = cal.get(Calendar.YEAR);
        int mmonthCurrent = cal.get(Calendar.MONTH) ;
        c.add(Calendar.DATE, 30);
        enddate = c.get(Calendar.DATE);
        mmonth = c.get(Calendar.MONTH) ;
        myear = c.get(Calendar.YEAR);


        if (FromDate.equalsIgnoreCase("")) {
            FromDate = "01/" + String.valueOf(mmonthCurrent - 1) + "/" + String.valueOf(myear);
        }
        if (ToDate.equalsIgnoreCase("")) {
            ToDate = String.valueOf(enddate) + "/" + String.valueOf(mmonth) + "/" + String.valueOf(myear);
        }

        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);

            object.put("UserID", pref.getUid());
            object.put("FromDate", FromDate);
            object.put("ToDate", ToDate);
           /* if (tv_pending_from_date.getText().toString().equals("") && tv_pending_to_date.getText().toString().equals("")) {
                object.put("FromDate", "");
                object.put("ToDate", "");
            } else {

                object.put("FromDate", MyCalenderUtil.getDateInServerSendFormat(tv_pending_from_date.getText().toString()));
                object.put("ToDate", MyCalenderUtil.getDateInServerSendFormat(tv_pending_to_date.getText().toString()));
            }*/
            object.put("DateType", DateType);
            object.put("ReqNo", ReqNo);
            object.put("Type", "Pending");
        } catch (Exception e) {
        }
        return object;
    }

    private void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            no_data_found.setVisibility(View.GONE);
            lv_pending_request.setVisibility(View.VISIBLE);
            if (Tag == UrlConstants.PENDING_ATTENDANCE_REQUEST_TAG) {
                pendingList = new ArrayList<AttendancePendingDetailModel>();
                AttendancePendingDetailModel model;
                try {
                    JSONArray arr = obj.getJSONArray("objARGetDataRes");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        model = new AttendancePendingDetailModel(object.optString("EmpName"),
                                object.optString("Period"),
                                object.optString("RMStatus"),
                                object.optString("RegularisationType"),
                                object.optString("ReqDate"),
                                object.optString("ReqID"),
                                object.optString("ReqNo"),
                                object.optString("Type"));
                        pendingList.add(model);
                    }
                    lv_pending_request.setAdapter(new AttendancePendingRequestAdapter(activity, pendingList));
                } catch (Exception e) {
                }
            }
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            lv_pending_request.setVisibility(View.GONE);
            no_data_found.setText(obj.optString("Message"));
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
    }
}
