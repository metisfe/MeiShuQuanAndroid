package com.metis.meishuquan.model.topline;

import java.io.Serializable;

/**
 * Created by wangjin on 15/5/8.
 */
public class UserMark implements Serializable {

    private boolean isFavorite;//是否收藏
    private boolean isSupport;//是否已赞
    private boolean isOpposition;//是否已踩
    private boolean isCanDel;//是否删除
    private boolean isAttention;
    private boolean mutualAttention;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isSupport() {
        return isSupport;
    }

    public void setSupport(boolean isSupport) {
        this.isSupport = isSupport;
    }

    public boolean isOpposition() {
        return isOpposition;
    }

    public void setOpposition(boolean isOpposition) {
        this.isOpposition = isOpposition;
    }

    public boolean isCanDel() {
        return isCanDel;
    }

    public void setCanDel(boolean isCanDel) {
        this.isCanDel = isCanDel;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setIsAttention(boolean isAttention) {
        this.isAttention = isAttention;
    }

    public boolean isMutualAttention() {
        return mutualAttention;
    }

    public void setMutualAttention(boolean mutualAttention) {
        this.mutualAttention = mutualAttention;
    }
}
