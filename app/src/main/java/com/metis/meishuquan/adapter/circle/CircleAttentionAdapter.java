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
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.circle.MomentsGroup;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注群组列表适配器
 * <p/>
 * Created by wangjin on 15/5/2.
 */
public class CircleAttentionAdapter extends BaseAdapter {
    private Context context;
    private List<MomentsGroup> lstMomentsGroup=new ArrayList<MomentsGroup>();
    private String json = "";

    public CircleAttentionAdapter(Context context, int type) {
        this.context = context;
        getMomentsGroupInfo(type);
    }

    private void getMomentsGroupInfo(final int type) {
        json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.MOMENTS_GROUP_INFO);
        if (json.isEmpty()) {
            CommonOperator.getInstance().getMomentsGroups(new ApiOperationCallback<ReturnInfo<String>>() {
                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null && result.isSuccess()) {
                        json = new Gson().toJson(result);
                        lstMomentsGroup = getLstMomentsGroupByJson(type, json);
                        SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(SharedPreferencesUtil.MOMENTS_GROUP_INFO,json);
                        notifyDataSetChanged();
                    }
                }
            });
        } else {
            lstMomentsGroup.clear();
            lstMomentsGroup = getLstMomentsGroupByJson(type, json);
        }
    }

    private List<MomentsGroup> getLstMomentsGroupByJson(int type, String json) {
        ReturnInfo<List<MomentsGroup>> returnInfo = new ReturnInfo<List<MomentsGroup>>();
        List<MomentsGroup> groups = new ArrayList<MomentsGroup>();
        if (type == 0) {
            ReturnInfo<List<MomentsGroup>> result = new Gson().fromJson(json, new TypeToken<ReturnInfo<List<MomentsGroup>>>() {
            }.getType());
            groups.addAll(result.getData());
            for (MomentsGroup group : result.getData()) {
                if (group.name.equals("全部")) {
                    groups.remove(group);
                } else if (group.name.equals("朋友圈")) {
                    groups.remove(group);
                } else if (group.name.equals("我的微博")) {
                    groups.remove(group);
                }
            }
            returnInfo.setData(groups);
        } else if (type == 1) {
            ReturnInfo<List<MomentsGroup>> result = new Gson().fromJson(json, new TypeToken<ReturnInfo<List<MomentsGroup>>>() {
            }.getType());
            returnInfo.setData(result.getData());
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
