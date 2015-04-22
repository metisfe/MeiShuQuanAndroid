package com.metis.meishuquan.model.assess;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.model.commons.SimpleUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/22.
 */
public class AssessSupportAndComment implements Serializable {

    @SerializedName("assessCommentList")
    private List<AssessComment> assessCommentList;
    @SerializedName("supportUserList")
    private List<SimpleUser> supportUserList;

    public List<AssessComment> getAssessCommentList() {
        if (assessCommentList == null) {
            assessCommentList = new ArrayList<AssessComment>();
        }
        return assessCommentList;
    }

    public void setAssessCommentList(List<AssessComment> assessCommentList) {
        this.assessCommentList = assessCommentList;
    }

    public List<SimpleUser> getSupportUserList() {
        if (supportUserList == null) {
            supportUserList = new ArrayList<SimpleUser>();
        }
        return supportUserList;
    }

    public void setSupportUserList(List<SimpleUser> supportUserList) {
        this.supportUserList = supportUserList;
    }
}
