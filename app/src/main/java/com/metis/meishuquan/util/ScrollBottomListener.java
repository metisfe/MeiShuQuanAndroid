package com.metis.meishuquan.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by WJ on 2015/6/9.
 */
public abstract class ScrollBottomListener extends RecyclerView.OnScrollListener {

    private static final String TAG = ScrollBottomListener.class.getSimpleName();

    private int mLastDy = 0;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new IllegalArgumentException("ScrollBottomListener only use for LinearLayoutManager");
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
        final int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
        final int count = linearLayoutManager.getItemCount();
        if (lastPosition >= count - 1 && mLastDy > 0 && newState == RecyclerView.SCROLL_STATE_IDLE) {
            onScrolledBottom();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mLastDy = dy;
    }

    public abstract void onScrolledBottom ();
}
