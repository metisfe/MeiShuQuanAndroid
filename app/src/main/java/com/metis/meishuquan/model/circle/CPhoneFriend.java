package com.metis.meishuquan.model.circle;

/**
 * Created by wudi on 4/17/2015.
 */
public class CPhoneFriend {

    @com.google.gson.annotations.SerializedName("userid")
    public long userid;

    @com.google.gson.annotations.SerializedName("phoneNumber")
    public String phoneNumber;

    @com.google.gson.annotations.SerializedName("isFriend")
    public int isFriend;

    @com.google.gson.annotations.SerializedName("userNickName")
    public String userNickName;

    @com.google.gson.annotations.SerializedName("userAvatar")
    public String userAvatar;
}
