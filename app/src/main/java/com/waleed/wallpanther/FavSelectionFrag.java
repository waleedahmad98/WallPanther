package com.waleed.wallpanther;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class FavSelectionFrag extends Fragment {
    MyRecyclerViewAdapter adapter;
    private ArrayList<Entry> backgrounds;
    Database db;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        db = new Database(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_selection, container, false);

        backgrounds = db.getAllWallpapers();
        RecyclerView recyclerView = view.findViewById(R.id.rvNumbers);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//new GridLayoutManager(view.getContext(), numberOfColumns));
        adapter = new MyRecyclerViewAdapter(view.getContext(), backgrounds);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.backgrounds.clear();
        this.backgrounds = db.getAllWallpapers();
        adapter.updateData(this.backgrounds);
    }
}