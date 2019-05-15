package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.AttadenceRegulerizeDetailModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.LEAVE_APPROVE_DISAPPROVE_REQUEST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.LEAVE_CANCEL_REQUEST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.OD_DISAPPROVE_REQUEST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SL_DISAPPROVE_REQUEST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SL_VIEW_DETAIL_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.VIEW_MY_LEAVE_REQUEST_IN_POPUP_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.VIEW_MY_REGULERIZE_DETAIL_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentViewData extends Fragment implements ResponseListener, View.OnClickListener {

    private TextView tvname, tvempcode, tvdepartment, tvdesignation, tvlocation, tvdoj, tvsubmitdate, tvregtype, tvfrmdate, tvtodate, tvfrmtime, tvtotime, tvcontact, tvplace, tvreason;
    private Button btnapprove, btndisapprove, btncancle;
    private EditText etreason;
    private String ReqNo, Type,ReqId;
    private ProjectWebRequest request;
    private FrameActivity activity;
    private TextView tvremark1, tvremark2, tvdate1, tvdate2, tvstatus1, tvstatus2;
    private RelativeLayout rlplace;
    private LinearLayout llapproval1, llapproval2,llcancle;
    private String url, disApproveUrl, approveUrl, cancleUrl;
    private int tag, disApproveUrlTag, approveUrlTag, cancleUrlTag;
    private String title, RM_HOD_Flag, EmpID;
    private TextView tv_regularization,tv_submittion,tvfrmtimetxt,tv_totimetxt,tvdatefrm,tvdateto;
    private boolean from_cancel;
    private TextView tv_canceldate,tv_cancelstatus;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FrameActivity) {
            activity = (FrameActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_data, container, false);
        findviewByid(v);
        return v;
    }

    private void findviewByid(View v) {
        tvname = v.findViewById(R.id.tv_name);
        tvempcode = v.findViewById(R.id.tv_empcode);
        tvdepartment = v.findViewById(R.id.tv_department);
        tvdesignation = v.findViewById(R.id.tv_designation);
        tvlocation = v.findViewById(R.id.tv_location);
        tvdoj = v.findViewById(R.id.tv_doj);
        tvsubmitdate = v.findViewById(R.id.tv_submitdate);
        tvregtype = v.findViewById(R.id.tv_regtype);
        tvfrmdate = v.findViewById(R.id.tv_frmdate);
        tvtodate = v.findViewById(R.id.tv_todate);
        tvfrmtime = v.findViewById(R.id.tv_frmtime);
        tvtotime = v.findViewById(R.id.tv_totime);
        tvcontact = v.findViewById(R.id.tv_contact);
        tvplace = v.findViewById(R.id.tv_place);
        tvreason = v.findViewById(R.id.tv_reason);
        btnapprove = v.findViewById(R.id.btn_approve);
        btndisapprove = v.findViewById(R.id.btn_disapprove);
        btncancle = v.findViewById(R.id.btn_cancle);
        etreason = v.findViewById(R.id.et_remark);


        tv_regularization = v.findViewById(R.id.tv_regularization);
        tvdatefrm = v.findViewById(R.id.tvdatefrm);
        tvdateto = v.findViewById(R.id.tvdateto);
        tv_submittion = v.findViewById(R.id.tv_submittion);
        tvfrmtimetxt = v.findViewById(R.id.tvfrmtimetxt);
        tv_totimetxt = v.findViewById(R.id.tv_totimetxt);


        tv_cancelstatus = v.findViewById(R.id.tv_cancelstatus);
        tv_canceldate = v.findViewById(R.id.tv_canceldate);
        llcancle = v.findViewById(R.id.llcancle);

        btncancle.setOnClickListener(this);
        btndisapprove.setOnClickListener(this);
        btnapprove.setOnClickListener(this);

        rlplace = v.findViewById(R.id.rlplace);
        llapproval1 = v.findViewById(R.id.llapproval1);
        llapproval2 = v.findViewById(R.id.llapproval2);
        llcancle = v.findViewById(R.id.llcancle);


        tvremark1 = v.findViewById(R.id.tv_approval1remark);
        tvdate1 = v.findViewById(R.id.tv_approval1date);
        tvstatus1 = v.findViewById(R.id.tv_approval1status);

        tvremark2 = v.findViewById(R.id.tv_approval2remark);
        tvdate2 = v.findViewById(R.id.tv_approval2date);
        tvstatus2 = v.findViewById(R.id.tv_approval2status);

        ReqNo = getArguments().getString("reqnum");
        ReqId = getArguments().getString("reqid");
        Type = getArguments().getString("type");
        title = getArguments().getString("urltype");
        String cancel = getArguments().getString("show_cancel");

        if (!TextUtils.isEmpty(cancel)){
            if (cancel.equalsIgnoreCase("true")){
                from_cancel = true;
            }
        }


        if (from_cancel){
            btncancle.setVisibility(View.VISIBLE);
        }
        else {
            btncancle.setVisibility(View.GONE);
        }

        if (title.equalsIgnoreCase("AttReg")) {
            url = UrlConstants.VIEW_MY_REGULERIZE_DETAIL;
            tag = VIEW_MY_REGULERIZE_DETAIL_TAG;

            disApproveUrl = UrlConstants.OD_DISAPPROVE_REQUEST;
            disApproveUrlTag = OD_DISAPPROVE_REQUEST_TAG;

            approveUrl = UrlConstants.OD_APPROVE_REQUEST;
            approveUrlTag = UrlConstants.OD_APPROVE_REQUEST_TAG;

            cancleUrl = UrlConstants.OD_CANCEL_REQUEST;
            cancleUrlTag = UrlConstants.OD_CANCEL_REQUEST_TAG;

            hitApiForViewDetail(ReqId, Type);

        } else if (title.equalsIgnoreCase("short")) {


            tv_regularization.setText("Leave Date");
            tvdatefrm.setText("Purpose");
            tvdateto.setText("Attendance Time");



            url = UrlConstants.SL_VIEW_DETAIL;
            tag = SL_VIEW_DETAIL_TAG;

            disApproveUrl = UrlConstants.SL_DISAPPROVE_REQUEST;
            disApproveUrlTag = SL_DISAPPROVE_REQUEST_TAG;

            approveUrl = UrlConstants.SL_APPROVE_REQUEST;
            approveUrlTag = UrlConstants.SL_APPROVE_REQUEST_TAG;

            cancleUrl = UrlConstants.SL_CANCEL_REQUEST;
            cancleUrlTag = UrlConstants.SL_CANCEL_REQUEST_TAG;
            hitApiForViewDetail(ReqId, Type);
        } else {

            tv_regularization.setText("Leave Type");
            tv_submittion.setText("Submission Date");
            tvfrmtimetxt.setText("Purpose");
            tv_totimetxt.setText("No. Of Days");

            url = UrlConstants.VIEW_MY_LEAVE_REQUEST_IN_POPUP;
            tag = VIEW_MY_LEAVE_REQUEST_IN_POPUP_TAG;

            disApproveUrl = UrlConstants.LEAVE_APPROVE_DISAPPROVE_REQUEST;
            disApproveUrlTag = LEAVE_APPROVE_DISAPPROVE_REQUEST_TAG;

            cancleUrl = UrlConstants.LEAVE_CANCEL_REQUEST;
            cancleUrlTag = LEAVE_CANCEL_REQUEST_TAG;

            hitApiForViewDetailLeaveType(ReqNo, Type);

        }



      /*  btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWarnAlertForCancelRequest("Cancle");
            }
        });*/
    }

    private void hitApiForViewDetailLeaveType(String reqNo, String Mode) {
        try {
            request = new ProjectWebRequest(activity, getLeaveParam(reqNo, Mode), url, this, tag);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getLeaveParam(String reqNo, String mode) {
        MySharedPreference pref = MySharedPreference.getInstance(activity);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqNo", reqNo);
            object.put("Mode", mode);
        } catch (Exception e) {
        }
        return object;
    }


    void hitApiForViewDetail(String reqId, String Type) {
        try {
            request = new ProjectWebRequest(activity, getParam(reqId, Type), url, this, tag);
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

    private JSONObject getParam(String reqId, String type) {
        MySharedPreference pref = MySharedPreference.getInstance(activity);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", reqId);
            object.put("Type", Type);
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {

        if (Tag == VIEW_MY_REGULERIZE_DETAIL_TAG) {
            if (obj.optString("Status").equals("true")) {
                tvname.setText(obj.optString("EmpName"));
                tvempcode.setText(obj.optString("EmpCode"));
                tvdepartment.setText(obj.optString("EmpDepartment"));
                tvdesignation.setText(obj.optString("EmpDesignation"));
                tvlocation.setText(obj.optString("EmpLocation"));
                tvdoj.setText(obj.optString("EmpDOJ"));
                tvsubmitdate.setText(obj.optString("ReqDate"));
                tvregtype.setText(obj.optString("RegularisationType"));
                tvfrmdate.setText(obj.optString("FromDate"));
                tvtodate.setText(obj.optString("AttendanceTime"));
                tvfrmtime.setText(obj.optString("FromTime"));
                tvtotime.setText(obj.optString("ToTime"));
                tvcontact.setText(obj.optString("ContactNo"));
                tvreason.setText(obj.optString("Reason"));
                tvplace.setText(obj.optString("Place"));

                if (obj.optString("IsApprovalbuttonVisable").equalsIgnoreCase("true")) {
                    etreason.setVisibility(View.VISIBLE);
                    btnapprove.setVisibility(View.VISIBLE);
                    btndisapprove.setVisibility(View.VISIBLE);
                } else {
                    etreason.setVisibility(View.GONE);
                    btnapprove.setVisibility(View.GONE);
                    btndisapprove.setVisibility(View.GONE);
                }

                if (obj.optString("IsCancelDetailsVisable").equalsIgnoreCase("true")){
                    llcancle.setVisibility(View.VISIBLE);
                    try {
                        tv_canceldate.setText(obj.getString("CancelDate"));
                        tv_cancelstatus.setText(obj.getString("CancelStatus"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                if (!from_cancel){
                    if (obj.optString("IsCancelButtonVisable").equalsIgnoreCase("true")) {
                        btncancle.setVisibility(View.VISIBLE);
                    } else {
                        btncancle.setVisibility(View.GONE);
                    }
//                }

                if (obj.optString("IsPlaceVisable").equalsIgnoreCase("true")) {
                    rlplace.setVisibility(View.VISIBLE);
                } else {
                    rlplace.setVisibility(View.GONE);
                }


                if (obj.optString("IsFirstApprovalDetailsVisable").equalsIgnoreCase("true")) {
                    llapproval1.setVisibility(View.VISIBLE);
                    tvstatus1.setText(obj.optString("RMStatus"));
                    tvdate1.setText(obj.optString("Approver1_Date"));
                    tvremark1.setText(obj.optString("Approver1_Remarks"));
                } else {
                    llapproval1.setVisibility(View.GONE);
                }

                if (obj.optString("IsSecondApprovalDetailsVisable").equalsIgnoreCase("true")) {
                    llapproval2.setVisibility(View.VISIBLE);
                    tvstatus2.setText(obj.optString("RMStatus"));
                    tvdate2.setText(obj.optString("Approver1_Date"));
                    tvremark2.setText(obj.optString("Approver1_Remarks"));
                } else {
                    llapproval2.setVisibility(View.GONE);
                }
            }
        } else if (Tag == UrlConstants.OD_CANCEL_REQUEST_TAG) {
            Toast.makeText(activity, obj.optString("Message"), Toast.LENGTH_SHORT).show();
            activity.onBackPressed();
        } else if (Tag == UrlConstants.OD_APPROVE_REQUEST_TAG) {
            Toast.makeText(activity, obj.optString("Message"), Toast.LENGTH_SHORT).show();
            activity.onBackPressed();
        } else if (Tag == OD_DISAPPROVE_REQUEST_TAG) {
            Toast.makeText(activity, obj.optString("Message"), Toast.LENGTH_SHORT).show();
            activity.onBackPressed();
        } else if (Tag == SL_VIEW_DETAIL_TAG) {
            if (obj.optString("Status").equals("true")) {
                tvname.setText(obj.optString("EmpName"));
                tvempcode.setText(obj.optString("EmpCode"));
                tvdepartment.setText(obj.optString("EmpDepartment"));
                tvdesignation.setText(obj.optString("EmpDesignation"));
                tvlocation.setText(obj.optString("EmpLocation"));
                tvdoj.setText(obj.optString("EmpDOJ"));
                tvsubmitdate.setText(obj.optString("ReqDate"));
                tvregtype.setText(obj.optString("LeaveDate"));
                tvfrmdate.setText(obj.optString("Purpose"));
                tvtodate.setText(obj.optString("ToDate"));
                tvfrmtime.setText(obj.optString("FromTime"));
                tvtotime.setText(obj.optString("ToTime"));
                tvcontact.setText(obj.optString("ContactNo"));
                tvreason.setText(obj.optString("Reason"));
                tvplace.setText(obj.optString("Place"));


                if (obj.optString("IsApprovalbuttonVisable").equalsIgnoreCase("true")) {
                    etreason.setVisibility(View.VISIBLE);
                    btnapprove.setVisibility(View.VISIBLE);
                    btndisapprove.setVisibility(View.VISIBLE);
                } else {
                    etreason.setVisibility(View.GONE);
                    btnapprove.setVisibility(View.GONE);
                    btndisapprove.setVisibility(View.GONE);
                }


                if (obj.optString("IsCancelDetailsVisable").equalsIgnoreCase("true")){
                    llcancle.setVisibility(View.VISIBLE);
                    try {
                        tv_canceldate.setText(obj.getString("CancelDate"));
                        tv_cancelstatus.setText(obj.getString("CancelStatus"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

//                if (!from_cancel){
                    if (obj.optString("IsCancelButtonVisable").equalsIgnoreCase("true")) {
                        btncancle.setVisibility(View.VISIBLE);
                    } else {
                        btncancle.setVisibility(View.GONE);
                    }
//                }

               /* if (obj.optString("IsPlaceVisable").equalsIgnoreCase("true")) {
                    rlplace.setVisibility(View.VISIBLE);
                } else {
                    rlplace.setVisibility(View.GONE);
                }*/


                if (obj.optString("IsFirstApprovalDetailsVisable").equalsIgnoreCase("true")) {
                    llapproval1.setVisibility(View.VISIBLE);
                    tvstatus1.setText(obj.optString("RMStatus"));
                    tvdate1.setText(obj.optString("Approver1_Date"));
                    tvremark1.setText(obj.optString("Approver1_Remarks"));
                } else {
                    llapproval1.setVisibility(View.GONE);
                }

                if (obj.optString("IsSecondApprovalDetailsVisable").equalsIgnoreCase("true")) {
                    llapproval2.setVisibility(View.VISIBLE);
                    tvstatus2.setText(obj.optString("RMStatus"));
                    tvdate2.setText(obj.optString("Approver1_Date"));
                    tvremark2.setText(obj.optString("Approver1_Remarks"));
                } else {
                    llapproval2.setVisibility(View.GONE);
                }
            }
        } else if (Tag == VIEW_MY_LEAVE_REQUEST_IN_POPUP_TAG) {
            try {
                if (obj.optString("Status").equals("true")) {
                    tvname.setText(obj.optString("EmpName"));
                    tvempcode.setText(obj.optString("EmpCode"));
                    tvdepartment.setText(obj.optString("EmpDepartment"));
                    tvdesignation.setText(obj.optString("EmpDesignation"));
                    tvlocation.setText(obj.optString("EmpLocation"));
                    tvdoj.setText(obj.optString("EmpDOJ"));
                    tvsubmitdate.setText(obj.optString("ReqDate"));

                    tvfrmdate.setText(obj.optString("From_Date"));
                    tvtodate.setText(obj.optString("To_Date"));
                    tvcontact.setText(obj.optString("Contact"));
                    tvreason.setText(obj.optString("Reason"));
                    tvplace.setText(obj.optString("EmpLocation"));

                    EmpID = obj.optString("EmpID");
                    RM_HOD_Flag = obj.optString("RM_HOD_Flag");

                    tvregtype.setText(obj.optString("Leave_Type"));
                    tvsubmitdate.setText(obj.optString("Req_Date"));
                    tvfrmtime.setText(obj.optString("Reason"));
                    tvtotime.setText(obj.optString("NoOfDays"));

                    if (obj.optString("IsApprovalSectionVisible").equalsIgnoreCase("true")) {
                        etreason.setVisibility(View.VISIBLE);
                        btnapprove.setVisibility(View.VISIBLE);
                        btndisapprove.setVisibility(View.VISIBLE);
                    } else {
                        etreason.setVisibility(View.GONE);
                        btnapprove.setVisibility(View.GONE);
                        btndisapprove.setVisibility(View.GONE);
                    }

                    if (!from_cancel){
                        if (obj.optString("IsCancelSectionVisible").equalsIgnoreCase("true")) {
                            btncancle.setVisibility(View.VISIBLE);
                        } else {
                            btncancle.setVisibility(View.GONE);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (Tag == disApproveUrlTag) {
            Toast.makeText(activity, obj.optString("Message"), Toast.LENGTH_SHORT).show();
            activity.onBackPressed();
        }
        else if (Tag == LEAVE_CANCEL_REQUEST_TAG){
            Toast.makeText(activity, obj.optString("Message"), Toast.LENGTH_SHORT).show();
            if (obj.optString("Status").equalsIgnoreCase("true")){
                activity.onBackPressed();
            }

        }else{
            Toast.makeText(activity, obj.optString("Message"), Toast.LENGTH_SHORT).show();
            if (obj.optString("Status").equalsIgnoreCase("true")){
                activity.onBackPressed();
            }
        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        Toast.makeText(activity, "" + error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_approve:

                openWarnAlertForCancelRequest("Approve");
                break;
            case R.id.btn_disapprove:
                if (etreason.getText().toString().trim().length() > 0) {
                    openWarnAlertForCancelRequest("Disapprove");
                } else {
                    Toast.makeText(activity, "Please enter Remark!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancle:
                openWarnAlertForCancelRequest("Cancel");
                break;
        }
    }

    private void openWarnAlertForCancelRequest(final String Status) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // dialog.dismiss();
                        arg0.dismiss();
                        if (Status.equalsIgnoreCase("Cancel")) {
                            if (title.equalsIgnoreCase("leaveReg")) {
                                hitApiForCancelLeaveRequest();
                            } else {
                                hitApiForCancelRequest();
                            }

                        } else if (Status.equalsIgnoreCase("Approve")) {
                            if (title.equalsIgnoreCase("leaveReg")) {
                                hitApiForapproveLeaveRequest("APPROVE");
                            } else {
                                hitApiForapproveRequest();
                            }

                        } else if (Status.equalsIgnoreCase("Disapprove")) {
                            if (title.equalsIgnoreCase("leaveReg")) {
                                hitApiFordisapproveLeaveRequest("DISAPPROVE");
                            } else {
                                hitApiFordisapproveRequest();
                            }

                        }


                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomThemeBottomAndUpAnimation;
        alertDialog.show();
    }

    private void hitApiFordisapproveLeaveRequest(String disapprove) {
        try {
            request = new ProjectWebRequest(activity, getParamForLeavedisApprove(ReqNo, disapprove), disApproveUrl, this, disApproveUrlTag);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParamForLeavedisApprove(String reqNo, String disapprove) {
        MySharedPreference pref = MySharedPreference.getInstance(activity);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqNo", reqNo);
            object.put("UserID", pref.getUid());
            object.put("EmpID", EmpID);
            object.put("RM_HOD_Flag", RM_HOD_Flag);
            object.put("Command", disapprove);
            object.put("Remarks", etreason.getText().toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void hitApiForapproveLeaveRequest(String ApproveOrDisapprove) {
        try {
            request = new ProjectWebRequest(activity, getParamForApproveLeave(ReqNo, ApproveOrDisapprove), disApproveUrl, this, disApproveUrlTag);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParamForApproveLeave(String reqNo, String ApproveOrDisapprove) {
        MySharedPreference pref = MySharedPreference.getInstance(activity);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqNo", reqNo);
            object.put("UserID", pref.getUid());
            object.put("EmpID", EmpID);
            object.put("RM_HOD_Flag", RM_HOD_Flag);
            object.put("Command", ApproveOrDisapprove);
            object.put("Remarks", etreason.getText().toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void hitApiForCancelLeaveRequest() {
        try {
            request = new ProjectWebRequest(activity, getParamForCancelLeaveRequest(ReqNo), cancleUrl, this, cancleUrlTag);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParamForCancelLeaveRequest(String reqNo) {
        MySharedPreference pref = MySharedPreference.getInstance(activity);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", reqNo);
            object.put("UserID", pref.getUid());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    private void hitApiFordisapproveRequest() {
        try {
            request = new ProjectWebRequest(activity, getParamFordisApprove(ReqId), disApproveUrl, this, disApproveUrlTag);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParamFordisApprove(String reqNo) {
        MySharedPreference pref = MySharedPreference.getInstance(activity);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", reqNo);
            object.put("UserID", pref.getUid());
            object.put("Type", Type);
            object.put("Remarks", etreason.getText().toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void hitApiForapproveRequest() {
        try {
            request = new ProjectWebRequest(activity, getParamForApprove(ReqId), approveUrl, this, approveUrlTag);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParamForApprove(String reqNo) {
        MySharedPreference pref = MySharedPreference.getInstance(activity);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", reqNo);
            object.put("UserID", pref.getUid());
            object.put("Type", Type);
            object.put("Remarks", etreason.getText().toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void hitApiForCancelRequest() {
        try {
            request = new ProjectWebRequest(activity, getParamForCancelRequest(ReqId,ReqNo), cancleUrl, this, cancleUrlTag);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParamForCancelRequest(String reqNo,String reqId) {
        MySharedPreference pref = MySharedPreference.getInstance(activity);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqNo", reqId);
            object.put("ReqID", reqNo);
            object.put("UserID", pref.getUid());
            object.put("Remarks", "ere");
            object.put("Type", Type);

        } catch (Exception e) {
        }
        return object;
    }
}
