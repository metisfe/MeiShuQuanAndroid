package com.metis.meishuquan.model.commons;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wangjin on 15/5/14.
 */
public class AndroidVersion {
    @SerializedName("versionId")
    private int versionId;

    @SerializedName("versionName")
    private String versionName;

    @SerializedName("versionNumber")
    private String versionNumber;

    @SerializedName("downUrl")
    private String downUrl;

    @SerializedName("createTime")
    private String createTime;

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
