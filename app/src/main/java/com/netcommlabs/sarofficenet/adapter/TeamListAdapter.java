package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.model.TeamListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Flash_Netcomm on 2/18/2019.
 */

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.ListHolder> {
    private ArrayList<TeamListModel> teamListModelArrayList;
    private Context context;
    private Activity activity;

    public TeamListAdapter(ArrayList<TeamListModel> teamListModelArrayList, Context context, Activity activity) {
        this.teamListModelArrayList = teamListModelArrayList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public TeamListAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_list_row, parent, false);
        return new TeamListAdapter.ListHolder(itemView, context, teamListModelArrayList);
    }

    @Override
    public void onBindViewHolder(TeamListAdapter.ListHolder holder, final int position) {
        TeamListModel teamListModel = teamListModelArrayList.get(position);
        holder.tvname.setText(teamListModelArrayList.get(position).getName());
        holder.tvdesignation.setText(teamListModelArrayList.get(position).getDesignation());
        Picasso.with(activity).load(teamListModel.getImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentcalender = new Intent(activity, FrameActivity.class);
                intentcalender.putExtra("frag_name", "FragmentAttendance");
                intentcalender.putExtra("frag_tag", "attendencecalender");
                intentcalender.putExtra("title", "Attendance Calender");
                intentcalender.putExtra("UserID",teamListModelArrayList.get(position).getId());
                activity.startActivity(intentcalender);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (teamListModelArrayList == null)
            return 0;
        return teamListModelArrayList.size();
    }

    public class ListHolder extends ViewHolder {
        TextView tvname, tvdesignation;
        ImageView imageView;

        public ListHolder(View itemView, Context context, ArrayList<TeamListModel> teamListModelArrayList) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_name);
            tvdesignation = itemView.findViewById(R.id.tv_designation);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }
}
