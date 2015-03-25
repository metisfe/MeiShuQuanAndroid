package com.metis.meishuquan.adapter.topline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;

import java.util.List;

/**
 * Created by wudi on 3/15/2015.
 */
public class ToplineCustomAdapter extends ToplineAdapter {

    private int resourseId;
    private LayoutInflater mInflater;
    private List<String> lstData;
    private Context context;

    private ViewHolder holder;

    public ToplineCustomAdapter(Context context, int resourseId) {
        super(context);
        this.context = context;
        this.resourseId = resourseId;
        this.mInflater = LayoutInflater.from(context);
    }

    public ToplineCustomAdapter(Context context,List<String> lstData ) {
        super(context);
        this.context = context;
        this.lstData = lstData;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            holder= new ViewHolder();

            convertView = mInflater.inflate(R.layout.fragment_topline_topbar_list_item, null, false);
            holder.img_thumbnail = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_source_and_readcount = (TextView) convertView.findViewById(R.id.tv_source_and_readcount);
            holder.tv_comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Bitmap bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.icon);
        holder.img_thumbnail.setImageBitmap(bmp);
        holder.tv_title.setText(lstData.get(position));
        holder.tv_source_and_readcount.setText("消息来源"+" | "+"阅读("+100+")");
        holder.tv_comment_count.setText("评论("+10+")");

        return convertView;
    }

    private static class ViewHolder{
         ImageView img_thumbnail;
         TextView tv_title, tv_source_and_readcount, tv_comment_count;
    }
}
