package com.metis.meishuquan.view.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.metis.meishuquan.R;

/**
 * PopupWindow:分享
 * Created by wj on 15/4/7.
 */
public class SharePopupWindow extends PopupWindow {
    public SharePopupWindow(Context mContext, View parent) {

        View view = View.inflate(mContext, R.layout.popup_share, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
        GridLayout ll_popup = (GridLayout) view.findViewById(R.id.gl_share);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        super.update();

        Button btnMeishuquan = (Button) view.findViewById(R.id.id_btn_share_meishuquan);
        Button btnWeixinFriends = (Button) view.findViewById(R.id.id_btn_share_weixin_friends);
        Button btnWeixin = (Button) view.findViewById(R.id.id_btn_share_weixin);
        Button btnQzone = (Button) view.findViewById(R.id.id_btn_share_qq_zone);
        Button btnSinaWeibo = (Button) view.findViewById(R.id.id_btn_share_sina_weibo);
        Button btnCancel = (Button) view.findViewById(R.id.id_btn_cancel);

        btnMeishuquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnWeixinFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnSinaWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
