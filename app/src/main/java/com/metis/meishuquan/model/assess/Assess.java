package com.metis.meishuquan.model.assess;

import com.metis.meishuquan.model.topline.User;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO:Assess
 * 
 * Created by WJ on 2015/3/31.
 */
public class Assess {
    private int id;

    private User user;

    private List<User> replyUsers;

    private Thumbnails thumbnails;

    private OriginalImage originalImage;

    private AssessChannel assessChannel;

    private String createTime;

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
}
