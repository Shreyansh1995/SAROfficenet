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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.NewJoineeModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Flash_Netcomm on 2/20/2019.
 */

public class NewJoineeAdapter extends RecyclerView.Adapter<NewJoineeAdapter.JoineeHolder> {
    public NewJoineeAdapter(ArrayList<NewJoineeModel> newJoineeModelArrayList, Context context, Activity activity) {
        this.newJoineeModelArrayList = newJoineeModelArrayList;
        this.context = context;
        this.activity = activity;
    }

    private ArrayList<NewJoineeModel> newJoineeModelArrayList;
    private Context context;
    private Activity activity;
    private static int currentPosition = -1;

    @Override
    public NewJoineeAdapter.JoineeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_joinee_row, parent, false);
        return new NewJoineeAdapter.JoineeHolder(itemView, context, newJoineeModelArrayList);
    }

    @Override
    public void onBindViewHolder(NewJoineeAdapter.JoineeHolder holder, final int position) {
        NewJoineeModel newJoineeModel = newJoineeModelArrayList.get(position);
        holder.tvEmpName.setText(newJoineeModelArrayList.get(position).getEmpName());
        holder.tvEmpDesignation.setText(newJoineeModelArrayList.get(position).getEmpDesignation());
        holder.tvEmpDOB.setText(newJoineeModelArrayList.get(position).getEmpDOB());
        holder.tvEmpDOJ.setText(newJoineeModelArrayList.get(position).getEmpDOJ());
        holder.tvEmpDepartment.setText(newJoineeModelArrayList.get(position).getEmpDepartment());
        holder.tvEmpDesignation.setText(newJoineeModelArrayList.get(position).getEmpDesignation());
        holder.tvEmpLocation.setText(newJoineeModelArrayList.get(position).getEmpLocation());
        Picasso.with(activity).load(newJoineeModel.getEmpImage()).into(holder.imageView);

        holder.imageView.setVisibility(View.VISIBLE);

        if (newJoineeModelArrayList.get(position).getType().equalsIgnoreCase("New Joinee")){
            holder.tvdob.setVisibility(View.GONE);
            holder.tvEmpDOB.setVisibility(View.GONE);
            holder.tvdoj.setVisibility(View.VISIBLE);
            holder.tvEmpDOJ.setVisibility(View.VISIBLE);
        }else {
            holder.tvdoj.setVisibility(View.GONE);
            holder.tvEmpDOB.setVisibility(View.GONE);
            holder.tvdob.setVisibility(View.VISIBLE);
            holder.tvEmpDOB.setVisibility(View.VISIBLE);
        }


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
        return newJoineeModelArrayList.size();
    }

    public class JoineeHolder extends RecyclerView.ViewHolder {
        TextView tvEmpDOB, tvEmpDOJ, tvEmpDepartment, tvEmpDesignation, tvEmpFirstName, tvEmpID, tvEmpLocation, tvEmpName;
        ImageView imageView,ivarrow;
        RelativeLayout relativeLayout;
        View view;
        TextView tvdob,tvdoj;

        public JoineeHolder(View itemView, Context context, ArrayList<NewJoineeModel> newJoineeModelArrayList) {
            super(itemView);
            tvEmpName = itemView.findViewById(R.id.tv_name);
            tvEmpDesignation = itemView.findViewById(R.id.tv_designation);
            tvEmpDOB = itemView.findViewById(R.id.tv_dob);
            tvEmpDOJ = itemView.findViewById(R.id.tv_doj);
            tvEmpDepartment = itemView.findViewById(R.id.tv_dprt);
            tvEmpLocation = itemView.findViewById(R.id.tv_location);
            imageView = itemView.findViewById(R.id.profile_image);
            relativeLayout = itemView.findViewById(R.id.rl_details);
            view = itemView.findViewById(R.id.view);
            ivarrow = itemView.findViewById(R.id.iv_arrow);
            tvdob = itemView.findViewById(R.id.tvdob);
            tvdoj = itemView.findViewById(R.id.tvdoj);
        }
    }
}
