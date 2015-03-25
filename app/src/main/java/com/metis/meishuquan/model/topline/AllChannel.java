package com.metis.meishuquan.model.topline;

import java.util.List;

/**
 * Created by WJ on 2015/3/25.
 */
public class AllChannel {
    private List<ChannelItem> selectedChannels;
    private List<ChannelItem> unSelectedChannels;

    public List<ChannelItem> getSelectedChannels() {
        return selectedChannels;
    }

    public void setSelectedChannels(List<ChannelItem> selectedChannels) {
        this.selectedChannels = selectedChannels;
    }

    public List<ChannelItem> getUnSelectedChannels() {
        return unSelectedChannels;
    }

    public void setUnSelectedChannels(List<ChannelItem> unSelectedChannels) {
        this.unSelectedChannels = unSelectedChannels;
    }
}
