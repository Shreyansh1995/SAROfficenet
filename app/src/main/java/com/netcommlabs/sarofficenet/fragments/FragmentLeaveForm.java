package com.netcommlabs.sarofficenet.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.activity.MainActivity;
import com.netcommlabs.sarofficenet.adapter.CompOffAdapter;
import com.netcommlabs.sarofficenet.adapter.LeaveTypeListAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.CompOffModel;
import com.netcommlabs.sarofficenet.model.LeaveBalanceModel;
import com.netcommlabs.sarofficenet.model.LeaveTypeModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.DateManagerUtility;
import com.netcommlabs.sarofficenet.utils.MyCalenderUtil;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.netcommlabs.sarofficenet.utils.MyCalenderUtil.createCalender;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLeaveForm extends Fragment implements ResponseListener, View.OnClickListener {
    private static final int PERMISSIONS_REQUEST_CAMERA = 20;
    private View viewMain;
    private String fromDateString = null;
    private String toDateString = null;
    private String expectedDeliveryDate = null;
    private Spinner spnr_leave_type;
    private Spinner spnr_materinity;
    private Spinner spnr_year;
    private String calcDays = null;
    private String hstate = null;
    private String thstate = null;
    private String CompOffDays = null;
    static String base64;
    private String RmName;
    private MySharedPreference pref;
    private ProjectWebRequest request;
    private TextView tv_to_date;
    private TextView tv_from_date;
    private String totalDayToTakeLeaveCount = "0";
    private EditText et_reason;
    private EditText et_leave_address;
    private EditText et_contact;
    private TextView et_expected_delivery;
    private Button btn_submit_regularization_type;
    private int curYear;
    private int curMonth;
    private int curDay;
    private String userChoosenTask;


    private static final int PICKFILE_RESULT_CODE = 1;
    private CheckBox rd_first_half;
    private CheckBox rd_sec_half;
    private String encodedImageData = "";
    //  private LinearLayout ll_first_half;
    // private LinearLayout ll_sec_half;
    private LinearLayout ll_to_date;
    private LinearLayout ll_from_date;
    // private LinearLayout ll_spnr_materinity;
    //    private LinearLayout ll_exp_del;
    private RelativeLayout rl_show_leave_detail;
    private String formattedMonth;
    private String formattedDayOfMonth;
    private TextView tv_count_days;
    private EditText tv_rm_name;
    private TextView tv_hod_name;
    // private TextView tv_calc_days;
    private ImageView img_plus_minus;
    private LinearLayout ll_leave_detail;
    private LinearLayout ll_leave_detail_row;
    private LinearLayout ll_leave_detail_row_dialog;

    private LinearLayout ll_upload_doc;
    private static ImageView img_uplaod_doc;
    private String mainFilename;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ArrayList<LeaveTypeModel> leaveTypeList;
    private ArrayList<CompOffModel> compOffList;
    private ArrayList<LeaveBalanceModel> listLeaveBalanceModel;
    private ArrayList<String> Year;

    private String str_AtAtimedays, str_SystemLeaveName, CertificateFlag;
    private ImageView img_set_image;
    String filename;
    private TextView error_text;
    private String noOfdays;
    int count = 0;

    private int fromYear;
    private int fromMonth;
    private int fromDay;
    private int toYear;
    private int toMonth;
    private int toDay;
    private LinearLayout ll_comp_off;
    private ListView lv_comp_off_list;
    private int CurrentYear;
    private boolean isFirstTime = true;
    private boolean isFirstTimeHitApi = false;
    private String MaximumFrequency, SystemLeaveName, FutureGracePeriod, CertificateReqDayLmt, IsDeliveryDateVisable, IsFirstHalfVisable, IsSecondHalfVisable, IsUploadDocumentVisable;
    private LinearLayout v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_leave_form, container, false);
        Calendar calendar = Calendar.getInstance();
        CurrentYear = calendar.get(Calendar.YEAR);
        initView();
        return viewMain;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ll_from_date:
//                tv_count_days.setVisibility(View.GONE);
                if (spnr_leave_type.getSelectedItemPosition() != 0) {
                    calcDays = null;
                    fromDatePicker();
                } else
                    Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();
                break;


            case R.id.rl_show_leave_detail:
                if (ll_leave_detail.getVisibility() == View.VISIBLE) {
                    img_plus_minus.setBackgroundResource(R.drawable.plus_white);
                    // ll_leave_detail.setVisibility(View.GONE);
                } else {
                    // img_plus_minus.setBackgroundResource(R.drawable.minus_white);
                    // ll_leave_detail.setVisibility(View.VISIBLE);
                    showDialog();
                }
                break;
            case R.id.ll_to_date:
//                tv_count_days.setVisibility(View.GONE);
                if (spnr_leave_type.getSelectedItemPosition() != 0) {
                    calcDays = null;
                    toDatePicker();

                } else
                    Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_submit_regularization_type:
                if (startValidation()) {
                    hitApiForSubmitLeaveRequest();
                }
                break;

           /* case R.id.tv_calc_days:
                calcDays = null;
//                tv_count_days.setVisibility(View.GONE);
                hitApiForCalcDays();
                break;*/

            case R.id.rd_first_half:
                if (spnr_leave_type.getSelectedItemPosition() != 0) {
                    if (rd_first_half.isChecked()) {
                        rd_first_half.setChecked(false);

                    } else {
                        rd_first_half.setChecked(true);

                        rd_sec_half.setChecked(false);

                    }
                    hitApiForCalcDays();
                } else
                    Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rd_sec_half:
                if (spnr_leave_type.getSelectedItemPosition() != 0) {
                    if (rd_sec_half.isChecked()) {
                        rd_sec_half.setChecked(false);
                    } else {
                        rd_sec_half.setChecked(true);
                        rd_first_half.setChecked(false);
                    }
                    hitApiForCalcDays();
                } else
                    Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();

                break;

            case R.id.et_expected_delivery:
                getSelectedDate();
                break;

            case R.id.img_uplaod_doc:
                //  permissionListener.checkPermission();
                checkPermission();
                //  pickFileFromGallary();
                break;


        }
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
            } else {
                pickFileFromGallary();
            }
        } else
            pickFileFromGallary();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickFileFromGallary();
            } else {
                //code for deny
            }
        }
    }

    private void pickFileFromGallary() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //  boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    //  if(result)
                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    //   if(result)
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        final Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        encodedImageData = getEncoded64ImageStringFromBitmap(thumbnail);

        img_set_image.setImageBitmap(thumbnail);


    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String file = String.valueOf(data.getData());
                filename = file.substring(file.lastIndexOf("/") + 1);
                mainFilename = filename.replace("%", "");
                encodedImageData = getEncoded64ImageStringFromBitmap(bm);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        img_set_image.setImageBitmap(bm);
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    private void getSelectedDate() {
        int month = curMonth, day = curDay, year = curYear;
        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                expectedDeliveryDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                et_expected_delivery.setText(expectedDeliveryDate);
            }
        }, year, month, day);
        Calendar cal = createCalender("" + day, "" + (month - 1), "" + year);
        expdatePickerDialog.show();
    }

    void initView() {
        pref = MySharedPreference.getInstance(getContext());
        getCurrentDateTime();
        error_text = (TextView) viewMain.findViewById(R.id.error_text);
        spnr_leave_type = (Spinner) viewMain.findViewById(R.id.spnr_leave_type);
        spnr_materinity = (Spinner) viewMain.findViewById(R.id.spnr_materinity);
        spnr_year = (Spinner) viewMain.findViewById(R.id.sp_year);
        img_plus_minus = (ImageView) viewMain.findViewById(R.id.img_plus_minus);
        img_set_image = (ImageView) viewMain.findViewById(R.id.img_set_image);

        rl_show_leave_detail = (RelativeLayout) viewMain.findViewById(R.id.rl_show_leave_detail);
        btn_submit_regularization_type = (Button) viewMain.findViewById(R.id.btn_submit_regularization_type);
        tv_to_date = (TextView) viewMain.findViewById(R.id.tv_to_date);
        tv_from_date = (TextView) viewMain.findViewById(R.id.tv_from_date);
        et_reason = (EditText) viewMain.findViewById(R.id.et_reason);
        et_leave_address = (EditText) viewMain.findViewById(R.id.et_leave_address);
        et_contact = (EditText) viewMain.findViewById(R.id.et_contact);
        et_expected_delivery = (TextView) viewMain.findViewById(R.id.et_expected_delivery);

        //   tv_calc_days = (TextView) viewMain.findViewById(R.id.tv_calc_days);
        tv_count_days = (TextView) viewMain.findViewById(R.id.tv_count_days);
        rd_first_half = (CheckBox) viewMain.findViewById(R.id.rd_first_half);
        rd_sec_half = (CheckBox) viewMain.findViewById(R.id.rd_sec_half);

        //   ll_first_half = (LinearLayout) viewMain.findViewById(R.id.ll_first_half);
        //   ll_sec_half = (LinearLayout) viewMain.findViewById(R.id.ll_sec_half);
        // ll_spnr_materinity = (LinearLayout) viewMain.findViewById(R.id.ll_spnr_materinity);

        ll_to_date = (LinearLayout) viewMain.findViewById(R.id.ll_to_date);
        ll_from_date = (LinearLayout) viewMain.findViewById(R.id.ll_from_date);
        tv_rm_name = viewMain.findViewById(R.id.et_rm);
        //  tv_hod_name = (TextView) viewMain.findViewById(R.id.tv_hod_name);
        ll_leave_detail = (LinearLayout) viewMain.findViewById(R.id.ll_leave_detail);
        ll_leave_detail_row = (LinearLayout) viewMain.findViewById(R.id.ll_leave_detail_row);

        ll_upload_doc = (LinearLayout) viewMain.findViewById(R.id.ll_upload_doc);
        img_uplaod_doc = (ImageView) viewMain.findViewById(R.id.img_uplaod_doc);
        ll_comp_off = viewMain.findViewById(R.id.ll_comp_off);
        lv_comp_off_list = viewMain.findViewById(R.id.lv_comp_off_list);

        String RM = pref.getRm();
        if (!TextUtils.isEmpty(RM)) {
            tv_rm_name.setText(RM);
        } else {
            tv_rm_name.setText("Not Found");
        }


        btn_submit_regularization_type.setOnClickListener(this);
//        tv_calc_days.setOnClickListener(this);
        // ll_first_half.setOnClickListener(this);
        //ll_sec_half.setOnClickListener(this);
        ll_to_date.setOnClickListener(this);
        ll_from_date.setOnClickListener(this);
        et_expected_delivery.setOnClickListener(this);
        rl_show_leave_detail.setOnClickListener(this);
        ll_leave_detail.setOnClickListener(this);
        ll_leave_detail_row.setOnClickListener(this);
        img_uplaod_doc.setOnClickListener(this);
        et_contact.setText(pref.getMobile());
        tv_from_date.setText("" + formattedDayOfMonth + "/" + formattedMonth + "/" + curYear);
        tv_to_date.setText("" + formattedDayOfMonth + "/" + formattedMonth + "/" + curYear);
        fromDateString = toDateString = formattedDayOfMonth + "/" + formattedMonth + "/" + curYear;

        spnr_leave_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                et_expected_delivery.setVisibility(View.GONE);
                if (position > 0) {

                    hitApiforGetDataOnLeaveTypeChange();

                    count = 0;
                    //14 for materinity check


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        spnr_materinity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 1) {
                    et_expected_delivery.setVisibility(View.VISIBLE);
                } else {
                    et_expected_delivery.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spnr_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //      Toast.makeText(activity, ""+i, Toast.LENGTH_SHORT).show();
                CurrentYear = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                if (!TextUtils.isEmpty(String.valueOf(CurrentYear))) {
                    if (isFirstTimeHitApi) {
                        hitApiForLeaveBalance();
                    }
                    isFirstTimeHitApi = true;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
            }
        });

        //  hitApiForLeaveType();

        rd_first_half.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (spnr_leave_type.getSelectedItemPosition() != 0) {
                    if (rd_first_half.isChecked()) {
                        rd_sec_half.setChecked(false);
                    }
                    hitApiForCalcDays();
                } else {
                    Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rd_sec_half.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (spnr_leave_type.getSelectedItemPosition() != 0) {

                    if (rd_sec_half.isChecked()) {
                        rd_first_half.setChecked(false);
                    }
                   /* if (rd_first_half.isChecked()) {
                        rd_first_half.setChecked(false);
                    } else {
                        rd_sec_half.setChecked(true);
                        rd_first_half.setChecked(false);
                    }*/
                    hitApiForCalcDays();
                } else {
                    Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //hitApiForLeaveBalance();
        if (getUserVisibleHint()) {
            hitApiForLeaveBalance();
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            hitApiForLeaveBalance();
        }
    }


    private void hitApiForLeaveBalance() {
        try {
            request = new ProjectWebRequest(getActivity(), getParamForLeaveType(), UrlConstants.GET_LEAVE_BALANCE, this, UrlConstants.GET_LEAVE_BALANCE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void hitApiforGetDataOnLeaveTypeChange() {
        try {
            request = new ProjectWebRequest(getActivity(), getParamForLeaveTypeChnage(), UrlConstants.GET_DATA_ON_LEAVE_CHANGE_TYPE, this, UrlConstants.GET_DATA_ON_LEAVE_CHANGE_TYPE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParamForLeaveTypeChnage() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());
            object.put("LeaveTypeID", leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId());
            object.put("LeaveTypeText", leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveType());
        } catch (Exception e) {
        }
        return object;
    }

    void hitApiForCalcDays() {
        if (validateForCalDays())
            try {
                request = new ProjectWebRequest(getActivity(), getParamForCalDays(), UrlConstants.CALCULATE_LEAVE, this, UrlConstants.CALCULATE_LEAVE_TAG);
                request.execute();
            } catch (Exception e) {
                clearRef();
                e.printStackTrace();
            }
    }

    JSONObject getParamForLeaveType() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());
            object.put("PlantID", pref.getPlantid());
            object.put("Year", CurrentYear);
        } catch (Exception e) {
        }
        return object;
    }

    JSONObject getParamForCalDays() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());

            object.put("MaximumFrequency", MaximumFrequency);
            object.put("SystemLeaveName", SystemLeaveName);
            // object.put("FutureGracePeriod", FutureGracePeriod);
            // object.put("CertificateFlag", CertificateFlag);
            object.put("LeaveTypeText", leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveType());
            object.put("CertificateReqDayLmt", CertificateReqDayLmt);

            object.put("LeaveTypeID", leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId());
            if (rd_sec_half.isChecked()) {
                object.put("SecondHalfLeave", "1");
            } else {
                object.put("SecondHalfLeave", "0");
            }

            if (rd_first_half.isChecked()) {
                object.put("FirstHalfLeave", "1");
            } else {
                object.put("FirstHalfLeave", "0");
            }

            object.put("FromDate", fromDateString);
            object.put("ToDate", toDateString);
            Log.i("object", "CALCULATE_LEAVE_TAG :" + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        Log.i("responceleaveform", "" + obj.toString());
        clearRef();
        if (Tag == UrlConstants.CALCULATE_LEAVE_TAG) {
            String msg = null;
            msg = obj.optString("Message");
            if (obj.optString("Status").equals("true")) {
                btn_submit_regularization_type.setEnabled(true);
                btn_submit_regularization_type.setTextColor(getResources().getColor(R.color.colorWhite));
                tv_count_days.setVisibility(View.VISIBLE);
                calcDays = obj.optString("NoOfDays");
                hstate = obj.optString("SecondHalfStatus");
                thstate = obj.optString("firstHalfStatus");
                IsUploadDocumentVisable = obj.optString("IsUploadDocumentVisable");

                tv_count_days.setText(calcDays + " Day");

                if (IsUploadDocumentVisable.equalsIgnoreCase("true")) {
                    ll_upload_doc.setVisibility(View.VISIBLE);
                } else {
                    ll_upload_doc.setVisibility(View.GONE);
                }


            } else {
                btn_submit_regularization_type.setEnabled(false);
                btn_submit_regularization_type.setTextColor(getResources().getColor(R.color.border_color));
               /* tv_from_date.setHint("");
                tv_to_date.setHint("");
                tv_from_date.setText("");
                tv_to_date.setText("");

                fromDateString = null;
                toDateString = null;*/

                tv_count_days.setText("0 Days");

                AppAlertDialog.showDialogSelfFinish(getContext(), "Message", obj.optString("Message"));

                //   Toast.makeText(activity, "" + msg, Toast.LENGTH_SHORT).show();
            }
        } else if (Tag == UrlConstants.SUBMIT_LEAVE_REQUEST_TAG) {
            if (obj.optString("Status").equals("true")) {
                // AppAlertDialog.showDialogFinishWithActivity(getContext(), "", obj.optString("Message"));
                showDialogOnSuccess(obj.optString("Message"));
            } else {
                AppAlertDialog.showDialogSelfFinish(getContext(), "", obj.optString("Message"));
            }

        } else if (Tag == UrlConstants.GET_DATA_ON_LEAVE_CHANGE_TYPE_TAG) {

            if (obj.optString("Status").equals("true")) {
                str_AtAtimedays = obj.optString("AtAtimedays");
                str_SystemLeaveName = obj.optString("SystemLeaveName");
                CertificateReqDayLmt = obj.optString("CertificateReqDayLmt");
                IsDeliveryDateVisable = obj.optString("IsDeliveryDateVisable");
                IsFirstHalfVisable = obj.optString("IsFirstHalfVisable");
                IsSecondHalfVisable = obj.optString("IsSecondHalfVisable");
                try {
                    MaximumFrequency = obj.getString("MaximumFrequency");
                    SystemLeaveName = obj.getString("SystemLeaveName");
                    FutureGracePeriod = obj.getString("FutureGracePeriod");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (IsDeliveryDateVisable.equalsIgnoreCase("true")) {
                    et_expected_delivery.setVisibility(View.VISIBLE);
                } else {
                    et_expected_delivery.setVisibility(View.GONE);
                }
                if (IsFirstHalfVisable.equalsIgnoreCase("true")) {
                    rd_first_half.setVisibility(View.VISIBLE);
                } else {
                    rd_first_half.setVisibility(View.GONE);
                    rd_first_half.setChecked(false);
                }

                if (IsSecondHalfVisable.equalsIgnoreCase("true")) {
                    rd_sec_half.setVisibility(View.VISIBLE);
                } else {
                    rd_sec_half.setVisibility(View.GONE);
                    rd_sec_half.setChecked(false);
                }

                if (str_SystemLeaveName.equals("COMP OFF")) {
                    //  hitApiForGetUsersCompOffList();
                    showCompoff(obj);
                } else {
                    // ll_comp_off.setVisibility(View.GONE);
                }


            } else if (obj.optString("Status").equalsIgnoreCase("false")) {
                Toast.makeText(getContext(), obj.optString("Message"), Toast.LENGTH_SHORT).show();
            }

        } else if (Tag == UrlConstants.GET_LEAVE_BALANCE_TAG) {
            if (obj.optString("Status").equals("true")) {
                listLeaveBalanceModel = new ArrayList<LeaveBalanceModel>();
                LeaveBalanceModel model;
                try {
                    JSONArray arr = obj.getJSONArray("objLeaveBalanceType");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        model = new LeaveBalanceModel();
                        model.setAvailedIn(object.optString("AvailedIn"));
                        model.setBalance(object.optString("Balance"));
                        model.setCarryForwarded(object.optString("CarryForwarded"));
                        model.setCreditedIn(object.optString("CreditedIn"));
                        model.setLeaveType(object.optString("LeaveType"));
                        model.setPendingForApproval(object.optString("PendingForApproval"));
                        listLeaveBalanceModel.add(model);
                    }
                    //initLeaveDetail();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                leaveTypeList = new ArrayList<LeaveTypeModel>();
                LeaveTypeModel model1;
                try {
                    model1 = new LeaveTypeModel();
                    model1.setLeaveType("-Select Leave Type-");
                    model1.setLeaveTypeId("-1");
                    leaveTypeList.add(model1);
                    JSONArray arr = obj.getJSONArray("objLeaveType");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        model1 = new LeaveTypeModel();
                        model1.setLeaveTypeId(object.optString("LeaveTypeID"));
                        model1.setLeaveType(object.optString("LeaveTypeText"));
                        leaveTypeList.add(model1);
                    }
                    spnr_leave_type.setAdapter(new LeaveTypeListAdapter(getActivity(), leaveTypeList));
                    if (isFirstTime) {
                        Year = new ArrayList<String>();
                        JSONArray jsonArray = obj.getJSONArray("objLeaveCalFinYear");
                        for (int z = 0; z < jsonArray.length(); z++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(z);
                            Year.add(jsonObject.getString("Year"));
                        }
                        spnr_year.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, Year));
                    }
                    isFirstTime = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AppAlertDialog.showDialogFinishWithActivity(getContext(), "Message", obj.optString("Message"));
                //getActivity().onBackPressed();
            }
        }
    }

    private void showDialogOnSuccess(String message) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setTitle("").setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(getContext(), FrameActivity.class);
                        intent.putExtra("frag_name", "FragmentLeaveTab");
                        intent.putExtra("frag_tag", "leave request");
                        intent.putExtra("title", "Leave");
                        intent.putExtra("tab", "1");
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        Toast.makeText(getContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();

    }


    void showCompoff(JSONObject obj) {

        compOffList = new ArrayList<CompOffModel>();
        CompOffModel model;
        try {
            JSONArray arr = obj.getJSONArray("UsersCompOff");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                model = new CompOffModel();
                model.setAvailedDays(object.optString("AvailedDays"));
                model.setBalance(object.optString("Balance"));
                model.setCompOffDate(object.optString("CompOffDate"));
                model.setCompOffID(object.optString("CompOffID"));
                model.setNoOfDays(object.optString("NoOfDays"));
                model.setReqDate(object.optString("ReqDate"));
                compOffList.add(model);
            }
            if (compOffList.size() != 0) {
                ll_comp_off.setVisibility(View.VISIBLE);
                lv_comp_off_list.setAdapter(new CompOffAdapter(getContext(), compOffList));
            } else {
                error_text.setText("No CompOff available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLeaveDetail() {
        for (int i = 0; i < listLeaveBalanceModel.size(); i++) {
            v = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.leave_detail_row, null);
            TextView type_leave = (TextView) v.findViewById(R.id.type_leave);
            TextView carry_forwd_leave = (TextView) v.findViewById(R.id.carry_forwd_leave);
            TextView credit_in_leave = (TextView) v.findViewById(R.id.credit_in_leave);
            TextView Availed_leave = (TextView) v.findViewById(R.id.Availed_leave);
            TextView pending_leave = (TextView) v.findViewById(R.id.pending_leave);
            TextView balance_leave = (TextView) v.findViewById(R.id.balance_leave);
            type_leave.setText(listLeaveBalanceModel.get(i).getLeaveType());
            carry_forwd_leave.setText(listLeaveBalanceModel.get(i).getCarryForwarded());
            credit_in_leave.setText(listLeaveBalanceModel.get(i).getCreditedIn());
            Availed_leave.setText(listLeaveBalanceModel.get(i).getAvailedIn());
            pending_leave.setText(listLeaveBalanceModel.get(i).getPendingForApproval());
            balance_leave.setText(listLeaveBalanceModel.get(i).getBalance());
            //  ll_leave_detail_row.addView(v);
        }
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    void getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH);
        curDay = c.get(Calendar.DAY_OF_MONTH);
        DateFormat df = new SimpleDateFormat("kk:mm a");
        //  currentTime = df.format(Calendar.getInstance().getTime());

        int month = curMonth + 1;
        formattedMonth = "" + month;
        formattedDayOfMonth = "" + curDay;

        if (month < 10) {

            formattedMonth = "0" + month;
        }
        if (curDay < 10) {

            formattedDayOfMonth = "0" + curDay;
        }


    }

    void fromDatePicker() {

        int month, year, day;
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
                fromYear = year;
                fromDay = dayOfMonth;
                fromMonth = month;

                int _month = month + 1;
                formattedMonth = "" + _month;
                formattedDayOfMonth = "" + dayOfMonth;

                if (_month < 10) {

                    formattedMonth = "0" + _month;
                }
                if (dayOfMonth < 10) {

                    formattedDayOfMonth = "0" + dayOfMonth;
                }


                String mDate = formattedDayOfMonth + "/" + formattedMonth + "/" + year;

                fromDateString = mDate;

                tv_from_date.setText(fromDateString);
                tv_to_date.setText(fromDateString);
                toDateString = fromDateString;
                error_text.setVisibility(View.GONE);
                hitApiForCalcDays();

            }
        }, year, month, day);

        expdatePickerDialog.show();
    }

    boolean startValidation() {
        if (spnr_leave_type.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select Leave Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("14")) {
            if (spnr_materinity.getSelectedItemPosition() == 0) {
                Toast.makeText(getContext(), "Please select Materinity Type", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveType().equalsIgnoreCase("Sick Leave")) {
            if (Integer.parseInt(calcDays) > 2){
                if (TextUtils.isEmpty(encodedImageData)){
                    if (encodedImageData.equalsIgnoreCase("")){
                        Toast.makeText(getContext(), "Please upload document", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        }

        if (fromDateString == null) {
            Toast.makeText(getContext(), "Please select From Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (toDateString == null) {
            Toast.makeText(getContext(), "Please select To Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_reason.getText().toString().trim().length() <= 0) {
            Toast.makeText(getContext(), "Please fill the reason of leave", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (spnr_materinity.getSelectedItemPosition() == 1) {
            if (et_expected_delivery.getText().toString().trim().length() <= 0) {
                Toast.makeText(getContext(), "Please Select Expected Delivery Date ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;

    }

    void toDatePicker() {
        int month, year, day;
        if (toYear != 0 && toDay != 0) {
            month = toMonth;
            year = toYear;
            day = toDay;
        } else {
            month = curMonth;
            year = curYear;
            day = curDay;
        }
        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                toYear = year;
                toDay = dayOfMonth;
                toMonth = month;
                int _month = month + 1;
                formattedMonth = "" + _month;
                formattedDayOfMonth = "" + dayOfMonth;

                if (_month < 10) {

                    formattedMonth = "0" + _month;
                }
                if (dayOfMonth < 10) {

                    formattedDayOfMonth = "0" + dayOfMonth;
                }


                toDateString = formattedDayOfMonth + "/" + formattedMonth + "/" + year;
                tv_to_date.setText(toDateString);

                if (tv_from_date.getText().toString() != null) {


                    if (MyCalenderUtil.validDate1(getContext(), fromDateString, toDateString, error_text)) {

                        error_text.setVisibility(View.GONE);
                    } else {

                    }
                }
                hitApiForCalcDays();

            }
        }, year, month, day);
        /*Calendar cal = createCalender("" + day, "" + (month - 1), "" + year);
        expdatePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());*/
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

    boolean validateForCalDays() {
        if (spnr_leave_type.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select Leave Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fromDateString == null) {
            Toast.makeText(getContext(), "Please select From Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (toDateString == null) {
            Toast.makeText(getContext(), "Please select To Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void hitApiForSubmitLeaveRequest() {
        try {
            request = new ProjectWebRequest(getActivity(), getParamToSubmitLeaveRequest(), UrlConstants.SUBMIT_LEAVE_REQUEST, this, UrlConstants.SUBMIT_LEAVE_REQUEST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    JSONObject getParamToSubmitLeaveRequest() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());
            object.put("LeaveTypeID", leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId());
            object.put("LeaveTypeText", leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveType());

            if (rd_sec_half.isChecked()) {
                object.put("SecondHalfStatus", "1");
            } else {
                object.put("SecondHalfStatus", "0");
            }

            if (rd_first_half.isChecked()) {
                object.put("FirstHalfStatus", "1");
            } else {
                object.put("FirstHalfStatus", "0");
            }


            object.put("FromDate", fromDateString);
            object.put("ToDate", toDateString);
            object.put("NoOfDays", calcDays);
            object.put("Reason", et_reason.getText().toString().trim());
            object.put("Address", et_leave_address.getText().toString().trim());
            object.put("ContactNo", et_contact.getText().toString().trim());
            // object.put("AtaTimeDays", str_AtAtimedays);
            object.put("SystemLeaveType", str_SystemLeaveName);
            object.put("BalanceYearType", CurrentYear);
            if (et_expected_delivery.getVisibility() == View.VISIBLE) {
                object.put("DeliveryDate", expectedDeliveryDate);
            } else {
                object.put("DeliveryDate", "");
            }
            if (encodedImageData != null) {

                object.put("FileInBase64", encodedImageData);
                //  object.put("FileName", mainFilename);
                object.put("FileExtension", ".jpg");
            } else {
                object.put("FileInBase64", "");
                //    object.put("FileName", "");
                object.put("FileExtension", "");
            }

            JSONArray array = new JSONArray();
            if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("5")) {

                for (int i = 0; i < compOffList.size(); i++) {
                    if (compOffList.get(i).isChecked() == true) {
                        JSONObject obj = new JSONObject();
                        if (compOffList.get(i).getCompOffID() != null) {
                            obj.put("CompOffID", compOffList.get(i).getCompOffID());
                            noOfdays = compOffList.get(i).getNoOfDays();
                            count++;
                            array.put(obj);
                        } else {
                            obj.put("CompOffID", "");
                            array.put(obj);
                        }

                    }
                }
            } else {
                JSONObject obj = new JSONObject();
                obj.put("CompOffID", "");
                array.put(obj);
            }
            object.put("LeaveCompOffReq", array);
            //   object.put("NoOfCompOffDays", strUSerCompOff);
            //   object.put("CertificateFlag", CertificateFlag);
            //    object.put("CertificateReqDayLimit", CertificateReqDayLimit);
            //   object.put("MaximumFrequency", strMaxFrequency);
            //   object.put("FutureGracePeriod", futureGracePeriod);

            Log.e("@@@@@@@@@@", object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.leave_detail_dialog);

        ll_leave_detail_row_dialog = dialog.findViewById(R.id.ll_leave_detail_row_dialog);
        for (int i = 0; i < listLeaveBalanceModel.size(); i++) {
            v = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.leave_detail_row, null);
            TextView type_leave = (TextView) v.findViewById(R.id.type_leave);
            TextView carry_forwd_leave = (TextView) v.findViewById(R.id.carry_forwd_leave);
            TextView credit_in_leave = (TextView) v.findViewById(R.id.credit_in_leave);
            TextView Availed_leave = (TextView) v.findViewById(R.id.Availed_leave);
            TextView pending_leave = (TextView) v.findViewById(R.id.pending_leave);
            TextView balance_leave = (TextView) v.findViewById(R.id.balance_leave);

            type_leave.setText(listLeaveBalanceModel.get(i).getLeaveType());
            carry_forwd_leave.setText(listLeaveBalanceModel.get(i).getCarryForwarded());
            credit_in_leave.setText(listLeaveBalanceModel.get(i).getCreditedIn());
            Availed_leave.setText(listLeaveBalanceModel.get(i).getAvailedIn());
            pending_leave.setText(listLeaveBalanceModel.get(i).getPendingForApproval());
            balance_leave.setText(listLeaveBalanceModel.get(i).getBalance());
          /*  if (pref.getGender().equalsIgnoreCase("1") && listLeaveBalanceModel.get(i).getLeaveType().equalsIgnoreCase("ML")) {

            }else {
                ll_leave_detail_row_dialog.addView(v);
            }*/
            //  ll_leave_detail_row.addView(v);
            ll_leave_detail_row_dialog.addView(v);
        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }
}
