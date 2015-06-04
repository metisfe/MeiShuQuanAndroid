package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
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
}
