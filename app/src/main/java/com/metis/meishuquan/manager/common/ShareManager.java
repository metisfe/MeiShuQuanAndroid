package com.metis.meishuquan.manager.common;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * Created by WJ on 2015/6/11.
 */
public class ShareManager {

    private static final String TAG = ShareManager.class.getSimpleName();

    private static final String DEFAULT_URL = "https://www.baidu.com/";

    public static ShareManager sManager = null;
    public static synchronized ShareManager getInstance (Activity activity) {
        if (sManager == null) {
            sManager = new ShareManager(activity);
        }
        return sManager;
    }

    private boolean isInited = false;
    private UMSocialService mService = null;

    private ShareManager (Activity activity) {
        isInited = init(activity);
    }

    private boolean init (Activity activity) {
        mService = UMServiceFactory.getUMSocialService(DEFAULT_URL);
        initQZ(activity);
        initQQ(activity);
        initWX(activity);
        return true;
    }

    private void initQZ(Activity context) {
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, "1104485283", "k9f8JhWppP5r1N5t");
        qZoneSsoHandler.addToSocialSDK();
    }

    private void initQQ (Activity activity) {
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, "1104485283",
                "k9f8JhWppP5r1N5t");
        qqSsoHandler.addToSocialSDK();
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

    public UMSocialService getSocialService () {
        return mService;
    }

}
