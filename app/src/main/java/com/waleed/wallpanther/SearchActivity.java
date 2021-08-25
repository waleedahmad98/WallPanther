package com.waleed.wallpanther;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    MyRecyclerViewAdapter adapter;
    private ArrayList<Entry> backgrounds;
    String word = "";
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search);
        mSwipeRefreshLayout = findViewById(R.id.swipe);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        word = getIntent().getExtras().getString("query");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Showing results for '" + word + "'");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        backgrounds = new ArrayList<Entry>();
        RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//new GridLayoutManager(view.getContext(), numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, backgrounds);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                try {
                    fetchBackgrounds();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(adapter);

    }

    public void fetchBackgrounds() throws InterruptedException {
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    WallpaperSource amoledBackgrounds = new AmoledBackgrounds();
                    WallpaperSource verticalWallpapers = new Verticalwallpapers();
                    for (Entry e : amoledBackgrounds.searchWord(word)){
                        backgrounds.add(e);
                    }
                    for (Entry e : verticalWallpapers.searchWord(word)){
                        backgrounds.add(e);

                    }
                    Collections.sort(backgrounds, new EntryComparator());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(backgrounds);
                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                });
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRefresh() {
        backgrounds.clear();
        try {
            fetchBackgrounds();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




