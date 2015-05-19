package com.metis.meishuquan.model.commons;

import android.content.Context;

import com.metis.meishuquan.R;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by WJ on 2015/5/12.
 */
public class College implements Serializable {

    private static College sDefaultCollege = null;

    private int pId;
    private String name;
    private String description;
    private String createTime;

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public synchronized static College getDefaultOne (Context context) {
        if (sDefaultCollege == null) {
            sDefaultCollege = new College();
            sDefaultCollege.setpId(0);
            sDefaultCollege.setName(context.getString(R.string.all));
        }
        return sDefaultCollege;
    }
}
