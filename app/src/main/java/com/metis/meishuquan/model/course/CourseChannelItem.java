package com.metis.meishuquan.model.course;

/**
 * Created by wangjin on 15/4/18.
 */
public class CourseChannelItem {
    private String channelId;

    private String channelName;

    private boolean isChecked = false;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
