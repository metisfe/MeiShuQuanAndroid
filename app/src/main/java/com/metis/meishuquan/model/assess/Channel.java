package com.metis.meishuquan.model.assess;

/**
 * POJO:标签
 * Created by wj on 15/4/2.
 */
public class Channel {
    private int channelId;

    private String channelName="";

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
