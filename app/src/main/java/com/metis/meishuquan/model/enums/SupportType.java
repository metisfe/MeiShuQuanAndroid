package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/13.
 */
public enum SupportType {
    Assess(1), AssessComment(2), News(3), NewsComment(4), Course(5), CourseComment(6), Circle(7), CircleComment(8);

    private final int val;

    private SupportType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
