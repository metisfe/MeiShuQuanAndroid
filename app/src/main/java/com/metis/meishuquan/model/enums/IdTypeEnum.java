package com.metis.meishuquan.model.enums;

/**
 * Enum:用户类型
 * Created by wj on 15/4/6.
 */
public enum IdTypeEnum {
    Student(1), Teacher(2), HuaShi(3), Parent(4), Other(5);

    private final int val;

    private IdTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

}
