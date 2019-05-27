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
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.MySpannable;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.ADMIN_VIEW_DETAILS;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.ADMIN_VIEW_DETAILS_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_CATEGORYLIST;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_CATEGORYLIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_DEPARTMENT;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_DEPARTMENT_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_SUBCATEGORYLIST;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_SUBCATEGORYLIST_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAdminHelpDeskDetail extends Fragment implements View.OnClickListener, ResponseListener, AdapterView.OnItemSelectedListener {
    private TextView tv_num, tv_category, tv_subcategory, tv_contact, tv_submitby, tv_submiton, tv_raisedissue, tv_attach, tv_doc, tvstatus;
    private EditText et_issue, et_remark;
    private Button btn_onhold, btn_reassign, btn_solved, btn_submit;
    private String ReqNo;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    final int ACTIVITY_CHOOSE_FILE = 7;
    private String extension = "", encodedResume = "", FileName = "";
    private Bitmap bitmap;
    private LinearLayout ll, llbtn, ll_admin;
    private ArrayList<String> DepartmentName, DepartmentID;
    private ArrayList<String> CategoryName, CategoryID;
    private ArrayList<String> SubCategoryName, SubCategoryID;
    private Spinner spnr_department, spnr_category, spnr_subcategory;
    private String ButtonURL, Solution = "", Remark = "";
    private int ButtonTag;
    private String fileName, filePath, fileNameAdmin, filePathAdmin, status;
    private TextView tv_adminstatus, tv_actionby, tv_actiondate, tv_adminremark, tv_attachadmin;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_help_desk_detail, container, false);
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
        tv_attach = v.findViewById(R.id.tv_attach);
        tv_doc = v.findViewById(R.id.tv_doc);
        btn_onhold = v.findViewById(R.id.btn_onhold);
        btn_reassign = v.findViewById(R.id.btn_reassign);
        btn_solved = v.findViewById(R.id.btn_solved);
        btn_submit = v.findViewById(R.id.btn_submit);

        et_issue = v.findViewById(R.id.et_issue);
        et_remark = v.findViewById(R.id.et_remark);

        spnr_department = v.findViewById(R.id.spnr_department);
        spnr_category = v.findViewById(R.id.spnr_category);
        spnr_subcategory = v.findViewById(R.id.spnr_subcategory);

        ll = v.findViewById(R.id.ll);
        llbtn = v.findViewById(R.id.llbtn);
        ll_admin = v.findViewById(R.id.ll_admin);
        tvstatus = v.findViewById(R.id.tvstatus);

        tv_adminstatus = v.findViewById(R.id.tv_adminstatus);
        tv_actionby = v.findViewById(R.id.tv_actionby);
        tv_actiondate = v.findViewById(R.id.tv_actiondate);
        tv_adminremark = v.findViewById(R.id.tv_adminremark);
        tv_attachadmin = v.findViewById(R.id.tv_attachadmin);

        DepartmentName = new ArrayList<String>();
        DepartmentID = new ArrayList<String>();
        CategoryID = new ArrayList<String>();
        CategoryName = new ArrayList<String>();
        SubCategoryName = new ArrayList<String>();
        SubCategoryID = new ArrayList<String>();

        ReqNo = getArguments().getString("ReqId");
       /* tv_num.setText(getArguments().getString("Ticket"));
        tv_category.setText(getArguments().getString("Category"));
        tv_subcategory.setText(getArguments().getString("SubCategory"));
        tv_contact.setText(getArguments().getString("Mobile"));
        tv_submitby.setText(getArguments().getString("Submitby"));
        tv_submiton.setText(getArguments().getString("Submitdate"));
        tv_raisedissue.setText(getArguments().getString("issue"));
        status = getArguments().getString("status");

        if (status.equalsIgnoreCase("Solved")) {
            llbtn.setVisibility(View.GONE);
        }

        if (!getArguments().getString("issue").equalsIgnoreCase("null")) {
            makeTextViewResizable(tv_raisedissue, 3, "See More", true);
        }*/


        tv_attach.setOnClickListener(this);
        btn_onhold.setOnClickListener(this);
        btn_reassign.setOnClickListener(this);
        btn_solved.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        spnr_department.setOnItemSelectedListener(this);
        spnr_category.setOnItemSelectedListener(this);

        getDepartment();
        getDetails();


        tv_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(filePath));
                startActivity(browserIntent);
            }
        });
    }

    private void getDetails() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", ReqNo);
            request = new ProjectWebRequest(activity, object, ADMIN_VIEW_DETAILS, this, ADMIN_VIEW_DETAILS_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void getDepartment() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            request = new ProjectWebRequest(activity, object, GET_DEPARTMENT, this, GET_DEPARTMENT_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void getCategory(String departmentID) {

        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("DepartmentID", departmentID);
            request = new ProjectWebRequest(activity, object, GET_CATEGORYLIST, this, GET_CATEGORYLIST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void getSubCategory(String CategoryID) {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("CategoryID", CategoryID);
            object.put("PlantID", mySharedPreference.getPlantid());
            request = new ProjectWebRequest(activity, object, GET_SUBCATEGORYLIST, this, GET_SUBCATEGORYLIST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void hitApiforSubmit() {

        if (!encodedResume.equalsIgnoreCase("")) {
            FileName = "FileName_" + mySharedPreference.getUid();
        }
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", ReqNo);
            object.put("UserID", mySharedPreference.getUid());
            object.put("Solution", Solution);
            object.put("FileInBase64", encodedResume);
            object.put("FileExt", extension);
            object.put("FileName", FileName);
            request = new ProjectWebRequest(activity, object, ButtonURL, this, ButtonTag);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void hitApiforReassign() {
        if (!encodedResume.equalsIgnoreCase("")) {
            FileName = "FileName_" + mySharedPreference.getUid();
        }
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", ReqNo);
            object.put("UserID", mySharedPreference.getUid());
            object.put("Issues", Remark);
            object.put("DepartmentID", DepartmentID.get(spnr_department.getSelectedItemPosition()));
            object.put("CategoryID", CategoryID.get(spnr_category.getSelectedItemPosition()));
            object.put("SubCategoryID", SubCategoryID.get(spnr_subcategory.getSelectedItemPosition()));
            request = new ProjectWebRequest(activity, object, ButtonURL, this, ButtonTag);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_attach:
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
                break;
            case R.id.btn_reassign:
                ll.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_submit:
                ButtonURL = UrlConstants.REASSIGN;
                ButtonTag = UrlConstants.REASSIGNED_TAG;
                if (!TextUtils.isEmpty(et_remark.getText().toString())) {
                    Remark = et_remark.getText().toString();
                }
                if (DepartmentID.get(spnr_department.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    Toast.makeText(activity, "Please select department", Toast.LENGTH_SHORT).show();
                } else if (CategoryID.get(spnr_category.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    Toast.makeText(activity, "Please select category", Toast.LENGTH_SHORT).show();
                } else if (SubCategoryID.get(spnr_subcategory.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    Toast.makeText(activity, "Please select sub category", Toast.LENGTH_SHORT).show();
                } else {
                    hitApiforReassign();
                }

                break;
            case R.id.btn_onhold:
                ButtonURL = UrlConstants.HOLD;
                ButtonTag = UrlConstants.HOLD_TAG;
                if (!TextUtils.isEmpty(et_issue.getText().toString())) {
                    Solution = et_issue.getText().toString();
                }
                hitApiforSubmit();
                break;
            case R.id.btn_solved:
                ButtonURL = UrlConstants.SOLVED;
                ButtonTag = UrlConstants.SOLVED_TAG;
                if (!TextUtils.isEmpty(et_issue.getText().toString())) {
                    Solution = et_issue.getText().toString();
                }
                hitApiforSubmit();
                break;

        }
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultcode, Intent data) {
        super.onActivityResult(requestCode, resultcode, data);

        InputStream stream = null;
        if (requestCode == ACTIVITY_CHOOSE_FILE && resultcode == Activity.RESULT_OK) {
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
                extension = path.substring(path.lastIndexOf("."));
                if (extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png")) {
                    encodedResume = FilePath.encodeImage(path);
                } else if (extension.equals(".pdf") || extension.equals(".doc") || extension.equals(".docx")) {
                    encodedResume = ConvertFile(myFile);
                } else {
                    Toast.makeText(getContext(), "Unsupported media", Toast.LENGTH_SHORT).show();
                }
                tv_attach.setText("Attached");

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

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        if (Tag == GET_DEPARTMENT_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {

                    if (DepartmentID.size() > 0) {
                        DepartmentID.clear();
                        DepartmentName.clear();
                    }
                    DepartmentID.add("-1");
                    DepartmentName.add("-Select Department-");
                    JSONArray jsonArray = call.getJSONArray("HelpDeskDepartmentList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DepartmentID.add(jsonObject.getString("DepartmentID"));
                        DepartmentName.add(jsonObject.getString("DepartmentName"));
                    }
                    spnr_department.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, DepartmentName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Tag == GET_CATEGORYLIST_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    if (CategoryID.size() > 0) {
                        CategoryID.clear();
                        CategoryName.clear();
                    }
                    CategoryID.add("-1");
                    CategoryName.add("-Select Category-");
                    JSONArray jsonArray = call.getJSONArray("HelpDeskCategoryList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CategoryID.add(jsonObject.getString("CategoryID"));
                        CategoryName.add(jsonObject.getString("CategoryName"));
                    }
                    spnr_category.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, CategoryName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Tag == GET_SUBCATEGORYLIST_TAG) {
            LogUtils.showLog("Department list : ", "" + call.toString());
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    if (SubCategoryID.size() > 0) {
                        SubCategoryID.clear();
                        SubCategoryName.clear();
                    }
                    SubCategoryID.add("-1");
                    SubCategoryName.add("-Select Sub Category-");
                    JSONArray jsonArray = call.getJSONArray("HelpDeskSubCategoryList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SubCategoryID.add(jsonObject.getString("SubCategoryID"));
                        SubCategoryName.add(jsonObject.getString("SubCategoryName"));
                    }
                    spnr_subcategory.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, SubCategoryName));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Tag == ButtonTag) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    AppAlertDialog.showDialogFinishWithActivity(activity, "", call.getString("Message"));
                } else {
                    AppAlertDialog.showDialogFinishWithActivity(activity, "", call.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (Tag == ADMIN_VIEW_DETAILS_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    tv_num.setText(call.getString("TicketNo"));
                    tv_category.setText(call.getString("Category"));
                    tv_subcategory.setText(call.getString("SubCategory"));
                    tv_contact.setText(call.getString("MobileNo"));
                    tv_submitby.setText(call.getString("SubmittedBy"));
                    tv_submiton.setText(call.getString("ReqDate"));
                    tv_raisedissue.setText(call.getString("Issues"));

                    if (!call.getString("AttachedDocumentPath").equalsIgnoreCase("null")) {
                        filePath = call.getString("AttachedDocumentPath");
                        fileName = filePath.substring(filePath.lastIndexOf("/"));
                        fileName = fileName.replace("/", "");
                        tv_doc.setText(fileName);
                    } else {
                        //tv_doc.setText("Not available");
                    }

                    if (!call.getString("Issues").equalsIgnoreCase("null")) {
                        makeTextViewResizable(tv_raisedissue, 3, "See More", true);
                    }

                    if (call.getString("AdminStatus").equalsIgnoreCase("Solved") || call.getString("AdminStatus").equalsIgnoreCase("Closed")) {
                        ll_admin.setVisibility(View.VISIBLE);
                        tvstatus.setVisibility(View.VISIBLE);
                        llbtn.setVisibility(View.GONE);

                        tv_adminstatus.setText(call.getString("AdminStatus"));
                        tv_actionby.setText(call.getString("AdminActionBy"));
                        tv_actiondate.setText(call.getString("AdminActionDate"));
                        tv_adminremark.setText(call.getString("AdminRemarks"));

                        if (!call.getString("AdminAttachedDocumentPath").equalsIgnoreCase("null")) {
                            filePath = call.getString("AdminAttachedDocumentPath");
                            fileNameAdmin = filePath.substring(filePath.lastIndexOf("/"));
                            fileNameAdmin = fileName.replace("/", "");
                            tv_attachadmin.setText(fileName);
                        } else {
                            //tv_attachadmin.setText("Not available");
                        }
                    } else if (call.getString("AdminStatus").equalsIgnoreCase("Onhold")) {
                        ll_admin.setVisibility(View.VISIBLE);
                        tvstatus.setVisibility(View.VISIBLE);
                        tv_adminstatus.setText(call.getString("AdminStatus"));
                        tv_actionby.setText(call.getString("AdminActionBy"));
                        tv_actiondate.setText(call.getString("AdminActionDate"));
                        tv_adminremark.setText(call.getString("AdminRemarks"));
                        //tv_attachadmin.setText("Not available");
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spnr_department:
                if (!DepartmentID.get(spnr_department.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    getCategory(DepartmentID.get(spnr_department.getSelectedItemPosition()));
                }
                break;
            case R.id.spnr_category:
                if (!CategoryID.get(spnr_category.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    getSubCategory(CategoryID.get(spnr_category.getSelectedItemPosition()));
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
