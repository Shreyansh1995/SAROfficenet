package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.content.Intent;
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
import com.netcommlabs.sarofficenet.adapter.NotesAdapter;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.model.NotesModel;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.DELETE_NOTES_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.MY_TEAM_LIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.NOTES_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotes extends Fragment implements ResponseListener {
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    private RecyclerView recyclerView;
    private ArrayList<NotesModel> notesModelArrayList;
    private NotesAdapter notesAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_note, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                Intent intentNotes= new Intent(getContext(), FrameActivity.class);
                intentNotes.putExtra("frag_name", "FragmentAddNotes");
                intentNotes.putExtra("frag_tag", "addnotes");
                intentNotes.putExtra("title", "Add Note");
                startActivity(intentNotes);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_notes, container, false);
        setHasOptionsMenu(true);
        mySharedPreference = MySharedPreference.getInstance(activity);
        findViewByid(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetworkUtils.isConnected(activity)) {
            hitApi();
        } else {
            Toast.makeText(activity, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void findViewByid(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        notesModelArrayList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesModelArrayList, getContext(), getActivity(), new NotesAdapter.RemoveNotes() {
            @Override
            public void onDeleteClick(String NotesID) {
                deleteNote(NotesID);
            }
        });

    }

    private void deleteNote(String notesID) {
        try {
            request = new ProjectWebRequest(activity, getdeleteParam(notesID), UrlConstants.DELETE_NOTES, this, DELETE_NOTES_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getdeleteParam(String notesID) {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("NotesID", notesID);
        } catch (Exception e) {
        }
        return object;
    }

    private void hitApi() {
        try {
            request = new ProjectWebRequest(activity, getParam(), UrlConstants.NOTES, this, NOTES_TAG);
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
            LogUtils.showLog("request :", "Tag :" + NOTES_TAG + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        try {
            if (Tag == NOTES_TAG) {
                if (call.optString("Message").equalsIgnoreCase("Success")) {
                    if (notesModelArrayList.size()>0){
                        notesModelArrayList.clear();
                    }
                    JSONArray jsonArray = call.getJSONArray("DailyNotesList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NotesModel notesModel = new NotesModel();
                        notesModel.setDate(jsonObject.getString("Date"));
                        notesModel.setFullNotes(jsonObject.getString("FullNotes"));
                        notesModel.setNotesID(jsonObject.getString("NotesID"));
                        notesModel.setShortNotes(jsonObject.getString("ShortNotes"));
                        notesModelArrayList.add(notesModel);
                    }
                    recyclerView.setAdapter(notesAdapter);
                    notesAdapter.notifyDataSetChanged();

                }else {
                    AppAlertDialog.showDialogSelfFinish(activity, "", call.optString("Message"));
                }
            }else if (Tag == DELETE_NOTES_TAG){
                if (call.getString("Message").equalsIgnoreCase("Success")){
                    Toast.makeText(activity, "Deleted Successfully.", Toast.LENGTH_SHORT).show();
                }
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
