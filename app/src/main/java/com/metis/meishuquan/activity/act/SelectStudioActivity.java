package com.metis.meishuquan.activity.act;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.act.StudioListFragment;

public class SelectStudioActivity extends BaseActivity {

    private StudioListFragment mStudioListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_studio);

        mStudioListFragment = (StudioListFragment)getSupportFragmentManager().findFragmentById(R.id.select_studio_fragment);
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.act_select_studio);
    }
}
