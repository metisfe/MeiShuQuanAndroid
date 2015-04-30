package com.metis.meishuquan.model.topline;

import java.util.List;

/**
 * Created by wj on 15/3/26.
 */
public class NewsInfo {

    private static final String shareBaseUrl = "http://www.meishuquan.net/H5/ContentDetial.ASPX?ID=";

    private String newsId;

    private String title;

    private String subTitle;

    private String author;

    private Source source;

    private String commentEnable;

    private String modifyTime;

    private String content;

    private List<Urls> urls;

    private String commentDefaultText;

    private List<RelatedNewsList> relatedNewsLists;

    private int commentCount;

    private String shareUrl = "http://www.meishuquan.net/H5/ContentDetial.ASPX?ID=";

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getCommentEnable() {
        return commentEnable;
    }

    public void setCommentEnable(String commentEnable) {
        this.commentEnable = commentEnable;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
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

    public String getCommentDefaultText() {
        return commentDefaultText;
    }

    public void setCommentDefaultText(String commentDefaultText) {
        this.commentDefaultText = commentDefaultText;
    }

    public List<RelatedNewsList> getRelatedNewsLists() {
        return relatedNewsLists;
    }

    public void setRelatedNewsLists(List<RelatedNewsList> relatedNewsLists) {
        this.relatedNewsLists = relatedNewsLists;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getShareUrl() {
        String url = shareBaseUrl + newsId;
        return url;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
