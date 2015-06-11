package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.common.BadgeView;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/6/2015.
 */
public class CircleChatListItemView extends LinearLayout {
    public RelativeLayout contentContainer;
    private ImageView imageView, imgFlag;
    private TextView titleView, contentView, timeView, customView;
    private BadgeView badge;

    public CircleChatListItemView(Context context, RongIMClient.Conversation conversation) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_circlechatlistitemview, this);
        this.contentContainer = (RelativeLayout) this.findViewById(R.id.id_rl_content_container);
        this.imageView = (ImageView) this.findViewById(R.id.views_circle_circlechatlistitemview_image);
        this.imgFlag = (ImageView) this.findViewById(R.id.id_num_flag);
        this.titleView = (TextView) this.findViewById(R.id.views_circle_circlechatlistitemview_title);
        this.contentView = (TextView) this.findViewById(R.id.views_circle_circlechatlistitemview_content);
        this.timeView = (TextView) this.findViewById(R.id.views_circle_circlechatlistitemview_time);
        this.customView = (TextView) this.findViewById(R.id.views_circle_custom_at_text);
        this.badge = new BadgeView(getContext(), imgFlag);
        setData(conversation);
    }

    public void setData(RongIMClient.Conversation conversation) {
        if (conversation.getConversationType() == RongIMClient.ConversationType.CUSTOMER_SERVICE) {
            this.titleView.setVisibility(View.GONE);
            this.contentView.setVisibility(View.GONE);
            this.timeView.setVisibility(View.GONE);
            this.imgFlag.setVisibility(VISIBLE);
            this.imgFlag.setImageResource(R.drawable.btn_right_arrow);

            this.customView.setVisibility(View.VISIBLE);
            this.customView.setText(conversation.getObjectName());
        } else if (conversation.getConversationType() != RongIMClient.ConversationType.CUSTOMER_SERVICE) {
            this.titleView.setVisibility(View.VISIBLE);
            this.contentView.setVisibility(View.VISIBLE);
            this.timeView.setVisibility(View.VISIBLE);
            this.imgFlag.setVisibility(GONE);

            this.customView.setVisibility(View.GONE);
            this.titleView.setText(ChatManager.getConversationTitle(conversation));
            this.contentView.setText(ChatManager.getLastString(conversation));
        }

        //标识未读数量
        int unReadCount = conversation.getUnreadMessageCount();
        if (unReadCount > 0) {
            Log.i("unReadCount", unReadCount + "");
            badge.setText(unReadCount + "");
            badge.setTextSize(10);
            badge.setBackgroundDrawable(badge.getDefaultBackground());
            badge.show();
        } else {
            badge.hide();
        }

        Time time = new Time();
        time.set(conversation.getReceivedTime() > conversation.getSentTime() ? conversation.getReceivedTime() : conversation.getSentTime());
        String s = time.hour >= 12 ? "下午" : "上午";
        String s2 = ":";
        if (time.minute < 10) s2 = ":0";
        this.timeView.setText(s + ' ' + (time.hour > 12 ? time.hour - 12 : time.hour) + s2 + time.minute);

        if (conversation.getConversationType() == RongIMClient.ConversationType.PRIVATE) {
            final String uid = conversation.getTargetId();
            String url = ChatManager.getUserInfo(uid).avatar;
            if (TextUtils.isEmpty(url)) {
                imageView.setImageResource(R.drawable.default_portrait_fang);
            } else {
                ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(url, imageView, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.default_portrait_fang));
            }
        } else if (conversation.getConversationType() == RongIMClient.ConversationType.CUSTOMER_SERVICE) {
            if (conversation.getObjectName().equals("@我的")) {
                imageView.setImageResource(R.drawable.icon_at_me);
            } else if (conversation.getObjectName().equals("评论我的")) {
                imageView.setImageResource(R.drawable.icon_comment_me);
            }
        } else {
            imageView.setImageResource(R.drawable.view_circle_groupicon);
        }
    }
}
