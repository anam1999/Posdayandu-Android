package com.posdayandu.posdayandu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DaftarKader {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("unread")
    @Expose
    private String unread;

    public DaftarKader(String id, String name, String unread) {
        this.id = id;
        this.name = name;
        this.unread = unread;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }
}
