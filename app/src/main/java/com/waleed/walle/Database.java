package com.waleed.walle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "Favourites";
    static final String DATABASE_NAME = "WallE";
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String AUTHOR = "AUTHOR";
    public static final String MAIN_LINK = "MAIN_LINK";
    public static final String THUMBNAIL_LINK = "THUMBNAIL_LINK";

    static final int VERSION = 1;

    private static final String CREATE = "CREATE TABLE " + TABLE_NAME + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT NOT NULL, " + AUTHOR + " TEXT NOT NULL, " + MAIN_LINK + " TEXT NOT NULL, " + THUMBNAIL_LINK + " TEXT NOT NULL);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Entry> getAllWallpapers(){
        String[] cols = new String[] { ID, TITLE, AUTHOR, MAIN_LINK, THUMBNAIL_LINK };
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, cols, null, null, null, null, null);
        ArrayList<Entry> backgrounds = new ArrayList<Entry>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                backgrounds.add(new Entry(cursor.getString(1), cursor.getString(3),cursor.getString(4), cursor.getString(2)));
                cursor.moveToNext();
            }
        }
        return backgrounds;
    }

    public void insertWallpaper(Entry e){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(TITLE, e.getTitle());
        contentValue.put(AUTHOR, e.getAuthor());
        contentValue.put(MAIN_LINK, e.getLink());
        contentValue.put(THUMBNAIL_LINK, e.getThumbnail());
        db.insert(TABLE_NAME, null, contentValue);

    }

    public void deleteWallpaper(String link){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME,"MAIN_LINK=?",new String[]{link});
    }
}
