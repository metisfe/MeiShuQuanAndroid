package com.metis.meishuquan.model.course;

import com.metis.meishuquan.model.topline.Urls;

import java.util.List;

/**
 * Created by wangjin on 15/4/17.
 */
public class Course {
    private String coursePic;

    private int courseId;

    private String title;

    private String content;

    private Author author;

    private String createTime;

    private int viewCount;

    private int commentCount;

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

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Urls> getUrlss() {
        return urls;
    }

    public void setUrlss(List<Urls> urlss) {
        this.urls = urlss;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
