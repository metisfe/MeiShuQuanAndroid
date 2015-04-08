package com.metis.meishuquan.model.assess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/4/5.
 */
public class AllFileData {
    private List<FileData> data;

    public List<FileData> getData() {
        if (data == null) {
            data = new ArrayList<FileData>();
        }
        return data;
    }

    public void setData(List<FileData> data) {
        this.data = data;
    }
}
