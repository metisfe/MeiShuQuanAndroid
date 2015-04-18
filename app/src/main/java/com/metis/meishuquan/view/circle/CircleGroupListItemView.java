package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by wudi on 4/18/2015.
 */
public class CircleGroupListItemView extends RelativeLayout {
    private ImageView imageView;
    private TextView titleView,countView;
    public CircleGroupListItemView(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_circlegrouplistitemview, this);
        this.imageView = (ImageView) this.findViewById(R.id.view_circle_cieclegrouplistitemview_icon);
        this.titleView = (TextView) this.findViewById(R.id.view_circle_cieclegrouplistitemview_title);
        this.countView = (TextView) this.findViewById(R.id.view_circle_cieclegrouplistitemview_count);
    }

    public void setData(String title,int count,String url)
    {
        this.titleView.setText(title);
        this.countView.setText("（" + String.valueOf(count) + "人）");
    }

}
