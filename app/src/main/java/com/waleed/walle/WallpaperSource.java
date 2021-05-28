package com.waleed.walle;

import com.google.android.gms.common.util.ArrayUtils;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public interface WallpaperSource {
    public ArrayList<Entry> getTop100() throws IOException, ParseException;
}
