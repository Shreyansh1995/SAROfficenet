package com.netcommlabs.sarofficenet.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.FilePath;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.MySpannable;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUBMIT_STATUS;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUBMIT_STATUS_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_DETAIL;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_DETAIL_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.TICKET_LIST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHelpDeskDetail extends Fragment implements ResponseListener {
    private TextView tv_num, tv_category, tv_subcategory, tv_contact, tv_submitby, tv_submiton, tv_raisedissue, tv_doc, tvstatus;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    private String ReqID = "";
    private String fileName, filePath;
    private TextView tv_adminstatus, tv_actionby, tv_actiondate, tv_adminremark, tv_attach;
    private LinearLayout ll_admin, llradio;
    private View view;
    private RadioButton rb_satisfied, rb_unsatisfied;
    private Button btn_submit;
    final int ACTIVITY_CHOOSE_FILE = 7;
    private String extension = "", encodedResume = "", fileNameLocal = "";
    private Bitmap bitmap;
    private EditText et_issue;
    private String Remark = "", IsSatisfied = "0";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help_desk_detail, container, false);
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
        tvstatus = v.findViewById(R.id.tvstatus);

        tv_adminstatus = v.findViewById(R.id.tv_adminstatus);
        tv_actionby = v.findViewById(R.id.tv_actionby);
        tv_actiondate = v.findViewById(R.id.tv_actiondate);
        tv_adminremark = v.findViewById(R.id.tv_adminremark);
        tv_attach = v.findViewById(R.id.tv_attach);


        et_issue = v.findViewById(R.id.et_issue);

        ll_admin = v.findViewById(R.id.ll_admin);
        llradio = v.findViewById(R.id.llradio);
        //  llunsat = v.findViewById(R.id.llunsat);
        rb_satisfied = v.findViewById(R.id.rb_satisfied);
        rb_unsatisfied = v.findViewById(R.id.rb_unsatisfied);
        btn_submit = v.findViewById(R.id.btn_submit);
        view = v.findViewById(R.id.view);

        ReqID = getArguments().getString("ReqId");

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

        tv_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri photoURI = FileProvider.getUriForFile(activity,
                            "com.netcommlabs.sarofficenet.fileprovider", createImageFile());

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent.setType("*/*");
                    startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        rb_satisfied.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rb_unsatisfied.setChecked(false);
                    IsSatisfied = "0";
                    tv_attach.setVisibility(View.GONE);
                }
            }
        });
        rb_unsatisfied.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rb_satisfied.setChecked(false);
                    IsSatisfied = "1";
                    tv_attach.setVisibility(View.VISIBLE);
                }
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(et_issue.getText().toString())) {
                    Remark = et_issue.getText().toString();
                }
                submitReview();
            }
        });


    }

   /* public void seeMoreText(final String myReallyLongText) {
        tv_raisedissue.post(new Runnable() {
            @Override
            public void run() {
                // Past the maximum number of lines we want to display.
                if (tv_raisedissue.getLineCount() > MAX_LINES) {
                    int lastCharShown = tv_raisedissue.getLayout().getLineVisibleEnd(MAX_LINES - 1);

                    tv_raisedissue.setMaxLines(MAX_LINES);

                    String moreString = "see more";
                    String suffix = TWO_SPACES + moreString;

                    // 3 is a "magic number" but it's just basically the length of the ellipsis we're going to insert
                    String actionDisplayText = myReallyLongText.substring(0, lastCharShown - suffix.length() - 3) + "..." + suffix;

                    SpannableString truncatedSpannableString = new SpannableString(actionDisplayText);
                    int startIndex = actionDisplayText.indexOf(moreString);
                    truncatedSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.black)), startIndex, startIndex + moreString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_raisedissue.setText(truncatedSpannableString);
                }
            }
        });
    }*/
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
            ssb.setSpan(new MySpannable(false){
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


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }

    private void hitApi() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", ReqID);
            request = new ProjectWebRequest(activity, object, TICKET_LIST_DETAIL, this, TICKET_LIST_DETAIL_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void submitReview() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", mySharedPreference.getUid());
            object.put("ReqID", ReqID);
            object.put("Remarks", Remark);
            object.put("IsSatisfied", IsSatisfied);
            object.put("FileInBase64", encodedResume);
            object.put("FileExt", extension);
            object.put("FileName", fileNameLocal);
            request = new ProjectWebRequest(activity, object, SUBMIT_STATUS, this, SUBMIT_STATUS_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }
    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        if (Tag == TICKET_LIST_DETAIL_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    tv_num.setText(call.getString("TicketNo"));
                    tv_category.setText(call.getString("Category"));
                    tv_subcategory.setText(call.getString("SubCategory"));
                    tv_contact.setText(call.getString("ContactNo"));
                    tv_submitby.setText(call.getString("SubmittedBy"));
                    tv_submiton.setText(call.getString("SubmittedOn"));
                    tv_raisedissue.setText(call.getString("RaisedIssue"));
                    if (!call.getString("AttachedDocumentPath").equalsIgnoreCase("null")) {
                        filePath = call.getString("AttachedDocumentPath");
                        fileName = filePath.substring(filePath.lastIndexOf("/"));
                        fileName = fileName.replace("/", "");
                        tv_doc.setText(fileName);
                    }

                    if (!call.getString("RaisedIssue").equalsIgnoreCase("null")) {
                        makeTextViewResizable(tv_raisedissue, 3, "See More", true);
                    }

                    if (call.getString("AdminStatus").equalsIgnoreCase("Solved")) {
                        ll_admin.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);
                        llradio.setVisibility(View.VISIBLE);
                        et_issue.setVisibility(View.VISIBLE);
                        btn_submit.setVisibility(View.VISIBLE);
                        tvstatus.setVisibility(View.VISIBLE);

                        tv_adminstatus.setText(call.getString("AdminStatus"));
                        tv_actionby.setText(call.getString("AdminActionBy"));
                        tv_actiondate.setText(call.getString("AdminActionDate"));
                        tv_adminremark.setText(call.getString("AdminRemarks"));
                    }

                    if (call.getString("AdminStatus").equalsIgnoreCase("Onhold")) {
                        ll_admin.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);
                        tvstatus.setVisibility(View.VISIBLE);
                        tv_adminstatus.setText(call.getString("AdminStatus"));
                        tv_actionby.setText(call.getString("AdminActionBy"));
                        tv_actiondate.setText(call.getString("AdminActionDate"));
                        tv_adminremark.setText(call.getString("AdminRemarks"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Tag == SUBMIT_STATUS_TAG) {
            try {
                AppAlertDialog.showDialogFinishWithActivity(activity, "", call.getString("Message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        if (requestCode == ACTIVITY_CHOOSE_FILE && resultCode == Activity.RESULT_OK) {
            try {
                //String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                //File myFile = new File(path);
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                Uri urii = data.getData();
                String path = FilePath.getPath(getContext(), urii);
                File myFile = new File(path);
                fileNameLocal = path.substring(path.lastIndexOf("/"));
                extension = path.substring(path.lastIndexOf("."));
                if (extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png")) {
                    encodedResume = FilePath.encodeImage(path);
                } else if (extension.equals(".pdf") || extension.equals(".doc") || extension.equals(".docx")) {
                    encodedResume = ConvertFile(myFile);
                } else {
                    Toast.makeText(getContext(), "Unsupported media", Toast.LENGTH_SHORT).show();
                }
                fileNameLocal = fileNameLocal.replace("/", "");
                tv_attach.setText(fileNameLocal);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else {
            // Toast.makeText(getContext(), "error in image loading", Toast.LENGTH_LONG).show();
        }
    }

    public static String ConvertFile(File file) {

        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            for (int j = 0; j < b.length; j++) {
                System.out.print((char) b[j]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }

        byte[] byteFileArray = new byte[0];
        try {
            byteFileArray = FileUtils.readFileToByteArray(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String base64String = "";
        if (byteFileArray.length > 0) {
            base64String = android.util.Base64.encodeToString(byteFileArray, android.util.Base64.NO_WRAP);
            Log.i("File Base64 string", "IMAGE PARSE ==>" + base64String);
        }
        return base64String;
    }
}
