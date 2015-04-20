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

    private List<Assess> selectAssessList;//精选推荐

    public List<Assess> getLastAssessList() {
        if (lastAssessList == null) {
            lastAssessList = new ArrayList<Assess>();
        }
        return lastAssessList;
    }

    public void setLastAssessList(List<Assess> lastAssessList) {
        this.lastAssessList = lastAssessList;
    }

    public List<Assess> getHotAssessList() {
        if (hotAssessList == null) {
            hotAssessList = new ArrayList<Assess>();
        }
        return hotAssessList;
    }

    public void setHotAssessList(List<Assess> hotAssessList) {
        this.hotAssessList = hotAssessList;
    }

    public List<Assess> getSelectAssessList() {
        if (selectAssessList == null) {
            selectAssessList = new ArrayList<Assess>();
        }
        return selectAssessList;
    }

    public void setSelectAssessList(List<Assess> selectAssessList) {
        this.selectAssessList = selectAssessList;
    }
}
