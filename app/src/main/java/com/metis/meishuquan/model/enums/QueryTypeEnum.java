package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/13.
 */
public enum QueryTypeEnum {
    ALL(0), HOT(1), NEW(2);
    private final int val;

    private QueryTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
