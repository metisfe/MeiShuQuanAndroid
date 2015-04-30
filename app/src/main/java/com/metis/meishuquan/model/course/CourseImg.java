package com.metis.meishuquan.model.course;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wangjin on 15/4/30.
 */
public class CourseImg implements Serializable {

    @SerializedName("newsid")
    private int newsid;

    @SerializedName("dir")
    private String dir = "";

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;
    
    @SerializedName("thumbnails")
    private String thumbnails;

    public int getNewsid() {
        return newsid;
    }

    public void setNewsid(int newsid) {
        this.newsid = newsid;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }
}
