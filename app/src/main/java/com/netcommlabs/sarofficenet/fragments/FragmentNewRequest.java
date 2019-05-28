package com.netcommlabs.sarofficenet.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.AttendanceRegulerizeModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.CalenderUtil;
import com.netcommlabs.sarofficenet.utils.MyCalenderUtil;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.OD_GET_BASIC_DETAILS_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUBMIT_ATTENDANCE_REGULARIZATION_TYPE_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TOKEN_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewRequest extends Fragment implements View.OnClickListener, ResponseListener {
    // String[] SPINNERLIST = {"Android Material Design", "Material Design Spinner", "Spinner Using Material Library", "Material Spinner Example"};

    private TextView tvfromdate, tvtodate, tvfromtime, tvtotime;
    private EditText etplace, etremark, etcontactno,et_rm;
    private Button btnSubmit;
    private FrameActivity activity;
    private Spinner spinner;

    private String fromDateString;
    private String toDateString;
    private String dateString;


    private int fromDay;
    private int fromMonth;
    private int fromYear;


    private int day;
    private int month;
    private int year;

    private int curYear;
    private int curMonth;
    private int curDay;

    private MySharedPreference pref;
    private String currentTime;
    private ProjectWebRequest request;
    private ArrayList<AttendanceRegulerizeModel> BasicTypeList;
    private ArrayList<String> Type, TypeID;
    private LinearLayout lldate, lltodate, lltotime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_new_request, container, false);
        // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        // spinner.setAdapter(arrayAdapter);

        pref = MySharedPreference.getInstance(getActivity());
        findviewByid(v);

        if (getUserVisibleHint()) {
            GetBasicDetails();
        }
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            GetBasicDetails();
        }
    }

    private void GetBasicDetails() {
        try {
            request = new ProjectWebRequest(activity, getBasicDeatils(), UrlConstants.OD_GET_BASIC_DETAILS, this, OD_GET_BASIC_DETAILS_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    private JSONObject getBasicDeatils() {
        JSONObject object = new JSONObject();

        try {
            object.put("TokenNo", "abcHkl7900@8Uyhkj");
            object.put("UserID", pref.getUid());

        } catch (Exception e) {

        }
        return object;
    }

    private void findviewByid(View v) {
        getCurrentDateTime();
        spinner = v.findViewById(R.id.spinner);
        tvfromdate = v.findViewById(R.id.tv_fromdate);
        tvtodate = v.findViewById(R.id.tv_todate);
        tvfromtime = v.findViewById(R.id.tv_fromtime);
        tvtotime = v.findViewById(R.id.tv_totime);
        etplace = v.findViewById(R.id.et_place);
        etremark = v.findViewById(R.id.et_remark);
        etcontactno = v.findViewById(R.id.et_contactno);
        et_rm = v.findViewById(R.id.et_rm);
        btnSubmit = v.findViewById(R.id.btn_submit);

        lldate = v.findViewById(R.id.lldate);
        lltodate = v.findViewById(R.id.lltodate);
        lltotime = v.findViewById(R.id.lltotime);


        //  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        // spinner.setAdapter(arrayAdapter);

        tvfromdate.setOnClickListener(this);
        tvfromtime.setOnClickListener(this);
        tvtodate.setOnClickListener(this);
        tvfromtime.setOnClickListener(this);
        tvtotime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        fromDateString = toDateString = dateString = "" + curDay + "/" + (curMonth + 1) + "/" + curYear;
        etcontactno.setText(pref.getMobile());
        tvfromdate.setText(fromDateString);
        tvtodate.setText(toDateString);
        tvtotime.setText(currentTime);
        tvfromtime.setText(currentTime);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if (!Type.get(i).equalsIgnoreCase("-Select Type-")) {
                    showLayoutCorrespondingToCondition(Type.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void showLayoutCorrespondingToCondition(String Type) {
        if (Type.equalsIgnoreCase("In Punch Regularisation") || Type.equalsIgnoreCase("Out Puch Regularisation")) {
            etplace.setVisibility(View.GONE);
            lltodate.setVisibility(View.GONE);
            lltotime.setVisibility(View.GONE);
        } else if (Type.equals("In-Out Punch Regularisation") || Type.equals("On Duty")) {
            etplace.setVisibility(View.GONE);
            lltotime.setVisibility(View.VISIBLE);
            lltodate.setVisibility(View.GONE);

        } else {
            etplace.setVisibility(View.VISIBLE);
            lltotime.setVisibility(View.VISIBLE);
            lltodate.setVisibility(View.VISIBLE);
        }
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
            case R.id.tv_fromdate:
                fromDatePicker();
                break;
            case R.id.tv_todate:
                toDatePicker();
                break;
            case R.id.tv_fromtime:
                setTime(tvfromtime);
                break;
            case R.id.tv_totime:
                setTime(tvtotime);
                break;
            case R.id.btn_submit:
                startValidation();
                break;
        }
    }

    private void startValidation() {
        if (spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(activity, "Please select regularization type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etplace.getVisibility() == View.VISIBLE) {
            if (etplace.getText().toString().trim().length() <= 0) {
                Toast.makeText(activity, "Please fill the place field", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (etremark.getText().toString().trim().length() <= 0) {
            Toast.makeText(activity, "Please fill the remark field", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etcontactno.getText().toString().trim().length() <= 0) {
            Toast.makeText(activity, "Please fill the contact number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lltotime.getVisibility() == View.VISIBLE) {
            if (!MyCalenderUtil.validateFromTimeAndToTime(tvfromtime.getText().toString(), tvtotime.getText().toString())) {
                Toast.makeText(activity, "From time can't be greater the to time", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        hitApiForSubmit();
    }

    private void hitApiForSubmit() {
        try {
            request = new ProjectWebRequest(getActivity(), getParamToSendRegulerizationData(), UrlConstants.SUBMIT_ATTENDANCE_REGULARIZATION_TYPE, this, SUBMIT_ATTENDANCE_REGULARIZATION_TYPE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParamToSendRegulerizationData() {
        JSONObject object = null;
        String ToDate, Place, ToTimehour = "", ToTimeMinut = "", FrmTimehour = "", FrmTimeMinut = "";
        try {

            if (lltodate.getVisibility() == View.VISIBLE) {
                ToDate = tvtodate.getText().toString();
            } else {
                ToDate = "";
            }

            if (lltotime.getVisibility() == View.VISIBLE) {
                String ToTime = tvtotime.getText().toString();
                StringTokenizer st = new StringTokenizer(ToTime, ":");
                ToTimehour = st.nextToken();
                ToTimeMinut = st.nextToken();
            } else {
                ToTimehour = "";
                ToTimeMinut = "";
            }

            if (etplace.getVisibility() == View.VISIBLE) {
                Place = etplace.getText().toString();
            } else {
                Place = "";
            }

            StringTokenizer st = new StringTokenizer(tvfromtime.getText().toString(), ":");
            FrmTimehour = st.nextToken();
            FrmTimeMinut = st.nextToken();

            object = new JSONObject();
            object.put(TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());
            object.put("FromDate", tvfromdate.getText().toString());
            object.put("FromTimeHour", FrmTimehour);
            object.put("FromTimeMin", FrmTimeMinut);
            object.put("ToDate", ToDate);
            object.put("ToTimeHour", ToTimehour);
            object.put("ToTimeMin", ToTimeMinut);
            object.put("Reason", etremark.getText().toString());
            object.put("RegularizationTypeID", TypeID.get(spinner.getSelectedItemPosition()));
            object.put("ContactNo", etcontactno.getText().toString());
            object.put("Place", Place);


            // object.put("HODID", pref.getStringData(HODID));
            Log.e("@@@@@@@@", object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void setTime(final TextView tv) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String aTime = new StringBuilder().append(selectedHour).append(':').append(String.valueOf(selectedMinute)).toString();
                tv.setText(aTime);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void toDatePicker() {
        int month, day, year;
        if (fromYear != 0 && fromDay != 0) {
            month = fromMonth;
            year = fromYear;
            day = fromDay;
        } else {
            month = curMonth;
            year = curYear;
            day = curDay;
        }
        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                toDateString = dayOfMonth + "/" + (month + 1) + "/" + year;
                tvtodate.setText(toDateString);
            }
        }, year, month, day);
        Calendar cal = createCalender("" + day, "" + (month - 1), "" + year);
        expdatePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        expdatePickerDialog.show();
    }

    private void fromDatePicker() {
        final int month, day, year;
        if (fromYear != 0 && fromDay != 0) {
            month = fromMonth;
            year = fromYear;
            day = fromDay;
        } else {
            month = curMonth;
            year = curYear;
            day = curDay;
        }

        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String mDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                if (lltodate.getVisibility() == View.VISIBLE) {
                    if (!CalenderUtil.validDate(activity, mDate, toDateString)) {
                        Toast.makeText(activity, "From Date Must be Less Than To Date", Toast.LENGTH_LONG).show();
                    } else {
                        fromMonth = month;
                        fromDay = dayOfMonth;
                        fromYear = year;
                        fromDateString = mDate;
                    }
                } else {
                    fromMonth = month;
                    fromDay = dayOfMonth;
                    fromYear = year;
                    fromDateString = mDate;
                }


                tvfromdate.setText(fromDateString);
            }
        }, year, month, day);
        expdatePickerDialog.show();
    }

    Calendar createCalender(String day, String month, String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, Integer.parseInt(month) + 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        return calendar;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        if (Tag == OD_GET_BASIC_DETAILS_TAG) {
            if (call.optString("Status").equals("true")) {

                try {
                    String RMName = call.getString("RMName");
                    et_rm.setText(RMName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Type = new ArrayList<String>();
                TypeID = new ArrayList<String>();

                TypeID.add("-1");
                Type.add("-Select Type-");

                try {
                    JSONArray arr = call.getJSONArray("objARRegularizationType");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        Type.add(object.optString("Text"));
                        TypeID.add(object.optString("ID"));
                    }
                    spinner.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, Type));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (Tag == SUBMIT_ATTENDANCE_REGULARIZATION_TYPE_TAG) {
            try {
                if (call.optString("Status").equals("true")) {
                    //showDialog(call.getString("Message"));
                    AppAlertDialog.showDialogOnSuccess(activity, call.getString("Message"), "FragmentAttendanceRegularizationTab", "newrequest",
                            "Attendance Regularization", "1");
                }else {
                    AppAlertDialog.showDialogSelfFinish(activity, "Short Leave", call.optString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

   /* private void showDialog(String message) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(activity);
        builder.setTitle("").setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent= new Intent(getContext(), FrameActivity.class);
                        intent.putExtra("frag_name", "FragmentAttendanceRegularizationTab");
                        intent.putExtra("frag_tag", "newrequest");
                        intent.putExtra("title", "Attendance Regularization");
                        intent.putExtra("tab", "1");
                        startActivity(intent);
                    }
                })
                .show();
    }*/

    @Override
    public void onFailure(VolleyError error, int Tag) {
        Toast.makeText(activity, "" + error.toString(), Toast.LENGTH_SHORT).show();
    }
}
