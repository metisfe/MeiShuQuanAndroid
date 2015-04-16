package com.metis.meishuquan.model.commons;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.model.enums.LoginStateEnum;

import java.io.Serializable;

/**
 * Created by WJ on 2015/4/9.
 */
public class User implements Serializable {

    private static final String TAG = User.class.getSimpleName();

    public static final String
            KEY_NICK_NAME = "UserNickName",
            KEY_GENDER = "Gender",
            KEY_GRADE = "Grade",
            KEY_SELFINTRODUCE = "SelfIntroduce",
            KEY_USERAVATAR = "UserAvatar",
            KEY_REGION = "Region";

    @SerializedName("userId")
    private int userId = -1;

    @SerializedName("name")
    private String name = "";

    @SerializedName("mailbox")
    private String mailbox = "";

    @SerializedName("gender")
    private String gender = "";

    @SerializedName("phoneNum")
    private String phoneNum = "";

    @SerializedName("selfIntroduce")
    private String selfIntroduce = "";

    @SerializedName("grade")
    private String grade = "";

    @SerializedName("region")
    private String region = "";

    @SerializedName("userAvatar")
    private String userAvatar = "";

    @SerializedName("userRole")
    private String userRole = "";

    @SerializedName("birthday")
    private String birthday = "";

    @SerializedName("accout")
    private String accout = "";

    @SerializedName("relationType")
    private int relationType;

    @SerializedName("registrationDate")
    private String registrationDate = "";

    @SerializedName("cookie")
    private String cookie = "";

    @SerializedName("token")
    private String token = "";

    private LoginStateEnum appLoginState = LoginStateEnum.NO;

    private LoginStateEnum rongLoginState = LoginStateEnum.NO;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        Log.v(TAG, "getUserAvatar " + userAvatar);
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        Log.v(TAG, "setUserAvatar " + userAvatar);
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

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginStateEnum getAppLoginState() {
        return appLoginState;
    }

    public void setAppLoginState(LoginStateEnum appLoginState) {
        this.appLoginState = appLoginState;
    }

    public int getAttCount() {
        //TODO
        return 0;
    }

    public int getFollowsCount() {
        //TODO
        return 0;
    }
}
