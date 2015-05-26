package com.metis.meishuquan.model.topline;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wangjin on 15/5/2.
 */
public class RelatedRead implements Serializable {
    @SerializedName("newsId")
    private int newsId=0;

    @SerializedName("title")
    private String title = "";

    @SerializedName("modifyTime")
    private String modifyTime = "";

    @SerializedName("source")
    private String source = "";

    @SerializedName("pageViewCount")
    private int pageViewCount;

    @SerializedName("commentCount")
    private int commentCount;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
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
