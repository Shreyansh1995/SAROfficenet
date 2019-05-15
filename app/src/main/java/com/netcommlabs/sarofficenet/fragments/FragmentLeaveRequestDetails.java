package com.netcommlabs.sarofficenet.fragments;


import android.app.Dialog;
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
import com.netcommlabs.sarofficenet.adapter.AttendanceRegularizationListAdapter;
import com.netcommlabs.sarofficenet.adapter.LeaveDetailsAdpter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.AttendanceRegularizationListModel;
import com.netcommlabs.sarofficenet.model.LeaveListModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.DateManagerUtility;
import com.netcommlabs.sarofficenet.utils.MyCalenderUtil;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLeaveRequestDetails extends Fragment implements ResponseListener, View.OnClickListener {
    private View viewMain;
    private ListView lv_details;
    private ProjectWebRequest request;
    private MySharedPreference pref;
    private TextView tv_short_leave_list_from_date;
    private TextView tv_short_leave_list_to_date;
    private TextView error_text;
    private LinearLayout ll_short_leave_list_search;
    private DateManagerUtility utility;
    private LinearLayout ll_short_leave_list_from_date;
    private LinearLayout ll_short_leave_list_to_date;
    // private LinearLayout header_row;
    private ArrayList<LeaveListModel> leaveListModelArrayList;
    private TextView error;
    private Spinner spinner;
    private EditText et_reqno;
    String[] spinnerData = {"Leave Date","Applied Date"};
    private String FromDate = "", ToDate = "";
    private int myear, mmonth, mstartdate = 1, enddate;
    String DateType = "1";
    private String ReqNo = "";

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
        //utility.setCurrentDate(getContext(), tv_short_leave_list_from_date, tv_short_leave_list_to_date);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_leave_request_details, container, false);
        pref = MySharedPreference.getInstance(getContext());
        setHasOptionsMenu(true);
     //   initView();
        return viewMain;
    }
    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        // header_row = (LinearLayout) viewMain.findViewById(R.id.header_row);
        lv_details = (ListView) viewMain.findViewById(R.id.lv_details);
        tv_short_leave_list_from_date = (TextView) viewMain.findViewById(R.id.tv_short_leave_list_from_date);
        tv_short_leave_list_to_date = (TextView) viewMain.findViewById(R.id.tv_short_leave_list_to_date);
        error = (TextView) viewMain.findViewById(R.id.error);


        error_text = (TextView) viewMain.findViewById(R.id.error_text);
        ll_short_leave_list_search = (LinearLayout) viewMain.findViewById(R.id.ll_short_leave_list_search);
        ll_short_leave_list_from_date = (LinearLayout) viewMain.findViewById(R.id.ll_short_leave_list_from_date);
        ll_short_leave_list_to_date = (LinearLayout) viewMain.findViewById(R.id.ll_short_leave_list_to_date);
        utility = new DateManagerUtility();
        utility.setCurrentDate(getContext(), tv_short_leave_list_from_date, tv_short_leave_list_to_date);
        tv_short_leave_list_from_date.setText("");
        tv_short_leave_list_to_date.setText("");
        ll_short_leave_list_search.setOnClickListener(this);
        ll_short_leave_list_from_date.setOnClickListener(this);
        ll_short_leave_list_to_date.setOnClickListener(this);

        if (getUserVisibleHint()){
            hitApiForLeaveDetails();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            hitApiForLeaveDetails();
        }
    }

    private void validateNow(Dialog dialog) {
        if (tv_short_leave_list_from_date.getText().toString().equals("")) {
            Toast.makeText(getContext(), "please choose from date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_short_leave_list_to_date.getText().toString().equals("")) {
            Toast.makeText(getContext(), "please choose  to date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (MyCalenderUtil.validDate(getContext(), tv_short_leave_list_from_date.getText().toString(), tv_short_leave_list_to_date.getText().toString(), error_text)) {
            error_text.setVisibility(View.GONE);
            ReqNo = et_reqno.getText().toString();
            if (spinner.getSelectedItem().toString().equalsIgnoreCase("Applied Date")) {
                DateType = "2";
            } else {
                DateType = "1";
            }
            FromDate = MyCalenderUtil.getDateInServerSendFormat(tv_short_leave_list_from_date.getText().toString());
            ToDate = MyCalenderUtil.getDateInServerSendFormat(tv_short_leave_list_to_date.getText().toString());
            dialog.dismiss();
            hitApiForLeaveDetails();
        }
    }

    private void hitApiForLeaveDetails() {
        try {
            request = new ProjectWebRequest(getContext(), getParamForLeaveDetails(), UrlConstants.VIEW_LEAVE_DEATILS, this, UrlConstants.VIEW_LEAVE_DEATILS_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParamForLeaveDetails() {
        Calendar c = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        //   myear = cal.get(Calendar.YEAR);
        int mmonthCurrent = cal.get(Calendar.MONTH) ;
        c.add(Calendar.DATE, 30);
        enddate = c.get(Calendar.DATE);
        mmonth = c.get(Calendar.MONTH) ;
        myear = c.get(Calendar.YEAR);


        if (FromDate.equalsIgnoreCase("")) {
            FromDate = "01/" + String.valueOf(mmonthCurrent) + "/" + String.valueOf(myear);
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
           /* object.put("DateType", DateType);
            object.put("ReqNo", ReqNo);
            object.put("Type", "UserView");*/


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
            error.setVisibility(View.GONE);
            lv_details.setVisibility(View.VISIBLE);


            if (Tag == UrlConstants.VIEW_LEAVE_DEATILS_TAG) {
                leaveListModelArrayList = new ArrayList<LeaveListModel>();
                LeaveListModel model;
                try {
                    JSONArray arr = obj.getJSONArray("objleaveGetDetailsRes");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        model = new LeaveListModel(object.optString("EmpCode"),
                                object.optString("EmpID"),
                                object.optString("EmpName"),
                                object.optString("HODStatus"),
                                object.optString("LeaveFinalStatus"),
                                object.optString("Leave_Name"),
                                object.optString("Mode"),
                                object.optString("No_of_Days"),
                                object.optString("Period"),
                                object.optString("ReqDate"),
                                object.optString("ReqID"),
                                object.optString("ReqNo"),
                                object.optString("Rm_Status"),
                                object.optString("User_Status"),
                                "Details");
                        leaveListModelArrayList.add(model);
                    }
                    if (leaveListModelArrayList != null) {
                        //  header_row.setVisibility(View.VISIBLE);

                        lv_details.setAdapter(new LeaveDetailsAdpter(getContext(), leaveListModelArrayList));
                    }

                } catch (Exception e) {
                }
            }
        } else {
            //  header_row.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
            lv_details.setVisibility(View.GONE);
            error.setText(obj.optString("Message"));
            AppAlertDialog.showDialogSelfFinish(getContext(), "", obj.optString("Message"));

        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
    }

    @Override
    public void onClick(View view) {

    }
}
