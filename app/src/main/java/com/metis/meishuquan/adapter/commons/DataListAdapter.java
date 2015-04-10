package com.metis.meishuquan.adapter.commons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.commons.Item;

import java.util.List;

/**
 * Created by WJ on 2015/4/10.
 */
public class DataListAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Item> mData = null;
    private LayoutInflater mInflater = null;

    public DataListAdapter (Context context, List<Item> dataList) {
        mContext = context;
        mData = dataList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_topline_topbar_list_item, null, false);
            holder = new ViewHolder();
            holder.img_thumbnail = (SmartImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
            holder.tv_readCount = (TextView) convertView.findViewById(R.id.tv_readcount);
            holder.tv_comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        Item item = (Item)getItem(position);
        holder.img_thumbnail.setImageUrl(item.getImage());
        holder.tv_title.setText(item.getTitle());
        holder.tv_readCount.setText(mContext.getString(R.string.collections_read_count, item.getReadCount()));
        holder.tv_comment_count.setText(mContext.getString(R.string.collections_read_count, item.getCommentCount()));
        holder.tv_source.setText(item.getSource());
        return convertView;
    }

    private static class ViewHolder {
        SmartImageView img_thumbnail;
        TextView tv_title, tv_source, tv_readCount, tv_comment_count;
    }
}
