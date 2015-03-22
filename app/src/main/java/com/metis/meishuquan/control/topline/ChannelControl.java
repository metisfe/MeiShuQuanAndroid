package com.metis.meishuquan.control.topline;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.topline.ChannelItem;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelControl{
	private SharedPreferences sp = null;
	private Editor editor = null;
    private List<ChannelItem> lstChannelItems;

    private TopLineOperator topLineOperator;

	public ChannelControl(Context context) {
		if (context != null) {
			sp = context.getSharedPreferences("channel", Context.MODE_PRIVATE);
			editor = sp.edit();
		}
	}

    /**
     * 添加缓存
     * @param item
     * @return
     */
	public boolean addCache(ChannelItem item) {
		boolean flag = false;
		if (editor != null) {
			editor.putString(String.valueOf(item.getSelected()),
					item.toString());
			flag = editor.commit();
		}
		return flag;
	}

    /**
     * 删除缓存
     * @param keys
     * @return
     */
	public boolean deleteCache(String[] keys) {
		boolean flag = false;
		if (editor != null) {
			for (int i = 0; i < keys.length; i++) {
				editor.remove(keys[i]);
			}
			flag = editor.commit();
		}
		return flag;
	}

    /**
     * 更新缓存
     * @param id
     * @param item
     * @return
     */
	public boolean updateCache(int id, ChannelItem item) {
		boolean flag = false;
		if (editor != null) {
			editor.remove(String.valueOf(id));
			addCache(item);
		}
		return flag;
	}

    /**
     * 获取缓存集合
     * @param seleced
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<ChannelItem> getChannelCache(boolean seleced) {
		List<ChannelItem> lstChannelItem = new ArrayList<ChannelItem>();

		Map<String, String> map=(Map<String, String>) sp.getAll();

		for (String key : map.keySet()) {
			if (key.equals(seleced)) {
				String itemStr=map.get(key);
				ChannelItem item=getChannelItemObjectByString(itemStr);
				lstChannelItem.add(item);
			}
		}
		return lstChannelItem;
	}

	private ChannelItem getChannelItemObjectByString(String itemStr){
		JSONTokener jsonParser = null;
		JSONObject jItem = null;
		ChannelItem item = null;
		try {
			jsonParser = new JSONTokener(itemStr);
			// 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
			jItem = (JSONObject) jsonParser.nextValue();
			// 接下来的就是JSON对象的操作了
			int id = jItem.getInt("id");
			String name = jItem.getString("name");
			int orderId = jItem.getInt("orderId");
			boolean selected = jItem.getBoolean("selected");

			item = new ChannelItem();
			item.setId(id);
			item.setName(name);
			item.setOrderId(orderId);
			item.setSelected(selected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	public void clearFeedTable() {
		if (editor != null) {
			editor.clear();
		}
	}
}
