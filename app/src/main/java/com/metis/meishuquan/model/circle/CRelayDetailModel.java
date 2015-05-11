package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangjin on 15/5/11.
 */
public class CRelayDetailModel implements Serializable {

    @SerializedName("user")
    public CUserModel user;

    @SerializedName("id")
    public int id;

    @SerializedName("type")
    public int type;

    @SerializedName("title")
    public String title;

    @SerializedName("desc")
    public String desc;

    @SerializedName("source")
    public String source;
    /**
     * 报名状态 仅仅非画室机构转发有效
     * 0无 1已报名 2不能修改
     */
    @SerializedName("status")
    public int status;

    @SerializedName("upCount")
    public int upCount;

    @SerializedName("joinCount")
    public int joinCount;

    @SerializedName("userMark")
    public CUserMarkModel userMark;

    @SerializedName("images")
    public List<CircleImage> images;
}
