package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.netcommlabs.sarofficenet.model.AttendanceRegularizationListModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONObject;

import java.util.ArrayList;

public class AttendanceRegularizationListAdapter extends BaseAdapter  {
    private Context mContext;
    private ArrayList<AttendanceRegularizationListModel> list;
    private ProjectWebRequest request;

    public AttendanceRegularizationListAdapter(Context mContext, ArrayList<AttendanceRegularizationListModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AttendanceRegularizationListModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AttendanceRegularizationListAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new AttendanceRegularizationListAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.attendance_regularization_list_items, null);
            holder.tv_rd_req_code = (TextView) convertView.findViewById(R.id.tv_rd_req_code);
            holder.tv_submit_rd_view = (TextView) convertView.findViewById(R.id.tv_submit_rd_view);
            holder.tvperiod = (TextView) convertView.findViewById(R.id.tv_period);
            holder.tvtype = (TextView) convertView.findViewById(R.id.tv_type);
            holder.ivindicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(holder);
        } else {
            holder = (AttendanceRegularizationListAdapter.ViewHolder) convertView.getTag();
        }
        final AttendanceRegularizationListModel data = getItem(position);
        holder.tv_rd_req_code.setText(data.getReqNo());
        holder.tvperiod.setText(data.getPeriod());
        holder.tvtype.setText(data.getRegularisationType());


        if (data.getRMStatus().contains("Approved")) {
            holder.ivindicator.setColorFilter(mContext.getResources().getColor(R.color.approve), PorterDuff.Mode.SRC_IN);
        } else if (data.getRMStatus().contains("Disapproved")) {
            holder.ivindicator.setColorFilter(mContext.getResources().getColor(R.color.disapprove), PorterDuff.Mode.SRC_IN);
        } else if (data.getRMStatus().contains("Pending")) {
            holder.ivindicator.setColorFilter(mContext.getResources().getColor(R.color.pending), PorterDuff.Mode.SRC_IN);
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FrameActivity.class);
                intent.putExtra("frag_name", "FragmentViewData");
                intent.putExtra("frag_tag", "viewdata");
                intent.putExtra("title", "Request Detail");
                intent.putExtra("urltype", "AttReg");
                intent.putExtra("reqnum", data.getReqNo());
                intent.putExtra("reqid", data.getReqID());
                intent.putExtra("type", data.getType());
                mContext.startActivity(intent);

            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_rd_req_code, tvperiod, tvtype;
        TextView tv_submit_rd_view;
        ImageView ivindicator;
    }
}