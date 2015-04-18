package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.ViewUtils;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/11/2015.
 */
public class ContactListItemView extends LinearLayout {
    private View checkView;
    private SmartImageView smartImageView;
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
        this.smartImageView = (SmartImageView) this.findViewById(R.id.view_circle_contactlistitemview_image);
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
                this.checkView.setBackgroundResource(R.drawable.view_circle_checkcircle_uncheck);
                break;
            case 1:
                this.checkView.setBackgroundResource(R.drawable.view_circle_checkcircle_check);
                break;
            case -1:
                this.checkView.setBackgroundResource(R.drawable.view_circle_checkcircle_disable);
                break;
        }

        this.nameView.setText(title);
        this.smartImageView.setImageUrl(url);

        this.buttonView.setVisibility(GONE);
        this.addedView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
        this.nextView.setVisibility(GONE);
    }

    public void setNormalMode(final String uid, String title, String subtitle, String url, int resourceId, boolean next) {
        this.nameView.setText(title);
        if (resourceId > 0) {
            this.smartImageView.setImageResource(resourceId);
        } else {
            //if image url missing then use rongcloud to get real picture
            if (TextUtils.isEmpty(url) && MainApplication.rongClient != null) {
                MainApplication.rongClient.getUserInfo(uid, new RongIMClient.GetUserInfoCallback() {
                    @Override
                    public void onSuccess(final RongIMClient.UserInfo userInfo) {
                        Log.d("circle", "get user info success id: " + uid);
                        ChatManager.contactCache.put(userInfo.getUserId(), userInfo);
                        //TODO: save to DB
                        ViewUtils.delayExecute(new Runnable() {
                            @Override
                            public void run() {
                                if (!TextUtils.isEmpty(userInfo.getPortraitUri())) {
                                    smartImageView.setImageUrl(userInfo.getPortraitUri());
                                } else {
                                    smartImageView.setImageResource(R.drawable.view_circle_defaulticon);
                                }
                            }
                        }, 50);
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {
                        ViewUtils.delayExecute(new Runnable() {
                            @Override
                            public void run() {
                                smartImageView.setImageResource(R.drawable.view_circle_defaulticon);
                            }
                        }, 50);
                        Log.d("circle", "fail to get user info id: " + uid);
                    }
                });
            } else {
                smartImageView.setImageUrl(url);
            }


            this.smartImageView.setImageUrl(url);
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

    public void setAcceptMode(String title, String url, boolean mode) {
        this.nameView.setText(title);
        this.smartImageView.setImageUrl(url);

        this.checkView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
        this.addedView.setVisibility(mode ? VISIBLE : GONE);
        this.buttonView.setVisibility(mode ? GONE : VISIBLE);
        this.buttonView.setText("接受");
        this.nextView.setVisibility(GONE);
    }

    public void setRequestMode(String title, String url, OnClickListener onClickListener) {
        this.nameView.setText(title);
        this.smartImageView.setImageUrl(url);

        this.checkView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
//        this.reasonView.setVisibility(VISIBLE);
//        this.reasonView.setText(reason);
        if (onClickListener != null) {
            this.addedView.setVisibility(GONE);
            this.buttonView.setVisibility(VISIBLE);
            this.buttonView.setOnClickListener(onClickListener);
        } else {
            this.addedView.setVisibility(VISIBLE);
            this.buttonView.setVisibility(GONE);
        }

        this.buttonView.setText("增加");
        this.nextView.setVisibility(GONE);
    }
}
