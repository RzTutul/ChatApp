package com.rztechtunes.chatapp.pojo;

public class StoriesPojo {
    String uid;
    String name;
    String profile;
    String image;
    String time;

    public StoriesPojo() {
    }

    public StoriesPojo(String uid, String name,String profile, String image, String time) {
        this.uid = uid;
        this.name = name;
        this.profile = profile;
        this.image = image;
        this.time = time;
    }


    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

