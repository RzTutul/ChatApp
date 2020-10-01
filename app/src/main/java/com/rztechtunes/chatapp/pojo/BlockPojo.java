package com.rztechtunes.chatapp.pojo;

public class BlockPojo {
    String u_id;
    String name;

    public BlockPojo() {
    }

    public BlockPojo(String u_id, String name) {
        this.u_id = u_id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }
}
