package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.HolidayModel;

import java.util.ArrayList;

/**
 * Created by Flash_Netcomm on 2/20/2019.
 */

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.HolidayHolder> {
    private ArrayList<HolidayModel> holidayModelArrayList;

    public HolidayAdapter(ArrayList<HolidayModel> holidayModelArrayList, Context context, Activity activity) {
        this.holidayModelArrayList = holidayModelArrayList;
        this.context = context;
        this.activity = activity;
    }

    private Context context;
    private Activity activity;

    @Override
    public HolidayAdapter.HolidayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_row, parent, false);
        return new HolidayAdapter.HolidayHolder(itemView, context, holidayModelArrayList);
    }

    @Override
    public void onBindViewHolder(HolidayAdapter.HolidayHolder holder, int position) {
        holder.tvname.setText(holidayModelArrayList.get(position).getOccation());
        holder.tvday.setText(holidayModelArrayList.get(position).getDayStatus() +", "+holidayModelArrayList.get(position).getHolidayType());
        holder.tvdate.setText(holidayModelArrayList.get(position).getDate() + ", " + holidayModelArrayList.get(position).getDay());
        if (holidayModelArrayList.get(position).getDayStatus().equalsIgnoreCase("Full Day")) {
            holder.imageView.setImageResource(R.drawable.full_day);
        } else if (holidayModelArrayList.get(position).getDayStatus().equalsIgnoreCase("Half Day")) {
            holder.imageView.setImageResource(R.drawable.half_day);
        }
    }

    @Override
    public int getItemCount() {
        return holidayModelArrayList.size();
    }

    public class HolidayHolder extends RecyclerView.ViewHolder {
        TextView tvname, tvdate, tvday;
        ImageView imageView;

        public HolidayHolder(View itemView, Context context, ArrayList<HolidayModel> holidayModelArrayList) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_name);
            tvdate = itemView.findViewById(R.id.tv_date);
            tvday = itemView.findViewById(R.id.tv_day);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }
}
