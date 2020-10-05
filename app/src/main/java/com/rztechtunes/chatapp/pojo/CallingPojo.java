package com.rztechtunes.chatapp.pojo;

public class CallingPojo {
    String u_id;
    String room_name;
    String name;
    String image;
    String time;
    String call_type;

    public CallingPojo() {
    }

    public CallingPojo(String u_id, String room_name, String name, String image, String time, String call_type) {
        this.u_id = u_id;
        this.room_name = room_name;
        this.name = name;
        this.image = image;
        this.time = time;
        this.call_type = call_type;
    }

    public String getCall_type() {
        return call_type;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
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
