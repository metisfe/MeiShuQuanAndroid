package com.metis.meishuquan.model.assess;

import com.metis.meishuquan.model.commons.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangjin on 15/4/22.
 */
public class AssessComment implements Serializable {
    private int id;

    private User user;

    private int supportCount;

    private String content;

    private String commentDateTime;

    private int replyCount;

    private User replyUser;

    private List<ImgOrVoiceUrl> imgOrVoiceUrls;

    private int commentType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
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
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }

    public List<ImgOrVoiceUrl> getImgOrVoiceUrls() {
        return imgOrVoiceUrls;
    }

    public void setImgOrVoiceUrls(List<ImgOrVoiceUrl> imgOrVoiceUrls) {
        this.imgOrVoiceUrls = imgOrVoiceUrls;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }
}
