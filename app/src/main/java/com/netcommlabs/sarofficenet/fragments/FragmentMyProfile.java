package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.utils.ImageBlur;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import jp.wasabeef.blurry.Blurry;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyProfile extends Fragment {
    private ImageView ivblur, imageView;
    private TextView tvdob, tvdoj, tvname, tvdesignation, tvempcode, tvemail, tvmobile, tvdprtmnt, tvrm, tvhod, tvlocation, tvbloodgroup, tvgrade, tvempstatus;

    private MySharedPreference mySharedPreference;
    private FrameActivity activity;

    private int BLUR_PRECENTAGE = 3;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        mySharedPreference = MySharedPreference.getInstance(getContext());
        findviewByid(v);
        return v;
    }

    private void findviewByid(View v) {
       // ivblur = v.findViewById(R.id.iv_blur);
        imageView = v.findViewById(R.id.imageView);
        tvdob = v.findViewById(R.id.tv_dob);
        tvdoj = v.findViewById(R.id.tv_doj);
        tvname = v.findViewById(R.id.tv_name);
        tvdesignation = v.findViewById(R.id.tv_designation);
        tvempcode = v.findViewById(R.id.tv_empcode);
        tvemail = v.findViewById(R.id.tv_email);
        tvmobile = v.findViewById(R.id.tv_mobile);
        tvdprtmnt = v.findViewById(R.id.tv_dept);
        tvrm = v.findViewById(R.id.tv_rm);
        tvhod = v.findViewById(R.id.tv_hod);
        tvlocation = v.findViewById(R.id.tv_location);
        tvbloodgroup = v.findViewById(R.id.tv_bloodgrop);
        tvgrade = v.findViewById(R.id.tv_grade);
        tvempstatus = v.findViewById(R.id.tv_empstatus);

        //making image blue
      //  Blurry.with(activity).from(bitmap).into(ivblur);

        tvdob.setText(mySharedPreference.getDob());
        tvdoj.setText(mySharedPreference.getDoj());
        tvname.setText(mySharedPreference.getUsername());
        tvdesignation.setText(mySharedPreference.getDesignation());
        tvempcode.setText(mySharedPreference.getEmpcode());
        tvemail.setText(mySharedPreference.getEmail());
        tvmobile.setText(mySharedPreference.getMobile());
        tvdprtmnt.setText(mySharedPreference.getDepartment());
        tvrm.setText(mySharedPreference.getRm());
        tvhod.setText(mySharedPreference.getHod());
        tvlocation.setText(mySharedPreference.getLocation());
        tvbloodgroup.setText(mySharedPreference.getBloodgroup());
        tvgrade.setText(mySharedPreference.getGrade());
        tvempstatus.setText(mySharedPreference.getEmpstatus());
        Picasso.with(getContext()).load(mySharedPreference.getImage()).into(imageView);

        //-----------Making image blur -----------------------------------------
        //Configure target for
       /* Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ivblur.setImageBitmap(ImageBlur.fastblur(bitmap, 1f, BLUR_PRECENTAGE));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                ivblur.setImageResource(R.mipmap.ic_launcher);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };*/


       /* Picasso.with(activity).load(mySharedPreference.getImage()).into(imageView);
        ivblur.setTag(target);
        Picasso.with(activity)
                .load(mySharedPreference.getImage())
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(target);*/




    }

}
