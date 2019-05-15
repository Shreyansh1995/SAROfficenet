package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.ImageActivity;
import com.netcommlabs.sarofficenet.model.GalleryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Flash_Netcomm on 2/19/2019.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {

    public GalleryAdapter(ArrayList<GalleryModel> galleryModelArrayList, Context context, Activity activity) {
        this.galleryModelArrayList = galleryModelArrayList;
        this.context = context;
        this.activity = activity;
    }

    private ArrayList<GalleryModel> galleryModelArrayList;
    private Context context;
    private Activity activity;

    @Override
    public GalleryAdapter.GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_row, parent, false);
        return new GalleryAdapter.GalleryHolder(itemView, context, galleryModelArrayList);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.GalleryHolder holder, final int position) {
        GalleryModel galleryModel = galleryModelArrayList.get(position);
        Picasso.with(activity).load(galleryModel.getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ImageActivity.class);
                intent.putExtra("CatId",galleryModelArrayList.get(position).getCategoryID());
                intent.putExtra("ImageId",galleryModelArrayList.get(position).getID());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryModelArrayList.size();
    }

    public class GalleryHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public GalleryHolder(View itemView, Context context, ArrayList<GalleryModel> galleryModelArrayList) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }
}
