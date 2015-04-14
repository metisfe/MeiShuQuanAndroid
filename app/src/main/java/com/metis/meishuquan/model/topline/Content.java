package com.metis.meishuquan.model.topline;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wangjin on 15/4/14.
 */
public class Content {
    @SerializedName("Content")
    private String content = "";
    @SerializedName("ContentType")
    private String contentType = "";
    @SerializedName("URL")
    private String url = "";
    @SerializedName("ThumbnailsURL")
    private String thumbnailsURL = "";
    @SerializedName("Width")
    private int width = 0;
    @SerializedName("Height")
    private int height = 0;
    @SerializedName("Desc")
    private String desc = "";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailsURL() {
        return thumbnailsURL;
    }

    public void setThumbnailsURL(String thumbnailsURL) {
        this.thumbnailsURL = thumbnailsURL;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
