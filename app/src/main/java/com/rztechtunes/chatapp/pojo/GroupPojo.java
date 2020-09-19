package com.rztechtunes.chatapp.pojo;

public class GroupPojo {
    String images;
    String name;
    String description;

    public GroupPojo() {
    }

    public GroupPojo(String images, String name, String description) {
        this.images = images;
        this.name = name;
        this.description = description;
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
}
