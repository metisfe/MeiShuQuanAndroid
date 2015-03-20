package com.metis.meishuquan.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.view.topline.ChannelManageView;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.fragment.TopBarFragment.ItemFragment;
import com.metis.meishuquan.model.topline.ChannelItem;
import com.metis.meishuquan.view.shared.TabBar;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：头条
 *
 * Created by wudi on 3/15/2015.
 */
public class ToplineFragment extends BaseFragment implements View.OnClickListener,ChannelManageView.ChannelManageViewListener{
    private TabBar tabBar;//底部导航栏
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private TabPageIndicator indicator;
    private ImageView imgAddChannel;
    private ChannelManageView cmv;
    private List<ChannelItem> lstUserItems=new ArrayList<>();
    private List<ChannelItem> lstOtherItems=new ArrayList<>();


    private static final String[] USER_CHANNEL = new String[] { "推荐", "热点", "素描", "色彩",
            "速写", "创作", "设计", "动漫" ,"摄影"};
    private static final String[] OTHER_CHANNEL = new String[] { "轻松一刻", "正能量", "另一面", "女人",
            "财经", "数码", "情感", "科技" };




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_toplinefragment, container, false);

        //初始化视图及成员
        initView(rootView);

        //加载数据
        //1、加载频道数据
        //getChannelItems();

        //2、默认加载首个频道的内容

        return rootView;
    }

    /**
     * 根据网络状态获取TopBar的频道数据（有网络时，从网络上获取；无网络时，从本地缓存中获取；缓存中无数据，加载默认数据）
     */
    private void getChannelItems() {
        //加载默认数据
        for (int i = 0; i <this.USER_CHANNEL.length ; i++) {
            ChannelItem userItem=new ChannelItem(i,USER_CHANNEL[i],i,true);
            lstUserItems.add(userItem);
        }
        for (int i = 0; i <this.OTHER_CHANNEL.length ; i++) {
            ChannelItem otherItem=new ChannelItem(i,OTHER_CHANNEL[i],i,false);
            lstOtherItems.add(otherItem);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //初始化事件
        initEvent();

        super.onActivityCreated(savedInstanceState);
    }

    //初始化视图及成员
    private void initView(ViewGroup rootView){
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_toplinefragment_tab_bar);
        this.viewPager = (ViewPager)  rootView.findViewById(R.id.fragment_shared_toplinefragment_viewpager);
        this.indicator = (TabPageIndicator)rootView.findViewById(R.id.topbar_indicator);
        cmv=new ChannelManageView(getActivity(),null,0);

        this.imgAddChannel= (ImageView) rootView.findViewById(R.id.img_add_channel);
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

        this.imgAddChannel.setOnClickListener(this);

        this.cmv.setChannelManageViewListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_add_channel:
                //打开频道管理Activity
                openChannelManageView();
                break;
        }

    }

    /**
     * 打开频道管理
     */
    private void openChannelManageView() {

        ViewGroup topLineViewGroup= (ViewGroup) getActivity().findViewById(R.id.rl_topline);
        topLineViewGroup.addView(this.cmv);
        int yStart = -getActivity().getResources().getDisplayMetrics().heightPixels;
        int yEnd = 0;
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,yStart, yEnd);
        getAnimation(translateAnimation);
        cmv.startAnimation(translateAnimation);
//        popupWindow = new PopupWindow(cmv,topLineViewGroup.getWidth(),topLineViewGroup.getHeight());
//        Log.d("width",String.valueOf(topLineViewGroup.getWidth()));
//        Log.d("height",String.valueOf(topLineViewGroup.getHeight()));
//        popupWindow.setAnimationStyle(R.style.AnimationFade);
//        popupWindow.showAsDropDown(cmv,0,-topLineViewGroup.getHeight());//20为系统标题栏的高度
    }

    @Override
    public void hide(ViewGroup channelManageView) {
        //ChannelManageView cmv=new ChannelManageView(getActivity(),null,0);
        ViewGroup topLineViewGroup= (ViewGroup) getActivity().findViewById(R.id.rl_topline);
        topLineViewGroup.removeView(channelManageView);
        int yStart = -getActivity().getResources().getDisplayMetrics().heightPixels;
        int yEnd = 0;
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,yEnd, yStart);
        getAnimation(translateAnimation);
        channelManageView.startAnimation(translateAnimation);
    }

    private void getAnimation(TranslateAnimation animation){
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
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
            Toast.makeText(getActivity(), OTHER_CHANNEL[position], Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * ViewPager适配器
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        private Fragment fragment=null;

        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
            fragment=null;
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递数据
            fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("arg", OTHER_CHANNEL[position]);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return OTHER_CHANNEL[position % OTHER_CHANNEL.length];
        }

        @Override
        public int getCount() {
            return OTHER_CHANNEL.length;
        }
    }
}
