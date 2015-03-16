package com.metis.meishuquan.framework.cache;

import android.graphics.Bitmap;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.util.SystemUtil;
import com.metis.meishuquan.view.shared.RecycleBitmapDrawable;

import java.io.File;

/**
 * Created by wudi on 3/15/2015.
 */
public class CacheManager
{

    private static CacheManager instance;

    private CacheManager()
    {

    }

    public static synchronized CacheManager getInstance()
    {
        if (instance == null)
        {
            instance = new CacheManager();
        }

        return instance;
    }

    public boolean isCachedInMemory(String url)
    {
        return MemoryCache.getInstance().isCached(url);
    }

    public RecycleBitmapDrawable getBitmapFromMemory(String url)
    {
        return MemoryCache.getInstance().getBitmap(url);
    }

    public String getStringFromMemory(String url)
    {
        return MemoryCache.getInstance().getString(url);
    }

    public Object getObjectFromMemory(String url)
    {
        return MemoryCache.getInstance().get(url);
    }

    public void cache(String url, Bitmap bitmap)
    {
        MemoryCache.getInstance().cache(url, bitmap);
        DiskCache.getInstance().cache(url, bitmap);
    }

    public void cacheLargeImage(String url, Bitmap bitmap)
    {
        DiskCache.getInstance().cache(url, bitmap);
    }

    public void cacheBitmapToDisk(String url, Bitmap bitmap)
    {
        DiskCache.getInstance().cache(url, bitmap);
    }

    public void cacheMemoryImage(String url, RecycleBitmapDrawable bitmap)
    {
        MemoryCache.getInstance().cache(url, bitmap);
    }

    public void cache(String url, String value)
    {
        MemoryCache.getInstance().cache(url, value);
        DiskCache.getInstance().cache(url, value);
    }

    public void cache(String url, Object value)
    {
        MemoryCache.getInstance().cache(url, value);
        DiskCache.getInstance().cache(url, value);
    }

    public Object getObject(String url)
    {
        Object obj = MemoryCache.getInstance().get(url);

        if (obj != null)
        {
            return obj;
        }
        return DiskCache.getInstance().getObject(url);
    }

    public RecycleBitmapDrawable getBitmap(String url)
    {
        RecycleBitmapDrawable drawable = MemoryCache.getInstance().getBitmap(url);

        if (drawable != null)
        {
            return drawable;
        }
        Bitmap bm = DiskCache.getInstance().getBitmap(url);
        if (bm != null)
        {
            return new RecycleBitmapDrawable(MainApplication.UIContext.getResources(), bm);
        }
        return null;
    }

    public Bitmap getBitmapFromDisk(String url)
    {
        return DiskCache.getInstance().getBitmap(url);
    }

    public String getString(String url)
    {
        String str = MemoryCache.getInstance().getString(url);

        if (str != null)
        {
            return str;
        }
        return DiskCache.getInstance().getString(url);
    }

    public File getFile(String url)
    {
        return DiskCache.getInstance().getFile(url);
    }

    public CacheSize size()
    {
        CacheSize size = new CacheSize();
        size.memorySize = MemoryCache.getInstance().size();
        size.diskSize = SystemUtil.getDiskCacheSize(MainApplication.UIContext);
        return size;
    }

    public void clear()
    {
        MemoryCache.getInstance().clear();
        DiskCache.getInstance().clear();
    }
}
