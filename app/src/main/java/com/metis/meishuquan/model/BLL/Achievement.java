package com.metis.meishuquan.model.BLL;

import java.io.Serializable;

/**
 * Created by WJ on 2015/5/6.
 */
public class Achievement implements Serializable {
    private int achievementId;
    private int studioId;
    private String achievementTitle;
    private String achievementInfo;
    private String createTime;

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
}
