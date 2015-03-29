package com.metis.meishuquan.model.topline;

import java.util.List;

/**
 * Created by wj on 15/3/29.
 */
public class Comments {
    private int id;

    private User user;

    private String supportCount;

    private String content;

    private String commentDateTime;

    private int replyCount;

    private User replyUser;

    private List<ImgOrVoiceUrl> imgOrVoiceUrls ;

    private String group="";

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

    public String getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(String supportCount) {
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
