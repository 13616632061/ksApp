package com.ui.entity;

import java.io.Serializable;

public class AddressCity implements Serializable {
    private String cid;
    private String title;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
