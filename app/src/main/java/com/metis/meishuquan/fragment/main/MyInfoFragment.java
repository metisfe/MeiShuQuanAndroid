package com.metis.meishuquan.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.AdvanceActivity;
import com.metis.meishuquan.activity.info.InfoActivity;
import com.metis.meishuquan.activity.info.SettingActivity;
import com.metis.meishuquan.fragment.login.LoginFragment;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.view.shared.TabBar;

/**
 * Created by wudi on 3/15/2015.
 */
public class MyInfoFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MyInfoFragment.class.getSimpleName();

    private TabBar tabBar;
    private View mInfoContainer = null, mLoginView;
    private TextView mInfoName = null;
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
        mInfoContainer = view.findViewById(R.id.my_info_profile_container);
        mLoginView = view.findViewById(R.id.my_info_login);
        mInfoName = (TextView)view.findViewById(R.id.my_info_name);

        mCollectionView = view.findViewById(R.id.my_info_collections);
        mAskView = view.findViewById(R.id.my_info_asks);
        mCommentView = view.findViewById(R.id.my_info_comments);
        mClassesView = view.findViewById(R.id.my_info_classes);
        mNameCardView = view.findViewById(R.id.my_info_name_card);
        mAdvanceView = view.findViewById(R.id.my_info_advances);
        mSettingView = view.findViewById(R.id.my_info_setting);

        mInfoContainer.setOnClickListener(this);
        //mLoginView.setOnClickListener(this);

        mCollectionView.setOnClickListener(this);
        mAskView.setOnClickListener(this);
        mCommentView.setOnClickListener(this);
        mClassesView.setOnClickListener(this);
        mNameCardView.setOnClickListener(this);
        mAdvanceView.setOnClickListener(this);
        mSettingView.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UserInfoOperator.getInstance().getUserInfo("100001", new UserInfoOperator.OnGetListener<User>() {
            @Override
            public void onGet(boolean succeed, User user) {
                if (succeed) {
                    mLoginView.setVisibility(View.GONE);
                    mInfoName.setText(user.getName());
                } else {
                    mLoginView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_info_profile_container:
                startActivity(new Intent(getActivity(), InfoActivity.class));
                break;
            case R.id.my_info_login:
                showLoginFragment();
                break;
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
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

    private LoginFragment mLoginFm = null;
    private void showLoginFragment () {
        if (mLoginFm == null) {
            mLoginFm = new LoginFragment();
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.my_info_extra_layout, mLoginFm);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void hideLoginFragment () {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(mLoginFm);
        manager.popBackStack();
        ft.commit();
    }
}
