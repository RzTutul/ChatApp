package com.rztechtunes.chatapp.pojo;

public class UserInformationPojo {
    String u_ID;
    String name;
    String email;
    String phone;
    String hobby;
    String country;
    String profileImage;
    String coverImage;
    String status;
    String time;
    boolean isSelected = false;

    public UserInformationPojo() {
    }


    public UserInformationPojo(String u_ID, String name, String email, String phone, String hobby, String country, String profileImage, String coverImage, String status, String time) {
        this.u_ID = u_ID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.hobby = hobby;
        this.country = country;
        this.profileImage = profileImage;
        this.coverImage = coverImage;
        this.status = status;
        this.time = time;
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

    public String gethobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getprofileImage() {
        return profileImage;
    }

    public void setprofileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
