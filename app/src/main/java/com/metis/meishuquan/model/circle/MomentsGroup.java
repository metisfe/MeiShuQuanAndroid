package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wangjin on 15/5/17.
 */
public class MomentsGroup implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("headIco")
    public String headIco;

    @SerializedName("founderUserId")
    public int founderUserId;

    @SerializedName("createTime")
    public String createTime;

    @SerializedName("groupType")
    public String groupType;
}
