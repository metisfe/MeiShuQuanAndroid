package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by wudi on 4/2/2015.
 */
public class CircleTitleBar extends RelativeLayout {
    private Button left, right;
    private TextView text;

    public CircleTitleBar(Context context) {
        super(context);
        init();
    }

    public CircleTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.views_circle_titlebar, this);
        left = (Button) this.findViewById(R.id.views_circle_titlebar_leftbutton);
        right = (Button) this.findViewById(R.id.views_circle_titlebar_rightbutton);
        text = (TextView) this.findViewById(R.id.views_circle_titlebar_text);
    }

    public void setText(String text) {
        this.text.setVisibility(VISIBLE);
        this.text.setText(text);
    }

    public void setLeft(String leftText, OnClickListener leftClick) {
        this.left.setVisibility(VISIBLE);
        this.left.setText(leftText);
        this.left.setOnClickListener(leftClick);
    }

    public void setRight(String rightText, OnClickListener rightClick) {
        this.right.setVisibility(VISIBLE);
        this.right.setText(rightText);
        this.right.setOnClickListener(rightClick);
    }

    public void hideLeft() {
        this.left.setVisibility(GONE);
    }

    public void hideRight() {
        this.right.setVisibility(GONE);
    }

    public void hideText() {
        this.text.setVisibility(GONE);
    }
}
