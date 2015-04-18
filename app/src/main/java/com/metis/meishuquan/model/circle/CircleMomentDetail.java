package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.model.contract.OptionSettings;

import java.util.List;

/**
 * Created by jx on 4/17/2015.
 */
public class CircleMomentDetail {

    @SerializedName("option")
    private OptionSettings option;

    public boolean isSuccess()
    {
        return option != null && "0".equals(option.status) && data != null;
    }

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

    @SerializedName("data")
    public CCircleTabModel data;
}
