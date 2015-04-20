package com.metis.meishuquan.fragment.commons;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.meishuquan.R;
import com.uk.co.senab.photoview.PhotoView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by WJ on 2015/4/20.
 */
public class ImagePreviewSingleFragment extends Fragment {

    private static final String TAG = ImagePreviewSingleFragment.class.getSimpleName();

    private String mRootPath = null;
    private String mImagePath = null;

    private PhotoView mPhotoView = null;
    private Bitmap mBmp = null;
    private HttpHandler mHttpHandler = null;
    private String mUrl = null;
    private boolean isLoaded = false;
    private WeakReference<Bitmap> mBitmapWeakReference = null;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File externalCacheDir = activity.getExternalCacheDir();
            if (externalCacheDir.exists()) {
                mRootPath = new File(externalCacheDir, "temp").getAbsolutePath();
                return;
            }

            mImagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }

        /*File cacheDir = activity.getCacheDir();
        if (cacheDir.exists()) {
            mRootPath = new File(cacheDir, "temp").getAbsolutePath();
            return;
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_preview_single, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhotoView = (PhotoView)view.findViewById(R.id.image_preview_single_iv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadImageUrl(mUrl);
    }

    public String getName (String url) {
        int index = url.lastIndexOf(File.separator);
        if (index > 0) {
            return url.substring(index + 1, url.length());
        }
        return url;
    }

    public boolean isLoaded () {
        return isLoaded;
    }

    public void loadImageUrl (String url) {
        isLoaded = true;
        mUrl = url;
        if (mUrl == null || mPhotoView == null) {
            return;
        }

        Log.v(TAG, "loadImageUrl before request " + url);
        File cacheFile = new File(mImagePath + File.separator + getName(url));
        if (cacheFile.exists()) {
            mBmp = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
            mPhotoView.setImageDrawable(new BitmapDrawable(mBmp));
            return;
        }
        HttpUtils httpUtils = new HttpUtils();
        mHttpHandler = httpUtils.download(url, mRootPath + File.separator + System.currentTimeMillis() + ".jpg", true, true, new RequestCallBack<File>() {

            @Override
            public void onSuccess(ResponseInfo<File> fileResponseInfo) {
                if (fileResponseInfo != null) {
                    Log.v(TAG, "loadImageUrl fileResponseInfo=" + fileResponseInfo.result.getAbsolutePath());
                    File file = fileResponseInfo.result;
                    file.deleteOnExit();
                    mBmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                    mPhotoView.setImageDrawable(new BitmapDrawable(mBmp));
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public void releaseImage () {
        if (mPhotoView != null) {
            mPhotoView.setImageDrawable(null);
        }

        if (mBmp != null) {
            if (!mBmp.isRecycled()) {
                mBmp.recycle();
                mBmp = null;
            }
        }
        if (mHttpHandler != null) {
            mHttpHandler.cancel();
        }
        isLoaded = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseImage();
    }
}
