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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.topline.DataHelper;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.fragment.ToplineFragment.ItemFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.ChannelItem;
import com.metis.meishuquan.model.topline.News;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.SystemUtil;
import com.metis.meishuquan.view.shared.TabBar;
import com.metis.meishuquan.view.topline.ChannelManageView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：头条
 * <p/>
 * Created by wudi on 3/15/2015.
 */
public class ToplineFragment extends BaseFragment {
    private TabBar tabBar;//底部导航栏
    private ViewPager viewPager;
    private TabPageIndicatorAdapter fragmentPagerAdapter;
    private TabPageIndicator indicator;
    private ImageView imgAddChannel;
    private ChannelManageView cmv;
    private ViewGroup rootView;
    private SharedPreferencesUtil spu;
    private String channelJsonStr;

    private List<News> lstNews = new ArrayList<>();
    private boolean addChannelPoped;
    private int lastNewsId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //TODO: read channel cache if not exist read from hard code
        //加载频道数据
        getChannelItems();

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_toplinefragment, container, false);

        //初始化视图及成员
        initView(rootView);
        initEvent();

        //2、默认加载首个频道的内容
//        if (lstOtherItems.size() > 0) {
//            lastNewsId = 0;
//            //initNews(lstOtherItems.get(0).getId(), lastNewsId);//TODO:测试数据在lstOtherItems中，应改成lstUserItems  获取最新数据
//            initNews(6,0);
//        }

        initNews(6, 0);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //初始化事件
//        initEvent();

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
                //lstNews=getListNews(jsonStr);
                Log.i("jsonStr", String.valueOf(lstNews.size()));
            }
        }, channelId, lastNewsId);
    }

    public static List<News> getListNews(String jsonString) {
        List<News> list = new ArrayList<News>();
        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<News>>() {
        }.getType());
        return list;
    }

    /**
     * 根据网络状态获取TopBar的频道数据（有网络时，从网络上获取；无网络时，从本地缓存中获取；缓存中无数据，加载默认数据）
     */
    private void getChannelItems() {
        TopLineOperator topLineOperator = TopLineOperator.getInstance();
        //将网络返回的数据添加至缓存中
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            topLineOperator.addChannelItemsToLoacal();
        }

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
    private void initView(ViewGroup rootView) {
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_toplinefragment_tab_bar);
        this.viewPager = (ViewPager) rootView.findViewById(R.id.fragment_shared_toplinefragment_viewpager);
        this.indicator = (TabPageIndicator) rootView.findViewById(R.id.topbar_indicator);

        this.imgAddChannel = (ImageView) rootView.findViewById(R.id.img_add_channel);
        this.imgAddChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开频道管理Activity
                openChannelManageView();
            }
        });

//        spu=SharedPreferencesUtil.getInstanse(getActivity());
//        String json=spu.getStringByKey(SharedPreferencesUtil.CHANNELS);
//        this.fragmentPagerAdapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager(), "");

        this.cmv = new ChannelManageView(getActivity(), null, 0);
        this.cmv.setChannelManageViewListener(new ChannelManageView.ChannelManageViewListener() {
            @Override
            public void hide(ViewGroup channelManageView, List<ChannelItem> lstUserChannel, List<ChannelItem> lstOtherChannel) {
                if (!addChannelPoped) {
                    return;
                }

                addChannelPoped = false;
                //TODO: copy the data from lstuserchannel to viewpager's adapter's data then notifydatasetchanged.

                channelJsonStr=spu.getStringByKey(SharedPreferencesUtil.CHANNELS);
                fragmentPagerAdapter.changeData(channelJsonStr);
                fragmentPagerAdapter.notifyDataSetChanged();
                indicator.notifyDataSetChanged();

                ViewGroup topLineViewGroup = (ViewGroup) getActivity().findViewById(R.id.rl_topline);
                topLineViewGroup.removeView(channelManageView);
                int yStart = -getActivity().getResources().getDisplayMetrics().heightPixels;
                int yEnd = 0;
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, yEnd, yStart);
                getAnimation(translateAnimation);
                channelManageView.startAnimation(translateAnimation);

                //重新加载数据
//                String json=spu.getStringByKey(SharedPreferencesUtil.CHANNELS);
//                fragmentPagerAdapter= new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager(),json);
//                viewPager.setAdapter(fragmentPagerAdapter);
//                indicator.setViewPager(viewPager);
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
     * ViewPager监听类
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //Toast.makeText(getActivity(), lstUserItems.get(position).getName(), Toast.LENGTH_SHORT).show();
            //切换Fragment并加载数据
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void refreshData(){
    }


    /**
     * ViewPager适配器
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        private Fragment fragment = null;
        public List<ChannelItem> userItems = null;
        public List<ChannelItem> otherItems=null;


        public TabPageIndicatorAdapter(FragmentManager fm, String jsonStr) {
            super(fm);
            fragment = null;
            changeData(jsonStr);
        }

        //交换数据
        public void changeData(String jsonStr){
            DataHelper helper= new DataHelper(jsonStr);
            if (jsonStr.equals("")){
                userItems=helper.getLocalUserChannel();
                otherItems=helper.getLocalOtherChannel();
            }else{
                userItems=helper.getUserChannels();
                if (userItems.size()==0){
                    userItems=helper.getLocalUserChannel();
                }
                otherItems=helper.getOtherChannels();
            }
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递数据
            fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("arg", "test");
            fragment.setArguments(args);

            return fragment;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return userItems.get(position % userItems.size()).getChannelName();
        }

        @Override
        public int getCount() {
            return userItems.size();
            //return OTHER_CHANNEL.length;
        }
    }
}
