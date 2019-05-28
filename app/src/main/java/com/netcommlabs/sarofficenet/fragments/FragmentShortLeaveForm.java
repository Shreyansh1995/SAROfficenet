package com.netcommlabs.sarofficenet.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.SL_BASIC_DETAIL_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SL_GET_DATA_ON_CHANGED_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SL_SUBMIT_REQUEST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentShortLeaveForm extends Fragment implements View.OnClickListener, ResponseListener {
    private EditText et_date, et_contactno, et_purpose, et_rm;
    private TextView tv_atttime, tv_totime;
    private Spinner spinner;
    private Button btn_submit;
    private MySharedPreference pref;
    private String dateString;

    private int curYear;
    private int curMonth;
    private int curDay;
    private String currentTime;

    private ProjectWebRequest request;

    private int fromYearSelected;
    private int fromMonthSelected;
    private int fromDaySelected;
    //  private String dateString;

    private String formattedMonth;
    private String formattedDayOfMonth;
    private ArrayList<String> ShiftFromTime = new ArrayList<>();
    private ArrayList<String> ShiftToTime = new ArrayList<>();
    private FrameActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment__short__leave__form, container, false);
        pref = MySharedPreference.getInstance(getActivity());
        init(v);
        if (getUserVisibleHint()) {
            hitApiForRmAndHod();
        }
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            hitApiForRmAndHod();
        }
    }

    private void init(View v) {
        getCurrentDateTime();
        et_date = v.findViewById(R.id.et_date);
        et_contactno = v.findViewById(R.id.et_contactno);
        et_purpose = v.findViewById(R.id.et_purpose);
        et_rm = v.findViewById(R.id.et_rm);
        tv_atttime = v.findViewById(R.id.tv_atttime);
        tv_totime = v.findViewById(R.id.tv_totime);
        spinner = v.findViewById(R.id.spinner);
        btn_submit = v.findViewById(R.id.btn_submit);

        dateString = "" + curDay + "/" + (curMonth + 1) + "/" + curYear;
        et_date.setText(dateString);

        et_date.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItemPosition() == 0) {
                    tv_totime.setText("");
                } else {
                    tv_totime.setText(ShiftToTime.get(spinner.getSelectedItemPosition() - 1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH);
        curDay = c.get(Calendar.DAY_OF_MONTH);
        DateFormat df = new SimpleDateFormat("kk:mm");
        currentTime = df.format(Calendar.getInstance().getTime());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.et_date:
                datePicker();
                break;
            case R.id.btn_submit:
                startValidation();
                break;
        }
    }

    private void startValidation() {


        if (et_contactno.getText().toString().trim().length() <= 0) {
            Toast.makeText(activity, "Please enter Contact number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinner.getSelectedItemPosition() <= 0) {
            Toast.makeText(activity, "Please select from time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (et_purpose.getText().toString().trim().length() <= 0) {
            Toast.makeText(activity, "Please fill the  purpose", Toast.LENGTH_SHORT).show();
            return;
        }

        hitApiForShortLeave();
    }

    private void hitApiForShortLeave() {
        try {
            request = new ProjectWebRequest(getActivity(), getParamToSendShortLeaveData(), UrlConstants.SL_SUBMIT_REQUEST, this, SL_SUBMIT_REQUEST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }


    }

    private JSONObject getParamToSendShortLeaveData() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());
            object.put("LeaveDate", et_date.getText().toString());
            object.put("FromTime", spinner.getSelectedItem().toString());
            object.put("ToTime", tv_totime.getText().toString());
            object.put("ContactNo", et_contactno.getText().toString());
            object.put("Purpose", et_purpose.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    private void hitApiForRmAndHod() {

        try {
            request = new ProjectWebRequest(getActivity(), getParamForRMandHod(), UrlConstants.SL_BASIC_DETAIL, this, SL_BASIC_DETAIL_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }

    }

    private JSONObject getParamForRMandHod() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());

            LogUtils.showLog("@@@@@@@@", object.toString());
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
    public void onSuccess(JSONObject call, int Tag) {
        try {
            if (Tag == SL_BASIC_DETAIL_TAG) {
                if (!call.optString("RMName").matches("") || call.optString("RMName").matches("null")) {
                    et_rm.setText(call.getString("RMName"));
                }
                if (!call.optString("MobileNo").matches("") || call.optString("MobileNo").matches("null")) {
                    et_contactno.setText(call.getString("MobileNo"));
                }
            } else if (Tag == SL_GET_DATA_ON_CHANGED_TAG) {
                if (call.optString("Message").equalsIgnoreCase("Success")) {
                    if (ShiftFromTime.size() > 0) {
                        ShiftFromTime.clear();
                        ShiftToTime.clear();
                    }
                    String AttendanceFromTime = call.getString("AttendanceFromTime");
                    String AttendanceToTime = call.getString("AttendanceToTime");
                    tv_atttime.setText(AttendanceFromTime + " to " + AttendanceToTime);

                    JSONArray jsonArray = call.getJSONArray("SLAttendanceTimeList");
                    ShiftFromTime.add("-Select Time-");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ShiftFromTime.add(jsonObject.optString("ShiftFromTime"));
                        ShiftToTime.add(jsonObject.optString("ShiftToTime"));

                    }
                    spinner.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, ShiftFromTime));
                }else {
                    AppAlertDialog.showDialogSelfFinish(activity, "Short Leave", call.optString("Message"));
                }
            } else if (Tag == SL_SUBMIT_REQUEST_TAG) {


                if (call.optString("Status").equals("true")) {
                    //showDialog(call.getString("Message"));
                    AppAlertDialog.showDialogOnSuccess(activity, call.getString("Message"), "FragmentShortLeaveTab", "newrequest",
                            "Short Leave", "1");
                }else {
                    AppAlertDialog.showDialogSelfFinish(activity, "Short Leave", call.optString("Message"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        Toast.makeText(getActivity(), "" + error.toString(), Toast.LENGTH_SHORT).show();
    }

    void datePicker() {
        int _month, _day, _year;
        if (fromYearSelected != 0 && fromDaySelected != 0) {
            _month = fromMonthSelected;
            _year = fromYearSelected;
            _day = fromDaySelected;
        } else {
            _month = curMonth;
            _year = curYear;
            _day = curDay;
        }

        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int _year, int _month, int _dayOfMonth) {

                int month = _month + 1;
                formattedMonth = "" + month;
                formattedDayOfMonth = "" + _dayOfMonth;

                if (month < 10) {

                    formattedMonth = "0" + month;
                }
                if (_dayOfMonth < 10) {

                    formattedDayOfMonth = "0" + _dayOfMonth;
                }

                fromYearSelected = _year;
                fromDaySelected = _dayOfMonth;
                fromMonthSelected = _month;

                dateString = formattedDayOfMonth + "/" + formattedMonth + "/" + _year;
                et_date.setText(dateString);

                hitApiForSL_GetDataOnDateChange();
            }
        }, _year, _month, _day);

        expdatePickerDialog.getDatePicker();
        expdatePickerDialog.show();
    }

    private void hitApiForSL_GetDataOnDateChange() {

        try {
            request = new ProjectWebRequest(getActivity(), getParamGetDataOnDataChange(), UrlConstants.SL_GET_DATA_ON_CHANGED, this, SL_GET_DATA_ON_CHANGED_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }


    }

    private JSONObject getParamGetDataOnDataChange() {
        JSONObject objct = null;
        try {
            objct = new JSONObject();
            objct.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            objct.put("UserID", pref.getUid());
            objct.put("Date", et_date.getText().toString());

            Log.e("@@@@@@@@", objct.toString());

        } catch (Exception e) {
        }
        return objct;
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(activity);
        builder.setTitle("").setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent= new Intent(getContext(), FrameActivity.class);
                        intent.putExtra("frag_name", "FragmentShortLeaveTab");
                        intent.putExtra("frag_tag", "newrequest");
                        intent.putExtra("title", "Short Leave");
                        intent.putExtra("tab", "1");
                        startActivity(intent);
                    }
                })
                .show();
    }
}
