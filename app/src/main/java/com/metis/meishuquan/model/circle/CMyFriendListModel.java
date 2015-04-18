package com.metis.meishuquan.model.circle;

import java.util.List;

/**
 * Created by wudi on 4/18/2015.
 */
public class CMyFriendListModel {

    @com.google.gson.annotations.SerializedName("myFirends")
    public List<CUserModel> myFirends;

    @com.google.gson.annotations.SerializedName("historyFirends")
    public List<CUserModel> historyFirends;
}
