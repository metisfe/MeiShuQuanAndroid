package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.InputActivity;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.view.shared.MyInfoBtn;

public class InfoActivity extends BaseActivity implements View.OnClickListener {

    private MyInfoBtn mNickView, mGenderView, mGradeView, mAgeView, mCvView, mDepartmentView;

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

        mNickView = (MyInfoBtn)findViewById(R.id.info_nick);
        mGenderView = (MyInfoBtn)findViewById(R.id.info_gender);
        mGradeView = (MyInfoBtn)findViewById(R.id.info_level);
        mAgeView = (MyInfoBtn)findViewById(R.id.info_age);
        mDepartmentView = (MyInfoBtn)findViewById(R.id.info_department);
        mCvView = (MyInfoBtn)findViewById(R.id.info_cv);

        mRecentsContainer = findViewById(R.id.info_recents_container);
        mRecentsContentTv = (TextView)findViewById(R.id.info_recents_content);

        mNickView.setOnClickListener(this);
        mRecentsContainer.setOnClickListener(this);
        mAgeView.setOnClickListener(this);
        mDepartmentView.setOnClickListener(this);
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
            case R.id.info_age:
                startInputActivityForResult(getString(R.string.info_ages), mAgeView.getSecondaryText(), true, InputActivity.REQUEST_CODE_AGE, InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case R.id.info_department:
                startActivity(new Intent (this, DepartmentActivity.class));
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
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void fillUserInfo (User user) {
        mNickView.setSecondaryText(user.getName());
        mGenderView.setSecondaryText(user.getGender());
        mGradeView.setSecondaryText(user.getGrade());
    }
}
