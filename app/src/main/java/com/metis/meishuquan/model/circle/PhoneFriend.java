package com.metis.meishuquan.model.circle;

import com.metis.meishuquan.model.circle.CPhoneFriend;
import com.metis.meishuquan.model.contract.OptionSettings;

import java.util.List;

/**
 * Created by wudi on 4/17/2015.
 */
public class PhoneFriend {

    @com.google.gson.annotations.SerializedName("option")
    public OptionSettings option;

    @com.google.gson.annotations.SerializedName("data")
    public List<CPhoneFriend> data;
}
