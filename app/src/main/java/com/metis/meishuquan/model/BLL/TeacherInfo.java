package com.metis.meishuquan.model.BLL;

import com.metis.meishuquan.adapter.ImgTitleSubAdapter;
import com.metis.meishuquan.adapter.ImgTitleSubImpl;

import java.io.Serializable;

/**
 * Created by WJ on 2015/5/6.
 */
public class TeacherInfo implements Serializable, ImgTitleSubImpl {
    private int teacherId;
    private int studioId;
    private String teacherPhoto;
    private String teacherInfo;
    private String teacherName;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public String getTeacherPhoto() {
        return teacherPhoto;
    }

    public void setTeacherPhoto(String teacherPhoto) {
        this.teacherPhoto = teacherPhoto;
    }

    public String getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(String teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public String getImageUrl() {
        return getTeacherPhoto();
    }

    @Override
    public String getTitle() {
        return getTeacherName();
    }

    @Override
    public String getSubTitle() {
        return getTeacherInfo();
    }
}
