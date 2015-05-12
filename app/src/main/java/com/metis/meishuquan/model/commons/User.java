package com.metis.meishuquan.model.commons;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.model.enums.IdTypeEnum;
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
            KEY_BIRTHDAY = "Birthday",
            KEY_GRADE = "Grade",
            KEY_SELFINTRODUCE = "SelfIntroduce",
            KEY_USERAVATAR = "UserAvatar",
            KEY_REGION = "Region",
            KEY_SELFSIGNATURE = "SelfSignature",
            KEY_HOROSCOPE = "Horoscope",
            KEY_LOCATION = "Location",
            KEY_LOCATIONADDRESS = "LocationAddress",
            KEY_GOODSUBJECTS = "GoodSubjects",
            KEY_ACHIEVEMENT = "Achievement",
            KEY_USER_ID = "userId",
            KEY_ACCOUNT = "Account",
            KEY_USER_ROLE = "userRole",
            KEY_USER_RESUME = "UserResume",
            KEY_LOCATION_STUDIO = "LocationStudio",
            KEY_LOCATION_SCHOOL = "LocationSchool";

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
    private int region;

    @SerializedName("userAvatar")
    private String userAvatar = "";

    @SerializedName("avatar")
    private String avatar = "";

    @SerializedName("userRole")
    private int userRole;

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

    @SerializedName("rongCloudId")
    private String rongCloudId = "";

    @SerializedName("token")
    private String token = "";

    @SerializedName("goodSubjects")
    private String goodSubjects = "";

    @SerializedName("selfSignature")
    private String selfSignature = "";

    @SerializedName("horoscope")
    private String horoscope = "";

    @SerializedName("LocationStudio")
    private int LocationStudio;

    @SerializedName("locationSchool")
    private String locationSchool;

    @SerializedName("locationAddress")
    private String locationAddress = "";

    @SerializedName("achievement")
    private String achievement = "";

    @SerializedName("backgroundImg")
    private String backgroundImg;

    @SerializedName("userResume")
    private String userResume;

    @SerializedName("studio")
    private Studio studio;

    @SerializedName("FansNum")
    private int FansNum;

    @SerializedName("FocusNum")
    private int FocusNum;

    private LoginStateEnum appLoginState = LoginStateEnum.NO;

    private LoginStateEnum rongLoginState = LoginStateEnum.NO;

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

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

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public IdTypeEnum getUserRoleEnum () {
        return IdTypeEnum.getUserRoleByType(this.userRole);
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

    public String getSelfSignature() {
        return selfSignature;
    }

    public void setSelfSignature(String selfSignature) {
        this.selfSignature = selfSignature;
    }

    public String getRongCloudId() {
        return rongCloudId;
    }

    public void setRongCloudId(String rongCloudId) {
        this.rongCloudId = rongCloudId;
    }

    public String getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
    }

    public int getLocationStudio() {
        return LocationStudio;
    }

    public void setLocationStudio(int locationStudio) {
        LocationStudio = locationStudio;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationSchool() {
        return locationSchool;
    }

    public void setLocationSchool(String locationSchool) {
        this.locationSchool = locationSchool;
    }

    public String getGoodSubjects() {
        return goodSubjects;
    }

    public void setGoodSubjects(String goodSubjects) {
        this.goodSubjects = goodSubjects;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getUserResume() {
        return userResume;
    }

    public void setUserResume(String userResume) {
        this.userResume = userResume;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    /*public List<CourseChannelItem> getGoodSubjectsList () {
        String subjects = getGoodSubjects();
        subjects = subjects.replace("\\(", "\\{").replaceAll("\\)", "\\}");
        Log.v(TAG, "getGoodSubjectsList subjects=" + subjects);
        Gson gson = new Gson();
        return gson.fromJson(subjects, new TypeToken<List<CourseChannelItem>>(){}.getType());
    }*/

    public int getFansNum() {
        return FansNum;
    }

    public void setFansNum(int fansNum) {
        FansNum = fansNum;
    }

    public int getFocusNum() {
        return FocusNum;
    }

    public void setFocusNum(int focusNum) {
        FocusNum = focusNum;
    }
}
