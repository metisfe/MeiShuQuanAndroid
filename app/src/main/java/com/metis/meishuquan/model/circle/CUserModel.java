package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.fragment.circle.FriendMatchFragment;

import java.io.Serializable;

/**
 * Created by jiaxh on 4/18/2015.
 */

public class CUserModel implements Serializable, FriendMatchFragment.UserInfoImpl {

    @SerializedName("userId")
    public int userId;

    @SerializedName("name")
    public String name;//昵称

    @SerializedName("remarkName")
    public String remarkName;//备注名

    @SerializedName("avatar")
    public String avatar;

    @SerializedName("grade")
    public String grade;

    @SerializedName("identity")
    public int identity;

    @SerializedName("")
    public int relation;

    @SerializedName("account")
    public String account;

    @SerializedName("rongCloud")
    public String rongCloud;

    @SerializedName("isFakeData")
    public boolean isFakeData;

    @SerializedName("userRole")
    public int userRole;

    @Override
    public String getUserId() {
        return userId + "";
    }

    @Override
    public String getUserName() {
        return name;
    }

    @Override
    public String getUserTelephone() {
        return "";
    }
}
