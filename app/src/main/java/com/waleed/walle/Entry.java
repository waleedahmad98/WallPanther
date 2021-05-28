package com.waleed.walle;

import android.os.Parcel;
import android.os.Parcelable;

public class Entry implements Parcelable {
    private String title;
    private String link;
    private String thumbnail;
    private String author;
    private String date_created;

    public Entry(String title, String link, String thumbnail, String author, String date_created){
        setLink(link);
        setTitle(title);
        setThumbnail(thumbnail);
        setAuthor(author);
        setDate_created(date_created);
    }

    protected Entry(Parcel in) {
        title = in.readString();
        link = in.readString();
        thumbnail = in.readString();
        author = in.readString();
        date_created = in.readString();
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(thumbnail);
        dest.writeString(author);
        dest.writeString(date_created);
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
