package com.metis.meishuquan.model.assess;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO:用于接收服务器返回的集合数据
 * <p/>
 * Created by WJ on 2015/3/31.
 */
public class AssessData {
    private List<Assess> lastAssessList;//最新点评

    private List<Assess> hotAssessList;//热门点评

    public List<Assess> getLastAssessLists() {
        if (lastAssessList == null) {
            lastAssessList = new ArrayList<>();
        }
        return lastAssessList;
    }

    public void setLastAssessLists(List<Assess> lastAssessLists) {
        this.lastAssessList = lastAssessLists;
    }

    public List<Assess> getHotAssessLists() {
        if (hotAssessList == null) {
            hotAssessList = new ArrayList<>();
        }
        return hotAssessList;
    }

    public void setHotAssessLists(List<Assess> hotAssessLists) {
        this.hotAssessList = hotAssessLists;
    }
}
