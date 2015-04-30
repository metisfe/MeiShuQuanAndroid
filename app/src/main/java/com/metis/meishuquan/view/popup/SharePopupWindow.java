package com.metis.meishuquan.view.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * PopupWindow:分享
 * Created by wj on 15/4/7.
 */
public class SharePopupWindow extends PopupWindow {

    private static final String TAG = SharePopupWindow.class.getSimpleName();

    private boolean isInited = false;
    private UMSocialService mController = null;

    private String
            mTitle = "Let it be",
            mContent = "let it be",
            mTargetUrl = "http://blog.sina.com.cn/s/blog_5da93c8f0101o2k1.html",
            mImageUrl = "http://d8.sina.com.cn/pfpghc/4580073d1256498f97e2750165754027.jpg";

    public SharePopupWindow(final Activity mContext, View parent) {

        initYM(mContext);

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
                CircleShareContent content = new CircleShareContent();
                fillShareContent(mContext, content);
                mController.directShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        Toast.makeText(mContext, "share success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                WeiXinShareContent content = new WeiXinShareContent();
                fillShareContent(mContext, content);
                mController.directShare(mContext, SHARE_MEDIA.WEIXIN, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        Toast.makeText(mContext, "share success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                QZoneShareContent content = new QZoneShareContent();
                fillShareContent(mContext, content);
                mController.directShare(mContext, SHARE_MEDIA.QZONE, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        Toast.makeText(mContext, "share success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnSinaWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                //SinaShareContent
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void initYM (Activity context) {
        mController = UMServiceFactory.getUMSocialService("www.baidu.com");
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);

        initQZ(context);
        initWX(context);
        isInited = true;
    }

    private void initQZ (Activity context) {
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, "1104485283", "k9f8JhWppP5r1N5t");
        qZoneSsoHandler.addToSocialSDK();
    }

    private void initWX (Context context) {
        String appID = "wx144663d4ae48cdcf";
        String appSecret = "81daa257f2c448725dc737d656aa947d";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context,appID,appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context,appID,appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    public void setShareInfo (String title, String content, String targetUrl, String imageUrl) {
        mTitle = title;
        mContent = content;
        mTargetUrl = targetUrl;
        mImageUrl = imageUrl;
    }

    private void fillShareContent (Activity activity, BaseShareContent content) {
        content.setTitle(mTitle);
        content.setShareContent(mContent);
        content.setTargetUrl(mTargetUrl);
        content.setShareImage(new UMImage(activity, mImageUrl));
        mController.setShareMedia(content);
    }
}
