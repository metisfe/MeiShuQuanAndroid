package com.metis.meishuquan;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.model.provider.DataProvider;
import com.metis.meishuquan.util.ChatManager;

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

    @Override
    public void onCreate() {
        super.onCreate();

        Resources = this.getResources();
        PackageName = this.getPackageName();
        AssetManager = this.getAssets();
        UIThread = Thread.currentThread();
        UIContext = this.getApplicationContext();
        Handler = new Handler();

        DataProvider.setDefaultUIThreadHandler(Handler);
        ApiDataProvider.initProvider();
        RongIM.init(this);
        String token = "qHluhCF1dAj8xOPUjg0JZCvfK7JNZxM37vO0IUSktXEbZvgMV3hl8xVzOvjzPOe636oIalCHGnIJYO9VPv6SGQ==";

        // 连接融云服务器。
        try {
            rongIM = RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String s) {
                    if (rongIM != null) {
                        rongIM.setReceiveMessageListener(new RongIM.OnReceiveMessageListener() {
                            @Override
                            public void onReceived(RongIMClient.Message message, int i) {
                                ChatManager.onReceive(message);
                            }
                        });
                    }
                }

                @Override
                public void onError(ErrorCode errorCode) {
                }
            });
            rongClient = rongIM.getRongIMClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDisplayMetrics(DisplayMetrics dm) {
        displayMetrics = dm;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }
}
