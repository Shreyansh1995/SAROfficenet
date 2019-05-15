package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.fragments.Fragment_Gallery;
import com.netcommlabs.sarofficenet.model.GalleryFolderModel;
import com.netcommlabs.sarofficenet.utils.FragmentTransections;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Flash_Netcomm on 2/19/2019.
 */

public class GalleryFolderAdapter extends RecyclerView.Adapter<GalleryFolderAdapter.FolderHolder> {
    public GalleryFolderAdapter(ArrayList<GalleryFolderModel> galleryFolderModelArrayList, Context context, Activity activity) {
        this.galleryFolderModelArrayList = galleryFolderModelArrayList;
        this.context = context;
        this.activity = activity;
    }

    private ArrayList<GalleryFolderModel> galleryFolderModelArrayList;
    private Context context;
    private Activity activity;

    @Override
    public GalleryFolderAdapter.FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_folder_row, parent, false);
        return new GalleryFolderAdapter.FolderHolder(itemView, context, galleryFolderModelArrayList);
    }

    @Override
    public void onBindViewHolder(GalleryFolderAdapter.FolderHolder holder, final int position) {
        GalleryFolderModel galleryFolderModel = galleryFolderModelArrayList.get(position);
        holder.tvname.setText(galleryFolderModelArrayList.get(position).getName());
        Picasso.with(activity).load(galleryFolderModel.getImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, FrameActivity.class);
                intent.putExtra("frag_name","Fragment_Gallery");
                intent.putExtra("frag_tag","gallery");
                intent.putExtra("title",galleryFolderModelArrayList.get(position).getName());
                intent.putExtra("id",galleryFolderModelArrayList.get(position).getId());

                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (galleryFolderModelArrayList == null)
            return 0;
        return galleryFolderModelArrayList.size();

    }

    public class FolderHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvname;

        public FolderHolder(View itemView, Context context, ArrayList<GalleryFolderModel> galleryFolderModelArrayList) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            tvname = itemView.findViewById(R.id.tv_name);
        }
    }
}
