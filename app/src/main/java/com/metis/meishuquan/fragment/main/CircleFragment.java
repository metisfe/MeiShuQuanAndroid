package com.metis.meishuquan.fragment.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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
import com.metis.meishuquan.push.UnReadManager;
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

    private static final String arrowUp = "∧";
    private static final String arrowDown = "∨";

    private TabBar tabBar;
    private CircleTitleBar titleBar;
    private ViewPager viewPager;
    private TabPageIndicatorAdapter fragmentPagerAdapter;
    private TabPageIndicator indicator;
    private PopupMomentsWindow momentsGroupWindow;

    private UnReadManager.Observable mObservable = new UnReadManager.Observable() {
        @Override
        public void onChanged(String tag, int count, int delta) {
            manageTip(tag, count, delta);
        }
    };

    private void manageTip (String tag, int count, int delta) {
        tabBar.setActivityTipVisible(count > 0 ? View.VISIBLE : View.GONE);
        tabBar.setActivityTipText(count + "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UnReadManager.getInstance(getActivity()).registerObservable(UnReadManager.TAG_NEW_STUDENT, mObservable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UnReadManager.getInstance(getActivity()).unregisterObservable(UnReadManager.TAG_NEW_STUDENT, mObservable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String tag = UnReadManager.TAG_NEW_STUDENT;
        int count = UnReadManager.getInstance(getActivity()).getCountByTag(tag);
        manageTip(tag, count, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_circlefragment, container, false);
        //getMomentsGroupInfo();


        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_circlefragment_tab_bar);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        if (GlobalData.tabs.size() > 0) {
            for (int i = 0; i < GlobalData.tabs.size(); i++) {
                TabBar.showOrHide(GlobalData.tabs.get(i), true);
            }
        }

        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_shared_circlefragment_title_bar);

        this.viewPager = (ViewPager) rootView.findViewById(R.id.fragment_shared_circlefragment_viewpager);
        this.indicator = (TabPageIndicator) rootView.findViewById(R.id.fragment_shared_circlefragment_topbar_indicator);

        //朋友圈点击
        this.indicator.setOnTabReselectedListener(new TabPageIndicator.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                final String groupName = fragmentPagerAdapter.titles[0].substring(0, fragmentPagerAdapter.titles[0].length() - 1);
                updateIndicator(new String[]{groupName + arrowUp, "消息", "通讯录"});
                if (position == 0) {
                    momentsGroupWindow = new PopupMomentsWindow(getActivity(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) getActivity()).removeAllAttachedView();
                            updateIndicator(new String[]{groupName + arrowDown, "消息", "通讯录"});
                        }
                    }, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            int groupId = momentsGroupWindow.getGroupId(i);
                            String groupName = momentsGroupWindow.getGroupName(i);
                            GlobalData.momentsGroupId = groupId;
                            updateIndicator(new String[]{groupName + arrowDown, "消息", "通讯录"});
                            LocalBroadcastManager.getInstance(MainApplication.UIContext).sendBroadcast(new Intent("update_moments_list"));
                        }
                    });

                    ((MainActivity) getActivity()).addAttachView(momentsGroupWindow);
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

    private void updateIndicator(String[] title) {
        fragmentPagerAdapter.titles = title;
        indicator.notifyDataSetChanged();
        fragmentPagerAdapter.notifyDataSetChanged();
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
        public String[] titles = new String[]{"全部" + arrowDown, "消息", "通讯录"};

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
            return titles[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
