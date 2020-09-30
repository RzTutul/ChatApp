package com.rztechtunes.chatapp.pojo;

public class UserInformationPojo {
    String u_ID;
    String name;
    String email;
    String phone;
    String about;
    String country;
    String image;
    String status;
    boolean isSelected = false;

    public UserInformationPojo() {
    }

    public UserInformationPojo(String u_ID, String name, String email, String phone, String about, String country, String image, String status) {

        this.u_ID = u_ID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.country = country;
        this.image = image;
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getU_ID() {
        return u_ID;
    }

    public void setU_ID(String u_ID) {
        this.u_ID = u_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
