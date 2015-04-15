package com.metis.meishuquan.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.topline.DataHelper;
import com.metis.meishuquan.fragment.Topline.ItemFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.topline.ChannelItem;
import com.metis.meishuquan.model.topline.News;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.shared.TabBar;
import com.metis.meishuquan.view.topline.ChannelManageView;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：头条
 * <p/>
 * Created by wudi on 3/15/2015.
 */
public class ToplineFragment extends Fragment {
    private TabBar tabBar;//底部导航栏
    private ViewPager viewPager;
    private TabPageIndicatorAdapter fragmentPagerAdapter;
    private TabPageIndicator indicator;
    private ImageView imgAddChannel;
    private ChannelManageView cmv;
    private ViewGroup rootView;
    private SharedPreferencesUtil spu;
    private String channelJsonStr;

    private List<News> lstNews = new ArrayList<News>();
    private boolean addChannelPoped;
    private int lastNewsId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //TODO: read channel cache if not exist read from hard code
        //加载频道数据
        getChannelItems();

        //加载默认频道的数据
        initNews(6, 0);

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_toplinefragment, container, false);

        //初始化视图及成员变量
        initView(rootView);
        initEvent();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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
        topLineOperator.addNewsListByChannelIdToCache(channelId, lastNewsId);
    }

    /**
     * 根据网络状态获取TopBar的频道数据（有网络时，从网络上获取；无网络时，从本地缓存中获取；缓存中无数据，加载默认数据）
     */
    private void getChannelItems() {
        TopLineOperator topLineOperator = TopLineOperator.getInstance();
        //将网络返回的数据添加至缓存中
        topLineOperator.addChannelItemsToLoacal();


        //加载缓存中的数据
        spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
        channelJsonStr = spu.getStringByKey(SharedPreferencesUtil.CHANNELS);
        //判断在缓存中是否有数据
        if (!channelJsonStr.equals("")) {
            this.fragmentPagerAdapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager(), channelJsonStr);
        } else {
            //缓存无数据时加载默认数据
            this.fragmentPagerAdapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager(), "");
        }
    }

    //初始化视图及成员
    private void initView(final ViewGroup rootView) {
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_toplinefragment_tab_bar);
        this.viewPager = (ViewPager) rootView.findViewById(R.id.fragment_shared_toplinefragment_viewpager);
        this.indicator = (TabPageIndicator) rootView.findViewById(R.id.topbar_indicator);

        this.imgAddChannel = (ImageView) rootView.findViewById(R.id.img_add_channel);
        this.imgAddChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开频道管理
                openChannelManageView();
            }
        });

        this.cmv = new ChannelManageView(getActivity(), null, 0);
        this.cmv.setChannelManageViewListener(new ChannelManageView.ChannelManageViewListener() {
            @Override
            public void hide(ViewGroup channelManageView, List<ChannelItem> lstUserChannel, List<ChannelItem> lstOtherChannel) {
                if (!addChannelPoped) {
                    return;
                }
                addChannelPoped = false;

                channelJsonStr = spu.getStringByKey(SharedPreferencesUtil.CHANNELS);
                fragmentPagerAdapter.changeData(channelJsonStr);
                fragmentPagerAdapter.notifyDataSetChanged();
                indicator.notifyDataSetChanged();

                ViewGroup topLineViewGroup = (ViewGroup) rootView.findViewById(R.id.rl_topline);
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
    }

    /**
     * 打开频道管理
     */
    private void openChannelManageView() {
        if (addChannelPoped) {
            return;
        }

        addChannelPoped = true;
        ViewGroup topLineViewGroup = (ViewGroup) rootView.findViewById(R.id.rl_topline);
        topLineViewGroup.addView(this.cmv);
        this.cmv.refreshData();//重新加载数据（数据来源于网络缓存）
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
     * ViewPager适配器
     */
    class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
        public List<ChannelItem> userItems = null;
        public List<ChannelItem> otherItems = null;


        public TabPageIndicatorAdapter(FragmentManager fm, String jsonStr) {
            super(fm);
            changeData(jsonStr);
        }

        //交换数据
        public void changeData(String jsonStr) {
            DataHelper helper = new DataHelper(jsonStr);
            if (jsonStr.equals("")) {
                userItems = helper.getLocalUserChannel();
                otherItems = helper.getLocalOtherChannel();
            } else {
                userItems = helper.getUserChannels();
                if (userItems.size() == 0) {
                    userItems = helper.getLocalUserChannel();
                }
                otherItems = helper.getOtherChannels();
            }
        }

        @Override
        public Fragment getItem(int position) {
            int channelId = userItems.get(position).getChannelId();
            initNews(channelId, 0);
            ItemFragment fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putInt("channelId", channelId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return userItems.get(position % userItems.size()).getChannelName();
        }

        @Override
        public int getCount() {
            return userItems.size();
        }
    }
}
