package com.metis.meishuquan;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.metis.meishuquan.model.provider.DataProvider;

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
