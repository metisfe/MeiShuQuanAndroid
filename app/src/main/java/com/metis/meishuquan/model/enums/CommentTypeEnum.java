package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/22.
 */
public enum CommentTypeEnum {

    Image(1), Voice(2), Text(3), Portrait(4);

    private final int val;

    private CommentTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
