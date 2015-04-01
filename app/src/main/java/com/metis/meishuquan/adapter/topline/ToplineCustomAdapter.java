package com.metis.meishuquan.adapter.topline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.topline.News;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by wj on 3/15/2015.
 */
public class ToplineCustomAdapter extends ToplineAdapter {

    private int resourseId;
    private LayoutInflater mInflater;
    private List<News> lstData;
    private Context context;

    private ViewHolder holder;
    //绑定数据
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (holder.img_thumbnail != null) {
                Bitmap bitmap = (Bitmap) msg.obj;
                holder.img_thumbnail.setImageBitmap(bitmap);
            }
        }
    };

    public ToplineCustomAdapter(Context context, int resourseId) {
        super(context);
        this.context = context;
        this.resourseId = resourseId;
        this.mInflater = LayoutInflater.from(context);
    }

    public ToplineCustomAdapter(Context context, List<News> lstData) {
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
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_topline_topbar_list_item, null, false);

            holder.img_thumbnail = (SmartImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_source_and_readcount = (TextView) convertView.findViewById(R.id.tv_source_and_readcount);
            holder.tv_comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News news = lstData.get(position);
        holder.img_thumbnail.setImageUrl(news.getImgUrl().trim());
        holder.tv_title.setText(news.getTitle());
        holder.tv_source_and_readcount.setText(news.getSource().getTitle() + " | " + "阅读(" + news.getPageViewCount() + ")");
        holder.tv_comment_count.setText("评论(" + news.getCommentCount() + ")");

        return convertView;
    }

    private static class ViewHolder {
        SmartImageView img_thumbnail;
        TextView tv_title, tv_source_and_readcount, tv_comment_count;
    }
}
