package com.rztechtunes.chatapp.pojo;

public class GroupPojo {
    String groupID;
    String images;
    String name;
    String description;
    String createTime;
    String grpAdmin;

    public GroupPojo() {
    }

    public GroupPojo(String groupID, String images, String name, String description, String createTime, String grpAdmin) {
        this.groupID = groupID;
        this.images = images;
        this.name = name;
        this.description = description;
        this.createTime = createTime;
        this.grpAdmin = grpAdmin;
    }



    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGrpAdmin() {
        return grpAdmin;
    }

    public void setGrpAdmin(String grpAdmin) {
        this.grpAdmin = grpAdmin;
    }
}
