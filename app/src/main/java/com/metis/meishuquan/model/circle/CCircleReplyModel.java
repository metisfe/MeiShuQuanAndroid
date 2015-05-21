package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wangjin on 15/5/22.
 */
public class CCircleReplyModel {
    public int id;
    public CUserModel user;
    public String createTime;
    public int comentCount;
    public int relayCount;
    public int supportCount;
    public String device;
    public String content;
    public int viewCount;
    public int lastCircleId;
    public String remindUserlist;
    public int isTop;
    public List<CircleImage> images;
    public CRelayDetailModel relayCircle;
    public String relayImgUrl;
    public CUserMarkModel userMark;
    public CCircleCommentModel lastComment;
}
