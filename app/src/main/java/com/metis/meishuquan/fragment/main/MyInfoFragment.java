package com.metis.meishuquan.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.AdvanceActivity;
import com.metis.meishuquan.view.shared.TabBar;

/**
 * Created by wudi on 3/15/2015.
 */
public class MyInfoFragment extends Fragment implements View.OnClickListener {

    private TabBar tabBar;

    private View mCollectionView, mAskView, mCommentView, mClassesView, mNameCardView, mAdvanceView, mSettingView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_myinfofragment, container, false);

        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_myinfofragment_tab_bar);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCollectionView = view.findViewById(R.id.my_info_collections);
        mAskView = view.findViewById(R.id.my_info_asks);
        mCommentView = view.findViewById(R.id.my_info_comments);
        mClassesView = view.findViewById(R.id.my_info_classes);
        mNameCardView = view.findViewById(R.id.my_info_name_card);
        mAdvanceView = view.findViewById(R.id.my_info_advances);
        mSettingView = view.findViewById(R.id.my_info_setting);

        mCollectionView.setOnClickListener(this);
        mAskView.setOnClickListener(this);
        mCommentView.setOnClickListener(this);
        mClassesView.setOnClickListener(this);
        mNameCardView.setOnClickListener(this);
        mAdvanceView.setOnClickListener(this);
        mSettingView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_info_collections:
                break;
            case R.id.my_info_asks:
                break;
            case R.id.my_info_comments:
                break;
            case R.id.my_info_classes:
                break;
            case R.id.my_info_name_card:
                break;
            case R.id.my_info_advances:
                startActivity(new Intent(getActivity(), AdvanceActivity.class));
                break;
            case R.id.my_info_setting:
                break;
        }
    }
}
