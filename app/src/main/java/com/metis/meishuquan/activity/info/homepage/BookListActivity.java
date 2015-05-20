package com.metis.meishuquan.activity.info.homepage;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.adapter.ImgTitleSubAdapter;
import com.metis.meishuquan.model.BLL.BookInfo;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;

import java.util.List;

public class BookListActivity extends BaseActivity {

    private RecyclerView mRecyclerView = null;
    private ImgTitleSubAdapter mAdapter = null;
    private GridLayoutManager mGridLayoutManager = null;

    private int mStudioId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        mRecyclerView = (RecyclerView)findViewById(R.id.book_recycler_view);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mStudioId = getIntent().getIntExtra(StudioBaseInfo.KEY_STUDIO_ID, 0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //TODO
        StudioOperator.getInstance().getBookList(mStudioId, new UserInfoOperator.OnGetListener<List<BookInfo>>() {
            @Override
            public void onGet(boolean succeed, List<BookInfo> bookInfos) {
                if (succeed) {
                    mAdapter = new ImgTitleSubAdapter(BookListActivity.this, bookInfos);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_book_publish);
    }
}
