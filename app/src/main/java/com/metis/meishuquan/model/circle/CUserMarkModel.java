package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wangjin on 15/5/11.
 */
public class CUserMarkModel implements Serializable {

    @SerializedName("isFavorite")
    public boolean isFavorite;

    @SerializedName("isSupport")
    public boolean isSupport;// 是否赞

    @SerializedName("isOpposition")
    public boolean isOpposition;// 是否踩

    @SerializedName("isCanDel")
    public boolean isCanDel;

    @SerializedName("isReplyed")
    public boolean isReplyed;

    @SerializedName("isAttention")
    public boolean isAttention;//是否关注

    @SerializedName("isTop")
    public boolean isTop;
}
