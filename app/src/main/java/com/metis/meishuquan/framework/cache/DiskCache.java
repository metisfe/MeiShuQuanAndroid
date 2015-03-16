package com.metis.meishuquan.framework.cache;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.util.ImageUtil;
import com.metis.meishuquan.util.SystemUtil;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by wudi on 3/15/2015.
 */

public class DiskCache
{

    protected static final String TAG = "DiskCache";
    protected static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 200; // 200MB
    protected static final String DISK_CACHE_DIR = "lrucache";

    // Compression settings when writing images to disk cache
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 70;
    protected static final int DISK_CACHE_INDEX = 0;
    protected static final boolean DEFAULT_DISK_CACHE_ENABLED = true;

    protected DiskLruCache mDiskLruCache;
    protected final Object mDiskCacheLock = new Object();
    protected boolean mDiskCacheStarting = true;

    private static DiskCache instance = new DiskCache();

    private DiskCache()
    {
        initDiskCache();
    }

    public static DiskCache getInstance()
    {
        return instance;
    }

    public boolean isCached(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return false;
        }

        synchronized (mDiskCacheLock)
        {
            // Add to disk cache
            if (mDiskLruCache != null)
            {
                final String key = SystemUtil.hashKeyForDisk(url);
                try
                {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot == null)
                    {
                        return false;
                    }
                    else
                    {
                        snapshot.getInputStream(DISK_CACHE_INDEX).close();
                        return true;
                    }
                }
                catch (IOException e)
                {
                }
            }
        }

        return false;
    }

    public void cache(String url, Object obj)
    {
        if (TextUtils.isEmpty(url) || obj == null || !(obj instanceof Serializable))
        {
            return;
        }

        synchronized (mDiskCacheLock)
        {
            // Add to disk cache
            if (mDiskLruCache != null)
            {
                OutputStream out = null;
                ObjectOutputStream objOut = null;
                final String key = SystemUtil.hashKeyForDisk(url);
                try
                {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot == null)
                    {
                        final DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                        if (editor != null)
                        {
                            out = editor.newOutputStream(DISK_CACHE_INDEX);
                            if (out != null)
                            {
                                objOut = new ObjectOutputStream(out);
                                objOut.writeObject(obj);
                                editor.commit();
                                objOut.close();
                            }
                        }
                    }
                    else
                    {
                        snapshot.getInputStream(DISK_CACHE_INDEX).close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "cache object - " + e);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "cache object- " + e);
                }
            }
        }
    }

    public void cache(String url, Bitmap bitmap)
    {
        if (TextUtils.isEmpty(url) || bitmap == null)
        {
            return;
        }

        synchronized (mDiskCacheLock)
        {
            // Add to disk cache
            if (mDiskLruCache != null)
            {
                final String key = SystemUtil.hashKeyForDisk(url);
                OutputStream out = null;
                try
                {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot == null)
                    {
                        final DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                        if (editor != null)
                        {
                            out = editor.newOutputStream(DISK_CACHE_INDEX);
                            if (bitmap.getConfig() != null && bitmap.getConfig().compareTo(Bitmap.Config.ARGB_8888) >= 0)
                            {
                                bitmap.compress(Bitmap.CompressFormat.PNG, DEFAULT_COMPRESS_QUALITY, out);
                            }
                            else
                            {
                                bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, out);
                            }
                            editor.commit();
                            out.close();
                        }
                    }
                    else
                    {
                        snapshot.getInputStream(DISK_CACHE_INDEX).close();
                    }
                }
                catch (final IOException e)
                {
                    Log.e(TAG, "addBitmapToCache - " + e);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "addBitmapToCache - " + e);
                }
                finally
                {
                    try
                    {
                        if (out != null)
                        {
                            out.close();
                        }
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
        }
    }

    public void cache(String url, String data)
    {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(data))
        {
            return;
        }

        synchronized (mDiskCacheLock)
        {
            // Add to disk cache
            if (mDiskLruCache != null)
            {
                final String key = SystemUtil.hashKeyForDisk(url);
                try
                {
                    final DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null)
                    {
                        editor.set(DISK_CACHE_INDEX, data);
                        editor.commit();
                        mDiskLruCache.flush();
                    }
                }
                catch (final IOException e)
                {
                    Log.e(TAG, "addStringToCache - " + e);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "addStringToCache - " + e);
                }
            }
        }
    }

    public void cache(String url, InputStream is)
    {
        if (TextUtils.isEmpty(url) || is == null)
        {
            return;
        }

        synchronized (mDiskCacheLock)
        {
            // Add to disk cache
            if (mDiskLruCache != null)
            {
                final String key = SystemUtil.hashKeyForDisk(url);
                OutputStream out = null;
                try
                {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot == null)
                    {
                        final DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                        if (editor != null)
                        {
                            out = editor.newOutputStream(DISK_CACHE_INDEX);
                            byte[] buffer = new byte[4 * 1024];
                            int readSize = 0;
                            while ((readSize = is.read(buffer)) > 0)
                            {
                                out.write(buffer, 0, readSize);
                            }

                            editor.commit();
                            out.close();
                        }
                    }
                    else
                    {
                        snapshot.getInputStream(DISK_CACHE_INDEX).close();
                    }
                }
                catch (final IOException e)
                {
                    Log.e(TAG, "addDataToCache - " + e);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "addDataToCache - " + e);
                }
                finally
                {
                    try
                    {
                        if (out != null)
                        {
                            out.close();
                        }
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
        }
    }

    public Object getObject(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        final String key = SystemUtil.hashKeyForDisk(url);
        Object obj = null;

        synchronized (mDiskCacheLock)
        {
            while (mDiskCacheStarting)
            {
                try
                {
                    mDiskCacheLock.wait();
                }
                catch (InterruptedException e)
                {
                }
            }

            if (mDiskLruCache != null)
            {
                InputStream inputStream = null;
                ObjectInputStream objIS = null;
                try
                {
                    final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot != null)
                    {
                        inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
                        if (inputStream != null)
                        {
                            objIS = new ObjectInputStream(inputStream);
                            try
                            {
                                obj = objIS.readObject();
                            }
                            catch (ClassNotFoundException e)
                            {
                                Log.e(TAG, "getObject - " + e);
                            }
                            finally
                            {
                                objIS.close();
                            }
                        }
                    }
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "getObject - " + e);
                }
                finally
                {
                    try
                    {
                        if (inputStream != null)
                        {
                            inputStream.close();
                        }
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
            return obj;
        }
    }

    public String getString(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        final String key = SystemUtil.hashKeyForDisk(url);
        String result = null;
        synchronized (mDiskCacheLock)
        {
            while (mDiskCacheStarting)
            {
                try
                {
                    mDiskCacheLock.wait();
                }
                catch (InterruptedException e)
                {
                }
            }

            if (mDiskLruCache != null)
            {
                try
                {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot != null)
                    {
                        result = snapshot.getString(DISK_CACHE_INDEX);
                    }

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        }
        return result;
    }

    public File getFile(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        final String key = SystemUtil.hashKeyForDisk(url);

        return SystemUtil.getDiskCacheDir(MainApplication.UIContext, key);
    }

    public InputStream getData(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        final String key = SystemUtil.hashKeyForDisk(url);

        synchronized (mDiskCacheLock)
        {
            while (mDiskCacheStarting)
            {
                try
                {
                    mDiskCacheLock.wait();
                }
                catch (InterruptedException e)
                {
                }
            }

            if (mDiskLruCache != null)
            {
                InputStream inputStream = null;
                try
                {
                    final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot != null)
                    {
                        return snapshot.getInputStream(DISK_CACHE_INDEX);
                    }
                }
                catch (final IOException e)
                {
                    Log.e(TAG, "getDataFromCache - " + e);
                }
                finally
                {
                    try
                    {
                        if (inputStream != null)
                        {
                            inputStream.close();
                        }
                    }
                    catch (IOException e)
                    {
                    }
                }
            }

            return null;
        }
    }

    public Bitmap getBitmap(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        final String key = SystemUtil.hashKeyForDisk(url);
        Bitmap bitmap = null;

        synchronized (mDiskCacheLock)
        {
            while (mDiskCacheStarting)
            {
                try
                {
                    mDiskCacheLock.wait();
                }
                catch (InterruptedException e)
                {
                }
            }

            if (mDiskLruCache != null)
            {
                InputStream inputStream = null;
                try
                {
                    final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot != null)
                    {
                        inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
                        if (inputStream != null)
                        {
                            FileDescriptor fd = ((FileInputStream) inputStream).getFD();

                            // Decode bitmap, but we don't want to sample so
                            // give
                            // MAX_VALUE as the target dimensions
                            Log.d("DiskCache", "decode image from disk url ==" + url);
                            bitmap = ImageUtil.decodeSampledBitmapFromDescriptor(fd, ImageUtil.getImageMaxWidth(), ImageUtil.getImageMaxHeight());
                        }
                    }
                }
                catch (final IOException e)
                {
                    Log.e(TAG, "getBitmapFromDiskCache - " + e);
                }
                finally
                {
                    try
                    {
                        if (inputStream != null)
                        {
                            inputStream.close();
                        }
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
            return bitmap;
        }
    }

    public long size()
    {
        if (mDiskLruCache == null)
            return 0;
        return mDiskLruCache.size();
    }

    public long maxSize()
    {
        if (mDiskLruCache == null)
            return 0;
        return mDiskLruCache.maxSize();
    }

    /**
     * Initializes the disk cache. Note that this includes disk access so this
     * should not be executed on the main/UI thread. By default an ImageCache
     * does not initialize the disk cache when it is created, instead you should
     * call initDiskCache() to initialize it on a background thread.
     */
    private void initDiskCache()
    {
        // Set up disk cache
        synchronized (mDiskCacheLock)
        {
            if (mDiskLruCache == null || mDiskLruCache.isClosed())
            {
                File diskCacheDir = SystemUtil.getDiskCacheDir(MainApplication.UIContext, DISK_CACHE_DIR);
                if (DEFAULT_DISK_CACHE_ENABLED && diskCacheDir != null)
                {
                    if (!diskCacheDir.exists())
                    {
                        diskCacheDir.mkdirs();
                    }

                    long usableDiskSize = SystemUtil.getUsableSpace(diskCacheDir) / 2;
                    long diskCacheSize = usableDiskSize > DEFAULT_DISK_CACHE_SIZE ? DEFAULT_DISK_CACHE_SIZE : usableDiskSize;
                    if (usableDiskSize > 0)
                    {
                        try
                        {
                            mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, diskCacheSize);
                            Log.d(TAG, "Disk cache initialized");
                        }
                        catch (final IOException e)
                        {
                            Log.e(TAG, "initDiskCache - " + e);
                        }
                    }
                }
            }
            mDiskCacheStarting = false;
            mDiskCacheLock.notifyAll();
        }
    }

    public void clear()
    {
        synchronized (mDiskCacheLock)
        {
            mDiskCacheStarting = true;
            if (mDiskLruCache != null && !mDiskLruCache.isClosed())
            {
                try
                {
                    mDiskLruCache.delete();
                    Log.d(TAG, "Disk cache cleared");
                }
                catch (IOException e)
                {
                    Log.e(TAG, "clearCache - " + e);
                }
                mDiskLruCache = null;
                initDiskCache();
            }
        }
    }

    public void flush()
    {
        synchronized (mDiskCacheLock)
        {
            if (mDiskLruCache != null)
            {
                try
                {
                    mDiskLruCache.flush();
                    Log.d(TAG, "Disk cache flushed");
                }
                catch (IOException e)
                {
                    Log.e(TAG, "flush - " + e);
                }
            }
        }
    }

    public void close()
    {
        synchronized (mDiskCacheLock)
        {
            if (mDiskLruCache != null)
            {
                try
                {
                    if (!mDiskLruCache.isClosed())
                    {
                        mDiskLruCache.close();
                        mDiskLruCache = null;
                        Log.d(TAG, "Disk cache closed");
                    }
                }
                catch (IOException e)
                {
                    Log.e(TAG, "close - " + e);
                }
            }
        }
    }
}
