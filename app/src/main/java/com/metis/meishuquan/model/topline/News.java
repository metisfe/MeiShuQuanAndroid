package com.metis.meishuquan.model.topline;

/**
 * 实体类：新闻
 *
 * Created by WJ on 2015/3/24.
 */
public class News {

    private int newsId=0;
    private String title="";
    private String desc="";
    private String createTime="";
    private String imgUrl="";
    private int pageViewCount=0;//阅读数量
    private int commentCount=0;//评论数量

    private Source source;

    public News(){

    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "News{" +
                "newsId=" + newsId +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", createTime='" + createTime + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", pageViewCount=" + pageViewCount +
                ", commentCount=" + commentCount +
                ", source=" + source +
                '}';
    }
}
