package com.metis.meishuquan.activity.info.homepage;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.adapter.ImgTitleSubAdapter;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.BLL.VideoInfo;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends BaseActivity {

    private RecyclerView mRecyclerView = null;
    private GridLayoutManager mGridLayoutManager = null;

    private int mStudioId = 0;

    private ImgTitleSubAdapter mAdapter = null;

    private List<VideoInfo> mDataList = new ArrayList<VideoInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        mStudioId = getIntent().getIntExtra(StudioBaseInfo.KEY_STUDIO_ID, 0);

        mRecyclerView = (RecyclerView)findViewById(R.id.video_recycler_view);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mAdapter = new ImgTitleSubAdapter(this, mDataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //TODO
        StudioOperator.getInstance().getVideoList(mStudioId, 0, new UserInfoOperator.OnGetListener<List<VideoInfo>>() {
            @Override
            public void onGet(boolean succeed, List<VideoInfo> videoInfos) {
                if (succeed) {
                    mDataList.clear();
                    mDataList.addAll(videoInfos);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_video);
    }
}
