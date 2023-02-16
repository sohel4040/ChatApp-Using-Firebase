package com.example.firebasechat;

import java.util.ArrayList;

public class Post {
    String imageurl;
    ArrayList<String> likedBy;
    String profileurl;
    String text;
    String time;
    String user;

    public Post() {
    }

    public Post(String imageurl, ArrayList<String> likedBy, String profileurl, String text, String time, String user) {
        this.imageurl = imageurl;
        this.likedBy = likedBy;
        this.profileurl = profileurl;
        this.text = text;
        this.time = time;
        this.user = user;
    }
    public int likecount()
    {
        return likedBy.size();
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public ArrayList<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(ArrayList<String> likedBy) {
        this.likedBy = likedBy;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
