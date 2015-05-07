package com.metis.meishuquan.activity;

import android.os.Bundle;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.commons.WebFragment;

public class WebActivity extends BaseActivity {

    public static final String
            KEY_URL = "url",
            KEY_TITLE = "title";

    private WebFragment mWebFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mWebFragment = (WebFragment)getSupportFragmentManager().findFragmentById(R.id.web_fragment);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String url = getIntent().getStringExtra(KEY_URL);
        String title = getIntent().getStringExtra(KEY_TITLE);
        setTitleCenter(title);

        mWebFragment.loadUrl(url);
    }
}
