package com.netcommlabs.sarofficenet.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.netcommlabs.sarofficenet.BuildConfig;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MyCheckPermission;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.FORGOT_PASSWORD_TAG;

public class LoginActivity extends AppCompatActivity implements ResponseListener, View.OnClickListener {
    private EditText etempcode, etpassword;
    private Button btnLogin;
    private TextView tvfrgtpass,tvversion;
    private TelephonyManager mTelephonyManager;
    private MySharedPreference mySharedPreference;
    private String DeviceID, DeviceName;
    //private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private static final int PERMISSION_REQUEST = 1999;
    private ProjectWebRequest request;
    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mySharedPreference = MySharedPreference.getInstance(this);


        final SharedPreferences prefss = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String StatusCheck = prefss.getString("flag", null);
        if (!TextUtils.isEmpty(StatusCheck)) {
            // startActivity(new Intent(this, MainActivity.class));
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else {
            //deviceIdPermission();
            checkAppPermission();
            findviewById();

        }

    }

    void checkAppPermission() {
        if (MyCheckPermission.checkAppPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE)) {
            if (MyCheckPermission.checkAppPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (MyCheckPermission.checkAppPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    getDeviceInfo();
                } else {
                    MyCheckPermission.requestPermissionNow(LoginActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST);
                }
            } else {
                MyCheckPermission.requestPermissionNow(LoginActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST);
            }
        } else {
            MyCheckPermission.requestPermissionNow(LoginActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
    }

    private void findviewById() {
        etempcode = findViewById(R.id.edt_emp_id);
        etpassword = findViewById(R.id.edt_emp_password);
        btnLogin = findViewById(R.id.btn_login);
        tvfrgtpass = findViewById(R.id.tv_frgtpassword);
        tvversion = findViewById(R.id.tvversion);

        btnLogin.setOnClickListener(this);
        tvfrgtpass.setOnClickListener(this);

        versionName = BuildConfig.VERSION_NAME;
        tvversion.setText("Version "+versionName);

    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        LogUtils.showLog("response :", "Tag :" + call.toString());
        clearRef();
        if (Tag == UrlConstants.USER_AUTH_TAG) {
            if (call.optString("Status").equals("true")) {
                try {
                    if (call.getString("Message").equals("Success")) {
                        mySharedPreference.setBloodgroup(call.getString("BloodGroup"));
                        mySharedPreference.setDob(call.getString("DateOfBirth"));
                        mySharedPreference.setDoj(call.getString("DateOfJoining"));
                        mySharedPreference.setDepartment(call.getString("Department"));
                        mySharedPreference.setDesignation(call.getString("Designation"));
                        mySharedPreference.setEmail(call.getString("EmailID"));
                        mySharedPreference.setEmpcode(call.getString("EmpCode"));
                        mySharedPreference.setEmpstatus(call.getString("EmployeeStatus"));
                        mySharedPreference.setGrade(call.getString("Grade"));
                        mySharedPreference.setHod(call.getString("HeadOfDepartment"));
                        mySharedPreference.setImage(call.getString("ImageURL"));
                        mySharedPreference.setLocation(call.getString("Location"));
                        mySharedPreference.setMobile(call.getString("MobileNo"));
                        mySharedPreference.setPlantid(call.getString("PlantID"));
                        mySharedPreference.setPlantname(call.getString("PlantName"));
                        mySharedPreference.setRm(call.getString("ReportingManager"));
                        mySharedPreference.setUsername(call.getString("UserName"));
                        mySharedPreference.setUserId(call.getString("UserID"));
                        mySharedPreference.setGender(call.getString("Gender"));
                        mySharedPreference.setLocationCode(call.getString("LocationCode"));

                        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("flag", "1");
                        editor.commit();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Toast.makeText(this, "" + call.getString("Message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (Tag == FORGOT_PASSWORD_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")){
                    AppAlertDialog.showDialogSelfFinish(this, "", call.getString("Message"));
                }else {
                    AppAlertDialog.showDialogSelfFinish(this, "", call.getString("Message"));
                }
            }catch (Exception e){

            }
        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        LogUtils.showLog("response :", "Tag :" + error.toString());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                if (Validation()) {
                    getDeviceInfo();
                    if (!TextUtils.isEmpty(DeviceID)) {
                        hitApiforLogin();
                    } else {
                        Toast.makeText(this, "Please allow permission to access.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.tv_frgtpassword:
                if (etempcode.getText().toString().equals("")) {
                    Toast.makeText(this, "Please Enter employee code", Toast.LENGTH_SHORT).show();
                    return;
                }
                hitApiforforgotPassword();
                break;
        }

    }

    private void hitApiforforgotPassword() {
        try {
            request = new ProjectWebRequest(LoginActivity.this, getPasswordParam(), UrlConstants.FORGOT_PASSWORD, this, FORGOT_PASSWORD_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject getPasswordParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserName", etempcode.getText().toString());

        } catch (Exception e) {
        }
        return object;
    }

    private void getDeviceInfo() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        DeviceID = mTelephonyManager.getDeviceId();
        DeviceName = android.os.Build.BRAND + android.os.Build.MODEL;

        LogUtils.showLog("DeviceInfo", "IMEI :" + DeviceID + "Device Name :" + DeviceName);

    }

    private void hitApiforLogin() {
        try {
            request = new ProjectWebRequest(LoginActivity.this, getParam(), UrlConstants.USER_AUTH, this, UrlConstants.USER_AUTH_TAG);
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
            object.put("LoginID", etempcode.getText().toString());
            object.put("Password", etpassword.getText().toString());
            object.put("IMEINo", DeviceID);
            object.put("DeviceName", DeviceName);
            LogUtils.showLog("request :", "Tag :" + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    private boolean Validation() {
        if (etempcode.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter employee code", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etpassword.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    /*private void deviceIdPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {

                getDeviceInfo();
            }
        } else
            getDeviceInfo();
    }*/

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        getDeviceInfo();
                    } else {
                        Toast.makeText(this, "You have to accept data write permission ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(this, "You have to accept data read permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "You have to accept Phone state permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (requestCode == 1234) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceInfo();
            } else {
                Toast.makeText(this, "You have to accept Phone state permission ", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
