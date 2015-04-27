package com.metis.meishuquan.model.assess;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/27.
 */
public class AssessCommentImgData {

    @SerializedName("data")
    private List<AssessCommentImg> data;

    public List<AssessCommentImg> getData() {
        if (data == null) {
            data = new ArrayList<AssessCommentImg>();
        }
        return data;
    }

    public void setData(List<AssessCommentImg> data) {
        this.data = data;
    }
}
