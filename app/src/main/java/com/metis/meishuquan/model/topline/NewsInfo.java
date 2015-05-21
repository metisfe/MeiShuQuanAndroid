package com.metis.meishuquan.model.topline;

import com.metis.meishuquan.model.commons.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/3/26.
 */
public class NewsInfo implements Serializable {

    private static final String shareBaseUrl = "http://www.meishuquan.net/H5/ContentDetial.ASPX?ID=";

    private int newsId;

    private String title;

    private String subTitle;

    private String author;

    private Source source;

    private String commentEnable;

    private String modifyTime;

    private String content;

    private List<Urls> urls;

    private String commentDefaultText;

    private List<RelatedRead> relatedNewsList;

    private int supportCount;

    private int oppositionCount;

    private int commentCount;

    private UserMark userMark;

    private String description="";

    private String shareUrl = "http://www.meishuquan.net/H5/ContentDetial.ASPX?ID=";

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

    public User user;

    public List<RelatedRead> getRelatedNewsList() {
        if (relatedNewsList == null) {
            relatedNewsList = new ArrayList<RelatedRead>();
        }
        return relatedNewsList;
    }

    public void setRelatedNewsList(List<RelatedRead> relatedNewsList) {
        this.relatedNewsList = relatedNewsList;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public int getOppositionCount() {
        return oppositionCount;
    }

    public void setOppositionCount(int oppositionCount) {
        this.oppositionCount = oppositionCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public UserMark getUserMark() {
        return userMark;
    }

    public void setUserMark(UserMark userMark) {
        this.userMark = userMark;
    }

    public String getShareUrl() {
        String url = shareBaseUrl + newsId;
        return url;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
