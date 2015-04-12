package com.metis.meishuquan.model.circle;

import android.os.Parcel;

import com.metis.meishuquan.util.Utils;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/11/2015.
 */
public class UserAdvanceInfo extends RongIMClient.UserInfo {
    private String pinYin;

    public UserAdvanceInfo(Parcel in) {
        super(in);
    }

    public UserAdvanceInfo(String userId, String name, String portraitUri) {
        super(userId, name, portraitUri);
        pinYin = Utils.toPinYinStringWithPrefix(name);
    }

    public UserAdvanceInfo(RongIMClient.UserInfo info) {
        super(info.getUserId(), info.getName(), info.getPortraitUri());
        pinYin = Utils.toPinYinStringWithPrefix(info.getName());
    }

    public String getPinYin() {
        return pinYin;
    }

}
