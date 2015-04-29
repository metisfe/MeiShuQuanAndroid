package com.metis.meishuquan.model.topline;

import java.io.Serializable;

/**
 * Created by xiaoxiao on 15/3/26.
 */
public class Urls implements Serializable {
    private String newsUploadID;

    private String upType;

    private String newsid;

    private String dir;

    private String ext;

    private String time;

    private int width;

    private int height;

    private String description;

    private String newShowContent;

    private String thumbnails;

    public String getNewsUploadID() {
        return newsUploadID;
    }

    public void setNewsUploadID(String newsUploadID) {
        this.newsUploadID = newsUploadID;
    }

    public String getUpType() {
        return upType;
    }

    public void setUpType(String upType) {
        this.upType = upType;
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewShowContent() {
        return newShowContent;
    }

    public void setNewShowContent(String newShowContent) {
        this.newShowContent = newShowContent;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }
}
