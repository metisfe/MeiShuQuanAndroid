package com.metis.meishuquan.model.commons;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by WJ on 2015/5/12.
 */
public class College implements Serializable {
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
}
