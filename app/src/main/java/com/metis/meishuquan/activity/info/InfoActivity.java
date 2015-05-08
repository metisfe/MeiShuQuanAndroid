package com.metis.meishuquan.activity.info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.InputActivity;
import com.metis.meishuquan.activity.course.ChooseCourseActivity;
import com.metis.meishuquan.fragment.assess.ChooseCityFragment;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.assess.City;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.course.CourseChannel;
import com.metis.meishuquan.model.course.CourseChannelItem;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.PatternUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.shared.MyInfoBtn;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = InfoActivity.class.getSimpleName();

    public static final int REQUEST_CODE_CHOOSE_COURSE = 500;

    private View mParentView = null;
    private View mProfileContainer = null;
    private ImageView mProfile = null;

    private MyInfoBtn mNickView, mMeishuquanIdView, mGenderView, mConstellationView, mGradeView, mProvienceView,
            mAgeView, mCvView, mDepartmentView, mDepartmentAddrView, mGoodAtView,
            mAchievementView;

    private View mRecentsContainer = null;
    private TextView mRecentsContentTv = null;

    private PopupWindow mPopupWindow = null;

    private String mCameraOutputPath = null;

    private List<CourseChannelItem> mCourseItems = null;

    private boolean isStudent = false;
    private boolean isTeacher = false;

    private boolean canEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mParentView = findViewById(R.id.info_parent);

        mProfileContainer = findViewById(R.id.info_profile_container);
        mProfile = (ImageView)findViewById(R.id.info_profile);

        mNickView = (MyInfoBtn)findViewById(R.id.info_nick);
        mMeishuquanIdView = (MyInfoBtn)findViewById(R.id.info_meishuquan_id);
        mGenderView = (MyInfoBtn)findViewById(R.id.info_gender);
        mConstellationView = (MyInfoBtn)findViewById(R.id.info_constellation);
        mGradeView = (MyInfoBtn)findViewById(R.id.info_level);
        mProvienceView = (MyInfoBtn)findViewById(R.id.info_provience);
        mAgeView = (MyInfoBtn)findViewById(R.id.info_age);
        mDepartmentView = (MyInfoBtn)findViewById(R.id.info_department);
        mDepartmentAddrView = (MyInfoBtn)findViewById(R.id.info_department_address);
        mGoodAtView = (MyInfoBtn)findViewById(R.id.info_good_at);
        mCvView = (MyInfoBtn)findViewById(R.id.info_cv);
        mAchievementView = (MyInfoBtn)findViewById(R.id.info_achievement);

        mRecentsContainer = findViewById(R.id.info_recents_container);
        mRecentsContentTv = (TextView)findViewById(R.id.info_recents_content);

        mProfileContainer.setOnClickListener(this);
        mNickView.setOnClickListener(this);
        mMeishuquanIdView.setOnClickListener(this);
        mRecentsContainer.setOnClickListener(this);
        mGenderView.setOnClickListener(this);
        mProvienceView.setOnClickListener(this);
        mConstellationView.setOnClickListener(this);
        mAgeView.setOnClickListener(this);
        mDepartmentView.setOnClickListener(this);
        mDepartmentAddrView.setOnClickListener(this);
        mGoodAtView.setOnClickListener(this);
        mCvView.setOnClickListener(this);
        mAchievementView.setOnClickListener(this);

        if (isStudent) {
            mDepartmentView.setText(R.string.info_school);
            mDepartmentAddrView.setText(R.string.info_school_address);
            mGoodAtView.setVisibility(View.GONE);
            mCvView.setVisibility(View.GONE);
            mAchievementView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        UserInfoOperator.getInstance().getUserInfo(MainApplication.userInfo.getUserId(), new UserInfoOperator.OnGetListener<User>() {
            @Override
            public void onGet(boolean succeed, User user) {
                canEdit = succeed;
                if (succeed) {
                    fillUserInfo(user);
                }
            }
        });
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.info_title);
    }

    @Override
    public void onClick(View v) {
        if (!canEdit) {
            return;
        }
        Intent it = null;
        switch (v.getId()) {
            case R.id.info_profile_container:
                showPopwindow(v);
                break;
            case R.id.info_nick:
                startInputActivityForResult(getString(R.string.info_modify_nick), mNickView.getSecondaryText(), true, InputActivity.REQUEST_CODE_NICK);
                break;
            case R.id.info_meishuquan_id:
                if (TextUtils.isEmpty(mMeishuquanIdView.getSecondaryText().toString())) {
                    startInputActivityForResult(mMeishuquanIdView.getText().toString(), mMeishuquanIdView.getSecondaryText(), true, InputActivity.REQUEST_CODE_MEISHUQUAN_ID);
                }
                break;
            case R.id.info_recents_container:
                startInputActivityForResult(getString(R.string.info_recents), mRecentsContentTv.getText(), false, InputActivity.REQUEST_CODE_RECENTS);
                break;
            case R.id.info_gender:
                showDialog();
                break;
            case R.id.info_provience:
                showCityFragment();
                break;
            case R.id.info_constellation:
                it = new Intent(this, ConstellationActivity.class);
                it.putExtra(ConstellationActivity.KEY_CONSTELLATION, mConstellationView.getSecondaryText().toString());
                Log.v("TESTS", "info_constellation");
                startActivityForResult(it, ConstellationActivity.REQUEST_CODE_CONSTELLATION);
                break;
            case R.id.info_age:
                startInputActivityForResult(getString(R.string.info_ages), mAgeView.getSecondaryText(), true, InputActivity.REQUEST_CODE_AGE, InputType.TYPE_CLASS_NUMBER);
                break;
            case R.id.info_department:
                startActivity(new Intent (this, DepartmentActivity.class));
                break;
            case R.id.info_department_address:
                startInputActivityForResult(getString(R.string.info_department_address), mDepartmentAddrView.getSecondaryText(), false, InputActivity.REQUEST_CODE_DEPARTMENT_ADDRESS);
                break;
            case R.id.info_good_at:
                it = new Intent (this, ChooseCourseActivity.class);
                it.putExtra(ChooseCourseActivity.OLDSELECTEDCHANNELITEMS, (Serializable)mCourseItems);
                startActivityForResult(it, REQUEST_CODE_CHOOSE_COURSE);
                break;
            case R.id.info_cv:
                startInputActivityForResult(mCvView.getText().toString(), mCvView.getSecondaryText(), false, InputActivity.REQUEST_CODE_CV);
                break;
            case R.id.info_achievement:
                startInputActivityForResult(mAchievementView.getText().toString(), mAchievementView.getSecondaryText(), false, InputActivity.REQUEST_CODE_ACHIEVEMENT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case InputActivity.REQUEST_CODE_NICK:
                if (resultCode == RESULT_OK) {
                    CharSequence nick = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    if (!PatternUtils.PATTERN_NICK_NAME.matcher(nick.toString()).matches()) {
                        Toast.makeText(this, R.string.info_nick_name_illegal, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mNickView.setSecondaryText(nick);
                    updateInfo(User.KEY_NICK_NAME, nick.toString());
                }
                break;
            case InputActivity.REQUEST_CODE_MEISHUQUAN_ID:
                if (resultCode == RESULT_OK) {
                    CharSequence id = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    //Pattern pattern = new Pattern("");
                    if (!PatternUtils.PATTERN_MEISHUQUAN_ID.matcher(id.toString()).matches()) {
                        Toast.makeText(this, R.string.info_meishuquan_id_illegal, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mMeishuquanIdView.setSecondaryText(id);
                    updateInfo(User.KEY_ACCOUNT, id + "");
                }
                break;
            case InputActivity.REQUEST_CODE_RECENTS:
                if (resultCode == RESULT_OK) {
                    CharSequence recents = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    mRecentsContentTv.setText(recents);
                    updateInfo(User.KEY_SELFSIGNATURE, recents.toString());
                }
                break;
            case InputActivity.REQUEST_CODE_AGE:
                if (resultCode == RESULT_OK) {
                    CharSequence content = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    mAgeView.setSecondaryText(content);
                    updateInfo("age", content.toString());
                }
                break;
            case InputActivity.REQUEST_CODE_CV:
                if (resultCode == RESULT_OK) {
                    CharSequence content = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    mCvView.setSecondaryText(content);
                    updateInfo(User.KEY_USER_RESUME, content.toString());
                }
                break;
            case InputActivity.REQUEST_CODE_DEPARTMENT_ADDRESS:
                if (resultCode == RESULT_OK) {
                    CharSequence content = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    mDepartmentAddrView.setSecondaryText(content);
                    updateInfo(User.KEY_LOCATIONADDRESS, content.toString());
                }
                break;
            case REQUEST_CODE_CHOOSE_COURSE:
                if (resultCode == RESULT_OK) {
                    List<CourseChannelItem> list = (List<CourseChannelItem>)data.getExtras().getSerializable("tags");
                    /*JsonArray array = new JsonArray();
                    for (CourseChannelItem item : list) {
                        JsonObject object = new JsonObject();
                        object.addProperty(CourseChannelItem.KEY_CHANNEL_ID, item.getChannelId());
                        object.addProperty(CourseChannelItem.KEY_CHANNEL_NAME, item.getChannelName());
                        array.add(object);
                    }
                    String json = array.toString().replaceAll("\\{", "\\(");
                    json = json.replaceAll("\\}", "\\)");*/
                    StringBuilder builder = new StringBuilder();
                    StringBuilder nameBuilder = new StringBuilder();
                    for (CourseChannelItem item : list) {
                        builder.append(item.getChannelId() + ",");
                        nameBuilder.append(item.getChannelName() + " ");
                    }
                    mGoodAtView.setSecondaryText(nameBuilder.toString());
                    updateInfo(User.KEY_GOODSUBJECTS, builder.toString());
                    Log.v(TAG, "REQUEST_CODE_CHOOSE_COURSE " + builder);
                }
                break;
            case InputActivity.REQUEST_CODE_ACHIEVEMENT:
                if (resultCode == RESULT_OK) {
                    CharSequence content = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    mAchievementView.setSecondaryText(content);
                    updateInfo(User.KEY_ACHIEVEMENT, content.toString());
                }
                break;
            case ConstellationActivity.REQUEST_CODE_CONSTELLATION:
                if (resultCode == RESULT_OK) {
                    String constellation = data.getStringExtra(ConstellationActivity.KEY_CONSTELLATION);
                    mConstellationView.setSecondaryText(constellation);
                    updateInfo(User.KEY_HOROSCOPE, constellation.toString());
                }
                break;
            case 222:
                if (resultCode == RESULT_OK) {
                    final int size = getResources().getDimensionPixelSize(R.dimen.info_profile_size);
                    ImageLoaderUtils.getImageLoader(this).displayImage(ImageDownloader.Scheme.FILE.wrap(mCameraOutputPath), mProfile, ImageLoaderUtils.getRoundDisplayOptions(size));
                    UserInfoOperator.getInstance().updateUserProfile(MainApplication.userInfo.getUserId(), mCameraOutputPath);
                }
                hidePopwindow();
                break;
            case 333:
                if (resultCode == RESULT_OK) {
                    final String path = ImageLoaderUtils.getFilePathFromUri(this, data.getData());
                    final int profileSize = getResources().getDimensionPixelSize(R.dimen.info_profile_size);
                    ImageLoaderUtils.getImageLoader(this).displayImage(ImageDownloader.Scheme.FILE.wrap(path), mProfile, ImageLoaderUtils.getRoundDisplayOptions(profileSize));
                    UserInfoOperator.getInstance().updateUserProfile(MainApplication.userInfo.getUserId(), path);
                    //UserInfoOperator.getInstance().updateUserProfileByUrl(MainApplication.userInfo.getUserId(), "http://ww1.sinaimg.cn/bmiddle/6cd6d028jw1er7i1933eaj20go0cnjs7.jpg");

                    File file = new File(path);
                    Log.v(TAG, "onActivityResult 333 " + file.getAbsolutePath());
                }
                hidePopwindow();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        if (isPopwindowShowing()) {
            hidePopwindow();
            return;
        }
        super.onBackPressed();
    }

    public void updateInfo (String key, String value) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        UserInfoOperator.getInstance().updateUserInfo(MainApplication.userInfo.getUserId(), map);
    }

    private void fillUserInfo (User user) {

        mNickView.setSecondaryText(user.getName());
        mMeishuquanIdView.setSecondaryText(user.getAccout());
        if (!TextUtils.isEmpty(user.getAccout())) {
            mMeishuquanIdView.setArrowVisible(View.INVISIBLE);
            mMeishuquanIdView.setClickable(false);
        }
        mGenderView.setSecondaryText(user.getGender());
        mGradeView.setSecondaryText(user.getGrade());
        final int profileSize = getResources().getDimensionPixelSize(R.dimen.info_profile_size);
        ImageLoaderUtils.getImageLoader(this).displayImage(
                user.getUserAvatar(),
                mProfile,
                ImageLoaderUtils.getRoundDisplayOptions(profileSize));
        mCvView.setSecondaryText(user.getSelfIntroduce());
        mConstellationView.setSecondaryText(user.getHoroscope());
        mRecentsContentTv.setText(user.getSelfSignature());
        mDepartmentAddrView.setSecondaryText(user.getLocationAddress());
        mAchievementView.setSecondaryText(user.getAchievement());
        Log.v(TAG, "fillUserInfo userRole=" + user.getUserRole());
        /*if (user.getUserRole()) {

        }*/
        String birthday = user.getBirthday();
        if (TextUtils.isEmpty(birthday)) {
            mAgeView.setSecondaryText(0 + "");
        }
        String subjectsId = user.getGoodSubjects();
        if (!TextUtils.isEmpty(subjectsId)) {
            String allCourse = SharedPreferencesUtil.getInstanse(this).getStringByKey(SharedPreferencesUtil.COURSE_CHANNEL_LIST);
            Log.v(TAG, "fillUserInfo allCourse=" + allCourse);
            if (TextUtils.isEmpty(allCourse)) {
                return;
            }
            mCourseItems = new ArrayList<CourseChannelItem>();
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
        /*List<CourseChannelItem> list = user.getGoodSubjectsList();
        StringBuilder sb = new StringBuilder();
        for (CourseChannelItem item : list) {
            sb.append(item.getChannelName() + " ");
        }*/
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

    private Dialog mDialog = null;
    public void showDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.info_gender);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_gender_chooser, null);
        builder.setView(contentView);
        builder.setPositiveButton(R.string.gender_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();
            }
        });
        RadioGroup group = (RadioGroup)contentView.findViewById(R.id.gender_group);
        RadioButton female = (RadioButton)contentView.findViewById(R.id.gender_famale);
        RadioButton male = (RadioButton)contentView.findViewById(R.id.gender_male);
        final String femaleStr = getString(R.string.gender_famale);
        final String maleStr = getString(R.string.gender_male);
        if (mGenderView.getSecondaryText().toString().equals(femaleStr)) {
            female.setChecked(true);
        } else {
            male.setChecked(true);
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gender_famale) {
                    mGenderView.setSecondaryText(femaleStr);
                } else {
                    mGenderView.setSecondaryText(maleStr);
                }
                updateInfo(User.KEY_GENDER, mGenderView.getSecondaryText().toString());
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    public void showPopwindow (View view) {
        Toast.makeText(this, "showPopwindow", Toast.LENGTH_SHORT).show();
        final View contentView = LayoutInflater.from(this).inflate(R.layout.choose_img_source_popupwindows, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);

        final View photoView = contentView.findViewById(R.id.item_popupwindows_camera);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraOutputPath = photo();
            }
        });
        contentView.findViewById(R.id.item_popupwindows_Photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
        contentView.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePopwindow();
            }
        });
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              //mPopupWindow.dismiss();
              return false;
            }
        });


        mPopupWindow.showAtLocation(mParentView, Gravity.BOTTOM, 0, 0);
    }

    public void hidePopwindow () {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public boolean isPopwindowShowing () {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    public String photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Environment.getExternalStorageDirectory()
                + "/myimage/");
        if (!dir.exists()) {
            dir.mkdir();
        } else {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
        File file = new File(dir, String.valueOf(System.currentTimeMillis())
                + ".jpg");
        //path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, 222);
        return file.getAbsolutePath();
    }

    public void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        startActivityForResult(intent, 333);
    }
    private ChooseCityFragment mCityFragment = null;
    public void showCityFragment () {
        if (mCityFragment == null) {
            mCityFragment = new ChooseCityFragment();
            mCityFragment.setOnCityChooseListener(mCityListener);
            Bundle bundle = new Bundle();
            bundle.putBoolean(ChooseCityFragment.KEY_SHOW_TITLE, false);
            mCityFragment.setArguments(bundle);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, mCityFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void hideCityFragment () {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(mCityFragment);
        manager.popBackStack();
        ft.commit();
    }

    private ChooseCityFragment.OnCityChooseListener mCityListener = new ChooseCityFragment.OnCityChooseListener() {
        @Override
        public void onChoose(City city) {
            mProvienceView.setSecondaryText(city.getGroupName() + "-" + city.getCityName());
            updateInfo(User.KEY_REGION, "city.getGroupName() + \"-\" + city.getCityName()");
            hideCityFragment();
        }
    };

}
