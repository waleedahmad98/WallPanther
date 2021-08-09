package com.waleed.wallpanther;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;

public class MainSelectionFrag extends Fragment {
    MyRecyclerViewAdapter adapter;
    private ArrayList<Entry> backgrounds;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_selection, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        backgrounds = new ArrayList<Entry>();
        RecyclerView recyclerView = view.findViewById(R.id.rvNumbers);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//new GridLayoutManager(view.getContext(), numberOfColumns));
        adapter = new MyRecyclerViewAdapter(view.getContext(), backgrounds);
        try {
            fetchBackgrounds();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(adapter);

        return view;

    }


    public void fetchBackgrounds() throws InterruptedException {
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    WallpaperSource amoledBackgrounds = new AmoledBackgrounds();
                    WallpaperSource verticalWallpapers = new Verticalwallpapers();
                    for (Entry e : amoledBackgrounds.getTop100()){
                        backgrounds.add(e);
                    }
                    for (Entry e : verticalWallpapers.getTop100()){
                        backgrounds.add(e);

                    }
                    Collections.sort(backgrounds, new EntryComparator());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(backgrounds);
                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        }).start();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                i.putExtra("query", query);
                searchView.clearFocus();
                startActivity(i);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
}
