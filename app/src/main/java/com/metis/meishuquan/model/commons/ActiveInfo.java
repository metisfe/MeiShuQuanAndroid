package com.metis.meishuquan.model.commons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by WJ on 2015/5/5.
 */
public class ActiveInfo implements Serializable {

    @SerializedName("pId")
    private int pId;

    @SerializedName("title")
    private String title;

    @SerializedName("desc")
    private String desc;

    @SerializedName("image")
    private String image;//图片跳转的链接

    @SerializedName("activityDatetime")
    private String activityDatetime;

    @SerializedName("activityLocation")
    private String activityLocation;

    @SerializedName("activityRole")
    private String activityRole;

    @SerializedName("activityAward")
    private String activityAward;

    @SerializedName("useState")
    private boolean useState;

    @SerializedName("createDatetime")
    private String createDatetime;

    @SerializedName("content")
    private String content;

    @SerializedName("topImage")
    private String topImage;//图片本身的地址

//    @SerializedName("height")
//    private int height=0;
//
//    @SerializedName("width")
//    private int width=0;

    @SerializedName("agreement")
    private String agreement;

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getActivityDatetime() {
        return activityDatetime;
    }

    public void setActivityDatetime(String activityDatetime) {
        this.activityDatetime = activityDatetime;
    }

    public String getActivityLocation() {
        return activityLocation;
    }

    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }

    public String getActivityRole() {
        return activityRole;
    }

    public void setActivityRole(String activityRole) {
        this.activityRole = activityRole;
    }

    public String getActivityAward() {
        return activityAward;
    }

    public void setActivityAward(String activityAward) {
        this.activityAward = activityAward;
    }

    public boolean isUseState() {
        return useState;
    }

    public void setUseState(boolean useState) {
        this.useState = useState;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopImage() {
        return topImage;
    }

    public void setTopImage(String topImage) {
        this.topImage = topImage;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }
}
