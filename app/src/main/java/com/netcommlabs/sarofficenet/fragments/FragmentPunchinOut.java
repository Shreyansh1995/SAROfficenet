package com.netcommlabs.sarofficenet.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.activity.FrameActivity;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.database.DataBaseOperations;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.AppAlertDialog;
import com.netcommlabs.sarofficenet.utils.AppUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.netcommlabs.sarofficenet.utils.NetworkUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_STATUS;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_STATUS_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SAVEPUNCHINOOUT;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.SAVEPUNCHINOOUT_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.VALIDATEREF;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.VALIDATEREF_TAG;
import static com.netcommlabs.sarofficenet.utils.RequestCodeConstant.CAMERA_IMG_REQUEST;
import static com.netcommlabs.sarofficenet.utils.RequestCodeConstant.IMAGE_DIRECTORY_NAME;
import static com.netcommlabs.sarofficenet.utils.RequestCodeConstant.LOCATION_SETTINGS_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPunchinOut extends Fragment implements View.OnClickListener, ResponseListener {
    private ImageView iv_mark, iv_day_off, iv_image;
    private String PunchType = null;
    private FrameActivity activity;
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;

    File photoFile = null;
    String encoded_image = "";
    String extension = "";

    Location location;
    //Location
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    private String FullAddress = "";
    private int CAMERA = 2;

    private RelativeLayout rl_punchout, rl_punchin;
    private double Latitude = 0, Longitude = 0;
    private DataBaseOperations dop;
    private String CurrentDate, CurrentTime, dateTime;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FrameActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_punchin_out, container, false);
        mySharedPreference = MySharedPreference.getInstance(activity);
        dop = new DataBaseOperations(getContext());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dftime = new SimpleDateFormat("hh:mm");
        SimpleDateFormat dftimedate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentDate = df.format(c);
        CurrentTime = dftime.format(c);
        dateTime = dftimedate.format(c);

        init(v);
        return v;
    }

    private void init(View v) {
        iv_mark = v.findViewById(R.id.iv_mark);
        iv_day_off = v.findViewById(R.id.iv_day_off);
        iv_image = v.findViewById(R.id.iv_image);

        rl_punchout = v.findViewById(R.id.rl_punchout);
        rl_punchin = v.findViewById(R.id.rl_punchin);

        iv_mark.setOnClickListener(this);
        iv_day_off.setOnClickListener(this);
        iv_image.setOnClickListener(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createLocationRequest();
        } else {
            if (mLocationRequest == null) {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(120000); //two minute interval
                mLocationRequest.setFastestInterval(120000); //two minute interval
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            }

            requestLocationUpdates();
        }

        if (!NetworkUtils.isConnected(activity)) {
            final SharedPreferences prefss = getActivity().getSharedPreferences("ATTENDANCE", Context.MODE_PRIVATE);
            String type = prefss.getString("type", null);
            String date = prefss.getString("date", null);
            if (!TextUtils.isEmpty(date)) {
                if (CurrentDate.equalsIgnoreCase(date)) {
                    if (type.equalsIgnoreCase("PUNCHIN")) {
                        rl_punchin.setVisibility(View.GONE);
                        rl_punchout.setVisibility(View.VISIBLE);
                    } else {
                        rl_punchin.setVisibility(View.VISIBLE);
                        rl_punchout.setVisibility(View.GONE);
                    }
                } else {
                   /* SharedPreferences preferences = getContext().getSharedPreferences("ATTENDANCE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();*/
                }
            }
        }
        hitApiToShowButton();
    }

    private void savePreferencePunch(String Type) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ATTENDANCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type", Type);
        editor.putString("date", CurrentDate);
        editor.commit();
    }

    private void hitApiToShowButton() {
        try {
            JSONObject object = null;
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("EmpCode", mySharedPreference.getEmpcode());
            request = new ProjectWebRequest(activity, object, GET_STATUS, this, GET_STATUS_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private void hitApiForAttendance() {
        if (!encoded_image.equalsIgnoreCase("")) {
            extension = ".png";
        }
        if (FullAddress.equalsIgnoreCase("")) {
            FullAddress = "Unable To get Location";
        }
        try {

            if (NetworkUtils.isConnected(activity)) {
                JSONObject object = null;
                object = new JSONObject();
                object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
                object.put("UserID", mySharedPreference.getUid());
                object.put("Latitude", Latitude);
                object.put("Longitude", Longitude);
                object.put("Address", FullAddress);
                object.put("Type", PunchType);
                object.put("DateTime", dateTime);
                object.put("FileInBase64", encoded_image);
                object.put("FileExt", extension);
                object.put("AttendanceType", "ONLINE");
                request = new ProjectWebRequest(activity, object, SAVEPUNCHINOOUT, this, SAVEPUNCHINOOUT_TAG);
                request.execute();
            } else {
                JSONObject object = null;
                object = new JSONObject();
                object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
                object.put("UserID", mySharedPreference.getUid());
                object.put("Latitude", Latitude);
                object.put("Longitude", Longitude);
                object.put("Address", FullAddress);
                object.put("Type", PunchType);
                object.put("DateTime", dateTime);
                object.put("FileInBase64", encoded_image);
                object.put("FileExt", extension);
                object.put("AttendanceType", "OFFLINE");
                dop.insertAttendance(dop, CurrentDate, CurrentTime, object.toString(), PunchType);
                dop.insertRequest(dop, SAVEPUNCHINOOUT, object.toString());

                String type = null;
                if (PunchType.equalsIgnoreCase("PUNCHIN")) {
                    type = "Punch in";
                } else {
                    type = "Punch out";
                }

                AppAlertDialog.showDialogFinishWithActivity(activity, "", type + " at " + CurrentTime);
            }

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_mark:
                getLocation();
                PunchType = "PUNCHIN";
                hitApiForAttendance();
                savePreferencePunch("PUNCHIN");
                break;
            case R.id.iv_day_off:
                getLocation();
                PunchType = "PUNCHOUT";
                hitApiForAttendance();
                savePreferencePunch("PUNCHOUT");
                break;
            case R.id.iv_image:
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    captureImage();
                } else {
                    captureImage2();
                }*/
                break;
        }
    }


    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        if (Tag == GET_STATUS_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    String InButton = call.optString("IsInButtonVisable");
                    String OutButton = call.optString("IsOutButtonVisable");

                    if (InButton.equalsIgnoreCase("true")) {
                        rl_punchin.setVisibility(View.VISIBLE);
                        rl_punchout.setVisibility(View.GONE);
                    } else {
                        rl_punchin.setVisibility(View.GONE);
                        rl_punchout.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Tag == SAVEPUNCHINOOUT_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    AppAlertDialog.showDialogFinishWithActivity(activity, "", call.getString("Message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
        Toast.makeText(getContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); //two minute interval
        mLocationRequest.setFastestInterval(120000); //two minute interval
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        checkResolutionAndProceed();
    }

    private void requestLocationUpdates() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
        getLastLocation();
    }

    private void checkResolutionAndProceed() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build());
        result.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                requestLocationUpdates();
            }
        });
        result.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity, LOCATION_SETTINGS_REQUEST);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(activity)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(activity, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                        }
                    }
                });
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);

                if (location != null) {
                    if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                        mLastLocation = location;
                        //Place current location marker
                        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


                        String Show_lat_long = "Latitude: " + String.valueOf(location.getLatitude()) + "\nLongitude: " + String.valueOf(location.getLongitude());

                        Log.d("PunchInLocation", Show_lat_long);
                    }

                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA) {
            try {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                iv_image.setImageBitmap(thumbnail);
                thumbnail = AppUtils.scaleBitmap(thumbnail, 1000, 1000);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encoded_image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                if (!TextUtils.isEmpty(encoded_image)) {

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        location = mLastLocation;

        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
            convertToLocation(location.getLatitude(), location.getLongitude());
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            // savePunchToDatabase(encoded_image, location.getLatitude(), location.getLongitude(), punch_flag);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mLastLocation = location;
                }
            });
            final Handler handler = new Handler();
            final int[] count = {0};
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    location = mLastLocation;
                    if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                        convertToLocation(location.getLatitude(), location.getLongitude());
                        Latitude = location.getLatitude();
                        Longitude = location.getLongitude();
                        //savePunchToDatabase(encoded_image, location.getLatitude(), location.getLongitude(), punch_flag);
                    } else {
                        if (count[0]++ < 5) {
                            handler.postDelayed(this, 10000);
                        } else {
                            Toast.makeText(activity, "Error Retriving Location: Try again after some time", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };

            handler.post(runnable);
        }
    }

    public void convertToLocation(double Latitude, double Longitude) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
            String address = addresses.get(0).getSubLocality();
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            FullAddress = address + " " + cityName + " " + stateName + " " + country;
            // Toast.makeText(activity, "" + FullAddress, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
