package com.metis.meishuquan.model.assess;

import java.io.Serializable;

/**
 * POJO:缩略图
 * <p/>
 * Created by WJ on 2015/3/31.
 */
public class Thumbnails implements Serializable {
    private String url = "";

    private int width = 0;

    private int heigth = 0;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }
}
