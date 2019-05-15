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
import com.netcommlabs.sarofficenet.adapter.TeamListAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.TeamListModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.HOLIDAY_LIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.MY_TEAM_LIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.NEW_JOINEE_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTeamList extends Fragment implements ResponseListener {
    private RecyclerView recyclerView;
    private ArrayList<TeamListModel> teamListModelArrayList;
    private TeamListAdapter teamListAdapter;
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
        View v = inflater.inflate(R.layout.fragment_fragment_team_list, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
        findViewByid(v);
        return v;
    }

    private void findViewByid(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        teamListModelArrayList = new ArrayList<>();
        teamListAdapter = new TeamListAdapter(teamListModelArrayList, getContext(), getActivity());
        if (NetworkUtils.isConnected(activity)) {
            hitApi();
        } else {
            Toast.makeText(activity, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }

    }

    private void hitApi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), UrlConstants.MY_TEAM_LIST, this, MY_TEAM_LIST_TAG);
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
            if (call.getString("Message").equalsIgnoreCase("Success")) {
                JSONArray jsonArray = call.getJSONArray("MyTeamList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TeamListModel teamListModel = new TeamListModel();
                    teamListModel.setDesignation(jsonObject.getString("EmpDesignation"));
                    teamListModel.setId(jsonObject.getString("EmpID"));
                    teamListModel.setImage(jsonObject.getString("EmpImage"));
                    teamListModel.setName(jsonObject.getString("EmpName"));
                    teamListModel.setEmpCode(jsonObject.getString("EmpCode"));
                    teamListModel.setEmpDateOfJoining(jsonObject.getString("EmpDateOfJoining"));
                    teamListModelArrayList.add(teamListModel);
                }
                recyclerView.setAdapter(teamListAdapter);
                teamListAdapter.notifyDataSetChanged();

            } else {
                AppAlertDialog.showDialogSelfFinish(activity, "", call.optString("Message"));
            }
        } catch (Exception ex) {
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
