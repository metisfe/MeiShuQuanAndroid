package com.metis.meishuquan.model.commons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by WJ on 2015/4/9.
 */
public class User implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("mailbox")
    private String mailbox;

    @SerializedName("gender")
    private String gender;

    @SerializedName("phoneNum")
    private String phoneNum;

    @SerializedName("selfIntroduce")
    private String selfIntroduce;

    @SerializedName("grade")
    private String grade;

    @SerializedName("region")
    private String region;

    @SerializedName("userId")
    private String userId;

    @SerializedName("userAvatar")
    private String userAvatar;

    @SerializedName("userRole")
    private String userRole;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("accout")
    private String accout;

    @SerializedName("relationType")
    private int relationType;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSelfIntroduce() {
        return selfIntroduce;
    }

    public void setSelfIntroduce(String selfIntroduce) {
        this.selfIntroduce = selfIntroduce;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getAccout() {
        return accout;
    }

    public void setAccout(String accout) {
        this.accout = accout;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }
}
