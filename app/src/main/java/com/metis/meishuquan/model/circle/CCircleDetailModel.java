package com.metis.meishuquan.model.circle;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi on 4/14/2015.
 */
public class CCircleDetailModel {
    private static String shareUrl = "http://meishuquan.net/H5/CircleShare.aspx?id=";

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
    public int supportCount = 0;

    @SerializedName("device")
    public String device;

    @SerializedName("content")
    public String content;

    @SerializedName("viewCount")
    public int viewCount;

    @SerializedName("relayImgUrl")
    public String relayImgUrl;

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
        return user != null /*&& user.name != null*/ /* && user.grade != null */ && createTime != null && content != null;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public ArrayList<String> getImagesUrl() {
        ArrayList<String> imageUrls = new ArrayList<String>();
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                imageUrls.add(images.get(i).OriginalImage);
            }
        }
        return imageUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof CCircleDetailModel) {
            return ((CCircleDetailModel) o).id == id;
        }
        return false;
    }
}
