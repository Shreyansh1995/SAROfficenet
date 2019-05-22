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
import com.netcommlabs.sarofficenet.model.HelpDeskDetailsList;

import java.util.ArrayList;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.TicketHolder> {
    Activity activity;
    Context context;
    private ArrayList<HelpDeskDetailsList> helpDeskDetailsListArrayList;

    public TicketListAdapter(Activity activity, ArrayList<HelpDeskDetailsList> helpDeskDetailsListArrayList, Context context) {
        this.activity = activity;
        this.helpDeskDetailsListArrayList = helpDeskDetailsListArrayList;
        this.context = context;
    }

    @Override
    public TicketListAdapter.TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_desk_row, parent, false);
        return new TicketHolder(itemView, context, helpDeskDetailsListArrayList);
    }

    @Override
    public void onBindViewHolder(TicketListAdapter.TicketHolder holder, final int position) {
        holder.tv_num.setText(helpDeskDetailsListArrayList.get(position).getTicketNo());
        holder.tv_department.setText(helpDeskDetailsListArrayList.get(position).getDepartment());
        holder.tv_category.setText(helpDeskDetailsListArrayList.get(position).getCategory());
        holder.tv_subcategory.setText(helpDeskDetailsListArrayList.get(position).getSubCategory());
        holder.tv_status.setText(helpDeskDetailsListArrayList.get(position).getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(activity, FrameActivity.class);
                intent.putExtra("frag_name", "FragmentHelpDeskDetail");
                intent.putExtra("frag_tag", "helpdesk");
                intent.putExtra("title", "Raised Ticket Details");
                intent.putExtra("ReqId", helpDeskDetailsListArrayList.get(position).getReqID());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return helpDeskDetailsListArrayList.size();
    }

    public class TicketHolder extends RecyclerView.ViewHolder {
        TextView tv_num, tv_department, tv_category, tv_subcategory, tv_status;

        public TicketHolder(View itemView, Context context, ArrayList<HelpDeskDetailsList> helpDeskDetailsListArrayList) {
            super(itemView);
            tv_num = itemView.findViewById(R.id.tv_num);
            tv_department = itemView.findViewById(R.id.tv_department);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_subcategory = itemView.findViewById(R.id.tv_subcategory);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
