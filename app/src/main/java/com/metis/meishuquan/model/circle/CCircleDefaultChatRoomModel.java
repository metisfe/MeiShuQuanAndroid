package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wangjin on 15/6/12.
 */
public class CCircleDefaultChatRoomModel implements Serializable {

    @SerializedName("discussionId")
    private String discussionId = "";

    @SerializedName("discussionName")
    private String discussionName = "";

    @SerializedName("discussionImage")
    private String discussionImage = "";

    @SerializedName("createTime")
    private String createTime = "";

    @SerializedName("founderUser")
    private int founderUser = 0;

    @SerializedName("discussionType")
    private int discussionType = 0;

    public String getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(String discussionId) {
        this.discussionId = discussionId;
    }

    public String getDiscussionName() {
        return discussionName;
    }

    public void setDiscussionName(String discussionName) {
        this.discussionName = discussionName;
    }

    public String getDiscussionImage() {
        return discussionImage;
    }

    public void setDiscussionImage(String discussionImage) {
        this.discussionImage = discussionImage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getFounderUser() {
        return founderUser;
    }

    public void setFounderUser(int founderUser) {
        this.founderUser = founderUser;
    }

    public int getDiscussionType() {
        return discussionType;
    }

    public void setDiscussionType(int discussionType) {
        this.discussionType = discussionType;
    }
}
