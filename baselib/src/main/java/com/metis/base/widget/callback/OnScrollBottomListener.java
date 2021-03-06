package com.metis.base.widget.callback;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Beak on 2015/7/9.
 */
public abstract class OnScrollBottomListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        View lastChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        final int positionInAdapter = recyclerView.getChildAdapterPosition(lastChild);
        if (positionInAdapter == recyclerView.getAdapter().getItemCount() - 1 && newState == RecyclerView.SCROLL_STATE_IDLE) {
            onScrollBottom(recyclerView, newState);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    public abstract void onScrollBottom (RecyclerView recyclerView, int newState);

}
