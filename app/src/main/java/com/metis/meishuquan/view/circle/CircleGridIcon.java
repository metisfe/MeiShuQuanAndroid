package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;

/**
 * Created by wudi on 4/7/2015.
 */
public class CircleGridIcon extends LinearLayout {
    private SmartImageView imageView;
    private TextView textView;

    public CircleGridIcon(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_circlegridicon, this);
        this.imageView = (SmartImageView) this.findViewById(R.id.view_circle_circlegridicon_image);
        this.textView = (TextView) this.findViewById(R.id.view_circle_circlegridicon_text);
    }

    public void setData(String url, String name, OnClickListener onClickListener) {
        this.imageView.setImageUrl(url);
        this.textView.setText(name);
        this.setOnClickListener(onClickListener);
    }

    public void setPlusMinus(boolean plus, OnClickListener onClickListener) {
        this.setOnClickListener(onClickListener);
        this.imageView.setImageResource(plus ? R.drawable.view_circle_plus : R.drawable.view_circle_minus);
        this.textView.setText("");
    }
}
