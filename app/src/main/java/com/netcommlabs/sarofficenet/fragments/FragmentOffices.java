package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.adapter.OfficesAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.OfficesModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_OFFICES_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.MY_TEAM_LIST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOffices extends Fragment implements ResponseListener {
    private RecyclerView recyclerView;
    private ArrayList<OfficesModel> officesModelArrayList;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private OfficesAdapter officesAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_offices, container, false);
        findViewByid(v);
        return v;
    }

    private void findViewByid(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        officesModelArrayList = new ArrayList<>();
        officesAdapter = new OfficesAdapter(officesModelArrayList, getContext(), getActivity());
        if (NetworkUtils.isConnected(activity)) {
            hitApi();
        } else {
            Toast.makeText(activity, "Please Check your internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    private void hitApi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), UrlConstants.GET_OFFICES, this, GET_OFFICES_TAG);
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
            LogUtils.showLog("request :", "Tag :" + GET_OFFICES_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        try {
            if (Tag == GET_OFFICES_TAG) {
                if (call.getString("Message").equalsIgnoreCase("Success")) {
                    JSONArray jsonArray = call.getJSONArray("MyOfficesList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        OfficesModel officesModel = new OfficesModel();
                        officesModel.setBranchID(jsonObject.getString("BranchID"));
                        officesModel.setCity(jsonObject.getString("City"));
                        officesModel.setContactAddress(jsonObject.getString("ContactAddress"));
                        officesModel.setContactNo(jsonObject.getString("ContactNo"));
                        officesModel.setCountry(jsonObject.getString("Country"));
                        officesModel.setContactPerson(jsonObject.getString("ContactPerson"));
                        officesModel.setEmailID(jsonObject.getString("EmailID"));
                        officesModel.setOfficeCategory(jsonObject.getString("OfficeCategory"));
                        officesModel.setState(jsonObject.getString("State"));
                        officesModelArrayList.add(officesModel);
                    }
                    recyclerView.setAdapter(officesAdapter);
                    officesAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {

    }
}
