package com.metis.meishuquan.model.circle;

/**
 * Created by wudi on 4/17/2015.
 */
public class CPhoneFriend {

    @com.google.gson.annotations.SerializedName("Userid")
    public long Userid;

    @com.google.gson.annotations.SerializedName("PhoneNumber")
    public String PhoneNumber;

    @com.google.gson.annotations.SerializedName("IsFriend")
    public int IsFriend;

    @com.google.gson.annotations.SerializedName("UserNickName")
    public String UserNickName;

    @com.google.gson.annotations.SerializedName("UserAvatar")
    public String UserAvatar;
}
