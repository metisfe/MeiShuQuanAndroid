package com.metis.meishuquan;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.model.provider.DataProvider;

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

    @Override
    public void onCreate()
    {
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
        String token = "RSorSOtm5wg2/54VUeDTdIGn6Ui0bBlr+zHn5QT+0f+TueCwMF65klKIr/oHR2+OU+SxbJgwLt/epq+dCRo5w8z59djPCUVA";

        // 连接融云服务器。
        try {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String s) {
                }

                @Override
                public void onError(ErrorCode errorCode) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDisplayMetrics(DisplayMetrics dm)
    {
        displayMetrics = dm;
    }

    public static DisplayMetrics getDisplayMetrics()
    {
        return displayMetrics;
    }
}
