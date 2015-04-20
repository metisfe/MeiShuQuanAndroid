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
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.shared.MyInfoBtn;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private MyInfoBtn mModifyPwdView, mClearCacheView, mAboutUsView;

    private Button mLogoutBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAboutUsView = (MyInfoBtn)this.findViewById(R.id.setting_about_us);
        mAboutUsView.setOnClickListener(this);
        mClearCacheView = (MyInfoBtn)this.findViewById(R.id.setting_clear_cache);
        mClearCacheView.setOnClickListener(this);

        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLogoutBtn = (Button) findViewById(R.id.setting_logout);
        mLogoutBtn.setOnClickListener(this);

        int size = ImageLoaderUtils.getImageLoader(this).getDiscCache().getCurrentSize();
        float sizeFloat = size / (1024 * 1024);
        mClearCacheView.setSecondaryText(sizeFloat + "m");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_about_us:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.setting_logout:
                //disconnect rong
                if (MainApplication.rongIM != null) {
                    MainApplication.rongIM.disconnect();
                    MainApplication.rongIM = null;
                    MainApplication.rongClient = null;
                }

                //clear userinfo of MainApplication
                MainApplication.userInfo = new User();

                //clear sharedpreferences
                SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(this);
                spu.delete(SharedPreferencesUtil.USER_LOGIN_INFO);

                //tip
                //Toast.makeText(this, "���˳�", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            case R.id.setting_clear_cache:
                ImageLoaderUtils.getImageLoader(this).getDiscCache().clear();
                int size = ImageLoaderUtils.getImageLoader(this).getDiscCache().getCurrentSize();
                float sizeFloat = size / (1024 * 1024);
                mClearCacheView.setSecondaryText(sizeFloat + "m");
                break;
        }
    }

}
