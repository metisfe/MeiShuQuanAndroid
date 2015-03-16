package com.metis.meishuquan.framework.cache;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.view.shared.RecycleBitmapDrawable;

/**
 * Created by wudi on 3/15/2015.
 */
public class MemoryCache
{

    private static final int CACHE_SIZE_MAX = 10 * 1024 * 1024;// b

    private static MemoryCache instance;
    private LruCache<String, Object> mCache;

    private MemoryCache()
    {
        init();
    }

    public static synchronized MemoryCache getInstance()
    {
        if (instance == null)
        {
            instance = new MemoryCache();
        }
        return instance;
    }

    private void init()
    {
        final int memClass = ((ActivityManager) MainApplication.UIContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 8;
        if (cacheSize <= 0 || cacheSize > CACHE_SIZE_MAX)
        {
            cacheSize = CACHE_SIZE_MAX;
        }

        this.mCache = new LruCache<String, Object>(cacheSize)
        {

            @Override
            protected boolean willEntryRemove(String key, Object value)
            {
                boolean willRemove = true;
                if (value instanceof RecycleBitmapDrawable)
                {
                    willRemove = !((RecycleBitmapDrawable) value).isDispalying();
                }
                Log.d("MemoryCache", "willEntryRemove key ==" + key + ";willRemove===" + willRemove + ";value ==" + value);
                return willRemove;
            }

            /**
             * Notify the removed entry that is no longer being cached
             */
            @Override
            protected void entryRemoved(boolean evicted, String key, Object oldValue, Object newValue)
            {
                if (RecycleBitmapDrawable.class.isInstance(oldValue))
                {
                    // The removed entry is a recycling drawable, so notify it
                    // that it has been removed from the memory cache
                    Log.d("MemoryCache", "entryRemoved key ==" + key + ";oldValue ==" + oldValue + "; cache size==" + mCache.size());
                    ((RecycleBitmapDrawable) oldValue).setIsCached(false);
                }
            }

            @SuppressLint("NewApi")
            @Override
            protected int sizeOf(String key, Object obj)
            {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                if (obj == null)
                {
                    return 0;
                }

                if (obj instanceof RecycleBitmapDrawable)
                {
                    Bitmap bitmap = ((RecycleBitmapDrawable) obj).getBitmap();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1)
                    {
                        return bitmap.getRowBytes() * bitmap.getHeight();
                    }
                    else
                    {
                        return bitmap.getByteCount();
                    }
                }
                else if (obj instanceof String)
                {
                    return ((String) obj).getBytes().length;
                }
                else if (obj instanceof CacheObject)
                {
                    return ((CacheObject) obj).cacheSize();
                }
                else
                {
                    return 1;
                }
            }
        };
    }

    public void cache(String key, Object obj)
    {
        if (TextUtils.isEmpty(key) || obj == null)
        {
            return;
        }

        synchronized (this.mCache)
        {
            this.mCache.put(key, obj);
        }
    }

    public void cache(String key, RecycleBitmapDrawable drawable)
    {
        if (TextUtils.isEmpty(key) || drawable == null)
        {
            return;
        }

        synchronized (this.mCache)
        {
            drawable.setIsCached(true);
            this.mCache.put(key, drawable);
        }
    }

    public void cache(String key, Bitmap bitmap)
    {
        if (TextUtils.isEmpty(key) || bitmap == null)
        {
            return;
        }

        synchronized (this.mCache)
        {
            RecycleBitmapDrawable drawable = new RecycleBitmapDrawable(MainApplication.UIContext.getResources(), bitmap);
            drawable.setIsCached(true);
            this.mCache.put(key, drawable);
        }
    }

    public void cache(String key, String value)
    {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value))
        {
            return;
        }
        synchronized (this.mCache)
        {
            this.mCache.put(key, value);
        }
    }

    public Object get(String key)
    {
        if (TextUtils.isEmpty(key))
        {
            return null;
        }

        synchronized (this.mCache)
        {
            return this.mCache.get(key);
        }
    }

    public RecycleBitmapDrawable getBitmap(String key)
    {
        if (TextUtils.isEmpty(key))
        {
            return null;
        }

        synchronized (this.mCache)
        {
            Object obj = this.mCache.get(key);

            if (obj != null && obj instanceof RecycleBitmapDrawable)
            {
                return (RecycleBitmapDrawable) obj;
            }
        }
        return null;
    }

    public String getString(String key)
    {
        if (TextUtils.isEmpty(key))
        {
            return null;
        }

        synchronized (this.mCache)
        {
            Object obj = this.mCache.get(key);
            if (obj != null && obj instanceof String)
            {
                return (String) obj;
            }
        }
        return null;
    }

    public boolean isCached(String key)
    {
        if (TextUtils.isEmpty(key))
        {
            return false;
        }

        synchronized (this.mCache)
        {
            return this.mCache.get(key) != null;
        }
    }

    public long size()
    {
        if (this.mCache == null)
        {
            return 0;
        }
        return this.mCache.size();
    }

    public void clear()
    {
        if (this.mCache != null)
        {
            synchronized (this.mCache)
            {
                this.mCache.evictAll();
            }
            Log.d("MemoryCache", "Memory cache cleared");
        }
    }
}
