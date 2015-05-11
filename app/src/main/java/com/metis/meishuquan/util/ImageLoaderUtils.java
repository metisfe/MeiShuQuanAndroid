package com.metis.meishuquan.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.metis.meishuquan.R;
import com.metis.meishuquan.ui.display.SquareRoundDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by WJ on 2015/4/13.
 */
public class ImageLoaderUtils {
    public static ImageLoader getImageLoader(Context context) {
        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            loader.init(ImageLoaderConfiguration.createDefault(context));
        }
        return loader;
    }

    public static DisplayImageOptions getRoundDisplayOptions(int size) {
        return getRoundDisplayOptions(size, R.drawable.default_user_dynamic);
    }

    public static DisplayImageOptions getRoundDisplayOptions(int size, int resousceId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new SquareRoundDisplayer(size))
                .showImageForEmptyUri(resousceId)
                .showImageOnFail(resousceId)
                .showImageOnLoading(resousceId)
                .considerExifParams(true)
                .build();
        return options;
    }

    public static DisplayImageOptions getNormalDisplayOptions(int resousceId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .showImageForEmptyUri(resousceId)
                .showImageOnFail(resousceId)
                .showImageOnLoading(resousceId)
                .considerExifParams(true)
                .build();
        return options;
    }

    public static String getFilePathFromUri(Activity activity, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        activity.startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static byte[] BitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static String saveBitmapAtMediaDir(String name, Bitmap bmp) {
        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File locationFile = new File(pictures, name);
        try {
            FileOutputStream fos = new FileOutputStream(locationFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return locationFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
