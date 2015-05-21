package com.metis.meishuquan.adapter.circle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.MomentsGroup;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注群组列表适配器
 * <p/>
 * Created by wangjin on 15/5/2.
 */
public class CircleAttentionAdapter extends BaseAdapter {
    private Context context;
    private List<MomentsGroup> lstMomentsGroup;

    public CircleAttentionAdapter(Context context, int type) {
        this.context = context;
        this.lstMomentsGroup = getMomentsGroupInfo(type);
    }

    private List<MomentsGroup> getMomentsGroupInfo(int type) {
        ReturnInfo<List<MomentsGroup>> returnInfo = null;
        String json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.MOMENTS_GROUP_INFO);
        if (type == 0) {
            ReturnInfo<List<MomentsGroup>> result = new Gson().fromJson(json, new TypeToken<ReturnInfo<List<MomentsGroup>>>() {
            }.getType());
            returnInfo = result;
        } else if (type == 1) {
            returnInfo = new ReturnInfo<List<MomentsGroup>>();
            ReturnInfo<List<MomentsGroup>> result = new Gson().fromJson(json, new TypeToken<ReturnInfo<List<MomentsGroup>>>() {
            }.getType());
            List<MomentsGroup> list = new ArrayList<MomentsGroup>();

            MomentsGroup group1 = new MomentsGroup();
            group1.id = -1;
            group1.name = "全部";
            MomentsGroup group2 = new MomentsGroup();
            group2.id = -2;
            group2.name = "我的微博";
            list.add(group1);
            list.add(group2);

            for (int i = 0; i < result.getData().size(); i++) {
                list.add(result.getData().get(i));
            }
            MomentsGroup temp = list.remove(2);
            list.add(1, temp);
            returnInfo.setData(list);
        }
        return returnInfo.getData();
    }

    @Override
    public int getCount() {
        return lstMomentsGroup.size();
    }

    @Override
    public MomentsGroup getItem(int i) {
        return lstMomentsGroup.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstMomentsGroup.get(i).id;
    }

    class ViewHolder {
        TextView tvTitle;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_attention_group_list_item, null, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.id_view_circle_attention_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //赋值
        MomentsGroup group = lstMomentsGroup.get(i);
        holder.tvTitle.setText(group.name);
        return convertView;
    }
}
