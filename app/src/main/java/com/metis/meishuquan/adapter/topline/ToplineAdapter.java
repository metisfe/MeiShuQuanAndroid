package com.metis.meishuquan.adapter.topline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.shared.ContractBaseAdapter;
import com.metis.meishuquan.model.contract.Moment;

/**
 * Created by wudi on 3/15/2015.
 */
public class ToplineAdapter extends ContractBaseAdapter<Moment> {

    private int resourseId;
    private LayoutInflater mInflater;
    private Context context;

    private ImageView img_thumbnail;
    private TextView tv_title, tv_source_and_readcount, tv_comment_count;

    public ToplineAdapter(Context context) {
        super(context);
    }

    public ToplineAdapter(Context context, int resourseId) {
        super(context);
        this.context = context;
        this.resourseId = resourseId;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getItemViewType(int position) {
        //Moment moment = (Moment) this.getItem(position);
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = mInflater.inflate(this.resourseId, null, false);
//            img_thumbnail = (ImageView) convertView.findViewById(R.id.img_thumbnail);
//            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//            tv_source_and_readcount = (TextView) convertView.findViewById(R.id.tv_source_and_readcount);
//            tv_comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);
//        }
//
//        Bitmap bmp = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.icon);
//        img_thumbnail.setImageBitmap(bmp);
//        tv_title.setText("俄方确认习近平将参加5月9日红场阅兵");
//        tv_source_and_readcount.setText("消息来源"+" | "+"阅读("+100+")");
//        tv_comment_count.setText("评论("+10+")");

        TextView view = new TextView(this.context);
        view.setText("hello world");
        view.setLayoutParams(new AbsListView.LayoutParams(1000, 1000));
        // this line cause the Cast Exception, change to use AbsListView's LayoutParams
        return view;
    }
}
