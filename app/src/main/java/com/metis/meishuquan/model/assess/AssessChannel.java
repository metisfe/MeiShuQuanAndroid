package com.metis.meishuquan.model.assess;

import java.io.Serializable;

/**
 * Created by WJ on 2015/3/31.
 */
public class AssessChannel implements Serializable {
    private int channelId=0;

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
