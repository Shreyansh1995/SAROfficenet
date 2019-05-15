package com.netcommlabs.sarofficenet.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.sarofficenet.R;
import com.netcommlabs.sarofficenet.fragments.FragmentAttendance;
import com.netcommlabs.sarofficenet.fragments.FragmentDashboard;
import com.netcommlabs.sarofficenet.fragments.FragmentGalleryFolders;
import com.netcommlabs.sarofficenet.fragments.FragmentMyProfile;
import com.netcommlabs.sarofficenet.fragments.FragmentNewJoinee;
import com.netcommlabs.sarofficenet.fragments.Fragment_Gallery;
import com.netcommlabs.sarofficenet.utils.FragmentTransections;

import java.util.Objects;

public class FrameActivity extends AppCompatActivity {
    //   private ImageView ivbck;


    //private TextView tvtitle;
    private RelativeLayout frameLayout;
    private String Tag;
    private Toolbar toolbar;
    private LinearLayout home_bottom, dashboard_home, dashboard_profile, dashboard_gallery, dashboard_holiday, dashboard_help;
   private String title;

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filtermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                // do stuff, like showing settings fragment
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.bck));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                if (backStackEntryCount == 0) {
                    finish();
                } else {
                    FragmentTransections.popFragment(FrameActivity.this);
                }
            }
        });

        findviewById();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void findviewById() {
        home_bottom = findViewById(R.id.home_bottom);
        dashboard_home = findViewById(R.id.dashboard_home);
        dashboard_profile = findViewById(R.id.dashboard_profile);
        dashboard_gallery = findViewById(R.id.dashboard_gallery);
        dashboard_holiday = findViewById(R.id.dashboard_holiday);
        dashboard_help = findViewById(R.id.dashboard_help);
        setFragments();

        dashboard_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FrameActivity.this, MainActivity.class));
                finish();
            }
        });
        dashboard_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(FrameActivity.this, FrameActivity.class);
                intent1.putExtra("frag_name", "FragmentMyProfile");
                intent1.putExtra("frag_tag", "profile");
                intent1.putExtra("title", "Profile");
                startActivity(intent1);
                finish();
            }
        });
        dashboard_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentgallery = new Intent(FrameActivity.this, FrameActivity.class);
                intentgallery.putExtra("frag_name", "FragmentGalleryFolders");
                intentgallery.putExtra("frag_tag", "galleryfolder");
                intentgallery.putExtra("title", "Gallery");
                startActivity(intentgallery);
                finish();
            }
        });
        dashboard_holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentnewJoinee = new Intent(FrameActivity.this, FrameActivity.class);
                intentnewJoinee.putExtra("frag_name", "FragmentHoliday");
                intentnewJoinee.putExtra("frag_tag", "holiday");
                intentnewJoinee.putExtra("title", "Holidays");
                startActivity(intentnewJoinee);
                finish();
            }
        });
        dashboard_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenthelp = new Intent(FrameActivity.this, FrameActivity.class);
                intenthelp.putExtra("frag_name", "FragmentHelp");
                intenthelp.putExtra("frag_tag", "help");
                intenthelp.putExtra("title", "Help");
                startActivity(intenthelp);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setFragments() {
        Bundle bundle = getIntent().getExtras();
        Bundle fragmentBundle = new Bundle();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                if (!key.equals("frag_name") && !key.equals("frag_tag") && !key.equals("title")) {
                    fragmentBundle.putString(key, bundle.getString(key));
                }
            }

            String frag_name = bundle.getString("frag_name");
            String frag_tag = bundle.getString("frag_tag");
             title = bundle.getString("title");
            toolbar.setTitle(title);

            if (frag_tag.equalsIgnoreCase("holiday")) {
                home_bottom.setVisibility(View.VISIBLE);
            } else if (frag_tag.equalsIgnoreCase("galleryfolder")) {
                home_bottom.setVisibility(View.VISIBLE);
            } else if (frag_tag.equalsIgnoreCase("profile")) {
                home_bottom.setVisibility(View.VISIBLE);
            } else if (frag_tag.equalsIgnoreCase("help")) {
                home_bottom.setVisibility(View.VISIBLE);
            } else {
                home_bottom.setVisibility(View.GONE);
            }


            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }

            try {
                Fragment fragment = (Fragment) Class.forName("com.netcommlabs.sarofficenet.fragments." + frag_name).newInstance();
                if (fragmentBundle.keySet().size() > 0) {
                    fragment.setArguments(fragmentBundle);
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame_dashboard, fragment, frag_tag);
                ft.commit();

            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }

        }


    }

    @SuppressLint("NewApi")
    public void setTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }


}

