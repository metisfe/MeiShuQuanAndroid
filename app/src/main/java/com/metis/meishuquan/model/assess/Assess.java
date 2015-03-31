package com.metis.meishuquan.model.assess;

import com.metis.meishuquan.model.topline.User;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO:Assess
 * <p/>
 * Created by WJ on 2015/3/31.
 */
public class Assess {
    private int id;

    private User user;

    private List<User> replyUsers;

    private Thumbnails thumbnails;

    private OriginalImage originalImage;

    private AssessChannel assessChannel;

    private int region;

    private int oppositionCount;

    private int supportCount;

    private int commentCount;

    private int pageViewCount;

    private String createTime;

    private String desc = "";

    private String group;//分组

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

    public List<User> getReplyUsers() {
        if (replyUsers == null) {
            replyUsers = new ArrayList<>();
        }
        return replyUsers;
    }

    public void setReplyUsers(List<User> replyUsers) {
        this.replyUsers = replyUsers;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public OriginalImage getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(OriginalImage originalImage) {
        this.originalImage = originalImage;
    }

    public AssessChannel getAssessChannel() {
        return assessChannel;
    }

    public void setAssessChannel(AssessChannel assessChannel) {
        this.assessChannel = assessChannel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getOppositionCount() {
        return oppositionCount;
    }

    public void setOppositionCount(int oppositionCount) {
        this.oppositionCount = oppositionCount;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPageViewCount() {
        return pageViewCount;
    }

    public void setPageViewCount(int pageViewCount) {
        this.pageViewCount = pageViewCount;
    }
}
