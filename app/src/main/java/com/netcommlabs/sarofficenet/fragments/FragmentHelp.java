package com.netcommlabs.sarofficenet.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.BuildConfig;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.MainActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONObject;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.HELP_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.LOG_OUT_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHelp extends Fragment implements ResponseListener {
    private TextView tv_version,tv_contact,tv_email,tv_site;
    private String versionName;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    private String EmailID,MobileNo,WebURL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_help, container, false);
        mySharedPreference = MySharedPreference.getInstance(getContext());
        init(v);
        return v;
    }

    private void init(View v) {
        tv_version = v.findViewById(R.id.tv_version);
        tv_contact = v.findViewById(R.id.tv_contact);
        tv_email = v.findViewById(R.id.tv_email);
        tv_site = v.findViewById(R.id.tv_site);
        versionName = BuildConfig.VERSION_NAME;
        tv_version.setText("Version " + versionName);

        hitApi();


        tv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", MobileNo, null)));
            }
        });

        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + ""+ "&body=" + "" + "&to=" + EmailID);
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
            }
        });
        tv_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW", Uri.parse(WebURL));
                startActivity(viewIntent);
            }
        });
    }

    private void hitApi() {
        try {
            request = new ProjectWebRequest(getContext(), getParam(), UrlConstants.HELP, this, HELP_TAG);
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

        } catch (Exception e) {
        }
        return object;
    }
    void clearRef() {
        if (request != null) {
            request = null;
        }
    }


    @Override
    public void onSuccess(JSONObject call, int Tag) {
        if (Tag == HELP_TAG){
            try {
                if (call.optString("Status").equalsIgnoreCase("true")){
                    EmailID = call.getString("EmailID");
                    MobileNo = call.getString("MobileNo");
                    WebURL = call.getString("WebURL");
                    tv_email.setText(EmailID);
                    tv_contact.setText(MobileNo);
                    tv_site.setText(WebURL);
                    if (!WebURL.startsWith("http://") && !WebURL.startsWith("https://"))
                        WebURL = "http://" + WebURL;
                }

            }catch (Exception e){
                e.printStackTrace();

            }

        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {

    }
}
