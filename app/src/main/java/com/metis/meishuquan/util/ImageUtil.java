package com.metis.meishuquan.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import java.io.FileDescriptor;

/**
 * Created by wudi on 3/15/2015.
 */

public class ImageUtil
{
    public static final int MAX_IMAGE_WIDTH_PHONE = 600;
    public static final int MAX_IMAGE_HEIGHT_PHONE = 720;
    public static final int MAX_IMAGE_SIZE = 10 * 1024;// 10k
    public static final float LONG_IMAGE_HEIGHT_WIDTH_ASPECT = 2;
    public static final float SHORT_IMAGE_HEIGHT_WIDTH_ASPECT = 0.5f;

    public static int getImageMaxWidth()
    {
        int diplayWidth = SystemUtil.getDisplayWidth();
        // if (diplayWidth > 0 && diplayWidth < MAX_IMAGE_WIDTH_PHONE) {
        return diplayWidth;
        // }
        // return MAX_IMAGE_WIDTH_PHONE ;
    }

    public static int getImageMaxHeight()
    {
        int diplayHeight = SystemUtil.getDisplayHeight();
        // if (diplayHeight > 0 && diplayHeight < MAX_IMAGE_HEIGHT_PHONE) {
        return diplayHeight;
        // }
        // return MAX_IMAGE_HEIGHT_PHONE ;
    }

    public static boolean isLargeImage(Bitmap bm)
    {
        if (bm == null)
        {
            return false;
        }

        if (bm.getWidth() >= getImageMaxWidth() || bm.getHeight() >= getImageMaxHeight())
        {
            return true;
        }

        return false;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options)
    {
        return calculateInSampleSize(options, getImageMaxWidth(), getImageMaxHeight());
    }

    /**
     * Get the size in bytes of a bitmap in a BitmapDrawable.
     *
     * @param value
     * @return size in bytes
     */
    @TargetApi(12)
    public static int getBitmapSize(BitmapDrawable value)
    {
        Bitmap bitmap = value.getBitmap();

        if (SystemUtil.hasHoneycombMR1())
        {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * @param candidate
     *            - Bitmap to check
     * @param targetOptions
     *            - Options that have the out* value populated
     * @return true if <code>candidate</code> can be used for inBitmap re-use
     *         with <code>targetOptions</code>
     */
    public static boolean canUseForInBitmap(Bitmap candidate, BitmapFactory.Options targetOptions)
    {
        int width = targetOptions.outWidth / targetOptions.inSampleSize;
        int height = targetOptions.outHeight / targetOptions.inSampleSize;

        return candidate.getWidth() == width && candidate.getHeight() == height;
    }

    /**
     * Decode and sample down a bitmap from a file input stream to the requested
     * width and height.
     *
     * @param fileDescriptor
     *            The file descriptor to read from
     * @param reqWidth
     *            The requested width of the resulting bitmap
     * @param reqHeight
     *            The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     *         ratio and dimensions that are equal to or greater than the
     *         requested width and height
     */
    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        // If we're running on Honeycomb or newer, try to use inBitmap

        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
     * object when decoding bitmaps using the decode* methods from
     * {@link BitmapFactory}. This implementation calculates the closest
     * inSampleSize that will result in the final decoded bitmap having a width
     * and height equal to or larger than the requested width and height. This
     * implementation does not ensure a power of 2 is returned for inSampleSize
     * which can be faster when decoding but results in a larger bitmap which
     * isn't as useful for caching purposes.
     *
     * @param options
     *            An options object with out* params already populated (run
     *            through a decode* method with inJustDecodeBounds==true
     * @param reqWidth
     *            The requested width of the resulting bitmap
     * @param reqHeight
     *            The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = (int) Math.ceil((float) height / (float) reqHeight);
            final int widthRatio = (int) Math.ceil((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee a final image
            // with both dimensions larger than or equal to the requested height
            // and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
            {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeFixedSizeImage(byte[] data, int dstWidth, int dstHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // save width and height
        int inWidth = options.outWidth;
        int inHeight = options.outHeight;

        options.inJustDecodeBounds = false;
        // calc rought re-size (this is no exact resize)
        options.inSampleSize = Math.max(inWidth / dstWidth, inHeight / dstHeight);
        // decode full image
        Bitmap roughBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // calc exact destination size
        Matrix m = new Matrix();
        RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
        RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
        m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
        float[] values = new float[9];
        m.getValues(values);

        // resize bitmap
        return Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
    }

}
