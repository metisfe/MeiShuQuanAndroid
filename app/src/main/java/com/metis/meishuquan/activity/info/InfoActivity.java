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
import android.text.InputType;
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

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.InputActivity;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.shared.BaseDialog;
import com.metis.meishuquan.view.shared.MyInfoBtn;
import com.metis.meishuquan.view.shared.TitleView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.view.SwitchGroup;

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    private View mParentView = null;
    private View mProfileContainer = null;
    private TitleView mTitleView = null;
    private ImageView mProfile = null;

    private MyInfoBtn mNickView, mGenderView, mConstellationView, mGradeView,
            mAgeView, mCvView, mDepartmentView, mDepartmentAddrView,
            mAchievementView;

    private View mRecentsContainer = null;
    private TextView mRecentsContentTv = null;

    private PopupWindow mPopupWindow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mParentView = findViewById(R.id.info_parent);

        mTitleView = (TitleView)findViewById(R.id.info_title);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProfileContainer = findViewById(R.id.info_profile_container);
        mProfile = (ImageView)findViewById(R.id.info_profile);

        mNickView = (MyInfoBtn)findViewById(R.id.info_nick);
        mGenderView = (MyInfoBtn)findViewById(R.id.info_gender);
        mConstellationView = (MyInfoBtn)findViewById(R.id.info_constellation);
        mGradeView = (MyInfoBtn)findViewById(R.id.info_level);
        mAgeView = (MyInfoBtn)findViewById(R.id.info_age);
        mDepartmentView = (MyInfoBtn)findViewById(R.id.info_department);
        mDepartmentAddrView = (MyInfoBtn)findViewById(R.id.info_department_address);
        mCvView = (MyInfoBtn)findViewById(R.id.info_cv);
        mAchievementView = (MyInfoBtn)findViewById(R.id.info_achievement);

        mRecentsContainer = findViewById(R.id.info_recents_container);
        mRecentsContentTv = (TextView)findViewById(R.id.info_recents_content);

        mProfileContainer.setOnClickListener(this);
        mNickView.setOnClickListener(this);
        mRecentsContainer.setOnClickListener(this);
        mGenderView.setOnClickListener(this);
        mConstellationView.setOnClickListener(this);
        mAgeView.setOnClickListener(this);
        mDepartmentView.setOnClickListener(this);
        mDepartmentAddrView.setOnClickListener(this);
        mCvView.setOnClickListener(this);
        mAchievementView.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        UserInfoOperator.getInstance().getUserInfo("100001", new UserInfoOperator.OnGetListener<User>() {
            @Override
            public void onGet(boolean succeed, User user) {
                if (succeed) {
                    fillUserInfo(user);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {
            case R.id.info_profile_container:
                showPopwindow(v);
                break;
            case R.id.info_nick:
                startInputActivityForResult(getString(R.string.info_modify_nick), mNickView.getSecondaryText(), true, InputActivity.REQUEST_CODE_NICK);
                break;
            case R.id.info_recents_container:
                startInputActivityForResult(getString(R.string.info_recents), mRecentsContentTv.getText(), false, InputActivity.REQUEST_CODE_RECENTS);
                break;
            case R.id.info_gender:
                showDialog();
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
                    mNickView.setSecondaryText(nick);
                    updateInfo("name", nick.toString());
                }

                break;
            case InputActivity.REQUEST_CODE_RECENTS:
                if (resultCode == RESULT_OK) {
                    CharSequence recents = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    mRecentsContentTv.setText(recents);
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
                }
                break;
            case InputActivity.REQUEST_CODE_DEPARTMENT_ADDRESS:
                if (resultCode == RESULT_OK) {
                    CharSequence content = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    mDepartmentAddrView.setSecondaryText(content);
                }
                break;
            case InputActivity.REQUEST_CODE_ACHIEVEMENT:
                if (resultCode == RESULT_OK) {
                    CharSequence content = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    mAchievementView.setSecondaryText(content);
                }
                break;
            case ConstellationActivity.REQUEST_CODE_CONSTELLATION:
                if (resultCode == RESULT_OK) {
                    String constellation = data.getStringExtra(ConstellationActivity.KEY_CONSTELLATION);
                    mConstellationView.setSecondaryText(constellation);
                }
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
        UserInfoOperator.getInstance().updateUserInfo("100001", map);
    }

    private void fillUserInfo (User user) {
        mNickView.setSecondaryText(user.getName());
        mGenderView.setSecondaryText(user.getGender());
        mGradeView.setSecondaryText(user.getGrade());
        final int profileSize = getResources().getDimensionPixelSize(R.dimen.info_profile_size);
        ImageLoaderUtils.getImageLoader(this).displayImage(
                "http://static.228.cn/upload/2015/01/28/AfterTreatment/1422412585979_b3m5-0.jpg",
                mProfile,
                ImageLoaderUtils.getRoundDisplayOptions(profileSize));
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
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    public void showPopwindow (View view) {
        Toast.makeText(this, "showPopwindow", Toast.LENGTH_SHORT).show();
        View contentView = LayoutInflater.from(this).inflate(R.layout.choose_img_source_popupwindows, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              mPopupWindow.dismiss();
              return false;
            }
        });
        /*mPopupWindow.findViewById(R.id.item_popupwindows_Photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo();
            }
        });
        contentView.findViewById(R.id.item_popupwindows_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });*/
        mPopupWindow.showAtLocation(mParentView, Gravity.BOTTOM, 0, 0);
    }

    public void hidePopwindow () {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public boolean isPopwindowShowing () {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    public void photo() {
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
    }

    public void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        startActivityForResult(intent, 333);
    }

}
