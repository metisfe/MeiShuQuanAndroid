package com.metis.meishuquan.adapter.studio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.view.shared.MyInfoBtn;

/**
 * Created by WJ on 2015/5/8.
 */
public class UserInfoAdapter extends BaseAdapter {

    private Context mContext = null;
    private User mUser = null;
    private boolean canEdit = false;

    public UserInfoAdapter (Context context, User user) {
        this (context, user, false);
    }

    public UserInfoAdapter (Context context, User user, boolean canEdit) {
        mUser = user;
        this.canEdit = canEdit;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public User getItem(int i) {
        return mUser;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_user_info, null);

        View mProfileContainer = view.findViewById(R.id.info_profile_container);
        ImageView mProfile = (ImageView)view.findViewById(R.id.info_profile);

        MyInfoBtn mNickView = (MyInfoBtn)view.findViewById(R.id.info_nick);
        MyInfoBtn mMeishuquanIdView = (MyInfoBtn)view.findViewById(R.id.info_meishuquan_id);
        MyInfoBtn mGenderView = (MyInfoBtn)view.findViewById(R.id.info_gender);
        MyInfoBtn mConstellationView = (MyInfoBtn)view.findViewById(R.id.info_constellation);
        MyInfoBtn mGradeView = (MyInfoBtn)view.findViewById(R.id.info_level);
        MyInfoBtn mProvienceView = (MyInfoBtn)view.findViewById(R.id.info_provience);
        MyInfoBtn mAgeView = (MyInfoBtn)view.findViewById(R.id.info_age);
        MyInfoBtn mCvView = (MyInfoBtn)view.findViewById(R.id.info_cv);
        MyInfoBtn mDepartmentView = (MyInfoBtn)view.findViewById(R.id.info_department);
        MyInfoBtn mDepartmentAddrView = (MyInfoBtn)view.findViewById(R.id.info_department_address);
        MyInfoBtn mGoodAtView = (MyInfoBtn)view.findViewById(R.id.info_good_at);
        MyInfoBtn mAchievementView = (MyInfoBtn)view.findViewById(R.id.info_achievement);

        View mRecentsContainer = view.findViewById(R.id.info_recents_container);
        TextView mRecentsContentTv = (TextView)view.findViewById(R.id.info_recents_content);

        return view;
    }
}
