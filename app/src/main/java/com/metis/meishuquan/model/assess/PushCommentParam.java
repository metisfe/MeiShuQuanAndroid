package com.metis.meishuquan.model.assess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/24.
 */
public class PushCommentParam {
    private int assessId = 0;
    private int userId = 0;
    private int replyUserId = 0;
    private String content = "";
    private int score = 0;
    private String points = "";
    private List<AssessCommentImg> imgs;
    private String voice = "";
    private int commentType = 0;
    private String session = "";

    public int getAssessId() {
        return assessId;
    }

    public void setAssessId(int assessId) {
        this.assessId = assessId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(int replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public List<AssessCommentImg> getImgs() {
        if (imgs == null) {
            imgs = new ArrayList<AssessCommentImg>();
        }
        return imgs;
    }

    public void setImgs(List<AssessCommentImg> imgs) {
        this.imgs = imgs;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
