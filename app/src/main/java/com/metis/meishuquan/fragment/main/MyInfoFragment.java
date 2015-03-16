package com.metis.meishuquan.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.view.shared.TabBar;

/**
 * Created by wudi on 3/15/2015.
 */
public class MyInfoFragment extends BaseFragment {
    private TabBar tabBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_myinfofragment, container, false);

        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_myinfofragment_tab_bar);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        return rootView;
    }
}
