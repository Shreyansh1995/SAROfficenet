package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONObject;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_DETAIL;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_DETAIL_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHelpDeskDetail extends Fragment implements ResponseListener {
    private TextView tv_num, tv_category, tv_subcategory, tv_contact, tv_submitby, tv_submiton, tv_raisedissue;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    private String ReqID = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help_desk_detail, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
        init(v);
        return v;
    }

    private void init(View v) {
        tv_num = v.findViewById(R.id.tv_num);
        tv_category = v.findViewById(R.id.tv_category);
        tv_subcategory = v.findViewById(R.id.tv_subcategory);
        tv_contact = v.findViewById(R.id.tv_contact);
        tv_submitby = v.findViewById(R.id.tv_submitby);
        tv_submiton = v.findViewById(R.id.tv_submiton);
        tv_raisedissue = v.findViewById(R.id.tv_raisedissue);

        ReqID = getArguments().getString("ReqId");

        if (NetworkUtils.isConnected(activity)) {
            hitApi();
        } else {
            Toast.makeText(activity, "Please Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }
    private void hitApi() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", ReqID);
            request = new ProjectWebRequest(activity, object, TICKET_LIST_DETAIL, this, TICKET_LIST_DETAIL_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }
    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        try {
            if (call.optString("Status").equalsIgnoreCase("true")) {
                tv_num.setText(call.getString("TicketNo"));
                tv_category.setText(call.getString("Category"));
                tv_subcategory.setText(call.getString("SubCategory"));
                tv_contact.setText(call.getString("ContactNo"));
                tv_submitby.setText(call.getString("SubmittedBy"));
                tv_submiton.setText(call.getString("SubmittedOn"));
                tv_raisedissue.setText(call.getString("RaisedIssue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }
}
