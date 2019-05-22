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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView tv_num, tv_category, tv_subcategory, tv_contact, tv_submitby, tv_submiton, tv_raisedissue, tv_attach;
    private EditText et_issue,et_remark;
    private Button btn_onhold, btn_reassign, btn_solved, btn_submit;
    private String ReqNo;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    final int ACTIVITY_CHOOSE_FILE = 7;
    private String extension = "", encodedResume = "";
    private Bitmap bitmap;
    private LinearLayout ll;
    private ArrayList<String> DepartmentName, DepartmentID;
    private ArrayList<String> CategoryName, CategoryID;
    private ArrayList<String> SubCategoryName, SubCategoryID;
    private Spinner spnr_department, spnr_category, spnr_subcategory;
    private String ButtonURL, Solution = "";
    private int ButtonTag;


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

        DepartmentName = new ArrayList<String>();
        DepartmentID = new ArrayList<String>();
        CategoryID = new ArrayList<String>();
        CategoryName = new ArrayList<String>();
        SubCategoryName = new ArrayList<String>();
        SubCategoryID = new ArrayList<String>();

        ReqNo = getArguments().getString("ReqId");
        tv_num.setText(getArguments().getString("Ticket"));
        tv_category.setText(getArguments().getString("Category"));
        tv_subcategory.setText(getArguments().getString("SubCategory"));
        tv_contact.setText(getArguments().getString("Mobile"));
        tv_submitby.setText(getArguments().getString("Submitby"));
        tv_submiton.setText(getArguments().getString("Submitdate"));
        tv_raisedissue.setText(getArguments().getString("issue"));

        tv_attach.setOnClickListener(this);
        btn_onhold.setOnClickListener(this);
        btn_reassign.setOnClickListener(this);
        btn_solved.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        spnr_department.setOnItemSelectedListener(this);
        spnr_category.setOnItemSelectedListener(this);

        getDepartment();
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
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("ReqID", ReqNo);
            object.put("UserID", mySharedPreference.getUid());
            object.put("Solution", Solution);
            object.put("FileInBase64", encodedResume);
            object.put("FileExt", extension);
            object.put("FileName", "FileName_" + mySharedPreference.getUid());
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
                if (!TextUtils.isEmpty(et_issue.getText().toString())) {
                    Solution = et_issue.getText().toString();
                }
                hitApiforSubmit();
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
        }else if (Tag == ButtonTag){
            if (call.optString("Status").equalsIgnoreCase("true")){
                try {
                    AppAlertDialog.showDialogFinishWithActivity(activity,"",call.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {

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
}
