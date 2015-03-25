package com.metis.meishuquan.model.topline;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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
		return;
	}

	public static ChannelManage getManage(Context context) {
		if (channelManage == null)
			channelManage = new ChannelManage(context);
		return channelManage;
	}
}
