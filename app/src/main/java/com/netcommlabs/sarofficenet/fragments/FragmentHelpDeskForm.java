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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUBMITHELPDESK;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUBMITHELPDESK_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUGGESTIONWITHIDENTITY_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.VALIDATEREF;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.VALIDATEREF_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHelpDeskForm extends Fragment implements View.OnClickListener, ResponseListener, AdapterView.OnItemSelectedListener {

    private Spinner spnr_department, spnr_category, spnr_subcategory;
    private EditText et_contact, et_refnumber, et_issue;
    private TextView tv_attach;
    private Button btn_submit;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    final int ACTIVITY_CHOOSE_FILE = 7;
    private String extension = "", encodedResume = "";
    private Bitmap bitmap;
    private ArrayList<String> DepartmentName, DepartmentID;
    private ArrayList<String> CategoryName, CategoryID;
    private ArrayList<String> SubCategoryName, SubCategoryID;
    private String TicketRefNo = "", MobileNo = "";
    private LinearLayout ll;
    private boolean focus = false;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help_desk_form, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
        init(v);
        return v;
    }

    private void init(View v) {
        spnr_department = v.findViewById(R.id.spnr_department);
        spnr_category = v.findViewById(R.id.spnr_category);
        spnr_subcategory = v.findViewById(R.id.spnr_subcategory);
        et_contact = v.findViewById(R.id.et_contact);
        et_refnumber = v.findViewById(R.id.et_refnumber);
        et_issue = v.findViewById(R.id.et_issue);
        tv_attach = v.findViewById(R.id.tv_attach);
        btn_submit = v.findViewById(R.id.btn_submit);
        ll = v.findViewById(R.id.ll);
        ll.setOnClickListener(this);

        tv_attach.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        spnr_department.setOnItemSelectedListener(this);
        spnr_category.setOnItemSelectedListener(this);
        spnr_subcategory.setOnItemSelectedListener(this);

        et_contact.setText(mySharedPreference.getMobile());

        DepartmentName = new ArrayList<String>();
        DepartmentID = new ArrayList<String>();
        CategoryID = new ArrayList<String>();
        CategoryName = new ArrayList<String>();
        SubCategoryName = new ArrayList<String>();
        SubCategoryID = new ArrayList<String>();

       /* et_refnumber.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    checkRefNum(et_refnumber.getText().toString());
                    return true;
                } else {
                    return false;
                }
            }
        });*/

        et_refnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (focus) {
                    checkRefNum(et_refnumber.getText().toString());
                }
                focus = true;
            }
        });


        getDepartment();

    }

    private void checkRefNum(String refNum) {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("TicketRefNo", refNum);
            object.put("UserID", mySharedPreference.getUid());
            request = new ProjectWebRequest(activity, object, VALIDATEREF, this, VALIDATEREF_TAG);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
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
            case R.id.btn_submit:
                if (DepartmentID.get(spnr_department.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    Toast.makeText(activity, "Please select department", Toast.LENGTH_SHORT).show();
                } else if (CategoryID.get(spnr_category.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    Toast.makeText(activity, "Please select category", Toast.LENGTH_SHORT).show();
                } else if (SubCategoryID.get(spnr_subcategory.getSelectedItemPosition()).equalsIgnoreCase("-1")) {
                    Toast.makeText(activity, "Please select sub category", Toast.LENGTH_SHORT).show();
                } else if (et_issue.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please enter issue", Toast.LENGTH_SHORT).show();
                } else {
                    hitSubmit();
                }
                break;
            case R.id.ll:
//                if (et_refnumber.getFocusable())
                break;
        }
    }

    private void hitSubmit() {

        if (!TextUtils.isEmpty(et_refnumber.getText().toString())) {
            TicketRefNo = et_refnumber.getText().toString();
        }
        if (!TextUtils.isEmpty(et_contact.getText().toString())) {
            MobileNo = et_contact.getText().toString();
        }

        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", mySharedPreference.getUid());
            object.put("FileName", "FileName_" + mySharedPreference.getUid());
            object.put("MobileNo", MobileNo);
            object.put("DepartmentID", DepartmentID.get(spnr_department.getSelectedItemPosition()));
            object.put("TicketRefNo", TicketRefNo);
            object.put("FileInBase64", encodedResume);
            object.put("FileExt", extension);
            object.put("CategoryID", CategoryID.get(spnr_category.getSelectedItemPosition()));
            object.put("SubCategoryID", SubCategoryID.get(spnr_subcategory.getSelectedItemPosition()));
            object.put("PlantID", mySharedPreference.getPlantid());
            object.put("Issue", et_issue.getText().toString());

            request = new ProjectWebRequest(activity, object, SUBMITHELPDESK, this, SUBMITHELPDESK_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
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
        } else if (Tag == VALIDATEREF_TAG) {
            if (!call.optString("Status").equalsIgnoreCase("true")) {
                try {
                    et_refnumber.setText("");
                    focus = false;
                    AppAlertDialog.showDialogSelfFinish(activity, "", call.getString("Message"));
                } catch (Exception e) {

                }

            }
        }else if (Tag == SUBMITHELPDESK_TAG){

                try {
                    if (!call.optString("Status").equalsIgnoreCase("true")) {
                        AppAlertDialog.showDialogFinishWithActivity(activity, "", call.getString("Message"));
                    }else {
                        AppAlertDialog.showDialogFinishWithActivity(activity, "", call.getString("Message"));
                    }
                } catch (JSONException e) {
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
}
