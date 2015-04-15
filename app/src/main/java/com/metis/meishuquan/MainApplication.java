package com.metis.meishuquan;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.enums.LoginStateEnum;
import com.metis.meishuquan.model.login.LoginUserData;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.model.provider.DataProvider;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.SharedPreferencesUtil;

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
        //String token = "vHlcG4hORBuPENRljGB6MoGn6Ui0bBlr+zHn5QT+0f+TueCwMF65klGMwsE+P2SPd8eazBQpOPpagJ1/lOVNMg==";
    }

    /**
     * 连接融云服务器
     */
    public static void rongConnect(String token, RongIMClient.ConnectCallback connectCallback) {
        try {
            rongIM = RongIM.connect(token, connectCallback);
            rongClient = rongIM.getRongIMClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO: this is fake data
        ChatManager.friendIdList.add("diwulechao");
        ChatManager.friendIdList.add("diwulechao1");
        ChatManager.friendIdList.add("diwulechao2");
        ChatManager.friendIdList.add("diwulechao4");
        ChatManager.friendIdList.add("diwulechao3");

        ChatManager.contactCache.put("diwulechao", new RongIMClient.UserInfo("diwulechao", "张三", ""));
        ChatManager.contactCache.put("diwulechao1", new RongIMClient.UserInfo("diwulechao1", "李四", ""));
        ChatManager.contactCache.put("diwulechao2", new RongIMClient.UserInfo("diwulechao2", "王二", ""));
        ChatManager.contactCache.put("diwulechao4", new RongIMClient.UserInfo("diwulechao4", "321（）", ""));
        ChatManager.contactCache.put("diwulechao3", new RongIMClient.UserInfo("diwulechao3", "麻子", ""));
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
        LoginUserData user = new Gson().fromJson(json, new TypeToken<LoginUserData>() {
        }.getType());
        if (user.getData() == null) {
            return new User();
        }
        return user.getData();
    }
}
