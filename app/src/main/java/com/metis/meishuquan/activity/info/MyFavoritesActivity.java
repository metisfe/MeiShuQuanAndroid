package com.metis.meishuquan.activity.info;

import android.os.Bundle;
import android.view.View;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.commons.DataListAdapter;
import com.metis.meishuquan.fragment.Topline.ItemInfoFragment;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.Item;

import java.util.List;

public class MyFavoritesActivity extends DataListActivity implements DataListAdapter.OnItemClickListener{

    private static final String TAG = MyFavoritesActivity.class.getSimpleName();

    private DataListAdapter mAdapter = null;
    private List<Item> mDataList = null;
    private int mIndex = 1;

    @Override
    public String getTitleText() {
        return getString(R.string.my_info_collections);
    }

    @Override
    public void loadData(final int index) {
        UserInfoOperator.getInstance().getFavoriteList(MainApplication.userInfo.getUserId() + "", index, new UserInfoOperator.OnGetListener<List<Item>>() {
            @Override
            public void onGet(boolean succeed, List<Item> items) {
                if (succeed) {
                    if (mAdapter == null) {
                        mDataList = items;
                        mAdapter = new DataListAdapter(MyFavoritesActivity.this, mDataList);
                        mAdapter.setOnItemClickListener(MyFavoritesActivity.this);
                        setAdapter(mAdapter);
                    } else {
                        if (index == 1) {
                            mDataList.clear();
                        }
                        mDataList.addAll(items);
                        mAdapter.notifyDataSetChanged();
                    }
                    mIndex++;

                } else {

                }
                if (index == 1) {
                    onRefreshComplete();
                }
                onLoadMoreComplete();
            }
        });
    }

    @Override
    public void onLoadMore() {
        loadData(mIndex);
        //Toast.makeText(this, "onLoadMore ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        mIndex = 1;
        loadData(mIndex);
    }

    @Override
    public void onItemClick(View view, int position, Item item) {
        ItemInfoFragment fragment = new ItemInfoFragment();
        Bundle args = new Bundle();
        args.putInt("newsId", item.getId());
        fragment.setArguments(args);
        addFragment(fragment);
    }
}
