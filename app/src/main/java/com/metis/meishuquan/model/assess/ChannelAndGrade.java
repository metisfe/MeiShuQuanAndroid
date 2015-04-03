package com.metis.meishuquan.model.assess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/4/2.
 */
public class ChannelAndGrade {
    private List<Grade> gradeList = null;
    private List<Channel> channelList = null;

    public List<Grade> getGradeList() {
        if (gradeList == null) {
            gradeList = new ArrayList<>();
        }
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public List<Channel> getChannelList() {
        if (channelList == null) {
            channelList = new ArrayList<>();
        }
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }
}
