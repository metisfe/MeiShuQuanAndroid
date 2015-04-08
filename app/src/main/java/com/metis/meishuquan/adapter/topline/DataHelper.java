package com.metis.meishuquan.adapter.topline;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.meishuquan.model.topline.AllChannel;
import com.metis.meishuquan.model.topline.ChannelItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/3/24.
 */
public class DataHelper {
    private String jsonStr = "";

    private static final String[] USER_CHANNEL = new String[]{"头条", "画室", "视频", "试卷",
            "联考", "校考", "专业", "志愿", "录取"};
    private static final String[] OTHER_CHANNEL = new String[]{"入学", "动态", "访谈", "网站",
            "播报"};


    public DataHelper() {
    }

    public DataHelper(String jsonStr) {
        this.jsonStr = jsonStr;
    }


    /**
     * 根据json串解析数据
     *
     * @return 用户已订阅频道
     */
    public List<ChannelItem> getUserChannels() {
        List<ChannelItem> lstUserItems = new ArrayList<ChannelItem>();
        try {
            Gson gson= new Gson();
            AllChannel allChannel=gson.fromJson(jsonStr,new TypeToken<AllChannel>(){}.getType());
            lstUserItems=allChannel.getSelectedChannels();
            Log.i("lstUserItems", String.valueOf(lstUserItems.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstUserItems;
    }

    /**
     * 根据json串解析数据
     *
     * @return 可订阅频道集合
     */
    public List<ChannelItem> getOtherChannels() {
        List<ChannelItem> lstOtherItems = new ArrayList<ChannelItem>();
        try {
            Gson gson= new Gson();
            AllChannel allChannel=gson.fromJson(jsonStr,new TypeToken<AllChannel>(){}.getType());
            lstOtherItems=allChannel.getUnSelectedChannels();
            Log.i("lstOtherItems", String.valueOf(lstOtherItems.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstOtherItems;
    }

    public List<ChannelItem> getLocalUserChannel() {
        List<ChannelItem> lstItems = new ArrayList<ChannelItem>();
        ChannelItem item1 = new ChannelItem(6, USER_CHANNEL[0], 1, true);
        ChannelItem item2 = new ChannelItem(17, USER_CHANNEL[1], 1, true);
        ChannelItem item3 = new ChannelItem(18, USER_CHANNEL[2], 1, true);
        ChannelItem item4 = new ChannelItem(19, USER_CHANNEL[3], 1, true);
        ChannelItem item5 = new ChannelItem(20, USER_CHANNEL[4], 1, true);
        ChannelItem item6 = new ChannelItem(21, USER_CHANNEL[5], 1, true);
        ChannelItem item7 = new ChannelItem(22, USER_CHANNEL[6], 1, true);
        ChannelItem item8 = new ChannelItem(23, USER_CHANNEL[7], 1, true);
        ChannelItem item9 = new ChannelItem(24, USER_CHANNEL[8], 1, true);
        lstItems.add(item1);
        lstItems.add(item2);
        lstItems.add(item3);
        lstItems.add(item4);
        lstItems.add(item5);
        lstItems.add(item6);
        lstItems.add(item7);
        lstItems.add(item8);
        lstItems.add(item9);
        return lstItems;
    }

    public List<ChannelItem> getLocalOtherChannel() {
        List<ChannelItem> lstItems = new ArrayList<ChannelItem>();
        ChannelItem item1 = new ChannelItem(25, OTHER_CHANNEL[0], 1, true);
        ChannelItem item2 = new ChannelItem(26, OTHER_CHANNEL[1], 1, true);
        ChannelItem item3 = new ChannelItem(27, OTHER_CHANNEL[2], 1, true);
        ChannelItem item4 = new ChannelItem(28, OTHER_CHANNEL[3], 1, true);
        ChannelItem item5 = new ChannelItem(29, OTHER_CHANNEL[4], 1, true);
        lstItems.add(item1);
        lstItems.add(item2);
        lstItems.add(item3);
        lstItems.add(item4);
        lstItems.add(item5);
        return lstItems;
    }

}
