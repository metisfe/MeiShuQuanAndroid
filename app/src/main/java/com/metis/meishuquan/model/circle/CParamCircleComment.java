package com.metis.meishuquan.model.circle;

/**
 * Created by wangjin on 15/6/1.
 */
public class CParamCircleComment {
    private int Id = 0;
    private String Content = "";
    private int relyUserId = 0;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public int getRelyUserId() {
        return relyUserId;
    }

    public void setRelyUserId(int relyUserId) {
        this.relyUserId = relyUserId;
    }
}
