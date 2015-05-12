package com.metis.meishuquan.adapter.commons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.List;

public class TopListAdapter extends BaseAdapter {

    private List<TopListItem> mData = null;
    private Context mContext = null;

    public TopListAdapter (Context context, List<TopListItem> data) {
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public TopListItem getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            View v = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.layout_active_top_list_item, null);
            holder.profileIv = (ImageView)v.findViewById(R.id.top_list_item_profile);
            holder.nameTv = (TextView)v.findViewById(R.id.top_list_item_name);
            holder.locationTv = (TextView)v.findViewById(R.id.top_list_item_location);
            holder.joinCountTv = (TextView)v.findViewById(R.id.top_list_item_join_count);
            holder.commentCountTv = (TextView)v.findViewById(R.id.top_list_item_comment_count);
            holder.supportCountTv = (TextView)v.findViewById(R.id.top_list_item_support_count);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        TopListItem item = getItem(i);
        holder.nameTv.setText(item.getUserNickName());
        holder.locationTv.setText(item.getProvince());
        ImageLoaderUtils.getImageLoader(mContext).displayImage(
                item.getUserAvatar(),
                holder.profileIv,
                ImageLoaderUtils.getRoundDisplayOptions(MainApplication.UIContext.getResources().getDimensionPixelSize(R.dimen.active_list_profile_size))
        );
        holder.joinCountTv.setText(
                MainApplication.UIContext.getString(R.string.act_joined_count, item.getRegisterCount())
        );
        holder.commentCountTv.setText(
                MainApplication.UIContext.getString(R.string.act_comment_count, item.getCommentCount())
        );
        holder.supportCountTv.setText(
                MainApplication.UIContext.getString(R.string.act_support_count, item.getUpCount())
        );
        return view;
    }

    public class ViewHolder {
        public RadioButton selectBtn;
        public ImageView profileIv;
        public TextView nameTv;
        public TextView locationTv;
        public TextView joinCountTv, commentCountTv, supportCountTv;
    }
}