package com.metis.meishuquan.model.course;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/18.
 */
public class CourseChannel {

    private static final String TAG = CourseChannel.class.getSimpleName();

    private int channelId;

    private String channelName;

    private List<CourseChannelItem> childChannelList;

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

    public List<CourseChannelItem> getChildChannelLists() {
        if (childChannelList == null) {
            childChannelList = new ArrayList<CourseChannelItem>();
        }
        return childChannelList;
    }

    public void setChildChannelLists(List<CourseChannelItem> childChannelLists) {
        this.childChannelList = childChannelLists;
    }

    public CourseChannelItem getItemById (int id) {
        for (CourseChannelItem item : childChannelList) {
            if (item.getChannelId() == id) {
                return item;
            }
        }
        return null;
    }

    public CourseChannelItem getFirstItem () {
        if (childChannelList == null || childChannelList.isEmpty()) {
            return null;
        }
        return childChannelList.get(0);
    }
    public CourseChannelItem getLastItem () {
        if (childChannelList == null || childChannelList.isEmpty()) {
            return null;
        }
        return childChannelList.get(childChannelList.size() - 1);
    }
}
