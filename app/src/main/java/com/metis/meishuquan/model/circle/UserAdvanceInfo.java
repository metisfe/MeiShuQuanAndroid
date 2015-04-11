package com.metis.meishuquan.model.circle;

import android.os.Parcel;
import android.util.Log;

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
        try {
            pinYin = Utils.toPinYinString(name);
        } catch (Exception e) {
            pinYin = "";
        }
    }

    public UserAdvanceInfo(RongIMClient.UserInfo info) {
        super(info.getUserId(), info.getName(), info.getPortraitUri());
        try {
            pinYin = Utils.toPinYinString(info.getName());
        } catch (Exception e) {
            pinYin = "";
        }
    }

    public String getPinYin() {
        return pinYin;
    }

}
