package com.waleed.walle;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainSelectionFrag extends Fragment {
    MyRecyclerViewAdapter adapter;
    private ArrayList<Entry> backgrounds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_selection, container, false);

        backgrounds = new ArrayList<Entry>();
        RecyclerView recyclerView = view.findViewById(R.id.rvNumbers);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));
        adapter = new MyRecyclerViewAdapter(view.getContext(), backgrounds);
        recyclerView.setAdapter(adapter);
        try {
            fetchBackgrounds();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return view;

    }



    public void fetchBackgrounds() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    JSONParser jsonParser = new JSONParser();
                    URLConnection connection = new URL("https://www.reddit.com/r/Amoledbackgrounds.json?limit=100").openConnection(); // must handle this exception with a toast notification
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    InputStream response = connection.getInputStream();
                    JSONObject obj = (JSONObject)jsonParser.parse(new InputStreamReader(response, "UTF-8"));
                    JSONObject data = (JSONObject) obj.get("data");
                    JSONArray children = (JSONArray) data.get("children");
                    for (int i = 0; i < children.size(); i++){
                        if(i > 1){
                            JSONObject index = (JSONObject) children.get(i);
                            JSONObject inner_data = (JSONObject) index.get("data");
                            Entry e = new Entry(inner_data.get("title").toString(), inner_data.get("url").toString(), inner_data.get("thumbnail").toString(), inner_data.get("author").toString());
                            backgrounds.add(e);
                        }
                    }
                    latch.countDown();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        latch.await();
        adapter.updateData(backgrounds);
    }
}