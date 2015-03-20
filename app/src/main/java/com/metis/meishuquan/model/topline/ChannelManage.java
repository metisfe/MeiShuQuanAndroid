package com.metis.meishuquan.model.topline;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import com.metis.meishuquan.control.topline.ChannelControl;

public class ChannelManage {
	public static ChannelManage channelManage;
	/**
	 * Ĭ我的频道集合
	 * */
	public List<ChannelItem> defaultUserChannels;
	/**
	 * Ĭ更多频道集合
	 * */
	public List<ChannelItem> defaultOtherChannels;

	private ChannelControl channelControl;

	private boolean userExist = false;

	private void initDefaultData() {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
		defaultUserChannels.add(new ChannelItem(1, "测试1", 1, true));
		defaultUserChannels.add(new ChannelItem(2, "测试2", 2, true));
		defaultUserChannels.add(new ChannelItem(3, "测试3", 3, true));
		defaultUserChannels.add(new ChannelItem(4, "测试4", 4, true));
		defaultUserChannels.add(new ChannelItem(5, "测试5", 5, true));
		defaultUserChannels.add(new ChannelItem(6, "测试6", 6, true));
		defaultUserChannels.add(new ChannelItem(7, "测试7", 7, true));

		defaultOtherChannels.add(new ChannelItem(8, "测试8", 1, false));
		defaultOtherChannels.add(new ChannelItem(9, "测试9", 2, false));
		defaultOtherChannels.add(new ChannelItem(10, "测试10", 3, false));
		defaultOtherChannels.add(new ChannelItem(11, "测试11", 4, false));
		defaultOtherChannels.add(new ChannelItem(12, "测试12", 5, false));
		defaultOtherChannels.add(new ChannelItem(13, "测试13", 6, false));
		defaultOtherChannels.add(new ChannelItem(14, "测试14", 7, false));
		defaultOtherChannels.add(new ChannelItem(15, "测试15", 8, false));
		defaultOtherChannels.add(new ChannelItem(16, "测试16", 9, false));
		defaultOtherChannels.add(new ChannelItem(17, "测试17", 10, false));
		defaultOtherChannels.add(new ChannelItem(18, "测试18", 11, false));
	}

	private ChannelManage(Context context) {
		if (channelControl == null)
			channelControl = new ChannelControl(context);
		return;
	}

	public static ChannelManage getManage(Context context) {
		if (channelManage == null)
			channelManage = new ChannelManage(context);
		return channelManage;
	}

	/**
	 * 删除所有频道
	 */
	public void deleteAllChannel() {
		channelControl.clearFeedTable();
	}

	/**
	 * 获得我的频道
	 * 
	 * @return 我的频道集合
	 */
	public List<ChannelItem> getUserChannel() {

		defaultUserChannels = channelControl.getChannelCache(true);
		if (defaultUserChannels == null || defaultUserChannels.size() == 0) {
			initDefaultData();
			initDefaultChannel();
		}
		return defaultUserChannels;
	}

	/**
	 * 获得更多频道
	 * 
	 * @return 返回更多频道集合
	 */
	public List<ChannelItem> getOtherChannel() {
		// TODO:加载数据
		// defaultOtherChannels = channelControl.getChannelCache(false);
		if (defaultOtherChannels == null || defaultOtherChannels.size() == 0) {

		}
		return defaultOtherChannels;
	}

	/**
	 * 保存我的频道
	 * 
	 * @param userList
	 */
	public void saveUserChannel(List<ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) userList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(true);
			channelControl.addCache(channelItem);
		}
	}

	/**
	 * 保存更多频道
	 * 
	 * @param otherList
	 */
	public void saveOtherChannel(List<ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			ChannelItem channelItem = otherList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(false);
			channelControl.addCache(channelItem);
		}
	}

	/**
	 * 初始化默认频道
	 */
	private void initDefaultChannel() {
		deleteAllChannel();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
