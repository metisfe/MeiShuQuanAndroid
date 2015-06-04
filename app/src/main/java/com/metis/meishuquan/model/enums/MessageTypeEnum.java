package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/6/4.
 */
public enum MessageTypeEnum {
    ATME(1), COMMENTME(3);
    private final int val;

    private MessageTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return this.val;
    }
}
