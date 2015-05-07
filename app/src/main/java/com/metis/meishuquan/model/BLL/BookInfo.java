package com.metis.meishuquan.model.BLL;

import com.metis.meishuquan.adapter.ImgTitleSubImpl;

import java.io.Serializable;

/**
 * Created by WJ on 2015/5/6.
 */
public class BookInfo implements Serializable, ImgTitleSubImpl {
    private int booksId;
    private int studioId;
    private String bookName;
    private String bookPhoto;
    private String bookAuthor;
    private String shoppingUrl;
    private String createTime;

    public int getBooksId() {
        return booksId;
    }

    public void setBooksId(int booksId) {
        this.booksId = booksId;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPhoto() {
        return bookPhoto;
    }

    public void setBookPhoto(String bookPhoto) {
        this.bookPhoto = bookPhoto;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getShoppingUrl() {
        return shoppingUrl;
    }

    public void setShoppingUrl(String shoppingUrl) {
        this.shoppingUrl = shoppingUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getImageUrl() {
        return getBookPhoto();
    }

    @Override
    public String getTitle() {
        return getBookName();
    }

    @Override
    public String getSubTitle() {
        return getBookAuthor();
    }
}
