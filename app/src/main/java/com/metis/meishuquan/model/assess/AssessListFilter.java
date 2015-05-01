package com.metis.meishuquan.model.assess;

import com.metis.meishuquan.model.enums.AssessStateEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/5/2.
 */
public class AssessListFilter implements Serializable {
    private AssessStateEnum assessState = AssessStateEnum.ALL;
    private List<Grade> lstSelectedGrade;
    private List<Channel> lstSelectedChannel;

    private List<Integer> lstSelectedGradeIds;
    private List<Integer> lstSelectedChannelIds;

    public AssessStateEnum getAssessState() {
        return assessState;
    }

    public void setAssessState(AssessStateEnum assessState) {
        this.assessState = assessState;
    }

    public List<Grade> getLstSelectedGrade() {
        if (lstSelectedGrade == null) {
            lstSelectedGrade = new ArrayList<Grade>();
        }
        return lstSelectedGrade;
    }

    public void setLstSelectedGrade(List<Grade> lstSelectedGrade) {
        if (lstSelectedChannel == null) {
            lstSelectedChannel = new ArrayList<Channel>();
        }
        this.lstSelectedGrade = lstSelectedGrade;
    }

    public List<Channel> getLstSelectedChannel() {
        return lstSelectedChannel;
    }

    public void setLstSelectedChannel(List<Channel> lstSelectedChannel) {
        this.lstSelectedChannel = lstSelectedChannel;
    }

    public List<Integer> getLstSelectedGradeIds() {
        if (lstSelectedGrade != null) {
            lstSelectedGradeIds = new ArrayList<Integer>();
            for (int i = 0; i < lstSelectedGradeIds.size(); i++) {
                lstSelectedGradeIds.add(lstSelectedGrade.get(i).getId());
            }
        }
        return lstSelectedGradeIds;
    }

    public void setLstSelectedGradeIds(List<Integer> lstSelectedGradeIds) {
        this.lstSelectedGradeIds = lstSelectedGradeIds;
    }

    public List<Integer> getLstSelectedChannelIds() {
        if (lstSelectedChannel != null) {
            lstSelectedChannelIds = new ArrayList<Integer>();
            for (int i = 0; i < lstSelectedGradeIds.size(); i++) {
                lstSelectedChannelIds.add(lstSelectedChannel.get(i).getChannelId());
            }
        }
        return lstSelectedChannelIds;
    }

    public void setLstSelectedChannelIds(List<Integer> lstSelectedChannelIds) {
        this.lstSelectedChannelIds = lstSelectedChannelIds;
    }
}
