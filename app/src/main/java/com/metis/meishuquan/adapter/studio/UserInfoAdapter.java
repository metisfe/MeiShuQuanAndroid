package com.metis.meishuquan.adapter.studio;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.manager.common.UserManager;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.course.CourseChannel;
import com.metis.meishuquan.model.course.CourseChannelItem;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.shared.MyInfoBtn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/8.
 */
public class UserInfoAdapter extends BaseAdapter implements View.OnClickListener{

    private static final String TAG = UserInfoAdapter.class.getSimpleName();

    private Context mContext = null;
    private List<User> mUserList = null;
    private boolean canEdit = false;

    private View.OnClickListener mOnClickListener = null;

    private String locationSchool = null;
    private String mGender = null;

    public UserInfoAdapter (Context context, User user) {
        this (context, user, false);
    }

    public UserInfoAdapter (Context context, User user, boolean canEdit) {
        mContext = context;
        mUserList = new ArrayList<User>();
        mUserList.add(user);
        this.canEdit = canEdit;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public User getItem(int i) {
        return mUserList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final User mUser = getItem(i);
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
        MyInfoBtn mSchoolView = (MyInfoBtn)view.findViewById(R.id.info_school);
        MyInfoBtn mStudiolView = (MyInfoBtn)view.findViewById(R.id.info_studio);
        MyInfoBtn mGoodAtView = (MyInfoBtn)view.findViewById(R.id.info_good_at);
        MyInfoBtn mAchievementView = (MyInfoBtn)view.findViewById(R.id.info_achievement);
        MyInfoBtn mPhoneView = (MyInfoBtn)view.findViewById(R.id.info_modify_phone);
        MyInfoBtn mRegisterView = (MyInfoBtn)view.findViewById(R.id.info_register_time);

        View mRecentsContainer = view.findViewById(R.id.info_recents_container);
        TextView mRecentsContentTv = (TextView)view.findViewById(R.id.info_recents_content);

        ImageLoaderUtils.getImageLoader(mContext).displayImage(
                mUser.getUserAvatar(), mProfile,
                ImageLoaderUtils.getRoundDisplayOptions(mContext.getResources().getDimensionPixelSize(R.dimen.info_profile_size))
        );

        mProfileContainer.findViewById(R.id.my_info_btn_arrow).setVisibility(canEdit ? View.VISIBLE : View.INVISIBLE);

        mNickView.setSecondaryText(mUser.getName());
        mNickView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);

        mMeishuquanIdView.setSecondaryText(mUser.getAccout());
        mMeishuquanIdView.setArrowVisible(canEdit && TextUtils.isEmpty(mUser.getAccout()) ? View.VISIBLE : View.INVISIBLE);

        mGenderView.setSecondaryText(mGender == null ? mUser.getGender() : mGender);
        mGenderView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);

        mConstellationView.setSecondaryText(mUser.getHoroscope());
        mConstellationView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);
        mGradeView.setSecondaryText(mContext.getString(mUser.getUserRoleEnum().getStringResource()));

        mProvienceView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);
        mProvienceView.setSecondaryText(UserInfoOperator.getInstance().getProvinceName(mUser.getRegion()));

        mAgeView.setSecondaryText(UserManager.caculateAgeByBirthday(mUser.getBirthday()) + "");
        mAgeView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);

        mCvView.setVisibility(mUser.getUserRoleEnum() == IdTypeEnum.TEACHER ? View.VISIBLE : View.GONE);
        mCvView.setSecondaryText(mUser.getUserResume());
        mCvView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);

        mSchoolView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);
        mSchoolView.setVisibility(mUser.getUserRoleEnum() == IdTypeEnum.STUDENT ? View.VISIBLE : View.GONE);
        mSchoolView.setSecondaryText(locationSchool != null ? locationSchool : mUser.getLocationSchool());

        mStudiolView.setText(mUser.getUserRoleEnum() == IdTypeEnum.STUDENT ? R.string.info_studio : R.string.info_department);
        mStudiolView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);
        if (mStudioName != null) {
            mStudiolView.setSecondaryText(mStudioName);
        } else {
            if (mUser.getStudio() != null) {
                mStudiolView.setSecondaryText(mUser.getStudio().getName());
                //TODO
            }

        }
        mDepartmentView.setVisibility(/*mUser.getUserRoleEnum() == IdTypeEnum.TEACHER ? View.VISIBLE : */View.GONE);
        mDepartmentView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);
        mDepartmentAddrView.setVisibility(mUser.getUserRoleEnum() == IdTypeEnum.TEACHER ? View.VISIBLE : View.GONE);
        if (mUser.getStudio() != null) {
            mDepartmentAddrView.setSecondaryText(/*mUser.getLocationAddress()*/mUser.getStudio().getAddress());
        }

        mDepartmentAddrView.setArrowVisible(View.INVISIBLE);

        //mGoodAtView.setVisibility(mUser.getUserRoleEnum() == IdTypeEnum.TEACHER ? View.VISIBLE : View.GONE);
        if (mGoodAtSubjects != null) {
            mGoodAtView.setSecondaryText(mGoodAtSubjects);
        }
        String subjectsId = mUser.getGoodSubjects();
        if (!TextUtils.isEmpty(subjectsId)) {
            String allCourse = SharedPreferencesUtil.getInstanse(mContext).getStringByKey(SharedPreferencesUtil.COURSE_CHANNEL_LIST);
            Log.v(TAG, "fillUserInfo allCourse=" + allCourse);
            if (!TextUtils.isEmpty(allCourse)) {
                List<CourseChannelItem> mCourseItems = new ArrayList<CourseChannelItem>();
                Gson gson = new Gson();
                Result<List<CourseChannel>> courseChannel = gson.fromJson(allCourse, new TypeToken<Result<List<CourseChannel>>>(){}.getType());
                List<CourseChannel> channelList = courseChannel.getData();
                String[] ids = subjectsId.split(",");
                StringBuilder builder = new StringBuilder();
                for (String id : ids) {
                    if (TextUtils.isEmpty(id)) {
                        continue;
                    }
                    Log.v(TAG, "fillUserInfo id=" + id);
                    Integer idInt = -1;
                    try{
                        idInt = Integer.valueOf(id);
                    } catch (Exception e) {
                        continue;
                    }
                    CourseChannelItem it = getCourseChannelItem(channelList, idInt);
                    if (it != null) {
                        Log.v(TAG, "fillUserInfo it=" + it.getChannelName());
                        mCourseItems.add(it);
                        builder.append(it.getChannelName() + " ");
                    }
                }
                mGoodAtView.setSecondaryText(builder.toString());
            }

        }
        mAchievementView.setVisibility(mUser.getUserRoleEnum() == IdTypeEnum.TEACHER ? View.VISIBLE : View.GONE);
        mAchievementView.setSecondaryText(mUser.getAchievement());
        mAchievementView.setArrowVisible(canEdit ? View.VISIBLE : View.INVISIBLE);

        mRecentsContentTv.setText(mUser.getSelfSignature());
        mRecentsContainer.findViewById(R.id.info_btn_arrow).setVisibility(canEdit ? View.VISIBLE : View.INVISIBLE);

        mPhoneView.setVisibility(MainApplication.userInfo != null && mUser.getUserId() == MainApplication.userInfo.getUserId() ? View.VISIBLE : View.GONE);

        mRegisterView.setArrowVisible(View.INVISIBLE);
        mRegisterView.setSecondaryText(mUser.getRegisterTimeFormated());

        mProfileContainer.setOnClickListener(this);
        mNickView.setOnClickListener(this);
        mMeishuquanIdView.setOnClickListener(this);
        mRecentsContainer.setOnClickListener(this);
        mGenderView.setOnClickListener(this);
        mProvienceView.setOnClickListener(this);
        mConstellationView.setOnClickListener(this);
        mAgeView.setOnClickListener(this);
        mSchoolView.setOnClickListener(this);
        mStudiolView.setOnClickListener(this);
        mDepartmentView.setOnClickListener(this);
        mDepartmentAddrView.setOnClickListener(this);
        mGoodAtView.setOnClickListener(this);
        mCvView.setOnClickListener(this);
        mAchievementView.setOnClickListener(this);

        return view;
    }

    private String mStudioName = null;
    public void setStudioName (String studio) {
        mStudioName = studio;
    }

    private String mGoodAtSubjects = null;
    public void setGoodAtSubjects (String goodAtSubjects) {
        mGoodAtSubjects = goodAtSubjects;
    }

    private CourseChannelItem getCourseChannelItem (List<CourseChannel> channelsList, int channelId) {
        for (CourseChannel channel : channelsList) {
            CourseChannelItem first = channel.getFirstItem();
            CourseChannelItem last = channel.getLastItem();
            if (first != null && last != null && channelId >= first.getChannelId() && channelId <= last.getChannelId()) {
                for (CourseChannelItem item : channel.getChildChannelLists()) {
                    if (item.getChannelId() == channelId) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    public void setLocationSchool (String schoolNames) {
        locationSchool = schoolNames;
    }

    public void setGender (String gender) {
        mGender = gender;
    }

    @Override
    public void onClick(View v) {
        if (!canEdit) {
            return;
        }
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }

    public void setOnClickListener (View.OnClickListener listener) {
        mOnClickListener = listener;
    }
}
