package com.metis.meishuquan.fragment.commons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.view.shared.TabBar;

/**
 * Created by wangjin on 15/5/26.
 */
public class FragmentWaitingForAssess extends Fragment {
    private TabBar tabBar;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_buid_waiting_assess_fragment, null, false);
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_circlefragment_tab_bar);
//        if (GlobalData.tabs.size() > 0) {
//            for (int i = 0; i < GlobalData.tabs.size(); i++) {
//                TabBar.showOrHide(GlobalData.tabs.get(i), true);
//            }
//        }
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        return rootView;
    }
}
