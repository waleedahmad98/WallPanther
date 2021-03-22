package com.example.walle;

public class Entry {
    private String title;
    private String link;

    public Entry() {
        title = null;
        link = null;
    }

    public Entry(String title, String link){
        setLink(link);
        setTitle(title);
    }

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
}
