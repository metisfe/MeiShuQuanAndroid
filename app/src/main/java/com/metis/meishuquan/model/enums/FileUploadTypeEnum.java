package com.metis.meishuquan.model.enums;

/**
 * Created by wangjin on 15/4/15.
 */
public enum FileUploadTypeEnum {
    IMG(1), VOINCE(2);
    private final int val;

    private FileUploadTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
