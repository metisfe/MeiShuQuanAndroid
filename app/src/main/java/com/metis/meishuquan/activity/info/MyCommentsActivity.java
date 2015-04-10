package com.metis.meishuquan.activity.info;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.meishuquan.R;

public class MyCommentsActivity extends DataListActivity {

    @Override
    public String getTitleText() {
        return getString(R.string.my_info_comments);
    }

    @Override
    public void loadData(int index) {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }
}
