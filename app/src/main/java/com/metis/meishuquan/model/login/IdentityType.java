package com.metis.meishuquan.model.login;

import java.util.List;

/**
 * Created by wj on 15/4/8.
 */
public class IdentityType {
    private int id;

    private String name;

    private List<Identity> childList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Identity> getChildLists() {
        return childList;
    }

    public void setChildLists(List<Identity> childLists) {
        this.childList = childLists;
    }
}
