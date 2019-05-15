package com.netcommlabs.sarofficenet.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.model.AttendancePendingDetailModel;
import com.netcommlabs.sarofficenet.model.ShortLeavePendingModel;

import java.util.ArrayList;

public class ShortLeavePendingAdapter  extends BaseAdapter {
    private Context mContext;
    private ArrayList<ShortLeavePendingModel> list;

    public ShortLeavePendingAdapter(Context mContext, ArrayList<ShortLeavePendingModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ShortLeavePendingModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ShortLeavePendingAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new ShortLeavePendingAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.attendance_panding_request_item, null);
            holder.tv_emp_name_and_type = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_emp_req_code = (TextView) convertView.findViewById(R.id.tv_rd_req_code);
            holder.tv_emp_req_period = (TextView) convertView.findViewById(R.id.tv_period);
            holder.tv_emp_req_period = (TextView) convertView.findViewById(R.id.tv_period);
            holder.tvtype = (TextView) convertView.findViewById(R.id.tv_type);
            //holder.tv_emp_req_resion = (TextView) convertView.findViewById(R.id.tv_emp_req_resion);
            convertView.setTag(holder);
        } else {
            holder = (ShortLeavePendingAdapter.ViewHolder) convertView.getTag();
        }
        final ShortLeavePendingModel data = getItem(position);
        holder.tv_emp_name_and_type.setText(data.getEmpName() +" - "+data.getReqNo());
        holder.tv_emp_req_period.setText(data.getPeriod());
        holder.tvtype.setText(data.getRMstatus());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  hitApiForViewDetail(data.getReqID());
               // selectedPosition = position;

                Intent intent = new Intent(mContext, FrameActivity.class);
                intent.putExtra("frag_name", "FragmentViewData");
                intent.putExtra("frag_tag", "viewdata");
                intent.putExtra("title", "Pending Request");
                intent.putExtra("urltype", "short");
                intent.putExtra("reqnum", data.getReqNo());
                intent.putExtra("reqid", data.getReqID());
                intent.putExtra("type", data.getType());
                mContext.startActivity(intent);
            }
        });
        return convertView;

    }
    class ViewHolder {
        TextView tv_emp_name_and_type;
        TextView tv_emp_req_code;
        TextView tv_emp_req_period,tvtype;
        //TextView tv_emp_req_resion;
    }
}
