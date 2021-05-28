package com.waleed.walle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Verticalwallpapers implements WallpaperSource {
    @Override
    public ArrayList<Entry> getTop100() throws IOException, ParseException {
        ArrayList<Entry> backgrounds = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        URLConnection connection = new URL("https://www.reddit.com/r/Verticalwallpapers.json?limit=100").openConnection(); // must handle this exception with a toast notification
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        InputStream response = connection.getInputStream();
        JSONObject obj = (JSONObject)jsonParser.parse(new InputStreamReader(response, "UTF-8"));
        JSONObject data = (JSONObject) obj.get("data");
        JSONArray children = (JSONArray) data.get("children");
        for (int i = 0; i < children.size(); i++){
            if(i > 1){
                JSONObject index = (JSONObject) children.get(i);
                JSONObject inner_data = (JSONObject) index.get("data");
                if (inner_data.containsKey("post_hint")) {
                    Entry e = new Entry(inner_data.get("title").toString(), inner_data.get("url").toString(), inner_data.get("thumbnail").toString(), inner_data.get("author").toString());
                    backgrounds.add(e);
                }
            }
        }
        return backgrounds;
    }
}
