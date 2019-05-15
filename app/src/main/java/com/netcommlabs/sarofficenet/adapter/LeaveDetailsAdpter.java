package com.netcommlabs.sarofficenet.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.model.LeaveListModel;

import java.util.ArrayList;

public class LeaveDetailsAdpter extends BaseAdapter {
    private Context mContext;
    private ArrayList<LeaveListModel> list;

    public LeaveDetailsAdpter(Context mContext, ArrayList<LeaveListModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LeaveListModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LeaveDetailsAdpter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new LeaveDetailsAdpter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.attendance_regularization_list_items, null);
            holder.tv_rd_req_code = (TextView) convertView.findViewById(R.id.tv_rd_req_code);
            holder.tv_submit_rd_view = (TextView) convertView.findViewById(R.id.tv_submit_rd_view);
            holder.tvperiod = (TextView) convertView.findViewById(R.id.tv_period);
            holder.tvtype = (TextView) convertView.findViewById(R.id.tv_type);
            holder.ivindicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(holder);
        } else {
            holder = (LeaveDetailsAdpter.ViewHolder) convertView.getTag();
        }
        final LeaveListModel data = getItem(position);

      //  Toast.makeText(mContext, ""+data.getReqNo()+data.getPeriod()+data.getRm_Status(), Toast.LENGTH_SHORT).show();
        holder.tv_rd_req_code.setText(data.getReqNo());
        holder.tvperiod.setText(data.getPeriod());
        holder.tvtype.setText(data.getRm_Status());
        if (data.getRm_Status().contains("Approved")) {
            holder.ivindicator.setColorFilter(mContext.getResources().getColor(R.color.approve), PorterDuff.Mode.SRC_IN);
        } else if (data.getRm_Status().contains("Disapproved")) {
            holder.ivindicator.setColorFilter(mContext.getResources().getColor(R.color.disapprove), PorterDuff.Mode.SRC_IN);
        } else if (data.getRm_Status().contains("Pending")) {
            holder.ivindicator.setColorFilter(mContext.getResources().getColor(R.color.pending), PorterDuff.Mode.SRC_IN);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FrameActivity.class);
                intent.putExtra("frag_name", "FragmentViewData");
                intent.putExtra("frag_tag", "viewdata");
                intent.putExtra("title", "Request Detail");
                intent.putExtra("urltype", "leaveReg");
                intent.putExtra("reqnum", data.getReqNo());
                intent.putExtra("type", data.getMode());
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
