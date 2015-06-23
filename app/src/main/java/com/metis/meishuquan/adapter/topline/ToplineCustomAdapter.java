package com.metis.meishuquan.adapter.topline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.topline.News;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.List;

/**
 * Created by wj on 3/15/2015.
 */
public class ToplineCustomAdapter extends BaseAdapter {

    private int resourseId;
    private LayoutInflater mInflater;
    private List<News> lstData;
    private Context context;

    private ViewHolder holder;

    public ToplineCustomAdapter(Context context, int resourseId) {
//        super(context);
        this.context = context;
        this.resourseId = resourseId;
        this.mInflater = LayoutInflater.from(context);
    }

    public ToplineCustomAdapter(Context context, List<News> lstData) {
//        super(context);
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        News news = lstData.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_topline_topbar_list_item, null, false);

            holder.img_thumbnail = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
            holder.tv_readCount = (TextView) convertView.findViewById(R.id.tv_readcount);
            holder.tv_comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.img_thumbnail.setTag(news.getNewsId());
        }

        holder.img_thumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_topline_default_background));
        ImageLoaderUtils.getImageLoader(this.context).displayImage(news.getImgUrl().trim(), holder.img_thumbnail);
        holder.tv_title.setText(news.getTitle());
        String source = news.getSource().getTitle().trim();
        holder.tv_source.setText(source);
        holder.tv_readCount.setText("阅读(" + news.getPageViewCount() + ")");
        holder.tv_comment_count.setText("评论(" + news.getCommentCount() + ")");

        return convertView;
    }

    private static class ViewHolder {
        ImageView img_thumbnail;
        TextView tv_title, tv_source, tv_readCount, tv_comment_count;
    }
}
