package com.netcommlabs.sarofficenet.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.adapter.TeamListAdapter;
import com.netcommlabs.sarofficenet.adapter.TicketListAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.HelpDeskDetailsList;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.DateManagerUtility;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_DEPARTMENT;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_DEPARTMENT_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHelpDeskTicketList extends Fragment implements ResponseListener {
    private RecyclerView recyclerView;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    View v;
    private TicketListAdapter ticketListAdapter;
    private ArrayList<HelpDeskDetailsList> helpDeskDetailsListArrayList;
    String[] spinnerData = {"All Tickets", "Pending", "Solved", "Closed", "On Hold", "Re-Assigned"};
    private String StatId = "%";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filtermenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                showDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_help_desk);
        final Spinner spinner = dialog.findViewById(R.id.spinner);
        Button btn_search = dialog.findViewById(R.id.btn_search);

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItemPosition() == 0) {
                    StatId = "%";
                } else if (spinner.getSelectedItemPosition() == 1) {
                    StatId = "0";
                } else if (spinner.getSelectedItemPosition() == 2) {
                    StatId = "1";
                } else if (spinner.getSelectedItemPosition() == 3) {
                    StatId = "2";
                } else if (spinner.getSelectedItemPosition() == 4) {
                    StatId = "3";
                } else if (spinner.getSelectedItemPosition() == 5) {
                    StatId = "4";
                }
                getList(StatId);
                dialog.dismiss();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_help_desk_ticket_list, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        init(v);
    }

    private void init(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        helpDeskDetailsListArrayList = new ArrayList<>();
        ticketListAdapter = new TicketListAdapter(getActivity(), helpDeskDetailsListArrayList, getContext());
        if (getUserVisibleHint()) {
            getList("%");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getList("%");
        }
    }

    private void getList(String StatusId) {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", mySharedPreference.getUid());
            object.put("StatusID", StatusId);
            request = new ProjectWebRequest(activity, object, TICKET_LIST, this, TICKET_LIST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        if (Tag == TICKET_LIST_TAG) {
            if (call.optString("Status").equalsIgnoreCase("true")) {
                if (helpDeskDetailsListArrayList.size() > 0) {
                    helpDeskDetailsListArrayList.clear();
                }
                try {
                    JSONArray jsonArray = call.getJSONArray("HelpDeskDetailsList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HelpDeskDetailsList helpDeskDetailsList = new Gson().fromJson(jsonArray.get(i).toString(), HelpDeskDetailsList.class);
                        helpDeskDetailsListArrayList.add(helpDeskDetailsList);
                    }

                    recyclerView.setAdapter(ticketListAdapter);
                    ticketListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (helpDeskDetailsListArrayList.size() > 0) {
                    helpDeskDetailsListArrayList.clear();
                }
                recyclerView.setAdapter(ticketListAdapter);
                ticketListAdapter.notifyDataSetChanged();
            }
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
