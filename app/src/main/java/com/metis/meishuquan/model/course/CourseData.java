package com.metis.meishuquan.model.course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/17.
 */
public class CourseData {
    private List<Course> data;

    public List<Course> getData() {
        if (data == null) {
            data = new ArrayList<Course>();
        }
        return data;
    }

    public void setData(List<Course> data) {
        this.data = data;
    }
}
