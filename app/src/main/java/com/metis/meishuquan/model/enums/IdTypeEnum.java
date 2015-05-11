package com.metis.meishuquan.model.enums;

import com.metis.meishuquan.R;

/**
 * Enum:用户类型
 * Created by wj on 15/4/6.
 */
public enum IdTypeEnum {
    STUDENT(1, R.string.id_type_student),
    TEACHER(2, R.string.id_type_teacher),
    STUDIO(3, R.string.id_type_studio),
    PARENT(4, R.string.id_type_parent),
    OTHER(5, R.string.id_type_other);

    private final int val;
    private int stringRes;

    private IdTypeEnum(int val, int stringRes) {
        this.val = val;
        this.stringRes = stringRes;
    }

    public int getVal() {
        return val;
    }

    public static IdTypeEnum getUserRoleByType (int val) {
        for (IdTypeEnum e : values()) {
            if (e.val == val) {
                return e;
            }
        }
        return OTHER;
    }

    public int getStringResource () {
        return stringRes;
    }

}
