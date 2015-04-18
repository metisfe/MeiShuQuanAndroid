package com.metis.meishuquan.model.contract;

/**
 * Created by wudi on 4/17/2015.
 */
public class ReturnGsonInfo<E> {

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

    @com.google.gson.annotations.SerializedName("data")
    private E data;

    public ReturnGsonInfo() {
    }

    public ReturnGsonInfo(E data) {
        this.data = data;
    }

    public E getData() {
        return this.data;
    }
}
