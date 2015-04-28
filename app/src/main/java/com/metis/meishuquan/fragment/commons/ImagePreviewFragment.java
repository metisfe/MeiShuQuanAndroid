package com.metis.meishuquan.fragment.commons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/4/20.
 */
public class ImagePreviewFragment extends Fragment implements ViewPager.OnPageChangeListener{

    //public static final String KEY_

    private ViewPager mViewPager = null;

    private String[] mUrlArray = {};
    private List<ImagePreviewSingleFragment> mFragmentList = new ArrayList<ImagePreviewSingleFragment>();

    private ImagePreviewAdapter mAdapter = null;

    private ImagePreviewSingleFragment.OnPhotoClickListener mPhotoClickListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_prview, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager)view.findViewById(R.id.image_preview_viewpager);
        mViewPager.setOnPageChangeListener(this);
        mAdapter = new ImagePreviewAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        setOnPhotoClickListener(mPhotoClickListener);
    }

    public void setUrlArray (String[] urlArray) {
        mUrlArray = urlArray;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            final int offset = Math.abs(position - i);
            if (offset > 1) {
                mAdapter.getItem(i).releaseImage();
            }/* else {
                ImagePreviewSingleFragment fragment = mAdapter.getItem(i);
                if (!fragment.isLoaded()) {
                    fragment.loadImageUrl(mUrlArray[position]);
                }
            }*/
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragmentList.clear();
    }

    private class ImagePreviewAdapter extends FragmentStatePagerAdapter {

        public ImagePreviewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public ImagePreviewSingleFragment getItem(int position) {
            if (position < mFragmentList.size()) {
                ImagePreviewSingleFragment singleFragment = mFragmentList.get(position);
                singleFragment.loadImageUrl(mUrlArray[position]);
                return singleFragment;
            }
            ImagePreviewSingleFragment singleFragment = new ImagePreviewSingleFragment();
            singleFragment.loadImageUrl(mUrlArray[position]);
            mFragmentList.add(singleFragment);
            return singleFragment;
        }

        @Override
        public int getCount() {
            return mUrlArray.length;
        }
    }

    public void setOnPhotoClickListener (ImagePreviewSingleFragment.OnPhotoClickListener listener) {
        mPhotoClickListener = listener;
        if (mAdapter != null) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                mAdapter.getItem(i).setOnPhotoClickListener(listener);
            }
        }
    }
}
