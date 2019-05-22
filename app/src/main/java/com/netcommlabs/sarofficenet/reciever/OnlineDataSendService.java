package com.netcommlabs.sarofficenet.reciever;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.database.DataBaseOperations;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.AttendanceModelOffline;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.MyApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_STATUS;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_STATUS_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SAVEPUNCHINOOUT_TAG;

public class OnlineDataSendService extends IntentService {
    private Context context;
    private ArrayList<AttendanceModelOffline> data;
    private DataBaseOperations dop;
    private String URL, reqq, ID;
    private ProjectWebRequest request;
    private int size, i = 0;

    public OnlineDataSendService() {
        super("OnlineDataSendService");
    }

    public OnlineDataSendService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        this.context = OnlineDataSendService.this;
        data = new ArrayList<>();
        dop = new DataBaseOperations(context);
        data = dop.getdata();

        size = data.size();
        sendDate(i);
    }

    private void sendDate(int j) {
        if (j < size) {
            URL = data.get(j).getModule();
            reqq = data.get(j).getRequest();
            ID = data.get(j).getID();
            hitApi();

        }
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }


    void hitApi() {
        JSONObject object = null;
        try {
            object = new JSONObject(reqq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URL, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.optString("Status").equalsIgnoreCase("true")) {
                                dop.DeleteRow(dop, ID);
                                sendDate(i++);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {

                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
            }
        }
        );
        MyApp.getInstance().addToRequestQueue(jsonObjReq, "" + 1000);
    }


}
