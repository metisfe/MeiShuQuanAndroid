package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.SharedPreferencesUtil;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private View mModifyPwdView, mClearCacheView, mAboutUsView;

    private Button mLogoutBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAboutUsView = this.findViewById(R.id.setting_about_us);
        mAboutUsView.setOnClickListener(this);

        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLogoutBtn = (Button) findViewById(R.id.setting_logout);
        mLogoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_about_us:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.setting_logout:
                //disconnect rong
                if (MainApplication.rongClient != null) {
                    MainApplication.rongClient.disconnect();
                }

                //clear userinfo of MainApplication
                MainApplication.userInfo = new User();

                //clear sharedpreferences
                SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(this);
                spu.delete(SharedPreferencesUtil.USER_LOGIN_INFO);

                //tip
                Toast.makeText(this, "已退出", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
        }
    }

}
