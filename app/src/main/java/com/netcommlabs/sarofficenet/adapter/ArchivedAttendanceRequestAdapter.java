package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.AttadenceRegulerizeDetailModel;
import com.netcommlabs.sarofficenet.model.AttendancePendingDetailModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONObject;

import java.util.ArrayList;

public class ArchivedAttendanceRequestAdapter extends BaseAdapter implements ResponseListener {
    private Context mContext;
    private ArrayList<AttendancePendingDetailModel> list;
    private int selectedPosition;
    private ProjectWebRequest request;

    public ArchivedAttendanceRequestAdapter(Context mContext, ArrayList<AttendancePendingDetailModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AttendancePendingDetailModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ArchivedAttendanceRequestAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new ArchivedAttendanceRequestAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_archive_request_item, null);
            holder.tv_ar_emp_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_ar_regu_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_ar_period = (TextView) convertView.findViewById(R.id.tv_period);
            convertView.setTag(holder);
        } else {
            holder = (ArchivedAttendanceRequestAdapter.ViewHolder) convertView.getTag();
        }
        final  AttendancePendingDetailModel data=getItem(position);

//        holder.tv_ar_emp_name.setText(list.get(position).getEmpName()+" - "+ list.get(position).getReqNo());
        holder.tv_ar_emp_name.setText(data.getEmpName()+" - "+ data.getReqNo());

        holder.tv_ar_regu_type.setText(data.getRegularisationType());
        holder.tv_ar_period.setText(data.getPeriod());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* hitApiForViewDetail(data.getReqID());
                selectedPosition = position;*/
                Intent intent = new Intent(mContext, FrameActivity.class);
                intent.putExtra("frag_name", "FragmentViewData");
                intent.putExtra("frag_tag", "viewdata");
                intent.putExtra("title", "Pending Request");
                intent.putExtra("reqnum", data.getReqNo());
                intent.putExtra("reqid", data.getReqID());
                intent.putExtra("urltype", "AttReg");
                intent.putExtra("type", data.getType());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }


    class ViewHolder {

        TextView tv_ar_emp_name;
        TextView tv_ar_req_no;
        TextView tv_ar_req_date;
        TextView tv_ar_regu_type;
        TextView tv_ar_period;


    }

    void hitApiForViewDetail(String reqId) {
        try {
            request = new ProjectWebRequest(mContext, getParam(reqId), UrlConstants.VIEW_MY_REGULERIZE_DETAIL, this, UrlConstants.VIEW_MY_REGULERIZE_DETAIL_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            if (Tag == UrlConstants.VIEW_MY_REGULERIZE_DETAIL_TAG) {
                AttadenceRegulerizeDetailModel data = new AttadenceRegulerizeDetailModel(obj.optString("Approver1_Date"),
                        obj.optString("Approver1_Remarks"),
                        obj.optString("CancelDate"),
                        obj.optString("CancelStatus"),
                        obj.optString("ContactNo"),
                        obj.optString("FromDate"),
                        obj.optString("FromTime"),
                        obj.optString("IsApprovalbuttonVisable"),
                        obj.optString("IsCancelButtonVisable"),
                        obj.optString("IsPlaceVisable"),
                        obj.optString("Message"),
                        obj.optString("Place"),
                        obj.optString("RMStatus"),
                        obj.optString("Reason"),
                        obj.optString("RegularisationType"),
                        obj.optString("ReqDate"),
                        obj.optString("ReqNo"),
                        obj.optString("Status"),
                        obj.optString("ToDate"),
                        obj.optString("ToTime"),
                        obj.optString("UserID"),
                        obj.optString("UserName"),
                        obj.optString("reqid"));
                popUpForvehicleList(data);
            } else {
                Toast.makeText(mContext, obj.optString("Message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    JSONObject getParam(String reqId) {
        MySharedPreference pref = MySharedPreference.getInstance(mContext);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserID", pref.getUid());
            object.put("ReqID", reqId);
        } catch (Exception e) {
        }
        return object;
    }

    void popUpForvehicleList(final AttadenceRegulerizeDetailModel data) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup_view_request_detail, null);

        TextView tv_header = (TextView) dialogView.findViewById(R.id.tv_header);
        ImageView close_win = (ImageView) dialogView.findViewById(R.id.close_win);

        TextView tv_name = (TextView) dialogView.findViewById(R.id.tv_name);
        TextView tv_req_no = (TextView) dialogView.findViewById(R.id.tv_req_no);
        /*TextView tv_dept = (TextView) dialogView.findViewById(R.id.tv_dept);
        TextView tv_location = (TextView) dialogView.findViewById(R.id.tv_location);*/
        TextView tv_submit_date = (TextView) dialogView.findViewById(R.id.tv_submit_date);
        TextView tv_date_duration = (TextView) dialogView.findViewById(R.id.tv_date_duration);
        // TextView tv_to_date = (TextView) dialogView.findViewById(R.id.tv_to_date);
        TextView tv_time_duration = (TextView) dialogView.findViewById(R.id.tv_time_duration);
        // TextView tv_to_time = (TextView) dialogView.findViewById(R.id.tv_to_time);
        TextView tv_purpose = (TextView) dialogView.findViewById(R.id.tv_purpose);
        TextView tv_regularization_type = (TextView) dialogView.findViewById(R.id.tv_regularization_type);
        TextView tv_cantact_no = (TextView) dialogView.findViewById(R.id.tv_cantact_no);


        TextView tv_place = (TextView) dialogView.findViewById(R.id.tv_place);
        LinearLayout ll_place = (LinearLayout) dialogView.findViewById(R.id.ll_place);

        LinearLayout ll_approval_header = (LinearLayout) dialogView.findViewById(R.id.approval_header);
        LinearLayout ll_approval_panel = (LinearLayout) dialogView.findViewById(R.id.ll_approval_panel);

        TextView tv_cancel_request = (TextView) dialogView.findViewById(R.id.tv_cancel_request);
        tv_cancel_request.setVisibility(View.GONE);
        tv_header.setText("Request No. " + data.getReqNo());
        tv_name.setText(data.getUserName());
        tv_req_no.setText(data.getReqNo());

        tv_submit_date.setText(data.getReqDate());
        if (data.getToDate().equals("01/01/1900") && data.getToTime().equals(data.getFromTime())) {
            tv_date_duration.setText(data.getFromDate());
            tv_time_duration.setText(data.getFromTime());
        } else {
            tv_date_duration.setText(data.getFromDate() + " - " + data.getToDate());
            tv_time_duration.setText(data.getFromTime() + " - " + data.getToTime());
        }

        if (!data.getPlace().equals("")){
            ll_place.setVisibility(View.VISIBLE);
            tv_place.setText(data.getPlace());
        }else{
            ll_place.setVisibility(View.GONE);
        }
        tv_purpose.setText(data.getReason());
        tv_regularization_type.setText(data.getRegularisationType());
        tv_cantact_no.setText(data.getContactNo());

        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        /****************************************************/

        if (data.getIsApprovalbuttonVisable().equals("false")){
            ll_approval_header.setVisibility(View.VISIBLE);
            View v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.approval_level_layout, null);
            TextView tv_approval_label = (TextView) v.findViewById(R.id.tv_approval_label);
            TextView tv_approval_remark_label = (TextView) v.findViewById(R.id.tv_approval_remark_label);
            TextView tv_approval_status = (TextView) v.findViewById(R.id.tv_approval_status);
            TextView tv_approval_remark = (TextView) v.findViewById(R.id.tv_approval_remark);
            TextView tv_rm_date = (TextView) v.findViewById(R.id.tv_rm_date);

            tv_approval_label.setText("Status");
            tv_approval_remark_label.setText("Remark");
            tv_approval_status.setText(data.getRMStatus());
            tv_approval_remark.setText(data.getApprover1_Remarks());
            tv_rm_date.setText(data.getApprover1_Date());
            ll_approval_panel.addView(v);
        }
        else {
            ll_approval_header.setVisibility(View.GONE);
        }



        /****************************************************/
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomThemeBottomAndUpAnimation;
        alertDialog.show();
        close_win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
    }

}