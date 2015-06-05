package com.metis.meishuquan.activity.info;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.manager.common.QrCodeMakerTask;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.InviteCodeInfo;
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

public class InviteActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = InviteActivity.class.getSimpleName();

    public static final String KEY_INVITE_CODE_INFO = "invite_code_info";

    private ImageView mQrIv = null;
    private TextView mContentTv, mDownloadCountTv, mMyCodeTv;
    private View mShareWeChatView, mShareWeiboView, mShareQQView, mShareFriendsView;

    private UMSocialService mController = null;
    private Bitmap mQrCodeBmp = null;

    private InviteCodeInfo mCodeInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        mQrIv = (ImageView)findViewById(R.id.invite_qr_code);
        mContentTv = (TextView)findViewById(R.id.invite_content);
        mDownloadCountTv = (TextView)findViewById(R.id.invite_download_count);
        mMyCodeTv = (TextView)findViewById(R.id.invite_my_code);
        mShareWeChatView = findViewById(R.id.invite_share_wechat);
        mShareWeiboView = findViewById(R.id.invite_share_weibo);
        mShareQQView = findViewById(R.id.invite_share_qq);
        mShareFriendsView = findViewById(R.id.invite_share_friends);

        mShareWeChatView.setOnClickListener(this);
        mShareWeiboView.setOnClickListener(this);
        mShareQQView.setOnClickListener(this);
        mShareFriendsView.setOnClickListener(this);

        mController = UMServiceFactory.getUMSocialService("www.baidu.com");
        initQZ(this);
        initWX(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mCodeInfo = (InviteCodeInfo)getIntent().getSerializableExtra(KEY_INVITE_CODE_INFO);
        Log.v(TAG, "onPostCreate " + mCodeInfo);
        if (mCodeInfo == null) {
            UserInfoOperator.getInstance().getInviteCode(new UserInfoOperator.OnGetListener<InviteCodeInfo>() {
                @Override
                public void onGet(boolean succeed, InviteCodeInfo codeInfo) {
                    if (succeed) {
                        mCodeInfo = codeInfo;
                        fillCodeInfo(mCodeInfo);
                    }
                }
            });
            return;
        }
        fillCodeInfo(mCodeInfo);
    }

    private void fillCodeInfo (InviteCodeInfo codeInfo) {
        new QrCodeMakerTask().makeQrCode(
                new QrCodeMakerTask.Task(codeInfo.getInvitationurl(), getResources().getDimensionPixelSize(R.dimen.invite_qr_code_size)),
                new QrCodeMakerTask.Callback() {
                    @Override
                    public void onCallback(Bitmap bitmap) {
                        mQrCodeBmp = bitmap;
                        mQrIv.setImageBitmap(bitmap);
                    }
                });
        mContentTv.setText(codeInfo.getInvitationDesc());
        mDownloadCountTv.setText(getString(R.string.invite_down_count, codeInfo.getInvitationCount()));
        mMyCodeTv.setText(getString(R.string.invite_my_code, codeInfo.getInvitationCodeNum()));
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.title_activity_invite);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite_share_wechat:
                WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
                fillShareContent(this, weiXinShareContent);
                mController.directShare(this, SHARE_MEDIA.WEIXIN, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        Toast.makeText(InviteActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                        //sendShareInfoToUMeng("weixin_moments");
                    }
                });
                break;
            case R.id.invite_share_weibo:
                SinaShareContent sinaShareContent = new SinaShareContent();
                fillShareContent(this, sinaShareContent);
                mController.directShare(this, SHARE_MEDIA.SINA, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {

                    }
                });
                break;
            case R.id.invite_share_qq:
                QZoneShareContent qZoneShareContent = new QZoneShareContent();
                fillShareContent(this, qZoneShareContent);
                mController.directShare(this, SHARE_MEDIA.QZONE, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        Toast.makeText(InviteActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                        //sendShareInfoToUMeng("Qzone");
                    }
                });
                break;
            case R.id.invite_share_friends:
                CircleShareContent circleShareContent = new CircleShareContent();
                fillShareContent(this, circleShareContent);
                mController.directShare(this, SHARE_MEDIA.WEIXIN_CIRCLE, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        Toast.makeText(InviteActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
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

    private void fillShareContent(Activity activity, BaseShareContent content) {
        if (mQrCodeBmp == null || mCodeInfo == null) {
            return;
        }
        content.setTitle(getString(R.string.invite_my_code, mCodeInfo.getInvitationCodeNum()));
        content.setShareContent(mCodeInfo.getInvitationDesc());
        content.setTargetUrl(mCodeInfo.getInvitationurl());
        content.setShareImage(new UMImage(activity, mQrCodeBmp));
        mController.setShareMedia(content);
    }
}
