package com.netcommlabs.sarofficenet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.LeaveTypeModel;

import java.util.ArrayList;

public class LeaveTypeListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<LeaveTypeModel> list;

    public LeaveTypeListAdapter(Context mContext, ArrayList<LeaveTypeModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LeaveTypeModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeaveTypeListAdapter.ViewHolder holder = null;
        LeaveTypeModel data = getItem(position);
        if (convertView == null) {
            holder = new LeaveTypeListAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_year_item, null);
            holder.spnr_year_item = (TextView) convertView.findViewById(R.id.spnr_year_item);
            convertView.setTag(holder);
        } else {
            holder = (LeaveTypeListAdapter.ViewHolder) convertView.getTag();
        }
        holder.spnr_year_item.setText(data.getLeaveType());
        return convertView;
    }


    class ViewHolder {
        TextView spnr_year_item;
    }
}