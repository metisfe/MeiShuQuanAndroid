package com.metis.meishuquan.view.shared;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by wudi on 3/15/2015.
 */

public class RecycleBitmapDrawable extends BitmapDrawable
{
    private int mCacheRefCount = 0;
    private int mDisplayRefCount = 0;

    private boolean mHasBeenDisplayed;

    public RecycleBitmapDrawable(Resources res, Bitmap bitmap)
    {
        super(res, bitmap);
    }

    /**
     * Notify the drawable that the displayed state has changed. Internally a
     * count is kept so that the drawable knows when it is no longer being
     * displayed.
     *
     * @param isDisplayed
     *            - Whether the drawable is being displayed or not
     */
    public void setIsDisplayed(boolean isDisplayed)
    {
        synchronized (this)
        {
            if (isDisplayed)
            {
                mDisplayRefCount++;
                mHasBeenDisplayed = true;
            }
            else
            {
                mDisplayRefCount--;
            }
        }

        // Check to see if recycle() can be called
        checkState();
    }

    public boolean isDispalying()
    {
        return mDisplayRefCount > 0;
    }

    /**
     * Notify the drawable that the cache state has changed. Internally a count
     * is kept so that the drawable knows when it is no longer being cached.
     *
     * @param isCached
     *            - Whether the drawable is being cached or not
     */
    public void setIsCached(boolean isCached)
    {
        synchronized (this)
        {
            if (isCached)
            {
                mCacheRefCount++;
            }
            else
            {
                mCacheRefCount--;
            }
        }

        // Check to see if recycle() can be called
        checkState();
    }

    private synchronized void checkState()
    {
        // If the drawable cache and display ref counts = 0, and this drawable
        // has been displayed, then recycle
        if (mCacheRefCount <= 0 && mDisplayRefCount <= 0 && mHasBeenDisplayed && hasValidBitmap())
        {
            getBitmap().recycle();
        }
    }

    private synchronized boolean hasValidBitmap()
    {
        Bitmap bitmap = getBitmap();
        return bitmap != null && !bitmap.isRecycled();
    }
}
