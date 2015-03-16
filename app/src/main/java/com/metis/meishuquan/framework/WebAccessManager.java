package com.metis.meishuquan.framework;

import android.graphics.Bitmap;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.framework.cache.CacheManager;
import com.metis.meishuquan.framework.cache.CacheSize;
import com.metis.meishuquan.framework.runner.ModelWebRunner;
import com.metis.meishuquan.framework.runner.RunnerManager;
import com.metis.meishuquan.framework.runner.WebRunner;
import com.metis.meishuquan.framework.util.ThreadPool;
import com.metis.meishuquan.util.SystemUtil;
import com.metis.meishuquan.view.shared.RecycleBitmapDrawable;

import java.io.File;

/**
 * Created by wudi on 3/15/2015.
 */

public class WebAccessManager
{
    private static WebAccessManager instance;

    private WebAccessManager()
    {
    }

    public static WebAccessManager getInstance()
    {
        if (instance == null)
        {
            instance = new WebAccessManager();
        }

        return instance;
    }

    public boolean isCachedInMemory(String url)
    {
        return CacheManager.getInstance().isCachedInMemory(url);
    }

    public RecycleBitmapDrawable getBitmapFromMemory(String url)
    {
        return CacheManager.getInstance().getBitmapFromMemory(url);
    }

    public String getStringFromMemory(String url)
    {
        return CacheManager.getInstance().getStringFromMemory(url);
    }

    public Object getObjectFromMemory(String url)
    {
        return CacheManager.getInstance().getObjectFromMemory(url);
    }

    public void cache(String url, Bitmap bitmap)
    {
        CacheManager.getInstance().cache(url, bitmap);
    }

    public void cacheLargeImage(String url, Bitmap bitmap)
    {
        CacheManager.getInstance().cacheLargeImage(url, bitmap);
    }

    public void cacheBitmapToDisk(String url, Bitmap bitmap)
    {
        CacheManager.getInstance().cacheBitmapToDisk(url, bitmap);
    }

    public void cacheMemoryImage(String url, RecycleBitmapDrawable bitmap)
    {
        CacheManager.getInstance().cacheMemoryImage(url, bitmap);
    }

    public void cache(String url, String value)
    {
        CacheManager.getInstance().cache(url, value);
    }

    public void cache(String url, Object value)
    {
        CacheManager.getInstance().cache(url, value);
    }

    public Object getObject(String url)
    {
        return CacheManager.getInstance().getObject(url);
    }

    public RecycleBitmapDrawable getBitmap(String url)
    {
        return CacheManager.getInstance().getBitmap(url);
    }

    public Bitmap getBitmapFromDisk(String url)
    {
        return CacheManager.getInstance().getBitmapFromDisk(url);
    }

    public String getString(String url)
    {
        return CacheManager.getInstance().getString(url);
    }

    public File getFile(String url)
    {
        return CacheManager.getInstance().getFile(url);
    }

    public CacheSize getCacheSize()
    {
        return CacheManager.getInstance().size();
    }

    public void clearCache()
    {
        CacheManager.getInstance().clear();
    }

    public void start(WebRunner<?> runner)
    {
        RunnerManager.getInstance().run(runner);
    }

    public void start(ModelWebRunner<?> runner)
    {
        ThreadPool.APIThread.run(runner);
    }

    public void removeBlockedRunner(WebRunner<?> runner)
    {
        RunnerManager.getInstance().removeBlockedRunner(runner);
    }

    public boolean isWiFiConnected()
    {
        return SystemUtil.isWiFiConnected(MainApplication.UIContext);
    }

    public boolean isNetworkAvailable()
    {
        return SystemUtil.isNetworkAvailable(MainApplication.UIContext);
    }

    public boolean isConnectedFast()
    {
        return SystemUtil.isConnectedFast(MainApplication.UIContext);
    }

}
