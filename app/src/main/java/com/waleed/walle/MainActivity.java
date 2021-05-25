 package com.waleed.walle;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

 public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        if(!isOnline()) {
            try {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage("Internet not available. Please connect to an internet connection and try again.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        }).show();
            } catch (Exception e) {

            }
        }
        else {
            setContentView(R.layout.activity_main);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.myactionbar);
            getSupportActionBar().setElevation(0);

            //Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
            //setSupportActionBar(toolbar);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF66BB6A"));
            tabLayout.setTabTextColors(Color.parseColor("#808080"), Color.parseColor("#FF66BB6A"));
            tabLayout.addTab(tabLayout.newTab().setText("Home"));
            tabLayout.addTab(tabLayout.newTab().setText("Favourites"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
            TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(tabsAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }



     public class TabsAdapter extends FragmentStatePagerAdapter {
         int mNumOfTabs;
         public TabsAdapter(FragmentManager fm, int NoofTabs){
             super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
             this.mNumOfTabs = NoofTabs;

         }
         @Override
         public int getCount() {
             return mNumOfTabs;
         }
         @Override
         public Fragment getItem(int position){
             switch (position){
                 case 0:
                     MainSelectionFrag home = new MainSelectionFrag();
                     return home;
                 case 1:
                     FavSelectionFrag fav = new FavSelectionFrag();
                     return fav;
                 default:
                     return null;
             }
         }
     }

     public boolean isOnline() {
         ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

         if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
             Toast.makeText(getBaseContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
             return false;
         }
         return true;
     }
}




