package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.AbbreviationModel;

import java.util.ArrayList;

/**
 * Created by Flash_Netcomm on 2/19/2019.
 */

public class AbbreviationAdapter extends RecyclerView.Adapter<AbbreviationAdapter.AbbriviationHolder> {


    public AbbreviationAdapter(ArrayList<AbbreviationModel> abbreviationModelArrayList, Activity activity, Context context) {
        this.abbreviationModelArrayList = abbreviationModelArrayList;
        this.activity = activity;
        this.context = context;
    }

    private ArrayList<AbbreviationModel> abbreviationModelArrayList;
    private Activity activity;
    private Context context;

    @Override
    public AbbreviationAdapter.AbbriviationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.abbreviation_row, parent, false);
        return new AbbreviationAdapter.AbbriviationHolder(itemView, context, abbreviationModelArrayList);
    }

    @Override
    public void onBindViewHolder(AbbreviationAdapter.AbbriviationHolder holder, int position) {
        holder.tvfull.setText(abbreviationModelArrayList.get(position).getFull() + " ("+ abbreviationModelArrayList.get(position).getCount() + ")");
        holder.tvshort.setText(abbreviationModelArrayList.get(position).getShort());
        holder.imageView.setColorFilter(Color.parseColor(abbreviationModelArrayList.get(position).getColor()), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount() {
        return abbreviationModelArrayList.size();
    }

    public class AbbriviationHolder extends RecyclerView.ViewHolder {
        TextView tvfull, tvshort;
        ImageView imageView;

        public AbbriviationHolder(View itemView, Context context, ArrayList<AbbreviationModel> abbreviationModelArrayList) {
            super(itemView);
            tvfull = itemView.findViewById(R.id.tv_fullName);
            tvshort = itemView.findViewById(R.id.tv_shortName);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }
}
