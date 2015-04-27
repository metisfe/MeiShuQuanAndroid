package com.metis.meishuquan.model.commons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by WJ on 2015/4/9.
 */
public class Item implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("image")
    private String image;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private int type;

    @SerializedName("source")
    private String source;

    @SerializedName("pageViewCount")
    private int pageViewCount;

    @SerializedName("commentCount")
    private int commentCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getPageViewCount() {
        return pageViewCount;
    }

    public void setPageViewCount(int pageViewCount) {
        this.pageViewCount = pageViewCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
