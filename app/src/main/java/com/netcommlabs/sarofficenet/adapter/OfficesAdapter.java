package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.OfficesModel;

import java.util.ArrayList;

public class OfficesAdapter extends RecyclerView.Adapter<OfficesAdapter.OfficesHolder> {

    public OfficesAdapter(ArrayList<OfficesModel> officesModelArrayList, Context context, Activity activity) {
        this.officesModelArrayList = officesModelArrayList;
        this.context = context;
        this.activity = activity;
    }

    private ArrayList<OfficesModel> officesModelArrayList;
    private Context context;
    private Activity activity;
    private static int currentPosition = -1;

    @Override
    public OfficesAdapter.OfficesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offices_row, parent, false);
        return new OfficesAdapter.OfficesHolder(itemView, context, officesModelArrayList);
    }

    @Override
    public void onBindViewHolder(OfficesAdapter.OfficesHolder holder, final int position) {
        holder.tvEmpName.setText(officesModelArrayList.get(position).getCity() + "," + officesModelArrayList.get(position).getState());
        holder.tvEmpDesignation.setText(officesModelArrayList.get(position).getContactAddress());
        holder.tvEmpDOB.setText(officesModelArrayList.get(position).getContactPerson());
        holder.tvEmpDOJ.setText(officesModelArrayList.get(position).getEmailID());
        holder.tvEmpDepartment.setText(officesModelArrayList.get(position).getOfficeCategory());
        holder.tvEmpLocation.setText(officesModelArrayList.get(position).getCountry());

        holder.relativeLayout.setVisibility(View.GONE);
        holder.ivarrow.setImageResource(R.drawable.arrowright);
        holder.view.setVisibility(View.VISIBLE);

        if (currentPosition == position) {

            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.relativeLayout.startAnimation(slideDown);
            holder.view.setVisibility(View.GONE);
            holder.ivarrow.setImageResource(R.drawable.downarrow);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (officesModelArrayList == null)
            return 0;
        return officesModelArrayList.size();
    }

    public class OfficesHolder extends RecyclerView.ViewHolder {
        TextView tvEmpDOB, tvEmpDOJ, tvEmpDepartment, tvEmpDesignation, tvEmpFirstName, tvEmpID, tvEmpLocation, tvEmpName;
        ImageView  ivarrow;
        RelativeLayout relativeLayout;
        View view;
        TextView tvdob, tvdoj;

        public OfficesHolder(View itemView, Context context, ArrayList<OfficesModel> officesModelArrayList) {
            super(itemView);
            tvEmpName = itemView.findViewById(R.id.tv_name);
            tvEmpDesignation = itemView.findViewById(R.id.tv_designation);
            tvEmpDOB = itemView.findViewById(R.id.tv_dob);
            tvEmpDOJ = itemView.findViewById(R.id.tv_doj);
            tvEmpDepartment = itemView.findViewById(R.id.tv_dprt);
            tvEmpLocation = itemView.findViewById(R.id.tv_location);
         //   imageView = itemView.findViewById(R.id.profile_image);
            relativeLayout = itemView.findViewById(R.id.rl_details);
            view = itemView.findViewById(R.id.view);
            ivarrow = itemView.findViewById(R.id.iv_arrow);
            tvdob = itemView.findViewById(R.id.tvdob);
            tvdoj = itemView.findViewById(R.id.tvdoj);
        }
    }
}
