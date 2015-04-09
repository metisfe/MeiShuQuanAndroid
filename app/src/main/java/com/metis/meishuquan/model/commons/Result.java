package com.metis.meishuquan.model.commons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by WJ on 2015/4/9.
 */
public class Result<T> implements Serializable {

    @SerializedName("option")
    private Option option;

    @SerializedName("data")
    private T data;

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
