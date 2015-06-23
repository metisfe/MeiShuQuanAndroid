package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by wudi on 4/2/2015.
 */
public class CircleTitleBar extends RelativeLayout {
    private ViewGroup leftButton, rightButton;
    private TextView textView, leftTextView, rightTextView;
    private ImageView leftImageView, rightImageView;

    public CircleTitleBar(Context context) {
        super(context);
        init();
    }

    public CircleTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_titlebar, this);

        leftButton = (ViewGroup) this.findViewById(R.id.views_circle_titlebar_leftbutton);
        rightButton = (ViewGroup) this.findViewById(R.id.views_circle_titlebar_rightbutton);
        textView = (TextView) this.findViewById(R.id.views_circle_titlebar_text);

        leftTextView = (TextView) this.findViewById(R.id.views_circle_titlebar_leftbuttontext);
        rightTextView = (TextView) this.findViewById(R.id.views_circle_titlebar_rightbuttontext);

        leftImageView = (ImageView) this.findViewById(R.id.views_circle_titlebar_leftbuttonimage);
        rightImageView = (ImageView) this.findViewById(R.id.views_circle_titlebar_rightbuttonimage);
    }

    public void setText(String text) {
        this.textView.setVisibility(VISIBLE);
        this.textView.setText(text);
    }

    public void setLeftButton(String leftText, int resourceId, OnClickListener leftClick) {
        if (!TextUtils.isEmpty(leftText)) {
            this.leftTextView.setVisibility(VISIBLE);
            this.leftTextView.setText(leftText);
        }

        if (resourceId > 0) {
            this.leftImageView.setBackgroundResource(resourceId);
        }

        this.leftButton.setOnClickListener(leftClick);
    }

    public void setRightButton(String rightText, int resourceId, OnClickListener rightClick) {
        if (!TextUtils.isEmpty(rightText)) {
            this.rightTextView.setVisibility(VISIBLE);
            this.rightTextView.setText(rightText);
        } else {
            this.rightTextView.setVisibility(GONE);
        }

        if (resourceId != 0) {
            this.rightImageView.setImageResource(resourceId);
        } else {
            this.rightImageView.setVisibility(GONE);
        }

        this.rightButton.setOnClickListener(rightClick);
    }

    public void hideRight(){
        this.rightButton.setVisibility(GONE);
        this.rightTextView.setVisibility(GONE);
        this.rightImageView.setVisibility(GONE);
    }

    public void showRight(){
        this.rightButton.setVisibility(VISIBLE);
        this.rightTextView.setVisibility(VISIBLE);
        this.rightImageView.setVisibility(VISIBLE);
    }

    public void hideAll() {
        this.leftButton.setVisibility(GONE);
        this.rightButton.setVisibility(GONE);
        this.leftTextView.setVisibility(GONE);
        this.leftImageView.setVisibility(GONE);
        this.rightTextView.setVisibility(GONE);
        this.rightImageView.setVisibility(GONE);
        this.textView.setVisibility(GONE);
    }

}
