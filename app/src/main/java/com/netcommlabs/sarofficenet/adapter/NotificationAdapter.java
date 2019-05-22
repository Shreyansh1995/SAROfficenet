package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.model.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    private ArrayList<NotificationModel> notificationModelArrayList;
    private Context context;
    private Activity activity;
    checkNotification checkNotification;

    public NotificationAdapter(ArrayList<NotificationModel> notificationModelArrayList, Context context, Activity activity, checkNotification checkNotification) {
        this.notificationModelArrayList = notificationModelArrayList;
        this.context = context;
        this.activity = activity;
        this.checkNotification = checkNotification;
    }


    @Override
    public NotificationAdapter.NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        return new NotificationAdapter.NotificationHolder(itemView, context, notificationModelArrayList);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.NotificationHolder holder, final int position) {
        holder.tvtitle.setText(notificationModelArrayList.get(position).getTitle());

        if (notificationModelArrayList.get(position).getIfRead().equalsIgnoreCase("False")) {
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.header_grey_color));
        } else {
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNotification != null) {
                    checkNotification.read(notificationModelArrayList.get(position).getId());
                }

                if (notificationModelArrayList.get(position).getModuleID().equalsIgnoreCase("1")) {
                    Intent intentpending = new Intent(activity, FrameActivity.class);
                    intentpending.putExtra("frag_name", "FragmentLeaveTab");
                    intentpending.putExtra("frag_tag", "leave pending request");
                    intentpending.putExtra("title", "Leave");
                    intentpending.putExtra("tab", "2");
                    activity.startActivity(intentpending);

                } else if (notificationModelArrayList.get(position).getModuleID().equalsIgnoreCase("2")) {
                    Intent intentpending = new Intent(activity, FrameActivity.class);
                    intentpending.putExtra("frag_name", "FragmentShortLeaveTab");
                    intentpending.putExtra("frag_tag", "newrequest");
                    intentpending.putExtra("title", "Short Leave");
                    intentpending.putExtra("tab", "2");
                    activity.startActivity(intentpending);
                } else {
                    Intent intentpending = new Intent(activity, FrameActivity.class);
                    intentpending.putExtra("frag_name", "FragmentAttendanceRegularizationTab");
                    intentpending.putExtra("frag_tag", "newrequest");
                    intentpending.putExtra("title", "Attendance Regularization");
                    intentpending.putExtra("tab", "2");
                    activity.startActivity(intentpending);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (notificationModelArrayList == null)
            return 0;
        return notificationModelArrayList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        TextView tvtitle;

        public NotificationHolder(View itemView, Context context, ArrayList<NotificationModel> notificationModelArrayList) {
            super(itemView);
            tvtitle = itemView.findViewById(R.id.tv_title);
        }
    }

    public interface checkNotification {
        void read(String id);
    }
}
