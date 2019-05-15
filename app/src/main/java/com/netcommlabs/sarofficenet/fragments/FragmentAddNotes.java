package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.FragmentTransections;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONObject;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.ADD_NOTES_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.DELETE_NOTES_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.NOTES_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddNotes extends Fragment implements ResponseListener {
    private EditText et_msg;
    private Button btnsubmit;
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
        View v = inflater.inflate(R.layout.fragment_add_notes, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
        findviewBYId(v);
        return v;
    }

    private void findviewBYId(View v) {
        et_msg = v.findViewById(R.id.et_msg);
        btnsubmit = v.findViewById(R.id.btn_submit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isConnected(activity)) {
                    if (!et_msg.getText().toString().equalsIgnoreCase("")) {
                        hitApi();
                    } else {
                        Toast.makeText(activity, "Please enter message", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(activity, "Please Check your intenet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hitApi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), UrlConstants.ADD_NOTES, this, ADD_NOTES_TAG);
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
            object.put("Notes", et_msg.getText().toString());
            LogUtils.showLog("request :", "Tag :" + ADD_NOTES_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        try {
            if (call.optString("Status").equalsIgnoreCase("true")) {
                Toast.makeText(activity, "" + call.getString("Message"), Toast.LENGTH_SHORT).show();
//                FragmentTransections.popFragment(activity);
                activity.finish();
            }
        } catch (Exception e) {
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
