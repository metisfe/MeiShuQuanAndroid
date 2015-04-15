package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/15.
 */
public enum LoginStateEnum {
    YES(0), NO(1);
    private final int val;

    private LoginStateEnum(int val) {
        this.val = val;
    }

    private int getVal() {
        return this.val;
    }
}
