package com.netcommlabs.sarofficenet.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.MySpannable;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONObject;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.REF_TICKET_DETAILS;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.REF_TICKET_DETAILS_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_DETAIL;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_DETAIL_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRefDetails extends Fragment implements ResponseListener {
    private TextView tv_num, tv_category, tv_subcategory, tv_contact, tv_submitby, tv_submiton, tv_raisedissue, tv_doc;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    private String RefTicketNo = "";
    private String fileName, filePath;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ref_details, container, false);
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
        tv_doc = v.findViewById(R.id.tv_doc);
        RefTicketNo = getArguments().getString("RefTicketNo");

        if (NetworkUtils.isConnected(activity)) {
            hitApi();
        } else {
            Toast.makeText(activity, "Please Check your internet connection!", Toast.LENGTH_SHORT).show();
        }

        tv_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(filePath));
                startActivity(browserIntent);
            }
        });
    }

    private void hitApi() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("RefTicketNo", RefTicketNo);
            request = new ProjectWebRequest(activity, object, REF_TICKET_DETAILS, this, REF_TICKET_DETAILS_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }


    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        if (Tag == REF_TICKET_DETAILS_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    tv_num.setText(call.getString("RefTicketNo"));
                    tv_category.setText(call.getString("Category"));
                    tv_subcategory.setText(call.getString("SubCategory"));
                    tv_contact.setText(call.getString("MobileNo"));
                    tv_submitby.setText(call.getString("SubmittedBy"));
                    tv_submiton.setText(call.getString("ReqDate"));
                        tv_raisedissue.setText(call.getString("Issues"));
                    // tv_doc.setText(call.getString("Issues"));

                    if (!call.getString("Issues").equalsIgnoreCase("null")) {
                        makeTextViewResizable(tv_raisedissue, 3, "See More", true);
                    }

                    if (!call.getString("AttachmentPath").equalsIgnoreCase("null")) {
                        filePath = call.getString("AttachmentPath");
                        fileName = filePath.substring(filePath.lastIndexOf("/"));
                        fileName = fileName.replace("/", "");
                        tv_doc.setText(fileName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
    }
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }
    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
}
