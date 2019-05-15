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
import com.netcommlabs.sarofficenet.model.PendingRequestsModel;

import java.util.ArrayList;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.RequestHolder> {
    private ArrayList<PendingRequestsModel> pendingRequestsModelArrayList;
    private Context context;
    private Activity activity;

    public PendingRequestAdapter(ArrayList<PendingRequestsModel> pendingRequestsModelArrayList, Context context, Activity activity) {
        this.pendingRequestsModelArrayList = pendingRequestsModelArrayList;
        this.context = context;
        this.activity = activity;
    }


    @Override
    public PendingRequestAdapter.RequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_request_row, parent, false);
        return new PendingRequestAdapter.RequestHolder(itemView, context, pendingRequestsModelArrayList);
    }

    @Override
    public void onBindViewHolder(PendingRequestAdapter.RequestHolder holder, final int position) {
        holder.tvName.setText(pendingRequestsModelArrayList.get(position).getModuleName());
        holder.tvCount.setText(pendingRequestsModelArrayList.get(position).getPendingCount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pendingRequestsModelArrayList.get(position).getModuleID().equalsIgnoreCase("1")) {
                    if (!pendingRequestsModelArrayList.get(position).getPendingCount().equalsIgnoreCase("0")) {
                        Intent intentpending = new Intent(activity, FrameActivity.class);
                        intentpending.putExtra("frag_name", "FragmentLeaveTab");
                        intentpending.putExtra("frag_tag", "leave pending request");
                        intentpending.putExtra("title", "Leave");
                        intentpending.putExtra("tab", "2");
                        activity.startActivity(intentpending);
                    }


                } else if (pendingRequestsModelArrayList.get(position).getModuleID().equalsIgnoreCase("2")) {
                    if (!pendingRequestsModelArrayList.get(position).getPendingCount().equalsIgnoreCase("0")) {
                        Intent intentpending = new Intent(activity, FrameActivity.class);
                        intentpending.putExtra("frag_name", "FragmentAttendanceRegularizationTab");
                        intentpending.putExtra("frag_tag", "newrequest");
                        intentpending.putExtra("title", "Attendance Regularization");
                        intentpending.putExtra("tab", "2");
                        activity.startActivity(intentpending);
                    }
                } else if (pendingRequestsModelArrayList.get(position).getModuleID().equalsIgnoreCase("3")) {
                    if (!pendingRequestsModelArrayList.get(position).getPendingCount().equalsIgnoreCase("0")) {
                        Intent intentpending = new Intent(activity, FrameActivity.class);
                        intentpending.putExtra("frag_name", "FragmentShortLeaveTab");
                        intentpending.putExtra("frag_tag", "newrequest");
                        intentpending.putExtra("title", "Short Leave");
                        intentpending.putExtra("tab", "2");
                        activity.startActivity(intentpending);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (pendingRequestsModelArrayList == null)
            return 0;
        return pendingRequestsModelArrayList.size();
    }

    public class RequestHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCount;

        public RequestHolder(View itemView, Context context, ArrayList<PendingRequestsModel> pendingRequestsModelArrayList) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCount = itemView.findViewById(R.id.tv_count);
        }
    }
}
