package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;

/**
 * Created by wudi on 4/7/2015.
 */
public class CircleGridIcon extends RelativeLayout {
    private SmartImageView imageView;
    private TextView textView;
    private View deleteIcon;
    public int type;

    public CircleGridIcon(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_circlegridicon, this);
        this.imageView = (SmartImageView) this.findViewById(R.id.view_circle_circlegridicon_image);
        this.textView = (TextView) this.findViewById(R.id.view_circle_circlegridicon_text);
        this.deleteIcon = this.findViewById(R.id.view_circle_circlegridicon_deleteicon);
    }

    public void setData(String url, String name) {
        this.imageView.setImageUrl(url);
        this.textView.setText(name);
        type = 0;
    }

    public void setPlusMinus(boolean plus) {
        this.imageView.setImageResource(plus ? R.drawable.view_circle_plus : R.drawable.view_circle_minus);
        this.textView.setText("");
        type = plus ? 1 : 2;
    }

    public void setEditMode(boolean editMode) {
        this.deleteIcon.setVisibility(editMode ? VISIBLE : GONE);
    }
}
