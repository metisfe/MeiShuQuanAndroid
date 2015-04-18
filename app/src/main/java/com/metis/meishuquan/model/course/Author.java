package com.metis.meishuquan.model.course;

/**
 * Created by wangjin on 15/4/17.
 */
public class Author {
    private int userId;

    private String name;

    private String avatar;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
