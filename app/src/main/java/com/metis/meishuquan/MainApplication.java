package com.metis.meishuquan;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.MyFriendList;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.enums.LoginStateEnum;
import com.metis.meishuquan.model.login.LoginUserData;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.model.provider.DataProvider;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 3/15/2015.
 */
public class MainApplication extends Application {
    private static DisplayMetrics displayMetrics;
    public static android.content.res.Resources Resources;
    public static String PackageName;
    public static MainActivity MainActivity;
    public static android.content.res.AssetManager AssetManager;

    public static Thread UIThread;
    public static Handler Handler;
    public static Context UIContext;
    public static RongIMClient rongClient;
    public static RongIM rongIM;
    public static User userInfo;//登录或注册成功后会更新此对象，默认对象中有登录状态及默认的UserId

    @Override
    public void onCreate() {
        super.onCreate();

        Resources = this.getResources();
        PackageName = this.getPackageName();
        AssetManager = this.getAssets();
        UIThread = Thread.currentThread();
        UIContext = this.getApplicationContext();
        Handler = new Handler();
        userInfo = getUserInfoFromSharedPreferences();

        DataProvider.setDefaultUIThreadHandler(Handler);
        ApiDataProvider.initProvider();
        RongIM.init(this);
        ChatManager.userRongId = userInfo.getRongCloudId();
        rongConnect(userInfo.getToken());
    }

    /**
     * 连接融云服务器
     */
    public static void rongConnect(String token) {
        if (TextUtils.isEmpty(token) || token.length() < 50) return;
        try {
            rongIM = RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String s) {
                    if (MainApplication.rongIM != null) {
                        MainApplication.rongIM.setReceiveMessageListener(new RongIM.OnReceiveMessageListener() {
                            @Override
                            public void onReceived(RongIMClient.Message message, int i) {
                                ChatManager.onReceive(message);
                            }
                        });
                    }
                }

                @Override
                public void onError(ErrorCode errorCode) {
                    Log.e("rongConnect", errorCode.toString());
                    MainApplication.rongClient = null;
                    MainApplication.rongIM = null;
                    ChatManager.userRongId = "";
                }
            });
            rongClient = rongIM.getRongIMClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ChatManager.refreshFriendData();
    }

    public static void setDisplayMetrics(DisplayMetrics dm) {
        displayMetrics = dm;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    private User getUserInfoFromSharedPreferences() {
        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(UIContext);
        String json = spu.getStringByKey(SharedPreferencesUtil.USER_LOGIN_INFO);
        if (json.isEmpty()) {
            return new User();
        }
        LoginUserData user = new Gson().fromJson(json, new TypeToken<LoginUserData>() {
        }.getType());
        return user.getData();
    }
}
