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
 * Created by wudi on 4/2/2015.
 */
public class PostMomentFragment extends Fragment {
    private CircleTitleBar titleBar;
    private ViewGroup openArea;
    private View openIcon;

    private boolean open;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_postmoment, container, false);

        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_postmoment_title_bar);
        titleBar.setText("post moment");
        titleBar.setRightButton("send",0, null);
        titleBar.setLeftButton("cancel",0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(PostMomentFragment.this);
                ft.commit();
            }
        });

        this.openIcon = rootView.findViewById(R.id.fragment_circle_postmoment_open_image);

        this.openArea = (ViewGroup) rootView.findViewById(R.id.fragment_circle_postmoment_open_area);
        this.openArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open = !open;
                openIcon.setBackgroundResource(open ? R.drawable.fragment_circle_postmoment_choice_true : R.drawable.fragment_circle_postmoment_choice_false);
            }
        });
        return rootView;
    }
}
