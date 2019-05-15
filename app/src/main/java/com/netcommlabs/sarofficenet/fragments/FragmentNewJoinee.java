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
import com.netcommlabs.sarofficenet.adapter.NewJoineeAdapter;
import com.netcommlabs.sarofficenet.adapter.TeamListAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.NewJoineeModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.BIRTHDAY_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GALLERY_CATEGORY_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.NEW_JOINEE_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewJoinee extends Fragment implements ResponseListener {

    private RecyclerView recyclerView;
    private ArrayList<NewJoineeModel> newJoineeModelArrayList;
    private NewJoineeAdapter newJoineeAdapter;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private String Type,Url;
    private int Tag;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_joinee, container, false);

        Type = getArguments().getString("type");
        if (Type.equalsIgnoreCase("New Joinee")){
            Url = UrlConstants.NEW_JOINEE;
            Tag = NEW_JOINEE_TAG;
        }else {
            Url = UrlConstants.BIRTHDAY;
            Tag = BIRTHDAY_TAG;
        }

        findViewByid(v);
        return v;
    }

    private void findViewByid(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        newJoineeModelArrayList = new ArrayList<>();
        newJoineeAdapter = new NewJoineeAdapter(newJoineeModelArrayList, getContext(), getActivity());
        if (NetworkUtils.isConnected(activity)) {
            hitApi();
        } else {
            Toast.makeText(activity, "Please Check your internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    private void hitApi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), Url, this, Tag);
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
            LogUtils.showLog("request :", "Tag :" + NEW_JOINEE_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();

        try {
            if (call.getString("Message").equalsIgnoreCase("Success")) {
                JSONArray jsonArray = call.getJSONArray("EmpBirthdayList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String DOB = jsonObject.getString("EmpDOB");
                    String DOJ = jsonObject.getString("EmpDOJ");

                    NewJoineeModel newJoineeModel = new NewJoineeModel();
                    newJoineeModel.setEmpDepartment(jsonObject.getString("EmpDepartment"));
                    newJoineeModel.setEmpDesignation(jsonObject.getString("EmpDesignation"));
                    newJoineeModel.setEmpDOB(DOB);
                    newJoineeModel.setEmpDOJ(DOJ);
                    newJoineeModel.setEmpFirstName(jsonObject.getString("EmpFirstName"));
                    newJoineeModel.setEmpID(jsonObject.getString("EmpID"));
                    newJoineeModel.setEmpImage(jsonObject.getString("EmpImage"));
                    newJoineeModel.setEmpName(jsonObject.getString("EmpName"));
                    newJoineeModel.setEmpLocation(jsonObject.getString("EmpLocation"));
                    newJoineeModel.setType(Type);
                    newJoineeModelArrayList.add(newJoineeModel);
                }
                recyclerView.setAdapter(newJoineeAdapter);
                newJoineeAdapter.notifyDataSetChanged();
            } else {
                AppAlertDialog.showDialogSelfFinish(activity, "", call.optString("Message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {

    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }
}
