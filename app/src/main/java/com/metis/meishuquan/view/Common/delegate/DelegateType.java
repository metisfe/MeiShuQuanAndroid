package com.metis.meishuquan.view.Common.delegate;

import com.metis.meishuquan.R;

public enum DelegateType {

    DIVIDER_TITLE (100, R.layout.layout_student_item_header);

    private int mType, mLayoutId;

    DelegateType (int type, int layoutId) {
        mType = type;
        mLayoutId = layoutId;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public void setLayoutId(int mLayoutId) {
        this.mLayoutId = mLayoutId;
    }

    public static DelegateType getDelegateTypeByType (int type) {
        for (DelegateType t : DelegateType.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        throw new IllegalStateException("no DelegateType found for type " + type);
    }

    public static int getLayoutIdByType (int type) {
        for (DelegateType t : DelegateType.values()) {
            if (t.getType() == type) {
                return t.getLayoutId();
            }
        }
        throw new IllegalStateException("no layout found for type " + type);
    }
}