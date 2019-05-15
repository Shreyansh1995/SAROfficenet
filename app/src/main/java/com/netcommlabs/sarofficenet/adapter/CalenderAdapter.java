package com.netcommlabs.sarofficenet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.AttendanceModel;

import java.util.List;


public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.ViewHolder> {
private LayoutInflater mInflater;
private String[] days = {"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"};
//private int count = 0;
private Context context;
private int day;
private int num_of_days;
private int incCount = 0;
private List<AttendanceModel> attendanceModelsList;

//boolean isDateSet = false;

public CalenderAdapter(Context context, int day, int num_of_days, List<AttendanceModel> attendanceModelsList) {

        Log.e("^^^^^^^", new Gson().toJson(attendanceModelsList));
        this.mInflater = LayoutInflater.from(context);
        this.day = day;
        this.num_of_days = num_of_days;
        this.attendanceModelsList = attendanceModelsList;
        this.context = context;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.calender_item, parent, false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(ViewHolder holder, final int position) {
final int date_to_set = (position - (day - 1)) + 1;
        if ((position - (day - 1)) < num_of_days + (day - 1)) {
        if (position < (day - 1)) {
        holder.date_textview.setText("");
        } else {
        if (position == 0 && day != 1)
        holder.date_textview.setText("");
        else {
                       /* if (count < attendanceModelsList.size()) {

                            String splitStatus[] = attendanceModelsList.get(position - (day + 6)).getMyStatus().split("\\|");
                            *//*String date[] = attendanceModelsList.get(position - (day + 6)).getDate().split("\\/");
                            if (count == Integer.parseInt(date[1])) {*//*
                            holder.cal_date_rlayout.setBackgroundColor(Color.parseColor(splitStatus[1]));
                            holder.att_status_txtview.setText(splitStatus[0]);

//                            }
                        }*/

        if (date_to_set <= num_of_days){
        if (date_to_set <= 9) {
        holder.date_textview.setText("0" + date_to_set);
        } else {
        holder.date_textview.setText("" + date_to_set);
        }
        }

        //count = count + 1;

        if (incCount <= attendanceModelsList.size() - 1) {
        String date[] = attendanceModelsList.get(incCount).getDateOffice().split("\\/");
        //int current_date_pos = (position - ((day - 1) + 7)) + 1;
        if (date_to_set == Integer.parseInt(date[1])) {
        //String splitStatus[] = attendanceModelsList.get(incCount).getMyStatus().split("\\|");

        String color = "#808080";
        if (attendanceModelsList.get(incCount).getColor().matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")){
        color = attendanceModelsList.get(incCount).getColor();
        }

        //    String color = attendanceModelsList.get(incCount).getColor();
        String status = attendanceModelsList.get(incCount).getStatus();

                                /*String intime = "IN: " + splitDateTime(attendanceModelsList.get(incCount).getInTime());
                                String outtime = "OUT: " + splitDateTime(attendanceModelsList.get(incCount).getOutTime());*/
        holder.cal_date_rlayout.setBackgroundColor(Color.parseColor(color));
        holder.att_status_txtview.setText(status);
                                /*holder.att_intime_txtview.setText(splitDateTime(attendanceModelsList.get(incCount).getInTime()));
                                holder.att_outtime_txtview.setText(splitDateTime(attendanceModelsList.get(incCount).getOutTime()));*/

        incCount++;





        }
        }
        }
        }
        }

      /*  holder.cal_date_rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+attendanceModelsList.get(date_to_set).getLeaveStatus(), Toast.LENGTH_SHORT).show();
            }
        });*/
        }

@Override
public int getItemCount() {
        return num_of_days + (day - 1);
        }

    /*@Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int date_to_set = (position - ((day - 1) + 7)) + 1;
        if (position < 7) {
            //holder.att_status_txtview.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
            holder.att_status_txtview.setBackgroundColor(Color.TRANSPARENT);
            holder.cal_date_rlayout.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
            holder.att_status_txtview.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            holder.att_status_txtview.setText(days[position]);
        } else {
            if ((position - ((day - 1) + 7)) < num_of_days + (day - 1)) {
                if (position < ((day - 1) + 7) && position > 7) {
                    holder.date_textview.setText("");
                } else {
                    if (position == 7 && day != 1)
                        holder.date_textview.setText("");
                    else {
                       *//* if (count < attendanceModelsList.size()) {

                            String splitStatus[] = attendanceModelsList.get(position - (day + 6)).getMyStatus().split("\\|");
                            *//**//*String date[] = attendanceModelsList.get(position - (day + 6)).getDate().split("\\/");
                            if (count == Integer.parseInt(date[1])) {*//**//*
                            holder.cal_date_rlayout.setBackgroundColor(Color.parseColor(splitStatus[1]));
                            holder.att_status_txtview.setText(splitStatus[0]);

//                            }
                        }*//*

                        if (date_to_set <= num_of_days){
                            if (date_to_set <= 9) {
                                holder.date_textview.setText("0" + date_to_set);
                            } else {
                                holder.date_textview.setText("" + date_to_set);
                            }
                        }

                        //count = count + 1;

                        if (incCount <= attendanceModelsList.size() - 1) {
                            String date[] = attendanceModelsList.get(incCount).getDateOffice().split("\\/");
                            //int current_date_pos = (position - ((day - 1) + 7)) + 1;
                            if (date_to_set == Integer.parseInt(date[1])) {
                                //String splitStatus[] = attendanceModelsList.get(incCount).getMyStatus().split("\\|");
                                String color = attendanceModelsList.get(incCount).getColor();
                                String status = attendanceModelsList.get(incCount).getStatus();

                                *//*String intime = "IN: " + splitDateTime(attendanceModelsList.get(incCount).getInTime());
                                String outtime = "OUT: " + splitDateTime(attendanceModelsList.get(incCount).getOutTime());*//*
                                holder.cal_date_rlayout.setBackgroundColor(Color.parseColor(color));
                                holder.att_status_txtview.setText(status);
                                *//*holder.att_intime_txtview.setText(splitDateTime(attendanceModelsList.get(incCount).getInTime()));
                                holder.att_outtime_txtview.setText(splitDateTime(attendanceModelsList.get(incCount).getOutTime()));*//*

                                incCount++;

                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return num_of_days + 7 + (day - 1);
    }*/

@Override
public int getItemViewType(int position) {
        return position;
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView date_textview;
    public TextView att_status_txtview;
    /*public TextView att_intime_txtview;
    public TextView att_outtime_txtview;*/
    public RelativeLayout cal_date_rlayout;

    public ViewHolder(View itemView) {
        super(itemView);
        date_textview = (TextView) itemView.findViewById(R.id.date_textview);
        att_status_txtview = (TextView) itemView.findViewById(R.id.att_status_txtview);
            /*att_intime_txtview = (TextView) itemView.findViewById(R.id.att_intime_txtview);
            att_outtime_txtview = (TextView) itemView.findViewById(R.id.att_outtime_txtview);*/
        cal_date_rlayout = (RelativeLayout) itemView.findViewById(R.id.cal_date_rlayout);

    }
}

    private String splitDateTime(String dateString){
        String[] parts = dateString.split(" ");
        if (parts.length > 0){
            String time = dateString.replace(parts[0] + " ", "");
            String[] time_split = time.split(":");
            if (time_split.length >= 2){
                String get_second = time_split[2].split(" ")[0];
                return time.replace(":" + get_second, "");
            }
        }
        return "NA";
    }

}
