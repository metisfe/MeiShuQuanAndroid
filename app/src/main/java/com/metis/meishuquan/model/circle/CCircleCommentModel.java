package com.metis.meishuquan.model.circle;

import android.text.TextUtils;

import com.metis.meishuquan.model.topline.UserMark;

/**
 * Created by jiaxh on 4/18/2015.
 */
public class CCircleCommentModel {
    public int id;
    public int circleId;
    public CUserModel user;
    public int supportCount;
    public String content;
    public String createTime;
    public UserMark userMark;
    public int isRelyComment;//1:是回复他人的评论 0：是正常评论
    public CUserModel relyUser;

    public boolean isValid() {
        return user != null && !TextUtils.isEmpty(content) && !TextUtils.isEmpty(createTime);
    }

    @Override
    public boolean equals(Object o) {
        if (((CCircleCommentModel) o).id == this.id) {
            return true;
        }
        return false;
    }
}
