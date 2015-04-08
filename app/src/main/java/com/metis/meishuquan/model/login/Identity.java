package com.metis.meishuquan.model.login;

import java.io.Serializable;

/**
 * 身份
 * Created by wj on 15/4/8.
 */
public class Identity implements Serializable {
    private int id;//身份Id

    private String name;//身份名称

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
}
