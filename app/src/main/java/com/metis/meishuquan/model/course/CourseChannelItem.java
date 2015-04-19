package com.metis.meishuquan.model.course;

import java.io.Serializable;

/**
 * Created by wangjin on 15/4/18.
 */
public class CourseChannelItem implements Serializable {
    private int channelId;

    private String channelName;

    private boolean isChecked = false;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof CourseChannelItem) {
            return this.getChannelId() == ((CourseChannelItem) o).getChannelId();
        }
        return false;
    }
}
