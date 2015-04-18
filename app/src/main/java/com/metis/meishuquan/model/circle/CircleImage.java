package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jiaxh on 4/18/2015.
 */
public class CircleImage {
    @SerializedName("id")
    public int id;

    @SerializedName("originalImage")
    public String originalImage;

    @SerializedName("thumbnails")
    public String thumbnails ;

    @SerializedName("voiceUrl")
    public String voiceUrl ;

    @SerializedName("thumbnailsHeight")
    public int thumbnailsHeight ;

    @SerializedName("thumbnailsWidth")
    public int thumbnailsWidth ;

    @SerializedName("circleId")
    public int circleId ;
}
