package com.metis.meishuquan.model.commons;

import java.io.Serializable;

/**
 * Created by wangjin on 15/4/22.
 */
public class SimpleUser implements Serializable {
    private int userId;

    private String name;

    private String remarkName;

    private String avatar;

    private String grade;

    private int identity;

    private String relation;

    private String account;

    private String rongCloud;

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

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRongCloud() {
        return rongCloud;
    }

    public void setRongCloud(String rongCloud) {
        this.rongCloud = rongCloud;
    }
}
