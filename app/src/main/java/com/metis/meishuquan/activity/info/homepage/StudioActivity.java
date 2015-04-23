package com.metis.meishuquan.activity.info.homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;

public class StudioActivity extends BaseActivity {

    private View mTitleView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);

        mTitleView = LayoutInflater.from(this).inflate(R.layout.layout_studio_title, null);
        getTitleView().setCenterView(mTitleView);

    }

}
