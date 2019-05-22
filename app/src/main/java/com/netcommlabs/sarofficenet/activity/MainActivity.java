package com.netcommlabs.sarofficenet.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.constants.UrlConstants;
import com.netcommlabs.sarofficenet.fragments.FragmentDashboard;
import com.netcommlabs.sarofficenet.interfaces.ResponseListener;
import com.netcommlabs.sarofficenet.interfacess.NotificationCount;
import com.netcommlabs.sarofficenet.services.ProjectWebRequest;
import com.netcommlabs.sarofficenet.utils.LogUtils;
import com.netcommlabs.sarofficenet.utils.MySharedPreference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.netcommlabs.sarofficenet.constants.UrlConstants.GET_NOTIFICATION_LIST_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.LOG_OUT_TAG;
import static com.netcommlabs.sarofficenet.constants.UrlConstants.UPLOAD_IMAGE_TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ResponseListener, View.OnClickListener, NotificationCount {
    private ProjectWebRequest request;
    private MySharedPreference mySharedPreference;
    private ImageView imageView;
    private TextView tvname, tvemail;
    private LinearLayout dashboard_holiday, dashboard_gallery, dashboard_profile, dashboard_help;
    private int GALLERY = 1, CAMERA = 2;
    private String encodedResume;
    private TextView tv_count;
    private String mNotifCount;
    private NotificationCount notificationCount;

    public void updateApi(String Count, NotificationCount listener) {
        notificationCount = listener;
        mNotifCount = Count;
        tv_count.setText(mNotifCount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Humsar - Officenet");

        mySharedPreference = MySharedPreference.getInstance(this);
        findviewById();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setTitle("Humsar - Officenet");
        FragmentDashboard homeFragment = new FragmentDashboard();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, homeFragment, " Dashboard");
        fragmentTransaction.commit();
    }


    private void findviewById() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        imageView = headerView.findViewById(R.id.imageView);
        tvname = headerView.findViewById(R.id.tv_name);
        tvemail = headerView.findViewById(R.id.textView);

        tvname.setText(mySharedPreference.getUsername());
        tvemail.setText(mySharedPreference.getEmail());
        Picasso.with(this).load(mySharedPreference.getImage()).into(imageView);

        dashboard_holiday = findViewById(R.id.dashboard_holiday);
        dashboard_gallery = findViewById(R.id.dashboard_gallery);
        dashboard_profile = findViewById(R.id.dashboard_profile);
        dashboard_help = findViewById(R.id.dashboard_help);
        dashboard_holiday.setOnClickListener(this);
        dashboard_gallery.setOnClickListener(this);
        dashboard_profile.setOnClickListener(this);
        dashboard_help.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);

                    InputStream stream = null;
                    stream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    encodedResume = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    if (!TextUtils.isEmpty(encodedResume)) {
                        uploadImage();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            try {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(thumbnail);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedResume = Base64.encodeToString(byteArray, Base64.DEFAULT);

                if (!TextUtils.isEmpty(encodedResume)) {
                    uploadImage();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            //    saveImage(thumbnail);
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImage() {
        try {
            request = new ProjectWebRequest(MainActivity.this, uploadimageparam(), UrlConstants.UPLOAD_IMAGE, this, UPLOAD_IMAGE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    private JSONObject uploadimageparam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(UrlConstants.TOKEN_KEY, UrlConstants.TOKEN_NO);
            object.put("UserID", mySharedPreference.getUid());
            object.put("EmpCode", mySharedPreference.getEmpcode());
            object.put("FileInBase64", encodedResume);
            object.put("FileExt", ".jpg");
            LogUtils.showLog("request :", "Tag :" + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.notification_count);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        tv_count = (TextView) notifCount.findViewById(R.id.tv_count);
        ImageView imageView = notifCount.findViewById(R.id.iv_notify);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tv_count.getText().toString().equalsIgnoreCase("0")) {
                    Intent intentbadge = new Intent(MainActivity.this, FrameActivity.class);
                    intentbadge.putExtra("frag_name", "FragmentNotification");
                    intentbadge.putExtra("frag_tag", "notes");
                    intentbadge.putExtra("title", "Notifications");
                    startActivity(intentbadge);
                }

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.badge) {
            //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

            /*Intent intentbadge= new Intent(this, FrameActivity.class);
            intentbadge.putExtra("frag_name", "FragmentNotification");
            intentbadge.putExtra("frag_tag", "notes");
            intentbadge.putExtra("title", "Notifications");
            startActivity(intentbadge);*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            Logout();
        } else if (id == R.id.nav_notes) {
            Intent intentNotes = new Intent(this, FrameActivity.class);
            intentNotes.putExtra("frag_name", "FragmentNotes");
            intentNotes.putExtra("frag_tag", "notes");
            intentNotes.putExtra("title", "Notes");
            startActivity(intentNotes);

        } else if (id == R.id.nav_suggestion) {
            Intent intentsuggestion = new Intent(this, FrameActivity.class);
            intentsuggestion.putExtra("frag_name", "FragmentSuggestions");
            intentsuggestion.putExtra("frag_tag", "suggestion");
            intentsuggestion.putExtra("title", "Suggestion");
            startActivity(intentsuggestion);
        } else if (id == R.id.nav_bday) {
            Intent intentbday = new Intent(this, FrameActivity.class);
            intentbday.putExtra("frag_name", "FragmentNewJoinee");
            intentbday.putExtra("frag_tag", "newjoinee");
            intentbday.putExtra("title", "Birthday");
            intentbday.putExtra("type", "Bday");
            startActivity(intentbday);

        } else if (id == R.id.nav_newJoinee) {
            Intent intentnewJoinee = new Intent(this, FrameActivity.class);
            intentnewJoinee.putExtra("frag_name", "FragmentNewJoinee");
            intentnewJoinee.putExtra("frag_tag", "newjoinee");
            intentnewJoinee.putExtra("title", "New Joinee");
            intentnewJoinee.putExtra("type", "New Joinee");
            startActivity(intentnewJoinee);
        } else if (id == R.id.nav_team) {
            Intent intentteam = new Intent(this, FrameActivity.class);
            intentteam.putExtra("frag_name", "FragmentTeamList");
            intentteam.putExtra("frag_tag", "teamlist");
            intentteam.putExtra("title", "My Team");
            startActivity(intentteam);
        }else if (id == R.id.nav_att) {
            Intent intentteam = new Intent(this, FrameActivity.class);
            intentteam.putExtra("frag_name", "FragmentPunchinOut");
            intentteam.putExtra("frag_tag", "attendance");
            intentteam.putExtra("title", "Attendace");
            startActivity(intentteam);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout() {
        SharedPreferences con = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = con.edit();
        ed.clear();
        ed.commit();

        try {
            request = new ProjectWebRequest(MainActivity.this, getParam(), UrlConstants.LOG_OUT, this, LOG_OUT_TAG);
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
            LogUtils.showLog("request :", "Tag :" + object.toString());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject call, int Tag) {
        clearRef();
        if (Tag == LOG_OUT_TAG) {
            SharedPreferences preferences = getSharedPreferences("ATTENDANCE", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (Tag == UPLOAD_IMAGE_TAG) {
            try {
                if (call.optString("Status").equalsIgnoreCase("true")) {
                    mySharedPreference.setImage(call.getString("ImagePath"));
                }
                Toast.makeText(this, "" + call.getString("Message"), Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.dashboard_holiday:
                Intent intentnewJoinee = new Intent(this, FrameActivity.class);
                intentnewJoinee.putExtra("frag_name", "FragmentHoliday");
                intentnewJoinee.putExtra("frag_tag", "holiday");
                intentnewJoinee.putExtra("title", "Holidays");
                startActivity(intentnewJoinee);
                break;
            case R.id.dashboard_gallery:
                Intent intentgallery = new Intent(this, FrameActivity.class);
                intentgallery.putExtra("frag_name", "FragmentGalleryFolders");
                intentgallery.putExtra("frag_tag", "galleryfolder");
                intentgallery.putExtra("title", "Gallery");
                startActivity(intentgallery);
                break;
            case R.id.dashboard_profile:
                Intent intent1 = new Intent(this, FrameActivity.class);
                intent1.putExtra("frag_name", "FragmentMyProfile");
                intent1.putExtra("frag_tag", "profile");
                intent1.putExtra("title", "Profile");
                startActivity(intent1);
                break;
            case R.id.dashboard_help:
                Intent intenthelp = new Intent(this, FrameActivity.class);
                intenthelp.putExtra("frag_name", "FragmentHelp");
                intenthelp.putExtra("frag_tag", "help");
                intenthelp.putExtra("title", "Help");
                startActivity(intenthelp);
                break;
            case R.id.imageView:
                showPictureDialog();
                break;
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void notificationCount(String count) {
        tv_count.setText(mNotifCount);
    }
}
