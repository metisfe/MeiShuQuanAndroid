package com.metis.meishuquan;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GuideActivity extends ActionBarActivity {

    private ViewPager mPager = null;

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mPager = (ViewPager)findViewById(R.id.guide_pager);

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

        mPager.setAdapter(new GuideAdapter(getSupportFragmentManager()));
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
