package com.metis.meishuquan.model.commons;

import java.io.Serializable;

/**
 * Created by WJ on 2015/5/11.
 */
public class School implements Serializable {
    private int schoolId;
    private String schoolName;
    private String schoolLoction;

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolLoction() {
        return schoolLoction;
    }

    public void setSchoolLoction(String schoolLoction) {
        this.schoolLoction = schoolLoction;
    }
}
