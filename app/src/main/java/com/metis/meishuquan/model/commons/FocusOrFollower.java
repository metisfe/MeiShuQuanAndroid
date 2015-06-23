package com.metis.meishuquan.model.commons;

import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.topline.UserMark;

import java.io.Serializable;

/**
 * Created by WJ on 2015/6/8.
 */
public class FocusOrFollower implements Serializable {

    public static final int
            TYPE_NONE = 0,
            TYPE_I_FOCUS = 1,
            TYPE_FOCUS_ME = 2,
            TYPE_FOCUS_EACH = 3;


    private long id;
    private long userId;
    private CUserModel usermodel;

    /**
     * 关注状态 0 无 1我关注他 2他关注我 3 相互关注
     */
    private int relationType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public CUserModel getUsermodel() {
        return usermodel;
    }

    public void setUsermodel(CUserModel usermodel) {
        this.usermodel = usermodel;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }
}
