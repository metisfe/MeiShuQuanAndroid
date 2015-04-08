package com.metis.meishuquan.model.topline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxiao on 15/3/26.
 */
public class ToplineNewsList {
    private List<News> data;

    public List<News> getData() {
        if (data==null){
            data= new ArrayList<News>();
        }
        return data;
    }

    public void setData(List<News> data) {
        this.data = data;
    }
}
