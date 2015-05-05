package com.metis.meishuquan.adapter.commons;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wangjin on 15/5/5.
 */
public class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {

    private String[] titles = null;
    private List<String> lstTitle = null;

    public TabPageIndicatorAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    public TabPageIndicatorAdapter(FragmentManager fm, List<String> lstTitle) {
        super(fm);
        this.lstTitle = lstTitle;
    }

    @Override
    public Fragment getItem(int position) {
        //TODO:
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            return titles[position];
        } else {
            return lstTitle.get(position);
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
