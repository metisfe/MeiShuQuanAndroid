package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/15.
 */
public enum PrivateResultEnum {
    PRIVATE(1), CANCEL(2);
    private final int val;

    private PrivateResultEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
