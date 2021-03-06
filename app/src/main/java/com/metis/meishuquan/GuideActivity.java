package com.metis.meishuquan;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GuideActivity extends ActionBarActivity {

    private ViewPager mPager = null;

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private LinearLayout mAbsLayout = null;
    private FrameLayout mCircleLayout = null;
    private ImageView mCirleIv = null;

    private float mDensity = 1;
    private int marginInDp = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mDensity = getResources().getDisplayMetrics().density;

        mPager = (ViewPager)findViewById(R.id.guide_pager);
        mAbsLayout = (LinearLayout)findViewById(R.id.guide_dircle_container);
        mCircleLayout = (FrameLayout)findViewById(R.id.guide_dot_container);

        CommonFragment fragment1 = new CommonFragment();
        fragment1.setImage(R.drawable.guide_1);

        CommonFragment fragment2 = new CommonFragment();
        fragment2.setImage(R.drawable.guide_2);

        CommonFragment fragment3 = new CommonFragment();
        fragment3.setImage(R.drawable.guide_3);

        LastFragment lastFragment = new LastFragment();
        lastFragment.setImage(R.drawable.guide_4);
        lastFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
        mFragments.add(lastFragment);

        for (int i = 0; i < mFragments.size(); i++) {
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)iv.getLayoutParams();
            if (params == null) {
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            params.setMargins((int)(marginInDp * mDensity), 0, (int)(marginInDp * mDensity), 0);
            iv.setImageResource(R.drawable.guide_circle);
            iv.setLayoutParams(params);
            mAbsLayout.addView(iv);
        }
        mCirleIv = new ImageView(this);
        mCirleIv.setImageResource(R.drawable.guide_dot);
        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins((int)(marginInDp + 1 * mDensity), (int)(1 * mDensity), (int)(marginInDp + 1 * mDensity), (int)(1 * mDensity));
        mCirleIv.setLayoutParams(params);
        mCircleLayout.addView(mCirleIv);
        mPager.setAdapter(new GuideAdapter(getSupportFragmentManager()));
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float offset = (marginInDp + 1) * mDensity + (marginInDp + 15) * mDensity * position + (marginInDp + 15) * mDensity * positionOffset;
                FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams)mCirleIv.getLayoutParams();
                params1.setMargins((int)offset, (int)(1 * mDensity), 0, (int)(1 * mDensity));
                mCirleIv.setLayoutParams(params1);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class GuideAdapter extends FragmentStatePagerAdapter {

        public GuideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    public static class CommonFragment extends Fragment {

        private ImageView mImageView = null;

        private int mRes = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_user_guide_common, null);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mImageView = (ImageView)view.findViewById(R.id.common_image);

            if (mRes != 0) {
                setImage(mRes);
            }
        }

        public void setImage (int id) {
            mRes = id;
            if (mImageView != null) {
                mImageView.setImageResource(mRes);
            }
        }
    }

    public static class LastFragment extends Fragment {

        private ImageView mImageView = null;
        private TextView mBtn = null;

        private int mRes = 0;
        private View.OnClickListener mListener = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_user_guide_last, null);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            mImageView = (ImageView)view.findViewById(R.id.last_image);
            mBtn = (TextView)view.findViewById(R.id.last_btn);

            if (mRes != 0) {
                setImage(mRes);
            }
            setOnClickListener(mListener);
        }

        public void setOnClickListener (View.OnClickListener listener) {
            mListener = listener;
            if (mBtn != null) {
                mBtn.setOnClickListener(mListener);
            }
        }

        public void setImage (int id) {
            mRes = id;
            if (mImageView != null) {
                mImageView.setImageResource(mRes);
            }
        }
    }

}
