package com.metis.meishuquan.model.topline;

import java.io.Serializable;

/**
 * ChannelItem
 * 
 * @author wj
 * 
 */

public class ChannelItem implements Serializable {

	private Integer channelId;
	private String channelName;
	private Integer orderNum;
	private boolean isAllowReset;

	public ChannelItem() {
	}

	public ChannelItem(int channelId, String channelName, int orderNum, boolean isAllowReset) {
		this.channelId = Integer.valueOf(channelId);
		this.channelName = channelName;
		this.orderNum = Integer.valueOf(orderNum);
		this.isAllowReset = isAllowReset;
	}

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public boolean isAllowReset() {
        return isAllowReset;
    }

    public void setAllowReset(boolean isAllowReset) {
        this.isAllowReset = isAllowReset;
    }

    @Override
    public String toString() {
        return "{'channelId':" + channelId +
                ", 'channelName':'" + channelName + '\'' +
                ", 'orderNum':" + orderNum +
                ", 'isAllowReset':" + isAllowReset +
                "}";
    }
}