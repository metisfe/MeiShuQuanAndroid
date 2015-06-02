package com.metis.meishuquan.activity.act;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.db.annotation.Id;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.RequestMessageActivity;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.act.StudentCanceledFragment;
import com.metis.meishuquan.fragment.act.StudentJoinedFragment;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.push.PushType;
import com.metis.meishuquan.push.UnReadManager;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.common.delegate.AbsDelegate;
import com.metis.meishuquan.view.common.delegate.AbsViewHolder;
import com.metis.meishuquan.view.common.delegate.DelegateAdapter;
import com.metis.meishuquan.view.common.delegate.DelegateImpl;
import com.metis.meishuquan.view.common.delegate.DelegateType;

import java.util.List;

public class StudentListActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private static final String TAG = StudentListActivity.class.getSimpleName();

    private View mCustomView = null;
    private RadioGroup mRadioGroup = null;

    private RadioButton leftBtn, rightBtn;

    private StudentJoinedFragment mJoinedFragment = new StudentJoinedFragment();
    private StudentCanceledFragment mCanceledFragment = new StudentCanceledFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_act_title, null);
        getTitleView().addCenterView(mCustomView);

        mRadioGroup = (RadioGroup)mCustomView.findViewById(R.id.act_title_group);
        leftBtn = (RadioButton)mCustomView.findViewById(R.id.act_title_details);
        rightBtn = (RadioButton)mCustomView.findViewById(R.id.act_title_list);

        leftBtn.setText(R.string.act_has_joined_title);
        rightBtn.setText(R.string.act_canceled_joined_title);

        mRadioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        leftBtn.setChecked(true);
        showFragment(mJoinedFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UnReadManager.getInstance(this).notifyByTag(PushType.ACTIVITY.getTag(), 0);
    }

    private Fragment mLastFragment = null;
    private void showFragment (Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(R.id.fragment_container, fragment);
        }
        ft.commit();
        if (mLastFragment != null) {
            hideFragment(mLastFragment);
        }
        mLastFragment = fragment;
    }

    private void hideFragment (Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.act_title_details:
                showFragment(mJoinedFragment);
                break;
            case R.id.act_title_list:
                showFragment(mCanceledFragment);
                break;
        }
    }

    public static class StudentAdapter extends DelegateAdapter {

        public StudentAdapter(Context context, List<? extends DelegateImpl> dataList) {
            super(context, dataList);
        }

        @Override
        public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View itemView) {
            DelegateType type = DelegateType.getDelegateTypeByType(viewType);
            switch (type) {
                case DIVIDER_TITLE:
                    return new StudentHeaderViewHolder(itemView);
                case STUDENT:
                    return new StudentViewHolder(itemView);
            }
            return null;
        }
    }

    public static class StudentHeaderViewHolder extends AbsViewHolder<HeaderDelegate> {

        public TextView headerTv = null;

        public StudentHeaderViewHolder(View itemView) {
            super(itemView);
            headerTv = (TextView)itemView.findViewById(R.id.student_item_header);
        }

        @Override
        public void bindData(Context context, List<? extends DelegateImpl> dataList, HeaderDelegate headerDelegate) {
            headerTv.setText(headerDelegate.getSource());
        }
    }

    public static class StudentViewHolder extends AbsViewHolder<StudentDelegate> {

        public ImageView profileIv = null;
        public TextView nameTv = null;
        public TextView phoneTv = null;
        public Button btn = null;

        public StudentViewHolder(View itemView) {
            super(itemView);
            profileIv = (ImageView)itemView.findViewById(R.id.student_item_profile);
            nameTv = (TextView)itemView.findViewById(R.id.student_item_name);
            phoneTv = (TextView)itemView.findViewById(R.id.student_item_phone);
            btn = (Button)itemView.findViewById(R.id.student_item_btn);
        }

        @Override
        public void bindData(final Context context, List<? extends DelegateImpl> dataList, StudentDelegate studentDelegate) {
            final Student student = studentDelegate.getSource();
            ImageLoaderUtils.getImageLoader(context)
                    .displayImage(student.getUserAvatar(), profileIv,
                            ImageLoaderUtils.getNormalDisplayOptions(R.drawable.default_portrait_fang));
            nameTv.setText(student.getUserNickName());
            phoneTv.setText(context.getString(R.string.act_contact, student.getPhoneNum()));
            if (studentDelegate.isFriend) {
                btn.setClickable(false);
                btn.setText(R.string.act_friend_has_joined);
            } else {
                btn.setClickable(true);
                btn.setText(R.string.act_friend_add);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserInfoOperator.getInstance().getUserInfo(student.getUserId(), new UserInfoOperator.OnGetListener<User>() {
                            @Override
                            public void onGet(boolean succeed, User user) {
                                if (succeed) {
                                    Intent it = new Intent(context, RequestMessageActivity.class);
                                    it.putExtra(RequestMessageActivity.KEY_TATGETID, student.getUserId());
                                    context.startActivity(it);
                                }
                            }
                        });
                    }
                });
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityUtils.startNameCardActivity(context, student.userId);
                }
            });
        }
    }

    public static class HeaderDelegate extends AbsDelegate<String> {

        public HeaderDelegate(String s) {
            super(s);
        }

        @Override
        public DelegateType getDelegateType() {
            return DelegateType.DIVIDER_TITLE;
        }
    }

    public static class StudentDelegate extends AbsDelegate<Student> {

        public boolean isNewOne = false;
        public boolean isFriend = false;

        public StudentDelegate(Student student) {
            super(student);
        }

        @Override
        public DelegateType getDelegateType() {
            return DelegateType.STUDENT;
        }
    }

    public static class Student {
        @Id
        public long userId;
        public String userNickName;
        public String userAvatar;
        public int upCount;
        public String createDatetime;
        public String phoneNum;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public int getUpCount() {
            return upCount;
        }

        public void setUpCount(int upCount) {
            this.upCount = upCount;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }
    }

    public static boolean hasHeader (List<? extends AbsDelegate> delegates) {
        for (AbsDelegate delegate : delegates) {
            if (delegate.getDelegateType() == DelegateType.DIVIDER_TITLE) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFriend (Context context, long userId) {
        String content = SharedPreferencesUtil.getInstanse(context).getStringByKey(SharedPreferencesUtil.CONTACTS + MainApplication.userInfo.getUserId());
        if (content != null) {
            return content.indexOf(userId + "") >= 0;
        }
        return false;
    }

}
