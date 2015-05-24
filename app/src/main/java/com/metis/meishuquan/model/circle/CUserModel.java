package com.metis.meishuquan.model.circle;

import com.metis.meishuquan.fragment.circle.FriendMatchFragment;

import java.io.Serializable;

/**
 * Created by jiaxh on 4/18/2015.
 */

public class CUserModel implements Serializable, FriendMatchFragment.UserInfoImpl {

    public int userId;

    public String name;//昵称

    public String remarkName;//备注名

    public String avatar;

    public String grade;

    public int identity;

    public int relation;

    public String account;

    public String rongCloud;

    public boolean isFakeData;

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
