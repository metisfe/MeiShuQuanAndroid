package com.metis.meishuquan.fragment.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.AdvanceActivity;
import com.metis.meishuquan.activity.info.InfoActivity;
import com.metis.meishuquan.activity.info.MyCommentsActivity;
import com.metis.meishuquan.activity.info.MyCourseActivity;
import com.metis.meishuquan.activity.info.MyFavoritesActivity;
import com.metis.meishuquan.activity.info.NameCardQrActivity;
import com.metis.meishuquan.activity.info.SettingActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.fragment.login.LoginFragment;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.shared.TabBar;

/**
 * Created by wudi on 3/15/2015.
 */
public class MyInfoFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MyInfoFragment.class.getSimpleName();

    private TabBar tabBar;
    private View mInfoContainer = null, mLoginView = null, mInfoDetailsContainer = null;
    private TextView mInfoName = null, mAttentionCountTv = null, mFollowersCountTv = null;
    private View mCollectionView, mCommentView, mClassesView, mNameCardView, mAdvanceView, mSettingView;
    private ImageView mProfileIv = null;

    private MainApplication mMainApplication = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainApplication = (MainApplication)activity.getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        mInfoDetailsContainer = view.findViewById(R.id.my_info_container);
        mLoginView = view.findViewById(R.id.my_info_login);
        mInfoName = (TextView)view.findViewById(R.id.my_info_name);
        mProfileIv = (ImageView)view.findViewById(R.id.my_info_profile);
        mAttentionCountTv = (TextView)view.findViewById(R.id.my_info_attention);
        mFollowersCountTv = (TextView)view.findViewById(R.id.my_info_followers);

        mCollectionView = view.findViewById(R.id.my_info_collections);
        mCommentView = view.findViewById(R.id.my_info_comments);
        mClassesView = view.findViewById(R.id.my_info_classes);
        mNameCardView = view.findViewById(R.id.my_info_name_card);
        mAdvanceView = view.findViewById(R.id.my_info_advances);
        mSettingView = view.findViewById(R.id.my_info_setting);

        mInfoContainer.setOnClickListener(this);
        mLoginView.setOnClickListener(this);

        mCollectionView.setOnClickListener(this);
        mCommentView.setOnClickListener(this);
        mClassesView.setOnClickListener(this);
        mNameCardView.setOnClickListener(this);
        mAdvanceView.setOnClickListener(this);
        mSettingView.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume " + MainApplication.userInfo + " " + MainApplication.isLogin());
        if (MainApplication.isLogin()) {
            getUserInfo(MainApplication.userInfo.getUserId());
            fillUserInfo(MainApplication.userInfo);
        } else {
            mLoginView.setVisibility(View.VISIBLE);
            mInfoDetailsContainer.setVisibility(View.INVISIBLE);
        }
    }

    private void getUserInfo (long userId) {
        UserInfoOperator.getInstance().getUserInfo(userId, new UserInfoOperator.OnGetListener<User>() {
            @Override
            public void onGet(boolean succeed, User user) {
                if (succeed) {
                    mLoginView.setVisibility(View.GONE);
                    mInfoDetailsContainer.setVisibility(View.VISIBLE);
                    fillUserInfo(user);
                } else {
                    mLoginView.setVisibility(View.VISIBLE);
                    mInfoDetailsContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*UserInfoOperator.getInstance().getFavoriteList("100001", new UserInfoOperator.OnGetListener<List<Item>>() {
            @Override
            public void onGet(boolean succeed, List<Item> items) {

            }
        });*/
    }

    private void fillUserInfo (User user) {
        final int profilePix = getResources().getDimensionPixelSize(R.dimen.my_info_profile_size);
        mInfoName.setText(user.getName());
        mAttentionCountTv.setText(getString(R.string.my_info_count, user.getAttCount()));
        mFollowersCountTv.setText(getString(R.string.my_info_count, user.getFollowsCount()));
        ImageLoaderUtils.getImageLoader(getActivity()).displayImage(user.getUserAvatar(),
                mProfileIv,
                ImageLoaderUtils.getRoundDisplayOptions(profilePix));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_info_profile_container:
                startActivity(new Intent(getActivity(), InfoActivity.class));
                break;
            case R.id.my_info_login:
                //showLoginFragment();
                startActivity(new Intent (getActivity(), LoginActivity.class));
                break;
            case R.id.my_info_collections:
                if (MainApplication.isLogin()) {
                    startActivity(new Intent(getActivity(), MyFavoritesActivity.class));
                } else {
                    Toast.makeText(getActivity(), R.string.my_info_toast_not_login, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_info_comments:
                startActivity(new Intent(getActivity(), MyCommentsActivity.class));
                break;
            case R.id.my_info_classes:
                startActivity(new Intent(getActivity(), MyCourseActivity.class));
                break;
            case R.id.my_info_name_card:
                if (MainApplication.isLogin()) {
                    Intent it = new Intent (getActivity(), NameCardQrActivity.class);
                    startActivity(it);
                } else {
                    Toast.makeText(getActivity(), R.string.my_info_toast_not_login, Toast.LENGTH_SHORT).show();
                }
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
