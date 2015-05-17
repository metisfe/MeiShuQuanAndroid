package com.metis.meishuquan.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.circle.ChatListFragment;
import com.metis.meishuquan.fragment.circle.CircleBaseFragment;
import com.metis.meishuquan.fragment.circle.ContactListFragment;
import com.metis.meishuquan.fragment.circle.MomentsFragment;
import com.metis.meishuquan.fragment.circle.PostMomentFragment;
import com.metis.meishuquan.model.circle.MomentsGroup;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.circle.PopupMomentsWindow;
import com.metis.meishuquan.view.shared.TabBar;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

/**
 * Created by wudi on 4/2/2015.
 */
public class CircleFragment extends CircleBaseFragment {

    private TabBar tabBar;
    private CircleTitleBar titleBar;
    private ViewPager viewPager;
    private TabPageIndicatorAdapter fragmentPagerAdapter;
    private TabPageIndicator indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_circlefragment, container, false);
        getMomentsGroupInfo();


        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_circlefragment_tab_bar);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_shared_circlefragment_title_bar);

        this.viewPager = (ViewPager) rootView.findViewById(R.id.fragment_shared_circlefragment_viewpager);
        this.indicator = (TabPageIndicator) rootView.findViewById(R.id.fragment_shared_circlefragment_topbar_indicator);

        //朋友圈点击
        this.indicator.setOnTabReselectedListener(new TabPageIndicator.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                if (position == 0) {
                    PopupMomentsWindow addWindow = new PopupMomentsWindow(getActivity(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) getActivity()).removeAllAttachedView();
                        }
                    }, new View.OnClickListener() {//全部
                        @Override
                        public void onClick(View v) {
                            GlobalData.momentsGroupId = 0;
                            indicator.setCurrentItem(0);
                        }
                    }, new View.OnClickListener() {//我的主页
                        @Override
                        public void onClick(View v) {
                            GlobalData.momentsGroupId = 0;
                            indicator.setCurrentItem(0);
                        }
                    }, new View.OnClickListener() {//朋友圈
                        @Override
                        public void onClick(View v) {
                            GlobalData.momentsGroupId = 0;
                            indicator.setCurrentItem(0);
                        }
                    }, new View.OnClickListener() {//特别关注
                        @Override
                        public void onClick(View view) {
                            GlobalData.momentsGroupId = 0;
                            indicator.setCurrentItem(0);
                        }
                    }, new View.OnClickListener() {//我的老师
                        @Override
                        public void onClick(View view) {
                            GlobalData.momentsGroupId = 0;
                            indicator.setCurrentItem(0);
                        }
                    }, new View.OnClickListener() {//我的同学
                        @Override
                        public void onClick(View view) {
                            GlobalData.momentsGroupId = 0;
                            indicator.setCurrentItem(0);
                        }
                    });

                    ((MainActivity) getActivity()).addAttachView(addWindow);
                }
            }
        });
        this.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (fragmentPagerAdapter != null && fragmentPagerAdapter.fragments != null
                        && fragmentPagerAdapter.fragments.length > position && fragmentPagerAdapter.fragments[position] != null) {
                    fragmentPagerAdapter.fragments[position].timeToSetTitleBar();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fragmentPagerAdapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager());
        this.viewPager.setAdapter(fragmentPagerAdapter);
        this.indicator.setViewPager(viewPager);
        return rootView;
    }

    private List<MomentsGroup> getMomentsGroupInfo() {
        String json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.MOMENTS_GROUP_INFO);
        ReturnInfo<List<MomentsGroup>> returnInfo = new Gson().fromJson(json, new TypeToken<ReturnInfo<List<MomentsGroup>>>() {
        }.getType());
        return returnInfo.getData();
    }

    @Override
    public void timeToSetTitleBar() {
        getTitleBar().setText("朋友圈");
        getTitleBar().setRightButton("", R.drawable.icon_pic, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostMomentFragment postMomentFragment = new PostMomentFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.content_container, postMomentFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
        boolean firstTimeLoad = true;

        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        public CircleBaseFragment[] fragments = new CircleBaseFragment[3];

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            fragments[position] = null;
        }

        @Override
        public Fragment getItem(int position) {
            CircleBaseFragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new MomentsFragment();
                    break;
                case 1:
                    fragment = new ChatListFragment();
                    break;
                case 2:
                    fragment = new ContactListFragment();
                    break;
            }

            fragment.setTitleBar(titleBar);
            if (firstTimeLoad) {
                firstTimeLoad = false;
                fragment.timeToSetTitleBar();
            }
            fragments[position] = fragment;
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "朋友圈";
                case 1:
                    return "消息";
                case 2:
                    return "通讯录";
            }
            return "";
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
