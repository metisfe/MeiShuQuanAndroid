package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/16.
 */
public enum RequestCodeTypeEnum {
    REGISTOR(1), RESETPWD(2);
    private final int val;

    private RequestCodeTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return this.val;
    }
}
