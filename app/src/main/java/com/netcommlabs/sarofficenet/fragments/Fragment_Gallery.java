package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.adapter.GalleryAdapter;
import com.netcommlabs.sarofficenet.adapter.GalleryFolderAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.GalleryModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.GALLERY_CATEGORY_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GALLERY_LIST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Gallery extends Fragment implements ResponseListener {
    private RecyclerView recyclerView;
    private ArrayList<GalleryModel> galleryArrayList;
    private GalleryAdapter galleryAdapter;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private String CategoryID;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        CategoryID = getArguments().getString("id");
        findViewByid(v);
        return v;
    }

    private void findViewByid(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
        recyclerView.setNestedScrollingEnabled(false);
        galleryArrayList = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(galleryArrayList, getContext(), getActivity());
        if (NetworkUtils.isConnected(activity)) {
            hitapi();
        } else {
            AppAlertDialog.showDialogSelfFinish(activity, "", "Please check your internet connection");
        }
    }

    private void hitapi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), UrlConstants.GALLERY_LIST, this, GALLERY_LIST_TAG);
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
            //  object.put("PlantID",mySharedPreference.getPlantid());
            object.put("CategoryID", CategoryID);
            LogUtils.showLog("request :", "Tag :" + GALLERY_LIST_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        LogUtils.showLog("Response :", "Tag :" + Tag + call.toString());
        try {
            if (call.getString("Message").equals("Success")) {
                JSONArray jsonArray = call.getJSONArray("GalleryCategoryList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    GalleryModel galleryModel = new GalleryModel();
                    galleryModel.setID(jsonObject.getString("GalleryCategoryID"));
                    galleryModel.setImage(jsonObject.getString("ImagePath"));
                    galleryModel.setCategoryID(CategoryID);
                    galleryArrayList.add(galleryModel);
                }
                recyclerView.setAdapter(galleryAdapter);
                galleryAdapter.notifyDataSetChanged();
            }else {
                AppAlertDialog.showDialogSelfFinish(activity, "", call.optString("Message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        LogUtils.showLog("Response :", "Tag :" + Tag + error.toString());
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }
}
