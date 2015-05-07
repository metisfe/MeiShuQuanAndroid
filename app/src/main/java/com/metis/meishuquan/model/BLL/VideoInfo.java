package com.metis.meishuquan.model.BLL;

import com.metis.meishuquan.adapter.ImgTitleSubImpl;

import java.io.Serializable;

/**
 * Created by WJ on 2015/5/6.
 */
public class VideoInfo implements Serializable, ImgTitleSubImpl {
    private int videoId;
    private int studioId;
    private String videoURL;
    private String videoPic;
    private boolean useState;
    private String createTime;

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(String videoPic) {
        this.videoPic = videoPic;
    }

    public boolean isUseState() {
        return useState;
    }

    public void setUseState(boolean useState) {
        this.useState = useState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getImageUrl() {
        return getVideoPic();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSubTitle() {
        return null;
    }
}
