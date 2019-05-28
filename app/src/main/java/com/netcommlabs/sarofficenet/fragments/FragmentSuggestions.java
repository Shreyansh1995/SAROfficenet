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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.BuildConfig;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.FilePath;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;


import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.DELETE_NOTES_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.NOTES_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUGGESTIONWITHIDENTITY;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUGGESTIONWITHIDENTITY_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SUGGESTIONWITHOUTIDENTITY;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSuggestions extends Fragment implements ResponseListener {
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    private EditText etmsg;
    private TextView tvattachment;
    private Button btn_anonymous, btn_identity;
    final int ACTIVITY_CHOOSE_FILE = 7;
    private String extension, encodedResume;
    private Bitmap bitmap;
    private String url;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_suggestions, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
        findViewByid(v);
        return v;
    }

    private void findViewByid(View v) {
        etmsg = v.findViewById(R.id.et_msg);
        tvattachment = v.findViewById(R.id.tv_attach);
        btn_anonymous = v.findViewById(R.id.btn_anonymous);
        btn_identity = v.findViewById(R.id.btn_identity);

        tvattachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*new MaterialFilePicker()
                        .withActivity(activity)
                        .withRequestCode(ACTIVITY_CHOOSE_FILE)
                        .start();*/
                try {
                    Uri photoURI = FileProvider.getUriForFile(activity,
                            "com.netcommlabs.sarofficenet.fileprovider",
                            createImageFile());

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

        btn_anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isConnected(activity)) {
                    if (!etmsg.getText().toString().equalsIgnoreCase("")) {
                        url = SUGGESTIONWITHOUTIDENTITY;
                        hitApi(url);
                    } else {
                        Toast.makeText(activity, "Please Enter suggestion", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Please check your intenet connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_identity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isConnected(activity)) {
                    if (!etmsg.getText().toString().equalsIgnoreCase("")) {
                        url = SUGGESTIONWITHIDENTITY;
                        hitApi(url);
                    } else {
                        Toast.makeText(activity, "Please Enter suggestion", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Please check your intenet connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });


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

    private void hitApi(String Url) {
        try {
            request = new ProjectWebRequest(activity, getParam(), Url, this, SUGGESTIONWITHIDENTITY_TAG);
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
            object.put("Suggestion", etmsg.getText().toString());
            object.put("FileName", "FileName_" + mySharedPreference.getUid());
            object.put("FileInBase64", encodedResume);
            object.put(" FileExt", extension);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        try {
            if (call.optString("Status").equalsIgnoreCase("true")){
                Toast.makeText(activity, ""+call.getString("Message"), Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {

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
                tvattachment.setText("Attached");

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
}
