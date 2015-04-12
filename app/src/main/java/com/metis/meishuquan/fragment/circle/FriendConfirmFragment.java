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
public class FriendConfirmFragment extends Fragment {
    private ViewGroup rootView;
    private CircleTitleBar titleBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_friendconfirmfragment, container, false);
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_friendconfirmfragment_titlebar);
        titleBar.setText("新的朋友");
        titleBar.setRightButton("增加朋友",0,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendFragment addFriendFragment = new AddFriendFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, addFriendFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        titleBar.setLeftButton("返回",0,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

}