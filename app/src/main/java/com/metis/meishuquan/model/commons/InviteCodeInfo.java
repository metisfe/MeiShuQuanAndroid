package com.metis.meishuquan.model.commons;

import java.io.Serializable;

/**
 * Created by WJ on 2015/6/5.
 */
public class InviteCodeInfo implements Serializable {
    private long userid;
    private int invitationCodeNum;
    private int invitationCount;
    private String invitationDesc;
    private String invitationurl;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public int getInvitationCodeNum() {
        return invitationCodeNum;
    }

    public void setInvitationCodeNum(int invitationCodeNum) {
        this.invitationCodeNum = invitationCodeNum;
    }

    public int getInvitationCount() {
        return invitationCount;
    }

    public void setInvitationCount(int invitationCount) {
        this.invitationCount = invitationCount;
    }

    public String getInvitationDesc() {
        return invitationDesc;
    }

    public void setInvitationDesc(String invitationDesc) {
        this.invitationDesc = invitationDesc;
    }

    public String getInvitationurl() {
        return invitationurl;
    }

    public void setInvitationurl(String invitationurl) {
        this.invitationurl = invitationurl;
    }
}
