package com.metis.meishuquan.activity.info.homepage;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;

public class TeacherTeamActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_team);
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_team);
    }
}
