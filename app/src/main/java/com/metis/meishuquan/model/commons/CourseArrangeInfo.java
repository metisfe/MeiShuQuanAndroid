package com.metis.meishuquan.model.commons;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by WJ on 2015/5/11.
 */
public class CourseArrangeInfo implements Serializable {
    private int courseId;
    private int studioId;
    private String courseName;
    private String courseInfo;
    private boolean useState;
    private Date courseBeginDate;
    private Date courseEndDate;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(String courseInfo) {
        this.courseInfo = courseInfo;
    }

    public boolean isUseState() {
        return useState;
    }

    public void setUseState(boolean useState) {
        this.useState = useState;
    }

    public Date getCourseBeginDate() {
        return courseBeginDate;
    }

    public void setCourseBeginDate(Date courseBeginDate) {
        this.courseBeginDate = courseBeginDate;
    }

    public Date getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(Date courseEndDate) {
        this.courseEndDate = courseEndDate;
    }
}
