package com.metis.meishuquan.activity.info.homepage;

import android.os.Bundle;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;

public class BookListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_book_publish);
    }
}
