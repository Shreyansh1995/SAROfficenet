package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.iid.FirebaseInstanceId;
import com.netcommlabs.sarofficenet.BuildConfig;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.activity.LoginActivity;
import com.netcommlabs.sarofficenet.activity.MainActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.interfacess.NotificationCount;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.VersionChecker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_NOTIFICATION_LIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_PENDING_DATA_DASHBOARD_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.LOG_OUT_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.MY_TEAM_LIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SEND_TOKEN_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashboard extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener, ResponseListener {
    private SliderLayout slider_layout;
    private LinearLayout llcalender, ll_newJoinee, ll_team, ll_leave, ll_bday, ll_pending, ll_office, ll_notes, ll_suggestion;
    private MySharedPreference pref;
    private ProjectWebRequest request;
    private TextView tv_pending;
    private ImageView iv_pending;
    private String refreshedToken;
    private NotificationCount notificationCount;
    private int versionCode;
    private String versionName, mLatestVersionName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        pref = MySharedPreference.getInstance(getActivity());
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        hitApiToSendToken();


        VersionChecker versionChecker = new VersionChecker();
        try {
            mLatestVersionName = versionChecker.execute().get();
            //    Toast.makeText(getContext(), "Playstore Version :" + mLatestVersionName, Toast.LENGTH_SHORT).show();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;
        // Toast.makeText(getContext(), "Current Version :" + versionName, Toast.LENGTH_SHORT).show();
        if (!versionName.equalsIgnoreCase(mLatestVersionName)) {
            //  AppAlertDialog.showDialogSelfFinish(getContext(),"","You are using outdated version. Please update app from play store.");
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
        //    dialbuilderog.setCanceledOnTouchOutside(true);
            builder.setTitle("Update Required").setMessage("The app is outdated. Please, update it to the latest version.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Logout();


                        }
                    })
                    .show();
        }

        findviewbyId(v);
        return v;
    }
    private void Logout() {
        SharedPreferences con = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = con.edit();
        ed.clear();
        ed.commit();

        try {
            request = new ProjectWebRequest(getContext(), getLogoutParam(), UrlConstants.LOG_OUT, this, LOG_OUT_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }

    }

    private JSONObject getLogoutParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());
            LogUtils.showLog("request :", "Tag :" + object.toString());
        } catch (Exception e) {
        }
        return object;
    }


    private void findviewbyId(View v) {
        slider_layout = (SliderLayout) v.findViewById(R.id.slider_layout);
        llcalender = v.findViewById(R.id.ll_calender);
        ll_newJoinee = v.findViewById(R.id.ll_newJoinee);
        ll_team = v.findViewById(R.id.ll_team);
        ll_leave = v.findViewById(R.id.ll_leave);
        ll_bday = v.findViewById(R.id.ll_bday);
        ll_pending = v.findViewById(R.id.ll_pending);
        ll_office = v.findViewById(R.id.ll_office);
        ll_notes = v.findViewById(R.id.ll_notes);
        ll_suggestion = v.findViewById(R.id.ll_suggestion);

        iv_pending = v.findViewById(R.id.iv_pending);
        tv_pending = v.findViewById(R.id.tv_pending);


        llcalender.setOnClickListener(this);
        ll_newJoinee.setOnClickListener(this);
        ll_team.setOnClickListener(this);
        ll_leave.setOnClickListener(this);
        ll_bday.setOnClickListener(this);
        ll_pending.setOnClickListener(this);
        ll_office.setOnClickListener(this);
        ll_notes.setOnClickListener(this);
        ll_suggestion.setOnClickListener(this);
        ShowSlider();


    }

    @Override
    public void onStart() {
        super.onStart();
        hitApi();
        hitApiTogetCount();
    }

    private void hitApiTogetCount() {
        try {
            request = new ProjectWebRequest(getContext(), getCountParam(), UrlConstants.GET_NOTIFICATION_LIST, this, GET_NOTIFICATION_LIST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getCountParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());
            LogUtils.showLog("request :", "Tag :" + GET_NOTIFICATION_LIST_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void hitApi() {
        try {
            request = new ProjectWebRequest(getContext(), getParam(), UrlConstants.GET_PENDING_DATA_DASHBOARD, this, GET_PENDING_DATA_DASHBOARD_TAG);
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
            object.put("UserID", pref.getUid());
            LogUtils.showLog("request :", "Tag :" + MY_TEAM_LIST_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void hitApiToSendToken() {
        try {
            request = new ProjectWebRequest(getContext(), getTokenParam(), UrlConstants.SEND_TOKEN, this, SEND_TOKEN_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getTokenParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", pref.getUid());
            object.put("DeviceID", refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    private void ShowSlider() {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("a", R.drawable.slider_one);
        file_maps.put("b", R.drawable.slider_two);
        file_maps.put("c", R.drawable.slider_three);
       /* HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");*/


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            slider_layout.addSlider(textSliderView);
        }
        slider_layout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        slider_layout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider_layout.setCustomAnimation(new DescriptionAnimation());
        slider_layout.setDuration(8000);


    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        // Toast.makeText(getContext(), "" + slider.getBundle().get("extra").toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.ll_calender:
                Intent intentcalender = new Intent(getContext(), FrameActivity.class);
                intentcalender.putExtra("frag_name", "FragmentAttendance");
                intentcalender.putExtra("frag_tag", "attendencecalender");
                intentcalender.putExtra("title", "Attendance Calender");
                intentcalender.putExtra("UserID", pref.getUid());
                startActivity(intentcalender);
                break;
            case R.id.ll_newJoinee:
                Intent intentnewJoinee = new Intent(getContext(), FrameActivity.class);
                intentnewJoinee.putExtra("frag_name", "FragmentNewJoinee");
                intentnewJoinee.putExtra("frag_tag", "newjoinee");
                intentnewJoinee.putExtra("title", "New Joinee");
                intentnewJoinee.putExtra("type", "New Joinee");
                startActivity(intentnewJoinee);
                break;
            case R.id.ll_team:
                Intent intentteam = new Intent(getContext(), FrameActivity.class);
                intentteam.putExtra("frag_name", "FragmentTeamList");
                intentteam.putExtra("frag_tag", "teamlist");
                intentteam.putExtra("title", "My Team");
                startActivity(intentteam);
                break;
            case R.id.ll_leave:
                Intent intentleave = new Intent(getContext(), FrameActivity.class);
                intentleave.putExtra("frag_name", "FragmentLeaveManagementSystem");
                intentleave.putExtra("frag_tag", "leavemanagement");
                intentleave.putExtra("title", "Leave Management System");
                startActivity(intentleave);
                break;
            case R.id.ll_pending:
                Intent intentpending = new Intent(getContext(), FrameActivity.class);
                intentpending.putExtra("frag_name", "FragmentPendingDashboard");
                intentpending.putExtra("frag_tag", "pendingrequests");
                intentpending.putExtra("title", "Pending Requests");
                startActivity(intentpending);
                break;
            case R.id.ll_office:
                Intent intentoffices = new Intent(getContext(), FrameActivity.class);
                intentoffices.putExtra("frag_name", "FragmentOffices");
                intentoffices.putExtra("frag_tag", "offices");
                intentoffices.putExtra("title", "Office List");
                startActivity(intentoffices);
                break;
            case R.id.ll_notes:
                Intent intentNotes = new Intent(getContext(), FrameActivity.class);
                intentNotes.putExtra("frag_name", "FragmentNotes");
                intentNotes.putExtra("frag_tag", "notes");
                intentNotes.putExtra("title", "Notes");
                startActivity(intentNotes);
                break;
            case R.id.ll_suggestion:
                Intent intentsuggestion = new Intent(getContext(), FrameActivity.class);
                intentsuggestion.putExtra("frag_name", "FragmentSuggestions");
                intentsuggestion.putExtra("frag_tag", "suggestion");
                intentsuggestion.putExtra("title", "Suggestion");
                startActivity(intentsuggestion);
                break;
            case R.id.ll_bday:
                Intent intentbday = new Intent(getContext(), FrameActivity.class);
                intentbday.putExtra("frag_name", "FragmentNewJoinee");
                intentbday.putExtra("frag_tag", "newjoinee");
                intentbday.putExtra("title", "Birthday");
                intentbday.putExtra("type", "Bday");
                startActivity(intentbday);
                break;
        }
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        try {
            if (Tag == GET_PENDING_DATA_DASHBOARD_TAG) {
                if (call.getString("Message").equalsIgnoreCase("Success")) {
                    JSONArray jsonArray = call.getJSONArray("DashboardList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (Integer.parseInt(jsonObject.getString("PendingCount")) > 0) {
                            tv_pending.setTextColor(getResources().getColor(R.color.red));
                            iv_pending.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                        } else {
                            tv_pending.setTextColor(getResources().getColor(R.color.colorPrimary));
                            iv_pending.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                    }
                }
            } else if (Tag == SEND_TOKEN_TAG) {
                //Toast.makeText(getContext(), "Response : " + call.toString(), Toast.LENGTH_SHORT).show();
            } else if (Tag == GET_NOTIFICATION_LIST_TAG) {
                if (call.getString("Message").equalsIgnoreCase("Success")) {
                    final String Count = call.getString("TotalCount");
                    ((MainActivity) getActivity()).updateApi(Count, new NotificationCount() {
                        @Override
                        public void notificationCount(String count) {
                            if (notificationCount != null) {
                                notificationCount.notificationCount(Count);
                            }
                        }
                    });
                } else {
                    ((MainActivity) getActivity()).updateApi("0", new NotificationCount() {
                        @Override
                        public void notificationCount(String count) {
                            if (notificationCount != null) {
                                notificationCount.notificationCount("0");
                            }
                        }
                    });
                }
            }else if (Tag == LOG_OUT_TAG){
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.netcommlabs.sarofficenet"));
                startActivity(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        Toast.makeText(getContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();
    }
}


