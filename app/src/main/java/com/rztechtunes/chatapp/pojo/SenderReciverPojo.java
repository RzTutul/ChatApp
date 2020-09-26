package com.rztechtunes.chatapp.pojo;

public class SenderReciverPojo {
    String id;
    String msg;
    String image;
    String senderID;
    String reciverID;
    String reciverName;
    String reciverImage;
    String senderName;
    String senderImage;
    String status;
    int isRead;


    public SenderReciverPojo() {
    }

    public SenderReciverPojo(String id, String msg, String senderID, String reciverID, String reciverName, String reciverImage, String senderName, String senderImage, String status,int isRead) {
        this.id = id;
        this.msg = msg;
        this.senderID = senderID;
        this.reciverID = reciverID;
        this.reciverName = reciverName;
        this.reciverImage = reciverImage;
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.status = status;
        this.isRead = isRead;
    }


    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public String getReciverImage() {
        return reciverImage;
    }

    public void setReciverImage(String reciverImage) {
        this.reciverImage = reciverImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReciverID() {
        return reciverID;
    }

    public void setReciverID(String reciverID) {
        this.reciverID = reciverID;
    }
}
