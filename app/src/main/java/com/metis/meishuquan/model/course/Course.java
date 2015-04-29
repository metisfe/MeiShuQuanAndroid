package com.metis.meishuquan.model.course;

import com.metis.meishuquan.model.topline.Urls;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangjin on 15/4/17.
 */
public class Course implements Serializable {
    private String coursePic;

    private int courseId;

    private String title;

    private String content;

    private Author author;

    private String createTime;

    private int courseType;

    private int viewCount;

    private int commentCount;

    private int supportCount;

    private int OppositionCount;

    private String desc;

    private List<Urls> urls;

    public String getCoursePic() {
        return coursePic;
    }

    public void setCoursePic(String coursePic) {
        this.coursePic = coursePic;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public int getOppositionCount() {
        return OppositionCount;
    }

    public void setOppositionCount(int oppositionCount) {
        OppositionCount = oppositionCount;
    }

    public List<Urls> getUrls() {
        return urls;
    }

    public void setUrls(List<Urls> urls) {
        this.urls = urls;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
