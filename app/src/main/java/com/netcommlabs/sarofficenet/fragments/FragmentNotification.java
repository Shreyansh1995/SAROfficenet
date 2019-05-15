package com.netcommlabs.sarofficenet.fragments;


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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.adapter.NotificationAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.NotificationModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.DELETE_NOTIFICATION_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_NOTIFICATION_LIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.MARK_NOTIFICATION_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.MY_TEAM_LIST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotification extends Fragment implements ResponseListener {
    private RecyclerView recyclerView;
    private ArrayList<NotificationModel> notificationModelArrayList;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private NotificationAdapter notificationAdapter;
    private MySharedPreference mySharedPreference;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
                clearNotification();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_notification, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
       // setHasOptionsMenu(true);
        findViewByid(v);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //clearNotification();
    }

    private void findViewByid(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        notificationModelArrayList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationModelArrayList, getContext(), getActivity(), new NotificationAdapter.checkNotification() {
            @Override
            public void read(String id) {
                hitApiIfRead(id);
            }
        });

        if (NetworkUtils.isConnected(activity)) {
            hitApi();
        } else {
            Toast.makeText(activity, "Please check your intenet connection!", Toast.LENGTH_SHORT).show();
        }

    }


    private void hitApi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), UrlConstants.GET_NOTIFICATION_LIST, this, GET_NOTIFICATION_LIST_TAG);
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
            LogUtils.showLog("request :", "Tag :" + GET_NOTIFICATION_LIST_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void hitApiIfRead(String id) {
        try {
            request = new ProjectWebRequest(activity, getReadParam(id), UrlConstants.MARK_NOTIFICATION, this, MARK_NOTIFICATION_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getReadParam(String id) {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("NotificatioID", id);
            LogUtils.showLog("request :", "Tag :" + GET_NOTIFICATION_LIST_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    private void clearNotification() {
        try {
            request = new ProjectWebRequest(activity, getdeleteParam(), UrlConstants.DELETE_NOTIFICATION, this, DELETE_NOTIFICATION_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getdeleteParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", mySharedPreference.getUid());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        try {
            if (Tag == GET_NOTIFICATION_LIST_TAG) {

                if (call.getString("Message").equalsIgnoreCase("Success")) {
                    JSONArray jsonArray = call.getJSONArray("PushNotificationList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NotificationModel notificationModel = new NotificationModel();
                        notificationModel.setTitle(jsonObject.getString("Message"));
                        notificationModel.setId(jsonObject.getString("NotificationID"));
                        notificationModel.setIfRead(jsonObject.getString("IsRead"));
                        notificationModel.setDateTime(jsonObject.getString("DateTime"));
                        notificationModel.setModuleID(jsonObject.getString("ModuleID"));
                        notificationModelArrayList.add(notificationModel);
                    }
                    recyclerView.setAdapter(notificationAdapter);
                    notificationAdapter.notifyDataSetChanged();
                    clearNotification();
                } else {
                    if (notificationModelArrayList.size() > 0) {
                        notificationModelArrayList.clear();
                    }
                    notificationAdapter = new NotificationAdapter(notificationModelArrayList, getContext(), getActivity(), new NotificationAdapter.checkNotification() {
                        @Override
                        public void read(String id) {
                            hitApiIfRead(id);
                        }
                    });
                    AppAlertDialog.showDialogSelfFinish(activity, "", call.optString("Message"));
                }

            } else if (Tag == MARK_NOTIFICATION_TAG) {
                // Toast.makeText(activity, "" + call.toString(), Toast.LENGTH_SHORT).show();

            } else if (Tag == DELETE_NOTIFICATION_TAG) {


               /* if (call.getString("Message").equalsIgnoreCase("Success")) {
                    if (notificationModelArrayList.size() > 0) {
                        notificationModelArrayList.clear();
                    }
                    notificationAdapter = new NotificationAdapter(notificationModelArrayList, getContext(), getActivity(), new NotificationAdapter.checkNotification() {
                        @Override
                        public void read(String id) {
                           // hitApiIfRead(id);
                        }
                    });
                    recyclerView.setAdapter(notificationAdapter);
                    notificationAdapter.notifyDataSetChanged();
                }else {
                    if (notificationModelArrayList.size() > 0) {
                        notificationModelArrayList.clear();
                    }
                    notificationAdapter = new NotificationAdapter(notificationModelArrayList, getContext(), getActivity(), new NotificationAdapter.checkNotification() {
                        @Override
                        public void read(String id) {
                            hitApiIfRead(id);
                        }
                    });
                    recyclerView.setAdapter(notificationAdapter);
                    notificationAdapter.notifyDataSetChanged();
                }*/

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
