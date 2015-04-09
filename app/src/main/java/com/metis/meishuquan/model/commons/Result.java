package com.metis.meishuquan.model.commons;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by WJ on 2015/4/9.
 */
public class Result implements Serializable {

    @SerializedName("option")
    private Option option;

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
