package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

    public void setData(String url, String name, final String uid) {
        //for user himself
        if (uid.equals(ChatManager.userId)) {
            if (TextUtils.isEmpty(url)) {
                imageView.setImageResource(R.drawable.view_circle_defaulticon);
            } else {
                imageView.setImageUrl(url);
            }
        } else {
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
                                    imageView.setImageUrl(userInfo.getPortraitUri());
                                } else {
                                    imageView.setImageResource(R.drawable.view_circle_defaulticon);
                                }
                            }
                        }, 50);
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {
                        ViewUtils.delayExecute(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(R.drawable.view_circle_defaulticon);
                            }
                        }, 50);
                        Log.d("circle", "fail to get user info id: " + uid);
                    }
                });
            } else {
                this.imageView.setImageUrl(url);
            }
        }

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
