package com.netcommlabs.sarofficenet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

/**
 * Created by Flash_Netcomm on 2/16/2019.
 */

public class MySharedPreference {
    private static MySharedPreference object;
    public static final String MyPREFERENCES = "officenetPrefs";
    private static final String DEVICEID = "device_id";
    private static final String UID = "user_id";
    private static final String BLOODGROUP = "blood_group";
    private static final String DOB= "date_of_birth";
    private static final String DOJ= "date_of_joining";
    private static final String DEPARTMENT= "department";
    private static final String DESIGNATION= "designation";
    private static final String EMAIL= "email";
    private static final String EMPCODE= "emp_code";
    private static final String EMPSTATUS= "emp_status";
    private static final String GRADE= "grade";
    private static final String HOD= "hod";
    private static final String IMAGE= "image";
    private static final String LOCATION= "location";
    private static final String MOBILE= "mobile";
    private static final String PLANTID= "plant_id";
    private static final String PLANTNAME= "plant_name";
    private static final String RM= "rm";
    private static final String USERNAME= "user_name";
    private static final String GENDER= "gender";
    private static final String LOCATION_CODE= "location_code";


    private SharedPreferences sharedpreferences;
    private Context mContext;


    public static MySharedPreference getInstance(Context mContext) {
        if (object == null) {
            object = new MySharedPreference(mContext);
        }
        return object;
    }

    public MySharedPreference(Context mContext) {
        this.mContext = mContext;
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void setUserId(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(UID, key);
        editor.commit();
    }

    public String getUid() {
        return sharedpreferences.getString(UID, null);
    }

    public void setBloodgroup(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(BLOODGROUP, key);
        editor.commit();
    }

    public String getBloodgroup() {
        return sharedpreferences.getString(BLOODGROUP, null);
    }

    public void setDob(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DOB, key);
        editor.commit();
    }

    public String getDob() {
        return sharedpreferences.getString(DOB, null);
    }

    public void setDoj(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DOJ, key);
        editor.commit();
    }

    public String getDoj() {
        return sharedpreferences.getString(DOJ, null);
    }

    public void setDepartment(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DEPARTMENT, key);
        editor.commit();
    }
    public String getDepartment() {
        return sharedpreferences.getString(DEPARTMENT, null);
    }


    public void setDesignation(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DESIGNATION, key);
        editor.commit();
    }
    public String getDesignation() {
        return sharedpreferences.getString(DESIGNATION, null);
    }


    public void setEmail(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(EMAIL, key);
        editor.commit();
    }
    public String getEmail() {
        return sharedpreferences.getString(EMAIL, null);
    }


    public void setEmpcode(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(EMPCODE, key);
        editor.commit();
    }
    public String getEmpcode() {
        return sharedpreferences.getString(EMPCODE, null);
    }

    public void setEmpstatus(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(EMPSTATUS, key);
        editor.commit();
    }
    public String getEmpstatus() {
        return sharedpreferences.getString(EMPSTATUS, null);
    }

    public void setGrade(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(GRADE, key);
        editor.commit();
    }
    public String getGrade() {
        return sharedpreferences.getString(GRADE, null);
    }

    public void setHod(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(HOD, key);
        editor.commit();
    }
    public String getHod() {
        return sharedpreferences.getString(HOD, null);
    }


    public void setImage(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(IMAGE, key);
        editor.commit();
    }
    public String getImage() {
        return sharedpreferences.getString(IMAGE, null);
    }


    public void setLocation(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(LOCATION, key);
        editor.commit();
    }
    public String getLocation() {
        return sharedpreferences.getString(LOCATION, null);
    }


    public void setMobile(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(MOBILE, key);
        editor.commit();
    }
    public String getMobile() {
        return sharedpreferences.getString(MOBILE, null);
    }


    public void setPlantid(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PLANTID, key);
        editor.commit();
    }
    public String getPlantid() {
        return sharedpreferences.getString(PLANTID, null);
    }


    public void setPlantname(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PLANTNAME, key);
        editor.commit();
    }
    public String getPlantname() {
        return sharedpreferences.getString(PLANTNAME, null);
    }

    public void setRm(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(RM, key);
        editor.commit();
    }
    public String getRm() {
        return sharedpreferences.getString(RM, null);
    }

    public void setUsername(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, key);
        editor.commit();
    }
    public String getUsername() {
        return sharedpreferences.getString(USERNAME, null);
    }
    public void setGender(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(GENDER, key);
        editor.commit();
    }
    public String getGender() {
        return sharedpreferences.getString(GENDER, null);
    }

    public void setLocationCode(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(LOCATION_CODE, key);
        editor.commit();
    }
    public String getLocationCode() {
        return sharedpreferences.getString(LOCATION_CODE, null);
    }

}
