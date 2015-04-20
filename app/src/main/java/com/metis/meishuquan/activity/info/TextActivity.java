package com.metis.meishuquan.activity.info;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.TitleView;

public class TextActivity extends BaseActivity {

    public static final String KEY_TITLE = "title",
                                KEY_CONTENT = "content";

    private TitleView mTitleView = null;
    private TextView mContentTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mTitleView = (TitleView)findViewById(R.id.title);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String title = getIntent().getStringExtra(KEY_TITLE);
        mTitleView.setTitleText(title);

        mContentTv = (TextView)findViewById(R.id.text_content);
        String content = getIntent().getStringExtra(KEY_CONTENT);
        mContentTv.setText(content);
    }

}
