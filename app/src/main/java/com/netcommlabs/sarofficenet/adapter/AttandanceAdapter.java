package com.netcommlabs.sarofficenet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.AttendanceModel;

import java.util.ArrayList;

/**
 * Created by Netcomm on 1/31/2017.
 */
public class AttandanceAdapter extends RecyclerView.Adapter<AttandanceAdapter.AttViewHolder> {
    private Context mContext;
    private ArrayList<AttendanceModel> data;

    public AttandanceAdapter(Context mContext, ArrayList<AttendanceModel> data) {
        this.mContext = mContext;
        this.data = data;
        Log.e("@@@@@@@@*****", "" + data.size());
    }



    private String splitDateTime(String dateString) {

        String[] parts = dateString.split(" ");
        if (parts.length > 0) {
            String time = dateString.replace(parts[0] + " ", "");
            String[] time_split = time.split(":");
            if (time_split.length >= 2) {
                String get_second = time_split[2].split(" ")[0];
                return time.replace(":" + get_second, "");

            }


        }
        return "NA";
    }

    private String getDate(String dateStr) {
        String date;
        String[] parts = dateStr.split("/");
        date = parts[1];
        return date;
    }

    @Override
    public AttViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attandance_detail_item, parent, false);
        return new AttViewHolder(itemView, mContext, data);
    }

    @Override
    public void onBindViewHolder(AttViewHolder holder, int position) {
        String status = data.get(position).getStatus();

        holder.tv_date.setText(data.get(position).getDateOffice2());
        holder.tv_intime.setText(data.get(position).getInTime());
        holder.tv_outtime.setText(data.get(position).getOutTime());
        holder.tv_status.setText(status);

      /*  String color = "#808080";
        if (data.get(position).getColor().matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")){
            color = data.get(position).getColor();
        }
        holder.tv_date.setText(data.get(position).getDateOffice2());
        if(data.get(position).getInTime().matches(""))
        {
            holder.tv_intime.setText("NA");
        }else {
            holder.tv_intime.setText(splitDateTime(data.get(position).getInTime()));
        }
        if(data.get(position).getOutTime().matches(""))
        {
            holder.tv_outtime.setText("NA");
        }else {
            holder.tv_outtime.setText(splitDateTime(data.get(position).getOutTime()));
        }
        holder.tv_status.setText(status);

        if (color.equals("#FFFFFF")){
            holder.tv_date.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
            holder.tv_intime.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
            holder.tv_outtime.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
            holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
        }
        else {
            holder.tv_date.setTextColor(Color.parseColor(color));
            holder.tv_intime.setTextColor(Color.parseColor(color));
            holder.tv_outtime.setTextColor(Color.parseColor(color));
            holder.tv_status.setTextColor(Color.parseColor(color));
        }*/
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    public class AttViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date;
        TextView tv_intime;
        TextView tv_outtime;
        TextView tv_status;
        public AttViewHolder(View convertView, Context mContext, ArrayList<AttendanceModel> data) {
            super(convertView);

            tv_date =  (TextView) convertView.findViewById(R.id.txt_date);
            tv_intime = (TextView) convertView.findViewById(R.id.tv_intime);
            tv_outtime = (TextView) convertView.findViewById(R.id.tv_outtime);
            tv_status = (TextView) convertView.findViewById(R.id.tv_status);
        }
    }
}