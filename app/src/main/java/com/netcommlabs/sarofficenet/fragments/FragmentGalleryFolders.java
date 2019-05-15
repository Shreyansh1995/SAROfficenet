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
import com.netcommlabs.sarofficenet.activity.LoginActivity;
import com.netcommlabs.sarofficenet.adapter.GalleryFolderAdapter;
import com.netcommlabs.sarofficenet.adapter.TeamListAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.GalleryFolderModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.GALLERY_CATEGORY_TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGalleryFolders extends Fragment implements ResponseListener {
    private RecyclerView recyclerView;
    private ArrayList<GalleryFolderModel> galleryFolderModelArrayList;
    private GalleryFolderAdapter galleryFolderAdapter;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery_folder, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
        findViewByid(v);
        return v;
    }

    private void findViewByid(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerView.setNestedScrollingEnabled(false);
        galleryFolderModelArrayList = new ArrayList<>();
        galleryFolderAdapter = new GalleryFolderAdapter(galleryFolderModelArrayList, getContext(), getActivity());
        if (NetworkUtils.isConnected(activity)) {
            hitapi();
        } else {
            AppAlertDialog.showDialogSelfFinish(activity, "", "Please check your internet connection");
        }
    }

    private void hitapi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), UrlConstants.GALLERY_CATEGORY, this, GALLERY_CATEGORY_TAG);
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
            object.put("PlantID", "0");
            LogUtils.showLog("request :", "Tag :" + GALLERY_CATEGORY_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        LogUtils.showLog("Response :", "Tag :" + Tag + call.toString());
        clearRef();
        try {
            if (call.getString("Message").equals("Success")) {
                JSONArray jsonArray = call.getJSONArray("GalleryCategoryList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    GalleryFolderModel galleryFolderModel = new GalleryFolderModel();
                    galleryFolderModel.setId(jsonObject.getString("GalleryCategoryID"));
                    galleryFolderModel.setImage(jsonObject.getString("ImagePath"));
                    galleryFolderModel.setName(jsonObject.getString("GalleryCategoryTitleName"));
                    galleryFolderModelArrayList.add(galleryFolderModel);
                }
                recyclerView.setAdapter(galleryFolderAdapter);
                galleryFolderAdapter.notifyDataSetChanged();
            } else {
                AppAlertDialog.showDialogSelfFinish(activity, "", call.optString("Message"));
            }
        } catch (JSONException e) {
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
