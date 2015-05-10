package com.metis.meishuquan.model.circle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/5/10.
 */
public class CirclePushBlogParm implements Serializable {

    private String Region = "";
    private String Content = "";
    private List<Integer> UserIds = null;
    private List<CircleImage> Images = null;
    private String Device = "";
    private int RelayId = 0;
    private int Type = 0;

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public List<Integer> getUserIds() {
        if (UserIds == null) {
            UserIds = new ArrayList<Integer>();
        }
        return UserIds;
    }

    public void setUserIds(List<Integer> userIds) {
        UserIds = userIds;
    }

    public List<CircleImage> getImages() {
        if (Images == null) {
            Images = new ArrayList<CircleImage>();
        }
        return Images;
    }

    public void setImages(List<CircleImage> images) {
        Images = images;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public int getRelayId() {
        return RelayId;
    }

    public void setRelayId(int relayId) {
        RelayId = relayId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
