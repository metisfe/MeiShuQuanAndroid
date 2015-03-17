package com.metis.meishuquan.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.topline.ChannelAdapter;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.fragment.TopBarFragment.ItemFragment;
import com.metis.meishuquan.view.shared.TabBar;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Fragment：头条
 *
 * Created by wudi on 3/15/2015.
 */
public class ToplineFragment extends BaseFragment{
    private TabBar tabBar;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private TabPageIndicator indicator;

    private static final String[] TITLE = new String[] { "头条", "房产", "另一面", "女人",
            "财经", "数码", "情感", "科技" };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_toplinefragment, container, false);

        //初始化视图及成员
        initView(rootView);

        //初始化事件
        initEvent();

        return rootView;
    }

    //初始化视图及成员
    private void initView(ViewGroup rootView){
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_toplinefragment_tab_bar);
        this.viewPager = (ViewPager)  rootView.findViewById(R.id.fragment_shared_toplinefragment_viewpager);
        this.indicator = (TabPageIndicator)rootView.findViewById(R.id.topbar_indicator);

        //this.channelAdapter = new ChannelAdapter(this.getActivity());
        this.adapter= new TabPageIndicatorAdapter(this.getActivity().getSupportFragmentManager());
    }

    //初始化事件
    private void initEvent() {
        //设置底部Tab监听事件
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);

        //设置ViewPager适配器
        this.viewPager.setAdapter(adapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        this.indicator.setViewPager(viewPager);

        //如果我们要对ViewPager设置监听，用indicator设置就行了
        indicator.setOnPageChangeListener(new PageChangeListener());
    }

    /**
     * ViewPager监听类
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Toast.makeText(getActivity(), TITLE[position], Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * ViewPager适配器
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递数据
            Fragment fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("arg", TITLE[position]);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position % TITLE.length];
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }
    }
}
