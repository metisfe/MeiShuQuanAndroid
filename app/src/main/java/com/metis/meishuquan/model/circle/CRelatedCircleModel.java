package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangjin on 15/6/4.
 */
public class CRelatedCircleModel implements Serializable {

    @SerializedName("aitelMeList")
    public List<CCircleDetailModel> aitelMeList;//@我的

    @SerializedName("commentList")
    public List<CCircleRelateMeDetail> commentList;//评论
}
