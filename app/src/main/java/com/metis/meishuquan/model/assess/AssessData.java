package com.metis.meishuquan.model.assess;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO:用于接收服务器返回的集合数据
 *
 * Created by WJ on 2015/3/31.
 */
public class AssessData {
    private List<Assess> lastAssessLists ;

    private List<Assess> hotAssessLists ;

    public List<Assess> getLastAssessLists() {
        if (lastAssessLists==null){
            lastAssessLists= new ArrayList<>();
        }
        return lastAssessLists;
    }

    public void setLastAssessLists(List<Assess> lastAssessLists) {
        this.lastAssessLists = lastAssessLists;
    }

    public List<Assess> getHotAssessLists() {
        if (hotAssessLists==null){
            hotAssessLists=new ArrayList<>();
        }
        return hotAssessLists;
    }

    public void setHotAssessLists(List<Assess> hotAssessLists) {
        this.hotAssessLists = hotAssessLists;
    }
}
