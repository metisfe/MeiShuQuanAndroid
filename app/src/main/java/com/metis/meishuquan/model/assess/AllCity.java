package com.metis.meishuquan.model.assess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/4/1.
 */
public class AllCity {

    private List<Province> data = null;

    public List<Province> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<Province> data) {
        this.data = data;
    }
}
