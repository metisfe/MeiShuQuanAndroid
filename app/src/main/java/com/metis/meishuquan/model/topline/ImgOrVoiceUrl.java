package com.metis.meishuquan.model.topline;

/**
 * Created by xiaoxiao on 15/3/29.
 */
public class ImgOrVoiceUrl {

    private int imgId;

    private int assessCommnetID;

    private String originalImage;

    private String thumbnails;

    private String voiceUrl;

    private int thumbnailsHeight;

    private int thumbnailsWidth;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getAssessCommnetID() {
        return assessCommnetID;
    }

    public void setAssessCommnetID(int assessCommnetID) {
        this.assessCommnetID = assessCommnetID;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public int getThumbnailsHeight() {
        return thumbnailsHeight;
    }

    public void setThumbnailsHeight(int thumbnailsHeight) {
        this.thumbnailsHeight = thumbnailsHeight;
    }

    public int getThumbnailsWidth() {
        return thumbnailsWidth;
    }

    public void setThumbnailsWidth(int thumbnailsWidth) {
        this.thumbnailsWidth = thumbnailsWidth;
    }
}
