package com.metis.meishuquan.model.assess;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wangjin on 15/4/24.
 */
public class AssessCommentImg {

    @SerializedName("imgId")
    public int ImgId;

    @SerializedName("assessCommnetID")
    public int AssessCommnetID;

    @SerializedName("originalImage")
    public String OriginalImage = "";

    @SerializedName("thumbnails")
    public String Thumbnails = "";

    @SerializedName("voiceUrl")
    public String VoiceUrl = "";

    @SerializedName("thumbnailsHeight")
    public int ThumbnailsHeight;

    @SerializedName("thumbnailsWidth")
    public int ThumbnailsWidth;

    @SerializedName("voiceLength")
    public int VoiceLength;
}
