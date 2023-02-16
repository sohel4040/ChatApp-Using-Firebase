package com.example.firebasechat;

public class User {
    String imageurl;
    String Name;
    String id;
    String status;
    public User(String imageurl, String name,String id,String status) {
        this.imageurl = imageurl;
        Name = name;
        this.id=id;
    }
    public User()
    {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
