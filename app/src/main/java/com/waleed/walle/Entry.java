package com.waleed.walle;

import android.os.Parcel;
import android.os.Parcelable;

public class Entry implements Parcelable {
    private String title;
    private String link;
    private String thumbnail;
    private String author;

    public Entry(String title, String link, String thumbnail, String author){
        setLink(link);
        setTitle(title);
        setThumbnail(thumbnail);
        setAuthor(author);
    }

    protected Entry(Parcel in) {
        title = in.readString();
        link = in.readString();
        thumbnail = in.readString();
        author = in.readString();
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
    }
}
