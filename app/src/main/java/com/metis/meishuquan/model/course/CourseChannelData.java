package com.metis.meishuquan.model.course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/18.
 */
public class CourseChannelData {
    private List<CourseChannel> data;

    public List<CourseChannel> getData() {
        if (data == null) {
            data = new ArrayList<CourseChannel>();
        }
        return data;
    }

    public void setData(List<CourseChannel> data) {
        this.data = data;
    }
}
