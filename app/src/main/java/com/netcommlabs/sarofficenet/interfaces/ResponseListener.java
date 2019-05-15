package com.netcommlabs.sarofficenet.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Flash_Netcomm on 2/16/2019.
 */

public interface ResponseListener {
    void onSuccess(JSONObject call, int Tag);
    void onFailure(VolleyError error, int Tag);
}
