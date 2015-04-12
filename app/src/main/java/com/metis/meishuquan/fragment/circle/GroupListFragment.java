package com.metis.meishuquan.fragment.circle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.circle.CircleTitleBar;

/**
 * Created by wudi on 4/12/2015.
 */
public class GroupListFragment extends Fragment {
    private ViewGroup rootView;
    private CircleTitleBar titleBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_grouplistfragment, container, false);
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_grouplistfragment_titlebar);
        titleBar.setText("群聊");
        titleBar.setLeftButton("返回",0,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

}
