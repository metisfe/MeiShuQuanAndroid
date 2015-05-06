package com.metis.meishuquan.model.BLL;

import java.io.Serializable;

/**
 * Created by WJ on 2015/5/6.
 */
public class WorkInfo implements Serializable {
    private int studioAlbumId;
    private int studioId;
    private int albumType;
    private String photoUrl;
    private String photoThumbnail;
    private String createTime;
    private int photoClassify;

    public int getStudioAlbumId() {
        return studioAlbumId;
    }

    public void setStudioAlbumId(int studioAlbumId) {
        this.studioAlbumId = studioAlbumId;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public int getAlbumType() {
        return albumType;
    }

    public void setAlbumType(int albumType) {
        this.albumType = albumType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoThumbnail() {
        return photoThumbnail;
    }

    public void setPhotoThumbnail(String photoThumbnail) {
        this.photoThumbnail = photoThumbnail;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getPhotoClassify() {
        return photoClassify;
    }

    public void setPhotoClassify(int photoClassify) {
        this.photoClassify = photoClassify;
    }
}
