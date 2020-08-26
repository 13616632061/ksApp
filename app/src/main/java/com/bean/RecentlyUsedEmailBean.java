package com.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by lyf on 2020/8/1.
 */

public class RecentlyUsedEmailBean extends LitePalSupport {
    @Column(unique = true)
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
