package com.metis.meishuquan.model.contract;

/**
 * Created by wudi on 3/17/2015.
 */
public class ReturnInfo<E> {
    @com.google.gson.annotations.SerializedName("option")
    private OptionSettings option;


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

    private E data;
}
