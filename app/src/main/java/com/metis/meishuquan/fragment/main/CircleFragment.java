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
        titleBar.setText("user name");
        titleBar.setRight("post",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostMomentFragment postMomentFragment = new PostMomentFragment();
                Bundle args = new Bundle();
                postMomentFragment.setArguments(args);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, postMomentFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return rootView;
    }

    class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            Fragment new fra
//            fragment.setArguments(args);
//            return fragment;
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}