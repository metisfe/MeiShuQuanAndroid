package com.metis.meishuquan.model.contract;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wudi on 3/17/2015.
 */
public class ReturnInfo<E> {
    @SerializedName("option")
    private OptionSettings option;

    @SerializedName("data")
    private E data;

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

    public boolean isSuccess(){
        return option.isSuccess();
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
