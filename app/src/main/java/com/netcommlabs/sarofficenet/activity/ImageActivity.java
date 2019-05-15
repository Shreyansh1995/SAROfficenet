package com.netcommlabs.sarofficenet.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.adapter.ImageAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.GalleryModel;
import com.netcommlabs.sarofficenet.model.ImageModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.GALLERY_LIST_TAG;

public class ImageActivity extends AppCompatActivity implements ResponseListener {
    private ImageView ivbck;
    private TextView tvtitle;
    private ArrayList<ImageModel> imageModelArrayList;
    private String CategoryID, ImageID;
    private ProjectWebRequest request;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        findviewByid();
    }

    private void findviewByid() {
        ivbck = findViewById(R.id.img_back);
        tvtitle = findViewById(R.id.tv_title);
        viewPager = findViewById(R.id.viewPager);

        ivbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        CategoryID = intent.getStringExtra("CatId");
        ImageID = intent.getStringExtra("ImageId");
        getImages();


    }

    private void getImages() {
        try {
            request = new ProjectWebRequest(this, getParam(), UrlConstants.GALLERY_LIST, this, GALLERY_LIST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("CategoryID", CategoryID);
            LogUtils.showLog("request :", "Tag :" + GALLERY_LIST_TAG + object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        try {
            if (call.getString("Message").equals("Success")) {
                imageModelArrayList = new ArrayList<>();
                JSONArray jsonArray = call.getJSONArray("GalleryCategoryList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                   ImageModel imageModel = new ImageModel();
                   imageModel.setImage(jsonObject.getString("ImagePath"));
                   imageModel.setImageID(jsonObject.getString("GalleryCategoryID"));
                   imageModelArrayList.add(imageModel);
                }
                viewPager.setAdapter(new ImageAdapter(this, imageModelArrayList));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        LogUtils.showLog("request :", "Tag :" + GALLERY_LIST_TAG + error.toString());
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }
}
