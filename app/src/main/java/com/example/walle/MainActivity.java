 package com.example.walle;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Entry> backgrounds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgrounds = new ArrayList<Entry>();
        fetchBackgrounds();



    }

    public void fetchBackgrounds(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    JSONParser jsonParser = new JSONParser();
                    URLConnection connection = new URL("https://www.reddit.com/r/Amoledbackgrounds.json").openConnection(); // must handle this exception with a toast notification
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    InputStream response = connection.getInputStream();
                    JSONObject obj = (JSONObject)jsonParser.parse(new InputStreamReader(response, "UTF-8"));
                    JSONObject data = (JSONObject) obj.get("data");
                    JSONArray children = (JSONArray) data.get("children");
                    for (int i = 0; i < children.size(); i++){
                        if(i > 1){
                            JSONObject index = (JSONObject) children.get(i);
                            JSONObject inner_data = (JSONObject) index.get("data");
                            Entry e = new Entry(inner_data.get("title").toString(), inner_data.get("url").toString());
                            backgrounds.add(e);
                        }
                    }
                    Log.d("TEST", "TEST");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();


    }


}

