package com.metis.meishuquan.model.commons;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WJ on 2015/5/6.
 */
public class Achievement implements Serializable {
    private int achievementId;
    private int studioId;
    private String achievementTitle;
    private String achievementInfo;
    private String createTime;

    private List<ImageItem> imglist;

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public String getAchievementTitle() {
        return achievementTitle;
    }

    public void setAchievementTitle(String achievementTitle) {
        this.achievementTitle = achievementTitle;
    }

    public String getAchievementInfo() {
        return achievementInfo;
    }

    public void setAchievementInfo(String achievementInfo) {
        this.achievementInfo = achievementInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ImageItem> getImglist() {
        return imglist;
    }

    public void setImglist(List<ImageItem> imglist) {
        this.imglist = imglist;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Achievement) {
            return ((Achievement) o).getAchievementId() == getAchievementId();
        }
        return false;
    }

    public static class ImageItem  implements Serializable {

        private String imgUrl, imgThumbnailUrl;
        private int imgWidth, imgHeight;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgThumbnailUrl() {
            return imgThumbnailUrl.trim();
        }

        public void setImgThumbnailUrl(String imgThumbnailUrl) {
            this.imgThumbnailUrl = imgThumbnailUrl;
        }

        public int getImgWidth() {
            return imgWidth;
        }

        public void setImgWidth(int imgWidth) {
            this.imgWidth = imgWidth;
        }

        public int getImgHeight() {
            return imgHeight;
        }

        public void setImgHeight(int imgHeight) {
            this.imgHeight = imgHeight;
        }
    }
}
