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

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.model.ShortLeaveModel;

import java.util.ArrayList;

public class ShortLeaveDetailAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ShortLeaveModel> list;

    public ShortLeaveDetailAdapter(Context mContext, ArrayList<ShortLeaveModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ShortLeaveModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ShortLeaveDetailAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new ShortLeaveDetailAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.attendance_regularization_list_items, null);
            holder.tv_rd_req_code = (TextView) convertView.findViewById(R.id.tv_rd_req_code);
            holder.tv_submit_rd_view = (TextView) convertView.findViewById(R.id.tv_submit_rd_view);
            holder.tvperiod = (TextView) convertView.findViewById(R.id.tv_period);
            holder.tvtype = (TextView) convertView.findViewById(R.id.tv_type);
            holder.ivindicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(holder);
        } else {
            holder = (ShortLeaveDetailAdapter.ViewHolder) convertView.getTag();
        }
        final ShortLeaveModel data = getItem(position);
        holder.tv_rd_req_code.setText(data.getReqNo());
        holder.tvperiod.setText(data.getPeriod());
        holder.tvtype.setText(data.getRMstatus());

        if (data.getRMstatus().contains("Approved")) {
            holder.ivindicator.setColorFilter(mContext.getResources().getColor(R.color.approve), PorterDuff.Mode.SRC_IN);
        } else if (data.getRMstatus().contains("Disapproved")) {
            holder.ivindicator.setColorFilter(mContext.getResources().getColor(R.color.disapprove), PorterDuff.Mode.SRC_IN);
        } else if (data.getRMstatus().contains("Pending")) {
            holder.ivindicator.setColorFilter(mContext.getResources().getColor(R.color.pending), PorterDuff.Mode.SRC_IN);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FrameActivity.class);
                intent.putExtra("frag_name", "FragmentViewData");
                intent.putExtra("frag_tag", "viewdata");
                intent.putExtra("title", "Short Leave Detail");
                intent.putExtra("urltype", "short");
                intent.putExtra("reqnum", data.getReqNo());
                intent.putExtra("reqid", data.getReqID());
                intent.putExtra("type",  data.getType());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView tv_rd_req_code, tvperiod, tvtype;
        TextView tv_submit_rd_view;
        ImageView ivindicator;
    }
}
