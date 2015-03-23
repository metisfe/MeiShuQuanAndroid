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

/**
 * Created by wudi on 3/15/2015.
 */
public class ToplineCustomAdapter extends ToplineAdapter {

    private int resourseId;
    private LayoutInflater mInflater;
    private Context context;

    private ImageView img_thumbnail;
    private TextView tv_title, tv_source_and_readcount, tv_comment_count;

    public ToplineCustomAdapter(Context context, int resourseId) {
        super(context);
        this.context = context;
        this.resourseId = resourseId;
        this.mInflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return 10;
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
            convertView = mInflater.inflate(this.resourseId, null, false);
            img_thumbnail = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_source_and_readcount = (TextView) convertView.findViewById(R.id.tv_source_and_readcount);
            tv_comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);
        }

        Bitmap bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.icon);
        img_thumbnail.setImageBitmap(bmp);
        tv_title.setText("俄方确认习近平将参加5月9日红场阅兵");
        tv_source_and_readcount.setText("消息来源"+" | "+"阅读("+100+")");
        tv_comment_count.setText("评论("+10+")");

        return convertView;
    }
}
