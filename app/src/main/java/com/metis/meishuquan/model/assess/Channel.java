package com.metis.meishuquan.model.assess;

import java.io.Serializable;

/**
 * POJO:标签
 * Created by wj on 15/4/2.
 */
public class Channel implements Serializable {
    private int channelId;

    private String channelName = "";

    private String thumbnails="";

    private boolean isChecked;//辅助字段

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

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Channel) {
            return this.getChannelId() == ((Channel) o).getChannelId();
        }
        return false;
    }
}
