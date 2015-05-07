package com.metis.meishuquan.activity.info.homepage;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.BLL.VideoInfo;

import java.util.List;

public class VideoListActivity extends BaseActivity {

    private RecyclerView mRecyclerView = null;
    private GridLayoutManager mGridLayoutManager = null;

    private int mStudioId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        mStudioId = getIntent().getIntExtra(StudioBaseInfo.KEY_STUDIO_ID, 0);

        mRecyclerView = (RecyclerView)findViewById(R.id.video_recycler_view);
        mGridLayoutManager = new GridLayoutManager(this, 2);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //TODO
        StudioOperator.getInstance().getVideoList(mStudioId, 0, new UserInfoOperator.OnGetListener<List<VideoInfo>>() {
            @Override
            public void onGet(boolean succeed, List<VideoInfo> videoInfos) {

            }
        });
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_video);
    }
}
