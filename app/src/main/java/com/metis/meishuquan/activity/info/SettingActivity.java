package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.metis.meishuquan.R;

public class SettingActivity extends FragmentActivity implements View.OnClickListener {

    private View mModifyPwdView, mClearCacheView, mAboutUsView;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_about_us:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

}
