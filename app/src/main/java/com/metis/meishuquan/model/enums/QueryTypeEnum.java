package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/13.
 */
public enum QueryTypeEnum {
    RECOMMEND(3), HOT(2), NEW(1);
    private final int val;

    private QueryTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
