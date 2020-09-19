package com.rztechtunes.chatapp.pojo;

public class SendGroupMsgPojo {
    String groupID;
    String msg;
    String image;
    String senderID;
    String senderName;
    String senderImage;
    String dateTime;


    public SendGroupMsgPojo() {
    }

    public SendGroupMsgPojo(String groupID, String msg, String image, String senderID, String senderName, String senderImage, String dateTime) {
        this.groupID = groupID;
        this.msg = msg;
        this.image = image;
        this.senderID = senderID;
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.dateTime = dateTime;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
