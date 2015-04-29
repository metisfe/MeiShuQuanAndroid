package com.metis.meishuquan.model.course;

import java.io.Serializable;

/**
 * Created by wangjin on 15/4/17.
 */
public class CourseInfo implements Serializable {
    private Course data;

    public Course getData() {
        return data;
    }

    public void setData(Course data) {
        this.data = data;
    }
}
