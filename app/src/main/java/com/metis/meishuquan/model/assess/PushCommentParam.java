package com.metis.meishuquan.model.assess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/24.
 */
public class PushCommentParam {
    private int AssessId = 0;
    private int UserId = 0;
    private int ReplyUserId = 0;
    private String Content = "";
    //    private int score = 0;
//    private String points = "";
    private List<AssessCommentImg> Imgs;
    private String Voice = "";
    private int CommentType = 0;
    private int VoiceLength = 0;
//    private String session = "";


    public int getAssessId() {
        return AssessId;
    }

    public void setAssessId(int assessId) {
        AssessId = assessId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getReplyUserId() {
        return ReplyUserId;
    }

    public void setReplyUserId(int replyUserId) {
        ReplyUserId = replyUserId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public List<AssessCommentImg> getImgs() {
        if (Imgs == null) {
            Imgs = new ArrayList<AssessCommentImg>();
        }
        return Imgs;
    }

    public void setImgs(List<AssessCommentImg> imgs) {
        Imgs = imgs;
    }

    public String getVoice() {
        return Voice;
    }

    public void setVoice(String voice) {
        Voice = voice;
    }

    public int getCommentType() {
        return CommentType;
    }

    public void setCommentType(int commentType) {
        CommentType = commentType;
    }

    public int getVoiceLength() {
        return VoiceLength;
    }

    public void setVoiceLength(int voiceLength) {
        VoiceLength = voiceLength;
    }
}
