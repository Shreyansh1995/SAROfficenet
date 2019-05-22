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
import com.netcommlabs.sarofficenet.model.HelpDeskAdminListModel;
import java.util.ArrayList;

public class PendingTicketListAdapter extends RecyclerView.Adapter<PendingTicketListAdapter.ListHolder> {
    Activity activity;
    Context context;
    private ArrayList<HelpDeskAdminListModel> helpDeskAdminListModelArrayList;

    public PendingTicketListAdapter(Activity activity, Context context, ArrayList<HelpDeskAdminListModel> helpDeskAdminListModelArrayList) {
        this.activity = activity;
        this.context = context;
        this.helpDeskAdminListModelArrayList = helpDeskAdminListModelArrayList;
    }

    @Override
    public PendingTicketListAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_desk_row, parent, false);
        return new ListHolder(itemView, context, helpDeskAdminListModelArrayList);
    }

    @Override
    public void onBindViewHolder(PendingTicketListAdapter.ListHolder holder, final int position) {
        holder.tv_num.setText(helpDeskAdminListModelArrayList.get(position).getTicketNo());
        holder.tv_department.setText(helpDeskAdminListModelArrayList.get(position).getSubmittedBy());
        holder.tv_category.setText(helpDeskAdminListModelArrayList.get(position).getCategory());
        holder.tv_subcategory.setText(helpDeskAdminListModelArrayList.get(position).getSubCategory());
        holder.tv_status.setText(helpDeskAdminListModelArrayList.get(position).getStatus());
        holder.tv_dept.setText("Submitted By");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(activity, FrameActivity.class);
                intent.putExtra("frag_name", "FragmentAdminHelpDeskDetail");
                intent.putExtra("frag_tag", "helpdesk");
                intent.putExtra("title", "Raised Ticket Solution");
                intent.putExtra("ReqId", helpDeskAdminListModelArrayList.get(position).getReqID());
                intent.putExtra("Mobile", helpDeskAdminListModelArrayList.get(position).getMobileNo());
                intent.putExtra("Ticket", helpDeskAdminListModelArrayList.get(position).getTicketNo());
                intent.putExtra("Category", helpDeskAdminListModelArrayList.get(position).getCategory());
                intent.putExtra("SubCategory", helpDeskAdminListModelArrayList.get(position).getSubCategory());
                intent.putExtra("Submitdate", helpDeskAdminListModelArrayList.get(position).getSubmitDate());
                intent.putExtra("Submitby", helpDeskAdminListModelArrayList.get(position).getSubmittedBy());
                intent.putExtra("issue", helpDeskAdminListModelArrayList.get(position).getQuery());
                activity.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return helpDeskAdminListModelArrayList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        TextView tv_num, tv_department, tv_category, tv_subcategory, tv_status,tv_dept;
        public ListHolder(View itemView, Context context, ArrayList<HelpDeskAdminListModel> helpDeskAdminListModelArrayList) {
            super(itemView);
            tv_num = itemView.findViewById(R.id.tv_num);
            tv_department = itemView.findViewById(R.id.tv_department);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_subcategory = itemView.findViewById(R.id.tv_subcategory);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_dept = itemView.findViewById(R.id.tv_dept);
        }
    }
}
