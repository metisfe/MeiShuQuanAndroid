package com.metis.meishuquan.activity.info;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.commons.DataListAdapter;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.Item;

import java.util.List;

public class MyFavoritesActivity extends DataListActivity {

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
        UserInfoOperator.getInstance().getFavoriteList("100001", index, new UserInfoOperator.OnGetListener<List<Item>>() {
            @Override
            public void onGet(boolean succeed, List<Item> items) {
                Log.v(TAG, "loadData succeed=" + succeed + " items.size=" + items.size());
                if (succeed) {
                    if (mAdapter == null) {
                        mDataList = items;
                        mAdapter = new DataListAdapter(MyFavoritesActivity.this, mDataList);
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
                } else {
                    onLoadMoreComplete();
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        loadData(mIndex);
        Toast.makeText(this, "onLoadMore ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        mIndex = 1;
        loadData(mIndex);
    }
}
