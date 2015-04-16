package com.metis.meishuquan.model.commons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by WJ on 2015/4/16.
 */
public class Profile implements Serializable {

    @SerializedName("originalImage")
    private String originalImage;

    @SerializedName("thumbnails")
    private String thumbnails;

    @SerializedName("thumbnailsHeight")
    private int thumbnailsHeight;

    @SerializedName("thumbnailsWidth")
    private int thumbnailsWidth;

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

    /*{
        "originalImage": "http://metisdata.blob.core.windows.net/assesscontainer/afbcac32-57bc-4a8f-8295-60c9e9c01812.jpg",
            "thumbnails": "http://metisdata.blob.core.windows.net/assesscontainer/55d5e45e-ccf6-4248-9a9b-4b6e86e017a0.jpg",
            "thumbnailsHeight": 112,
            "thumbnailsWidth": 150
    }*/

    @Override
    public String toString() {
        String str = "{\"originalImage\":" + "\"" + originalImage + "\"" + "," +
                "\"thumbnails\":" + "\"" + thumbnails + "\"" + "," +
                "\"thumbnailsHeight\":" + thumbnailsHeight + "," +
                "\"thumbnailsWidth\":" + thumbnailsWidth + "}";
        return str;
    }
}
