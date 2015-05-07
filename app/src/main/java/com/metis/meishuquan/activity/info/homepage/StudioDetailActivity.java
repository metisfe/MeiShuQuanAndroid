package com.metis.meishuquan.activity.info.homepage;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StudioDetailActivity extends BaseActivity {

    private static final String TAG = StudioDetailActivity.class.getSimpleName();

    public static final String KEY_STUDIO_INFO = "studio_info";

    private StudioBaseInfo mInfo = null;

    private ViewPager mViewPager = null;

    private List<ImageView> mImageView = new ArrayList<ImageView>();

    private ImageAdapter mAdapter = null;

    private int mOffset = 1;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            final int currentItem = mViewPager.getCurrentItem();
            if (currentItem == 0) {
                mOffset = 1;
            } else if (currentItem == mImageView.size() - 1) {
                mOffset = -1;
            }
            mViewPager.setCurrentItem(currentItem + mOffset, true);
            //mHandler.postDelayed(this, 3000);
        }
    };

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_detail);

        mViewPager = (ViewPager)this.findViewById(R.id.studio_detail_album);

        mInfo = (StudioBaseInfo)getIntent().getSerializableExtra(KEY_STUDIO_INFO);
        //mInfo = new StudioBaseInfo ();
        if (mInfo == null) {
            finish();
            return;
        }
        List<String> mImageUrlList = mInfo.getImageList();
        /*mImageUrlList.add("http://ww2.sinaimg.cn/bmiddle/91e4a538gw1ervix6wbtqj20c30itn18.jpg");
        mImageUrlList.add("http://ww1.sinaimg.cn/bmiddle/91e4a538gw1ervixmbgekj20c10irdjv.jpg");
        mImageUrlList.add("http://ww2.sinaimg.cn/bmiddle/91e4a538gw1ervixu8khlj20c40itaec.jpg");
        mImageUrlList.add("http://ww3.sinaimg.cn/bmiddle/91e4a538gw1erviyos36ej20sg0g0gr3.jpg");*/
        final int length = mImageUrlList.size();
        for (int i = 0; i < length; i++) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoaderUtils.getImageLoader(this).displayImage(
                    mImageUrlList.get(i), iv,
                    ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
            );
            mImageView.add(iv);
        }

        mAdapter = new ImageAdapter(mImageView);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        stop();
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        start();
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        stop();
                        break;
                }
            }
        });
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_introduce);
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    private void start () {
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void stop () {
        mHandler.removeCallbacks(mRunnable);
    }

    private class ImageAdapter extends PagerAdapter {

        //private List<String> mImageList = null;
        private List<ImageView> mViewList = null;

        public ImageAdapter (/*List<String> images, */List<ImageView> viewList) {
            /*mImageList = images;*/
            mViewList = viewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

}
