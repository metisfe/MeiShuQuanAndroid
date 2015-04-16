package com.metis.meishuquan.model.enums;

/**
 * Created by xiaoxiao on 15/4/16.
 */
public enum CourseType {
    Recommend(1), NewPublish(2), HotCourse(3), Pic(4);
    private final int val;

    private CourseType(int val) {
        this.val = val;
    }

    public int getVal() {
        return this.val;
    }
}
