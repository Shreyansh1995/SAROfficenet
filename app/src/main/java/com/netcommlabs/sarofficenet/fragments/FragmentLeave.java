package com.netcommlabs.sarofficenet.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLeave extends Fragment implements View.OnClickListener {
    private LinearLayout ll_newreq, ll_reqdetails, ll_pendingreq, ll_archivereq,ll_canceled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_leave, container, false);
        ll_newreq = v.findViewById(R.id.ll_newreq);
        ll_reqdetails = v.findViewById(R.id.ll_reqdetails);
        ll_pendingreq = v.findViewById(R.id.ll_pendingreq);
        ll_archivereq = v.findViewById(R.id.ll_archivereq);
        ll_canceled = v.findViewById(R.id.ll_canceled);
        ll_newreq.setOnClickListener(this);
        ll_reqdetails.setOnClickListener(this);
        ll_pendingreq.setOnClickListener(this);
        ll_archivereq.setOnClickListener(this);
        ll_canceled.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.ll_newreq:
                Intent intent1 = new Intent(getContext(), FrameActivity.class);
                intent1.putExtra("frag_name", "FragmentLeaveTab");
                intent1.putExtra("frag_tag", "leave form");
                intent1.putExtra("title", "Leave");
                intent1.putExtra("tab", "0");
                startActivity(intent1);
                break;
            case R.id.ll_reqdetails:
                Intent intent= new Intent(getContext(), FrameActivity.class);
                intent.putExtra("frag_name", "FragmentLeaveTab");
                intent.putExtra("frag_tag", "leave request");
                intent.putExtra("title", "Leave");
                intent.putExtra("tab", "1");
                startActivity(intent);
                break;
            case R.id.ll_pendingreq:
                Intent intentpending= new Intent(getContext(), FrameActivity.class);
                intentpending.putExtra("frag_name", "FragmentLeaveTab");
                intentpending.putExtra("frag_tag", "leave pending request");
                intentpending.putExtra("title", "Leave");
                intentpending.putExtra("tab", "2");
                startActivity(intentpending);
                break;
            case R.id.ll_archivereq:
                Intent intentarchive= new Intent(getContext(), FrameActivity.class);
                intentarchive.putExtra("frag_name", "FragmentLeaveTab");
                intentarchive.putExtra("frag_tag", "leave archive request");
                intentarchive.putExtra("title", "Leave");
                intentarchive.putExtra("tab", "3");
                startActivity(intentarchive);
                break;
            case R.id.ll_canceled:
                Intent intentcancled= new Intent(getContext(), FrameActivity.class);
                intentcancled.putExtra("frag_name", "FragmentLeaveTab");
                intentcancled.putExtra("frag_tag", "leave archive request");
                intentcancled.putExtra("title", "Leave");
                intentcancled.putExtra("tab", "4");
                startActivity(intentcancled);
                break;
        }
    }

}
