package com.metis.meishuquan.model.circle;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.util.Utils;

import java.util.List;

/**
 * Created by wudi on 4/14/2015.
 */
public class CCircleDetailModel {
    @SerializedName("id")
    public int id;

    @SerializedName("user")
    public CUserModel user;

    @SerializedName("createTime")
    public String createTime;

    @SerializedName("comentCount")
    public int comentCount;

    @SerializedName("relayCount")
    public int relayCount;

    @SerializedName("supportCount")
    public int supportCount;

    @SerializedName("device")
    public String device;

    @SerializedName("content")
    public String content;

    @SerializedName("viewCount")
    public int viewCount;

    @SerializedName("images")
    public List<CircleImage> images;

    @SerializedName("relayCircle")
    public CRelayDetailModel relayCircle;//等于null时，为非转发类型，不为null时，为转发类型

    @SerializedName("userMark")
    public CUserMarkModel userMark;

    public String getTimeText() {
        return Utils.getDateFromNow(Utils.getDate(createTime, null));
    }

    public String getDeviceText() {
        return !TextUtils.isEmpty(device) ? "来自 " + device : "";
    }

    public boolean isValid() {
        return user != null && user.name != null /* && user.grade != null */ && createTime != null && content != null;
    }
<<<<<<< HEAD
=======

    //TODO: add type
    //TODO: add original CCircleDetailModel if it's repost
>>>>>>> origin/master
}
