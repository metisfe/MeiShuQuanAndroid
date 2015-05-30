package com.metis.meishuquan.view.popup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.ReplyActivity;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;
import java.util.List;

/**
 * PopupWindow:分享
 * Created by wj on 15/4/7.
 */
public class SharePopupWindow extends PopupWindow {

    private static final String TAG = SharePopupWindow.class.getSimpleName();


    private boolean isInited = false;
    private UMSocialService mController = null;

    private String
            mInsideTitle = "",
            mOutsideTitle = "",
            mContent = "",
            mTargetUrl = "",
            mImageUrl = "";
    private int mType = 0;
    private int mShareId = 0;
    private CCircleDetailModel mMoment;
    private Context mContext;

    public SharePopupWindow(final Activity mContext, View parent) {
        this.mContext = mContext;
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

        //分享美术圈
        btnMeishuquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ReplyActivity.class);
                CirclePushBlogParm parm = new CirclePushBlogParm();

                if (mType == SupportTypeEnum.News.getVal()) {
                    parm.setType(mType);
                    parm.setRelayId(mShareId);
                    it.putExtra(ReplyActivity.PARM, parm);
                    it.putExtra(ReplyActivity.TITLE, mOutsideTitle);
                    it.putExtra(ReplyActivity.CONTENT, mContent);
                    it.putExtra(ReplyActivity.IMAGEURL, mImageUrl);
                }

                if (mMoment != null) {
                    if (mMoment.relayCircle != null && (mMoment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()
                            || mMoment.relayCircle.type == SupportTypeEnum.CircleActivity.getVal())) {
                        parm.setType(SupportTypeEnum.CircleActivity.getVal());
                        parm.setRelayId(mMoment.relayCircle.id);
                        parm.setLastCircleId(mMoment.id);
                    } else if (mMoment.relayCircle != null && mMoment.relayCircle.type == SupportTypeEnum.News.getVal()) {
                        parm.setType(SupportTypeEnum.News.getVal());
                        parm.setRelayId(mMoment.relayCircle.id);
                        parm.setLastCircleId(mMoment.id);
                    } else if (mMoment.relayCircle != null && mMoment.relayCircle.type == SupportTypeEnum.Circle.getVal()) {
                        parm.setType(SupportTypeEnum.Circle.getVal());
                        parm.setRelayId(mMoment.relayCircle.id);
                        parm.setLastCircleId(mMoment.id);
                        parm.setContent(mMoment.relayCircle.desc);
                        List<Integer> userIds = new ArrayList<Integer>();
                        userIds.add(mMoment.user.userId);
                        parm.setUserIds(userIds);
                    } else if (mMoment.relayCircle == null) {//转发圈子类型
                        parm.setType(SupportTypeEnum.Circle.getVal());
                        parm.setRelayId(mMoment.id);
                        parm.setLastCircleId(mMoment.id);
                        List<Integer> userIds = new ArrayList<Integer>();
                        userIds.add(mMoment.user.userId);
                        parm.setUserIds(userIds);
                        parm.setContent(mMoment.content);
                    }
                    it.putExtra(ReplyActivity.PARM, parm);
                    if (mMoment.relayCircle != null && (mMoment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()
                            || mMoment.relayCircle.type == SupportTypeEnum.CircleActivity.getVal()
                            || mMoment.relayCircle.type == SupportTypeEnum.News.getVal())) {
                        it.putExtra(ReplyActivity.TITLE, mMoment.relayCircle.title);
                        it.putExtra(ReplyActivity.CONTENT, mMoment.relayCircle.desc);
                        it.putExtra(ReplyActivity.IMAGEURL, mMoment.relayImgUrl);
                        it.putExtra(ReplyActivity.INPUT_CONTENT, "//@" + mMoment.user.name + " " + mMoment.content);
                    } else if (mMoment.relayCircle != null && mMoment.relayCircle.type == SupportTypeEnum.Circle.getVal()) {
                        it.putExtra(ReplyActivity.TITLE, "@" + mMoment.user.name);
                        it.putExtra(ReplyActivity.CONTENT, mMoment.relayCircle.desc);
                        it.putExtra(ReplyActivity.IMAGEURL, mMoment.relayImgUrl);
                        it.putExtra(ReplyActivity.INPUT_CONTENT, "//@" + mMoment.user.name + " " + mMoment.content);
                    } else if (mMoment.relayCircle == null) {
                        it.putExtra(ReplyActivity.TITLE, "@" + mMoment.user.name);
                        it.putExtra(ReplyActivity.CONTENT, mMoment.content);
                        it.putExtra(ReplyActivity.IMAGEURL, mMoment.relayImgUrl);
                        it.putExtra(ReplyActivity.INPUT_CONTENT, "//@" + mMoment.user.name + " " + mMoment.content);
                    }
                }

                mContext.startActivity(it);
                dismiss();

//                Intent it = new Intent(mContext, ReplyActivity.class);
//                CirclePushBlogParm parm = new CirclePushBlogParm();
//
////                mInsideTitle = "//@" + mMoment.user.name + " " + mMoment.content;
//
//                if (mMoment.relayCircle != null) {
//                    parm.setType(mMoment.relayCircle.type);
//                } else {
//                    parm.setType(SupportTypeEnum.Circle.getVal());
//                }
//
//                parm.setRelayId(mMoment.id);
//                it.putExtra(ReplyActivity.PARM, parm);
//                it.putExtra(ReplyActivity.TITLE, mInsideTitle);
//                it.putExtra(ReplyActivity.CONTENT, mContent);
//                it.putExtra(ReplyActivity.IMAGEURL, mImageUrl);
//                mContext.startActivity(it);

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
                        Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
                        sendShareInfoToUMeng("weixin_friend");
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
                        Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
                        sendShareInfoToUMeng("weixin_moments");
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
                        Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
                        sendShareInfoToUMeng("Qzone");
                    }
                });
            }
        });

        btnSinaWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent it = new Intent("join_succeed");
//                LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);
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

    private void initYM(Activity context) {
        mController = UMServiceFactory.getUMSocialService("www.baidu.com");
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);

        initQZ(context);
        initWX(context);
        isInited = true;
    }

    private void initQZ(Activity context) {
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, "1104485283", "k9f8JhWppP5r1N5t");
        qZoneSsoHandler.addToSocialSDK();
    }

    private void initWX(Context context) {
        String appID = "wx144663d4ae48cdcf";
        String appSecret = "81daa257f2c448725dc737d656aa947d";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, appID, appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, appID, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    public void setShareInfo(String title, String content, String targetUrl, String imageUrl) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(targetUrl) || TextUtils.isEmpty(content) || TextUtils.isEmpty(imageUrl)) {
            throw new IllegalArgumentException("title or targetUrl is Empty");
        }
        mOutsideTitle = title;
        mContent = content;
        mTargetUrl = targetUrl;
        mImageUrl = imageUrl;
        /*Log.v(TAG, "setShareInfo title=" + mOutsideTitle);
        Log.v(TAG, "setShareInfo content=" +  mContent);
        Log.v(TAG, "setShareInfo mTargetUrl=" + mTargetUrl);
        Log.v(TAG, "setShareInfo mImageUrl=" + mImageUrl);*/
    }

    //新闻模块调用
    public void setShareInfo(String title, String content, String targetUrl, String imageUrl, int type, int shareId) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(targetUrl) || TextUtils.isEmpty(content) || TextUtils.isEmpty(imageUrl)) {
            throw new IllegalArgumentException("title or targetUrl is Empty");
        }
        mOutsideTitle = title;
        mContent = content;
        mTargetUrl = targetUrl;
        mImageUrl = imageUrl;
        mType = type;
        mShareId = shareId;
    }

    //微博模块调用
    public void setShareInfo(String title, String content, String targetUrl, String imageUrl, CCircleDetailModel moment) {
        if (TextUtils.isEmpty(targetUrl) || TextUtils.isEmpty(content) || TextUtils.isEmpty(imageUrl)) {
            throw new IllegalArgumentException("title or targetUrl is Empty");
        }
        mInsideTitle = "@ " + moment.user.name;//分享至美术圈
        mOutsideTitle = "分享 " + moment.user.name + " 的内容";//分享至外面
        mContent = content;
        mTargetUrl = targetUrl;
        mImageUrl = imageUrl;
        mMoment = moment;
    }

    private void fillShareContent(Activity activity, BaseShareContent content) {
        content.setTitle(mOutsideTitle);
        content.setShareContent(mContent);
        content.setTargetUrl(mTargetUrl);
        content.setShareImage(new UMImage(activity, mImageUrl));
        mController.setShareMedia(content);
    }

    private void sendShareInfoToUMeng(String name) {
        UMPlatformData platform = new UMPlatformData(UMPlatformData.UMedia.SINA_WEIBO, String.valueOf(MainApplication.userInfo.getUserId()));
        platform.setGender(UMPlatformData.GENDER.MALE); //optional
        platform.setWeiboId(name);  //optional
        MobclickAgent.onSocialEvent(mContext, platform);
    }
}
