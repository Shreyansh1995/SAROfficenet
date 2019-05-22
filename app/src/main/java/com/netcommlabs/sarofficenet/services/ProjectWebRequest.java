package com.netcommlabs.sarofficenet.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.utils.CustomProgressDialog;
import com.netcommlabs.sarofficenet.utils.MyApp;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONObject;

/**
 * Created by Flash_Netcomm on 2/16/2019.
 */

public class ProjectWebRequest {
    private Context mContext;
    private JSONObject object;
    private String url;
    private ResponseListener listener;
    private CustomProgressDialog dialog;
    private int Tag;


    public ProjectWebRequest(Context mContext, JSONObject object, String url, ResponseListener listener, int Tag) {
        this.mContext = mContext;
        this.object = object;
        this.url = url;
        this.listener = listener;
        this.Tag = Tag;
        dialog = CustomProgressDialog.getInstance(mContext);
    }

    synchronized public void execute() {
        if (NetworkUtils.isConnected(mContext)) {
            dialog.showProgressBar();
            Log.e("@@@@@@URL->>>>>>>>>>>", url);
            Log.e("@@@@@@PARAM->>>>>>>>>", object.toString());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.hideProgressBar();
                            listener.onSuccess(response, Tag);
                            Log.e("@@@@@@@@", response.toString());
                        }
                    }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.hideProgressBar();
                    error.printStackTrace();
                    listener.onFailure(error, Tag);
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
            MyApp.getInstance().addToRequestQueue(jsonObjReq, "" + Tag);
            System.gc();
        } else {
            if (mContext != null){
                dialog.hideProgressBar();
               // listener.onNoInternet("No internet connection found", Tag);
                Toast.makeText(mContext, "No internet connection found", Toast.LENGTH_LONG).show();
            }

            return;
        }
    }
}
