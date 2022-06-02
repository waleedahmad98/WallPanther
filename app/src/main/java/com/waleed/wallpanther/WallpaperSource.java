package com.waleed.wallpanther;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public interface WallpaperSource {
    public ArrayList<Entry> getTop100() throws IOException, ParseException;
    public ArrayList<Entry> searchWord(String word) throws IOException, ParseException;
}
