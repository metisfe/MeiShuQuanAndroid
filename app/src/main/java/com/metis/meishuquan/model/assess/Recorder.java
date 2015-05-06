package com.metis.meishuquan.model.assess;

/**
 * Created by wangjin on 15/5/6.
 */
public class Recorder {
    private float time;
    private String path = "";

    public Recorder(float time, String path) {
        this.time = time;
        this.path = path;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
