package com.metis.meishuquan.model.assess;

/**
 * Created by wj on 15/3/31.
 */
public class AllAssess {
    private AssessData data;

    public AssessData getData() {
        if (data == null) {
            data = new AssessData();
        }
        return data;
    }

    public void setData(AssessData data) {
        this.data = data;
    }
}
