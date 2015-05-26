package com.metis.meishuquan.view.circle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.util.ImageLoaderUtils;

/**
 * Created by wudi on 4/11/2015.
 */
public class ContactListItemView extends LinearLayout {
    private View checkView;
    private ImageView imageView;
    private TextView nameView, reasonView, addedView, buttonView;
    private ImageView nextView;

    public ContactListItemView(Context context) {
        super(context);
        init();
    }

    public ContactListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_contactlistitemview, this);
        this.checkView = (View) this.findViewById(R.id.view_circle_contactlistitemview_check);
        this.imageView = (ImageView) this.findViewById(R.id.view_circle_contactlistitemview_image);
        this.nameView = (TextView) this.findViewById(R.id.view_circle_contactlistitemview_name);
        this.reasonView = (TextView) this.findViewById(R.id.view_circle_contactlistitemview_reason);
        this.addedView = (TextView) this.findViewById(R.id.view_circle_contactlistitemview_added);
        this.buttonView = (TextView) this.findViewById(R.id.view_circle_contactlistitemview_confirm);
        this.nextView = (ImageView) this.findViewById(R.id.view_circle_contactlistitemview_next);
    }

    public void setCheckMode(String title, String url, int checkStatus) {
        this.checkView.setVisibility(VISIBLE);
        switch (checkStatus) {
            case 0:
                this.checkView.setBackgroundResource(R.drawable.bg_btn_single_uncheck);
                break;
            case 1:
                this.checkView.setBackgroundResource(R.drawable.bg_btn_single_check);
                break;
            case -1:
                this.checkView.setBackgroundResource(R.drawable.bg_btn_single_check);
                break;
        }

        this.nameView.setText(title);
        ImageLoaderUtils.getImageLoader(getContext()).displayImage(url, imageView);

        this.buttonView.setVisibility(GONE);
        this.addedView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
        this.nextView.setVisibility(GONE);
    }

    public void setNormalMode(final String uid, String title, String subtitle, String url, int resourceId, boolean next) {
        this.nameView.setText(title);
        if (resourceId > 0) {
            this.imageView.setImageResource(resourceId);
        } else {
            //if image url missing then use rongcloud to get real picture
            if (TextUtils.isEmpty(url)) {
                imageView.setImageResource(R.drawable.default_portrait_fang);
            } else {
                ImageLoaderUtils.getImageLoader(getContext()).displayImage(url, imageView, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.default_portrait_fang));
            }

            ImageLoaderUtils.getImageLoader(getContext()).displayImage(url, imageView, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.default_portrait_fang));
        }

        this.checkView.setVisibility(GONE);
        this.buttonView.setVisibility(GONE);
        this.addedView.setVisibility(GONE);
        if (TextUtils.isEmpty(subtitle)) {
            this.reasonView.setVisibility(GONE);
        } else {
            this.reasonView.setVisibility(VISIBLE);
            this.reasonView.setText(subtitle);
        }
        this.nextView.setVisibility(next ? VISIBLE : GONE);
    }

    public void setAcceptMode(String title, String url, String reason, int relation, OnClickListener onClickListener) {
        this.nameView.setText(title);
        ImageLoaderUtils.getImageLoader(getContext()).displayImage(url, imageView, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.default_portrait_fang));

        this.checkView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
//        this.reasonView.setText(reason);
        if (relation == 5) {
            this.addedView.setVisibility(GONE);
            this.buttonView.setVisibility(VISIBLE);
//            this.buttonView.setText("接受");
            this.buttonView.setWidth(50);
            this.buttonView.setHeight(25);
            this.buttonView.setBackgroundResource(R.drawable.bg_btn_accept);
            this.buttonView.setOnClickListener(onClickListener);
        } else if (relation == 2) {
            this.addedView.setVisibility(VISIBLE);
            this.buttonView.setVisibility(GONE);
        }

        this.nextView.setVisibility(GONE);
    }

    public void setProfileVisibility (int visibility) {
        this.imageView.setVisibility(visibility);
    }

    public void setRequestMode(String title, String url, OnClickListener onClickListener) {
        setRequestMode(title, url, "增加", onClickListener);
        /*this.nameView.setText(title);
        ImageLoaderUtils.getImageLoader(getContext()).displayImage(url, imageView);

        this.checkView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
        if (onClickListener != null) {
            this.addedView.setVisibility(GONE);
            this.buttonView.setVisibility(VISIBLE);
            this.buttonView.setOnClickListener(onClickListener);
        } else {
            this.addedView.setVisibility(VISIBLE);
            this.buttonView.setVisibility(GONE);
        }

        this.buttonView.setText("增加");
        this.nextView.setVisibility(GONE);*/
    }

    public void setRequestMode(String title, String url, String btnText, OnClickListener onClickListener) {
        this.nameView.setText(title);
        ImageLoaderUtils.getImageLoader(getContext()).displayImage(url, imageView);

        this.checkView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
        if (onClickListener != null) {
            this.addedView.setVisibility(GONE);
            this.buttonView.setVisibility(VISIBLE);
            this.buttonView.setOnClickListener(onClickListener);
        } else {
            this.addedView.setVisibility(VISIBLE);
            this.buttonView.setVisibility(GONE);
        }

        this.buttonView.setText(btnText);
        this.nextView.setVisibility(GONE);
    }
}
