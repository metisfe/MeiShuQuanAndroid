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
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;

import java.text.DecimalFormat;

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

        mClearCacheView.setSecondaryText(formatSize(b2m(getCacheSize())));
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
                final int sizeBefore = getCacheSize();
                clear();
                mClearCacheView.setSecondaryText(formatSize(b2m(getCacheSize())));
                Toast.makeText(this, getString(R.string.setting_cleared, formatSize(b2m(sizeBefore - getCacheSize()))), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private int getCacheSize () {
        DiscCacheAware cache = ImageLoaderUtils.getImageLoader(this).getDiscCache();
        return cache.getCurrentSize();
    }

    private float b2m (int size) {
        return (float)size / (1024 * 1024);
    }

    private int clear () {
        DiscCacheAware cache = ImageLoaderUtils.getImageLoader(this).getDiscCache();
        cache.clear();
        return cache.getCurrentSize();
    }

    private String formatSize (float size) {
        float floatSize = size * 100;
        int left = (int)floatSize / 100;
        int right = (int)floatSize % 100;
        return left + "." + right + "m";
    }

}
