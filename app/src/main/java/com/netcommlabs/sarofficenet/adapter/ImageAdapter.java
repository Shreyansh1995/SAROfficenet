package com.netcommlabs.sarofficenet.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.model.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Flash_Netcomm on 2/19/2019.
 */

public class ImageAdapter extends PagerAdapter {
    public ImageAdapter(Activity activity, ArrayList<ImageModel> imageModelArrayList) {
        this.activity = activity;
        this.imageModelArrayList = imageModelArrayList;
    }

    private Activity activity;
    private ArrayList<ImageModel> imageModelArrayList;

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.image_row, container, false);
        ImageView imageView = layout.findViewById(R.id.photo);

        ImageModel imageModel = imageModelArrayList.get(position);
        Picasso.with(activity).load(imageModel.getImage()).into(imageView);
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
