package com.metis.meishuquan.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.control.topline.ChannelControl;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.fragment.ToplineFragment.ItemFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.ChannelItem;
import com.metis.meishuquan.view.shared.TabBar;
import com.metis.meishuquan.view.topline.ChannelManageView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：头条
 * <p/>
 * Created by wudi on 3/15/2015.
 */
public class ToplineFragment extends BaseFragment implements View.OnClickListener {
    private TabBar tabBar;//底部导航栏
    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private TabPageIndicator indicator;
    private ImageView imgAddChannel;
    private ChannelManageView cmv;
    private List<ChannelItem> lstAllChannels;
    private List<ChannelItem> lstUserItems = new ArrayList<>();
    private List<ChannelItem> lstOtherItems = new ArrayList<>();
    private boolean addChannelPoped;
    private int lastNewsId = 0;

    private static final String[] USER_CHANNEL = new String[]{"推荐", "热点", "素描", "色彩",
            "速写", "创作", "设计", "动漫", "摄影"};
    private static final String[] OTHER_CHANNEL = new String[]{"轻松一刻", "正能量", "另一面", "女人",
            "财经", "数码", "情感", "科技"};


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_toplinefragment, container, false);

        //初始化视图及成员
        initView(rootView);

        //1、加载频道数据
        getChannelItems();

        //2、默认加载首个频道的内容
//        if (lstOtherItems.size() > 0) {
//            lastNewsId = 0;
//            //initNews(lstOtherItems.get(0).getId(), lastNewsId);//TODO:测试数据在lstOtherItems中，应改成lstUserItems  获取最新数据
//            initNews(6,0);
//        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //初始化事件
        initEvent();

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 根据频道Id获取频道列表
     *
     * @param channelId  频道Id
     * @param lastNewsId
     */
    private void initNews(int channelId, int lastNewsId) {
        TopLineOperator topLineOperator = TopLineOperator.getInstance();
        topLineOperator.getNewsListByChannelId(new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                Gson gson = new Gson();
                String jsonStr = gson.toJson(result);
                Log.i("jsonStr", jsonStr);
            }
        }, channelId, lastNewsId);
    }

    /**
     * 根据网络状态获取TopBar的频道数据（有网络时，从网络上获取；无网络时，从本地缓存中获取；缓存中无数据，加载默认数据）
     */
    private void getChannelItems() {
        TopLineOperator topLineOperator = TopLineOperator.getInstance();
        ChannelControl channelContol = new ChannelControl(getActivity());
        List<ChannelItem> lstUserCache = channelContol.getChannelCache(true);
        //判断网络状态
        if (topLineOperator.isNetworkConnected(getActivity())) {
            //加载网络数据
            topLineOperator.getChannelItems(new ApiOperationCallback<ReturnInfo<String>>() {
                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    Log.i("result", result.getInfo());
                    Gson gson = new Gson();
                    String str = gson.toJson(result);
                    Log.i("str", str);
                    if (str.isEmpty()) {
                        lstUserItems.clear();
                        lstOtherItems.clear();
                        getHttpChannels(str);
                        initEvent();
                    }
                }
            });
        }
        if (lstUserCache.size() > 0) {//判断缓存中是否有数据，有数据则加载数据
            this.lstUserItems = lstUserCache;
            this.lstOtherItems = channelContol.getChannelCache(false);
        } else {
            //加载默认数据
            for (int i = 0; i < USER_CHANNEL.length; i++) {
                ChannelItem item = new ChannelItem(i, USER_CHANNEL[i], i, true);
                this.lstUserItems.add(item);
            }

            for (int i = 0; i < OTHER_CHANNEL.length; i++) {
                ChannelItem item = new ChannelItem(i, OTHER_CHANNEL[i], i, false);
                this.lstOtherItems.add(item);
            }
        }
    }

    //根据json串解析数据
    public void getHttpChannels(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject chennels = jsonObject.getJSONObject("data");//得到两个集合（已选择频道集合，未选择频道集合）
            JSONArray selectedChennels = chennels.getJSONArray("unSelectedChannels");
            JSONArray unSelectedChennels = chennels.getJSONArray("unSelectedChannels");
            for (int i = 0; i < selectedChennels.length(); i++) {
                JSONObject chennel = selectedChennels.getJSONObject(i);
                ChannelItem item = new ChannelItem();
                item.setId(chennel.getInt("channelId"));
                item.setName(chennel.getString("channelName"));
                item.setOrderId(chennel.getInt("orderNum"));
                item.setSelected(chennel.getBoolean("isAllowReset"));
                lstUserItems.add(item);
            }
            for (int i = 0; i < unSelectedChennels.length(); i++) {
                JSONObject chennel = unSelectedChennels.getJSONObject(i);
                ChannelItem item = new ChannelItem();
                item.setId(chennel.getInt("channelId"));
                item.setName(chennel.getString("channelName"));
                item.setOrderId(chennel.getInt("orderNum"));
                item.setSelected(chennel.getBoolean("isAllowReset"));
                lstOtherItems.add(item);
            }
            Log.i("lstOtherItems", String.valueOf(lstOtherItems.size()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //初始化视图及成员
    private void initView(ViewGroup rootView) {
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_toplinefragment_tab_bar);
        this.viewPager = (ViewPager) rootView.findViewById(R.id.fragment_shared_toplinefragment_viewpager);
        this.indicator = (TabPageIndicator) rootView.findViewById(R.id.topbar_indicator);

        this.imgAddChannel = (ImageView) rootView.findViewById(R.id.img_add_channel);
        this.fragmentPagerAdapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager());

        this.cmv = new ChannelManageView(getActivity(), null, 0);
        this.cmv.setChannelManageViewListener(new ChannelManageView.ChannelManageViewListener() {
            @Override
            public void hide(ViewGroup channelManageView, List<ChannelItem> lstUserChannel, List<ChannelItem> lstOtherChannel) {
                if (!addChannelPoped) {
                    return;
                }

                addChannelPoped = false;
                ToplineFragment.this.lstUserItems = lstUserChannel;
                ToplineFragment.this.lstOtherItems = lstOtherChannel;

                //TODO: copy the data from lstuserchannel to viewpager's adapter's data then notifydatasetchanged.

                ViewGroup topLineViewGroup = (ViewGroup) getActivity().findViewById(R.id.rl_topline);
                topLineViewGroup.removeView(channelManageView);
                int yStart = -getActivity().getResources().getDisplayMetrics().heightPixels;
                int yEnd = 0;
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, yEnd, yStart);
                getAnimation(translateAnimation);
                channelManageView.startAnimation(translateAnimation);
            }
        });
    }

    //初始化事件
    private void initEvent() {
        //设置底部Tab监听事件
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);

        //设置ViewPager适配器
        this.viewPager.setAdapter(fragmentPagerAdapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        this.indicator.setViewPager(viewPager);

        //如果我们要对ViewPager设置监听，用indicator设置就行了
        indicator.setOnPageChangeListener(new PageChangeListener());

        this.imgAddChannel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
        if (addChannelPoped) {
            return;
        }

        addChannelPoped = true;
        ViewGroup topLineViewGroup = (ViewGroup) getActivity().findViewById(R.id.rl_topline);
        topLineViewGroup.addView(this.cmv);
        cmv.setUserChannelList(this.lstUserItems);//将加载出来的数据传递给View
        cmv.setOtherChannelList(this.lstOtherItems);
        this.cmv.initData();
        int yStart = -getActivity().getResources().getDisplayMetrics().heightPixels;
        int yEnd = 0;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, yStart, yEnd);
        getAnimation(translateAnimation);
        cmv.startAnimation(translateAnimation);
    }

    private void getAnimation(TranslateAnimation animation) {
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
    }

    /**
     * ViewPager监听类
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Toast.makeText(getActivity(), lstUserItems.get(position).getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * ViewPager适配器
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        private Fragment fragment = null;


        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
            fragment = null;
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递数据
            fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("arg", lstUserItems.get(position).getName());
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return lstUserItems.get(position % lstUserItems.size()).getName();
            //return OTHER_CHANNEL[position % OTHER_CHANNEL.length];
        }

        @Override
        public int getCount() {
            return lstUserItems.size();
            //return OTHER_CHANNEL.length;
        }
    }
}
