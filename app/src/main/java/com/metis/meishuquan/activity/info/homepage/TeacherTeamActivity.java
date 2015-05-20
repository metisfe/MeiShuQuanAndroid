package com.metis.meishuquan.activity.info.homepage;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.adapter.ImgTitleSubAdapter;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.TeacherInfo;
import com.metis.meishuquan.model.BLL.UserInfoOperator;

import java.util.ArrayList;
import java.util.List;

public class TeacherTeamActivity extends BaseActivity {

    private RecyclerView mRecyclerView = null;
    private GridLayoutManager mGridLayoutManager = null;
    private ImgTitleSubAdapter mAdapter = null;

    private int mStudioId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_team);

        mStudioId = getIntent().getIntExtra(StudioBaseInfo.KEY_STUDIO_ID, 0);

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView = (RecyclerView)findViewById(R.id.teacher_recycler_view);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //TODO
        StudioOperator.getInstance().getTeacherList(mStudioId, new UserInfoOperator.OnGetListener<List<TeacherInfo>>() {
            @Override
            public void onGet(boolean succeed, List<TeacherInfo> teacherInfos) {
                if (succeed) {
                    mAdapter = new ImgTitleSubAdapter(TeacherTeamActivity.this, teacherInfos);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_team);
    }
}
