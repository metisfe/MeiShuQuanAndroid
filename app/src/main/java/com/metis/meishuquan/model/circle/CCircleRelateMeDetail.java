package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.util.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Model:评论我的详细信息
 *
 * Created by wangjin on 15/6/4.
 */
public class CCircleRelateMeDetail implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("user")
    public CUserModel user;

    @SerializedName("createTime")
    public String createTime;

    @SerializedName("device")
    public String device;

    @SerializedName("content")
    public String content;

    @SerializedName("images")
    public List<CircleImage> images;

    @SerializedName("relayImgUrl")
    public String relayImgUrl;// 转发图片

    @SerializedName("lastComment")
    public CCircleCommentModel lastComment;

    public String getTimeText() {
        return Utils.getDateFromNow(Utils.getDate(createTime, null));
    }
}
