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

    public String getInfo() {
        if (option != null && option.status != null) {
            return option.status;
        }
        return "";
    }

    public String getErrorCode() {
        if (option != null && option.errorCode != null) {
            return option.errorCode;
        }
        return "";
    }

    public String getMessage() {
        if (option != null && option.message != null) {
            return option.message;
        }
        return "";
    }

    @com.google.gson.annotations.SerializedName("data")
    public List<CPhoneFriend> data;
}
