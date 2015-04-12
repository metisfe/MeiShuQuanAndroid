package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.util.ChatManager;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/6/2015.
 */
public class CircleChatListItemView extends LinearLayout {
    private ImageView picView;
    private TextView titleView, contentView, timeView;

    public CircleChatListItemView(Context context, RongIMClient.Conversation conversation) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_circlechatlistitemview, this);
        this.picView = (ImageView) this.findViewById(R.id.views_circle_circlechatlistitemview_image);
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
    }
}
