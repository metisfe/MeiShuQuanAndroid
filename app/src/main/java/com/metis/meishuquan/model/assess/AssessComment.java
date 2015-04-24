package com.metis.meishuquan.model.assess;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.model.commons.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/22.
 */
public class AssessComment implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("user")
    private User user = null;

    @SerializedName("supportCount")
    private int supportCount;

    @SerializedName("content")
    private String content = "";

    @SerializedName("commentDateTime")
    private String commentDateTime = "";

    @SerializedName("replyCount")
    private int replyCount;

    @SerializedName("replyUser")
    private User replyUser = null;

    @SerializedName("imgOrVoiceUrl")
    private List<ImgOrVoiceUrl> imgOrVoiceUrl;

    @SerializedName("commentType")
    private int commentType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentDateTime() {
        return commentDateTime;
    }

    public void setCommentDateTime(String commentDateTime) {
        this.commentDateTime = commentDateTime;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public User getReplyUser() {
        if (replyUser == null) {
            replyUser = new User();
        }
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }

    public List<ImgOrVoiceUrl> getImgOrVoiceUrl() {
        if (imgOrVoiceUrl == null) {
            imgOrVoiceUrl = new ArrayList<ImgOrVoiceUrl>();
        }
        return imgOrVoiceUrl;
    }

    public void setImgOrVoiceUrl(List<ImgOrVoiceUrl> imgOrVoiceUrl) {
        this.imgOrVoiceUrl = imgOrVoiceUrl;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }
}
