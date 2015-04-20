package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/20.
 */
public enum PrivateTypeEnum {
    NEWS(0), NEWSCOMMENT(1), ASSESS(2), ASSESSCOMMENT(3), COURSE(4), COURSECOMMENT(5);
    private final int val;

    private PrivateTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
