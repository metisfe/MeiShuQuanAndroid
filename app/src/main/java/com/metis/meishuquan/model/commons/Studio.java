package com.metis.meishuquan.model.commons;

import java.io.Serializable;

/**
 * Created by WJ on 2015/5/11.
 */
public class Studio implements Serializable {
    private int role;
    private String name;
    private String address;

    private int userId;
    private int identity;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }
}
