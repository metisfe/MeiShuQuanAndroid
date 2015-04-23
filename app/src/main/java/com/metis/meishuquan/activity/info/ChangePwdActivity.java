package com.metis.meishuquan.activity.info;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.Option;
import com.metis.meishuquan.view.shared.TitleView;

public class ChangePwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText mOldEt, mNewEt, mConfirmEt;
    private Button mOkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        mOldEt = (EditText)findViewById(R.id.change_pwd_old);
        mNewEt = (EditText)findViewById(R.id.change_pwd_new);
        mConfirmEt = (EditText)findViewById(R.id.change_pwd_confirm);
        mOkBtn = (Button)findViewById(R.id.change_pwd_ok);

        mOkBtn.setOnClickListener(this);
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.setting_modify_pwd);
    }

    @Override
    public void onClick(View v) {
        String oldPwd = mOldEt.getText().toString();
        String newPwd = mNewEt.getText().toString();
        String confirmPwd = mConfirmEt.getText().toString();
        if (!newPwd.equals(confirmPwd)) {
            Toast.makeText(this, R.string.change_pwd_not_match, Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPwd.equals(oldPwd)) {
            Toast.makeText(this, R.string.change_pwd_not_new, Toast.LENGTH_SHORT).show();
            return;
        }
        UserInfoOperator.getInstance().changePwd(oldPwd, newPwd, new UserInfoOperator.OnGetListener<Option>() {
            @Override
            public void onGet(boolean succeed, Option o) {
                if (o.getStatus() == 0) {
                    Toast.makeText(ChangePwdActivity.this, R.string.change_pwd_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePwdActivity.this, R.string.change_pwd_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
