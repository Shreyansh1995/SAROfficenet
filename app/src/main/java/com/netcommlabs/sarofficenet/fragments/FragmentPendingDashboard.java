package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.adapter.PendingRequestAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.PendingRequestsModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_PENDING_DATA_DASHBOARD_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.MY_TEAM_LIST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPendingDashboard extends Fragment implements ResponseListener {
    private RecyclerView recyclerView;
    private ArrayList<PendingRequestsModel> pendingRequestsModelArrayList;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    private PendingRequestAdapter pendingRequestAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pending_dashboard, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
        findViewByid(v);
        return v;
    }

    private void findViewByid(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        pendingRequestsModelArrayList = new ArrayList<>();
        pendingRequestAdapter = new PendingRequestAdapter(pendingRequestsModelArrayList, getContext(), getActivity());
        if (NetworkUtils.isConnected(activity)) {
            hitApi();
        } else {
            Toast.makeText(activity, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void hitApi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), UrlConstants.GET_PENDING_DATA_DASHBOARD, this, GET_PENDING_DATA_DASHBOARD_TAG);
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
            object.put("UserID", mySharedPreference.getUid());
            LogUtils.showLog("request :", "Tag :" + MY_TEAM_LIST_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        try {
            if (Tag == GET_PENDING_DATA_DASHBOARD_TAG){
                if (call.getString("Message").equalsIgnoreCase("Success")){
                    JSONArray jsonArray = call.getJSONArray("DashboardList");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PendingRequestsModel pendingRequestsModel = new PendingRequestsModel();
                        pendingRequestsModel.setModuleID(jsonObject.getString("ModuleID"));
                        pendingRequestsModel.setModuleName(jsonObject.getString("ModuleName"));
                        pendingRequestsModel.setPendingCount(jsonObject.getString("PendingCount"));
                        pendingRequestsModelArrayList.add(pendingRequestsModel);
                    }
                    recyclerView.setAdapter(pendingRequestAdapter);
                    pendingRequestAdapter.notifyDataSetChanged();
                }else {
                    AppAlertDialog.showDialogSelfFinish(activity, "", call.optString("Message"));
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
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
