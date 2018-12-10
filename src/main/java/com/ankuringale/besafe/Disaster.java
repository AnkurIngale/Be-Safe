package com.ankuringale.besafe;

public class Disaster {
    String title;
    String storyLink;
    String date;

    public Disaster(){

    }
    public Disaster(String title, String storyLink,String date) {
        this.title = title;
        this.storyLink = storyLink;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoryLink() {
        return storyLink;
    }

    public void setStoryLink(String storyLink) {
        this.storyLink = storyLink;
    }

    public String getDate() {
        return date;
    }
}
