package com.metis.meishuquan.model.topline;

import java.io.Serializable;

/**
 * ChannelItem
 * 
 * @author wj
 * 
 */

public class ChannelItem implements Serializable {

	private static final long serialVersionUID = -6465237897027410019L;

	private Integer id;
	private String name;
	private Integer orderId;
	private boolean selected;

	public ChannelItem() {
	}

	public ChannelItem(int id, String name, int orderId, boolean selected) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
		this.selected = selected;
	}

	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	public boolean getSelected() {
		return this.selected;
	}

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	public void setSelected(boolean paramInteger) {
		this.selected = paramInteger;
	}

	public String toString() {
		return "ChannelItem [id=" + this.id + ", name=" + this.name + ",orderId="
				+ this.orderId + ", selected=" + this.selected + "]";
	}
}