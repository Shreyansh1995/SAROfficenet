package com.netcommlabs.sarofficenet.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.adapter.PendingTicketListAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.HelpDeskAdminListModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.DateManagerUtility;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.CATEGORY_LIST_ADMIN;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.CATEGORY_LIST_ADMIN_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_DEPARTMENT;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_DEPARTMENT_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_SUBCATEGORYLIST;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_SUBCATEGORYLIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUBMITTEDBY;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUBMITTEDBY_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_ADMIN;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_ADMIN_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHelpDeskPendingTickets extends Fragment implements ResponseListener {
    private RecyclerView recyclerView;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    View v;
    private DateManagerUtility utility;
    private String CategoryID = "%", SubCategoryID = "%", SubmittedByID = "%", FromDate = "",
            ToDate = "", TicketNo = "", StatId = "0";
    private TextView tv_short_leave_list_from_date;
    private TextView tv_short_leave_list_to_date;
    private Spinner sp_category, sp_subcategory, sp_submittedby, sp_status;
    String[] spinnerData = {"All Tickets", "Pending", "Solved", "Closed", "On Hold", "Re-Assigned"};
    private ArrayList<String> CategoryNameList, CategoryIDList, SubmittedByIDList, SubmittedByNameList, SubCategoryNameList, SubCategoryIDList;
    private int myear, mmonth, enddate;
    private ArrayList<HelpDeskAdminListModel> helpDeskAdminListModelArrayList;
    private PendingTicketListAdapter pendingTicketListAdapter;

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
        dialog.setContentView(R.layout.filter_helpdesk_admin);

        sp_category = dialog.findViewById(R.id.sp_category);
        sp_subcategory = dialog.findViewById(R.id.sp_subcategory);
        sp_submittedby = dialog.findViewById(R.id.sp_submittedby);
        sp_status = dialog.findViewById(R.id.sp_status);
        Button btn_search = dialog.findViewById(R.id.btn_search);
        tv_short_leave_list_from_date = dialog.findViewById(R.id.tv_short_leave_list_from_date);
        tv_short_leave_list_to_date = dialog.findViewById(R.id.tv_short_leave_list_to_date);
        LinearLayout ll_short_leave_list_from_date = dialog.findViewById(R.id.ll_short_leave_list_from_date);
        LinearLayout ll_short_leave_list_to_date = dialog.findViewById(R.id.ll_short_leave_list_to_date);
        final EditText et_reqno = dialog.findViewById(R.id.et_reqno);

        utility = new DateManagerUtility();

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_status.setAdapter(aa);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        String from_date = sdf.format(calendar.getTime());
        tv_short_leave_list_from_date.setText(from_date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 30);
        String to_date = sdf.format(calendar1.getTime());
        tv_short_leave_list_to_date.setText(to_date);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        getCategory();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp_status.getSelectedItemPosition() == 0) {
                    StatId = "%";
                } else if (sp_status.getSelectedItemPosition() == 1) {
                    StatId = "0";
                } else if (sp_status.getSelectedItemPosition() == 2) {
                    StatId = "1";
                } else if (sp_status.getSelectedItemPosition() == 3) {
                    StatId = "2";
                } else if (sp_status.getSelectedItemPosition() == 4) {
                    StatId = "3";
                } else if (sp_status.getSelectedItemPosition() == 5) {
                    StatId = "4";
                }

                FromDate = tv_short_leave_list_from_date.getText().toString().trim();
                ToDate = tv_short_leave_list_to_date.getText().toString().trim();
                if (!TextUtils.isEmpty(et_reqno.getText().toString())) {
                    TicketNo = et_reqno.getText().toString();
                }
                CategoryID = CategoryIDList.get(sp_category.getSelectedItemPosition());

                if (SubCategoryIDList.size() > 0){
                    SubCategoryID = SubCategoryIDList.get(sp_subcategory.getSelectedItemPosition());
                }

                SubmittedByID = SubmittedByIDList.get(sp_submittedby.getSelectedItemPosition());

                if (CategoryID.equalsIgnoreCase("-1")){
                    CategoryID = "%";
                }
                if (SubCategoryID.equalsIgnoreCase("-1")){
                    SubCategoryID = "%";
                }
                if (SubmittedByID.equalsIgnoreCase("-1")){
                    SubmittedByID = "%";
                }

                getList();
                dialog.dismiss();

              /*  if (CategoryIDList.get(sp_category.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    Toast.makeText(activity, "Please select category", Toast.LENGTH_SHORT).show();
                } else if (SubCategoryIDList.get(sp_subcategory.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    Toast.makeText(activity, "Please select sub category", Toast.LENGTH_SHORT).show();
                } else if (SubCategoryIDList.get(sp_submittedby.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    Toast.makeText(activity, "Please select Submitted by", Toast.LENGTH_SHORT).show();
                } else {
                    FromDate = tv_short_leave_list_from_date.getText().toString().trim();
                    ToDate = tv_short_leave_list_to_date.getText().toString().trim();
                    if (!TextUtils.isEmpty(et_reqno.getText().toString())) {
                        TicketNo = et_reqno.getText().toString();
                    }
                    CategoryID = CategoryIDList.get(sp_category.getSelectedItemPosition());
                    SubCategoryID = SubCategoryIDList.get(sp_subcategory.getSelectedItemPosition());
                    SubmittedByID = SubmittedByIDList.get(sp_submittedby.getSelectedItemPosition());
                    getList();
                    dialog.dismiss();
                }*/

            }
        });
        ll_short_leave_list_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utility.FromDatePicker(getContext(), tv_short_leave_list_from_date);
            }
        });
        ll_short_leave_list_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utility.ToDatePicker(getContext(), tv_short_leave_list_to_date);
            }
        });

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!CategoryIDList.get(sp_category.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    getSubCategory(CategoryIDList.get(sp_category.getSelectedItemPosition()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_help_desk_pending_tickets, container, false);
        setHasOptionsMenu(true);
        mySharedPreference = MySharedPreference.getInstance(activity);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        helpDeskAdminListModelArrayList = new ArrayList<>();
        pendingTicketListAdapter = new PendingTicketListAdapter(getActivity(), getContext(), helpDeskAdminListModelArrayList);

        CategoryNameList = new ArrayList<String>();
        CategoryIDList = new ArrayList<String>();
        SubmittedByIDList = new ArrayList<String>();
        SubmittedByNameList = new ArrayList<String>();
        SubCategoryNameList = new ArrayList<String>();
        SubCategoryIDList = new ArrayList<String>();

        if (getUserVisibleHint()) {
            getList();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
              getList();
        }
    }

    private void getList() {

        Calendar c = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        //   myear = cal.get(Calendar.YEAR);
        int mmonthCurrent = cal.get(Calendar.MONTH);
        c.add(Calendar.DATE, 30);
        enddate = c.get(Calendar.DATE);
        mmonth = c.get(Calendar.MONTH);
        myear = c.get(Calendar.YEAR);

        if (FromDate.equalsIgnoreCase("")) {
            FromDate = "01/" + String.valueOf(mmonthCurrent) + "/" + String.valueOf(myear);
        }
        if (ToDate.equalsIgnoreCase("")) {
            ToDate = String.valueOf(enddate) + "/" + String.valueOf(mmonth) + "/" + String.valueOf(myear);
        }

        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", mySharedPreference.getUid());
            object.put("CategoryID", CategoryID);
            object.put("SubCategoryID", SubCategoryID);
            object.put("SubmittedByID", SubmittedByID);
            object.put("FromDate", FromDate);
            object.put("ToDate", ToDate);
            object.put("TicketNo", TicketNo);
            object.put("StatusID", StatId);

            request = new ProjectWebRequest(activity, object, TICKET_LIST_ADMIN, this, TICKET_LIST_ADMIN_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void getCategory() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", mySharedPreference.getUid());
            request = new ProjectWebRequest(activity, object, CATEGORY_LIST_ADMIN, this, CATEGORY_LIST_ADMIN_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void getSubmittedBy() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", mySharedPreference.getUid());
            request = new ProjectWebRequest(activity, object, SUBMITTEDBY, this, SUBMITTEDBY_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void getSubCategory(String CategoryID) {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("CategoryID", CategoryID);
            object.put("PlantID", mySharedPreference.getPlantid());
            request = new ProjectWebRequest(activity, object, GET_SUBCATEGORYLIST, this, GET_SUBCATEGORYLIST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        if (Tag == TICKET_LIST_ADMIN_TAG) {
            // Toast.makeText(activity, "" + call.toString(), Toast.LENGTH_SHORT).show();
            if (call.optString("Status").equalsIgnoreCase("true")) {
                if (helpDeskAdminListModelArrayList.size() > 0) {
                    helpDeskAdminListModelArrayList.clear();
                }
                try {
                    JSONArray jsonArray = call.getJSONArray("HelpDeskAdminList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HelpDeskAdminListModel helpDeskDetailsList = new Gson().fromJson(jsonArray.get(i).toString(), HelpDeskAdminListModel.class);
                        helpDeskAdminListModelArrayList.add(helpDeskDetailsList);
                    }
                    recyclerView.setAdapter(pendingTicketListAdapter);
                    pendingTicketListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (helpDeskAdminListModelArrayList.size() > 0) {
                    helpDeskAdminListModelArrayList.clear();
                }
                recyclerView.setAdapter(pendingTicketListAdapter);
                pendingTicketListAdapter.notifyDataSetChanged();
            }
        } else if (Tag == CATEGORY_LIST_ADMIN_TAG) {
            getSubmittedBy();
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    if (CategoryIDList.size() > 0) {
                        CategoryIDList.clear();
                        CategoryNameList.clear();
                    }
                    CategoryIDList.add("-1");
                    CategoryNameList.add("-Select Category-");
                    JSONArray jsonArray = call.getJSONArray("HelpDeskCategoryList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CategoryIDList.add(jsonObject.getString("CategoryID"));
                        CategoryNameList.add(jsonObject.getString("CategoryName"));
                    }
                    sp_category.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, CategoryNameList));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Tag == SUBMITTEDBY_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    if (SubmittedByIDList.size() > 0) {
                        SubmittedByIDList.clear();
                        SubmittedByNameList.clear();
                    }
                    SubmittedByIDList.add("-1");
                    SubmittedByNameList.add("-Submitted By-");
                    JSONArray jsonArray = call.getJSONArray("SubmittedByList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SubmittedByIDList.add(jsonObject.getString("SubmittedByID"));
                        SubmittedByNameList.add(jsonObject.getString("SubmittedByName"));
                    }
                    sp_submittedby.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, SubmittedByNameList));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Tag == GET_SUBCATEGORYLIST_TAG) {
            LogUtils.showLog("Department list : ", "" + call.toString());
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    if (SubCategoryIDList.size() > 0) {
                        SubCategoryIDList.clear();
                        SubCategoryNameList.clear();
                    }
                    SubCategoryIDList.add("-1");
                    SubCategoryNameList.add("-Select Sub Category-");
                    JSONArray jsonArray = call.getJSONArray("HelpDeskSubCategoryList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SubCategoryIDList.add(jsonObject.getString("SubCategoryID"));
                        SubCategoryNameList.add(jsonObject.getString("SubCategoryName"));
                    }
                    sp_subcategory.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, SubCategoryNameList));

                }
            } catch (Exception e) {
                e.printStackTrace();
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
