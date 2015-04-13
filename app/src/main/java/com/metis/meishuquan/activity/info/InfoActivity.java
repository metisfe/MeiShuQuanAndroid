package com.metis.meishuquan.activity.info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mProfile = null;

    private MyInfoBtn mNickView, mGenderView, mConstellationView, mGradeView, mAgeView, mCvView, mDepartmentView, mDepartmentAddrView;

    private View mRecentsContainer = null;
    private TextView mRecentsContentTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        /*UserInfoOperator.getInstance().getUserInfo("0", new ApiOperationCallback<ReturnInfo<String>> () {

            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                Log.v()
            }
        });*/

        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProfile = (ImageView)findViewById(R.id.info_profile);

        mNickView = (MyInfoBtn)findViewById(R.id.info_nick);
        mGenderView = (MyInfoBtn)findViewById(R.id.info_gender);
        mConstellationView = (MyInfoBtn)findViewById(R.id.info_constellation);
        mGradeView = (MyInfoBtn)findViewById(R.id.info_level);
        mAgeView = (MyInfoBtn)findViewById(R.id.info_age);
        mDepartmentView = (MyInfoBtn)findViewById(R.id.info_department);
        mDepartmentAddrView = (MyInfoBtn)findViewById(R.id.info_department_address);
        mCvView = (MyInfoBtn)findViewById(R.id.info_cv);

        mRecentsContainer = findViewById(R.id.info_recents_container);
        mRecentsContentTv = (TextView)findViewById(R.id.info_recents_content);

        mNickView.setOnClickListener(this);
        mRecentsContainer.setOnClickListener(this);
        mGenderView.setOnClickListener(this);
        mConstellationView.setOnClickListener(this);
        mAgeView.setOnClickListener(this);
        mDepartmentView.setOnClickListener(this);
        mDepartmentAddrView.setOnClickListener(this);
        mCvView.setOnClickListener(this);
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case InputActivity.REQUEST_CODE_NICK:
                if (resultCode == RESULT_OK) {
                    CharSequence nick = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    mNickView.setSecondaryText(nick);
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
            case ConstellationActivity.REQUEST_CODE_CONSTELLATION:
                if (resultCode == RESULT_OK) {
                    String constellation = data.getStringExtra(ConstellationActivity.KEY_CONSTELLATION);
                    mConstellationView.setSecondaryText(constellation);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

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
}
