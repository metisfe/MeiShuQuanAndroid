package com.metis.meishuquan.activity.info;

import android.os.Bundle;
import android.widget.TextView;

import com.metis.meishuquan.R;

public class TextActivity extends BaseActivity {

    public static final String KEY_TITLE = "title",
                                KEY_CONTENT = "content";

    private TextView mContentTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        String title = getIntent().getStringExtra(KEY_TITLE);
        setTitleCenter(title);

        mContentTv = (TextView)findViewById(R.id.text_content);
        String content = getIntent().getStringExtra(KEY_CONTENT);
        mContentTv.setText(content);
    }

}
