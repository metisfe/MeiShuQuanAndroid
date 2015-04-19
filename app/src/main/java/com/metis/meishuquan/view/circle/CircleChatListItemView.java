package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.ViewUtils;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/6/2015.
 */
public class CircleChatListItemView extends LinearLayout {
    private SmartImageView smartImageView;
    private TextView titleView, contentView, timeView;

    public CircleChatListItemView(Context context, RongIMClient.Conversation conversation) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_circlechatlistitemview, this);
        this.smartImageView = (SmartImageView) this.findViewById(R.id.views_circle_circlechatlistitemview_image);
        this.titleView = (TextView) this.findViewById(R.id.views_circle_circlechatlistitemview_title);
        this.contentView = (TextView) this.findViewById(R.id.views_circle_circlechatlistitemview_content);
        this.timeView = (TextView) this.findViewById(R.id.views_circle_circlechatlistitemview_time);
        setData(conversation);
    }

    public void setData(RongIMClient.Conversation conversation) {
        this.titleView.setText(ChatManager.getConversationTitle(conversation));
        this.contentView.setText(ChatManager.getLastString(conversation));

        Time time = new Time();
        time.set(conversation.getReceivedTime() > conversation.getSentTime() ? conversation.getReceivedTime() : conversation.getSentTime());
        String s = time.hour >= 12 ? "下午" : "上午";
        String s2 = ":";
        if (time.minute < 10) s2 = ":0";
        this.timeView.setText(s + ' ' + (time.hour > 12 ? time.hour - 12 : time.hour) + s2 + time.minute);

        if (conversation.getConversationType() == RongIMClient.ConversationType.PRIVATE) {
            final String uid = conversation.getTargetId();
            String url = ChatManager.getUserInfo(uid).getPortraitUri();

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
                                if (smartImageView != null) {
                                    if (!TextUtils.isEmpty(userInfo.getPortraitUri())) {
                                        smartImageView.setImageUrl(userInfo.getPortraitUri());
                                    } else {
                                        smartImageView.setImageResource(R.drawable.view_circle_defaulticon);
                                    }
                                }
                            }
                        }, 50);
                    }

                    @Override
                    public void onError(ErrorCode errorCode) {
                        ViewUtils.delayExecute(new Runnable() {
                            @Override
                            public void run() {
                                if (smartImageView != null) {
                                    smartImageView.setImageResource(R.drawable.view_circle_defaulticon);
                                }
                            }
                        }, 50);
                        Log.d("circle", "fail to get user info id: " + uid);
                    }
                });
            } else {
                smartImageView.setImageUrl(url);
            }
        } else {
            smartImageView.setImageResource(R.drawable.view_circle_groupicon);
        }
    }
}
