package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jiaxh on 4/18/2015.
 */

public class CUserModel {
    @SerializedName("circleId")
    public long userId;

    @SerializedName("name")
    public String name;

    @SerializedName("remarkName")
    public String remarkName;

    @SerializedName("avatar")
    public String avatar;

    @SerializedName("grade")
    public String grade;

    @SerializedName("identity")
    public int identity;

    @SerializedName("relation")
    public int relation;
}
