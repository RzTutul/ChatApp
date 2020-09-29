package com.rztechtunes.chatapp.pojo;

public class FriendRequestPojo {
    String u_ID;
    String name;
    String email;
    String phone;
    String about;
    String image;



    public FriendRequestPojo() {
    }

    public FriendRequestPojo(String u_ID, String name, String email, String phone, String about, String image) {
        this.u_ID = u_ID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.image = image;

    }


    public String getEmail() {
        return email;
    }

    public String getU_ID() {
        return u_ID;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAbout() {
        return about;
    }

    public String getImage() {
        return image;
    }


    public void setU_ID(String u_ID) {
        this.u_ID = u_ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
