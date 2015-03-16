package com.metis.meishuquan.framework.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.net.URL;

/**
 * Created by wudi on 3/15/2015.
 */
public class TextureRender
{
    private static final int MAX_DISK_CACHE_SIZE = 100 * 1024 * 1024; // 100M
    private static TextureRender instance;
    private boolean isNeedLoadImageFromServer = true;

    private TextureRender()
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MainApplication.UIContext)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .discCacheSize(MAX_DISK_CACHE_SIZE)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().handleSlowNetwork(true);
    }

    public static TextureRender getInstance()
    {
        if (instance == null)
        {
            instance = new TextureRender();
        }
        return instance;
    }

    public void setNeedLoadImageFromServer(boolean isNeedLoadImageFromServer)
    {
        this.isNeedLoadImageFromServer = isNeedLoadImageFromServer;
    }

    public boolean isNeedLoadImageFromServer()
    {
        return this.isNeedLoadImageFromServer;
    }

    /**
     * download bitmap from cache or network with default image
     *
     * @param url
     * @param imageView
     */
    //public void setBitmap(String url, ImageView imageView)
    public void setBitmap(URL url, ImageView imageView)
    {
        setBitmap(url, imageView, null, R.drawable.view_shared_default_loading);
    }

    /**
     * download bitmap from cache or network
     *
     * @param url
     * @param imageView
     * @param replaceResourceId
     */
    //public void setBitmap(String url, ImageView imageView, int replaceResourceId)
    public void setBitmap(URL url, ImageView imageView, int replaceResourceId)
    {
        setBitmap(url, imageView, null, replaceResourceId);
    }

    /**
     * download bitmap from cache or network
     *
     * @param url
     * @param imageView
     * @param replaceResourceId
     * @param promotionCardImageListener
     */
    public void setBitmap(URL url, ImageView imageView, int replaceResourceId, ImageLoadingListener promotionCardImageListener)
    {
        setBitmap(url, imageView, null, replaceResourceId, 0, promotionCardImageListener, null);
    }

    /**
     * download bitmap from cache or network
     *
     * @param url
     * @param imageView
     * @param replaceResourceId
     */
    public void setBitmap(URL url, ImageView imageView, String defultUrl, int replaceResourceId)
    {
        setBitmap(url, imageView, defultUrl, replaceResourceId, true, 0, 0);
    }

    public void setBitmap(URL url, ImageView imageView, int replaceResourceId, int fixedWidth, int fixedHeight)
    {
        setBitmap(url, imageView, null, replaceResourceId, true, fixedWidth, fixedHeight);
    }

    public void setBitmap(URL url, ImageView imageView, String defultUrl, int replaceResourceId, int failResourceId, ImageLoadingListener listener, ImageLoadingProgressListener progressListener)
    {
        setBitmap(url, imageView, defultUrl, replaceResourceId, failResourceId, true, 0, 0, listener, progressListener, false);
    }

    public void setBitmap(URL url, ImageView imageView, String defultUrl, int replaceResourceId, boolean isNeedCache, int fixedWidth, int fixedHeight)
    {
        setBitmap(url, imageView, defultUrl, replaceResourceId, 0, isNeedCache, fixedWidth, fixedHeight, null, null, true);
    }

    public void setBitmap(URL url, ImageView imageView, String defultUrl, int replaceResourceId, int failResourceId, boolean isNeedCache, int fixedWidth, int fixedHeight, ImageLoadingListener listener, ImageLoadingProgressListener progressListener, boolean isCacheMemory)
    {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        if (failResourceId > 0)
        {
            builder.showImageOnFail(failResourceId);
        }
        else
        {
            if (replaceResourceId > 0)
            {
                builder.showImageOnFail(replaceResourceId);
            }
        }

        if (replaceResourceId > 0)
        {
            builder.showImageOnLoading(replaceResourceId).showImageForEmptyUri(replaceResourceId);
            if (failResourceId <= 0)
            {
                builder.showImageOnFail(replaceResourceId);
            }
        }

        builder.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(isCacheMemory);
        DisplayImageOptions options = builder.build();
        ImageLoader.getInstance().displayImage(url == null ? "" : url.toExternalForm(), imageView, options, listener, progressListener);
    }

    public void clearMemoryCache()
    {
        ImageLoader.getInstance().clearMemoryCache();
    }

    public void clearDiskCache()
    {
        ImageLoader.getInstance().clearDiscCache();
    }

    public int getCurrentMemoryCacheSize()
    {
        return ImageLoader.getInstance().getCurrentMemoryCacheSize();
    }

    public int getCurrentDiskCacheSize()
    {
        return ImageLoader.getInstance().getCurrentDiscCacheSize();
    }
}