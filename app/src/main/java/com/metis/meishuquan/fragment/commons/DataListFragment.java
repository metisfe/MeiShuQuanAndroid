package com.metis.meishuquan.fragment.commons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.DragListView;

/**
 * Created by WJ on 2015/4/9.
 */
public class DataListFragment extends Fragment implements DragListView.OnLoadListener, DragListView.OnRefreshListener{

    private DragListView mDragListView = null;
    private OnDragListViewListener mDragListener = null;
    private BaseAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_list, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDragListView = (DragListView)view.findViewById(R.id.drag_list);
        mDragListView.setOnLoadListener(this);
        mDragListView.setOnRefreshListener(this);
    }

    public void setAdapter (BaseAdapter adapter) {
        mAdapter = adapter;
        mDragListView.setAdapter(mAdapter);
    }

    public void notifyDataSetChanged () {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onLoadMoreComplete () {
        mDragListView.onLoadComplete();
    }

    public void onRefreshComplete () {
        mDragListView.onRefreshComplete();
    }

    @Override
    public void onLoad() {
        if (mDragListener != null) {
            mDragListener.onLoadMore();
        }
    }

    @Override
    public void onRefresh() {
        if (mDragListener != null) {
            mDragListener.onRefresh();
        }
    }

    public void setOnDragListener (OnDragListViewListener listener) {
        mDragListener = listener;
    }

    public static interface OnDragListViewListener {
        public void onLoadMore ();
        public void onRefresh ();
    }
}
