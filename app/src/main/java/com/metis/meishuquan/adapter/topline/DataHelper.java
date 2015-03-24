package com.metis.meishuquan.adapter.topline;

import android.util.Log;

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

    private static final String[] USER_CHANNEL = new String[]{"推荐", "热点", "素描", "色彩",
            "速写", "创作", "设计", "动漫", "摄影"};
    private static final String[] OTHER_CHANNEL = new String[]{"轻松一刻", "正能量", "另一面", "女人",
            "财经", "数码", "情感", "科技"};


    public DataHelper() {}
    public DataHelper(String jsonStr) {
        this.jsonStr = jsonStr;
    }


    /**
     * 根据json串解析数据
     *
     * @return 用户已订阅频道
     */
    public List<ChannelItem> getUserChannels() {
        List<ChannelItem> lstUserItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject chennels = jsonObject.getJSONObject("data");//得到两个集合（已选择频道集合，未选择频道集合）
            JSONArray selectedChennels = chennels.getJSONArray("SelectedChannels");
            for (int i = 0; i < selectedChennels.length(); i++) {
                JSONObject chennel = selectedChennels.getJSONObject(i);
                ChannelItem item = new ChannelItem();
                item.setChannelId(chennel.getInt("channelId"));
                item.setChannelName(chennel.getString("channelName"));
                item.setOrderNum(chennel.getInt("orderNum"));
                item.setAllowReset(chennel.getBoolean("isAllowReset"));
                lstUserItems.add(item);
            }
            Log.i("lstUserItems", String.valueOf(lstUserItems.size()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lstUserItems;
    }

    /**
     * 根据json串解析数据
     * @return 可订阅频道集合
     */
    public List<ChannelItem> getOtherChannels() {
        List<ChannelItem> lstOtherItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject chennels = jsonObject.getJSONObject("data");//得到两个集合（已选择频道集合，未选择频道集合）
            JSONArray unSelectedChennels = chennels.getJSONArray("unSelectedChannels");
            for (int i = 0; i < unSelectedChennels.length(); i++) {
                JSONObject chennel = unSelectedChennels.getJSONObject(i);
                ChannelItem item = new ChannelItem();
                item.setChannelId(chennel.getInt("channelId"));
                item.setChannelName(chennel.getString("channelName"));
                item.setOrderNum(chennel.getInt("orderNum"));
                item.setAllowReset(chennel.getBoolean("isAllowReset"));
                lstOtherItems.add(item);
            }
            Log.i("lstOtherItems", String.valueOf(lstOtherItems.size()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lstOtherItems;
    }

    public List<ChannelItem> getLocalUserChannel(){
        List<ChannelItem> lstItems= new ArrayList<>();
        ChannelItem item1=new ChannelItem(1, USER_CHANNEL[1], 1, true);
        ChannelItem item2=new ChannelItem(2, USER_CHANNEL[2], 2, true);
        ChannelItem item3=new ChannelItem(3, USER_CHANNEL[3], 3, true);
        ChannelItem item4=new ChannelItem(4, USER_CHANNEL[4], 4, true);
        lstItems.add(item1);
        lstItems.add(item2);
        lstItems.add(item3);
        lstItems.add(item4);
        return lstItems;
    }

    public List<ChannelItem> getLocalOtherChannel(){
        List<ChannelItem> lstItems= new ArrayList<>();
        ChannelItem item1=new ChannelItem(1, OTHER_CHANNEL[1], 1, true);
        ChannelItem item2=new ChannelItem(2, OTHER_CHANNEL[2], 2, true);
        ChannelItem item3=new ChannelItem(3, OTHER_CHANNEL[3], 3, true);
        ChannelItem item4=new ChannelItem(4, OTHER_CHANNEL[4], 4, true);
        lstItems.add(item1);
        lstItems.add(item2);
        lstItems.add(item3);
        lstItems.add(item4);
        return lstItems;
    }

}
