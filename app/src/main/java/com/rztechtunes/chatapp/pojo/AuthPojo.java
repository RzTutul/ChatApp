package com.rztechtunes.chatapp.pojo;

public class AuthPojo {
    String u_ID;
    String name;
    String phone;
    String about;
    String image;

    public AuthPojo() {
    }

    public AuthPojo(String u_ID, String name, String phone, String about, String image) {
        this.u_ID = u_ID;
        this.name = name;
        this.phone = phone;
        this.about = about;
        this.image = image;
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
}
