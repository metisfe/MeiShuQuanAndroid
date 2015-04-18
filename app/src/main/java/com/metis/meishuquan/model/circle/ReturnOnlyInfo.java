package com.metis.meishuquan.model.circle;

import android.text.TextUtils;

import com.metis.meishuquan.model.contract.OptionSettings;

/**
 * Created by wudi on 4/18/2015.
 */
public class ReturnOnlyInfo {
    @com.google.gson.annotations.SerializedName("option")
    public OptionSettings option;

    public boolean isSuccess() {
        if (option != null && !TextUtils.isEmpty(option.status) && option.status.equals("0"))
            return true;
        return false;
    }
}
