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

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.circle.ChatListFragment;
import com.metis.meishuquan.fragment.circle.CircleBaseFragment;
import com.metis.meishuquan.fragment.circle.ContactListFragment;
import com.metis.meishuquan.fragment.circle.MomentsFragment;
import com.metis.meishuquan.fragment.circle.PostMomentFragment;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.shared.TabBar;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

/**
 * Created by wudi on 4/2/2015.
 */
public class CircleFragment extends Fragment {

    private TabBar tabBar;
    private CircleTitleBar titleBar;
    private ViewPager viewPager;
    private TabPageIndicatorAdapter fragmentPagerAdapter;
    private TabPageIndicator indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_circlefragment, container, false);

        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_circlefragment_tab_bar);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_shared_circlefragment_title_bar);
//        titleBar.setText("user name");
//        titleBar.setRightButton("post", 0, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PostMomentFragment postMomentFragment = new PostMomentFragment();
//                Bundle args = new Bundle();
//                postMomentFragment.setArguments(args);
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
//                ft.add(R.id.content_container, postMomentFragment);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });
//
//        titleBar.setLeftButton("cancel",0,new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.remove(CircleFragment.this);
//                ft.commit();
//            }
//        });

        this.viewPager = (ViewPager) rootView.findViewById(R.id.fragment_shared_circlefragment_viewpager);
        this.indicator = (TabPageIndicator) rootView.findViewById(R.id.fragment_shared_circlefragment_topbar_indicator);
        this.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (fragmentPagerAdapter != null && fragmentPagerAdapter.fragments != null && fragmentPagerAdapter.fragments.length > position && fragmentPagerAdapter.fragments[position] != null) {
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
                    return "首页";
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
