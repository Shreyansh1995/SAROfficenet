package com.netcommlabs.sarofficenet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.CompOffModel;

import java.util.ArrayList;

public class CompOffAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CompOffModel> list;

    public CompOffAdapter(Context mContext, ArrayList<CompOffModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CompOffModel getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final CompOffModel data = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comp_off_list_row, null);
            holder.comp_off_date = (TextView) convertView.findViewById(R.id.comp_off_date);
            holder.tv_req_date = (TextView) convertView.findViewById(R.id.tv_req_date);
            holder.tv_days_earned = (TextView) convertView.findViewById(R.id.tv_days_earned);
            holder.tv_availed_days = (TextView) convertView.findViewById(R.id.tv_availed_days);
            holder.tv_balnce_days = (TextView) convertView.findViewById(R.id.tv_balnce_days);
            holder.cb_id = (CheckBox) convertView.findViewById(R.id.check_box);

            holder.cb_id.setChecked(data.isChecked());
            holder.cb_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    data.setChecked(b);
//                    data.getCompOffID();
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.comp_off_date.setText(data.getCompOffDate());
        holder.tv_req_date.setText(data.getReqDate());
        holder.tv_days_earned.setText(data.getNoOfDays());
        holder.tv_availed_days.setText(data.getAvailedDays());
        holder.tv_balnce_days.setText(data.getBalance());
        return convertView;
    }


    class ViewHolder {
        TextView comp_off_date;
        TextView tv_req_date;
        TextView tv_days_earned;
        TextView tv_availed_days;
        TextView tv_balnce_days;
        CheckBox cb_id;
    }}