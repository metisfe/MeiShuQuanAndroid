package com.metis.meishuquan.model.circle;

import android.text.TextUtils;

/**
 * Created by jiaxh on 4/18/2015.
 */
public class CCircleCommentModel
{
    public int id ;
    public int circleId ;
    public CUserModel user ;
    public int supportCount ;
    public String content ;
    public String createTime;

    public boolean isValid()
    {
        return user != null && !TextUtils.isEmpty(content) && !TextUtils.isEmpty(createTime);
    }
}
