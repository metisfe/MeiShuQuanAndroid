package com.metis.meishuquan.model.commons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WJ on 2015/4/21.
 */
public class Comment implements Serializable {
    /*
    * {
            "id": 34.0,
            "user": {
                "userId": 100058.0,
                "name": "测试",
                "avatar": "http://metisdata.blob.core.windows.net/assesscontainer/cfd0bc54-f20b-446b-95e3-97d4bd92f7b2.jpg",
                "rongCloud": "A1100638"
            },
            "isReplyed": true,
            "replyUser": [

            ],
            "thumbnails": {
                "url": "http://metisdata.blob.core.windows.net/assesscontainer/015b2391-8560-41ff-b122-7c66178f04f2.jpg",
                "width": 150.0,
                "heigth": 224.0
            },
            "originalImage": {
                "url": "http://metisdata.blob.core.windows.net/assesscontainer/856844a3-5315-4846-9b8d-ff8f3dd78380.jpg"
            },
            "desc": "ajgsdajkgdkalgdklasdgaljdgk",
            "assessChannel": {

            },
            "createTime": "2015-04-05 03:07:27"
        }
    * */
    @SerializedName("id")
    private int id;

    @SerializedName("user")
    private User user;

    @SerializedName("isReplyed")
    private boolean isReplyed;

    @SerializedName("replyUser")
    private List<User> replyUser;

    @SerializedName("thumbnails")
    private Thumbnail thumbnails;

    @SerializedName("originalImage")
    private OriginalImage originalImage;

    @SerializedName("desc")
    private String desc;

    //private

    @SerializedName("createTime")
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isReplyed() {
        return isReplyed;
    }

    public void setReplyed(boolean isReplyed) {
        this.isReplyed = isReplyed;
    }

    public List<User> getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(List<User> replyUser) {
        this.replyUser = replyUser;
    }

    public Thumbnail getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnail thumbnails) {
        this.thumbnails = thumbnails;
    }

    public OriginalImage getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(OriginalImage originalImage) {
        this.originalImage = originalImage;
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
}
