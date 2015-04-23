package com.metis.meishuquan.activity.info;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.DataListFragment;
import com.metis.meishuquan.view.shared.TitleView;

public abstract class DataListActivity extends BaseActivity implements DataListFragment.OnDragListViewListener{

    private TitleView mTitleView = null;

    private DataListFragment mDataListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        mDataListFragment = (DataListFragment)getSupportFragmentManager().findFragmentById(R.id.my_favorites_fragment);
        mDataListFragment.setOnDragListener(this);
    }

    @Override
    public String getTitleCenter() {
        return getTitleText();
    }

    public String getTitleText () {
        return getString(R.string.app_name).toString();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadData(1);
    }

    public abstract void loadData (int index);

    public void setAdapter (BaseAdapter adapter) {
        mDataListFragment.setAdapter(adapter);
    }

    public void notifyDataSetChanged () {
        mDataListFragment.notifyDataSetChanged();
    }

    public void onLoadMoreComplete () {
        mDataListFragment.onLoadMoreComplete();
    }

    public void onRefreshComplete () {
        mDataListFragment.onRefreshComplete();
    }
}
