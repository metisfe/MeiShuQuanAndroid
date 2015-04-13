package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/13.
 */
public enum AssessStateEnum {
    ALL(1), ASSESSED(2), UNASSESS(3);
    private final int val;

    private AssessStateEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
