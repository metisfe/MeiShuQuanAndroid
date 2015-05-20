package com.metis.meishuquan.model.topline;


import com.metis.meishuquan.model.commons.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/3/29.
 */
public class Comment {
    private int id;

    private User user;

    private int supportCount = 0;

    private String content;

    private String commentDateTime;

    private int replyCount;

    private User replyUser;

    private List<ImgOrVoiceUrl> imgOrVoiceUrls;

    private String group = "";

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

    public List<ImgOrVoiceUrl> getImgOrVoiceUrls() {
        if (imgOrVoiceUrls == null) {
            imgOrVoiceUrls = new ArrayList<ImgOrVoiceUrl>();
        }
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
